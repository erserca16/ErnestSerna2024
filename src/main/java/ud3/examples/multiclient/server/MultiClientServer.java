package ud3.examples.multiclient.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MultiClientServer extends Thread {
    private final ServerSocket server;
    private final List<ServerHandler> clients;
    private boolean running;

    /**
     * Crea un servidor ServerSocket a partir del port.
     *
     * @param port Port on escoltarà el servidor
     * @throws IOException Excepcions del constructor ServerSocket
     */
    public MultiClientServer(int port) throws IOException {
        server = new ServerSocket(port);
        clients = new ArrayList<>();
        running = true;
    }

    /**
     * Para l'execució del servidor i totes les gestions dels clients
     */
    public void close(){
        running = false;
        for (ServerHandler client : clients) {
            try {
                client.close();
            } catch (IOException ignored) {
            }
            client.interrupt();
        }
        this.interrupt();
    }

    /**
     * Fil d'execució del servidor.
     * <p>
     * El servidor escolta el port i espera noves connexions.
     * Quan una nou client es connecta, es crea un objecte ServerHandler,
     * que gestionarà la comunicació amb aquest client en un fil distint.
     * <p>
     * D'aquesta manera, el servidor pot continuar escoltant i esperant
     * noves connexions mentres cada fil gestiona la comunicació
     * amb cada client.
     */
    @Override
    public void run() {
        while (running){
            try {
                Socket client = server.accept();
                System.out.println("Nou client acceptat.");
                ServerHandler handler = new ServerHandler(client);
                clients.add(handler);
                handler.start();
            } catch (IOException e) {
                System.err.println("Error while accepting new connection");
                System.err.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            MultiClientServer server = new MultiClientServer(1234);
            server.start();

            scanner.nextLine();

            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}