package com.sillypantscoder.utils;

import java.util.AbstractList;
import java.util.List;

public class ListCombination<T> extends AbstractList<T> {
	public List<T>[] lists;
	public ListCombination(List<T>[] lists) {
		this.lists = lists;
	}
	@SuppressWarnings("unchecked")
	public ListCombination(List<? extends List<T>> lists) {
		this.lists = new List[lists.size()];
		// whyyyyyyyyyy
		for (int i = 0; i < lists.size(); i++) {
			this.lists[i] = lists.get(i);
		}
	}
	public boolean add(T e) {
		for (int i = 0; i < lists.length; i++) {
			lists[i].add(e);
		}
		return true;
	}
	public boolean remove(Object e) {
		for (int i = 0; i < lists.length; i++) {
			lists[i].remove(e);
		}
		return true;
	}
	public int size() {
		int s = 0;
		for (int i = 0; i < lists.length; i++) {
			s += lists[i].size();
		}
		return s;
	}
	public T get(int index) {
		int idx = index;
		for (int i = 0; i < lists.length; i++) {
			if (idx < lists[i].size()) return lists[i].get(idx);
			idx -= lists[i].size();
		}
		throw new IndexOutOfBoundsException();
	}
}
