package project.model.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

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

    public Type getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public void setType(Type type) {
        this.type = type;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return getId() == task.getId() && getDuration() == task.getDuration() && getStatus() == task.getStatus() && Objects.equals(getTaskName(), task.getTaskName()) && Objects.equals(getTaskDescription(), task.getTaskDescription()) && getType() == task.getType() && Objects.equals(getStartTime(), task.getStartTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStatus(), getTaskName(), getTaskDescription(), getType(), getDuration(), getStartTime());
    }

    @Override
    public String toString() {
        return id + "," + type + "," + taskName + "," + status + "," + taskDescription + "," + duration + ","
                + startTime + "\n";
    }


}
