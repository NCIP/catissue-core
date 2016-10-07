package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.administrative.events.StorageLocationSummary;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionEventDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.MasterSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ReceivedEventDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class MasterSpecimenImporter implements ObjectImporter<MasterSpecimenDetail, MasterSpecimenDetail> {

	private DaoFactory daoFactory;
	
	private CollectionProtocolRegistrationService cprSvc;
	
	private VisitService visitSvc;
	
	private SpecimenService specimenSvc;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setCprSvc(CollectionProtocolRegistrationService cprSvc) {
		this.cprSvc = cprSvc;
	}

	public void setVisitSvc(VisitService visitSvc) {
		this.visitSvc = visitSvc;
	}

	public void setSpecimenSvc(SpecimenService specimenSvc) {
		this.specimenSvc = specimenSvc;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<MasterSpecimenDetail> importObject(
			RequestEvent<ImportObjectDetail<MasterSpecimenDetail>> req) {
		try {
			ImportObjectDetail<MasterSpecimenDetail> detail = req.getPayload();
			if (!detail.isCreate()) {
				return null;
			}
			
			createCpr(detail.getObject());
			createVisit(detail.getObject());
			createSpecimen(detail.getObject());
			
			return ResponseEvent.response(detail.getObject());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void createCpr(MasterSpecimenDetail detail) {
		if (!isPrimarySpecimen(detail)) {
			return;
		}
		
		String cpShortTitle = detail.getCpShortTitle();
		if (StringUtils.isBlank(cpShortTitle)) {
			throw OpenSpecimenException.userError(CpErrorCode.SHORT_TITLE_REQUIRED);
		}
		
		Date collectionDate = detail.getCollectionDate();
		if (collectionDate == null) {
			throw OpenSpecimenException.userError(SpecimenErrorCode.COLL_DATE_REQUIRED);
		}
		
		CollectionProtocolRegistration cpr = null;
		String ppid = detail.getPpid();
		if (StringUtils.isNotBlank(ppid)) {
			cpr = daoFactory.getCprDao().getCprByCpShortTitleAndPpid(cpShortTitle, ppid);
		}
		
		if (cpr != null) {
			Visit matchedVisit = cpr.getVisits()
					.stream()
					.filter(visit -> DateUtils.isSameDay(visit.getVisitDate(), collectionDate))
					.findAny()
					.orElse(null);
			
			if (matchedVisit != null) {
				detail.setVisitId(matchedVisit.getId());
			}
			return;
		}
		
		CollectionProtocolRegistrationDetail cprDetail = new CollectionProtocolRegistrationDetail();
		cprDetail.setPpid(ppid);
		cprDetail.setCpShortTitle(cpShortTitle);
		cprDetail.setRegistrationDate(detail.getRegistrationDate() == null ? collectionDate : detail.getRegistrationDate());
		
		setParticipant(detail, cprDetail);
		
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(cprDetail));
		resp.throwErrorIfUnsuccessful();
		
		detail.setPpid(resp.getPayload().getPpid());
	}
	
	private void createVisit(MasterSpecimenDetail detail) {
		if (detail.getVisitId() != null || !isPrimarySpecimen(detail)) {
			return;
		}
		
		VisitDetail visitDetail = new VisitDetail();
		visitDetail.setCpShortTitle(detail.getCpShortTitle());
		visitDetail.setPpid(detail.getPpid());
		visitDetail.setName(detail.getVisitName());
		visitDetail.setEventLabel(getEventLabel(detail));
		visitDetail.setVisitDate(detail.getVisitDate() == null ? detail.getCollectionDate() : detail.getVisitDate());
		visitDetail.setSite(detail.getCollectionSite());
		visitDetail.setClinicalDiagnosis(detail.getClinicalDiagnosis());
		visitDetail.setClinicalStatus(detail.getClinicalStatus());
		visitDetail.setSurgicalPathologyNumber(detail.getSurgicalPathologyNumber());
		visitDetail.setComments(detail.getVisitComments());
		visitDetail.setStatus(StringUtils.isBlank(detail.getStatus()) ? Visit.VISIT_STATUS_COMPLETED : detail.getStatus());
		
		ResponseEvent<VisitDetail> resp = visitSvc.addVisit(getRequest(visitDetail));
		resp.throwErrorIfUnsuccessful();
		
		detail.setVisitId(resp.getPayload().getId());
	}
	
	private void createSpecimen(MasterSpecimenDetail detail) {
		SpecimenDetail specimenDetail = new SpecimenDetail();
		specimenDetail.setCpShortTitle(detail.getCpShortTitle());
		specimenDetail.setVisitId(detail.getVisitId());
		specimenDetail.setReqCode(detail.getReqCode());
		specimenDetail.setLabel(detail.getLabel());
		specimenDetail.setSpecimenClass(detail.getSpecimenClass());
		specimenDetail.setType(detail.getType());
		specimenDetail.setLineage(detail.getLineage());
		specimenDetail.setAnatomicSite(detail.getAnatomicSite());
		specimenDetail.setLaterality(detail.getLaterality());
		specimenDetail.setStatus(Specimen.COLLECTED); 
		specimenDetail.setPathology(detail.getPathology());
		specimenDetail.setInitialQty(detail.getInitialQty());
		specimenDetail.setConcentration(detail.getConcentration());
		specimenDetail.setCreatedOn(detail.getCreatedOn());
		specimenDetail.setComments(detail.getComments());
		specimenDetail.setExtensionDetail(detail.getExtensionDetail());
		
		setParentLabel(detail, specimenDetail);
		setLocation(detail, specimenDetail);
		setCollectionDetail(detail, specimenDetail);
		setReceiveDetail(detail, specimenDetail);
		
		ResponseEvent<SpecimenDetail> resp = specimenSvc.createSpecimen(getRequest(specimenDetail));
		resp.throwErrorIfUnsuccessful();
		
		detail.setLabel(resp.getPayload().getLabel());
	}
	
	private void setParticipant(MasterSpecimenDetail detail, CollectionProtocolRegistrationDetail cprDetail) {
		ParticipantDetail participantDetail = new ParticipantDetail();
		BeanUtils.copyProperties(detail, participantDetail, 
				new String[] {"id", "activityStatus", "phiAccess", "registeredCps", "extensionDetail"});
		
		cprDetail.setParticipant(participantDetail);
	}
	
	private void setParentLabel(MasterSpecimenDetail detail, SpecimenDetail specimenDetail) {
		if (isPrimarySpecimen(detail)) {
			return;
		}
		
		String parentLabel = detail.getParentLabel();
		if (StringUtils.isBlank(parentLabel)) {
			throw OpenSpecimenException.userError(SpecimenErrorCode.PARENT_REQUIRED);
		}
		
		specimenDetail.setParentLabel(parentLabel);
	}
	
	private void setLocation(MasterSpecimenDetail detail, SpecimenDetail specimenDetail) {
		StorageLocationSummary storageLocation = new StorageLocationSummary();
		storageLocation.setName(detail.getContainer());
		storageLocation.setPositionX(detail.getPositionX());
		storageLocation.setPositionY(detail.getPositionY());
		storageLocation.setPosition(detail.getPosition());
		specimenDetail.setStorageLocation(storageLocation);
	}
	
	private void setCollectionDetail(MasterSpecimenDetail detail, SpecimenDetail specimenDetail) {
		if (!isPrimarySpecimen(detail)) {
			return;
		}
		
		CollectionEventDetail collectionEvent = new CollectionEventDetail();
		collectionEvent.setProcedure(detail.getCollectionProcedure());
		collectionEvent.setContainer(detail.getCollectionContainer());
		collectionEvent.setTime(detail.getCollectionDate());
		collectionEvent.setUser(getUser(detail.getCollector()));
		specimenDetail.setCollectionEvent(collectionEvent);
	}
	
	private void setReceiveDetail(MasterSpecimenDetail detail, SpecimenDetail specimenDetail) {
		if (!isPrimarySpecimen(detail)) {
			return;
		}
		
		ReceivedEventDetail receivedEvent = new ReceivedEventDetail();
		receivedEvent.setReceivedQuality(detail.getReceivedQuality());
		receivedEvent.setTime(detail.getReceivedDate());
		receivedEvent.setUser(getUser(detail.getReceiver()));
		specimenDetail.setReceivedEvent(receivedEvent);
	}
	
	private String getEventLabel(MasterSpecimenDetail detail) {
		if (StringUtils.isNotBlank(detail.getEventLabel())) {
			return detail.getEventLabel();
		}
		
		CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getCpByShortTitle(detail.getCpShortTitle());
		CollectionProtocolEvent event = cp.firstEvent();
		if (event != null) {
			return event.getEventLabel();
		}
		
		return null;
	}
	
	private UserSummary getUser(String emailAddress) {
		if (StringUtils.isBlank(emailAddress)) {
			emailAddress = AuthUtil.getCurrentUser().getEmailAddress();
		}
		
		UserSummary userSummary = new UserSummary();
		userSummary.setEmailAddress(emailAddress);
		
		return userSummary;
	}
	
	private boolean isPrimarySpecimen(MasterSpecimenDetail detail) {
		return StringUtils.isBlank(detail.getLineage()) || detail.getLineage().equals(Specimen.NEW);
	}
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}
}
