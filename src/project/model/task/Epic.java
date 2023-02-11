package project.model.task;

import java.util.ArrayList;

public class Epic extends Task {
    private Type type;

    private ArrayList<Integer> subtaskIds;

    public Epic(Status status, String taskName, String taskDescription) {
        super(status, taskName, taskDescription);
        subtaskIds = new ArrayList<>();
        type = Type.EPIC;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(int taskId) {
        this.subtaskIds.add(taskId);
    }

    @Override
    public String toString() {
        return getId() + "," + type + "," + getTaskName() + "," + getStatus() + "," + getTaskDescription() + "," + "\n";
    }
}
