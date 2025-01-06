package com.sillypantscoder.utils;

import java.util.AbstractList;
import java.util.List;

/**
 * This object represents a combination of multiple lists.
 * Elements added to or removed from this list will be added to or removed from each of the composing lists.
 */
public class ListCombination<T> extends AbstractList<T> {
	public List<T>[] lists;
	public ListCombination(List<T>[] lists) {
		this.lists = lists;
	}
	@SuppressWarnings("unchecked")
	public ListCombination(List<? extends List<T>> lists) {
		this.lists = new List[lists.size()];
		for (int i = 0; i < lists.size(); i++) {
			this.lists[i] = lists.get(i);
		}
	}
	public boolean add(T e) {
		for (List<T> list : lists) {
			list.add(e);
		}
		return true;
	}
	public boolean remove(Object e) {
		for (List<T> list : lists) {
			list.remove(e);
		}
		return true;
	}
	public int size() {
		int s = 0;
		for (List<T> list : lists) {
			s += list.size();
		}
		return s;
	}
	public T get(int index) {
		int idx = index;
		for (List<T> list : lists) {
			if (idx < list.size()) {
				return list.get(idx);
			}
			idx -= list.size();
		}
		throw new IndexOutOfBoundsException();
	}
}
