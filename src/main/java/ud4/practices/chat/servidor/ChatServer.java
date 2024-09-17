package ud4.practices.chat.servidor;

import ud4.examples.Config;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import static ud4.examples.CertificateUtils.getCertificateInfo;
import static ud4.examples.CertificateUtils.loadKeyStore;

public class ChatServer extends Thread {
    private final Properties config;
    private final ServerSocket server;

    private final List<ChatHandler> clients;
    boolean running;

    public ChatServer() throws IOException {
        /*
             keytool -genkey -keyalg RSA \
            -alias messaging-server \
            -keystore files/prueba/messaging-keystore.jks \
            -storepass 123456 -keysize 2048 \
            -dname "CN=MessagingServer, OU=PSP-DAM2S, O=CIPFP Mislata, L=Mislata, ST=València, C=ES"
         */

        config = Config.getConfig("application.properties");
        int port = Integer.parseInt(config.getProperty("ud4.practices.chat.port"));
        System.out.println("Creant el Socket servidor en el port: " + port);

        String keyStorePath = config.getProperty("ud4.practices.chat.keystore.path");
        String keyStorePassword = config.getProperty("ud4.practices.chat.keystore.passwd");

        System.setProperty("javax.net.ssl.keyStore", keyStorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);

        SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        server = sslserversocketfactory.createServerSocket(port);
        // server = new ServerSocket(port);
        clients = new ArrayList<>();
        running = true;

        printCertificateInfo();
    }

    private void printCertificateInfo(){
        String keyStorePath = config.getProperty("ud4.practices.chat.keystore.path");
        String keyStorePassword = config.getProperty("ud4.practices.chat.keystore.passwd");
        String certificateAlias = config.getProperty("ud4.practices.chat.keystore.alias");

        try {
            KeyStore keyStore = loadKeyStore(keyStorePath, keyStorePassword);
            Certificate c = keyStore.getCertificate(certificateAlias);
            String info = getCertificateInfo(c);
            System.out.println(info.replace(",", "\n"));
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(){
        running = false;
        this.interrupt();
    }

    public synchronized void removeClient(ChatHandler client){
        clients.remove(client);
    }

    public void sendMessage(ChatHandler sender, String msg){
        for(ChatHandler client : clients) {
            if (client != sender)
                client.sendMessage(msg);
        }
    }

    @Override
    public void run() {
        while (running){
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

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            ChatServer server = new ChatServer();
            server.start();

            scanner.nextLine();

            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}