package exam1.concert;

import java.util.ArrayList;
import java.util.List;

public class BuyConcertTickets {
    public static void main(String[] args) {
        String[] names = {
                "Andrès", "Àngel", "Anna", "Carles", "Enric",
                "Helena", "Isabel", "Joan", "Lorena", "Mar",
                "Maria", "Marta", "Míriam", "Nicolàs", "Òscar",
                "Paula", "Pere", "Teresa", "Toni", "Vicent"
        };
        TicketWebsite website = new TicketWebsite(10);
        List<BuyerThread> buyers = new ArrayList<>();
        for (String name : names){
            BuyerThread b = new BuyerThread(name, website);
            buyers.add(b);
            b.start();
        }

        for(BuyerThread b : buyers) {
            try {
                b.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
