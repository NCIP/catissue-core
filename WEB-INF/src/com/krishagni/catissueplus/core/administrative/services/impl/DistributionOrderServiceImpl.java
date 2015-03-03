//package com.krishagni.catissueplus.core.administrative.services.impl;
//
//import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
//import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder.DistributionAction;
//import com.krishagni.catissueplus.core.administrative.domain.OrderItem;
//import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionErrorCode;
//import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderFactory;
//import com.krishagni.catissueplus.core.administrative.events.DistributionOrderEvent;
//import com.krishagni.catissueplus.core.administrative.events.DistributionOrdersEvent;
//import com.krishagni.catissueplus.core.administrative.events.CreateDistributionOrderEvent;
//import com.krishagni.catissueplus.core.administrative.events.DistributionOrderCreatedEvent;
//import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
//import com.krishagni.catissueplus.core.administrative.events.DistributionOrderUpdatedEvent;
//import com.krishagni.catissueplus.core.administrative.events.ReqDistributionOrderEvent;
//import com.krishagni.catissueplus.core.administrative.events.ReqDistributionOrdersEvent;
//import com.krishagni.catissueplus.core.administrative.events.UpdateDistributionOrderEvent;
//import com.krishagni.catissueplus.core.administrative.repository.DistributionListCriteria;
//import com.krishagni.catissueplus.core.administrative.services.DistributionOrderService;
//import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
//import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
//import com.krishagni.catissueplus.core.common.PlusTransactional;
//import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
//
//public class DistributionOrderServiceImpl implements DistributionOrderService {
//	private DaoFactory daoFactory;
//
//	private DistributionOrderFactory distributionFactory;
//
//	public void setDaoFactory(DaoFactory daoFactory) {
//		this.daoFactory = daoFactory;
//	}
//
//	public void setDistributionFactory(DistributionOrderFactory distributionFactory) {
//		this.distributionFactory = distributionFactory;
//	}
//	
//	@Override
//	@PlusTransactional
//	public DistributionOrdersEvent getDistributionOrders(ReqDistributionOrdersEvent req) {
//		try {
//			DistributionListCriteria criteria = new DistributionListCriteria()
//				.query(req.getSearchString())	
//				.startAt(req.getStartAt())
//				.maxResults(req.getMaxResults());
//			
//			return DistributionOrdersEvent.ok(DistributionOrderDetail.from(daoFactory.getDistributionOrderDao()
//					.getDistributionOrders(criteria)));
//		} catch (Exception e) {
//			return DistributionOrdersEvent.serverError(e);
//		}
//	}
//	
//	@Override
//	@PlusTransactional
//	public DistributionOrderEvent getDistributionOrder(ReqDistributionOrderEvent req) {
//		try {
//			Long distributionId = req.getDistributionId();
//			DistributionOrder order = daoFactory.getDistributionOrderDao().getById(distributionId);
//			if (order == null) {
//				return DistributionOrderEvent.notFound(distributionId);
//			}
//			
//			return DistributionOrderEvent.ok(DistributionOrderDetail.from(order));
//		} catch (Exception e) {
//			return DistributionOrderEvent.serverError(e);
//		}
//	}
//
//	@Override
//	@PlusTransactional
//	public DistributionOrderCreatedEvent createDistribution(CreateDistributionOrderEvent req) {
//		try {
//			DistributionOrderDetail detail = req.getOrder();
//			DistributionOrder distributionOrder = distributionFactory.createDistributionOrder(req.getOrder());
//			ensureUniqueConstraints(distributionOrder);
//			
//			updateSpecimenStatus(distributionOrder, detail);
//			daoFactory.getDistributionOrderDao().saveOrUpdate(distributionOrder);
//			return DistributionOrderCreatedEvent.ok(DistributionOrderDetail.from(distributionOrder));
//		} catch (ObjectCreationException oce) {
//			return DistributionOrderCreatedEvent.badRequest(oce);
//		} catch (Exception e) {
//			return DistributionOrderCreatedEvent.serverError(e);
//		}
//	}
//	
//	@Override
//	@PlusTransactional
//	public DistributionOrderUpdatedEvent updateDistribution(UpdateDistributionOrderEvent req) {
//		try {
//			Long distributionId = req.getDistributionId();
//			DistributionOrder existing = daoFactory.getDistributionOrderDao().getById(distributionId);
//			if (existing == null) {
//				return DistributionOrderUpdatedEvent.notFound(distributionId);
//			}
//			
//			DistributionOrder distributionOrder = distributionFactory.createDistributionOrder(req.getOrder());
//			ensureUniqueConstraints(distributionOrder);
//
//			existing.update(distributionOrder);
//			daoFactory.getDistributionOrderDao().saveOrUpdate(existing);
//			return DistributionOrderUpdatedEvent.ok(DistributionOrderDetail.from(distributionOrder));
//		} catch (ObjectCreationException oce) {
//			return DistributionOrderUpdatedEvent.badRequest(oce);
//		} catch (Exception e) {
//			return DistributionOrderUpdatedEvent.serverError(e);
//		}
//	}
//	
//	private void updateSpecimenStatus(DistributionOrder distributionOrder,	DistributionOrderDetail detail) {
//		for (OrderItem orderItem : distributionOrder.getOrderItems()) {
//			Specimen specimen = orderItem.getSpecimen();
//			Double avbQuantity = specimen.getAvailableQuantity() - orderItem.getQuantity();
//			
//			specimen.setAvailableQuantity(avbQuantity);
//			if (avbQuantity == 0) {
//				specimen.setIsAvailable(false);
//				virutalizeSpecimen(specimen);
//			}
//			
//			insertDistributedEvent(orderItem);
//			if (DistributionAction.DISTRIBUTED_AND_CLOSED.equals(detail.getDistributionAction())) {
//				specimen.setIsAvailable(false);
//				specimen.setAvailableQuantity(0.0D);
//				insertDisposalEvent(orderItem);
//				virutalizeSpecimen(specimen);
//			} 
//			daoFactory.getSpecimenDao().saveOrUpdate(specimen);			
//		}
//	}
//	
//	private void virutalizeSpecimen(Specimen specimen) {
//		// TODO: virutalize the specimen once the link b/w specimen and StorageContainerPosition is fromed
//	}
//
//	private void insertDisposalEvent(OrderItem orderItem) {
//		// TODO: make proper API call when API's are ready
//	}
//
//	private void insertDistributedEvent(OrderItem orderItem) {
//		// TODO: make proper API call when API's are ready
//	}
//	
//	private void ensureUniqueConstraints(DistributionOrder distribution) {
//		ObjectCreationException oce = new ObjectCreationException();
//		
//		if(!isUniqueName(distribution)) {
//			oce.addError(DistributionErrorCode.NOT_UNIQUE, "name");
//		}
//		
//		oce.checkErrorAndThrow();
//	}
//	
//	private boolean isUniqueName(DistributionOrder distribution) {
//		DistributionOrder existing = daoFactory.getDistributionOrderDao().getDistributionOrder(distribution.getName());
//		if (existing == null) {
//			return true; // no do by this name
//		} else if (distribution.getId() == null) {
//			return false; // a different do by this name exists 
//		} else if (existing.getId().equals(distribution.getId())) {
//			return true; // same do
//		} else {
//			return false;
//		}
//	}
//}
