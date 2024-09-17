package ud2.practices.collectivetasks;

public class Task {
    private int duration;
    private String name;
    private TaskStatus status;

    public Task(String name, int duration) {
        this.name = name;
        this.duration = duration;
        status = TaskStatus.UNFINISHED;
    }

    public int getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public TaskStatus status() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public synchronized void work() {
        Thread current = Thread.currentThread();

        // TODO: Hacer la tarea (dormir DURATION milisegundos)
        try {
            Thread.sleep(this.duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setStatus(TaskStatus.TESTING);
        System.out.printf("%s: Finished task %s (%d ms).\n", current.getName(), this.getName(), this.getDuration());
    }

    public synchronized void test() {
        Thread current = Thread.currentThread();

        // TODO: Hacer la tarea (dormir DURATION / 2 milisegundos)
        try {
            Thread.sleep(this.duration / 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setStatus(TaskStatus.FINISHED);
        System.out.printf("%s: Finished testing task %s (%d ms).\n", current.getName(), this.getName(), this.getDuration() / 2);
    }
    public TaskStatus getStatus() {
        return status;
    }
}