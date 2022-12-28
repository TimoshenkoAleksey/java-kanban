package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private HistoryManager historyManager;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private int counterId = 1;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    public void addTasks(int id, Task task) {
        tasks.put(id, task);
    }

    public void addEpics(int id, Epic epic) {
        epics.put(id, epic);
    }

    public void addSubtasks(int id, Subtask subtask) {
        subtasks.put(id, subtask);
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public int getNewId() {
        return counterId++;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList(subtasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    @Override
    public String getById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id).toString();
        }
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id).toString();
        }
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id).toString();
        }
        return null;
    }

    @Override
    public int createTask(Task task) {
        task.setId(getNewId());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        epic.setId(getNewId());
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int createSubTask(Subtask subtask) {
        subtask.setId(getNewId());
        epics.get(subtask.getEpicId()).setSubtaskIds(subtask.getId());
        epics.get(subtask.getEpicId()).setStatus(Status.IN_PROGRESS);
        subtasks.put(subtask.getId(), subtask);
        return subtask.getId();
    }

    @Override
    public void updateTask(int id, Task task, Status newStatus) {
        if (tasks.containsKey(id)) {
            tasks.replace(id, task);
            tasks.get(id).setStatus(newStatus);
        }
        if (subtasks.containsKey(id)) {
            subtasks.replace(id, (Subtask) task);
            subtasks.get(id).setStatus(newStatus);
            for (Integer subtask : epics.get(subtasks.get(id).getEpicId()).getSubtaskIds()) {
                if (subtasks.get(subtask).getStatus() == Status.NEW
                        || subtasks.get(subtask).getStatus() == Status.IN_PROGRESS) {
                    return;
                }
            }
            epics.get(subtasks.get(id).getEpicId()).setStatus(Status.DONE);
        }
    }

    @Override
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
                if (epics.get(subtaskId).getStatus() == Status.NEW
                        || epics.get(subtaskId).getStatus() == Status.IN_PROGRESS) {
                    return;
                }
            }
            epics.get(subtasks.get(id).getEpicId()).setStatus(Status.DONE);
        }
    }

    @Override
    public ArrayList<String> getAllSubtasksByEpic(int epicId) {
        ArrayList<String> allSubtasksByEpic = new ArrayList<>();
        for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {
            allSubtasksByEpic.add(subtasks.get(subtaskId).toString());
        }
        return allSubtasksByEpic;
    }
}
