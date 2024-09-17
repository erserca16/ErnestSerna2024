package ud2.practices.collectivetasks;

public class EmployeeThread extends Thread {

    private Team team;

    public EmployeeThread(String name) {
        super(name);
        this.team = null;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (team) {
                Task task = team.getNextTask();

                if (task != null) {
                    if (task.getStatus() == TaskStatus.UNFINISHED) {
                        System.out.printf("%s: Starting task %s...\n", this.getName(), task.getName());
                        task.work();
                        team.addToTestingTasks(task);
                    }
                    else if (task.getStatus() == TaskStatus.TESTING) {
                        System.out.printf("%s: Starting task %s...\n", this.getName(), task.getName());
                        task.test();
                        team.addToFinishedTasks(task);
                    }
                } else {
                    System.out.printf("%s: Ha realitzat totes les tasques assignades.\n", this.getName());
                    break;
                }
            }
        }
    }
}