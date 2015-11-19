package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Shipment;
import com.krishagni.catissueplus.core.administrative.domain.ShipmentItem;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.ShipmentErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.ShipmentFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.ShipmentDetail;
import com.krishagni.catissueplus.core.administrative.events.ShipmentItemDetail;
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

public class ShipmentFactoryImpl implements ShipmentFactory {
	private DaoFactory daoFactory;
	
	private SpecimenFactory specimenFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSpecimenFactory(SpecimenFactory specimenFactory) {
		this.specimenFactory = specimenFactory;
	}

	public Shipment createShipment(ShipmentDetail detail, Shipment.Status status) {
		Shipment shipment = new Shipment();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		shipment.setId(detail.getId());
		setName(detail, shipment, ose);
		setSite(detail, shipment, ose);
		setStatus(detail, status, shipment, ose);
		setShippedDate(detail, shipment, ose);
		setSender(detail, shipment, ose);
		setSenderComments(detail, shipment, ose);
		setReceivedDate(detail, shipment, ose);
		setReceiver(detail, shipment, ose);
		setReceiverComments(detail, shipment, ose);
		setActivityStatus(detail, shipment, ose);
		setShipmentItems(detail, shipment, ose);
		
		ose.checkAndThrow();
		return shipment;
	}
	
	private void setName(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		String name = detail.getName();
		if (StringUtils.isBlank(name)) {
			ose.addError(ShipmentErrorCode.NAME_REQUIRED);
			return;
		}
		
		shipment.setName(name);
	}
	
	private void setSite(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		String siteName = detail.getSiteName();
		if (StringUtils.isBlank(siteName)) {
			ose.addError(ShipmentErrorCode.SITE_REQUIRED);
			return;
		}
		
		Site site = daoFactory.getSiteDao().getSiteByName(siteName);
		if (site == null) {
			ose.addError(SiteErrorCode.NOT_FOUND);
			return;
		}
		
		shipment.setSite(site);
	}
	
	private void setStatus(ShipmentDetail detail, Shipment.Status initialStatus, Shipment shipment, OpenSpecimenException ose) {
		if (initialStatus != null) {
			shipment.setStatus(initialStatus);
			return;
		}
		
		if (detail.getStatus() == null) {
			ose.addError(ShipmentErrorCode.STATUS_REQUIRED);
			return;
		}
		
		Shipment.Status status = null;
			
		try {
			status = Shipment.Status.valueOf(detail.getStatus());
		} catch (IllegalArgumentException iae) {
			ose.addError(ShipmentErrorCode.INVALID_STATUS);
			return;
		}
		
		shipment.setStatus(status);
	}
	
	private void setShippedDate(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		Date shippedDate = detail.getShippedDate();
		if (shippedDate == null) {
			shippedDate = Calendar.getInstance().getTime();
		} else if (shippedDate.after(Calendar.getInstance().getTime())) {
			ose.addError(ShipmentErrorCode.INVALID_SHIPPED_DATE);
			return;
		}

		shipment.setShippedDate(shippedDate);
	}
	
	private void setSender(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		User sender = getUser(detail.getSender(), AuthUtil.getCurrentUser());
		if (sender == null) {
			ose.addError(UserErrorCode.NOT_FOUND);
			return;
		}
		
		shipment.setSender(sender);
	}
	
	private void setSenderComments(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		shipment.setSenderComments(detail.getSenderComments());
	}
	
	private void setReceivedDate(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		if (!shipment.isReceived()) {
			return;
		}
		
		Date receivedDate = detail.getReceivedDate();
		if (receivedDate == null) {
			receivedDate = Calendar.getInstance().getTime();
		} else if (receivedDate.before(shipment.getShippedDate()) || 
			receivedDate.after(Calendar.getInstance().getTime())) {
			ose.addError(ShipmentErrorCode.INVALID_RECEIVED_DATE);
			return;
		}

		shipment.setReceivedDate(receivedDate);
	}
	
	private void setReceiver(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		if (!shipment.isReceived()) {
			return;
		}
		
		User receiver = getUser(detail.getReceiver(), AuthUtil.getCurrentUser());
		if (receiver == null) {
			ose.addError(UserErrorCode.NOT_FOUND);
			return;
		}
		
		shipment.setReceiver(receiver);
	}
	
	private void setReceiverComments(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		if (!shipment.isReceived()) {
			return;
		}
		
		shipment.setReceiverComments(detail.getReceiverComments());
	}
	
	private void setActivityStatus(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		if (!Status.isValidActivityStatus(activityStatus)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
			return;
		}
		
		shipment.setActivityStatus(activityStatus);
	}
	
	
	private void setShipmentItems(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		if (CollectionUtils.isEmpty(detail.getShipmentItems())) {
			ose.addError(ShipmentErrorCode.NO_SPECIMENS_TO_SHIP);
			return;
		}
		
		Set<ShipmentItem> orderItems = new HashSet<ShipmentItem>();
		Set<Long> specimens = new HashSet<Long>();
		for (ShipmentItemDetail item : detail.getShipmentItems()) {
			ShipmentItem orderItem = getShipmentItem(item, shipment, ose);
			if (orderItem == null) {
				return;
			}
			
			if (!specimens.add(orderItem.getSpecimen().getId())) {
				ose.addError(ShipmentErrorCode.DUPLICATE_SPECIMENS);
				return;
			}
			
			orderItems.add(orderItem);
		}
		
		shipment.setShipmentItems(orderItems);
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
	
	private ShipmentItem getShipmentItem(ShipmentItemDetail detail, Shipment shipment, OpenSpecimenException ose) {
		if (shipment.isReceived() && StringUtils.isBlank(detail.getReceivedQuality())) {
			ose.addError(ShipmentErrorCode.SPECIMEN_RECEIVED_QUALITY_REQUIRED);
			return null;
		}
		
		ShipmentItem.ReceivedQuality receivedQuality = null;
		try {
			if (shipment.isReceived()) {
				receivedQuality = ShipmentItem.ReceivedQuality.valueOf(detail.getReceivedQuality().toUpperCase());
			}
		} catch (IllegalArgumentException iae) {
			ose.addError(ShipmentErrorCode.INVALID_SPECIMEN_RECEIVED_QUALITY, detail.getReceivedQuality());
			return null;
		}
		
		Specimen specimen = getSpecimen(detail.getSpecimen(), receivedQuality, ose);
		if (specimen == null) {
			return null;
		}
		
		ShipmentItem shipmentItem = new ShipmentItem();
		shipmentItem.setShipment(shipment);
		shipmentItem.setSpecimen(specimen);
		shipmentItem.setReceivedQuality(receivedQuality);
		
		return shipmentItem;
	}
	
	private Specimen getSpecimen(SpecimenInfo info, ShipmentItem.ReceivedQuality receivedQuality, OpenSpecimenException ose) {
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
		
		if (receivedQuality != ShipmentItem.ReceivedQuality.ACCEPTABLE) {
			return existing;
		} 
		
		SpecimenDetail detail = new SpecimenDetail();
		detail.setId(info.getId());
		detail.setStorageLocation(info.getStorageLocation());
		
		return specimenFactory.createSpecimen(existing, detail, null);
	}
}
