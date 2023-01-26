package project.service;

import project.model.list.CustomLinkedList;
import project.model.task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> taskViewHistory;
    private CustomLinkedList customLinkedList;

    public InMemoryHistoryManager() {
        taskViewHistory = new ArrayList<>();
        customLinkedList = new CustomLinkedList();
    }

    public ArrayList<Task> getTaskViewHistory() {
        return taskViewHistory;
    }

    public CustomLinkedList getCustomLinkedList() {
        return customLinkedList;
    }

    @Override
    public void add(Task task) {
        customLinkedList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        customLinkedList.removeFromHistory(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        customLinkedList.getTasks(taskViewHistory);
        return taskViewHistory;
    }
}
