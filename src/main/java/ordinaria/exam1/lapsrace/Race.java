package ordinaria.exam1.lapsrace;

import ordinaria.exam1.barbershop.BarberShop;
import ordinaria.exam1.barbershop.Customer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Race  {
    private int numberLaps;
    private int currentLap;
    private List<Runner> runners;
    private int runnersWaiting;

    public Race(int numberLaps) {
        this.numberLaps = numberLaps;
        this.currentLap = 0;
        this.runnersWaiting = 0;
        this.runners = new ArrayList<>();
    }

    public void addRunner(Runner r){
        runners.add(r);
    }

    public int getLap() {
        return currentLap;
    }

    public boolean isFinished(){
        return currentLap >= numberLaps;
    }

    public void createRunners(int numberRunners){
        for (int i = 0; i < numberRunners; i++) {
            addRunner(new Runner(this, i));
        }
    }

    /**
     * Actualitza els punts del corredor segons la seva posició
     * @param r Corredor
     */
    public void updatePoints(Runner r){
        String message = "";
        if(runnersWaiting == 0) {
            message = String.format(" %d + 5 points", r.getPoints());
            r.addPoints(5);
        } else if (runnersWaiting == 1) {
            r.addPoints(2);
            message = String.format(" %d + 2 points", r.getPoints());
        } else if (runnersWaiting == 2) {
            r.addPoints(1);
            message = String.format(" %d + 1 point", r.getPoints());
        }
        System.out.printf("%s has finished lap %d in position %d.%s\n", r.getName(), currentLap, runnersWaiting, message);
    }

    /**
     * TODO
     * El corredor ha acabat la carrera.
     * - Si encara queden corredors en la carrera, s'espera a començar
     * - Si és l'últim en arribar, comença la següent etapa
     * @param r
     */
    public void waitForOtherRunners(Runner r){
        runnersWaiting++;
        updatePoints(r);

        if(runnersWaiting == runners.size()){
            runnersWaiting = 0;
            currentLap++;
            if (!isFinished())
                System.out.printf("===== LAP %d =====\n", currentLap);
            if (isFinished()){
                startRace();
            }

            // TODO: Comença la següent etapa
        } else {

            // TODO: Espera a començar la següent etapa
        }
    }

    /**
     * Comença la carrera i inicia tots els corredors
     */
    public void startRace(){
        // TODO: Inicia tots els corredors
        //runners.start();
        System.out.println("===== LAP 0 =====");

    }

    /**
     * Acaba la carrera i mostra la classificació
     */
    public void endRace() {
        // TODO: Espera a que tots els corredors acaben

        System.out.println("===== END =====");
        runners.sort(Comparator.comparing(Runner::getPoints).reversed());
        for (int i = 0; i < runners.size(); i++) {
            Runner r = runners.get(i);
            System.out.printf("%d. %s - %d points\n", i+1, r.getName(), r.getPoints());
        }
    }

    public static void main(String[] args) {
        Race race = new Race(10);
        race.createRunners(5);
        race.startRace();
        race.endRace();
    }
}
