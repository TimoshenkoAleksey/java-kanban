package project.model.task;

public class Task {
    private int id;
    private Status status;
    private String taskName;
    private String taskDescription;
    private Type type;


    public Task(Status status, String taskName, String taskDescription) {
        this.status = status;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        type = Type.TASK;
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

    @Override
    public String toString() {
        return id + "," + type + "," + taskName + "," + status + "," + taskDescription + "\n";
    }
}
