package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.SpecimenRequest;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SpecimenRequestErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderItemDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenResolver;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.NumUtil;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

public class DistributionOrderFactoryImpl implements DistributionOrderFactory {
	private DaoFactory daoFactory;

	private SpecimenResolver specimenResolver;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSpecimenResolver(SpecimenResolver specimenResolver) {
		this.specimenResolver = specimenResolver;
	}

	@Override
	public DistributionOrder createDistributionOrder(DistributionOrderDetail detail, DistributionOrder.Status status) {
		DistributionOrder order = new DistributionOrder();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		order.setId(detail.getId());
		setName(detail, order, ose);
		setRequest(detail, order, ose);
		setDistributionProtocol(detail, order, ose);		
		setRequesterAndReceivingSite(detail, order, ose);
		setDistributionDate(detail, order, ose);
		setDistributor(detail, order, ose);
		setOrderItems(detail, order, ose);
		setStatus(detail, status, order,  ose);
		setActivityStatus(detail, order, ose);
		setTrackingUrl(detail, order, ose);
		setComments(detail, order, ose);
		
		ose.checkAndThrow();
		return order;
	}
	
	private void setName(DistributionOrderDetail detail, DistributionOrder order, OpenSpecimenException ose) {
		String name = detail.getName();
		if (StringUtils.isBlank(name)) {
			ose.addError(DistributionOrderErrorCode.NAME_REQUIRED);
			return;
		}
		
		order.setName(name);
	}

	private void setRequest(DistributionOrderDetail detail, DistributionOrder order, OpenSpecimenException ose) {
		if (detail.getRequest() == null || detail.getRequest().getId() == null) {
			return;
		}

		Long requestId = detail.getRequest().getId();
		SpecimenRequest request = daoFactory.getSpecimenRequestDao().getById(requestId);
		if (request == null) {
			ose.addError(SpecimenRequestErrorCode.NOT_FOUND, requestId);
			return;
		}

		order.setRequest(request);
	}

	private void setDistributionProtocol(DistributionOrderDetail detail, DistributionOrder order, OpenSpecimenException ose) {
		DistributionProtocolDetail dpDetail = detail.getDistributionProtocol();
		Long dpId = dpDetail != null ? dpDetail.getId() : null;
		String dpShortTitle = dpDetail != null ? dpDetail.getShortTitle() : null;
		
		if (dpId == null && StringUtils.isBlank(dpShortTitle)) {
			ose.addError(DistributionOrderErrorCode.DP_REQUIRED);
			return;
		}
		
		DistributionProtocol dp = null;
		if (dpId != null) {
			dp = daoFactory.getDistributionProtocolDao().getById(dpId);
		} else {
			dp = daoFactory.getDistributionProtocolDao().getByShortTitle(dpShortTitle);
		}
		
		if (dp == null) {
			ose.addError(DistributionProtocolErrorCode.NOT_FOUND);
			return;
		}

		SpecimenRequest request = order.getRequest();
		if (request != null && !request.getInstitute().equals(dp.getInstitute())) {
			ose.addError(DistributionOrderErrorCode.INVALID_DP_FOR_REQ, dp.getShortTitle(), request.getId());
			return;
		}

		Date dpEndDate = Utility.chopTime(dp.getEndDate());
		Date today = Utility.chopTime(Calendar.getInstance().getTime());
		if (dpEndDate != null && today.after(dpEndDate)) {
			ose.addError(DistributionProtocolErrorCode.EXPIRED, dp.getShortTitle());
			return;
		}

		order.setDistributionProtocol(dp);
	}

