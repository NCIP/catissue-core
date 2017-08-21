package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.Shipment;
import com.krishagni.catissueplus.core.administrative.domain.Shipment.Status;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.SpecimenRequest;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.ShipmentErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.ShipmentFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.SpecimenRequestErrorCode;
import com.krishagni.catissueplus.core.administrative.events.ShipmentDetail;
import com.krishagni.catissueplus.core.administrative.events.ShipmentListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.ShipmentDao;
import com.krishagni.catissueplus.core.administrative.services.ShipmentService;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListCriteria;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolver;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.MessageUtil;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.de.domain.Filter;
import com.krishagni.catissueplus.core.de.domain.Filter.Op;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.events.ExecuteQueryEventOp;
import com.krishagni.catissueplus.core.de.events.QueryDataExportResult;
import com.krishagni.catissueplus.core.de.services.QueryService;
import com.krishagni.catissueplus.core.de.services.SavedQueryErrorCode;
import com.krishagni.rbac.common.errors.RbacErrorCode;

import edu.common.dynamicextensions.query.WideRowMode;

public class ShipmentServiceImpl implements ShipmentService, ObjectStateParamsResolver {
	private static final String SHIPMENT_SHIPPED_EMAIL_TMPL = "shipment_shipped";
	
	private static final String SHIPMENT_RECEIVED_EMAIL_TMPL = "shipment_received";
	
	private static final String SHIPMENT_QUERY_REPORT_SETTING = "shipment_export_report";
	
	private DaoFactory daoFactory;
	
	private ShipmentFactory shipmentFactory;
	
	private EmailService emailService;
	
	private QueryService querySvc;
	
