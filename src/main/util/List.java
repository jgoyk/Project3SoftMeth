package util;

import clinic.Appointment;

import java.util.Iterator;

/**
 * A generic dynamic array implementation of a list that allows for adding,
 * removing, and accessing elements, along with iterator functionality.
 * 
 * @author Dhawal Arora (Netid: da812)
 *
 * @param <E> the type of elements in this list
 */
public class List<E> {
    public static final int INITIAL_CAPACITY = 4; // Initial capacity of the list
    public static final int INCREASE_CAPACITY = 4; // Amount to increase capacity by when needed
    public static final int NOT_FOUND = -1; // Constant used to indicate an item not found

    private E[] objects; // Array to store the elements of the list
    private int size; // Current size of the list

    /**
     * Constructs a List with an initial capacity of 4.
     */
    public List() {
        objects = (E[]) new Object[INITIAL_CAPACITY]; // Initializes the objects array
        size = 0; // Initializes size to 0
    }

    /**
     * Finds the index of the specified object in the list.
     *
     * @param e The object to find.
     * @return the index of the object if found, or NOT_FOUND if not.
     */
    private int find(E e) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(e)) {
                return i; // Return the index if the object is found
            }
        }
        return NOT_FOUND; // Return NOT_FOUND if the object is not found
    }

    /**
     * Grows the internal array to accommodate more elements.
     */
    private void grow() {
        E[] newObjects = (E[]) new Object[objects.length + INCREASE_CAPACITY]; // Create a new larger array
        for (int i = 0; i < size; i++) {
            newObjects[i] = objects[i]; // Copy elements to the new array
        }
        objects = newObjects; // Update the reference to the new array
    }

    /**
     * Checks if the list contains the specified object.
     *
     * @param e The object to check for.
     * @return true if the object is in the list, false otherwise.
     */
    public boolean contains(E e) {
        return find(e) != NOT_FOUND; // Return true if the object is found
    }

    /**
     * Adds a new object to the list if it is not already present.
     *
     * @param e The object to be added.
     */
    public void add(E e) {
        if (contains(e)) { // Check if the object already exists
            return; // Do not add duplicates
        }
        if (size == objects.length) { // Check if the array is full
            grow(); // Increase the capacity if necessary
        }
        objects[size++] = e; // Add the new object and increment the size
    }

    /**
     * Removes an object from the list.
     *
     * @param e The object to be removed.
     */
    public void remove(E e) {
        int index = find(e); // Find the index of the object
        if (index == NOT_FOUND) { // If the object is not found
            return; // Do nothing
        }
        // Shift elements to the left to fill the gap
        for (int i = index; i < size - 1; i++) {
            objects[i] = objects[i + 1]; // Shift elements to the left
        }
        objects[--size] = null; // Decrement size and set the last element to null
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0; // Return true if size is 0
    }

    /**
     * Gets the current size of the list.
     *
     * @return the number of elements in the list.
     */
    public int size() {
        return size; // Return the current size
    }

    /**
     * Returns an iterator over the elements in this list.
     *
     * @return an Iterator for the list.
     */
    public Iterator<E> iterator() {
        return new ListIterator<E>(); // Return a new iterator instance
    }

    /**
     * Gets the object at the specified index.
     *
     * @param index The index of the object to retrieve.
     * @return the object at the specified index, or null if the index is out of bounds.
     */
    public E get(int index) {
        if (index < 0 || index >= size) {
            return null; // Return null if the index is out of bounds
        }
        return objects[index]; // Return the object at the index
    }

    /**
     * Sets the object at the specified index to a new value.
     *
     * @param index The index of the object to set.
     * @param e The new object to set at the index.
     */
    public void set(int index, E e) {
        if (index < 0 || index >= size) {
            return; // Do nothing if the index is out of bounds
        }
        objects[index] = e; // Set the object at the index
    }

    /**
     * Returns the index of the specified object in the list.
     *
     * @param e The object to find.
     * @return the index of the object if found, or NOT_FOUND if not.
     */
    public int indexOf(E e) {
        return find(e); // Return the index found
    }

    /**
     * An inner class that implements the Iterator interface for the List.
     *
     * @param <E> the type of elements returned by this iterator
     */
    private class ListIterator<E> implements Iterator<E> {
        private int currentIndex = 0; // Current index for iteration

        /**
         * Checks if there are more elements to iterate over.
         *
         * @return true if there are more elements, false otherwise.
         */
        public boolean hasNext() {
            return currentIndex < size && objects[currentIndex] != null; // Check if the current index is valid
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the list.
         */
        public E next() {
            return (E) objects[currentIndex++]; // Return the next object and increment the index
        }
    }
}
