package com.krishagni.catissueplus.core.common.events;

import org.apache.commons.lang.StringUtils;

public enum Operation {
	CREATE("Create"), 
	
	READ("Read"),
	
	UPDATE("Update"),
	
	DELETE("Delete"),
	
	LOCK("Lock"),
	
	UNLOCK("Unlock");
	
	private final String name; 
	
	private Operation(String name) {
		this.name = name;
	}
	
	public String getName() { 
		return name;
	}
	
	public static Operation fromName(String name) {
		if (StringUtils.isBlank(name)) {
			return null;
		}
		
		Operation result = null;
		for (Operation op : Operation.values()) {
			if (op.name.equalsIgnoreCase(name)) {
				result = op;
				break;
			}
		}
		
		return result;		
	}
}
