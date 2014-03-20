
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public interface SpecimenCollectionGroupFactory {

	public SpecimenCollectionGroup createScg(ScgDetail scgDetail, ObjectCreationException exceptionHandler);

}
