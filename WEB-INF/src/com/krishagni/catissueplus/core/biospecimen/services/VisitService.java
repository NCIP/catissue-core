
package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.events.FileDetail;
import com.krishagni.catissueplus.core.biospecimen.events.LabelPrintJobSummary;
import com.krishagni.catissueplus.core.biospecimen.events.PrintVisitNameDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprFileDownloadDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprLockDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsListCriteria;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.LabelPrinter;

public interface VisitService {
	public ResponseEvent<VisitDetail> getVisit(RequestEvent<EntityQueryCriteria> req);

	public ResponseEvent<List<VisitDetail>> getVisits(RequestEvent<VisitsListCriteria> criteria);

	public ResponseEvent<VisitDetail> addVisit(RequestEvent<VisitDetail> req);
	
	public ResponseEvent<VisitDetail> updateVisit(RequestEvent<VisitDetail> req);
	
	public ResponseEvent<VisitDetail> patchVisit(RequestEvent<VisitDetail> req);
	
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<EntityQueryCriteria> req);
	
	public ResponseEvent<VisitDetail> deleteVisit(RequestEvent<EntityQueryCriteria> req);	
			
	public ResponseEvent<VisitSpecimenDetail> collectVisitAndSpecimens(RequestEvent<VisitSpecimenDetail> req);

	public ResponseEvent<LabelPrintJobSummary> printVisitNames(RequestEvent<PrintVisitNameDetail> req);

	//
	// SPR APIs
	//
	public ResponseEvent<FileDetail> getSpr(RequestEvent<SprFileDownloadDetail> req);
	
	public ResponseEvent<String> uploadSprFile(RequestEvent<SprDetail> req);

	public ResponseEvent<String> updateSprText(RequestEvent<SprDetail> req);

	public ResponseEvent<Boolean> deleteSprFile(RequestEvent<EntityQueryCriteria> req);

	public ResponseEvent<SprLockDetail> updateSprLockStatus(RequestEvent<SprLockDetail> req);

	//
	// Internal APIs
	//
	public LabelPrinter<Visit> getLabelPrinter();

	public List<Visit> getVisitsByName(List<String> visitNames);

	public List<Visit> getSpecimenVisits(List<String> specimenLabels);
}
