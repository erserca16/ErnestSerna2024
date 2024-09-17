package ud4.practices.chat.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatHandler extends Thread {
    private final Socket socket;
    private final ChatServer server;
    private final PrintWriter out;
    private final BufferedReader in;

    private String nom;
    public ChatHandler(Socket socket, ChatServer server) throws IOException {
        this.socket = socket;
        this.server = server;

        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void sendMessage(String message){
        out.println(message);
    }

    @Override
    public void run(){
        try {
            String nom = in.readLine();
            setNom(nom);

            System.out.printf("%s s'ha identificat.\n", getNom());

            // Quan un client es desconecta, l'operació readLine() retorna null
            String message;
            while((message = in.readLine()) != null){
                String messageWithName = String.format("%s: %s", getNom(), message);
                System.out.println(messageWithName);
                server.sendMessage(this, messageWithName);
            }
            System.out.printf("%s has disconnected.\n", getNom());
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            server.removeClient(this);
        }
    }
}
