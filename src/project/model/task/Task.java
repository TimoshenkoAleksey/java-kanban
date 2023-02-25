package project.model.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private int id;
    private Status status;
    private String taskName;
    private String taskDescription;
    private Type type;
    private long duration;
    private LocalDateTime startTime;

    public Task(Status status, String taskName, String taskDescription, long duration, LocalDateTime startTime) {
        this.status = status;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        type = Type.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    @Override
    public String toString() {
        return id + "," + type + "," + taskName + "," + status + "," + taskDescription + "," + duration + ","
                + startTime + "\n";
    }
}
