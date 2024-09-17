package pruebas.pruebaSemaforo;


import java.util.ArrayList;
import java.util.List;

public class CompraTickets {
    public static void main(String[] args) {
        String[] nombres = {
                "Ana", "Andres", "Ernest", "Miriam"
        };
        WebTicket website = new WebTicket(2);
        List<CompradoresThread> compradores = new ArrayList<>();
        for (String name : nombres) {
            CompradoresThread c = new CompradoresThread(name, website);
            compradores.add(c);
            c.start();

        }
        for(CompradoresThread c : compradores){
            try {
                c.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
