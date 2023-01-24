package project.model.task.list;

import project.model.task.Task;

public class Node {
    private Task data;
    private Node prev;
    private Node next;

    public Node(Task data) {
        prev = null;
        next = null;
        this.data = data;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Task getData() {
        return data;
    }
}
