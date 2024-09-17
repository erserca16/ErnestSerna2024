package exam2.client;

import exam2.models.Request;
import exam2.models.RequestType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessagingClientListener extends Thread {

    private final MessagingClient client;
    private final Socket socket;
    private final ObjectInputStream objIn;
    private final ObjectOutputStream objOut;

    // TODO: ObjectStream related object

    public MessagingClientListener(Socket socket, MessagingClient client) throws IOException {
        this.client = client;
        this.socket = socket;
        this.objOut = new ObjectOutputStream(socket.getOutputStream());
        this.objIn = new ObjectInputStream(socket.getInputStream());
        // TODO: ObjectStream related object
    }

    /**
     * TODO: Llegeix una petició
     * @return Petició llegida
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Request readRequest() throws IOException, ClassNotFoundException {
        return null;
    }

    // TODO: Reb missatges
    @Override
    public void run() {
        try {
            Request request;
            while((request = readRequest()) != null){
                System.out.println(request);
                if (request.getType() == RequestType.SEND){
                    // TODO: Acció del client a les respostes del tipus SEND
                    String resposta = objIn.readLine();
                }
                else if (request.getType() == RequestType.SUCCESS){
                    // TODO: Acció del client a les respostes del tipus SUCCESS
                }
                else if (request.getType() == RequestType.ERROR){
                    // TODO: Acció del client a les respostes del tipus ERROR

                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
