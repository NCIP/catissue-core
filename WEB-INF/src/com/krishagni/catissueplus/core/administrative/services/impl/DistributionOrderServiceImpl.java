package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder.Status;
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
			List<DistributionOrder> orders = daoFactory.getDistributionOrderDao().getDistributionOrders(req.getPayload());
			return ResponseEvent.response(DistributionOrderDetail.from(orders));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<DistributionOrderDetail> getDistributionOrder(RequestEvent<Long> req) {
		try {
			Long distributionId = req.getPayload();
			DistributionOrder order = daoFactory.getDistributionOrderDao().getById(distributionId);
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
			DistributionOrder distributionOrder = distributionFactory.createDistributionOrder(detail, Status.PENDING);
			ensureUniqueConstraints(distributionOrder);
			
			Status inputStatus = Status.valueOf(detail.getStatus());
			if (inputStatus == Status.DISTRIBUTED) {
				distributionOrder.distribute();
			} else if (inputStatus == Status.DISTRIBUTED_AND_CLOSED) {
				distributionOrder.distributeAndClose();
			}
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
			DistributionOrder existing = daoFactory.getDistributionOrderDao().getById(distributionId);
			if (existing == null) {
				return ResponseEvent.userError(DistributionOrderErrorCode.NOT_FOUND);
			}
			
			DistributionOrder distributionOrder = distributionFactory.createDistributionOrder(detail, null);
			if (!existing.getName().equals(distributionOrder.getName())) {
				ensureUniqueConstraints(distributionOrder);
			}

			existing.update(distributionOrder);
			daoFactory.getDistributionOrderDao().saveOrUpdate(existing);
			return ResponseEvent.response(DistributionOrderDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void ensureUniqueConstraints(DistributionOrder distribution) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		if(daoFactory.getDistributionOrderDao().getDistributionOrder(distribution.getName()) != null) {
			ose.addError(DistributionOrderErrorCode.DUP_NAME);
		}
		
		ose.checkAndThrow();
	}
}
