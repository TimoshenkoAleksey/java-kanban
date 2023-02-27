package project.model.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private Type type;
    private LocalDateTime endTime;
    private List<Subtask> subtaskIds;

    public Epic(Status status, String taskName, String taskDescription, long duration, LocalDateTime startTime) {
        super(status, taskName, taskDescription, duration, startTime);
        subtaskIds = new ArrayList<>();
        type = Type.EPIC;
        endTime = null;
    }

    public List<Subtask> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(Subtask subtask) {
        this.subtaskIds.add(subtask);
    }

    public void deleteOneSubtask(Subtask subtask) {
        this.subtaskIds.remove(subtask);
    }

    public void checkEpicStartAndEndTime() {
        if (getSubtaskIds().isEmpty()) {
            setDuration(0);
            setStartTime(null);
            endTime = null;
            return;
        }
        long epicDuration = 0;
        LocalDateTime epicStartTime = LocalDateTime.MAX;
        LocalDateTime epicEndTime = LocalDateTime.MIN;
        for (Subtask subtask : getSubtaskIds()) {
            epicDuration += subtask.getDuration();
            if (epicStartTime.isAfter(subtask.getStartTime())) {
                epicStartTime = subtask.getStartTime();
            }
            if (epicEndTime.isBefore(subtask.getEndTime())) {
                epicEndTime = subtask.getEndTime();
            }
        }
        setDuration(epicDuration);
        setStartTime(epicStartTime);
        endTime = epicEndTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return getId() + "," + type + "," + getTaskName() + "," + getStatus() + "," + getTaskDescription() + ","
                + getDuration() + "," + getStartTime() + "\n";
    }
}
