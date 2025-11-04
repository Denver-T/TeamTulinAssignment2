package utilities;
/**
 * 
 * @author Jared Gutierrez
 * @version 1.0
 * @since 2025-11-04
 */

 public interface QueueADT<T> {

    /**
     * Creates a new empty queue.
     * <p>POST: Queue is empty.</p>
     *  
     * @return a new empty queue
     */
    T newQueue();
    
    /**
     * Adds an element to the back of the queue.
     * <p>PRE: {@code element} is not null.</p>
     * <p>POST: The element is added to the back of the queue.</p>
     * 
     * @throws IllegalArgumentException if {@code element} is null
     * @throws IllegalStateException if the queue is full
     * @param element the item to be added to the back of the queue
     */
    void enqueue(Object element);
    
    /**
     * Removes and returns the front element of the queue.
     * <p>PRE: The queue is not empty.</p>
     * <p>PROST: The front element is removed and returned.</p>
     * 
     * @return the element at the front of the queue.
     * @throws IllegalStateException if the queue is empty
     */
    T dequeue();
    
    /**
     * Returns the front of the queue without removing it.
     * <p>PRE: The queue is not empty.</p>
     * 
     * @return the element at the front of the queue.
     * @throws IllegalStateException if the queue is empty
     */
    T peek();
 }