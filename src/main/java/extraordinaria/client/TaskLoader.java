package extraordinaria.client;

import extraordinaria.models.Request;
import extraordinaria.models.RequestType;
import ud4.examples.Config;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.Properties;

/**
 * Classe que s'encarrega de comunicar-se amb el servidor
 * per demanar-li tasques a realitzar
 */
public class TaskLoader extends Thread {

    /** Aplicació client principal */
    private final TaskManagerClient client;

    /** Sócol pel qual es comunica amb el servidor */
    private final Socket socket;

    /** TODO: Objectes per l'enviament i recepció d'objectes */
    private final PrintWriter out;
    private final BufferedReader in;
    private final ObjectOutputStream objOut;
    private final ObjectInputStream objIn;

    /* keytool -genkey -keyalg RSA \
            -alias taskmanager-server \
            -keystore files/extraordinaria/taskmanager-server_keystore.jks \
            -storepass 123456 -keysize 2048 \
            -dname "CN=TaskManagerServer2023, OU=DAM2S-PSP, O=CIPFP Mislata, L=Mislata, ST=València, C=ES" */


    /*
    keytool -export -alias taskmanager-server \
    -keystore files/extraordinaria/taskmanager-server_keystore.jks \
    -file files/extraordinaria/taskmanager-server.crt \
    -storepass 123456
     */
    /*
    keytool -import -alias taskmanager-server \
    -keystore files/extraordinaria/taskmanager-server_truststore.jks \
    -file files/extraordinaria/taskmanager-server.crt \
    -storepass 123456

     */
    public TaskLoader(TaskManagerClient client, String host, int port) throws IOException {
        this.client = client;
        objOut = new ObjectOutputStream(client.getOutputStream());
        objIn = new ObjectInputStream(client.getInputStream());

        // TODO: Connectar-se mitjançant JSSE
        Properties config = Config.getConfig("application.properties");
        host = config.getProperty("extraordinaria.host");
        port = Integer.parseInt(config.getProperty("extraordinaria.port"));

        System.setProperty("javax.net.ssl.keyStore", "files/extraordinaria/taskmanager-server_keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", config.getProperty("extraordinaria.keystore.passwd"));

        //this.socket = null; //aqui hacer Socket socket = new Socket(host, port); y comentar el private socket

        // TODO: Objectes per l'enviament i recepció d'objectes */
        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        this.socket = sslsocketfactory.createSocket(host, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * TODO: Envia una petició al servidor
     * @param request Petició a enviar
     */
    public void sendRequest(Request request) throws IOException {
        throw new RuntimeException("sendRequest() not yet implemented.");
        Request req;
        while ((req = (Request)objOut.writeObject()) !=null){
            if(req.getType() == RequestType.SUCCESS){
                TaskLoader task = (TaskLoader) req.getObject();
                client.addTask(task);
            }
    }

    /**
     * TODO: Reb una petició del servidor
     * @return Petició rebuda
     */
    public Request readRequest() throws IOException, ClassNotFoundException {
        Request req;
            System.out.printf("--> : %s\n", request);
            while ((req = (Request)objIn.readObject()) !=null){
                if(req.getType() == RequestType.SUCCESS){
                    TaskLoader task = (TaskLoader) req.getObject();
                    client.addTask();
        }
        throw new RuntimeException("readRequest() not yet implemented.");
    }

    /**
     * Tanca la connexió amb el servidor
     */
    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * TODO: Protocol IDENTIFY a la part del client.
     * Envia una petició amb el nom a establir.
     * - Si contesta SUCCESS, mostra el missatge i retorna true
     * - Si contesta ERROR, mostra el missatge i retorna false
     * @return true si s'ha identificat correctament; false en altre cas
     */
    public boolean identify(String name) {
        try {
        } catch (IOException | ClassNotFoundException ignored) {
        }
        return false;
    }

    /**
     * TODO: Protocol NEXT_TASK a la part del client.
     * Envia una petició per rebbre una nova tasca.
     * - Si contesta SUCCESS, afegeix la tasca a l'aplicació principal
     * - Si contesta ERROR, envia una petició DISCONNECT i tanca la connexió
     */
    public void nextTask() throws IOException, ClassNotFoundException, InterruptedException {
        Request response = null;

        if (response.getType() == RequestType.SUCCESS){

        } else {

            socket.close();
            client.close();
        }
    }

    /**
     * Execució principal del fil.
     * Mentre l'aplicació està executant-se, intenta demanar noves tasques al servidor.
     */
    @Override
    public void run(){
        try {
            while (client.isWorking()){
                client.waitWhileQueueIsFull();

                nextTask();
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
