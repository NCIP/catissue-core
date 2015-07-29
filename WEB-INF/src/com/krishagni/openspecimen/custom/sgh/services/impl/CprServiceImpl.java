package com.krishagni.openspecimen.custom.sgh.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.LabelPrintJobSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.PrintSpecimenLabelDetail;
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
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.openspecimen.custom.sgh.SghErrorCode;
import com.krishagni.openspecimen.custom.sgh.events.BulkParticipantRegDetail;
import com.krishagni.openspecimen.custom.sgh.events.BulkParticipantRegSummary;
import com.krishagni.openspecimen.custom.sgh.services.CprService;
import com.krishagni.openspecimen.custom.sgh.services.TridGenerator;

public class CprServiceImpl implements CprService {
	
	private DaoFactory daoFactory;
	
	private CollectionProtocolRegistrationService cprService;
	
	private SpecimenService specimenSvc;
	
	private VisitService visitService;
	
	private TridGenerator tridGenerator;
	
	@Autowired
	@Qualifier("specimenLabelGenerator")
	private LabelGenerator labelGenerator;
	
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
	
	public void setTridGenerator(TridGenerator tridGenerator) {
		this.tridGenerator = tridGenerator;
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
		
		List<Long> specimenIds = new ArrayList<Long>();
		for (CollectionProtocolEvent cpe : eventColl) {
			VisitDetail visitDetail = createVisit(cprDetail, cpe, visitCnt++);
			ResponseEvent<VisitDetail> visitResp = visitService.addVisit(getRequest(visitDetail));
			visitResp.throwErrorIfUnsuccessful();
			
			visitDetail = visitResp.getPayload();
			Visit visit = getVisit(visitDetail, cpe);
			
			List<SpecimenRequirement> requirements = new ArrayList<SpecimenRequirement>(cpe.getTopLevelAnticipatedSpecimens());
			Collections.sort(requirements);
			
			for (SpecimenRequirement sr : requirements) {
				createSpecimens(sr, visit, null, specimenIds);
			}
			visitCnt++;
		}
		
		if(isPrintLabels){
			RequestEvent<PrintSpecimenLabelDetail> printLabelsReq = getPrintLabelsReq(specimenIds);
			ResponseEvent<LabelPrintJobSummary> printLabelsResp = specimenSvc.printSpecimenLabels(printLabelsReq);
			printLabelsResp.throwErrorIfUnsuccessful();
		}
		return cprDetail;
	}

	private void createSpecimens(SpecimenRequirement sr, Visit visit, SpecimenDetail parent, List<Long> specimenIds) {
			Specimen specimen = sr.getSpecimen();
			specimen.setVisit(visit);
			if(parent != null){
				Specimen parentSpecimen = new Specimen();
				parentSpecimen.setLabel(parent.getLabel());
				parentSpecimen.setId(parent.getId());
				specimen.setParentSpecimen(parentSpecimen);
			}
			
			SpecimenDetail spDetail = SpecimenDetail.from(specimen);
			spDetail.setStatus(Status.SPECIMEN_COLLECTION_STATUS_PENDING.getStatus());
			
			String label = getLabel(specimen);
			spDetail.setLabel(label);
			
			ResponseEvent<SpecimenDetail> specimenResp = specimenSvc.createSpecimen(getRequest(spDetail));
			specimenResp.throwErrorIfUnsuccessful();
			
			spDetail = specimenResp.getPayload();
			if (Specimen.ALIQUOT.equals(spDetail.getLineage())){
				specimenIds.add(spDetail.getId());
			}
			
			for (SpecimenRequirement childSr : sr.getOrderedChildRequirements()) {
				createSpecimens(childSr, visit, spDetail, specimenIds);
			}
	}

	private Visit getVisit(VisitDetail visitDetail, CollectionProtocolEvent cpe) {
		Visit visit = new Visit();
		visit.setName(visitDetail.getName());
		
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		cpr.setPpid(visitDetail.getPpid());
		cpr.setId(visitDetail.getCprId());
		cpr.setCollectionProtocol(cpe.getCollectionProtocol());
		
		visit.setRegistration(cpr);
		return visit;
	}

	private CollectionProtocolRegistrationDetail getRegistrationDetail(CollectionProtocol cp) {
		CollectionProtocolRegistrationDetail cprDetail = new CollectionProtocolRegistrationDetail();
		cprDetail.setRegistrationDate(new Date());
		cprDetail.setCpId(cp.getId());
		cprDetail.setPpid(tridGenerator.getNextTrid());
		
		ParticipantDetail participant = new ParticipantDetail();
		cprDetail.setParticipant(participant);
		return cprDetail;
	}
	
	private VisitDetail createVisit(CollectionProtocolRegistrationDetail cprDetail,
			CollectionProtocolEvent cpe, int visitCnt) {
		VisitDetail visit = new VisitDetail();
		visit.setEventId(cpe.getId());
		visit.setStatus(Status.VISIT_STATUS_PENDING.getStatus());
		visit.setSite(cpe.getDefaultSite().getName());
		visit.setCprId(cprDetail.getId());
		visit.setPpid(cprDetail.getPpid());
		visit.setCpShortTitle(cpe.getCollectionProtocol().getShortTitle());
		visit.setName(cprDetail.getPpid() + "-v" + visitCnt);
		return visit;
	}
	
	
	private String getLabel(Specimen specimen) {
		String labelTmpl = specimen.getLabelTmpl();
		String label = null;
		if (StringUtils.isNotBlank(labelTmpl)) {
			label = labelGenerator.generateLabel(labelTmpl, specimen);
		}
		return label;
	}
	
	private RequestEvent<PrintSpecimenLabelDetail> getPrintLabelsReq(List<Long> specimenIds) {
		PrintSpecimenLabelDetail printLblDetail = new PrintSpecimenLabelDetail();
		printLblDetail.setSpecimenIds(specimenIds);
		return getRequest(printLblDetail);
	}
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}
}
