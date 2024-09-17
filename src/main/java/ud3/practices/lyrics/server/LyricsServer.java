package ud3.practices.lyrics.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LyricsServer {
    private final ServerSocket server;
    private final List<HandleClientLyrics> clients;
    private boolean running;

    private final ArrayList<String> lyrics;

    /**
     * Crea un servidor ServerSocket a partir del port.
     *
     * @param port Port on escoltarà el servidor
     * @throws IOException Excepcions del constructor ServerSocket
     */
    public LyricsServer(int port, String lyricsFilePath) throws IOException {
        server = new ServerSocket(port);
        clients = new ArrayList<>();
        running = true;
        lyrics = new ArrayList<>();
        loadLyrics(lyricsFilePath);
    }

    public void loadLyrics(String lyricsFilePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(lyricsFilePath));
        String line;
        while ((line = br.readLine()) != null) {
            this.lyrics.add(line);
        }
    }

    public ArrayList<String> getLyrics(){
        return lyrics;
    }

    /**
     * Para l'execució del servidor i totes les gestions dels clients
     */
    public void close(){
        running = false;
        for (HandleClientLyrics client : clients) {
            try {
                client.close();
            } catch (IOException ignored) {
            }
            client.interrupt();
        }
    }

    /**
     * Fil d'execució del servidor.
     * <p>
     * El servidor escolta el port i espera noves connexions.
     * Quan una nou client es connecta, es crea un objecte HandleClient,
     * que gestionarà la comunicació amb aquest client en un fil distint.
     * <p>
     * D'aquesta manera, el servidor pot continuar escoltant i esperant
     * noves connexions mentres cada fil gestiona la comunicació
     * amb cada client.
     */
    public void run() {
        while (running){
            try {
                Socket client = server.accept();
                System.out.println("Nou client acceptat.");
                HandleClientLyrics handleClientLyrics = new HandleClientLyrics(client, this);
                clients.add(handleClientLyrics);
                handleClientLyrics.start();
            } catch (IOException e) {
                System.err.println("Error while accepting new connection");
                System.err.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            LyricsServer server = new LyricsServer(1234,"files/ud2/lyrics.txt");
            server.run();
        } catch (IOException e) {
            System.err.println("Error iniciant el servidor.");
            System.err.println(e.getMessage());
        }
    }

}