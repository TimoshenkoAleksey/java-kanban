package project.service;

import project.model.task.Epic;
import project.model.task.Status;
import project.model.task.Subtask;
import project.model.task.Task;

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
