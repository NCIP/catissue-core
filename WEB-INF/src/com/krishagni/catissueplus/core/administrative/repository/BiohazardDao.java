
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Biohazard;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface BiohazardDao extends Dao<Biohazard> {

	public Biohazard getBiohazard(String name);

	public Boolean isUniqueBiohazardName(String biohazardName);

	public Biohazard getBiohazard(long id);

	public List<Biohazard> getAllBiohazards(int maxResults);

}
