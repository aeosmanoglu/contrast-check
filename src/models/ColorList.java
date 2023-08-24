package models;

public class ColorList {
    public Node head;

    public void add(Color data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            return;
        }
        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
    }

    public void add(int index, Color data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            return;
        }
        if (index == 0) {
            newNode.next = head;
            head = newNode;
            return;
        }
        Node current = head;
        for (int i = 0; i < index - 1; i++) {
            if (current.next == null) {
                current.next = newNode;
                return;
            }
            current = current.next;
        }
        newNode.next = current.next;
        current.next = newNode;
    }

    @Override
    public String toString() {
        return "ColorList{" + "head=" + head + '}';
    }
}
