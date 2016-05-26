package com.krishagni.catissueplus.core.common.events;

public class EntityDeleteResp<T> {
	private boolean completed;
	
	private T entity;

	public boolean isCompleted() {
		return completed;
	}

	public T getEntity() {
		return entity;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
}