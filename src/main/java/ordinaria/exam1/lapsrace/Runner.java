package ordinaria.exam1.lapsrace;

public class Runner {
    private final int id;
    private int points;
    private final Race race;

    public Runner(Race race, int id) {
        this.race = race;
        this.id = id;
        this.points = 0;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public String getName(){
        return "Runner" + id;
    }

    public void run() {
        while(!race.isFinished()){
            try {
                System.out.printf("%s is running lap %d\n", this.getName(), race.getLap());
                Thread.sleep((long) (Math.random() * 1000));
                race.waitForOtherRunners(this);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
