package com.krishagni.openspecimen.custom.demo.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.events.AddVisitEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitAddedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.openspecimen.custom.demo.events.CollectSpecimensEvent;
import com.krishagni.openspecimen.custom.demo.events.SpecimenCollectionDetail;
import com.krishagni.openspecimen.custom.demo.events.SpecimensCollectedEvent;
import com.krishagni.openspecimen.custom.demo.services.SpecimenCollectionService;

public class SpecimenCollectionServiceImpl implements SpecimenCollectionService {
	
	private DaoFactory daoFactory;
	
	private CollectionProtocolRegistrationService cprSvc;
	
	private SpecimenService specimenSvc;
	
	
	@Override
	@PlusTransactional
	public SpecimensCollectedEvent collect(CollectSpecimensEvent req) {
		try {
			SpecimenCollectionDetail result = new SpecimenCollectionDetail(); 
			
			//
			// Step 0: Obtain CP object
			//
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(CP_TITLE);			
			
			//
			// Step 1: Register participant to CP
			//
			CollectionProtocolRegistrationDetail cpr = register(cp, req);
			result.setCpr(cpr);
						
			//
			// Step 2: Add visit
			//
			VisitDetail visit = addVisit(cp, cpr.getId(), req);
			result.setVisit(visit);
			
			//
			// Step 3: Create Specimens
			//
			
			// Step 3.1: Blood Specimen
			result.setBlood(createBloodSpecimen(cp, visit, req));

			// Step 3.2: Tissue Specimen
			result.setFrozenTissue(createFrozenTissue(cp, visit, req));
			
			return SpecimensCollectedEvent.ok(result);			
		} catch (ObjectCreationException oce) {
			return SpecimensCollectedEvent.badRequest(oce);
		} catch (Exception e) {
			return SpecimensCollectedEvent.serverError(e);
		}		
	}
	
	private CollectionProtocolRegistrationDetail register(CollectionProtocol cp, CollectSpecimensEvent input) {
		Date regDate = Calendar.getInstance().getTime();
		
		VisitDetail visit = input.getCollectionDetail().getVisit();
		if (visit.getVisitDate() != null) {
			regDate = visit.getVisitDate();
		}

		CollectionProtocolRegistrationDetail cpr = input.getCollectionDetail().getCpr();
		cpr.setCpId(cp.getId());
		cpr.setRegistrationDate(regDate);			
			
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCprDetail(cpr);
		req.setSessionDataBean(input.getSessionDataBean());
			
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		if (!resp.isSuccess()) {
			resp.raiseException();	
		}
		
		return resp.getCprDetail();
	}
	
	private VisitDetail addVisit(CollectionProtocol cp, Long cprId, CollectSpecimensEvent input) {
		CollectionProtocolEvent cpe = cp.getCollectionProtocolEvents().iterator().next();
		
		VisitDetail visit = input.getCollectionDetail().getVisit();
		visit.setCpeId(cpe.getId());
		visit.setCprId(cprId);
		visit.setCpTitle(cp.getTitle());
		visit.setVisitStatus("Complete"); // TODO: hard coded
		
		AddVisitEvent req = new AddVisitEvent();
		req.setVisit(visit);
		req.setSessionDataBean(input.getSessionDataBean());
		
		VisitAddedEvent resp = cprSvc.addVisit(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getVisit();
	}
	
	private SpecimenDetail createBloodSpecimen(CollectionProtocol cp, VisitDetail visit, CollectSpecimensEvent input) {
		CollectionProtocolEvent cpe = cp.getCollectionProtocolEvents().iterator().next(); 
		SpecimenRequirement bloodReq = getByClassAndType(cpe.getSpecimenRequirements(), "Fluid", "Whole Blood");		
		return createSpecimen(visit, bloodReq, input.getCollectionDetail().getBlood());
	}
	
	private SpecimenDetail createFrozenTissue(CollectionProtocol cp, VisitDetail visit, CollectSpecimensEvent input) {
		CollectionProtocolEvent cpe = cp.getCollectionProtocolEvents().iterator().next();
		SpecimenRequirement frozenTissueReq = getByClassAndType(cpe.getSpecimenRequirements(), "Tissue", "Frozen Tissue");
		return createSpecimen(visit, frozenTissueReq, input.getCollectionDetail().getFrozenTissue());
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
		specimen.setLineage("New");
		specimen.setCollectionStatus("Collected");
		specimen.setCreatedOn(Calendar.getInstance().getTime());
		specimen.setRequirementId(sr.getId());
		specimen.setSpecimenClass(sr.getSpecimenClass());
		specimen.setSpecimenType(sr.getSpecimenType());
		
		CreateSpecimenEvent req = new CreateSpecimenEvent();
		req.setSpecimen(specimen);
		req.setVisitId(visit.getId());
		
		SpecimenCreatedEvent resp = specimenSvc.createSpecimen(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getSpecimen();
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
	
	public SpecimenService getSpecimenSvc() {
		return specimenSvc;
	}

	public void setSpecimenSvc(SpecimenService specimenSvc) {
		this.specimenSvc = specimenSvc;
	}

	private static final String CP_TITLE = "Advanced Specimen Collection";
}
