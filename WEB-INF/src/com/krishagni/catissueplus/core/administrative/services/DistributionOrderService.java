package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderItemDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderListCriteria;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderSummary;
import com.krishagni.catissueplus.core.administrative.events.ReturnedSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimensQueryCriteria;
import com.krishagni.catissueplus.core.common.EntityCrudListener;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.QueryDataExportResult;

public interface DistributionOrderService {
	public ResponseEvent<List<DistributionOrderSummary>> getOrders(RequestEvent<DistributionOrderListCriteria> req);

	public ResponseEvent<Long> getOrdersCount(RequestEvent<DistributionOrderListCriteria> req);

	public ResponseEvent<DistributionOrderDetail> getOrder(RequestEvent<Long> req);
	
	public ResponseEvent<DistributionOrderDetail> createOrder(RequestEvent<DistributionOrderDetail> req);
	
	public ResponseEvent<DistributionOrderDetail> updateOrder(RequestEvent<DistributionOrderDetail> req);
	
	public ResponseEvent<QueryDataExportResult> exportReport(RequestEvent<Long> req);
	
	public ResponseEvent<List<SpecimenInfo>> getSpecimens(RequestEvent<VisitSpecimensQueryCriteria> req);

	public ResponseEvent<List<DistributionOrderItemDetail>> getDistributedSpecimens(RequestEvent<List<String>> req);

	public ResponseEvent<List<SpecimenInfo>> returnSpecimens(RequestEvent<List<ReturnedSpecimenDetail>> req);


	//
	// Internal APIs for use by plugins and service extenders...
	//
	public void addListener(EntityCrudListener<DistributionOrderDetail, DistributionOrder> listener);
}
