
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.SpecimenRequirement;

public interface SpecimenCollectionGroupDao extends Dao<SpecimenCollectionGroup> {

	public List<Specimen> getSpecimensList(Long scgId);

	public boolean isNameUnique(String name);

	public boolean isBarcodeUnique(String barcode);

	public SpecimenCollectionGroup getscg(Long scgId);

	public List<SpecimenCollectionGroup> getAllScgs(int startAt, int maxRecords, String... searchString);

	public Long getScgsCount(String... searchString);

	public List<SpecimenRequirement> getSpecimenRequirments(Long scgId);

}