	private void setRequesterAndReceivingSite(DistributionOrderDetail detail, DistributionOrder order, OpenSpecimenException ose) {
		SpecimenRequest request = order.getRequest();
		User requestor = null;
		if (request != null) {
			requestor = request.getRequestor();
		} else if (detail.getRequester() != null) {
			requestor = getUser(detail.getRequester(), null, ose, DistributionOrderErrorCode.REQUESTER_NOT_FOUND);
			if (requestor == null) {
				return;
			}
		}

		if (requestor == null) {
			ose.addError(DistributionOrderErrorCode.REQUESTER_REQ);
			return;
		}

		order.setRequester(requestor);
		
		Long siteId = detail.getSiteId();
		String siteName = detail.getSiteName();		
		if (siteId == null && StringUtils.isBlank(siteName)) {
			return;
		}

		Site site = null;
		if (siteId != null) {
			site = daoFactory.getSiteDao().getById(siteId);
		} else {
			site = daoFactory.getSiteDao().getSiteByName(siteName);
		}

		if (site == null) {
			ose.addError(SiteErrorCode.NOT_FOUND);
			return;
		}

		if (!requestor.getInstitute().equals(site.getInstitute())) {
			ose.addError(DistributionOrderErrorCode.INVALID_REQUESTER_RECV_SITE_INST, requestor.formattedName(), site.getName());
			return;
		}
		
		order.setSite(site);
	}

	private void setDistributionDate(DistributionOrderDetail detail, DistributionOrder order, OpenSpecimenException ose) {
		SpecimenRequest request = order.getRequest();

		Date creationDate = detail.getCreationDate();
		if (creationDate == null) {
			creationDate = Calendar.getInstance().getTime();
		} else if (creationDate.after(Calendar.getInstance().getTime())) {
			ose.addError(DistributionOrderErrorCode.INVALID_CREATION_DATE);
			return;
		} else if (request != null && request.getDateOfRequest().after(creationDate)) {
			ose.addError(DistributionOrderErrorCode.INVALID_CREATION_DATE);
			return;
		}

		order.setCreationDate(creationDate);
		
		Date executionDate = detail.getExecutionDate();
		if (executionDate == null) {
			executionDate = Calendar.getInstance().getTime();
		} else if (executionDate.after(Calendar.getInstance().getTime())) {
			ose.addError(DistributionOrderErrorCode.INVALID_EXECUTION_DATE);
			return;
		}
		
		order.setExecutionDate(executionDate);
	}

	private void setDistributor(DistributionOrderDetail detail, DistributionOrder order, OpenSpecimenException ose) {
		order.setDistributor(getUser(detail.getDistributor(), AuthUtil.getCurrentUser(), ose, DistributionOrderErrorCode.DISTRIBUTOR_NOT_FOUND));
	}
		
	private void setOrderItems(DistributionOrderDetail detail, DistributionOrder order, OpenSpecimenException ose) {
		Set<DistributionOrderItem> items = new HashSet<DistributionOrderItem>();
		Set<Long> specimens = new HashSet<Long>();

		Set<Long> requestedSpmns = Collections.emptySet();
		if (order.getRequest() != null) {
			requestedSpmns = order.getRequest().getSpecimenIds();
		}

		boolean error = false;
		Set<String> duplicateSpecimens = new HashSet<>();
		for (DistributionOrderItemDetail oid : detail.getOrderItems()) {
			if (oid == null) {
				continue;
			}

			DistributionOrderItem item = getOrderItem(oid, order, requestedSpmns, ose);
			if (item == null) {
				error = true;
				break;
			}
			
			items.add(item);
			if (!specimens.add(item.getSpecimen().getId())) {
				duplicateSpecimens.add(item.getSpecimen().getLabel());
			}
		}

		if (CollectionUtils.isNotEmpty(duplicateSpecimens)) {
			ose.addError(DistributionOrderErrorCode.DUPLICATE_SPECIMENS, duplicateSpecimens);
			return;
		}

		if (error) {
			return;
		}

		if (CollectionUtils.isEmpty(items)) {
			ose.addError(DistributionOrderErrorCode.NO_SPECIMENS_TO_DIST);
			return;
		}

		order.setOrderItems(items);
	}
		
	private void setStatus(DistributionOrderDetail detail, DistributionOrder.Status initialStatus, DistributionOrder order, OpenSpecimenException ose) {
		if (StringUtils.isBlank(detail.getStatus())) {
			ose.addError(DistributionOrderErrorCode.STATUS_REQ);
			return;
		}

		if (initialStatus != null) {
			order.setStatus(initialStatus);
			return;
		}
		
		DistributionOrder.Status status = null;
		try {
			status = DistributionOrder.Status.valueOf(detail.getStatus());
		} catch (IllegalArgumentException iae) {
			ose.addError(DistributionOrderErrorCode.INVALID_STATUS, detail.getStatus());
			return;
		}
		
		order.setStatus(status);
	}
	
