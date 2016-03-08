
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderStat;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderStatListCriteria;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface DistributionProtocolDao extends Dao<DistributionProtocol> {

	public DistributionProtocol getByShortTitle(String shortTitle);

	public DistributionProtocol getDistributionProtocol(String title);

	public List<DistributionProtocol> getDistributionProtocols(DpListCriteria criteria);
	
	public List<DistributionProtocol> getExpiringDps(Date fromDate, Date toDate);
	
	//
	// At present this is only returning count of specimens distributed by protocol
	// in future this would be extended to return other stats related to protocol
	//	
	public Map<Long, Integer> getSpecimensCountByDpIds(Collection<Long> dpIds);
	
	public List<DistributionOrderStat> getOrderStats(DistributionOrderStatListCriteria listCrit);

	public Map<String, Object> getDpIds(String key, Object value);
}
