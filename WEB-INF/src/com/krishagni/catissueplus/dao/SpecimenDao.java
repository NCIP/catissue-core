
package com.krishagni.catissueplus.dao;

import java.util.List;

import com.krishagni.catissueplus.events.specimens.SpecimenInfo;

public interface SpecimenDao extends Dao<SpecimenDao> {

	public List<SpecimenInfo> getSpecimensList(Long scgId);
}
