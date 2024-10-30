package util;

import clinic.Technician;

/**
 * A class representing a node in a linked list that holds a Technician object.
 * 
 * Each node contains a reference to the next node in the list, enabling the construction
 * of a linked data structure.
 * 
 * @author Joshua Goykhman (Netid: jg1986)
 */
public class Node {
    Technician data; // The data held by the node
    Node next; // Reference to the next node in the list

    /**
     * Constructs a node with the specified Technician data.
     *
     * @param data The Technician data to store in the node.
     */
    public Node(Technician data) {
        this.data = data; // Initialize the node's data
        this.next = null; // Initialize next reference as null
    }

    /**
     * Returns the Technician data stored in this node.
     *
     * @return The Technician data.
     */
    public Technician getTechnician() {
        return data; // Return the Technician data
    }

    /**
     * Returns the next node in the linked list.
     *
     * @return The next Node object, or null if there is no next node.
     */
    public Node getNext() {
        return next; // Return the next node
    }
}
