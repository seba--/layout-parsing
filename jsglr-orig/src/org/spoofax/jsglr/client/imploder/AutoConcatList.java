package org.spoofax.jsglr.client.imploder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.spoofax.NotImplementedException;

/**
 * A list implementation that will automatically 
 * concatenate itself with any added lists
 * with nodes of the same Sort.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class AutoConcatList<E> implements List<E> {
	
	private static final long serialVersionUID = 349960310822051373L;
	
	private static final int INITIAL_LIST_SIZE = 10;
	
	private final String sort;

	private IToken emptyListToken;
	
	/** The inner list value, if there is only one value. */
	private E node;
	
	/** The inner list values, if there is more than one value. */
	private ArrayList<E> nodes;
	
	public AutoConcatList(String sort) {
		this.sort = sort;
	}
	
	public String getSort() {
		return sort;
	}
	
	public IToken getEmptyListToken() {
		return emptyListToken;
	}
	
	public void setEmptyListToken(IToken emptyListToken) {
		this.emptyListToken = emptyListToken;
	}
	
	private ArrayList<E> asList() {
		if (node != null) {
			nodes = new ArrayList<E>(INITIAL_LIST_SIZE);
			nodes.add(node);
			node = null;
		} else if (nodes == null) {
			nodes = new ArrayList<E>(INITIAL_LIST_SIZE);
		}
		return nodes;
	}

	@SuppressWarnings("unchecked")
	public boolean add(E e) {
		if (e == null) {
			throw new IllegalArgumentException();
		} else if (node == null && nodes == null) {
			if (e instanceof AutoConcatList) {
				AutoConcatList<E> eList = (AutoConcatList<E>) e;
				if (eList.getSort() == null || eList.getSort().equals(sort)) {
					node = eList.node;
					nodes = eList.nodes;
					return true;
				}
			}
			node = e;
		} else {
			asList();
			if (e instanceof AutoConcatList) {
				AutoConcatList<E> eList = (AutoConcatList<E>) e;
				if (eList.getSort() == null || eList.getSort().equals(sort)) {
					if (eList.node != null) {
						nodes.add(eList.node);
					} else if (eList.nodes != null) {
						nodes.addAll(eList.nodes);
					}
					return true;
				}
			}
			nodes.add(e);
		}
		return true;
	}

	public void add(int index, E element) {
		// asList().add(index, element);
		throw new NotImplementedException();
	}

	public boolean addAll(Collection<? extends E> c) {
	  boolean b = false;
		for (E e : c)
		  b |= add(e);
		return b;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		// throw new UnsupportedOperationException();
		throw new NotImplementedException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean contains(Object o) {
		if (node != null) return node == o;
		else return nodes != null && nodes.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return asList().containsAll(c);
	}

	public E get(int index) {
		if (index == 0 && node != null) {
			return node;
		} else {
			return asList().get(index);
		}
	}

	public int indexOf(Object o) {
		return asList().indexOf(o);
	}

	public boolean isEmpty() {
		if (node != null) {
			return false;
		} else {
			return nodes == null || nodes.isEmpty();
		}
	}

	public Iterator<E> iterator() {
		return asList().iterator();
	}

	public int lastIndexOf(Object o) {
		return asList().lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return asList().listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return asList().listIterator(index);
	}

	public boolean remove(Object o) {
		return asList().remove(o);
	}

	public E remove(int index) {
		return asList().remove(index);
	}

	public boolean removeAll(Collection<?> c) {
		return asList().removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return asList().retainAll(c);
	}

	public E set(int index, E element) {
		return asList().set(index, element);
	}

	public int size() {
		if (node != null) {
			return 1;
		} else {
			return nodes == null ? 0 : nodes.size();
		}
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return asList().subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return asList().toArray();
	}

	public <T> T[] toArray(T[] a) {
		return asList().toArray(a);
	}
	
	@Override
	public boolean equals(Object arg0) {
		return arg0 == this || asList().equals(arg0);
	}
	
	@Override
	public int hashCode() {
		return asList().hashCode();
	}
	
	@Override
	public String toString() {
		return asList().toString();
	}
}
