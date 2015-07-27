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
import com.krishagni.openspecimen.custom.sgh.TridGenerator;
import com.krishagni.openspecimen.custom.sgh.events.BulkParticipantRegDetail;
import com.krishagni.openspecimen.custom.sgh.events.BulkParticipantRegSummary;
import com.krishagni.openspecimen.custom.sgh.services.CprService;
import com.krishagni.openspecimen.custom.sgh.util.SpRequirementComparator;

public class CprServiceImpl implements CprService {
	
	private DaoFactory daoFactory;
	
	private CollectionProtocolRegistrationService cprService;
	
	private SpecimenService specimenSvc;
	
	private VisitService visitService;
	
	@Autowired
	@Qualifier("specimenLabelGenerator")
	private LabelGenerator labelGenerator;
	
	public static final String ALIQUOT = "Aliquot";
	
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
			VisitDetail visit = createVisit(cprDetail, cpe, visitCnt++);
			ResponseEvent<VisitDetail> visitResp = visitService.addVisit(getRequest(visit));
			visitResp.throwErrorIfUnsuccessful();
			
			visit = visitResp.getPayload();
			
			for (SpecimenRequirement sr : cpe.getSpecimenRequirements()) {
				if(sr.isAliquot() || sr.isDerivative()){
					continue;
				}
				createAndPrintSpecimen(sr, visit, null, isPrintLabels);
			}
			visitCnt++;
		}
		return cprDetail;
	}

	private void createAndPrintSpecimen(SpecimenRequirement sr, VisitDetail visitDetail, SpecimenDetail parent, Boolean isPrintLabels) {
			Specimen specimen = sr.getSpecimen();
			specimen.setVisit(getVisit(visitDetail));
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
			if (isPrintLabels && ALIQUOT.equals(spDetail.getLineage())){
				RequestEvent<PrintSpecimenLabelDetail> printLabelsReq = getPrintLabelsReq(spDetail);
				ResponseEvent<LabelPrintJobSummary> printLabelsResp = specimenSvc.printSpecimenLabels(printLabelsReq);
				printLabelsResp.throwErrorIfUnsuccessful();
			}
			
			List<SpecimenRequirement> childList = new ArrayList<SpecimenRequirement>(sr.getChildSpecimenRequirements());
			Collections.sort(childList, new SpRequirementComparator());
			
			for (SpecimenRequirement childSr : childList) {
				createAndPrintSpecimen(childSr, visitDetail, spDetail, isPrintLabels);
			}
	}

	private Visit getVisit(VisitDetail visitDetail) {
		Visit visit = new Visit();
		visit.setName(visitDetail.getName());
		
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		cpr.setPpid(visitDetail.getPpid());
		cpr.setId(visitDetail.getCprId());
		
		CollectionProtocol cp = new CollectionProtocol();
		cp.setTitle(visitDetail.getCpTitle());
		cp.setShortTitle(visitDetail.getCpShortTitle());
		cpr.setCollectionProtocol(cp);
		
		visit.setRegistration(cpr);
		return visit;
	}

	private CollectionProtocolRegistrationDetail getRegistrationDetail(CollectionProtocol cp) {
		CollectionProtocolRegistrationDetail cprDetail = new CollectionProtocolRegistrationDetail();
		cprDetail.setRegistrationDate(new Date());
		cprDetail.setCpId(cp.getId());
		cprDetail.setPpid(TridGenerator.getNextTrid());
		
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
		String labelTmpl = getLabelTmpl(specimen);				
		String label = null;
		if (StringUtils.isNotBlank(labelTmpl)) {
			label = labelGenerator.generateLabel(labelTmpl, specimen);
		}
		return label;
	}
	
	public String getLabelTmpl(Specimen specimen) {
		String labelTmpl = null;
		
		SpecimenRequirement sr = specimen.getSpecimenRequirement();
		if (sr != null) { 
			labelTmpl = sr.getLabelFormat();
		}
				
		if (StringUtils.isNotBlank(labelTmpl)) {
			return labelTmpl;
		}
		
		CollectionProtocol cp = sr.getCollectionProtocol();
		if (specimen.isAliquot()) {
			labelTmpl = cp.getAliquotLabelFormat();
		} else if (specimen.isDerivative()) {
			labelTmpl = cp.getDerivativeLabelFormat();
		} else {
			labelTmpl = cp.getSpecimenLabelFormat();
		}			
		
		return labelTmpl;		
	}
		
	private RequestEvent<PrintSpecimenLabelDetail> getPrintLabelsReq(SpecimenDetail SpecDetail) {
		PrintSpecimenLabelDetail printLblDetail = new PrintSpecimenLabelDetail();
		printLblDetail.setSpecimenIds(java.util.Arrays.asList(SpecDetail.getId()));
		return getRequest(printLblDetail);
	}
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}
}
