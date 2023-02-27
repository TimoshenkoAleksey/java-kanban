package project.service;

import project.model.list.CustomLinkedList;
import project.model.task.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList customLinkedList;

    public InMemoryHistoryManager() {
        customLinkedList = new CustomLinkedList();
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
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }
}
