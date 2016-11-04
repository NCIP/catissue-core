
package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.CpEntityDeleteCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.LabelPrintJobSummary;
import com.krishagni.catissueplus.core.biospecimen.events.PrintSpecimenLabelDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenAliquotsSpec;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenStatusDetail;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.LabelPrinter;

public interface SpecimenService {
	public ResponseEvent<SpecimenDetail> getSpecimen(RequestEvent<SpecimenQueryCriteria> req);
	
	public ResponseEvent<List<SpecimenInfo>> getSpecimens(RequestEvent<List<String>> req);

	public ResponseEvent<List<SpecimenInfo>> getSpecimensById(RequestEvent<List<Long>> req);

	public ResponseEvent<List<SpecimenInfo>> getPrimarySpecimensByCp(RequestEvent<Long> req);
	
	public ResponseEvent<SpecimenDetail> createSpecimen(RequestEvent<SpecimenDetail> req);
	
	public ResponseEvent<SpecimenDetail> updateSpecimen(RequestEvent<SpecimenDetail> req);
	
	public ResponseEvent<List<SpecimenDetail>> updateSpecimensStatus(RequestEvent<List<SpecimenStatusDetail>> req);

	public ResponseEvent<List<SpecimenInfo>> deleteSpecimens(RequestEvent<List<CpEntityDeleteCriteria>> req);

	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<SpecimenQueryCriteria> req);
	
	public ResponseEvent<List<SpecimenDetail>> collectSpecimens(RequestEvent<List<SpecimenDetail>> req);
	
	public ResponseEvent<List<SpecimenDetail>> createAliquots(RequestEvent<SpecimenAliquotsSpec> req);

	public ResponseEvent<SpecimenDetail> createDerivative(RequestEvent<SpecimenDetail> derivedReq);

	public ResponseEvent<Boolean> doesSpecimenExists(RequestEvent<SpecimenQueryCriteria> req);
	
	public ResponseEvent<LabelPrintJobSummary> printSpecimenLabels(RequestEvent<PrintSpecimenLabelDetail> req);
	
	/** Used mostly by plugins **/
	public LabelPrinter<Specimen> getLabelPrinter();
	
	/** Mostly present for UI **/
	public ResponseEvent<Map<String, Object>> getCprAndVisitIds(RequestEvent<Long> req);

	//
	// For internal and plugin usage purpose
	//
	public List<Specimen> getSpecimensByLabel(List<String> labels);

	public List<Specimen> getSpecimensById(List<Long> ids);
}
