package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ListFolderQueriesCriteria extends AbstractListCriteria<ListFolderQueriesCriteria> {	

	private Long folderId;
	
	private boolean countReq;
	
	public Long folderId() {
		return folderId;
	}

	public ListFolderQueriesCriteria folderId(Long folderId) {
		this.folderId = folderId;
		return self();
	}

	public boolean countReq() {
		return countReq;
	}

	public ListFolderQueriesCriteria countReq(boolean countReq) {
		this.countReq = countReq;
		return self();
	}

	@Override
	public ListFolderQueriesCriteria self() {
		return this;
	}

}
