package ordinaria.exam1.barbershop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class BarberShop {

    // TODO: Crea els mecanismes necessaris per gestionar les cues
    private int seients;
    private int tall;
    private Semaphore barberSemaphore;
    private Semaphore seientSemaphore;

    public BarberShop(int seients, int tall) {
        this.seients = seients;
        this.tall = tall;
        this.barberSemaphore = new Semaphore(seients);
        this.seientSemaphore = new Semaphore(tall);
        // TODO: Inicialitza els mecanismes necessaris per gestionar les cues
    }

    // TODO: Crea mètodes per accedir als mecanismes de sincronització


    public Semaphore getBarberSemaphore() {
        return barberSemaphore;
    }

    public Semaphore getSeientSemaphore() {
        return seientSemaphore;
    }

    public static void main(String[] args) {
        BarberShop barberShop = new BarberShop(3,1);
        List<Customer> customers = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            customers.add(new Customer(barberShop, i));
            Customer customer = new Customer(barberShop,i);
            customer.start();
            customers.add(customer);
        }

        // TODO: Inicia tots els clients
      try {
          Thread.sleep(1000);
      } catch (InterruptedException e) {
          throw new RuntimeException(e);
      }
      for (Customer customer : customers){
          try {
              customer.join();
          } catch (InterruptedException e) {
              throw new RuntimeException(e);
          }
      }


      }

        // TODO: Espera a que tots els clients acaben
    }

