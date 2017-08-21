package com.krishagni.openspecimen.custom.demo.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
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
import com.krishagni.catissueplus.core.de.services.FormService;
import com.krishagni.openspecimen.custom.demo.events.SpecimenCollectionDetail;
import com.krishagni.openspecimen.custom.demo.services.SpecimenCollectionService;

public class SpecimenCollectionServiceImpl implements SpecimenCollectionService {
	
	private DaoFactory daoFactory;
	
	private CollectionProtocolRegistrationService cprSvc;
	
	private VisitService visitService;
	
	private SpecimenService specimenSvc;
	
	private FormService formSvc;
	
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenCollectionDetail> collect(RequestEvent<SpecimenCollectionDetail> req) {
		try {
			SpecimenCollectionDetail result = new SpecimenCollectionDetail(); 
			
			//
			// Step 0: Obtain CP object
			//
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(CP_TITLE);			
			
			//
			// Step 1: Register participant to CP and its additional properties
			//
			
			// Step 1.1: Register
			CollectionProtocolRegistrationDetail cpr = register(cp, req.getPayload());
			result.setCpr(cpr);
			
			// Step 1.2: DE props - Smoking history
			savePatientSmokingHistory(cp, cpr.getId(), req.getPayload());
						
			//
			// Step 2: Add visit
			//
			VisitDetail visit = addVisit(cp, cpr.getId(), req.getPayload());
			result.setVisit(visit);
			
			//
			// Step 3: Create Specimens
			//
			
			// Step 3.1: Blood Specimen
			result.setBlood(createBloodSpecimen(cp, visit, req.getPayload()));
			
			// Step 3.2: DE props - Ischemia Time
			saveBloodSpecimenExtn(cp, result.getBlood().getId(), req.getPayload());

			// Step 3.2: Tissue Specimen
			result.setFrozenTissue(createFrozenTissue(cp, visit, req.getPayload()));
			
			return ResponseEvent.response(result);			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}		
	}
	
	private CollectionProtocolRegistrationDetail register(CollectionProtocol cp, SpecimenCollectionDetail input) {
		Date regDate = Calendar.getInstance().getTime();
		
		VisitDetail visit = input.getVisit();
		if (visit.getVisitDate() != null) {
			regDate = visit.getVisitDate();
		}

		CollectionProtocolRegistrationDetail cpr = input.getCpr();
		cpr.setCpId(cp.getId());
		cpr.setRegistrationDate(regDate);			
			
		RequestEvent<CollectionProtocolRegistrationDetail> req = new RequestEvent<CollectionProtocolRegistrationDetail>(cpr);
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	private void savePatientSmokingHistory(CollectionProtocol cp, Long cprId, SpecimenCollectionDetail input) {
		
	}
	
	private VisitDetail addVisit(CollectionProtocol cp, Long cprId, SpecimenCollectionDetail input) {
		CollectionProtocolEvent cpe = cp.getCollectionProtocolEvents().iterator().next();
		
		VisitDetail visit = input.getVisit();
		visit.setEventId(cpe.getId());
		visit.setCprId(cprId);
		visit.setCpTitle(cp.getTitle());
		visit.setCpId(cp.getId());

		visit.setStatus(Visit.VISIT_STATUS_COMPLETED);
		
		RequestEvent<VisitDetail> req = new RequestEvent<VisitDetail>(visit);
		ResponseEvent<VisitDetail> resp = visitService.addVisit(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	private SpecimenDetail createBloodSpecimen(CollectionProtocol cp, VisitDetail visit, SpecimenCollectionDetail input) {
		CollectionProtocolEvent cpe = cp.getCollectionProtocolEvents().iterator().next(); 
		SpecimenRequirement bloodReq = getByClassAndType(cpe.getSpecimenRequirements(), "Fluid", "Whole Blood");		
		return createSpecimen(visit, bloodReq, input.getBlood());
	}
	
	private void saveBloodSpecimenExtn(CollectionProtocol cp, Long specimenId, SpecimenCollectionDetail input) {
		
	}
	
	private SpecimenDetail createFrozenTissue(CollectionProtocol cp, VisitDetail visit, SpecimenCollectionDetail input) {
		CollectionProtocolEvent cpe = cp.getCollectionProtocolEvents().iterator().next();
		SpecimenRequirement frozenTissueReq = getByClassAndType(cpe.getSpecimenRequirements(), "Tissue", "Frozen Tissue");
		return createSpecimen(visit, frozenTissueReq, input.getFrozenTissue());
	}
	
	private SpecimenRequirement getByClassAndType(Set<SpecimenRequirement> srs, String klass, String type) {
		for (SpecimenRequirement sr : srs) {
			if (sr.getSpecimenClass().equals(klass) && sr.getSpecimenType().equals(type)) {
				return sr;
			}
		}
		
		return null;		
	}
	
	private SpecimenDetail createSpecimen(VisitDetail visit, SpecimenRequirement sr, SpecimenDetail specimen) {
		specimen.setVisitId(visit.getId());
		specimen.setLineage(Specimen.NEW);
		specimen.setStatus(Specimen.COLLECTED);
		specimen.setCreatedOn(Calendar.getInstance().getTime());
		specimen.setReqId(sr.getId());
		specimen.setSpecimenClass(sr.getSpecimenClass());
		specimen.setType(sr.getSpecimenType());
		
		RequestEvent<SpecimenDetail> req = new RequestEvent<SpecimenDetail>(specimen);
		ResponseEvent<SpecimenDetail> resp = specimenSvc.createSpecimen(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}


	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public CollectionProtocolRegistrationService getCprSvc() {
		return cprSvc;
	}

	public void setCprSvc(CollectionProtocolRegistrationService cprSvc) {
		this.cprSvc = cprSvc;
	}
	
	public VisitService getVisitService() {
		return visitService;
	}

	public void setVisitService(VisitService visitService) {
		this.visitService = visitService;
	}

	public SpecimenService getSpecimenSvc() {
		return specimenSvc;
	}

	public void setSpecimenSvc(SpecimenService specimenSvc) {
		this.specimenSvc = specimenSvc;
	}

	public FormService getFormSvc() {
		return formSvc;
	}

	public void setFormSvc(FormService formSvc) {
		this.formSvc = formSvc;
	}

	private static final String CP_TITLE = "Custom Protocol";
}
