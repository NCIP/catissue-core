package com.krishagni.openspecimen.rde.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.LabelPrintJobSummary;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.rde.events.BarcodeDetail;
import com.krishagni.openspecimen.rde.events.ContainerOccupancyDetail;
import com.krishagni.openspecimen.rde.events.ParticipantRegDetail;
import com.krishagni.openspecimen.rde.events.SpecimenAndFrozenEventDetail;
import com.krishagni.openspecimen.rde.events.SpecimenPrintDetail;
import com.krishagni.openspecimen.rde.events.VisitRegDetail;

public interface SpecimenCollectionService {
	public ResponseEvent<List<ParticipantRegDetail>> registerParticipants(RequestEvent<List<ParticipantRegDetail>> req);

	public ResponseEvent<List<VisitSpecimenDetail>> getVisitsByNames(RequestEvent<List<String>> req);

	public ResponseEvent<List<VisitSpecimenDetail>> getVisitsBySpecimens(RequestEvent<List<String>> req);

	public ResponseEvent<List<BarcodeDetail>> validateVisitNames(RequestEvent<List<String>> req);
	
	public ResponseEvent<List<VisitSpecimenDetail>> registerVisitNames(RequestEvent<List<VisitRegDetail>> req);

	public ResponseEvent<List<VisitSpecimenDetail>> registerVisits(RequestEvent<List<VisitDetail>> req);
	
	public ResponseEvent<List<SpecimenDetail>> collectPrimarySpecimens(RequestEvent<List<SpecimenDetail>> req);

	public ResponseEvent<ContainerOccupancyDetail> validateOccupancyEligibility(RequestEvent<ContainerOccupancyDetail> req);
	
	public ResponseEvent<SpecimenAndFrozenEventDetail> collectChildSpecimens(RequestEvent<SpecimenAndFrozenEventDetail> req);

	public ResponseEvent<LabelPrintJobSummary> printSpecimenLabels(RequestEvent<List<SpecimenPrintDetail>> req);
}