package Manager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    public HashMap<java.lang.Integer, Task> tasks;
    public HashMap<java.lang.Integer, Epic> epics;
    public HashMap<java.lang.Integer, Subtask> subtasks;
    public int counterID = 1;

    public Manager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public ArrayList<String> getAllTasks() {
        ArrayList<String> allTasks = new ArrayList<>();
        if (!tasks.isEmpty()) {
            for (java.lang.Integer taskID : tasks.keySet()) {
                allTasks.add(tasks.get(taskID).toString());
            }
        }
        return allTasks;
    }

    public ArrayList<String> getAllEpics() {
        ArrayList<String> allEpics = new ArrayList<>();
        if (!epics.isEmpty()) {
            for (java.lang.Integer epicID : epics.keySet()) {
                allEpics.add(epics.get(epicID).toString());
            }
        }
        return allEpics;
    }

    public ArrayList<String> getAllSubtasks() {
        ArrayList<String> allSubtasks = new ArrayList<>();
        if (!subtasks.isEmpty()) {
            for (java.lang.Integer subtaskID : subtasks.keySet()) {
                allSubtasks.add(subtasks.get(subtaskID).toString());
            }
        }
        return allSubtasks;
    }

    public void DeleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public String GetByID(int taskID) {
        if (tasks.containsKey(taskID))
            return tasks.get(taskID).toString();
        if (epics.containsKey(taskID))
            return epics.get(taskID).toString();
        if (subtasks.containsKey(taskID))
            return subtasks.get(taskID).toString();
        return null;
    }

    public int createTask(Task task) {
        task.setTaskID(counterID++);
        tasks.put(task.getTaskID(), task);
        return task.getTaskID();
    }

    public int createEpic(Epic epic) {
        epic.setTaskID(counterID++);
        epics.put(epic.getTaskID(), epic);
        return epic.getTaskID();
    }

    public int createSubTask(Subtask subtask) {
        subtask.setTaskID(counterID++);
        epics.get(subtask.getMyEpicID()).setMySubtasks(subtask.getTaskID());
        epics.get(subtask.getMyEpicID()).setStatus("IN_PROGRESS");
        subtasks.put(subtask.getTaskID(), subtask);
        return subtask.getTaskID();
    }

    public void updateTask(int taskID, Object task, String newStatus) {
        if (tasks.containsKey(taskID)) {
            tasks.replace(taskID, (Task) task);
            tasks.get(taskID).setStatus(newStatus);
        }
        if (subtasks.containsKey(taskID)) {
            subtasks.replace(taskID, (Subtask) task);
            subtasks.get(taskID).setStatus(newStatus);
            for (Integer mySubtask : epics.get(subtasks.get(taskID).getMyEpicID()).getMySubtasks()) {
                if (subtasks.get(mySubtask).getStatus() == "NEW"
                        || subtasks.get(mySubtask).getStatus() == "IN_PROGRESS") {
                    return;
                }
            }
            epics.get(subtasks.get(taskID).getMyEpicID()).setStatus("DONE");
        }
    }

    public void DeleteOneTasks(int taskID) {
        if (tasks.containsKey(taskID)) {
            tasks.remove(taskID);
        }
        if (epics.containsKey(taskID)) {
            for (Integer mySubtaskID : epics.get(taskID).getMySubtasks()) {
                subtasks.remove(mySubtaskID);
            }
            epics.remove(taskID);
        }
        if (subtasks.containsKey(taskID)) {
            epics.get(subtasks.get(taskID).getMyEpicID()).getMySubtasks().remove(subtasks.get(taskID));
            subtasks.remove(taskID);
            for (Integer mySubtaskID : epics.get(subtasks.get(taskID).getMyEpicID()).getMySubtasks()) {
                if (epics.get(mySubtaskID).getStatus() == "NEW"
                        || epics.get(mySubtaskID).getStatus() == "IN_PROGRESS") {
                    return;
                }
            }
            epics.get(subtasks.get(taskID).getMyEpicID()).setStatus("DONE");
        }
    }

    public ArrayList<String> GetAllSubtasksByEpic(int epicID) {
        ArrayList<String> allSubtasksByEpic = new ArrayList<>();
        for (Integer mySubtaskID : epics.get(epicID).getMySubtasks()) {
            allSubtasksByEpic.add(subtasks.get(mySubtaskID).toString());
        }
        return allSubtasksByEpic;
    }
}
