package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;

    public Epic(int taskId, String taskName, String taskDescription) {
        super(taskId, taskName, taskDescription);
        subtaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(int taskId) {
        this.subtaskIds.add(taskId);
    }
}
