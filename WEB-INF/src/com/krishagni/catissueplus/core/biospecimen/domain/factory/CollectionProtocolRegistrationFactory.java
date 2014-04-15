
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;

public interface CollectionProtocolRegistrationFactory {

	public CollectionProtocolRegistration createCpr(CollectionProtocolRegistrationDetail details);

	public CollectionProtocolRegistration patchCpr(CollectionProtocolRegistration oldCpr, Map<String, Object> cprProps);
}
