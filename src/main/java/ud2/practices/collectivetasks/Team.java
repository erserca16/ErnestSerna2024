package ud2.practices.collectivetasks;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private List<EmployeeThread> employees;
    private List<Task> unfinishedTasks;
    private List<Task> testingTasks;
    private List<Task> finishedTasks;


    public Team(String name) {
        this.name = name;
        this.employees = new ArrayList<>();
        this.unfinishedTasks = new ArrayList<>();
        this.testingTasks = new ArrayList<>();
        this.finishedTasks = new ArrayList<>();
    }

    public void addEmployee(EmployeeThread employee) {
        this.employees.add(employee);
        employee.setTeam(this);
    }

    public void addTask(String taskName, int taskDuration) {
        unfinishedTasks.add(new Task(taskName, taskDuration));
    }

    public List<EmployeeThread> getEmployees() {
        return employees;
    }
    public String getName() {
        return name;
    }

    public synchronized Task getNextTask() {
        synchronized (unfinishedTasks) {
            if (!unfinishedTasks.isEmpty()) {
                Task nextTask = unfinishedTasks.remove(0);
                return nextTask;
            }
        }
        synchronized (testingTasks) {
            if (!testingTasks.isEmpty()) {
                Task nextTask = testingTasks.remove(0);
                return nextTask;
            }
        }
        return null;
    }

    public void startTeam() {
        for (EmployeeThread employee : employees) {
            Thread thread = new Thread(employee);
            thread.start();
        }
    }
    public void joinTeam() throws InterruptedException {
        for (EmployeeThread employee : employees) {
            employee.join();
        }
    }
    public void addToTestingTasks(Task task) {
        testingTasks.add(task);
    }
    public void addToFinishedTasks(Task task) {
        finishedTasks.add(task);
    }
    public static void startAllTeams(Team... teams) {
        for (Team team : teams) {
            System.out.printf("Starting team: %s\n", team.getName());
            team.startTeam();
        }
    }
    public static void joinAllTeams(Team... teams) {
        for (Team team : teams) {
            try {
                team.joinTeam();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private static boolean allTeamsFinished(Team... teams) {
        for (Team team : teams) {
            if (!team.allTeamsFinished()) {
                return false;
            }
        }
        return true;
    }


}
