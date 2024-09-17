package exam1.sushi;

public class Sushi {
    public static void main(String[] args) {
        SushiRestaurant restaurant = new SushiRestaurant(10);
        Waiter w1 = new Waiter(5, restaurant);
        Waiter w2 = new Waiter(7, restaurant);

        restaurant.start();
        w1.start();
        w2.start();

        try {
            w1.join();
            w2.join();

            restaurant.interrupt();
            restaurant.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
