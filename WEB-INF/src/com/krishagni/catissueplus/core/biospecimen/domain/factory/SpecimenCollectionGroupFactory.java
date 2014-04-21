
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;

public interface SpecimenCollectionGroupFactory {

	public SpecimenCollectionGroup createScg(ScgDetail scgDetail);

	public SpecimenCollectionGroup patch(SpecimenCollectionGroup oldScg, Map<String, Object> scgProps);

}
