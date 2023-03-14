package project.service;

import project.model.task.Epic;
import project.model.task.Status;
import project.model.task.Subtask;
import project.model.task.Task;

import java.time.LocalDateTime;
import java.util.*;

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
    public List<Task> getAllTasks() {
        return new ArrayList(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
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
        if (task != null) {
            if (isNotIntersection(task)) {
                task.setId(getNewId());
                addTasks(task.getId(), task);
                return task.getId();
            }
        }
        return -1;
    }

    @Override
    public int createEpic(Epic epic) {
        if (epic != null) {
            epic.setId(getNewId());
            addEpics(epic.getId(), epic);
            checkEpicStatus(epic.getId());
            return epic.getId();
        }
        return -1;
    }

    @Override
    public int createSubTask(Subtask subtask) {
        if (subtask != null && epics.containsKey(subtask.getEpicId())) {
            if (isNotIntersection(subtask)) {
                subtask.setId(getNewId());
                Epic epic = epics.get(subtask.getEpicId());
                epic.setSubtaskIds(subtask);
                addSubtasks(subtask.getId(), subtask);
                checkEpicStatus(subtask.getEpicId());
                epic.checkEpicStartAndEndTime();
                return subtask.getId();
            }
        }
        return -1;
    }

    @Override
    public void updateTask(Integer id, Task task, Status newStatus) {
        if (tasks.containsKey(id)) {
            tasks.replace(id, task);
            tasks.get(id).setStatus(newStatus);
            tasks.get(id).setId(id);
        }
        if (subtasks.containsKey(id)) {
            Epic epic = epics.get(subtasks.get(id).getEpicId());
            epic.deleteOneSubtask(subtasks.get(id));
            subtasks.replace(id, (Subtask) task);
            Subtask subtask = subtasks.get(id);
            subtask.setStatus(newStatus);
            subtask.setId(id);
            epic.setSubtaskIds(subtask);
            checkEpicStatus(epic.getId());
            epic.checkEpicStartAndEndTime();
        }
    }

    @Override
    public void deleteOneTask(Integer id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            tasks.remove(id);
        }
        if (epics.containsKey(id)) {
            for (Subtask subtask : epics.get(id).getSubtaskIds()) {
                historyManager.remove(subtask.getId());
                subtasks.remove(subtask.getId());
            }
            historyManager.remove(id);
            epics.remove(id);
        }
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtaskIds().remove(subtask);
            historyManager.remove(id);
            subtasks.remove(id);
            checkEpicStatus(epic.getId());
            epic.checkEpicStartAndEndTime();
        }
    }

    @Override
    public List<String> getAllSubtasksByEpic(int epicId) {
        List<String> allSubtasksByEpic = new ArrayList<>();
        if (epics.containsKey(epicId)) {
            for (Subtask subtask : epics.get(epicId).getSubtaskIds()) {
                allSubtasksByEpic.add(subtask.toString());
            }
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
        for (Subtask subtask : epic.getSubtaskIds()) {
            Status status = subtask.getStatus();
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

    @Override
    public List<Task> getSortedTasks() {
        TreeSet<Task> set = new TreeSet<>(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                if (o1.getStartTime() == null && o2.getStartTime() == null)
                    return Integer.compare(o1.getId(), o2.getId());
                if (o1.getStartTime() == null)
                    return -1;
                if (o2.getStartTime() == null)
                    return 1;
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });
        set.addAll(getAllTasks());
        set.addAll(getAllSubtasks());
        return new ArrayList<Task> (set);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private boolean isNotIntersection(Task task) {
        for (Task treeTask : getSortedTasks()) {
            if (task.getStartTime().isAfter(treeTask.getStartTime())
                    && task.getStartTime().isBefore(treeTask.getEndTime())) {
                return false;
            }
            if (task.getEndTime().isAfter(treeTask.getStartTime())
                    && task.getEndTime().isBefore(treeTask.getEndTime())) {
                return false;
            }
        }
        return true;
    }
}
