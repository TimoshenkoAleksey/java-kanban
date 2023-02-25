package project.model.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private Type type;
    private LocalDateTime endTime;

    private ArrayList<Integer> subtaskIds;

    public Epic(Status status, String taskName, String taskDescription, long duration, LocalDateTime startTime) {
        super(status, taskName, taskDescription, duration, startTime);
        subtaskIds = new ArrayList<>();
        type = Type.EPIC;
        endTime = null;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(int taskId) {
        this.subtaskIds.add(taskId);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return getId() + "," + type + "," + getTaskName() + "," + getStatus() + "," + getTaskDescription() + ","
                + getDuration() + "," + getStartTime() + "\n";
    }
}
