package project.model.task;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;
    private Type type;

    public Subtask(Status status, int epicId, String taskName, String taskDescription,
                   long duration, LocalDateTime startTime) {
        super(status, taskName, taskDescription, duration, startTime);
        this.epicId = epicId;
        type = Type.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return getId() + "," + type + "," + getTaskName() + "," + getStatus() + "," + getTaskDescription() + ","
                + getDuration() + "," + getStartTime() + "," + epicId + "\n";
    }
}
