package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.OrderItem;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderFactory;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.OrderItemDetail;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.Status;

public class DistributionOrderFactoryImpl implements DistributionOrderFactory {
	private final String NAME = "name";
	
	private final String REQUESTER = "requester";
	
	private final String DISTRIBUTOR = "distributor";
	
	private final String SPECIMEN_ID = "specimen-id";
	
	private final String REQUESTED_QUANTITY = "requested-quantity";
	
	private final String SPECIMEN = "specimen";
	
	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public DistributionOrder createDistributionOrder(DistributionOrderDetail detail) {
		DistributionOrder distributionOrder = new DistributionOrder();
		ObjectCreationException oce = new ObjectCreationException();
		
		distributionOrder.setId(detail.getId());
		setName(detail, distributionOrder, oce);
		setDistributionProtocol(detail, distributionOrder, oce);
		setOrderItems(detail, distributionOrder, oce);
		setRequester(detail, distributionOrder, oce);
		distributionOrder.setRequestedDate(detail.getRequestedDate());
		setDistributor(detail, distributionOrder, oce);
		setActivityStatus(detail, distributionOrder,  oce);
		
		oce.checkErrorAndThrow();
		return distributionOrder;
	}

	private void setDistributionProtocol(DistributionOrderDetail detail, DistributionOrder distributionOrder, ObjectCreationException oce) {
		DistributionProtocol distributionProtocol = daoFactory.getDistributionProtocolDao().getById(detail.getDistributionProtocolId());
		if (distributionProtocol == null) {
			oce.addError(DistributionErrorCode.INVALID_ATTR_VALUE, "distribution-protocol-id");
			return;
		}
		
		distributionOrder.setDistributionProtocol(distributionProtocol);
	}

	private void setName(DistributionOrderDetail detail, DistributionOrder distributionOrder, ObjectCreationException oce) {
		String name = detail.getName();
		if (StringUtils.isBlank(name)) {
			oce.addError(DistributionErrorCode.MISSING_ATTR_VALUE, NAME);
			return;
		}
		
		distributionOrder.setName(name);
	}
	
	private void setRequester(DistributionOrderDetail detail, DistributionOrder distributionOrder, ObjectCreationException oce) {
		User requester = getUser(detail.getRequester(), REQUESTER, oce);
		distributionOrder.setRequester(requester);
	}
	
	private void setDistributor(DistributionOrderDetail detail, DistributionOrder distributionOrder, ObjectCreationException oce) {
		User distributor = getUser(detail.getDistributor(), DISTRIBUTOR, oce);
		distributionOrder.setDistributor(distributor);
	}
	
	private User getUser(UserSummary userSummary, String field, ObjectCreationException oce) {
		if (userSummary == null || userSummary.getId() == null) {
			oce.addError(DistributionErrorCode.MISSING_ATTR_VALUE, field);
			return null;
		}
		
		User user = daoFactory.getUserDao().getById(userSummary.getId());
		if (user == null) {
			oce.addError(DistributionErrorCode.INVALID_ATTR_VALUE, field);
			return null;
		}
		
		return user;
	}
	
	private void setActivityStatus(DistributionOrderDetail detail, DistributionOrder distributionOrder, ObjectCreationException oce) {
		String activityStatus = detail.getActivityStatus();
		if (StringUtils.isBlank(activityStatus)) {
			distributionOrder.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		}
		
		// TODO: validate activity status
		distributionOrder.setActivityStatus(activityStatus);
	}
	
	private void setOrderItems(DistributionOrderDetail detail, DistributionOrder distributionOrder, ObjectCreationException oce) {
		Set<OrderItem> items = new HashSet<OrderItem>();
		
		for (OrderItemDetail oid : detail.getOrderItems()) {
			OrderItem item = getOrderItem(distributionOrder, oid, oce);
			
			if (item != null) {
				items.add(item);
			}
		}
		
		distributionOrder.setOrderItems(items);
	}
	
	private OrderItem getOrderItem(DistributionOrder distributionOrder, OrderItemDetail detail, ObjectCreationException oce) {
		Specimen specimen = daoFactory.getSpecimenDao().getById(detail.getSpecimenId());
		
		if (specimen == null) {
			oce.addError(DistributionErrorCode.INVALID_ATTR_VALUE, SPECIMEN_ID);
		} else if (!"Collected".equals(specimen.getCollectionStatus())) { 
			oce.addError(DistributionErrorCode.SPECIMEN_NOT_COLLECTED, SPECIMEN);
		} else if (!specimen.getIsAvailable() || specimen.getAvailableQuantity() < detail.getQuantity()) {
			oce.addError(DistributionErrorCode.INVALID_ATTR_VALUE, REQUESTED_QUANTITY );
		} else {
			OrderItem orderItem = new OrderItem();
			orderItem.setQuantity(detail.getQuantity());
			orderItem.setSpecimen(specimen);
			orderItem.setOrder(distributionOrder);
			return orderItem;
		}
		
		return null;
	}
}
