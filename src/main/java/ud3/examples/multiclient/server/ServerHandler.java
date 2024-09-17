package ud3.examples.multiclient.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Classe que gestiona la comunicació del servidor
 * amb un únic client en un fil d'execució independent.
 */
public class ServerHandler extends Thread {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    private String nom;

    public ServerHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        nom = null;
    }

    public ServerHandler(Socket socket, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void close() throws IOException {
        socket.close();
    }
    /**
     * Fil d'execució independent per cada client.
     * <p>
     * Abans que res, el client s'identifica amb un nom.
     * Després, el servidor mostra els missatges que cada client ha enviat.
     */
    @Override
    public void run() {
        try {
            setNom(in.readLine());
            System.out.printf("%s s'ha identificat.\n", getNom());

            // Quan un client es desconecta, l'operació readLine() retorna null
            String message;
            while((message = in.readLine()) != null){
                System.out.printf("%s: %s\n", getNom(), message);
            }
            System.out.printf("%s has disconnected.\n", getNom());
            close();
        } catch (IOException e) {
            System.err.println("Error while handling client.");
            System.err.println(e.getMessage());
        }
    }
}

