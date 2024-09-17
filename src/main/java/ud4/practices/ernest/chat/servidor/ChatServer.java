package ud4.practices.ernest.chat.servidor;

import ud4.examples.jssecinema.CertificateUtils;
import ud4.examples.jssecinema.Config;
import ud4.examples.jssecinema.RSA;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

public class ChatServer extends Thread {


    ServerSocket server;

    List<ChatHandler> clients;
    boolean running;

    public ChatServer(int port) throws IOException {
        server = new ServerSocket(port);
        clients = new ArrayList<>();
        running = true;
    }

    public void close() {
        running = false;
        this.interrupt();
    }

    public synchronized void removeClient(ChatHandler client) {
        clients.remove(client);
    }

    public void sendMessage(ChatHandler sender, String msg) {
        for (ChatHandler client : clients) {
            if (client != sender)
                client.sendMessage(msg);
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket client = server.accept();
                ChatHandler handler = new ChatHandler(client, this);
                clients.add(handler);
                handler.start();
                System.out.println("Nova connexió acceptada.");
            } catch (IOException e) {
                System.err.println("Error while accepting new connection");
                System.err.println(e.getMessage());
            }
        }
    }


    public static KeyStore loadKeyStore(String ksFile, String ksPwd) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore ks = KeyStore.getInstance("JKS");
        File f = new File(ksFile);
        if (f.isFile()) {
            FileInputStream in = new FileInputStream(f);
            ks.load(in, ksPwd.toCharArray());
        }
        return ks;
    }

    public static void printCertificateInfo(Certificate certificate) {
        X509Certificate cert = (X509Certificate) certificate;
        String info = cert.getSubjectX500Principal().getName();
        System.out.println(info);
    }

    public static String signText(PrivateKey privateKey, String text) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(text.getBytes());
        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    public static boolean verifySignature(PublicKey publicKey, String text, String signed) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(text.getBytes());

        byte[] signatureBytes = Base64.getDecoder().decode(signed);

        return signature.verify(signatureBytes);
    }

    public static void main(String[] args) {
        try {
            Properties config = Config.getConfig("config.properties");
            int port = Integer.parseInt(config.getProperty("ud4.practices.chat.port"));
            System.out.println("Creant el Socket servidor en el port: " + port);

            System.setProperty("javax.net.ssl.keyStore", "files/ud4/chat/chat-server_keystore.jdk");
            System.setProperty("javax.net.ssl.keyStorePassword", config.getProperty("ud4.practices.chat.keystore.passwd"));

            Scanner scanner = new Scanner(System.in);
            ChatServer server = new ChatServer(port);
            server.start();

            scanner.nextLine();

            server.close();
            KeyStore keyStore = CertificateUtils.loadKeyStore("files/ud4/chat/chat-server.jdk", config.getProperty("ud4.practices.chat.keystore.passwd"));
            String[] aliases = {"alias1", "alias2", "alias3"};
            for (String alias : aliases) {
                Certificate certificate = keyStore.getCertificate(alias);
                CertificateUtils.printCertificateInfo(certificate);

                PublicKey publicKey = certificate.getPublicKey();

                PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, config.getProperty("ud4.practices.chat.keystore.passwd").toCharArray());

                String message = "Este es un mensaje secreto.";
                System.out.printf("Mensaje: %s\n", message);

                String encrypted = RSA.encrypt(publicKey, message);
                System.out.printf("Encriptado: %s\n", encrypted);

                String decrypted = RSA.decrypt(privateKey, encrypted);
                System.out.printf("Desencriptado: %s\n", decrypted);

                String signature = CertificateUtils.signText(privateKey, message);
                System.out.printf("Firmado: %s\n", signature);

                System.out.println("Verificación de firma: " + CertificateUtils.verifySignature(publicKey, message, signature));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