	private void setActivityStatus(DistributionOrderDetail detail, DistributionOrder order, OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		} else if (!Status.isValidActivityStatus(activityStatus)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
			return;
		}
		
		order.setActivityStatus(activityStatus);
	}

	private void setTrackingUrl(DistributionOrderDetail detail, DistributionOrder order, OpenSpecimenException ose) {
		order.setTrackingUrl(detail.getTrackingUrl());
	}
	
	private void setComments(DistributionOrderDetail detail, DistributionOrder order, OpenSpecimenException ose) {
		order.setComments(detail.getComments());
	}
		
	private User getUser(UserSummary userSummary, User defaultUser, OpenSpecimenException ose, ErrorCode error) {
		if (userSummary == null) {
			return defaultUser;
		}
		
		User user = defaultUser;
		String key = null;
		if (userSummary.getId() != null) {
			user = daoFactory.getUserDao().getById(userSummary.getId());
			key = userSummary.getId().toString();
		} else if (StringUtils.isNotBlank(userSummary.getEmailAddress())) {
			user = daoFactory.getUserDao().getUserByEmailAddress(userSummary.getEmailAddress());
			key = userSummary.getEmailAddress();
		} else if (StringUtils.isNotBlank(userSummary.getLoginName()) && StringUtils.isNotBlank(userSummary.getDomain())) {
			user = daoFactory.getUserDao().getUser(userSummary.getLoginName(), userSummary.getDomain());
			key = userSummary.getLoginName();
		}

		if (user == null) { //if default user is given then this will not be true in any way so it is optional
			ose.addError(error, key);
		}
		
		return user;
	}
	
	private DistributionOrderItem getOrderItem(
			DistributionOrderItemDetail detail,
			DistributionOrder order,
			Set<Long> requestedSpmns,
			OpenSpecimenException ose) {

		Specimen specimen = getSpecimen(detail.getSpecimen(), ose);
		if (specimen == null) {
			return null;
		}

		SpecimenRequest request = order.getRequest();
		if (request != null && !requestedSpmns.contains(specimen.getId())) {
			ose.addError(DistributionOrderErrorCode.SPECIMEN_NOT_IN_REQ, specimen.getLabel(), request.getId());
			return null;
		}

		if (detail.getQuantity() == null) {
			ose.addError(DistributionOrderErrorCode.ITEM_QTY_REQ, specimen.getLabel());
			return null;
		} else if (NumUtil.lessThanEqualsZero(detail.getQuantity())) {
			ose.addError(DistributionOrderErrorCode.ITEM_INVALID_QTY, specimen.getLabel());
			return null;
		}
				
		DistributionOrderItem orderItem = new DistributionOrderItem();
		orderItem.setQuantity(detail.getQuantity());
		orderItem.setSpecimen(specimen);
		orderItem.setOrder(order);

		setOrderItemStatus(detail, orderItem, ose);

		return orderItem;
	}
	
	private Specimen getSpecimen(SpecimenInfo info, OpenSpecimenException ose) {
		return specimenResolver.getSpecimen(info.getId(), info.getCpShortTitle(), info.getLabel(), ose);
	}

	private void setOrderItemStatus(DistributionOrderItemDetail detail, DistributionOrderItem item, OpenSpecimenException ose) {
		String status = detail.getStatus();
		if (StringUtils.isBlank(status)) {
			ose.addError(DistributionOrderErrorCode.ITEM_STATUS_REQ, item.getSpecimen().getLabel());
			return;
		}

		if (!DistributionOrderItem.isValidDistributionStatus(status)) {
			ose.addError(DistributionOrderErrorCode.INVALID_SPECIMEN_STATUS, status, item.getSpecimen().getLabel());
			return;
		}

		item.setStatus(DistributionOrderItem.Status.valueOf(status));
	}
}
