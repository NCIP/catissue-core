package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.domain.QueryFolder;

public class QueryFolderSummary {

	private Long id;
	
	private String name;
	
	private UserSummary owner;

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
	
	public UserSummary getOwner() {
		return owner;
	}

	public void setOwner(UserSummary owner) {
		this.owner = owner;
	}

	public static QueryFolderSummary fromQueryFolder(QueryFolder queryFolder){
		QueryFolderSummary folderSummary = new QueryFolderSummary();
		folderSummary.setId(queryFolder.getId());
		folderSummary.setName(queryFolder.getName());
		folderSummary.setOwner(UserSummary.fromUser(queryFolder.getOwner()));
		return folderSummary;
	}
}
