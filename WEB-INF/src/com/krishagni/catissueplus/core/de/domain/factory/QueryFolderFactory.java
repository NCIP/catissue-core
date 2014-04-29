package com.krishagni.catissueplus.core.de.domain.factory;

import java.util.List;

import com.krishagni.catissueplus.core.de.domain.QueryFolder;
import com.krishagni.catissueplus.core.de.events.QueryFolderDetails;

public interface QueryFolderFactory {
	//public QueryFolder createQueryFolder(Long userId, String name, List<Long> queryIds);
	
	public QueryFolder createQueryFolder(QueryFolderDetails details);
}
