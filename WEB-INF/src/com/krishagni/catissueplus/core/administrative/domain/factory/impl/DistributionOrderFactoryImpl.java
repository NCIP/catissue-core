package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.HashSet;
import java.util.List;
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
	public DistributionOrder create(DistributionOrderDetail detail) {
		
		ObjectCreationException oce = new ObjectCreationException();
		DistributionOrder distributionOrder = new DistributionOrder();
		
		setDistributionProtocol(distributionOrder, detail.getDistributionProtocolTitle(), oce);
		setName(distributionOrder, detail.getName(), oce);
		setRequester(distributionOrder, detail.getRequester(), oce);
		setDistributor(distributionOrder, detail.getDistributor(), oce);
		setActivityStatus(distributionOrder, detail.getActivityStatus(), oce);
		setOrderItems(distributionOrder, detail.getOrderItems(), oce);
		distributionOrder.setRequestedDate(detail.getRequestedDate());
		oce.checkErrorAndThrow();
		
		return distributionOrder;
	}

	private void setDistributionProtocol(DistributionOrder distributionOrder,
			String name, ObjectCreationException oce) {
		DistributionProtocol distributionProtocol = daoFactory.getDistributionProtocolDao().getDistributionProtocol(name);
		
		if (distributionProtocol == null) {
			oce.addError(DistributionErrorCode.INVALID_ATTR_VALUE, "distribution-protocol-title");
			return;
		}
		
		distributionOrder.setDistributionProtocol(distributionProtocol);
	}

	private void setName(DistributionOrder distributionOrder, String name, ObjectCreationException oce) {
		if (StringUtils.isBlank(name)) {
			oce.addError(DistributionErrorCode.MISSING_ATTR_VALUE, NAME);
			return;
		}
		
		distributionOrder.setName(name);
	}
	
	private void setRequester(DistributionOrder distributionOrder, UserSummary userSummary, ObjectCreationException oce) {
		User requester = getUser(userSummary, REQUESTER, oce);
		distributionOrder.setRequester(requester);
	}
	
	private void setDistributor(DistributionOrder distributionOrder, UserSummary userSummary, ObjectCreationException oce) {
		User distributor = getUser(userSummary, DISTRIBUTOR, oce);
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
	
	private void setActivityStatus(DistributionOrder distributionOrder, String activityStatus, ObjectCreationException oce) {
		if (StringUtils.isBlank(activityStatus)) {
			distributionOrder.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		}
		
		// TODO: validate activity status
		distributionOrder.setActivityStatus(activityStatus);
	}
	
	private void setOrderItems(DistributionOrder distributionOrder, List<OrderItemDetail> orderItems, ObjectCreationException oce) {
		Set<OrderItem> items = new HashSet<OrderItem>();
		
		for (OrderItemDetail oid : orderItems) {
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
