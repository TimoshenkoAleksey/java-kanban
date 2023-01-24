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
    public Task getById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        }
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
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
        subtasks.put(subtask.getId(), subtask);
        checkEpicStatus(subtask.getEpicId());
        return subtask.getId();
    }

    @Override
    public void updateTask(Integer id, Task task, Status newStatus) {
        if (tasks.containsKey(id)) {
            tasks.replace(id, task);
            tasks.get(id).setStatus(newStatus);
            tasks.get(id).setId(id);
        }
        if (subtasks.containsKey(id)) {
            subtasks.replace(id, (Subtask) task);
            subtasks.get(id).setStatus(newStatus);
            subtasks.get(id).setId(id);
            checkEpicStatus(subtasks.get(id).getEpicId());
        }
    }

    @Override
    public void deleteOneTask(Integer id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            tasks.remove(id);
        }
        if (epics.containsKey(id)) {
            for (Integer subtaskId : epics.get(id).getSubtaskIds()) {
                historyManager.remove(subtaskId);
                subtasks.remove(subtaskId);
            }
            historyManager.remove(id);
            epics.remove(id);
        }
        if (subtasks.containsKey(id)) {
            Epic epic = epics.get(subtasks.get(id).getEpicId());
            historyManager.remove(id);
            subtasks.remove(id);
            epic.getSubtaskIds().remove(id);
            checkEpicStatus(epic.getId());
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

    @Override
    public void checkEpicStatus(int id) {
        Epic epic = epics.get(id);
        boolean isDone = true;
        if (epic.getSubtaskIds().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        for (Integer subtaskId : epic.getSubtaskIds()) {
            Status status = subtasks.get(subtaskId).getStatus();
            if (status.equals(Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (status.equals(Status.NEW)) {
                isDone = false;
            }
        }
        if (isDone)
            epic.setStatus(Status.DONE);
        else
            epic.setStatus(Status.NEW);
    }
}
