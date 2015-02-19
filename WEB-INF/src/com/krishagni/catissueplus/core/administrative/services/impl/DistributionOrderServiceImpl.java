package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.OrderItem;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderFactory;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderListCriteria;
import com.krishagni.catissueplus.core.administrative.services.DistributionOrderService;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
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
			
			distributeSpecimens(distributionOrder, detail);
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

			if (DistributionOrder.PENDING.equals(existing.getStatus())) {
				distributeSpecimens(distributionOrder, detail);
			}
			existing.update(distributionOrder);
			daoFactory.getDistributionOrderDao().saveOrUpdate(existing);
			return ResponseEvent.response(DistributionOrderDetail.from(distributionOrder));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void distributeSpecimens(DistributionOrder distributionOrder,	DistributionOrderDetail detail) {
		if (DistributionOrder.PENDING.equals(detail.getStatus())) {
			/*
			 *  If the order is in pending state it should not be distributed
			 */
			return;
		}
		
		for (OrderItem orderItem : distributionOrder.getOrderItems()) {
			Specimen specimen = orderItem.getSpecimen();
			Double avbQuantity = specimen.getAvailableQuantity() - orderItem.getQuantity();
			
			specimen.setAvailableQuantity(avbQuantity);
			if (avbQuantity == 0) {
				specimen.setIsAvailable(false);
				virutalizeSpecimen(specimen);
			}
			
			insertDistributedEvent(orderItem);
			if (DistributionOrder.DISTRIBUTED_AND_CLOSED.equals(detail.getStatus())) {
				specimen.setIsAvailable(false);
				specimen.setAvailableQuantity(0.0D);
				insertDisposalEvent(orderItem);
				virutalizeSpecimen(specimen);
			} 
			daoFactory.getSpecimenDao().saveOrUpdate(specimen);			
		}
	}
	
	private void virutalizeSpecimen(Specimen specimen) {
		// TODO: virutalize the specimen once the link b/w specimen and StorageContainerPosition is fromed
	}

	private void insertDisposalEvent(OrderItem orderItem) {
		// TODO: make proper API call when API's are ready
	}

	private void insertDistributedEvent(OrderItem orderItem) {
		// TODO: make proper API call when API's are ready
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
