package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder.DistributionAction;
import com.krishagni.catissueplus.core.administrative.domain.OrderItem;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderFactory;
import com.krishagni.catissueplus.core.administrative.events.AllDistributionOrdersEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateDistributionOrderEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllDistributionOrdersEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDistributionOrderEvent;
import com.krishagni.catissueplus.core.administrative.services.DistributionOrderService;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

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
	public DistributionOrderCreatedEvent createDistribution(CreateDistributionOrderEvent req) {
		try {
			DistributionOrderDetail detail = req.getDetail();
			DistributionOrder distributionOrder = distributionFactory.create(req.getDetail());
			
			ObjectCreationException oce = new ObjectCreationException();
			ensureUniqueDOName(detail.getName(), oce);
			updateSpecimenStatus(distributionOrder, detail);
			oce.checkErrorAndThrow();
			
			daoFactory.getDistributionOrderDao().saveOrUpdate(distributionOrder);
			return DistributionOrderCreatedEvent.ok(DistributionOrderDetail.from(distributionOrder));
		} catch (ObjectCreationException oce) {
			return DistributionOrderCreatedEvent.badRequest(oce);
		} catch (Exception e) {
			return DistributionOrderCreatedEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public DistributionOrderUpdatedEvent updateDistribution(UpdateDistributionOrderEvent req) {
		try {
			DistributionOrderDetail detail = req.getDetail();
			if (detail == null || detail.getId() == null) {
				return DistributionOrderUpdatedEvent.badRequest(DistributionErrorCode.MISSING_ATTR_VALUE, "distribution-id");
			}
			
			DistributionOrder existing = daoFactory.getDistributionOrderDao().getById(detail.getId());
			if (existing == null) {
				return DistributionOrderUpdatedEvent.notFound(detail.getId());
			}
			
			DistributionOrder distributionOrder = distributionFactory.create(req.getDetail());
			ObjectCreationException oce = new ObjectCreationException();
			validateDOName(existing.getName(), distributionOrder.getName(), oce);
			oce.checkErrorAndThrow();
			existing.update(distributionOrder);
			
			daoFactory.getDistributionOrderDao().saveOrUpdate(existing);
			return DistributionOrderUpdatedEvent.ok(DistributionOrderDetail.from(distributionOrder));
		} catch (ObjectCreationException oce) {
			return DistributionOrderUpdatedEvent.badRequest(oce);
		} catch (Exception e) {
			return DistributionOrderUpdatedEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public AllDistributionOrdersEvent getAllDistributionOrders(ReqAllDistributionOrdersEvent req) {
		try {
			return AllDistributionOrdersEvent.ok(DistributionOrderDetail
					.from(daoFactory.getDistributionOrderDao()
							.getDistributionOrders(req.getStartAt(), req.getMaxResults())));
		} catch (Exception e) {
			return AllDistributionOrdersEvent.serverError(e);
		}
	}
	
	private void updateSpecimenStatus(DistributionOrder distributionOrder,	DistributionOrderDetail detail) {
		for (OrderItem orderItem : distributionOrder.getOrderItems()) {
			Specimen specimen = orderItem.getSpecimen();

			Double avbQuantity = specimen.getAvailableQuantity() - orderItem.getQuantity();
			specimen.setAvailableQuantity(avbQuantity);
			
			if (avbQuantity == 0) {
				specimen.setIsAvailable(false);
				virutalizeSpecimen(specimen);
			}
			
			insertDistributedEvent(orderItem);
			
			if (DistributionAction.DISTRIBUTED_AND_CLOSED.equals(detail.getDistributionAction())) {
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

	private void ensureUniqueDOName(String name, ObjectCreationException oce) {
		if (daoFactory.getDistributionOrderDao().getDistributionOrder(name) != null) {
			oce.addError(DistributionErrorCode.DUPLICATE_DISTRI_NAME, "distribution-name");
		}
	}
	
	private void validateDOName(String oldName, String newName, ObjectCreationException oce) {
		if (!oldName.equals(newName)) {
			ensureUniqueDOName(newName, oce);
		}
	}
}
