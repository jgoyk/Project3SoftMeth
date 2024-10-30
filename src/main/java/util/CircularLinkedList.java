package util;

import clinic.Technician;

/**
 * Represents a circular linked list that stores Technician objects.
 * This data structure allows for efficient insertion, deletion, and traversal of Technician nodes.
 * 
 * @author Dhawal Arora (Netid: da812)
 */
public class CircularLinkedList {
    private Node head; // The first node in the circular linked list
    private Node tail; // The last node in the circular linked list
    private int size; // The number of nodes in the circular linked list

    /**
     * Constructs an empty CircularLinkedList.
     * Initializes head and tail to null and size to 0.
     */
    public CircularLinkedList() {
        head = null; // Initialize head as null
        tail = null; // Initialize tail as null
        size = 0; // Initialize size as 0
    }

    /**
     * Gets the current size of the circular linked list.
     *
     * @return the number of nodes in the list
     */
    public int getSize() {
        return size; // Returns the size of the list
    }

    /**
     * Sets the head node of the circular linked list.
     *
     * @param head the new head node to set
     */
    public void setHead(Node head) {
        this.head = head; // Sets the head node
    }

    /**
     * Gets the head node of the circular linked list.
     *
     * @return the head node of the list
     */
    public Node getHead() {
        return head; // Returns the head node
    }

    /**
     * Adds a new Technician node to the end of the circular linked list.
     *
     * @param data the Technician object to be added
     */
    public void add(Technician data) {
        Node newNode = new Node(data); // Create a new node with the given data

        if (head == null) { // If the list is empty
            head = newNode; // Set head to new node
            tail = newNode; // Set tail to new node
            newNode.next = head; // Point new node to head (itself in this case)
        } else { // If the list is not empty
            newNode.next = head; // Point new node to the current head
            tail.next = newNode; // Link the current tail to the new node
            tail = newNode; // Update the tail to the new node
        }
        size += 1; // Increment the size of the list
    }

    /**
     * Reverses the circular linked list in place.
     * Updates the head to point to the new first node after reversal.
     */
    public void reverse() {
        if (head == null) { // Check if the list is empty
            return; // Exit if the list is empty
        }

        Node prev = null; // Previous node
        Node current = head; // Current node
        Node next; // Next node
        do {
            next = current.next; // Store the next node
            current.next = prev; // Reverse the current node's next pointer
            prev = current; // Move prev to current
            current = next; // Move current to next
        } while (current != head); // Continue until we loop back to head

        head.next = prev; // Update the head's next to the new first node
        head = prev; // Update head to the new first node
    }

    /**
     * Deletes the first occurrence of a Technician node with the specified key from the list.
     *
     * @param key the Technician object to be deleted
     */
    public void delete(Technician key) {
        if (head == null) { // Check if the list is empty
            return; // Exit if the list is empty
        }

        Node curr = head; // Start from the head node
        Node prev = null; // Previous node
        while (curr.next != head) { // Traverse until the last node
            if (curr.data == key) { // If the current node's data matches the key
                if (prev == null) { // If the node to be deleted is the head
                    Node last = head; // Find the last node
                    while (last.next != head) { // Traverse to the last node
                        last = last.next;
                    }
                    head = curr.next; // Update head to the next node
                    last.next = head; // Link last node to new head
                    return; // Exit after deletion
                } else { // If the node to be deleted is not the head
                    prev.next = curr.next; // Bypass the current node
                    if (curr == tail) { // If the current node is the tail
                        tail = prev; // Update tail to the previous node
                    }
                    return; // Exit after deletion
                }
            }
            prev = curr; // Move previous to current
            curr = curr.next; // Move current to next
        }

        if (curr == head && curr.data == key) { // Check if the head node is to be deleted
            prev.next = head; // Update the previous node's next to point to head
            tail = prev; // Update tail to the previous node
        }
        size -= 1; // Decrement the size of the list
    }
}
