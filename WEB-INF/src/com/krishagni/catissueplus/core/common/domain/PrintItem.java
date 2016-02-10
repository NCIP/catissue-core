package com.krishagni.catissueplus.core.common.domain;

import java.util.List;
import java.util.stream.Collectors;

public class PrintItem<T> {
	private T object;
	
	int copies;

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}
	
	public static <T> PrintItem<T> make(T object, Integer copies) {
		PrintItem<T> item = new PrintItem<T>();
		item.setObject(object);
		item.setCopies(copies != null ? copies : 1);
		return item;
	}
	
	public static <T> List<PrintItem<T>> make(List<T> objects, Integer copies) {
		return objects.stream().map((object) -> make(object, copies)).collect(Collectors.toList());
	}
}
