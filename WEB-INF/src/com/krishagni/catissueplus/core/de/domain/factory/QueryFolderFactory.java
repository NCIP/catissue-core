package com.krishagni.catissueplus.core.de.domain.factory;

import com.krishagni.catissueplus.core.de.domain.QueryFolder;
import com.krishagni.catissueplus.core.de.events.QueryFolderDetails;

public interface QueryFolderFactory {
	public QueryFolder createQueryFolder(QueryFolderDetails details);
}
