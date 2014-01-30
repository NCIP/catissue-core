
package com.krishagni.catissueplus.dao;

import java.util.List;

import com.krishagni.catissueplus.events.specimens.SpecimenInfo;

public interface SpecimenCollectionGroupDao extends Dao<SpecimenCollectionGroupDao> {

	public List<SpecimenInfo> getSpecimensList(Long scgId);
}
