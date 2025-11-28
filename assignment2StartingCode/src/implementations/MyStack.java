package implementations;

import java.util.EmptyStackException;

import utilities.Iterator;
import utilities.StackADT;

/**
 * @author Karina Chiste
 * 
 * A simple implementation of the StackADT.
 * This class uses MyArrayList as the underlying data structure.
 * 
 */


public class MyStack<E> implements StackADT<E>
{
	public MyArrayList<E> list;
	
	public MyStack(){
		list = new MyArrayList<E>();
	}
	
	@Override
	public void push(E toAdd) throws NullPointerException
	{
		if (toAdd == null) {
			throw new NullPointerException("Item cannot be null");
		}
		else {
			list.add(0,toAdd);
		}
		
		
	}

	@Override
	public E pop() throws EmptyStackException
	{
		if(list.isEmpty()) {
			throw new EmptyStackException();
		}
		else {
			E removedItem = list.get(0);
			list.remove(removedItem);
			return removedItem;
		}
		
	}

	@Override
	public E peek() throws EmptyStackException
	{
		if (list.isEmpty()) {
			throw new EmptyStackException();
		}
		else {
			return list.get(0);
		}
		
	}

	@Override
	public void clear()
	{
		list.clear();
		
	}

	@Override
	public boolean isEmpty()
	{
		if (list.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
		
	}

	@Override
	public Object[] toArray()
	{
		return list.toArray();
	}

	@Override
	public E[] toArray(E[] holder) throws NullPointerException
	{
		return list.toArray(holder);
	}

	@Override
	public boolean contains(E toFind) throws NullPointerException
	{
		
		if (toFind == null) {
			throw new NullPointerException();
		}
		else {
			return list.contains(toFind);
		}
		
	}

	@Override
	public int search(E toFind)
	{
		
		for (int i = list.size();i>0;i--) {
			if(list.get(i-1).equals(toFind)) {
				return i;
			}
		}
		
		return -1 ;
	}

	@Override
	public Iterator<E> iterator()
	{
		return list.iterator();
	}

	@Override
	public boolean equals(StackADT<E> that)
	{
//		MyArrayList<E> compareList = new MyArrayList<E>();
		if(that.size() != list.size()) {
			return false;
		}
		for(int i = 1;i<=list.size();i++) {
			if(!that.contains(list.get(i-1))) {
				
				return false;
			}
			else if(that.search(list.get(i-1)) != i) {
				
				return false;
			}
			
		}
		return true;
	}

	@Override
	public int size()
	{
		return list.size();
	}

	@Override
	public boolean stackOverflow()
	{
		int items = 0;
		for(int i = 0; i>list.size();i++) {
			items++;
		}
		if(items>=list.size()) {
			return false;
		}
		else {
			return true;
		}
		
	}

}
