package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.OrderItem;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.OrderItemDetail;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class DistributionOrderFactoryImpl implements DistributionOrderFactory {
	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public DistributionOrder createDistributionOrder(DistributionOrderDetail detail) {
		DistributionOrder distributionOrder = new DistributionOrder();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		distributionOrder.setId(detail.getId());
		setName(detail, distributionOrder, ose);
		setDistributionProtocol(detail, distributionOrder, ose);
		setOrderItems(detail, distributionOrder, ose);
		setRequester(detail, distributionOrder, ose);
		distributionOrder.setRequestedDate(detail.getRequestedDate());
		setDistributor(detail, distributionOrder, ose);
		setStatus(detail, distributionOrder,  ose);
		
		ose.checkAndThrow();
		return distributionOrder;
	}

	private void setDistributionProtocol(DistributionOrderDetail detail, DistributionOrder distributionOrder, OpenSpecimenException ose) {
		DistributionProtocol distributionProtocol = null;
		if (detail.getDistributionProtocol() != null) {
			Long id = detail.getDistributionProtocol().getId();
			String title = detail.getDistributionProtocol().getTitle();
			
			if (id != null) {
				distributionProtocol = daoFactory.getDistributionProtocolDao().getById(id);
			} else if (!StringUtils.isBlank(title)) {
				distributionProtocol = daoFactory.getDistributionProtocolDao().getDistributionProtocol(title);
			}
		}
		
		if (distributionProtocol == null) {
			ose.addError(DistributionProtocolErrorCode.NOT_FOUND);
			return;
		}
		
		distributionOrder.setDistributionProtocol(distributionProtocol);
	}

	private void setName(DistributionOrderDetail detail, DistributionOrder distributionOrder, OpenSpecimenException ose) {
		String name = detail.getName();
		if (StringUtils.isBlank(name)) {
			ose.addError(DistributionOrderErrorCode.NAME_REQUIRED);
			return;
		}
		
		distributionOrder.setName(name);
	}
	
	private void setRequester(DistributionOrderDetail detail, DistributionOrder distributionOrder, OpenSpecimenException ose) {
		User requester = getUser(detail.getRequester(), ose);
		if (requester == null) {
			ose.addError(UserErrorCode.NOT_FOUND);
			return;
		}
		
		distributionOrder.setRequester(requester);
	}
	
	private void setDistributor(DistributionOrderDetail detail, DistributionOrder distributionOrder, OpenSpecimenException ose) {
		User distributor = getUser(detail.getDistributor(), ose);
		if (distributor == null) {
			ose.addError(UserErrorCode.NOT_FOUND);
			return;
		}
		
		distributionOrder.setDistributor(distributor);
	}
	
	private User getUser(UserSummary userSummary, OpenSpecimenException ose) {
		if (userSummary == null) {
			return null;
		}
		
		User user = null;
		if (userSummary.getId() != null) {
			user = daoFactory.getUserDao().getById(userSummary.getId());
		} else if (!StringUtils.isBlank(userSummary.getLoginName()) && 
				   !StringUtils.isBlank(userSummary.getDomain())) {
			user = daoFactory.getUserDao().getUserByLoginNameAndDomainName(userSummary.getLoginName(), userSummary.getDomain());
		}
		
		return user;
	}
	
	private void setStatus(DistributionOrderDetail detail, DistributionOrder distributionOrder, OpenSpecimenException ose) {
		String status = detail.getStatus();
		if (!DistributionOrder.isDistributionStatusValid(status)) {
			ose.addError(DistributionOrderErrorCode.INVALID_STATUS);
			return;
		}
		
		distributionOrder.setStatus(status);
	}
	
	private void setOrderItems(DistributionOrderDetail detail, DistributionOrder distributionOrder, OpenSpecimenException ose) {
		Set<OrderItem> items = new HashSet<OrderItem>();
		
		for (OrderItemDetail oid : detail.getOrderItems()) {
			OrderItem item = getOrderItem(distributionOrder, oid, ose);
			
			if (item != null) {
				items.add(item);
			}
		}
		
		distributionOrder.setOrderItems(items);
	}
	
	private OrderItem getOrderItem(DistributionOrder distributionOrder, OrderItemDetail detail, OpenSpecimenException ose) {
		Specimen specimen = getSpecimen(detail.getSpecimen());
		if (specimen == null || !Specimen.COLLECTED.equals(specimen.getCollectionStatus())) {
			ose.addError(SpecimenErrorCode.NOT_FOUND);
		} else if (!specimen.getIsAvailable() || specimen.getAvailableQuantity() < detail.getQuantity()) {
			ose.addError(SpecimenErrorCode.INSUFFICIENT_QTY);
		} else {
			OrderItem orderItem = new OrderItem();
			orderItem.setQuantity(detail.getQuantity());
			orderItem.setSpecimen(specimen);
			orderItem.setOrder(distributionOrder);
			return orderItem;
		}
		
		return null;
	}
	
	private Specimen getSpecimen(SpecimenInfo info) {
		Specimen specimen = null;
		if (info != null) {
			if (info.getId() != null) {
				specimen = daoFactory.getSpecimenDao().getById(info.getId());
			} else if (!StringUtils.isBlank(info.getLabel())) {
				specimen = daoFactory.getSpecimenDao().getSpecimenByLabel(info.getLabel());
			}
		}
		
		return specimen;
	}
}
