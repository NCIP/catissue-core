package com.krishagni.catissueplus.core.biospecimen.events;

public class CpQueryCriteria {
	private Long id;
	
	private String title;
	
	private String shortTitle;
	
	private boolean fullObject;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public boolean isFullObject() {
		return fullObject;
	}

	public void setFullObject(boolean fullObject) {
		this.fullObject = fullObject;
	}
}
