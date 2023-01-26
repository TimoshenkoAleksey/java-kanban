package project.model.task;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int epicId, String taskName, String taskDescription) {
        super(taskName, taskDescription);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
