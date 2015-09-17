package com.krishagni.catissueplus.core.de.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseExtensionEntity;
import com.krishagni.catissueplus.core.de.domain.DeObject;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;

public interface ExtensionFactory {
	public DeObject createExtension(ExtensionDetail detail, BaseExtensionEntity entity);
}
