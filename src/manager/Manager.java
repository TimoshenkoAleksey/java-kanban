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

    public void setTasks(int taskId, Task task) {
        tasks.put(taskId, task);
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void setEpics(int taskId, Epic epic) {
        epics.put(taskId, epic);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(int taskId, Subtask subtask) {
        subtasks.put(taskId, subtask);
    }

    public int getCounterId() {
        return counterId;
    }

    public void setCounterId() {
        counterId++;
    }

    public ArrayList<String> getAllTasks() {
        ArrayList<String> allTasks = new ArrayList<>();
        if (!tasks.isEmpty()) {
            for (Task value : tasks.values()) {
                allTasks.add(value.toString());
            }
        }
        return allTasks;
    }

    public ArrayList<String> getAllEpics() {
        ArrayList<String> allEpics = new ArrayList<>();
        if (!epics.isEmpty()) {
            for (Epic value : epics.values()) {
                allEpics.add(value.toString());
            }
        }
        return allEpics;
    }

    public ArrayList<String> getAllSubtasks() {
        ArrayList<String> allSubtasks = new ArrayList<>();
        if (!subtasks.isEmpty()) {
            for (Subtask value : subtasks.values()) {
                allSubtasks.add(value.toString());
            }
        }
        return allSubtasks;
    }

    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public String getById(int taskId) {
        if (tasks.containsKey(taskId))
            return tasks.get(taskId).toString();
        if (epics.containsKey(taskId))
            return epics.get(taskId).toString();
        if (subtasks.containsKey(taskId))
            return subtasks.get(taskId).toString();
        return null;
    }

    public int createTask(Task task) {
        task.setTaskId(counterId++);
        tasks.put(task.getTaskId(), task);
        return task.getTaskId();
    }

    public int createEpic(Epic epic) {
        epic.setTaskId(counterId++);
        epics.put(epic.getTaskId(), epic);
        return epic.getTaskId();
    }

    public int createSubTask(Subtask subtask) {
        subtask.setTaskId(counterId++);
        epics.get(subtask.getEpicId()).setSubtaskIds(subtask.getTaskId());
        epics.get(subtask.getEpicId()).setStatus("IN_PROGRESS");
        subtasks.put(subtask.getTaskId(), subtask);
        return subtask.getTaskId();
    }

    public void updateTask(int taskId, Object task, String newStatus) {
        if (tasks.containsKey(taskId)) {
            tasks.replace(taskId, (Task) task);
            tasks.get(taskId).setStatus(newStatus);
        }
        if (subtasks.containsKey(taskId)) {
            subtasks.replace(taskId, (Subtask) task);
            subtasks.get(taskId).setStatus(newStatus);
            for (Integer subtask : epics.get(subtasks.get(taskId).getEpicId()).getSubtaskIds()) {
                if (subtasks.get(subtask).getStatus() == "NEW"
                        || subtasks.get(subtask).getStatus() == "IN_PROGRESS") {
                    return;
                }
            }
            epics.get(subtasks.get(taskId).getEpicId()).setStatus("DONE");
        }
    }

    public void deleteOneTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        }
        if (epics.containsKey(taskId)) {
            for (Integer subtaskId : epics.get(taskId).getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(taskId);
        }
        if (subtasks.containsKey(taskId)) {
            epics.get(subtasks.get(taskId).getEpicId()).getSubtaskIds().remove(subtasks.get(taskId));
            subtasks.remove(taskId);
            for (Integer subtaskId : epics.get(subtasks.get(taskId).getEpicId()).getSubtaskIds()) {
                if (epics.get(subtaskId).getStatus() == "NEW"
                        || epics.get(subtaskId).getStatus() == "IN_PROGRESS") {
                    return;
                }
            }
            epics.get(subtasks.get(taskId).getEpicId()).setStatus("DONE");
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
