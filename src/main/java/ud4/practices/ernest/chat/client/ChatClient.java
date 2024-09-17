package ud4.practices.ernest.chat.client;

import ud4.examples.jssecinema.Config;

import javax.net.ssl.SSLSocket;
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
        try {
            Properties config = Config.getConfig("config.properties");
            String host = config.getProperty("ud4.practices.chat.host");
            int port = Integer.parseInt(config.getProperty("ud4.practices.chat.port"));
            String keyStorePassword = config.getProperty("ud4.practices.chat.keystore.passwd");

            System.out.println("Creant el Socket client.");
            System.setProperty("javax.net.ssl.trustStore", "files/ud4/chat/chat-server_cliente.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", keyStorePassword);

            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            Socket socket = sslsocketfactory.createSocket(host, port);
            this.socket = (SSLSocket) sslsocketfactory.createSocket(host, port);

            this.scanner = new Scanner(System.in);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.listener = new ChatListener(socket, this);
        } catch (Exception e) {
            throw new IOException("Error de SSL connection", e);
        }

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