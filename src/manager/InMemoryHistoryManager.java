package manager;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    public static final int HISTORY_SIZE = 10;

    private ArrayList<Task> taskViewHistory;

    public InMemoryHistoryManager() {
        taskViewHistory = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (taskViewHistory.size() < HISTORY_SIZE) {
            taskViewHistory.add(task);
        } else {
            taskViewHistory.remove(0);
            taskViewHistory.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return taskViewHistory;
    }
}
