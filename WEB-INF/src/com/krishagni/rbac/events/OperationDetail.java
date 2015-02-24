package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.rbac.domain.Operation;

public class OperationDetail {
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
	
	public OperationDetail() {
		
	}
	
	public OperationDetail(Long id) {
		this.id = id;
	}
	
	public static OperationDetail from(Operation operation) {
		OperationDetail os = new OperationDetail();
		operation = operation == null ? new Operation() : operation;
		os.setId(operation.getId());
		os.setName(operation.getName());
		return os;
	}
	
	public static List<OperationDetail> from(List<Operation> operations) {
		List<OperationDetail> osl = new ArrayList<OperationDetail>();
		
		for (Operation o: operations) {
			osl.add(from(o));
		}
		return osl;
	}
}
