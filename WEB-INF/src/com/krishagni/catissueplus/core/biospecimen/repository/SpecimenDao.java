
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SpecimenDao extends Dao<Specimen> {

	public List<SpecimenInfo> getSpecimensList(Long scgId);

	public Long getScgId(Long specimenId);

	public Specimen getSpecimen(Long id);
}
