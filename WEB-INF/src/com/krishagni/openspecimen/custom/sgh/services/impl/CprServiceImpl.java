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
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.openspecimen.custom.sgh.events.BulkParticipantRegDetail;
import com.krishagni.openspecimen.custom.sgh.events.ParticipantRegDetail;
import com.krishagni.openspecimen.custom.sgh.services.CprService;

public class CprServiceImpl implements CprService {
	
	private DaoFactory daoFactory;
	
	private CollectionProtocolRegistrationService cprService;
	
	private VisitService visitService;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setCprService(CollectionProtocolRegistrationService cprService) {
		this.cprService = cprService;
	}
	
	public void setVisitService(VisitService visitService) {
		this.visitService = visitService;
	}

	@Override
	@PlusTransactional	
	public ResponseEvent<BulkParticipantRegDetail> registerParticipants(RequestEvent<BulkParticipantRegDetail> req) {		
		try {
			BulkParticipantRegDetail detail = req.getPayload();
			
			int participantCount = detail.getParticipantCount();
			if(participantCount < 1){
				return ResponseEvent.userError(CpErrorCode.INVALID_PARTICIPANT_COUNT);
			}
			
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(detail.getCpId());
			if (cp == null) {
				return ResponseEvent.userError(CpErrorCode.NOT_FOUND);
			}
			
			List<ParticipantRegDetail> result = new ArrayList<ParticipantRegDetail>();

			for (int i = 0; i < participantCount; i++){
				ParticipantRegDetail regDetail = registerParticipant(cp);
				result.add(regDetail);
			}
			
			return ResponseEvent.response(new BulkParticipantRegDetail(detail.getCpId(), result));			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);			
		}
	}
	
	private ParticipantRegDetail registerParticipant(CollectionProtocol cp) {
		CollectionProtocolRegistrationDetail cprDetail = getRegistrationDetail(cp);
		ResponseEvent<CollectionProtocolRegistrationDetail> response = cprService.createRegistration(getRequest(cprDetail));
		response.throwErrorIfUnsuccessful();
		
		cprDetail = response.getPayload();
		Set<CollectionProtocolEvent> eventColl = cp.getCollectionProtocolEvents();
		int visitCount=1;
			
		for (CollectionProtocolEvent cpe : eventColl) {
			VisitSpecimenDetail visitSpecDetail = new VisitSpecimenDetail();
			VisitDetail visit = createVisit(cp, cprDetail, cpe);
//			visit.setName(cprDetail.getPpid()+"-v"+visitCount);
			visitSpecDetail.setVisit(visit);
			visitSpecDetail.setSpecimens(getSpecimenList(cpe.getSpecimenRequirements()));
			ResponseEvent<VisitSpecimenDetail> result = visitService.collectVisitAndSpecimens(getRequest(visitSpecDetail));
			
			result.throwErrorIfUnsuccessful();
		
			visitCount++;
		}
		return ParticipantRegDetail.from(response.getPayload());
	}

	private VisitDetail createVisit(CollectionProtocol cp, CollectionProtocolRegistrationDetail cprDetail,
			CollectionProtocolEvent cpe) {
		VisitDetail visit = new VisitDetail();
		visit.setEventId(cpe.getId());
		visit.setStatus(Status.VISIT_STATUS_PENDING.getStatus());
		visit.setSite(cpe.getDefaultSite().getName());
		visit.setCprId(cprDetail.getId());
		visit.setCpShortTitle(cp.getShortTitle());
		return visit;
	}

	private CollectionProtocolRegistrationDetail getRegistrationDetail(CollectionProtocol cp) {
		CollectionProtocolRegistrationDetail cprDetail = new CollectionProtocolRegistrationDetail();
		cprDetail.setRegistrationDate(new Date());
		cprDetail.setCpId(cp.getId()); 
		
		ParticipantDetail participant = new ParticipantDetail();
		cprDetail.setParticipant(participant);
		return cprDetail;
	}

	private List<SpecimenDetail> getSpecimenList(Set<SpecimenRequirement> specimenRequirements) {
		List<SpecimenDetail> specimens = new ArrayList<SpecimenDetail>();
		for (SpecimenRequirement sr : specimenRequirements) {
			SpecimenDetail specimen = SpecimenDetail.from(sr);
			specimen.setStatus(Status.SPECIMEN_COLLECTION_STATUS_PENDING.getStatus());
			specimens.add(specimen);
		}
		return specimens;
	}

	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}
}
