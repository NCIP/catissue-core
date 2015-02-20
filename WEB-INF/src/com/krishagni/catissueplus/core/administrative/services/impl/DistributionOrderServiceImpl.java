package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderFactory;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderListCriteria;
import com.krishagni.catissueplus.core.administrative.services.DistributionOrderService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DistributionOrderServiceImpl implements DistributionOrderService {
	private DaoFactory daoFactory;

	private DistributionOrderFactory distributionFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setDistributionFactory(DistributionOrderFactory distributionFactory) {
		this.distributionFactory = distributionFactory;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<DistributionOrderDetail>> getDistributionOrders(RequestEvent<DistributionOrderListCriteria> req) {
		try {
			return ResponseEvent.response(DistributionOrderDetail.from(daoFactory.getDistributionOrderDao()
					.getDistributionOrders(req.getPayload())));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<DistributionOrderDetail> getDistributionOrder(RequestEvent<Long> req) {
		try {
			Long distributionId = req.getPayload();
			DistributionOrder order = daoFactory.getDistributionOrderDao().getDistributionOrder(distributionId);
			if (order == null) {
				return ResponseEvent.userError(DistributionOrderErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(DistributionOrderDetail.from(order));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionOrderDetail> createDistribution(RequestEvent<DistributionOrderDetail> req) {
		try {
			DistributionOrderDetail detail = req.getPayload();
			DistributionOrder distributionOrder = distributionFactory.createDistributionOrder(detail);
			ensureUniqueConstraints(distributionOrder);
			
			distributeSpecimens(distributionOrder, null);
			daoFactory.getDistributionOrderDao().saveOrUpdate(distributionOrder);
			return ResponseEvent.response(DistributionOrderDetail.from(distributionOrder));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<DistributionOrderDetail> updateDistribution(RequestEvent<DistributionOrderDetail> req) {
		try {
			DistributionOrderDetail detail = req.getPayload();
			Long distributionId = detail.getId();
			DistributionOrder existing = daoFactory.getDistributionOrderDao().getDistributionOrder(distributionId);
			if (existing == null) {
				return ResponseEvent.userError(DistributionOrderErrorCode.NOT_FOUND);
			}
			
			DistributionOrder distributionOrder = distributionFactory.createDistributionOrder(detail);
			ensureUniqueConstraints(distributionOrder);

			distributeSpecimens(distributionOrder, existing.getStatus());
			existing.update(distributionOrder);
			daoFactory.getDistributionOrderDao().saveOrUpdate(existing);
			return ResponseEvent.response(DistributionOrderDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void distributeSpecimens(DistributionOrder distributionOrder, String previousState) {
		if (distributionOrder.isPending() || DistributionOrder.DISTRIBUTED.equals(previousState) ||
				DistributionOrder.DISTRIBUTED_AND_CLOSED.equals(previousState)) {
			/*
			 *  Under two cases specimen's cant be distributed
			 *  1. If order is pending
			 *  2. If specimen have been distributed already
			 */
			return;
		}
		
		boolean closeAfterDistribution = distributionOrder.closeAfterDistribution();
		for (DistributionOrderItem orderItem : distributionOrder.getOrderItems()) {
			orderItem.distribute(closeAfterDistribution);
		}
	}
		
	private void ensureUniqueConstraints(DistributionOrder distribution) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		if(!isUniqueName(distribution)) {
			ose.addError(DistributionOrderErrorCode.DUP_NAME);
		}
		
		ose.checkAndThrow();
	}
	
	private boolean isUniqueName(DistributionOrder distribution) {
		DistributionOrder existing = daoFactory.getDistributionOrderDao().getDistributionOrder(distribution.getName());
		if (existing == null) {
			return true; // no do by this name
		} else if (distribution.getId() == null) {
			return false; // a different do by this name exists 
		} else if (existing.getId().equals(distribution.getId())) {
			return true; // same do
		} else {
			return false;
		}
	}
}
