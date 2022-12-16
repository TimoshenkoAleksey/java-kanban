package Tasks;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> mySubtasks;

    public Epic(int taskID, String taskName, String taskDescription) {
        super(taskID, taskName, taskDescription);
        mySubtasks = new ArrayList<>();
    }

    public ArrayList<Integer> getMySubtasks() {
        return mySubtasks;
    }

    public void setMySubtasks(int taskId) {

        this.mySubtasks.add(taskId);
    }


}
