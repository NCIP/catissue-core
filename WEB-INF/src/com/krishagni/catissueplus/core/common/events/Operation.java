package com.krishagni.catissueplus.core.common.events;

public enum Operation {
	CREATE("Create"), 
	
	READ("Read"),
	
	UPDATE("Update"),
	
	DELETE("Delete");
	
	private final String name; 
	
	private Operation(String name) {
		this.name = name;
	}
	
	public String getName() { 
		return name;
	}
}
