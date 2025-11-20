package implementations;

import utilities.ListADT;
import utilities.Iterator;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * @author Denver Timlick
 * A simple array-based implementation of ListADT.
 * This class mimics how ArrayList works internally, but only uses raw arrays,
 * as required by the assignment. The list grows automatically when full.
 *
 * @param <E> the type of element stored in this list
 */
public class MyArrayList<E> implements ListADT<E> {

    private E[] data;                  // The underlying array storing elements
    private int size;                  // Number of actual elements stored
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Default constructor. Creates a new array with the default starting capacity.
     */
    public MyArrayList() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * Returns the number of elements currently in the list.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Removes all elements by nulling them out and resetting the size.
     * We null values manually to avoid memory leaks.
     */
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;    // Help garbage collection
        }
        size = 0;
    }

    /**
     * Inserts an element at a specific index, shifting elements to the right.
     */
    @Override
    public boolean add(int index, E toAdd)
            throws NullPointerException, IndexOutOfBoundsException {

        if (toAdd == null) {
            throw new NullPointerException("Null elements are not allowed.");
        }

        // index == size is allowed (insert at end)
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Grow array if needed
        ensureCapacity();

        // Shift elements to make space
        for (int i = size - 1; i >= index; i--) {
            data[i + 1] = data[i];
        }

        data[index] = toAdd;
        size++;
        return true;
    }

    /**
     * Appends a new element to the end of the list using add(size, element).
     */
    @Override
    public boolean add(E toAdd) throws NullPointerException {
        if (toAdd == null)
            throw new NullPointerException("Null elements not allowed.");

        return add(size, toAdd);
    }

    /**
     * Ensures the internal array has room. If full, we double the size.
     */
    private void ensureCapacity() {
        if (size == data.length) {
            data = Arrays.copyOf(data, data.length * 2);
        }
    }

    /**
     * Adds all elements from another ListADT into this list.
     * We use the other list's iterator to traverse it.
     */
    @Override
    public boolean addAll(ListADT<? extends E> toAdd) throws NullPointerException {
        if (toAdd == null) {
            throw new NullPointerException("Cannot add elements from a null list.");
        }

        Iterator<? extends E> it = toAdd.iterator();

        while (it.hasNext()) {
            add(it.next());
        }

        return true;
    }

    /**
     * Returns the element at a specified index.
     */
    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        return data[index];
    }

    /**
     * Removes and returns the element at a specific index.
     * Shifts remaining elements to the left.
     */
    @Override
    public E remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        E removed = data[index];

        // Shift everything left to fill the gap
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        data[size - 1] = null;  // Avoid memory leak
        size--;

        return removed;
    }

    /**
     * Replaces the value at a given index with a new value.
     * Returns the old value.
     */
    @Override
    public E set(int index, E toChange)
            throws NullPointerException, IndexOutOfBoundsException {

        if (toChange == null) {
            throw new NullPointerException("Cannot set null elements in the list.");
        }

        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        E old = data[index];
        data[index] = toChange;   // Assign new value
        return old;
    }

    /**
     * Removes the first occurrence of a given element.
     * Returns the removed element, or null if not found.
     */
    @Override
    public E remove(E toRemove) throws NullPointerException {
        if (toRemove == null)
            throw new NullPointerException("Cannot remove a null element.");

        for (int i = 0; i < size; i++) {
            if (toRemove.equals(data[i])) {
                return remove(i);   // Re-use index-based remove
            }
        }

        return null;
    }

    /**
     * Returns true if the list contains a given element.
     */
    @Override
    public boolean contains(E toFind) throws NullPointerException {
        if (toFind == null)
            throw new NullPointerException("Cannot search for null elements.");

        for (int i = 0; i < size; i++) {
            if (data[i].equals(toFind)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if the list has no elements.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the list contents in an array of the same runtime type as toHold.
     */
    @Override
    public E[] toArray(E[] toHold) throws NullPointerException {
        if (toHold == null)
            throw new NullPointerException("Provided array cannot be null.");

        // If the provided array is too small, return a new one of the same type
        if (toHold.length < size) {
            return (E[]) Arrays.copyOf(data, size, toHold.getClass());
        }

        // Otherwise, copy elements into provided array
        for (int i = 0; i < size; i++) {
            toHold[i] = data[i];
        }

        // Add trailing null if array is larger
        if (toHold.length > size) {
            toHold[size] = null;
        }

        return toHold;
    }

    /**
     * Returns a plain Object[] copy of the list contents.
     */
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(data, 0, result, 0, size);
        return result;
    }

    /**
     * Simple internal iterator class that walks the list from index 0 to size-1.
     */
    private class MyArrayListIterator implements Iterator<E> {

        private int current = 0;   // Tracks the current index during iteration

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException("No more elements in the list.");

            return data[current++];
        }
    }

    /**
     * Returns a new iterator starting at index 0.
     */
    @Override
    public Iterator<E> iterator() {
        return new MyArrayListIterator();
    }
}