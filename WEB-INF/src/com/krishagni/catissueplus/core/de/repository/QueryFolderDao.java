package com.krishagni.catissueplus.core.de.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.de.domain.QueryFolder;

public interface QueryFolderDao extends Dao<QueryFolder> {		
	public List<QueryFolder> getUserFolders(Long userId);
	
	public QueryFolder getQueryFolder(Long folderId);
	
	public QueryFolder getByName(String name);
	
	public void deleteFolder(QueryFolder folder);
}
