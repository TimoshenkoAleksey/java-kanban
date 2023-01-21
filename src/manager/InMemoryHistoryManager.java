package manager;

import node.Node;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> taskViewHistory;
    CustomLinkedList customLinkedList;

    public InMemoryHistoryManager() {
        taskViewHistory = new ArrayList<>();
        customLinkedList = new CustomLinkedList();
    }

    @Override
    public void add(Task task) {
        if (customLinkedList.nodes.containsKey(task.getId())) {
            customLinkedList.removeNode(customLinkedList.nodes.get(task.getId()));
            customLinkedList.nodes.remove(task.getId());
        }
        customLinkedList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (customLinkedList.nodes.containsKey(id)) {
            customLinkedList.removeNode(customLinkedList.nodes.get(id));
            customLinkedList.getNodes().remove(id);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        customLinkedList.getTasks();
        return taskViewHistory;
    }

    private class CustomLinkedList {
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

        public void getTasks() {
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
            } else if (nextNode == null) {
                prevNode.setNext(null);
            } else {
                prevNode.setNext(nextNode);
                nextNode.setPrev(prevNode);
            }
        }
    }
}
