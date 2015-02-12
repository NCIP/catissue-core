
package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.ListDpCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface DistributionProtocolService {
	public ResponseEvent<DistributionProtocolDetail> getDistributionProtocol(RequestEvent<DistributionProtocolQueryCriteria> req);

	public ResponseEvent<List<DistributionProtocolDetail>> getDistributionProtocols(RequestEvent<ListDpCriteria> req);

	public ResponseEvent<DistributionProtocolDetail> createDistributionProtocol(RequestEvent<DistributionProtocolDetail> req);

	public ResponseEvent<DistributionProtocolDetail> updateDistributionProtocol(RequestEvent<DistributionProtocolDetail> req);

	public ResponseEvent<DistributionProtocolDetail> deleteDistributionProtocol(RequestEvent<DistributionProtocolQueryCriteria> req);
}
