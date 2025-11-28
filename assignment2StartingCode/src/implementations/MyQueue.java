package implementations;

import utilities.QueueADT;
import utilities.Iterator;
import exceptions.EmptyQueueException;

/**
 * @author Jared Gutierrez
 * 
 * Queue implementation using MyDLL as the underlying data structure.
 * Classic FIFO behavior - first in first out.
 * 
 * @param <E> the type of element stored in this queue
 */
public class MyQueue<E> implements QueueADT<E> {
    
    // Use MyDLL as the underlying data structure
    private MyDLL<E> list;

    /**
     * Creates a new empty queue.
     */
    public MyQueue() {
        this.list = new MyDLL<>();
    }

    @Override
    public void enqueue(E toAdd) throws NullPointerException {
        if (toAdd == null) {
            throw new NullPointerException("Cannot enqueue null element");
        }
        // Add to the end of the list (back of queue)
        list.add(toAdd);
    }

    @Override
    public E dequeue() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException("Queue is empty");
        }
        // Remove from the beginning of the list (front of queue)
        return list.remove(0);
    }

    @Override
    public E peek() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException("Queue is empty");
        }
        // Get the first element without removing
        return list.get(0);
    }

    @Override
    public void dequeueAll() {
        list.clear();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(E toFind) throws NullPointerException {
        if (toFind == null) {
            throw new NullPointerException("Cannot search for null element");
        }
        return list.contains(toFind);
    }

    @Override
    public int search(E toFind) {
        // Use iterator to find position (1-based)
        Iterator<E> iter = list.iterator();
        int position = 1;
        
        while (iter.hasNext()) {
            if (iter.next().equals(toFind)) {
                return position;
            }
            position++;
        }
        return -1;
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public boolean equals(QueueADT<E> that) {
        if (that == null) {
            return false;
        }

        if (this.size() != that.size()) {
            return false;
        }

        Iterator<E> thisIter = this.iterator();
        Iterator<E> thatIter = that.iterator();

        while (thisIter.hasNext() && thatIter.hasNext()) {
            E thisElement = thisIter.next();
            E thatElement = thatIter.next();

            if (!thisElement.equals(thatElement)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public E[] toArray(E[] holder) throws NullPointerException {
        if (holder == null) {
            throw new NullPointerException("Array cannot be null");
        }
        return list.toArray(holder);
    }

    @Override
    public boolean isFull() {
        // List-based queue is never full
        return false;
    }

    @Override
    public int size() {
        return list.size();
    }
}
