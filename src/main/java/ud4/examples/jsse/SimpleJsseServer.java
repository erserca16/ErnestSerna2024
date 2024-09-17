package ud4.examples.jsse;

import ud4.examples.jssecinema.Config;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class SimpleJsseServer {

    public static void main(String[] args) {
        try {
            Properties config = Config.getConfig("application.properties");
            int port = Integer.parseInt(config.getProperty("ud4.examples.jsse.port"));
            System.out.println("Creant el Socket servidor en el port: " + port);

            System.setProperty("javax.net.ssl.keyStore", "files/ud4/jsse/server_exampleJSSE_keystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", config.getProperty("ud4.examples.jsse.keystore.passwd"));

            SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            ServerSocket server = sslserversocketfactory.createServerSocket(port);
            // ServerSocket server = new ServerSocket(port);

            System.out.println("Esperant connexions...");
            // Aquest Socket es de tipus SSLSocket
            Socket connexio = server.accept();
            System.out.println("Connectat amb el client!");

            BufferedReader in = new BufferedReader(new InputStreamReader(connexio.getInputStream()));
            // Activem l'opció autoFlush
            PrintWriter out = new PrintWriter(connexio.getOutputStream(), true);

            System.out.println("Esperant missatge des del client...");
            String missatge = in.readLine();
            System.out.println("Sha rebut el missatge:");
            System.out.println(missatge);

            String resposta = "Rebut!";
            System.out.println("S'ha enviat el missatge: " + resposta);
            out.println(resposta);

            System.out.println("Tancant el servidor...");
            connexio.close();
            server.close();
            System.out.println("Tancat.");
        } catch (ConnectException e) {
            System.err.println("Connection refused!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}