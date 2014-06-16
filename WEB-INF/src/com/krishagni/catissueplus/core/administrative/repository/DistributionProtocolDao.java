
package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface DistributionProtocolDao extends Dao<DistributionProtocol> {

	public DistributionProtocol getDistributionProtocol(Long id);

	public Boolean isUniqueTitle(String title);

	public Boolean isUniqueShortTitle(String shortTitle);

}
