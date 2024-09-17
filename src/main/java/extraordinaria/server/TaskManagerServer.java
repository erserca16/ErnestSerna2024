package extraordinaria.server;

import extraordinaria.models.Task;
import ud4.examples.jssecinema.Config;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Classe principal del servidor.
 * S'encarrega de accpetar noves connexions de manera simultània
 * i de guardar les tasques disponibles per desenvolupar.
 * TODO: Implementar els mecanismes pertinents per evitar conflictes en l'accés a recursos compartits
 */
public class TaskManagerServer {
    private final Properties config;
    /** Servidor */
    private final ServerSocket server;

    /** Llista de tasques pendents a realitzar */
    private final List<Task> tasks;

    /** Llista de TaskManagerHandler per cada client connectat */
    private final List<TaskManagerHandler> handlers;
    
    /** Indicador si el servidor està en execució */
    boolean running;

    /**
     * Crea un servidor a partir del port especificat.
     * @param port Port on escoltarà el servidor
     */
    public TaskManagerServer(int port) throws IOException {
        // TODO: Inicialitzar el servidor utilitzant SSLSockets
        config = Config.getConfig("application.properties");
        port = Integer.parseInt(config.getProperty("extraordinaria.port"));
        System.out.println("Creando el Socket del servidor en el puerto:" + port);

        String keyStorePath = config.getProperty("extraordinaria.keystore.path");
        String keyStorePassword = config.getProperty("extraordinaria.keystore.password");

        System.setProperty("javax.net.ssl.keyStore", keyStorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);

        SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        server = sslserversocketfactory.createServerSocket(port);
        // TODO: Mostrar informació del certificat del servidor

        handlers = new ArrayList<>();
        tasks = new ArrayList<>();
        running = true;
    }

    public void close(){
        running = false;
    }

    /**
     * Afegeix una tasca a la llista de tasques pendents
     * @param name Nom de la tasca
     * @param duration Durada de la tasca
     */
    public void addTask(String name, int duration){
        tasks.add(new Task(name, duration));
    }

    /**
     * Retorna la següent tasca a desenvolupar.
     * @return Si queda alguna tasca pendent, la retorna; null en altre cas.
     */
    public Task nextTask(){
        if (tasks.size() == 0)
            return null;
        return tasks.remove(0);
    }

    /**
     * Elimina el Handler del client especificat.
     * @param hc Handler del client a eliminar.
     */
    public void removeClient(TaskManagerHandler hc){
        handlers.remove(hc);
    }

    /**
     * Execució del servidor.
     * Escolta en el port especificat a noves connexions.
     * Per cada connexió, crea un Handler.
     */
    public void run() {
        while (running){
            try {
                Socket client = server.accept();
                System.out.println("Nova connexió acceptada.");
                TaskManagerHandler handleClient = new TaskManagerHandler(client, this);
                handlers.add(handleClient);
                handleClient.start();
            } catch (IOException e) {
                System.err.println("Error while accepting new connection");
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Main del servidor. Crea el servidor amb unes tasques inicials i l'inícia.
     */
    public static void main(String[] args) {
        try {
            TaskManagerServer server = new TaskManagerServer(1234);
            server.addTask("Main page design", 2500);
            server.addTask("Shopping cart navigation bar", 5500);
            server.addTask("Breadcrumb", 1500);
            server.addTask("Profile page", 7500);
            server.addTask("Connect API to database", 3000);
            server.addTask("Shopping API models", 4200);
            server.addTask("API authentication", 2100);
            server.addTask("Testing API", 5500);
            server.addTask("Relation Model", 5000);
            server.addTask("Define models in the database", 4200);
            server.addTask("Install DBMS", 3000);
            server.addTask("Views", 2800);

            server.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
