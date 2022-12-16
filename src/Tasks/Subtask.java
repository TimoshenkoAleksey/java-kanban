package Tasks;

public class Subtask extends Task {
    private int myEpicID;

    public Subtask(int taskID, int myEpicID, String taskName, String taskDescription) {
        super(taskID, taskName, taskDescription);
        this.myEpicID = myEpicID;
    }

    public int getMyEpicID() {
        return myEpicID;
    }

    public void setMyEpicID(int myEpicID) {
        this.myEpicID = myEpicID;
    }


}
