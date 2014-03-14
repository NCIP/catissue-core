
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public interface CollectionProtocolRegistrationFactory {

	public CollectionProtocolRegistration createCpr(CollectionProtocolRegistrationDetail details, ObjectCreationException exception);
}
