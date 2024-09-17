package extraordinaria.client;

import extraordinaria.models.Task;

/**
 * Classe que s'encarrega de realitzar les tasques
 */
public class TaskConsumer extends Thread {

    /** Aplicació client principal */
    private final TaskManagerClient client;

    /**
     * Inicialitza el Consumer
     * @param client Aplicació client principal
     */
    public TaskConsumer(TaskManagerClient client) {
        this.client = client;
    }

    /**
     * TODO: Executa la tasca proporcionada
     * @param t Tasca a executar
     */
    private void executeTask(Task t) throws InterruptedException {
        System.out.printf("%s: Starting task %s...\n", client.getName(), t.getName());

        // TODO: Espera la duració de la tasca
        t.setFinished(true);
try{
    Thread.sleep(1000);
} catch (InterruptedException e){
    e.printStackTrace();
}
        System.out.printf("%s: Finished task %s (%d).\n", client.getName(), t.getName(), t.getDuration());
    }

    /**
     * Execució principal del fil Consumer.
     * Mentre l'apliació estiga en funcionament, intenta rebre la següent tasca
     * i l'executa.
     */
    @Override
    public void run(){
        try{
            while (client.isWorking()){
                Task t = client.getNextTask();
                executeTask(t);
            }
        } catch (InterruptedException e){
            System.out.println("TaskConsumer has been interrupted.");
        }
    }
}
