package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ListQueryAuditLogsCriteria extends AbstractListCriteria<ListQueryAuditLogsCriteria> {
	public static enum Type {
		ALL,
		LAST_24
	};
	
	private Long savedQueryId;
	
	private Type type;
	
	private boolean countReq;

	public Long savedQueryId() {
		return savedQueryId;
	}

	public ListQueryAuditLogsCriteria savedQueryId(Long savedQueryId) {
		this.savedQueryId = savedQueryId;
		return self();
	}
	
	public Type type() {
		return type;
	}

	public ListQueryAuditLogsCriteria type(Type type) {
		this.type = type;
		return self();
	}

	public boolean countReq() {
		return countReq;
	}

	public ListQueryAuditLogsCriteria countReq(boolean countReq) {
		this.countReq = countReq;
		return self();
	}

	@Override
	public ListQueryAuditLogsCriteria self() {
		return this;
	}
}
