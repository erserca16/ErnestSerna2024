package ordinaria.exam1.barbershop;

public class Customer extends Thread{
    private BarberShop shop;
    private int id;

    public Customer(BarberShop shop, int id) {
        this.shop = shop;
        this.id = id;
    }

    // TODO: Utilitza els mecanismes de sincronització
    public void run(){
        try {
            System.out.printf("El client %d està esperant...\n", id);
            shop.getBarberSemaphore().acquire();
            System.out.printf("El client %d està esperant en la sala d'espera\n", id);
            shop.getSeientSemaphore().acquire();
            System.out.printf("El client %d s'està tallant el monyo...\n", id);
            Thread.sleep(2000);
            System.out.printf("El client %d ja ha acabat!n", id);
            shop.getSeientSemaphore().release();
        } catch (InterruptedException ignored) {}
    }
}
