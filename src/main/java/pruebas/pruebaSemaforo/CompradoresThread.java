package pruebas.pruebaSemaforo;

public class CompradoresThread extends Thread {
    private WebTicket webTicket;

    public CompradoresThread(String name, WebTicket webTicket) {
        super(name);
        this.webTicket = webTicket;
    }
    @Override
    public void run() {
        try {
            int ticket = webTicket.comprarTickets();
            if (ticket > 0)
                System.out.printf("%s ha comprado la entrada %d\n", getName(), ticket);
            else
                System.out.printf("%s se ha quedado sin entrada\n", getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}