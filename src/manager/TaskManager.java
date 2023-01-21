package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    void deleteAllTasks();

    Task getById(int id);

    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubTask(Subtask subtask);

    void updateTask(Integer id, Task task, Status newStatus);

    void deleteOneTask(Integer id);

    ArrayList<String> getAllSubtasksByEpic(int epicId);

    void checkEpicStatus(int id);
}
