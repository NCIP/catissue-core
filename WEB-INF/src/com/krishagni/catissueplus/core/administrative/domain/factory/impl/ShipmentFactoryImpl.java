package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Shipment;
import com.krishagni.catissueplus.core.administrative.domain.ShipmentItem;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.SpecimenRequest;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.ShipmentErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.ShipmentFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SpecimenRequestErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.ShipmentDetail;
import com.krishagni.catissueplus.core.administrative.events.ShipmentItemDetail;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenResolver;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

public class ShipmentFactoryImpl implements ShipmentFactory {
	private DaoFactory daoFactory;
	
	private SpecimenFactory specimenFactory;

	private SpecimenResolver specimenResolver;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSpecimenFactory(SpecimenFactory specimenFactory) {
		this.specimenFactory = specimenFactory;
	}

	public void setSpecimenResolver(SpecimenResolver specimenResolver) {
		this.specimenResolver = specimenResolver;
	}

	public Shipment createShipment(ShipmentDetail detail, Shipment.Status status) {
		Shipment shipment = new Shipment();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		shipment.setId(detail.getId());
		setName(detail, shipment, ose);
		setRequest(detail, shipment, ose);
		setCourierName(detail, shipment, ose);
		setTrackingNumber(detail, shipment, ose);
		setTrackingUrl(detail, shipment, ose);
		setSendingSite(detail, shipment, ose);
		setReceivingSite(detail, shipment, ose);
		setStatus(detail, status, shipment, ose);
		setShippedDate(detail, shipment, ose);
		setSender(detail, shipment, ose);
		setSenderComments(detail, shipment, ose);
		setReceivedDate(detail, shipment, ose);
		setReceiver(detail, shipment, ose);
		setReceiverComments(detail, shipment, ose);
		setActivityStatus(detail, shipment, ose);
		setShipmentItems(detail, shipment, ose);
		setNotifyUser(detail, shipment, ose);
		
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

	private void setRequest(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		if (detail.getRequest() == null || detail.getRequest().getId() == null) {
			return;
		}

		Long requestId = detail.getRequest().getId();
		SpecimenRequest request = daoFactory.getSpecimenRequestDao().getById(requestId);
		if (request == null) {
			ose.addError(SpecimenRequestErrorCode.NOT_FOUND, requestId);
			return;
		}

		shipment.setRequest(request);
	}
	
	private void setCourierName(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		shipment.setCourierName(detail.getCourierName());
	}
	
	private void setTrackingNumber(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		shipment.setTrackingNumber(detail.getTrackingNumber());
	}
	
	private void setTrackingUrl(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		shipment.setTrackingUrl(detail.getTrackingUrl());
	}
	
	private void setSendingSite(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		String siteName = detail.getSendingSite();
		if (StringUtils.isBlank(siteName)) {
			ose.addError(ShipmentErrorCode.SEND_SITE_REQUIRED);
			return;
		}
		
		Site site = daoFactory.getSiteDao().getSiteByName(siteName);
		if (site == null) {
			ose.addError(SiteErrorCode.NOT_FOUND);
			return;
		}

		SpecimenRequest request = shipment.getRequest();
		if (request != null && !request.getCp().getRepositories().contains(site)) {
			ose.addError(ShipmentErrorCode.INVALID_SEND_SITE_FOR_REQ, siteName, request.getId());
			return;
		}
		
		shipment.setSendingSite(site);
	}
	
	private void setReceivingSite(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		String siteName = detail.getReceivingSite();
		if (StringUtils.isBlank(siteName)) {
			ose.addError(ShipmentErrorCode.REC_SITE_REQUIRED);
			return;
		}
		
		Site site = daoFactory.getSiteDao().getSiteByName(siteName);
		if (site == null) {
			ose.addError(SiteErrorCode.NOT_FOUND);
			return;
		}

		SpecimenRequest request = shipment.getRequest();
		if (request != null) {
			if (!request.getCp().getRepositories().contains(site)) {
				ose.addError(ShipmentErrorCode.INVALID_RECV_SITE_FOR_REQ, siteName, request.getId());
				return;
			}

			if (!request.getInstitute().equals(site.getInstitute())) {
				ose.addError(ShipmentErrorCode.INVALID_RECV_SITE_FOR_REQ, siteName, request.getId());
				return;
			}
		}

		shipment.setReceivingSite(site);
	}
	
	private void setStatus(ShipmentDetail detail, Shipment.Status initialStatus, Shipment shipment, OpenSpecimenException ose) {
		if (initialStatus != null) {
			shipment.setStatus(initialStatus);
			return;
		}
		
		if (StringUtils.isBlank(detail.getStatus())) {
			ose.addError(ShipmentErrorCode.STATUS_REQUIRED);
			return;
		}
		
		Shipment.Status status = Shipment.Status.fromName(detail.getStatus());
		if (status == null) {
			ose.addError(ShipmentErrorCode.INVALID_STATUS);
		}
		
		shipment.setStatus(status);
	}
	
	private void setShippedDate(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		Date shippedDate = detail.getShippedDate();
		Date todayDate = Utility.chopTime(Calendar.getInstance().getTime());
		if (shippedDate == null) {
			shippedDate = todayDate;
		} else {
			shippedDate = Utility.chopTime(shippedDate);
			if (shippedDate.after(todayDate)) {
				ose.addError(ShipmentErrorCode.INVALID_SHIPPED_DATE);
				return;
			}
		}

		if (shipment.getRequest() != null) {
			Date reqDate = Utility.chopTime(shipment.getRequest().getDateOfRequest());
			if (shippedDate.before(reqDate)) {
				ose.addError(ShipmentErrorCode.INVALID_SHIPPED_DATE);
				return;
			}
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
		Date todayDate = Utility.chopTime(Calendar.getInstance().getTime());
		if (receivedDate == null) {
			receivedDate = todayDate;
		} else {
			receivedDate = Utility.chopTime(receivedDate);
			if (receivedDate.before(Utility.chopTime(shipment.getShippedDate())) ||
				receivedDate.after(todayDate)) {
				ose.addError(ShipmentErrorCode.INVALID_RECEIVED_DATE);
				return;
			}
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

		Set<Long> requestedSpmns = Collections.emptySet();
		if (shipment.getRequest() != null) {
			requestedSpmns = shipment.getRequest().getSpecimenIds();
		}

		Set<ShipmentItem> items = new HashSet<ShipmentItem>();
		Set<Long> specimens = new HashSet<Long>();

		for (ShipmentItemDetail item : detail.getShipmentItems()) {
			ShipmentItem shipmentItem = getShipmentItem(item, shipment, requestedSpmns, ose);
			if (shipmentItem == null) {
				return;
			}
			
			if (!specimens.add(shipmentItem.getSpecimen().getId())) {
				ose.addError(ShipmentErrorCode.DUPLICATE_SPECIMENS);
				return;
			}
			
			items.add(shipmentItem);
		}
		
		shipment.setShipmentItems(items);
	}
	
	private void setNotifyUser(ShipmentDetail detail, Shipment shipment, OpenSpecimenException ose) {
		if (shipment.isReceived()) {
			return;
		}
		
		if (CollectionUtils.isEmpty(detail.getNotifyUsers())) {
			return;
		}
		
		Set<User> result = new HashSet<User>();
		for (UserSummary userSummary : detail.getNotifyUsers()) {
			User user = getUser(userSummary, null);
			if (user == null) {
				ose.addError(UserErrorCode.NOT_FOUND);
				return;
			}
			
			result.add(user);
		}
		
		shipment.setNotifyUsers(result);
	}
	
	private User getUser(UserSummary userSummary, User defaultUser) {
		if (userSummary == null) {
			return defaultUser;
		}
		
		User user = defaultUser;
		if (userSummary.getId() != null) {
			user = daoFactory.getUserDao().getById(userSummary.getId());
		} else if (StringUtils.isNotBlank(userSummary.getEmailAddress())) {
			user = daoFactory.getUserDao().getUserByEmailAddress(userSummary.getEmailAddress());
		} else if (StringUtils.isNotBlank(userSummary.getLoginName()) && StringUtils.isNotBlank(userSummary.getDomain())) {
			user = daoFactory.getUserDao().getUser(userSummary.getLoginName(), userSummary.getDomain());
		}
		
		return user;
	}
	
	private ShipmentItem getShipmentItem(
			ShipmentItemDetail detail,
			Shipment shipment,
			Set<Long> requestedSpmns,
			OpenSpecimenException ose) {

		if (shipment.isReceived() && StringUtils.isBlank(detail.getReceivedQuality())) {
			ose.addError(ShipmentErrorCode.SPEC_REC_QUALITY_REQUIRED);
			return null;
		}
		
		ShipmentItem.ReceivedQuality receivedQuality = null;
		if (shipment.isReceived()) {
			try {
				receivedQuality = ShipmentItem.ReceivedQuality.valueOf(detail.getReceivedQuality().toUpperCase());
			} catch (IllegalArgumentException iae) {
				ose.addError(ShipmentErrorCode.INVALID_SPEC_REC_QUALITY, detail.getReceivedQuality());
				return null;
			}
		}
		
		Specimen specimen = getSpecimen(detail.getSpecimen(), receivedQuality, ose);
		if (specimen == null) {
			return null;
		}

		SpecimenRequest request = shipment.getRequest();
		if (request != null && !requestedSpmns.contains(specimen.getId())) {
			ose.addError(ShipmentErrorCode.SPECIMEN_NOT_IN_REQ, specimen.getLabel(), request.getId());
			return null;
		}

		ShipmentItem shipmentItem = new ShipmentItem();
		shipmentItem.setShipment(shipment);
		shipmentItem.setSpecimen(specimen);
		shipmentItem.setReceivedQuality(receivedQuality);
		return shipmentItem;
	}
	
	private Specimen getSpecimen(SpecimenInfo info, ShipmentItem.ReceivedQuality receivedQuality, OpenSpecimenException ose) {
		Specimen existing = specimenResolver.getSpecimen(info.getId(), info.getCpShortTitle(), info.getLabel(), info.getBarcode(), ose);
		if (existing == null) {
			return null;
		}

		if (receivedQuality != ShipmentItem.ReceivedQuality.ACCEPTABLE) {
			return existing;
		} 

		//
		// Assign location only if received quality is acceptable
		//
		SpecimenDetail detail = new SpecimenDetail();
		detail.setId(info.getId());
		detail.setStorageLocation(info.getStorageLocation());
		return specimenFactory.createSpecimen(existing, detail, null);
	}
}
