
package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.administrative.repository.DpListCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface DistributionProtocolService {

	public ResponseEvent<List<DistributionProtocolDetail>> getDistributionProtocols(RequestEvent<DpListCriteria> req);
	
	public ResponseEvent<DistributionProtocolDetail> getDistributionProtocol(RequestEvent<Long> req);
	
	public ResponseEvent<DistributionProtocolDetail> createDistributionProtocol(RequestEvent<DistributionProtocolDetail> req);

	public ResponseEvent<DistributionProtocolDetail> updateDistributionProtocol(RequestEvent<DistributionProtocolDetail> req);
	
	public ResponseEvent<Map<String, List>> getDistributionProtocolDependencies(RequestEvent<Long> req);
	
	public ResponseEvent<DistributionProtocolDetail> deleteDistributionProtocol(RequestEvent<Long> req);
}
