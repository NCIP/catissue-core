package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.rbac.domain.Operation;

public class OperationDetails {
	private Long id;
	
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public OperationDetails() {
		
	}
	
	public OperationDetails(Long id) {
		this.id = id;
	}
	
	public static OperationDetails fromOperation(Operation operation) {
		OperationDetails os = new OperationDetails();
		operation = operation == null ? new Operation() : operation;
		os.setId(operation.getId());
		os.setName(operation.getName());
		return os;
	}
	
	public static List<OperationDetails> fromOperations(List<Operation> operations) {
		List<OperationDetails> osl = new ArrayList<OperationDetails>();
		
		for (Operation o: operations) {
			osl.add(fromOperation(o));
		}
		return osl;
	}
}
