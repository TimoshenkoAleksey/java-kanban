package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {
    HashMap<Integer, Epic> getEpics();

    int getNewId();

    HistoryManager getHistoryManager();

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    void deleteAllTasks();

    String getById(int id);

    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubTask(Subtask subtask);

    void updateTask(int id, Task task, Status newStatus);

    void deleteOneTask(int id);

    ArrayList<String> getAllSubtasksByEpic(int epicId);
}
