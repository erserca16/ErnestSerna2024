package exam2.client;

import exam2.models.Request;
import exam2.models.RequestType;
import ud4.examples.Config;


import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class MessagingClient {
    private final Socket socket;
    private final MessagingClientListener listener;
    private final Scanner scanner;
    private final PrintWriter out;
    private final ObjectInputStream objIn;
    private final ObjectOutputStream objOut;

    // TODO: ObjectStream related object

    public MessagingClient() throws IOException {
/*
        keytool  -export -alias messaging-server \
        -keystore files/exam2/messaging-keystore.jks \
        -file files/exam2/messaging-keystore.crt \
        -storepass 123456
 */
/*
        keytool -import -alias messaging-server \
        -keystore files/exam2/messaging-truststore.jks \
        -file files/exam2/messaging-keystore.crt \
        -storepass 123456
*/
        Properties config = Config.getConfig("application.properties");
        String host = config.getProperty("exam2.host");
        int port = Integer.parseInt(config.getProperty("exam2.port"));

        System.setProperty("javax.net.ssl.keyStore", "files/exam2/messaging-truststore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", config.getProperty("exam2.keystore.passwd"));
        this.scanner = new Scanner(System.in);
        // TODO: Connectar-se mitjançant JSSE
        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        this.socket = sslsocketfactory.createSocket(host, port);
        // TODO: Object stream related objects

        this.out= new PrintWriter(socket.getOutputStream(), true);

        this.listener = new MessagingClientListener(socket, this);
        this.listener.start();
        this.objOut = new ObjectOutputStream(socket.getOutputStream());
        this.objIn = new ObjectInputStream(socket.getInputStream());

    }

    /**
     * TODO: Envia una petició
     * @param request Petició a enviar
     * @throws IOException
     */
    public void sendRequest(Request request) throws IOException, ClassNotFoundException {
        Request req = new Request(RequestType.SEND, request.getMessage());
        objOut.writeObject(req);

        Request response = (Request) objIn.readObject();

        if(response.getType() == RequestType.ERROR)
            System.err.printf("ERROR: %s\n", response.getMessage());
        else if(response.getType() == RequestType.SUCCESS)
            System.out.printf("%s\n", response.getMessage());
    }

    /**
     * TODO: Demana a l'usuari el seu àlies i s'identifica amb el servidor
     * @throws IOException
     */
    public void identify() throws IOException {
        System.out.print("Introdueix el teu nom: ");
        String line = scanner.nextLine();


    }

    public void chat() throws IOException, ClassNotFoundException {
        System.out.println("Acabes d'entrar al chat.");
        System.out.println("Per exir, escriu \"/exit\".");
        String action;
        while((action = scanner.next()) != null && this.socket.isConnected()){
            if (action.equals("/exit")){
                this.close();
                return;
            }else if (action.equals("/list")) {
                // Send a request to the server to get the list
                Request request = new Request(RequestType.LIST); // Assuming there's a LIST type in RequestType
                sendRequest(request); // Use your existing method to send this request

                // Assuming the server responds with a Request object containing the list as its message
                // and that the response is handled elsewhere, either in this method or by the listener


        } else if (action.equals("/change")) {
                System.out.print("Introdueix el nou àlies: "); // Prompt the user for a new alias
                scanner.nextLine(); // Consume any leftover newline
                String newAlias = scanner.nextLine(); // Read the new alias

                // Create a request object for changing the alias
                Request request = new Request(RequestType.CHANGE_NAME, newAlias);

                // Send the request to the server
                sendRequest(request); // Assuming sendRequest handles the serialization and sending of Request objects

        } else if (action.equals("/msg")) {
                System.out.print("Enter recipient: ");
                String recipient = scanner.next(); // Read the recipient
                System.out.print("Enter message: ");
                scanner.nextLine(); // Consume the leftover newline
                String message = scanner.nextLine(); // Read the full message
                Request request = new Request(RequestType.SEND, message, recipient); // Assuming Request has a constructor for messages
                sendRequest(request);

        } else {
                System.out.printf("Ordre \"%s\" no trobada\n", action);
                scanner.nextLine();
            }
        }
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
            MessagingClient chat = new MessagingClient();
            chat.identify();
            chat.chat();
        } catch (IOException e){
            System.err.println("Error connectant-se amb el servidor.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
