
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.events.DpListCriteria;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface DistributionProtocolDao extends Dao<DistributionProtocol> {

	public DistributionProtocol getByShortTitle(String shortTitle);

	public DistributionProtocol getDistributionProtocol(String title);

	public List<DistributionProtocol> getDistributionProtocols(DpListCriteria criteria);

}
