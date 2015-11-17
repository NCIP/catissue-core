package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.ShippingOrder;
import com.krishagni.catissueplus.core.administrative.domain.ShippingOrderItem;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.ShippingOrderErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.ShippingOrderFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.ShippingOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.ShippingOrderItemDetail;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Status;

public class ShippingOrderFactoryImpl implements ShippingOrderFactory {
	private DaoFactory daoFactory;
	
	private SpecimenFactory specimenFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSpecimenFactory(SpecimenFactory specimenFactory) {
		this.specimenFactory = specimenFactory;
	}

	public ShippingOrder createShippingOrder(ShippingOrderDetail detail, ShippingOrder.Status status) {
		ShippingOrder order = new ShippingOrder();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		order.setId(detail.getId());
		setName(detail, order, ose);
		setSite(detail, order, ose);
		setSender(detail, order, ose);
		setStatus(detail, status, order, ose);
		setShippingDate(detail, order, ose);
		setComments(detail, order, ose);
		setActivityStatus(detail, order, ose);
		setReceiver(detail, order, ose);
		setReceiverComments(detail, order, ose);
		setOrderItems(detail, order, ose);
		
		ose.checkAndThrow();
		return order;
	}
	
	private void setName(ShippingOrderDetail detail, ShippingOrder order, OpenSpecimenException ose) {
		String name = detail.getName();
		if (StringUtils.isBlank(name)) {
			ose.addError(ShippingOrderErrorCode.NAME_REQUIRED);
			return;
		}
		
		order.setName(detail.getName());
	}
	
	private void setSite(ShippingOrderDetail detail, ShippingOrder order, OpenSpecimenException ose) {
		String siteName = detail.getSiteName();
		if (StringUtils.isBlank(siteName)) {
			ose.addError(ShippingOrderErrorCode.SITE_REQUIRED);
			return;
		}
		
		Site site = daoFactory.getSiteDao().getSiteByName(siteName);
		if (site == null) {
			ose.addError(SiteErrorCode.NOT_FOUND);
			return;
		}
		
		order.setSite(site);
	}
	
	private void setSender(ShippingOrderDetail detail, ShippingOrder order, OpenSpecimenException ose) {
		User sender = getUser(detail.getSender(), AuthUtil.getCurrentUser());
		if (sender == null) {
			ose.addError(UserErrorCode.NOT_FOUND);
			return;
		}
		
		order.setSender(sender);
	}
	
	private void setStatus(ShippingOrderDetail detail, ShippingOrder.Status initialStatus, ShippingOrder order, OpenSpecimenException ose) {
		if (initialStatus != null) {
			order.setStatus(initialStatus);
			return;
		}
		
		if (detail.getStatus() == null) {
			ose.addError(ShippingOrderErrorCode.STATUS_REQUIRED);
			return;
		}
		
		ShippingOrder.Status status = null;
			
		try {
			status = ShippingOrder.Status.valueOf(detail.getStatus());
		} catch (IllegalArgumentException iae) {
			ose.addError(ShippingOrderErrorCode.INVALID_STATUS);
			return;
		}
		
		order.setStatus(status);
	}
	
	private void setShippingDate(ShippingOrderDetail detail, ShippingOrder order, OpenSpecimenException ose) {
		Date shippingDate = detail.getShippingDate();
		if (shippingDate == null) {
			shippingDate = Calendar.getInstance().getTime();
		} else if (shippingDate.after(Calendar.getInstance().getTime())) {
			ose.addError(ShippingOrderErrorCode.INVALID_SHIPPING_DATE);
			return;
		}

		order.setShippingDate(shippingDate);
	}
	
	private void setComments(ShippingOrderDetail detail, ShippingOrder order, OpenSpecimenException ose) {
		order.setComments(detail.getComments());
	}
	
	private void setActivityStatus(ShippingOrderDetail detail, ShippingOrder order, OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		if (!Status.isValidActivityStatus(activityStatus)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
			return;
		}
		
		order.setActivityStatus(activityStatus);
	}
	
