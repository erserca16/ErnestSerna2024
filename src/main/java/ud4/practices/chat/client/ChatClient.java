package ud4.practices.chat.client;

import ud4.examples.Config;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class ChatClient {
    private final Socket socket;
    private final ChatListener listener;
    private final Scanner scanner;
    private final PrintWriter out;

    public ChatClient() throws IOException {
        Properties config = Config.getConfig("application.properties");
        String host = config.getProperty("ud4.practices.chat.host");
        int port = Integer.parseInt(config.getProperty("ud4.practices.chat.port"));

        System.setProperty("javax.net.ssl.keyStore", "files/ud4/chat/chat-client.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", config.getProperty("ud4.practices.chat.keystore.passwd"));

        /*
        keytool -export -alias chat-server \
        -keystore files/prueba/chat/chat-server.jks \
        -file files/prueba/chat/chat-server.crt \
        -storepass 123456

         keytool -import -alias chat-server \
        -keystore files/ud4/chat/chat-client.jks \
        -file files/ud4/chat/chat-server.crt \
        -storepass 123456
         */

        this.scanner = new Scanner(System.in);

        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        this.socket = sslsocketfactory.createSocket(host, port);
        // this.socket = new Socket(host, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.listener = new ChatListener(socket, this);
    }

    public void identify(){
        System.out.print("Introdueix el teu nom: ");
        String line = scanner.nextLine();
        out.println(line);
        listener.start();
    }

    public void chat() throws IOException {
        System.out.println("Acabes d'entrar al chat.");
        System.out.println("Per exir, escriu \"/exit\".");
        String line;
        while(!(line = scanner.nextLine()).equals("/exit") && this.socket.isConnected()){
            out.println(line);
        }
        close();
    }

    public void close(){
        try {
            socket.close();
            listener.interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Connectant-se amb el servidor...");
        try {
            ChatClient chat = new ChatClient();
            chat.identify();
            chat.chat();
        } catch (IOException e){
            System.err.println("Error connectant-se amb el servidor.");
        }
    }
}