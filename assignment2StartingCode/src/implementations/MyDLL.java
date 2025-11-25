package implementations;

import java.util.NoSuchElementException;

import utilities.Iterator;
import utilities.ListADT;

/**
 * Doubly linked list implementation of the ListADT interface.
 *
 * @param <E> the type of elements in this list
 */
public class MyDLL<E> implements ListADT<E> {

    private MyDLLNode<E> head;
    private MyDLLNode<E> tail;
    private int size;

    /**
     * Constructs an empty doubly linked list.
     */
    public MyDLL() {
        head = null;
        tail = null;
        size = 0;
    }

    // -------------------------
    // Basic helpers
    // -------------------------

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        MyDLLNode<E> current = head;
        while (current != null) {
            MyDLLNode<E> next = current.getNext();
            current.setPrev(null);
            current.setNext(null);
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void checkIndexInclusive(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    private void checkIndexExclusive(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    private MyDLLNode<E> getNode(int index) {
        checkIndexExclusive(index);

        MyDLLNode<E> current;
        // walk from nearest end
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.getPrev();
            }
        }
        return current;
    }

    // -------------------------
    // Add operations
    // -------------------------

    @Override
    public boolean add(int index, E toAdd)
            throws NullPointerException, IndexOutOfBoundsException {

        if (toAdd == null) {
            throw new NullPointerException("Element to add cannot be null.");
        }
        checkIndexInclusive(index);

        // append at end
        if (index == size) {
            return add(toAdd);
        }

        MyDLLNode<E> newNode = new MyDLLNode<>(toAdd);

        if (index == 0) {
            // insert at head
            newNode.setNext(head);
            if (head != null) {
                head.setPrev(newNode);
            }
            head = newNode;
            if (tail == null) {
                tail = newNode;
            }
        } else {
            MyDLLNode<E> current = getNode(index);
            MyDLLNode<E> previous = current.getPrev();

            newNode.setNext(current);
            newNode.setPrev(previous);

            previous.setNext(newNode);
            current.setPrev(newNode);
        }

        size++;
        return true;
    }

    @Override
    public boolean add(E toAdd) throws NullPointerException {
        if (toAdd == null) {
            throw new NullPointerException("Element to add cannot be null.");
        }

        MyDLLNode<E> newNode = new MyDLLNode<>(toAdd);

        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean addAll(ListADT<? extends E> toAdd) throws NullPointerException {
        if (toAdd == null) {
            throw new NullPointerException("List to add cannot be null.");
        }

        if (toAdd.isEmpty()) {
            return false;
        }

        Iterator<? extends E> it = toAdd.iterator();
        while (it.hasNext()) {
            E element = it.next();
            add(element); // this will throw NPE if element is null
        }
        return true;
    }

    // -------------------------
    // Access
    // -------------------------

    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        return getNode(index).getElement();
    }

    @Override
    public E set(int index, E toChange)
            throws NullPointerException, IndexOutOfBoundsException {

        if (toChange == null) {
            throw new NullPointerException("Element to set cannot be null.");
        }

        MyDLLNode<E> node = getNode(index);
        E old = node.getElement();
        node.setElement(toChange);
        return old;
    }

    // -------------------------
    // Remove operations
    // -------------------------

    @Override
    public E remove(int index) throws IndexOutOfBoundsException {
        MyDLLNode<E> node = getNode(index);
        return unlink(node);
    }

    @Override
    public E remove(E toRemove) throws NullPointerException {
        if (toRemove == null) {
            throw new NullPointerException("Element to remove cannot be null.");
        }

        MyDLLNode<E> current = head;
        while (current != null) {
            if (toRemove.equals(current.getElement())) {
                return unlink(current);
            }
            current = current.getNext();
        }
        return null; // not found
    }

    private E unlink(MyDLLNode<E> node) {
        E element = node.getElement();
        MyDLLNode<E> prev = node.getPrev();
        MyDLLNode<E> next = node.getNext();

        if (prev == null) {
            head = next;
        } else {
            prev.setNext(next);
        }

        if (next == null) {
            tail = prev;
        } else {
            next.setPrev(prev);
        }

        node.setNext(null);
        node.setPrev(null);

        size--;
        if (size == 0) {
            head = tail = null;
        }

        return element;
    }

    // -------------------------
    // Contains
    // -------------------------

    @Override
    public boolean contains(E toFind) throws NullPointerException {
        if (toFind == null) {
            throw new NullPointerException("Element to find cannot be null.");
        }

        MyDLLNode<E> current = head;
        while (current != null) {
            if (toFind.equals(current.getElement())) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    // -------------------------
    // toArray
    // -------------------------

    @Override
    public E[] toArray(E[] toHold) throws NullPointerException {
        if (toHold == null) {
            throw new NullPointerException("Destination array cannot be null.");
        }

        if (toHold.length < size) {
            @SuppressWarnings("unchecked")
            E[] newArray = (E[]) java.lang.reflect.Array.newInstance(
                    toHold.getClass().getComponentType(), size);
            toHold = newArray;
        }

        int i = 0;
        MyDLLNode<E> current = head;
        while (current != null) {
            toHold[i++] = current.getElement();
            current = current.getNext();
        }

        if (toHold.length > size) {
            toHold[size] = null;
        }

        return toHold;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        MyDLLNode<E> current = head;
        while (current != null) {
            result[i++] = current.getElement();
            current = current.getNext();
        }
        return result;
    }

    // -------------------------
    // Iterator
    // -------------------------

    @Override
    public Iterator<E> iterator() {
        return new DLLIterator();
    }

    private class DLLIterator implements Iterator<E> {

        private MyDLLNode<E> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in iterator.");
            }
            E element = current.getElement();
            current = current.getNext();
            return element;
        }
    }
}