	private void setReceiver(ShippingOrderDetail detail, ShippingOrder order, OpenSpecimenException ose) {
		if(!order.isOrderReceived()) {
			return;
		}
		
		User receiver = getUser(detail.getReceiver(), AuthUtil.getCurrentUser());
		if (receiver == null) {
			ose.addError(UserErrorCode.NOT_FOUND);
			return;
		}
		
		order.setReceiver(receiver);
	}
	
	private void setReceiverComments(ShippingOrderDetail detail, ShippingOrder order, OpenSpecimenException ose) {
		if(!order.isOrderReceived()) {
			return;
		}
		
		order.setReceiverComments(detail.getReceiverComments());
	}
	
	private void setOrderItems(ShippingOrderDetail detail, ShippingOrder order, OpenSpecimenException ose) {
		if (CollectionUtils.isEmpty(detail.getOrderItems())) {
			ose.addError(ShippingOrderErrorCode.NO_SPECIMENS_TO_SHIP);
			return;
		}
		
		Set<ShippingOrderItem> orderItems = new HashSet<ShippingOrderItem>();
		Set<Long> specimens = new HashSet<Long>();
		for (ShippingOrderItemDetail item : detail.getOrderItems()) {
			ShippingOrderItem orderItem = getOrderItem(item, order, ose);
			if (orderItem == null) {
				return;
			}
			
			if (!specimens.add(orderItem.getSpecimen().getId())) {
				ose.addError(ShippingOrderErrorCode.DUPLICATE_SPECIMENS);
				return;
			}
			
			orderItems.add(orderItem);
		}
		
		order.setOrderItems(orderItems);
	}
	
	private User getUser(UserSummary userSummary, User defaultUser) {
		if (userSummary == null) {
			return defaultUser;
		}
		
		User user = defaultUser;
		if (userSummary.getId() != null) {
			user = daoFactory.getUserDao().getById(userSummary.getId());
		} else if (StringUtils.isNotBlank(userSummary.getLoginName()) && StringUtils.isNotBlank(userSummary.getDomain())) {
			user = daoFactory.getUserDao().getUser(userSummary.getLoginName(), userSummary.getDomain());
		}
		
		return user;
	}
	
	private ShippingOrderItem getOrderItem(ShippingOrderItemDetail detail, ShippingOrder order, OpenSpecimenException ose) {
		Specimen specimen = getSpecimen(detail.getSpecimen(), order, ose);
		if (specimen == null) {
			return null;
		}
		
		ShippingOrderItem orderItem = new ShippingOrderItem();
		orderItem.setOrder(order);
		orderItem.setSpecimen(specimen);
		
		if (!order.isOrderReceived()) {
			return orderItem;
		}
		
		if (StringUtils.isBlank(detail.getQuality())) {
			ose.addError(ShippingOrderErrorCode.SPECIMEN_QUALITY_REQUIRED);
			return null;
		}
		
		try {
			orderItem.setQuality(ShippingOrderItem.Quality.valueOf(detail.getQuality().toUpperCase()));
		} catch (IllegalArgumentException iae) {
			ose.addError(ShippingOrderErrorCode.INVALID_SPECIMEN_QUALITY, detail.getQuality());
			return null;
		}
		
		return orderItem;
	}
	
	private Specimen getSpecimen(SpecimenInfo info, ShippingOrder order, OpenSpecimenException ose) {
		Specimen existing = null;
		Object key = null;
		
		if (info.getId() != null) {
			key = info.getId();
			existing = daoFactory.getSpecimenDao().getById(info.getId());
		} else if (StringUtils.isNotBlank(info.getLabel())) {
			key = info.getLabel();
			existing = daoFactory.getSpecimenDao().getByLabel(info.getLabel());
		}
		
		if (existing == null) {
			ose.addError(SpecimenErrorCode.NOT_FOUND, key);
			return null;
		}
		
		if (!order.isOrderReceived()) {
			return existing;
		} 
		
		SpecimenDetail detail = new SpecimenDetail();
		detail.setId(info.getId());
		detail.setStorageLocation(info.getStorageLocation());
		
		return specimenFactory.createSpecimen(existing, detail, null);
	}
}
