package models;

public class Node {
    public Color data;
    public Node next;

    public Node(Color data) {
        this.data = data;
        this.next = null;
    }

    public String toString() {
        return this.data.toString();
    }
}
