package extraordinaria.client;

import extraordinaria.models.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * Classe principal del client.
 * S'encarrega de guardar i gestionar la cua de tasques obtingudes per a aquest client
 * TODO: Implementar els mecanismes pertinents per evitar conflictes en l'accés a recursos compartits
 * TODO: Implementa la sincronització de fils per a la gestió de la cua
 */
public class TaskManagerClient {
    /** Capacitat màxima de la cua de tasques */
    private static final int MAX_CAPACITY = 5;

    /** Scanner que permet interactuar amb l'usuari */
    private final Scanner scanner;

    /** Nom d'usuari */
    private String name;

    /** Tasques pendents */
    private final List<Task> pendingTasks;

    /** Fil que s'encarrega de rebre tasques del servidor */
    private final TaskLoader taskLoader;

    /** Fil que s'encarrega de realitzar les tasques pendents */
    private final TaskConsumer taskConsumer;

    /** Indica si el client està en execució */
    private boolean working;
    private Semaphore semaphore;

    /**
     * Crea una instància del client.
     * @param host Host del servidor
     * @param port Port on escolta el servidor
     */
    public TaskManagerClient(String host, int port) throws IOException {
        this.scanner = new Scanner(System.in);
        pendingTasks = new ArrayList<>();

        taskLoader = new TaskLoader(this, host, port);
        taskConsumer = new TaskConsumer(this);

        working = false;
        name = null;
    }

    /**
     * Indica si el client està en execució
     * @return true si el client està en execució; false en altre cas
     */
    public boolean isWorking(){
        return working;
    }

    /**
     * Retorna el nom de l'usuari
     * @return El nom de l'usuari si s'ha identificat; null en altre cas
     */
    public String getName(){
        return name;
    }

    /**
     * Inicia el client
     */
    public void start() throws IOException {
        boolean identified = identify();
        if (identified){
            System.out.printf("%s s'ha identifact correctament.\n", name);
            // TODO: Inicia els fils Loader i Consumer
            taskLoader.start();
            taskConsumer.start();
         //   int n = scanner.nextInt();
            /*for (int i = 0; i < MAX_CAPACITY; i++) {
                TaskLoader taskLoader = new TaskLoader(client);
                taskLoader.start();
            }*/

            working = true;
        } else {
            System.err.println("Error al identificar-se.");
        }
    }

    /**
     * Espeara per acabar.
     */
    public void end() {
        try {
taskConsumer.join();;
taskLoader.join();
taskConsumer.interrupt();
taskLoader.interrupt();
        } catch (InterruptedException e){
            System.out.println("TaskManagerClient interrupted while waiting Loader & Consumer to finish.");
        }
    }

    /**
     * Tanca el client.
     */
    public void close(){
        working = false;
        taskConsumer.interrupt();
    }

    /**
     * Identifica al client
     * @return true si el client s'ha identificat correctament; false en altre cas
     */
    public boolean identify(){
        System.out.println("Introdueix el teu nom: ");
        this.name = scanner.nextLine();
        return taskLoader.identify(name);
    }

    /**
     * Indica si la cua de tasques està plena
     * @return true si la cua de tasques està plena; false en altre cas
     */
    public boolean isFull(){
        return pendingTasks.size() >= MAX_CAPACITY;
    }

    /**
     * TODO: Espera mentre la cua estiga plena
     */
    public void waitWhileQueueIsFull() throws InterruptedException {
        while (taskConsumer == MAX_CAPACITY) wait();


    }

    /**
     * Retorna la següent tasca de la cua.
     * TODO: Aquest mètode ha d'esperar mentre la cua estiga buida
     * @return Següent tasca de la cua
     */
    public Task getNextTask() throws InterruptedException {
        if(taskLoader)wait();
        return pendingTasks.remove(0);
    }

    /**
     * Afig una tasca a la cua de tasques pendents.
     * TODO: Aquest mètode ha d'esperar mentre la cua estiga plena
     */
    public void addTask(Task t) throws InterruptedException {
        pendingTasks.add(t);
    }

    /**
     * Main del client.
     * Inicia el client i espera a acabar.
     */
    public static void main(String[] args) {
        System.out.println("Connectant-se amb el servidor...");
        try {
            TaskManagerClient taskManagerClient = new TaskManagerClient("localhost", 1234);
            taskManagerClient.start();
            taskManagerClient.end();
        } catch (IOException e){
            System.err.println("Error connectant-se amb el servidor.");
        }
    }
}