	private com.krishagni.catissueplus.core.de.repository.DaoFactory deDaoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setShipmentFactory(ShipmentFactory shipmentFactory) {	
		this.shipmentFactory = shipmentFactory;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	public void setQuerySvc(QueryService querySvc) {
		this.querySvc = querySvc;
	}
	
	public void setDeDaoFactory(com.krishagni.catissueplus.core.de.repository.DaoFactory deDaoFactory) {
		this.deDaoFactory = deDaoFactory;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<ShipmentDetail>> getShipments(RequestEvent<ShipmentListCriteria> req) {
		try {
			ShipmentListCriteria listCrit = addShipmentListCriteria(req.getPayload());
			return ResponseEvent.response(ShipmentDetail.from(getShipmentDao().getShipments(listCrit)));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Long> getShipmentsCount(RequestEvent<ShipmentListCriteria> req) {
		try {
			ShipmentListCriteria crit = addShipmentListCriteria(req.getPayload());
			return ResponseEvent.response(getShipmentDao().getShipmentsCount(crit));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ShipmentDetail> getShipment(RequestEvent<Long> req) {
		try {
			Long shipmentId = req.getPayload();
			Shipment shipment = getShipmentDao().getById(shipmentId);
			if (shipment == null) {
				return ResponseEvent.userError(ShipmentErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureReadShipmentRights(shipment);
			return ResponseEvent.response(ShipmentDetail.from(shipment));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ShipmentDetail> createShipment(RequestEvent<ShipmentDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureCreateShipmentRights();
			ShipmentDetail detail = req.getPayload();
			Shipment shipment = shipmentFactory.createShipment(detail, Status.PENDING);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureValidShipmentStatus(shipment, detail.getStatus(), ose);
			ensureUniqueConstraint(null, shipment, ose);
			ensureValidSpecimens(shipment, ose);
			ensureValidNotifyUsers(shipment, ose);
			ose.checkAndThrow();

			SpecimenRequest request = shipment.getRequest();
			if (request != null && request.isClosed()) {
				throw OpenSpecimenException.userError(SpecimenRequestErrorCode.CLOSED, request.getId());
			}
			
			Status status = Status.fromName(detail.getStatus());
			if (status == Status.SHIPPED) {
				shipment.ship();
			}
			
			//
			//  Saved to obtain IDs to make shipment events
			//
			getShipmentDao().saveOrUpdate(shipment, true);
			
			sendEmailNotifications(shipment, null, detail.isSendMail());
			return ResponseEvent.response(ShipmentDetail.from(shipment));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ShipmentDetail> updateShipment(RequestEvent<ShipmentDetail> req) {
		try {
			ShipmentDetail detail = req.getPayload();
			Shipment existing = getShipment(detail);
			if (existing == null) {
				return ResponseEvent.userError(ShipmentErrorCode.NOT_FOUND);
			}
			
			Shipment newShipment = shipmentFactory.createShipment(detail, null);
			AccessCtrlMgr.getInstance().ensureUpdateShipmentRights(newShipment);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConstraint(existing, newShipment, ose);
			ensureValidSpecimens(newShipment, ose);
			ensureValidNotifyUsers(newShipment, ose);
			ose.checkAndThrow();
			
			Status oldStatus = existing.getStatus();
			existing.update(newShipment);
			getShipmentDao().saveOrUpdate(existing, true);
			sendEmailNotifications(newShipment, oldStatus, detail.isSendMail());
			return ResponseEvent.response(ShipmentDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<QueryDataExportResult> exportReport(RequestEvent<Long> req) {
		Shipment shipment = getShipmentDao().getById(req.getPayload());
		if (shipment == null) {
			return ResponseEvent.userError(ShipmentErrorCode.NOT_FOUND);
		}
		
		AccessCtrlMgr.getInstance().ensureReadShipmentRights(shipment);
		Integer queryId = ConfigUtil.getInstance().getIntSetting("common", SHIPMENT_QUERY_REPORT_SETTING, -1);
		if (queryId == -1) {
			return ResponseEvent.userError(ShipmentErrorCode.RPT_TMPL_NOT_CONF);
		}
		
		SavedQuery query = deDaoFactory.getSavedQueryDao().getQuery(new Long(queryId));
		if (query == null) {
			return ResponseEvent.userError(SavedQueryErrorCode.NOT_FOUND, queryId);
		}
		
		return new ResponseEvent<QueryDataExportResult>(exportShipmentReport(shipment, query));
	}

	@Override
	public String getObjectName() {
		return "shipment";
	}

	@Override
	@PlusTransactional
	public Map<String, Object> resolve(String key, Object value) {
		if (key.equals("id")) {
			value = Long.valueOf(value.toString());
		}

		return daoFactory.getShipmentDao().getShipmentIds(key, value);
	}

	private ShipmentListCriteria addShipmentListCriteria(ShipmentListCriteria crit) {
		Set<Long> siteIds = AccessCtrlMgr.getInstance().getReadAccessShipmentSiteIds();
		if (siteIds != null && siteIds.isEmpty()) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}
		
		if (siteIds != null) {
			crit.siteIds(siteIds);
		}
		
		return crit;
	}

	private List<Specimen> getValidSpecimens(List<Long> specimenIds, OpenSpecimenException ose) {
		List<Pair<Long, Long>> siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
		if (siteCpPairs != null && siteCpPairs.isEmpty()) {
			ose.addError(ShipmentErrorCode.INVALID_SPECIMENS);
			return null;
		}
		
		SpecimenListCriteria crit = new SpecimenListCriteria().ids(specimenIds).siteCps(siteCpPairs);
		List<Specimen> specimens = daoFactory.getSpecimenDao().getSpecimens(crit);
		if (specimenIds.size() != specimens.size()) {
			ose.addError(ShipmentErrorCode.INVALID_SPECIMENS);
			return null;
		}
		
		return specimens;
	}
	
	private void ensureValidShipmentStatus(Shipment shipment, String shipmentStatus, OpenSpecimenException ose) {
		if (StringUtils.isBlank(shipmentStatus)) {
			return;
		}
		
		Status status = Status.fromName(shipmentStatus);
		if (status == null) {
			ose.addError(ShipmentErrorCode.INVALID_STATUS);
		}
		
		if (status == Status.RECEIVED) {
			ose.addError(ShipmentErrorCode.NOT_SHIPPED_TO_RECV, shipment.getName());
		}
	}

	private void ensureUniqueConstraint(Shipment existing, Shipment newShipment, OpenSpecimenException ose) {
		if (existing == null || !newShipment.getName().equals(existing.getName())) {
			Shipment shipment = getShipmentDao().getShipmentByName(newShipment.getName());
			if (shipment != null) {
				ose.addError(ShipmentErrorCode.DUP_NAME, newShipment.getName());
			}
		}
	}
	
	private void ensureValidSpecimens(Shipment shipment, OpenSpecimenException ose) {
		List<Long> specimenIds = Utility.collect(shipment.getShipmentItems(), "specimen.id");
		List<Specimen> specimens = getValidSpecimens(specimenIds, ose);
		if (specimens == null) {
			return;
		}
		
		ensureSpecimensAreAvailable(specimens, ose);
		ensureValidSpecimenSites(specimens, shipment.getSendingSite(), shipment.getReceivingSite(), ose);
		ensureSpecimensAreNotShipped(shipment, specimenIds, ose);
	}
	
	private void ensureSpecimensAreAvailable(List<Specimen> specimens, OpenSpecimenException ose) {
		List<Specimen> closedSpecimens = new ArrayList<Specimen>();
		List<Specimen> unavailableSpecimens = new ArrayList<Specimen>();
		for (Specimen specimen : specimens) {
			if (specimen.isClosed()) {
				closedSpecimens.add(specimen);
			} else if (!specimen.isAvailable()) {
				unavailableSpecimens.add(specimen);
			}
		}
		
		if (CollectionUtils.isNotEmpty(closedSpecimens)) {
			List<String> labels = Utility.<List<String>>collect(closedSpecimens, "label");
			ose.addError(ShipmentErrorCode.CLOSED_SPECIMENS, StringUtils.join(labels, ','));
		}
		
		if (CollectionUtils.isNotEmpty(unavailableSpecimens)) {
			List<String> labels = Utility.<List<String>>collect(unavailableSpecimens, "label");
			ose.addError(ShipmentErrorCode.UNAVAILABLE_SPECIMENS, StringUtils.join(labels, ','));
		}
	}
	
	private void ensureValidSpecimenSites(List<Specimen> specimens, Site sendingSite, Site receivingSite, OpenSpecimenException ose) {
		Map<Long, Specimen> specimenMap = specimens.stream().collect(Collectors.toMap(Specimen::getId, spmn -> spmn));
		ensureValidSpecimenSendingSites(specimenMap, sendingSite, ose);
		ensureValidSpecimenRecvSites(specimenMap, receivingSite, ose);
	}

	private void ensureValidSpecimenSendingSites(Map<Long, Specimen> specimenMap, Site sendingSite, OpenSpecimenException ose) {
		Map<Long, Long> spmnStorageSites = daoFactory.getSpecimenDao().getSpecimenStorageSite(specimenMap.keySet());

		String invalidSpmnLabels = specimenMap.values().stream()
			.filter(spmn -> {
				Long spmnSiteId = spmnStorageSites.get(spmn.getId());
				return spmnSiteId != null && !spmnSiteId.equals(sendingSite.getId());
			})
			.map(spmn -> spmn.getLabel())
			.collect(Collectors.joining(", "));

		if (StringUtils.isNotBlank(invalidSpmnLabels)) {
			ose.addError(ShipmentErrorCode.SPEC_NOT_BELONG_TO_SEND_SITE, invalidSpmnLabels, sendingSite.getName());
		}
	}

	private void ensureValidSpecimenRecvSites(Map<Long, Specimen> specimenMap, Site receivingSite, OpenSpecimenException ose) {
		Map<Long, Set<Long>> spmnSites = daoFactory.getSpecimenDao().getSpecimenSites(specimenMap.keySet());

		String invalidSpmnLabels = spmnSites.entrySet().stream()
			.filter(spmnSite -> !spmnSite.getValue().contains(receivingSite.getId()))
			.map(spmnSite -> specimenMap.get(spmnSite.getKey()).getLabel())
			.collect(Collectors.joining(", "));

		if (StringUtils.isNotBlank(invalidSpmnLabels)) {
			ose.addError(ShipmentErrorCode.SPEC_NOT_BELONG_TO_REC_SITE, invalidSpmnLabels, receivingSite.getName());
		}
	}
	
	private void ensureSpecimensAreNotShipped(Shipment shipment, List<Long> specimenIds, OpenSpecimenException ose) {
		if (shipment.isReceived()) {
			return;
		}
		
		List<Specimen> shippedSpecimens = getShipmentDao().getShippedSpecimensByIds(specimenIds);
		if (CollectionUtils.isNotEmpty(shippedSpecimens)) {
			List<String> labels = Utility.<List<String>>collect(shippedSpecimens, "label");
			ose.addError(ShipmentErrorCode.SPECIMEN_ALREADY_SHIPPED, StringUtils.join(labels, ','));
		}
	}

	public void ensureValidNotifyUsers(Shipment shipment, OpenSpecimenException ose) {
		if (shipment.isReceived()) {
			return;
		}
		
		Institute institute = shipment.getReceivingSite().getInstitute();
		for (User user: shipment.getNotifyUsers()) {
			if (!user.getInstitute().equals(institute)) {
				ose.addError(ShipmentErrorCode.NOTIFY_USER_NOT_BELONG_TO_INST, user.formattedName(), institute.getName());
			}
		}
	}
	
	private Shipment getShipment(ShipmentDetail detail) {
		Shipment shipment = null;
		if (detail.getId() != null) {
			shipment = getShipmentDao().getById(detail.getId());
		} else if (StringUtils.isNotBlank(detail.getName())) {
			shipment = getShipmentDao().getShipmentByName(detail.getName());
		}
		
		return shipment;
	}
	
	private void sendEmailNotifications(Shipment shipment, Status oldStatus, boolean isSendMail) {
		if (!isSendMail) {
			return;
		}
		
		if ((oldStatus == null || oldStatus == Status.PENDING) && shipment.isShipped()) {
			sendShipmentShippedEmail(shipment);
		} else if (oldStatus == Status.SHIPPED && shipment.isReceived()) {
			sendShipmentReceivedEmail(shipment);
		}
	}
	
	private void sendShipmentShippedEmail(Shipment shipment) {
		if (CollectionUtils.isEmpty(shipment.getNotifyUsers())) {
			return;
		}
		
		Set<String> emailIds = Utility.<Set<String>>collect(shipment.getNotifyUsers(), "emailAddress", true);
		emailIds.add(shipment.getSender().getEmailAddress());
		String[] subjectParams = {shipment.getName()};
		
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("$subject", subjectParams);
		props.put("shipment", shipment);
		emailService.sendEmail(SHIPMENT_SHIPPED_EMAIL_TMPL, emailIds.toArray(new String[0]), props);
	}
	
	private void sendShipmentReceivedEmail(Shipment shipment) {
		String[] emailIds = new String[] {shipment.getSender().getEmailAddress()};
 		String[] subjectParams = {shipment.getName()};
		
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("$subject", subjectParams);
		props.put("shipment", shipment);
		emailService.sendEmail(SHIPMENT_RECEIVED_EMAIL_TMPL, emailIds, props);
	}
	
	private QueryDataExportResult exportShipmentReport(final Shipment shipment, SavedQuery query) {
		Filter filter = new Filter();
		filter.setField("Shipment.id");
		filter.setOp(Op.EQ);
		filter.setValues(new String[] { shipment.getId().toString() });
		
		ExecuteQueryEventOp execReportOp = new ExecuteQueryEventOp();
		execReportOp.setDrivingForm("Participant");
		execReportOp.setAql(query.getAql(new Filter[] { filter }));
		execReportOp.setWideRowMode(WideRowMode.DEEP.name());
		execReportOp.setRunType("Export");
		
		return querySvc.exportQueryData(execReportOp, new QueryService.ExportProcessor() {
			@Override
			public String filename() {
				return "shipment_" + shipment.getId() + "_" + UUID.randomUUID().toString();
			}

			@Override
			public void headers(OutputStream out) {
				@SuppressWarnings("serial")
				Map<String, String> headers = new LinkedHashMap<String, String>() {{
					put(getMessage("shipment_name"),            shipment.getName());
					put(getMessage("shipment_courier_name"),    shipment.getCourierName());
					put(getMessage("shipment_tracking_number"), shipment.getTrackingNumber());
					put(getMessage("shipment_tracking_url"),    shipment.getTrackingUrl());
					put(getMessage("shipment_sending_site"),    shipment.getSendingSite().getName());
					put(getMessage("shipment_sender"),          shipment.getSender().formattedName());
					put(getMessage("shipment_shipped_date"),    Utility.getDateString(shipment.getShippedDate()));
					put(getMessage("shipment_sender_comments"), shipment.getSenderComments());
					put(getMessage("shipment_recv_site"),       shipment.getReceivingSite().getName());
					
					if (shipment.getReceiver() != null) {
						put(getMessage("shipment_receiver"), shipment.getReceiver().formattedName());
					}
					
					if (shipment.getReceivedDate() != null) {
						put(getMessage("shipment_received_date"), Utility.getDateString(shipment.getReceivedDate()));
					}
					
					put(getMessage("shipment_receiver_comments"), shipment.getReceiverComments());
					put(getMessage("shipment_status"),            shipment.getStatus().getName());

					put("", ""); // blank line
				}};
				
				Utility.writeKeyValuesToCsv(out, headers);
			}
		});
	}
	
	private String getMessage(String code) {
		return MessageUtil.getInstance().getMessage(code);
	}
	
	private ShipmentDao getShipmentDao() {
		return daoFactory.getShipmentDao();
	}
	
}
