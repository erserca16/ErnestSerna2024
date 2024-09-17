package ordinaria.exam2.handshake.server;

import ordinaria.exam2.handshake.exceptions.CryptographyException;
import ordinaria.exam2.handshake.exceptions.HandshakeException;
import ud4.examples.CertificateUtils;
import ud4.examples.Config;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Properties;

public class HandshakeServerHandler extends Thread {
    private final HandshakeServer server;
    private final Socket socket;

    private final BufferedReader in;
    private final PrintWriter out;

    private SecretKey symetricKey;

    public HandshakeServerHandler(Socket socket, HandshakeServer server) throws IOException {
        this.server = server;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        symetricKey = null;
    }

    /**
     * TODO: Retorna la clau privada del certificat del servidor
     * @return Clau publica del servidor
     */
    private PrivateKey getPrivateKey() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {

        Properties config = ud4.examples.jssecinema.Config.getConfig("application.properties");
        int port = Integer.parseInt(config.getProperty("ud4.examples.jsse.port"));
        System.out.println("Creant el Socket servidor en el port: " + port);

        System.setProperty("javax.net.ssl.keyStore", "files/ud4/server_exampleJSSE_keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", config.getProperty("ud4.examples.jsse.keystore.passwd"));

        System.out.println("Creant el Socket servidor en el port: " + port);
        //String keyStorePassword = "123456";
        String alias = config.getProperty("ordinaria.exam2.keystore.alias");

        // TODO: Obté la clau privada de la KeyStore amb el alies especificat
        PrivateKey privateKey = getPrivateKey(); //TODO
        return privateKey;
    }
    /**
     * TODO: Realitza el handshake.
     * - Llegeix la contrasenya encriptada des del client
     * - Desencripta la contrasenya amb la clau privada del servidor
     * - Genera una clau simètrica utilitzant la contrasenya amb tamany 256 bits
     * - Envia la resposta "Handshake successful!"
     * @throws IOException
     */
    private void handshake() throws HandshakeException {
        try {
            // Llegeix la contrasenya encriptada
            String encryptedKeyPassword = in.readLine();

            // TODO: Desencripta la contrasenya amb la clau privada
            PrivateKey privateKey = getPrivateKey();
            String keyPasswd = "";

            // TODO: Genera una clau simètrica utilitzant la contrasenya amb tamany 256 bits
            this.symetricKey = null;

            // Envia la resposta
            sendEncryptedMessage("Handshake successful!");
            System.out.println("Handshake successful!");
        }  catch (UnrecoverableKeyException | NoSuchPaddingException | CertificateException |
                  IllegalBlockSizeException | IOException | KeyStoreException | NoSuchAlgorithmException |
                  BadPaddingException | InvalidKeyException e) {
            throw new HandshakeException(e.getMessage());
        }
    }

    /**
     * TODO: Encripta el missatge utilitzant la clau simètrica i envia'l al servidor
     * @param message Missatge a enviar
     */
    private void sendEncryptedMessage(String message) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (symetricKey == null)
            throw new RuntimeException("Symetric key not initialized");

        // TODO: Encripta el missatge amb la clau simètrica
        String encrypted = ""; // TODO

        // Envia el missatge
        out.println(encrypted);
    }

    /**
     * TODO: Llig un missatge del servidor encriptat i el desencripta amb la clau simètrica
     * @return Missatge rebut
     */
    private String readEncryptedMessage() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (symetricKey == null)
            throw new RuntimeException("Symetric key not initialized");

        // Llig el missatge
        String response = in.readLine();

        // Si la resposta és null, retornem null
        if(response == null)
            return null;

        // TODO: Desencripta'l mitjançant la clau simètrica
        return "";
    }

    private void echo() throws CryptographyException, IOException {
        try{
            String request = null;
            while((request = readEncryptedMessage()) != null){
                sendEncryptedMessage(request);
            }
        } catch (NoSuchPaddingException | IllegalBlockSizeException | IOException | NoSuchAlgorithmException |
                 BadPaddingException | InvalidKeyException e) {
            throw new CryptographyException(e.getMessage());
        }
        this.socket.close();
    }

    @Override
    public void run() {
        try {
            handshake();
            echo();
        } catch (HandshakeException e) {
            System.err.println("Error en el handshake: " + e.getMessage());
        } catch (CryptographyException e) {
            System.err.println("Error xifrant o desxifrant les dades: " + e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        server.removeClient(this);
    }
}
