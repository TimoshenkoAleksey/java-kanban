package project.model.list;

import project.model.task.Task;
import project.service.InMemoryHistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomLinkedList {
    private Node head;
    private Node tail;

    private Map<Integer, Node> nodes;

    public CustomLinkedList() {
        nodes = new HashMap<>();
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    public void linkLast(Task task) {
        removeFromHistory(task.getId());
        if (head == null) {
            head = new Node(task);
            tail = head;
            nodes.put(task.getId(), head);
        } else {
            Node newTail = new Node(task);
            newTail.setPrev(tail);
            tail.setNext(newTail);
            tail = newTail;
            nodes.put(task.getId(), tail);
        }
    }

    public void getTasks(ArrayList<Task> taskViewHistory) {
        taskViewHistory.clear();
        for (Node node : nodes.values())
            taskViewHistory.add(node.getData());
    }

    public void removeNode(Node node) {
        Node prevNode = node.getPrev();
        Node nextNode = node.getNext();
        if (prevNode == null && nextNode == null) {
            head = null;
            tail = null;
        } else if (prevNode == null) {
            nextNode.setPrev(null);
            head = nextNode;
        } else if (nextNode == null) {
            prevNode.setNext(null);
            tail = prevNode;
        } else {
            prevNode.setNext(nextNode);
            nextNode.setPrev(prevNode);
        }
    }

    public void removeFromHistory(int id) {
        if (nodes.containsKey(id)) {
            removeNode(nodes.get(id));
            nodes.remove(id);
        }
    }
}
