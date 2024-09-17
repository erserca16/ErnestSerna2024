package ud3.practices.lyrics.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe que gestiona la comunicació del servidor
 * amb un únic client en un fil d'execució independent.
 */
public class HandleClientLyrics extends Thread {
    private final Socket client;
    private final BufferedReader in;
    private final PrintWriter out;
    private final List<String> lyrics;

    public HandleClientLyrics(Socket client, LyricsServer server) throws IOException {
        this.client = client;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
        this.lyrics = server.getLyrics();
    }

    public void close() throws IOException {
        client.close();
    }

    /**
     * Aquest mètode processa peticions del tipus "GET i"
     * @param request Petició
     * @return Retorna un int amb valor "i" si la petició és correcta; -1 en altre cas.
     */
    public int processRequest(String request){
        int i = -1;
        /* Simple way to do it
        if(request.startsWith("GET "))
            i = Integer.parseInt(request.split(" ")[1]);
         */
        Pattern p = Pattern.compile("GET (\\d+)");
        Matcher m = p.matcher(request);
        if(m.matches()) {
            i = Integer.parseInt(m.group(1));
        }
        return i;
    }

    /**
     * Fil d'execució independent per cada client.
     * <p>
     * El servidor espera missatges del tipus "GET i"
     * i retorna la linia especificada.
     */
    @Override
    public void run() {
        try {
            // Quan un client es desconecta, l'operació readLine() retorna null
            String message;
            while((message = in.readLine()) != null){
                int i = processRequest(message);
                System.out.println(message);
                if (i >= 0 && i < lyrics.size()){
                    out.println(lyrics.get(i));
                    System.out.println(lyrics.get(i));
                } else {
                    out.println("ERROR: La línia solicitada no existeix.");
                    System.out.println("ERROR: La línia solicitada no existeix.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error while handling client.");
            System.err.println(e.getMessage());
        }
    }
}