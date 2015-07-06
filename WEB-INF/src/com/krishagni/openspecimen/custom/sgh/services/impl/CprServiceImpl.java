package com.krishagni.openspecimen.custom.sgh.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.PrintSpecimenLabelDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.LabelPrintJobSummary;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.openspecimen.custom.sgh.SghErrorCode;
import com.krishagni.openspecimen.custom.sgh.events.BulkParticipantRegDetail;
import com.krishagni.openspecimen.custom.sgh.events.BulkParticipantRegSummary;
import com.krishagni.openspecimen.custom.sgh.services.CprService;

public class CprServiceImpl implements CprService {
	
	private DaoFactory daoFactory;
	
	private CollectionProtocolRegistrationService cprService;
	
	private SpecimenService specimenSvc;
	
	private VisitService visitService;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setCprService(CollectionProtocolRegistrationService cprService) {
		this.cprService = cprService;
	}
	
	public void setSpecimenSvc(SpecimenService specimenSvc) {
		this.specimenSvc = specimenSvc;
	}

	public void setVisitService(VisitService visitService) {
		this.visitService = visitService;
	}

	@Override
	@PlusTransactional	
	public ResponseEvent<BulkParticipantRegDetail> registerParticipants(RequestEvent<BulkParticipantRegSummary> req) {		
		try {
			BulkParticipantRegSummary regReq = req.getPayload();
			int participantCount = regReq.getParticipantCount();
			if (participantCount < 1){
				return ResponseEvent.userError(SghErrorCode.INVALID_PARTICIPANT_COUNT);
			}
			
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(regReq.getCpId());
			if (cp == null) {
				return ResponseEvent.userError(CpErrorCode.NOT_FOUND);
			}
			
			List<CollectionProtocolRegistrationDetail> registrations = new ArrayList<CollectionProtocolRegistrationDetail>();
			for (int i = 0; i < participantCount; i++){
				CollectionProtocolRegistrationDetail regDetail = registerParticipant(cp, regReq.isPrintLabels());
				registrations.add(regDetail);
			}
			
			return ResponseEvent.response(BulkParticipantRegDetail.from(regReq, registrations));			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);			
		}
	}
	
	private CollectionProtocolRegistrationDetail registerParticipant(CollectionProtocol cp, Boolean isPrintLabels) {
		CollectionProtocolRegistrationDetail cprDetail = getRegistrationDetail(cp);
		ResponseEvent<CollectionProtocolRegistrationDetail> regResp = cprService.createRegistration(getRequest(cprDetail));
		regResp.throwErrorIfUnsuccessful();
		
		cprDetail = regResp.getPayload();
		Set<CollectionProtocolEvent> eventColl = cp.getCollectionProtocolEvents();
		int visitCnt = 1;
			
		for (CollectionProtocolEvent cpe : eventColl) {
			RequestEvent<VisitSpecimenDetail> visitCollReq = getVisitCollReq(cprDetail, cpe, visitCnt++);
			ResponseEvent<VisitSpecimenDetail> visitCollResp = visitService.collectVisitAndSpecimens(visitCollReq);
			visitCollResp.throwErrorIfUnsuccessful();
			if (isPrintLabels){
				RequestEvent<PrintSpecimenLabelDetail> printLabelsReq = getPrintLabelsReq(visitCollResp.getPayload());
				ResponseEvent<LabelPrintJobSummary> printLabelsResp = specimenSvc.printSpecimenLabels(printLabelsReq);
				printLabelsResp.throwErrorIfUnsuccessful();
			}
			visitCnt++;
		}
		return cprDetail;
	}

	private CollectionProtocolRegistrationDetail getRegistrationDetail(CollectionProtocol cp) {
		CollectionProtocolRegistrationDetail cprDetail = new CollectionProtocolRegistrationDetail();
		cprDetail.setRegistrationDate(new Date());
		cprDetail.setCpId(cp.getId()); 
		
		ParticipantDetail participant = new ParticipantDetail();
		cprDetail.setParticipant(participant);
		return cprDetail;
	}
	
	private RequestEvent<VisitSpecimenDetail> getVisitCollReq(CollectionProtocolRegistrationDetail cprDetail, CollectionProtocolEvent cpe, int visitCnt) {
		VisitDetail visit = createVisit(cprDetail, cpe);
//		visit.setName(cprDetail.getPpid() + "-v" + visitCnt);
		
		VisitSpecimenDetail visitSpecDetail = new VisitSpecimenDetail();
		visitSpecDetail.setVisit(visit);
		visitSpecDetail.setSpecimens(getSpecimensDetail(cpe.getSpecimenRequirements()));
		return getRequest(visitSpecDetail);
	}
	
	private VisitDetail createVisit(CollectionProtocolRegistrationDetail cprDetail,
			CollectionProtocolEvent cpe) {
		VisitDetail visit = new VisitDetail();
		visit.setEventId(cpe.getId());
		visit.setStatus(Status.VISIT_STATUS_PENDING.getStatus());
		visit.setSite(cpe.getDefaultSite().getName());
		visit.setCprId(cprDetail.getId());
		visit.setCpShortTitle(cpe.getCollectionProtocol().getShortTitle());
		return visit;
	}
	
	private List<SpecimenDetail> getSpecimensDetail(Set<SpecimenRequirement> specimenRequirements) {
		List<SpecimenDetail> specimens = new ArrayList<SpecimenDetail>();
		for (SpecimenRequirement sr : specimenRequirements) {
			SpecimenDetail specimen = SpecimenDetail.from(sr);
			specimen.setStatus(Status.SPECIMEN_COLLECTION_STATUS_PENDING.getStatus());
			specimens.add(specimen);
		}
		return specimens;
	}
	
	private RequestEvent<PrintSpecimenLabelDetail> getPrintLabelsReq(VisitSpecimenDetail visitSpecDetail) {
		List<Long> specimenIds = new ArrayList<Long>();
		for (SpecimenDetail specimen : visitSpecDetail.getSpecimens()) {
			specimenIds.add(specimen.getId());
		}
		
		PrintSpecimenLabelDetail printLblDetail = new PrintSpecimenLabelDetail();
		printLblDetail.setSpecimenIds(specimenIds);
		printLblDetail.setVisitId(visitSpecDetail.getVisit().getId());
		
		return getRequest(printLblDetail);
	}
	

	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}
}
