package pruebas.pruebaSemaforo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class WebTicket {
    private int totalTickets;
    private int ticketsRestantes;
    private Semaphore colaSemaforo;

    public WebTicket(int totalTickets) {
        this.totalTickets = totalTickets;
        this.ticketsRestantes = totalTickets;
        this.colaSemaforo = new Semaphore(2);
    }
    public synchronized int obtenerNextTicket(){
        int nextTicket = ticketsRestantes;
        ticketsRestantes--;
        return nextTicket;

    }
    public int comprarTickets() throws InterruptedException {
        System.out.printf("%s está en la cola\n", Thread.currentThread().getName());
        colaSemaforo.acquire();
        System.out.printf("%s está comprando la entrada \n", Thread.currentThread().getName());
        int segundos = ThreadLocalRandom.current().nextInt(1000,5000);
        Thread.sleep(segundos);
        int ticket = obtenerNextTicket();
        System.out.printf("%s ha comprado la entrada \n" , Thread.currentThread().getName());
        colaSemaforo.release();;
        System.out.printf("%s ha acabado la compra\n",Thread.currentThread().getName());

        return ticket;
    }
}
