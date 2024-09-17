package ud2.practices.swimmingpool;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.Semaphore;

public class SwimmingPool {
    private int poolCapacity;
    private int showersCapacity;

    // TODO: Add semaphores
    private Semaphore poolSemaphore;
    private Semaphore showerSemaphore;

    public SwimmingPool(int poolCapacity, int showersCapacity) {
        this.poolCapacity = poolCapacity;
        this.showersCapacity = showersCapacity;

        // TODO: Create semaphores
        this.poolSemaphore = new Semaphore(poolCapacity);
        this.showerSemaphore = new Semaphore(showersCapacity);
    }

    // TODO: create get() method for semaphores
    public Semaphore getPoolSemaphore() {
        return poolSemaphore;
    }

    public Semaphore getShowerSemaphore() {
        return showerSemaphore;
    }

    public static void main(String[] args) {
            SwimmingPool pool = new SwimmingPool(10, 3);
            String[] names = {
                    "Andrès", "Àngel", "Anna", "Carles", "Enric",
                    "Helena", "Isabel", "Joan", "Lorena", "Mar",
                    "Maria", "Marta", "Míriam", "Nicolàs", "Òscar",
                    "Paula", "Pere", "Teresa", "Toni", "Vicent"
            };
            List<PersonThread> persons = new ArrayList<>();
            for (String name : names) {
                // TODO: Create the threads and start them
                PersonThread person = new PersonThread(name, pool);
                person.start();
                persons.add(person);
            }

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (PersonThread person : persons) {
                person.interrupt();
            }

            for (PersonThread person : persons) {
                try {
                    person.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Tothom ha marxat de la piscina.");
        }
    }

