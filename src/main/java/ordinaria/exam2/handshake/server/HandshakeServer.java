package ordinaria.exam2.handshake.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HandshakeServer extends Thread {
    ServerSocket server;

    List<HandshakeServerHandler> clients;
    boolean running;

    public HandshakeServer(int port) throws IOException {
        server = new ServerSocket(port);
        clients = new ArrayList<>();
        running = true;
    }

    public void close(){
        running = false;
        this.interrupt();
    }

    public synchronized void removeClient(HandshakeServerHandler hc){
        clients.remove(hc);
    }

    @Override
    public void run() {
        while (running){
            try {
                Socket client = server.accept();
                HandshakeServerHandler handshakeServerHandler = new HandshakeServerHandler(client, this);
                clients.add(handshakeServerHandler);
                handshakeServerHandler.start();
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
            HandshakeServer server = new HandshakeServer(1234);
            server.start();

            scanner.nextLine();

            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
