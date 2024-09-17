package extraordinaria.models;

import java.io.Serializable;

public class Task implements Serializable {
    private final int duration;
    private final String name;
    boolean finished;

    public Task(String name, int duration) {
        this.name = name;
        this.duration = duration;
        finished = false;
    }

    public int getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "Task{" +
                "duration=" + duration +
                ", name='" + name + '\'' +
                '}';
    }
}
