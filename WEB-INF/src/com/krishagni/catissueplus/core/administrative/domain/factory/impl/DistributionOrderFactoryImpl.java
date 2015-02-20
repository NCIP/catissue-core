package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderItemDetail;
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
		setDistributionDate(detail, distributionOrder, ose);
		setDistributor(detail, distributionOrder, ose);
		setStatus(detail, distributionOrder,  ose);
		
		ose.checkAndThrow();
		return distributionOrder;
	}

	private void setDistributionProtocol(DistributionOrderDetail detail, DistributionOrder distributionOrder, OpenSpecimenException ose) {
		DistributionProtocol distributionProtocol = null;
		Long distributionProtocolId = null;
		String distributionProtocolTitle = null;
		if (detail.getDistributionProtocol() != null) {
			distributionProtocolId = detail.getDistributionProtocol().getId();
			distributionProtocolTitle = detail.getDistributionProtocol().getTitle();
		}
		
		if (distributionProtocolId != null) {
			distributionProtocol = daoFactory.getDistributionProtocolDao().getById(distributionProtocolId);
		} else if (!StringUtils.isBlank(distributionProtocolTitle)) {
			distributionProtocol = daoFactory.getDistributionProtocolDao().getDistributionProtocol(distributionProtocolTitle);
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
		User requester = getUser(detail.getRequester());
		if (requester == null) {
			ose.addError(UserErrorCode.NOT_FOUND);
			return;
		}
		
		distributionOrder.setRequester(requester);
	}
	
	private void setDistributor(DistributionOrderDetail detail, DistributionOrder distributionOrder, OpenSpecimenException ose) {
		User distributor = getUser(detail.getDistributor());
		if (distributor == null) {
			ose.addError(UserErrorCode.NOT_FOUND);
			return;
		}
		
		distributionOrder.setDistributor(distributor);
	}
	
	private User getUser(UserSummary userSummary) {
		User user = null;
		Long userId = null;
		String loginName = null;
		String domain = null;

		if (userSummary != null) {
			userId = userSummary.getId();
			loginName = userSummary.getLoginName();
			domain = userSummary.getDomain();
		}
		
		if (userId != null) {
			user = daoFactory.getUserDao().getById(userSummary.getId());
		} else if (!StringUtils.isBlank(loginName) && !StringUtils.isBlank(domain)) {
			user = daoFactory.getUserDao().getUserByLoginNameAndDomainName(userSummary.getLoginName(), userSummary.getDomain());
		}
		
		return user;
	}
	
	private void setDistributionDate(DistributionOrderDetail detail, DistributionOrder distributionOrder, OpenSpecimenException ose) {
		Date creationDate = detail.getCreationDate();
		if (creationDate == null) {
			creationDate = Calendar.getInstance().getTime();
		} else if (creationDate.after(Calendar.getInstance().getTime())) {
			ose.addError(DistributionOrderErrorCode.INVALID_CREATION_DATE);
			return;
		}
		
		distributionOrder.setCreationDate(creationDate);
		if (DistributionOrder.PENDING.equals(detail.getExecutionDate())) {
			return;
		}
		
		Date executionDate = detail.getExecutionDate();
		if (executionDate == null) {
			executionDate = Calendar.getInstance().getTime();
		} else if (executionDate.after(Calendar.getInstance().getTime())) {
			ose.addError(DistributionOrderErrorCode.INVALID_EXECUTION_DATE);
			return;
		}
		
		distributionOrder.setExecutionDate(executionDate);
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
		Set<DistributionOrderItem> items = new HashSet<DistributionOrderItem>();
		
		for (DistributionOrderItemDetail oid : detail.getOrderItems()) {
			DistributionOrderItem item = getOrderItem(oid, distributionOrder, ose);
			
			if (item != null) {
				items.add(item);
			}
		}
		
		distributionOrder.setOrderItems(items);
	}
	
	private DistributionOrderItem getOrderItem(DistributionOrderItemDetail detail, DistributionOrder distributionOrder, OpenSpecimenException ose) {
		Specimen specimen = getSpecimen(detail.getSpecimen());
		if (specimen == null) {
			ose.addError(SpecimenErrorCode.NOT_FOUND);
			return null;
		} 

		if (detail.getQuantity() == null || detail.getQuantity() <= 0) {
			ose.addError(DistributionOrderErrorCode.INVALID_DISTRIB_QUANTITY);
			return null;
		}
		
		DistributionOrderItem orderItem = new DistributionOrderItem();
		orderItem.setQuantity(detail.getQuantity());
		orderItem.setSpecimen(specimen);
		orderItem.setOrder(distributionOrder);
		return orderItem;
	}
	
	private Specimen getSpecimen(SpecimenInfo info) {
		Specimen specimen = null;
		Long specimenId = null;
		String specimenLabel = null;
		
		if (info != null) {
			specimenId = info.getId();
			specimenLabel = info.getLabel();
		}
		
		if (specimenId != null) {
			specimen = daoFactory.getSpecimenDao().getById(info.getId());
		} else if (!StringUtils.isBlank(specimenLabel)) {
			specimen = daoFactory.getSpecimenDao().getSpecimenByLabel(info.getLabel());
		}
		
		return specimen;
	}
}
