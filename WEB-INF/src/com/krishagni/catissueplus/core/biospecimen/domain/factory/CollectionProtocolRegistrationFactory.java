
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationPatchDetail;

public interface CollectionProtocolRegistrationFactory {

	public CollectionProtocolRegistration createCpr(CollectionProtocolRegistrationDetail details);

	public CollectionProtocolRegistration patchCpr(CollectionProtocolRegistration oldCpr,
			CollectionProtocolRegistrationPatchDetail detail);
}
