package utilities;
/**
 * 
 * @author Denver Timlick
 * @version 1.0
 * @since 2025-11-04
 */

public interface StackADT<T> {
	
    /**
     * Adds an element to the top of the stack.
     * <p>PRE: {@code element} is not null.</p>
     * <p>POST: The element is placed on top of the stack.</p>
     *
     * @param element the item to be pushed
     * @throws IllegalArgumentException if {@code element} is null
     */
	void push(T element);
	
    /**
     * Removes and returns the top element of the stack.
     * <p>PRE: The stack is not empty.</p>
     * <p>POST: The top element is removed and returned.</p>
     *
     * @return the element previously at the top of the stack
     */
	T pop();
	
	  /**
     * Returns (but does not remove) the top element of the stack.
     * <p>PRE: The stack is not empty.</p>
     *
     * @return the element currently at the top of the stack
     */
    T peek();

    /**
     * Determines whether the stack is empty.
     *
     * @return {@code true} if the stack has no elements, otherwise {@code false}
     */
    boolean isEmpty();

    /**
     * Returns the number of elements currently stored in the stack.
     *
     * @return the size of the stack
     */
    int size();

    /**
     * Removes all elements from the stack.
     * <p>POST: The stack is empty.</p>
     */
    void clear();

}
