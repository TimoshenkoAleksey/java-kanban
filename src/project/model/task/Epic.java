package project.model.task;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        subtaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(int taskId) {
        this.subtaskIds.add(taskId);
    }
}
