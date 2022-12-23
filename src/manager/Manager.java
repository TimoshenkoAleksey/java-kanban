package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private int counterId = 1;

    public Manager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void addTasks(int id, Task task) {
        tasks.put(id, task);
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void addEpics(int id, Epic epic) {
        epics.put(id, epic);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtasks(int id, Subtask subtask) {
        subtasks.put(id, subtask);
    }


    public int getNewId() {
        return counterId++;
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList(subtasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public String getById(int id) {
        if (tasks.containsKey(id))
            return tasks.get(id).toString();
        if (epics.containsKey(id))
            return epics.get(id).toString();
        if (subtasks.containsKey(id))
            return subtasks.get(id).toString();
        return null;
    }

    public int createTask(Task task) {
        task.setId(counterId);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public int createEpic(Epic epic) {
        epic.setId(counterId);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public int createSubTask(Subtask subtask) {
        subtask.setId(counterId);
        epics.get(subtask.getEpicId()).setSubtaskIds(subtask.getId());
        epics.get(subtask.getEpicId()).setStatus("IN_PROGRESS");
        subtasks.put(subtask.getId(), subtask);
        return subtask.getId();
    }

    public void updateTask(int id, Task task, String newStatus) {
        if (tasks.containsKey(id)) {
            tasks.replace(id, task);
            tasks.get(id).setStatus(newStatus);
        }
        if (subtasks.containsKey(id)) {
            subtasks.replace(id, (Subtask) task);
            subtasks.get(id).setStatus(newStatus);
            for (Integer subtask : epics.get(subtasks.get(id).getEpicId()).getSubtaskIds()) {
                if (subtasks.get(subtask).getStatus() == "NEW"
                        || subtasks.get(subtask).getStatus() == "IN_PROGRESS") {
                    return;
                }
            }
            epics.get(subtasks.get(id).getEpicId()).setStatus("DONE");
        }
    }

    public void deleteOneTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
        if (epics.containsKey(id)) {
            for (Integer subtaskId : epics.get(id).getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
        if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).getEpicId()).getSubtaskIds().remove(subtasks.get(id));
            subtasks.remove(id);
            for (Integer subtaskId : epics.get(subtasks.get(id).getEpicId()).getSubtaskIds()) {
                if (epics.get(subtaskId).getStatus() == "NEW"
                        || epics.get(subtaskId).getStatus() == "IN_PROGRESS") {
                    return;
                }
            }
            epics.get(subtasks.get(id).getEpicId()).setStatus("DONE");
        }
    }

    public ArrayList<String> getAllSubtasksByEpic(int epicId) {
        ArrayList<String> allSubtasksByEpic = new ArrayList<>();
        for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {
            allSubtasksByEpic.add(subtasks.get(subtaskId).toString());
        }
        return allSubtasksByEpic;
    }
}
