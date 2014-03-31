
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;

public interface SpecimenCollectionGroupFactory {

	public SpecimenCollectionGroup createScg(ScgDetail scgDetail);

}
