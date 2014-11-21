package com.krishagni.catissueplus.bulkoperator.common;

import com.krishagni.catissueplus.bulkoperator.events.ImportObjectEvent;
import com.krishagni.catissueplus.bulkoperator.events.ObjectImportedEvent;

public interface ObjectImporter {
	public ObjectImportedEvent importObject(ImportObjectEvent req);

}
