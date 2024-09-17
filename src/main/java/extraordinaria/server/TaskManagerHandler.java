package extraordinaria.server;

import extraordinaria.client.TaskLoader;
import extraordinaria.models.Request;
import extraordinaria.models.RequestType;
import extraordinaria.models.Task;

import java.io.*;
import java.net.Socket;

/**
 * Classe que s'encarrega de gestionar la comunicació amb el client.
 * TODO: Creació dels objectes necessaris per l'enviament i recepció d'objectes
 * TODO: Implementar els mètodes per l'enviament i recepció d'objectes
 */
public class TaskManagerHandler extends Thread {
    /** Servidor */
    private final TaskManagerServer server;

    /** TODO: Objectes per l'enviament i recepció d'objectes */
    private final ObjectOutputStream objOut;
    private final ObjectInputStream objIn;

    private String name;

    public TaskManagerHandler(Socket socket, TaskManagerServer server) throws IOException {
        name = null;
        this.server = server;
        objOut = new ObjectOutputStream(socket.getOutputStream());
        objIn = new ObjectInputStream(socket.getInputStream());
        // TODO: Objectes per l'enviament i recepció d'objectes */
    }

    /**
     * TODO: Envia una petició al client
     * @param request Petició a enviar
     */
    public void sendRequest(Request request) throws IOException, ClassNotFoundException {
        System.out.printf("<-- : %s\n", request);
        Request req;
        while ((req = (Request)objOut.writeObject();) !=null){
            if(req.getType() == RequestType.SUCCESS){
                TaskLoader task = (TaskLoader) req.getObject();
                server.addTask(name, 1000);
            }
        }
        throw new RuntimeException("sendRequest() not yet implemented.");
    }

    /**
     * TODO: Reb una petició del client
     * @return Petició rebuda
     */
    public Request readRequest() throws IOException, ClassNotFoundException {
        Request request = null;
        Request req;
        System.out.printf("--> : %s\n", request);
        while ((req = (Request)objIn.readObject()) !=null){
            if(req.getType() == RequestType.SUCCESS){
                TaskLoader task = (TaskLoader) req.getObject();
                server.addTask(name, 1000);
            }
        throw new RuntimeException("readRequest() not yet implemented.");
    }

    /**
     * Execució principal del Handler.
     * Fins que l'usuari no es desconnecta, gestiona les peticions rebudes
     */
    @Override
    public void run() {
        try {
            Request request;
            while((request = readRequest()) != null) {
                if (request.getType() == RequestType.NEXT_TASK){
                    nextTask(request);
                }
                else if (request.getType() == RequestType.IDENTIFY){
                    identify(request);
                }
                else if (request.getType() == RequestType.DISCONNECT){
                    System.out.printf("%s s'ha desconnectat\n", name);
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException ignored) {
        } finally {
            server.removeClient(this);
        }
    }

    /**
     * TODO: Protocol NEXT_TASK en la part del servidor.
     * El servidor busca una tasca.
     * - Si hi ha, envia SUCCESS amb la tasca
     * - Si no hi ha, envia ERROR amb un missatge
     * @param request Petició amb la qual s'ha llançat aquest protocol
     */
    private void nextTask(Request request) throws IOException {
        Task t = server.nextTask();
        if (t == null){
            System.out.printf("No queden tasques per a %s\n", name);
        } else {
            System.out.printf("Tasca %s enviada a %s\n", t, name);
        }
    }

    /**
     * TODO: Protocol IDENTIFY en la part del servidor.
     * El servidor s'identifica amb el nom rebut des de la petició.
     * El servidor contesta al client amb SUCCESS.
     * @param request Petició amb la qual s'ha llançat aquest protocol
     */
    private void identify(Request request) throws IOException {
        name = null;
        setName(name);

        String missatge = String.format("S'ha identificat correctament a %s", name);
        System.out.println(missatge);
    }

}
