package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode.*;
import static com.krishagni.catissueplus.core.common.PvAttributes.COLL_PROC;
import static com.krishagni.catissueplus.core.common.PvAttributes.CONTAINER;
import static com.krishagni.catissueplus.core.common.PvAttributes.PATH_STATUS;
import static com.krishagni.catissueplus.core.common.PvAttributes.SPECIMEN_ANATOMIC_SITE;
import static com.krishagni.catissueplus.core.common.PvAttributes.SPECIMEN_CLASS;
import static com.krishagni.catissueplus.core.common.PvAttributes.SPECIMEN_LATERALITY;
import static com.krishagni.catissueplus.core.common.service.PvValidator.isValid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.AliquotSpecimensRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.DerivedSpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement.LabelAutoPrintMode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenRequirementFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenPoolRequirements;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.util.NumUtil;
import com.krishagni.catissueplus.core.common.util.Status;

public class SpecimenRequirementFactoryImpl implements SpecimenRequirementFactory {

	private DaoFactory daoFactory;
	
	private LabelGenerator specimenLabelGenerator;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public LabelGenerator getSpecimenLabelGenerator() {
		return specimenLabelGenerator;
	}

	public void setSpecimenLabelGenerator(LabelGenerator specimenLabelGenerator) {
		this.specimenLabelGenerator = specimenLabelGenerator;
	}

	@Override
	public SpecimenRequirement createSpecimenRequirement(SpecimenRequirementDetail detail) {
		SpecimenRequirement requirement = new SpecimenRequirement();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		requirement.setId(detail.getId());
		requirement.setName(detail.getName());
		requirement.setLineage(Specimen.NEW);
		
		setCode(detail, requirement, ose);
		setLabelFormat(detail, requirement, ose);
		setLabelAutoPrintMode(detail, requirement, ose);
		setSpecimenClass(detail, requirement, ose);
		setSpecimenType(detail, requirement, ose);
		setAnatomicSite(detail, requirement, ose);
		setLaterality(detail, requirement, ose);
		setPathologyStatus(detail, requirement, ose);
		setStorageType(detail, requirement, ose);
		setInitialQty(detail, requirement, ose);
		setConcentration(detail, requirement, ose);
		setCollector(detail, requirement, ose);
		setCollectionProcedure(detail, requirement, ose);
		setCollectionContainer(detail, requirement, ose);
		setReceiver(detail, requirement, ose);		
		setCpe(detail, requirement, ose);
		
		requirement.setSortOrder(detail.getSortOrder());
		requirement.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());

		ose.checkAndThrow();

		setSpecimenPoolReqs(detail, requirement, ose);
		return requirement;
	}

	@Override
	public SpecimenRequirement createDerived(DerivedSpecimenRequirement req) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		Long parentId = req.getParentSrId();
		if (parentId == null) {
			ose.addError(PARENT_REQ_REQUIRED);
			throw ose;
		}
		
		SpecimenRequirement parent = daoFactory.getSpecimenRequirementDao().getById(parentId);
		if (parent == null) {
			ose.addError(SrErrorCode.PARENT_NOT_FOUND);
			throw ose;
		}
		
		SpecimenRequirement derived = parent.copy();
		derived.setLabelFormat(null);
		derived.setLineage(Specimen.DERIVED);
		setSpecimenClass(req.getSpecimenClass(), derived, ose);
		setSpecimenType(req.getSpecimenClass(), req.getType(), derived, ose);
		setInitialQty(req.getQuantity(), derived, ose);
		setStorageType(req.getStorageType(), derived, ose);
		setConcentration(req.getConcentration(), derived, ose);
		setPathologyStatus(req.getPathology(), derived, ose);
		setLabelFormat(req.getLabelFmt(), derived, ose);
		setLabelAutoPrintMode(req.getLabelAutoPrintMode(), derived, ose);
		setCode(req.getCode(), derived, ose);
		derived.setName(req.getName());
		
		ose.checkAndThrow();
		derived.setParentSpecimenRequirement(parent);
		return derived;
	}
	
	@Override
	public SpecimenRequirement createForUpdate(String lineage, SpecimenRequirementDetail req) {
		SpecimenRequirement sr = new SpecimenRequirement();
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		sr.setName(req.getName());
		sr.setSortOrder(req.getSortOrder());
		
		//
		// Specimen class and type are set here so that properties dependent on these can
		// be calculated and set appropriately. 
		//
		setSpecimenClass(req, sr, ose);
		setSpecimenType(req, sr, ose);		
		setInitialQty(req, sr, ose);
		setStorageType(req, sr, ose);
		setLabelFormat(req, sr, ose);
		setLabelAutoPrintMode(req, sr, ose);
		setCode(req, sr, ose);
		setConcentration(req, sr, ose);
		
		if (!lineage.equals(Specimen.ALIQUOT)) {
			setPathologyStatus(req, sr, ose);
		}

		if (!lineage.equals(Specimen.NEW)) {
			return sr;
		}
		
		setAnatomicSite(req, sr, ose);
		setLaterality(req, sr, ose);
		setCollector(req, sr, ose);
		setCollectionProcedure(req, sr, ose);
		setCollectionContainer(req, sr, ose);
		setReceiver(req, sr, ose);		
		
		ose.checkAndThrow();	
		return sr;		
	}
	
	@Override
	public List<SpecimenRequirement> createAliquots(AliquotSpecimensRequirement req) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		Long parentSrId = req.getParentSrId();
		if (parentSrId == null) {
			ose.addError(PARENT_REQ_REQUIRED);
			throw ose;
		}
		
		SpecimenRequirement parent = daoFactory.getSpecimenRequirementDao().getById(parentSrId);
		if (parent == null) {
			ose.addError(SrErrorCode.PARENT_NOT_FOUND);
			throw ose;
		}
		
		if (req.getNoOfAliquots() == null || req.getNoOfAliquots() < 1L) {
			ose.addError(SrErrorCode.INVALID_ALIQUOT_CNT);
			throw ose;
		}
		
		if (req.getQtyPerAliquot() == null || NumUtil.lessThanEqualsZero(req.getQtyPerAliquot())) {
			ose.addError(SrErrorCode.INVALID_QTY);
		}
		
		BigDecimal total = NumUtil.multiply(req.getQtyPerAliquot(), req.getNoOfAliquots());
		if (NumUtil.greaterThan(total, parent.getQtyAfterAliquotsUse())) {
			ose.addError(SrErrorCode.INSUFFICIENT_QTY);
		}
		
		List<SpecimenRequirement> aliquots = new ArrayList<SpecimenRequirement>();
		for (int i = 0; i < req.getNoOfAliquots(); ++i) {
			SpecimenRequirement aliquot = parent.copy();
			aliquot.setLabelFormat(null);
			aliquot.setLineage(Specimen.ALIQUOT);
			setStorageType(req.getStorageType(), aliquot, ose);
			setLabelFormat(req.getLabelFmt(), aliquot, ose);
			setLabelAutoPrintMode(req.getLabelAutoPrintMode(), aliquot, ose);
			aliquot.setInitialQuantity(req.getQtyPerAliquot());
			aliquot.setParentSpecimenRequirement(parent);
			aliquots.add(aliquot);
			
			ose.checkAndThrow();
		}

		return aliquots;
	}
	
	@Override
	public List<SpecimenRequirement> createSpecimenPoolReqs(SpecimenPoolRequirements req) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		Long pooledSpmnReqId = req.getPooledSpecimenReqId();
		if (pooledSpmnReqId == null) {
			ose.addError(POOLED_SPMN_REQ);
			throw ose;
		}

		SpecimenRequirement pooledSpecimenReq = daoFactory.getSpecimenRequirementDao().getById(pooledSpmnReqId);
		if (pooledSpecimenReq == null) {
			ose.addError(POOLED_SPMN_REQ_NOT_FOUND, pooledSpmnReqId);
			throw ose;
		}

		if (pooledSpecimenReq.getParentSpecimenRequirement() != null || pooledSpecimenReq.getPooledSpecimenRequirement() != null) {
			ose.addError(INVALID_POOLED_SPMN, pooledSpmnReqId);
			throw ose;
		}

		List<SpecimenRequirement> specimenPoolReqs = new ArrayList<SpecimenRequirement>();
		for (SpecimenRequirementDetail detail : req.getSpecimenPoolReqs()) {
			specimenPoolReqs.add(createSpecimenPoolReq(pooledSpecimenReq, detail, ose));
			ose.checkAndThrow();
		}

		return specimenPoolReqs;
	}

	private void setCode(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		setCode(detail.getCode(), sr, ose);
	}
	
	private void setCode(String code, SpecimenRequirement sr, OpenSpecimenException ose) {
		if (StringUtils.isNotBlank(code)) {
			sr.setCode(code.trim());
		} else {
			sr.setCode(null);
		}
	}
	
	private void setLabelFormat(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		setLabelFormat(detail.getLabelFmt(), sr, ose);
	}
	
	private void setLabelFormat(String labelFmt, SpecimenRequirement sr, OpenSpecimenException ose) {
		if (StringUtils.isBlank(labelFmt)) {
			return;
		}
		
		if (!specimenLabelGenerator.isValidLabelTmpl(labelFmt)) {
			ose.addError(INVALID_LABEL_FMT);
		}
		
		sr.setLabelFormat(labelFmt);
	}
	

	private void setLabelAutoPrintMode(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		setLabelAutoPrintMode(detail.getLabelAutoPrintMode(), sr, ose);
	}
	
	private void setLabelAutoPrintMode(String input, SpecimenRequirement sr, OpenSpecimenException ose) {
		if (StringUtils.isBlank(input)) {
			return;
		}
		
		LabelAutoPrintMode labelAutoPrintMode = null;
		try {
			labelAutoPrintMode = LabelAutoPrintMode.valueOf(input); 
		} catch (IllegalArgumentException iae) {
			ose.addError(INVALID_LABEL_AUTO_PRINT_MODE, input);
			return;
		}
		
		sr.setLabelAutoPrintMode(labelAutoPrintMode);
	}

	private void setSpecimenClass(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		setSpecimenClass(detail.getSpecimenClass(), sr, ose);
	}
	
	private void setSpecimenClass(String specimenClass, SpecimenRequirement sr, OpenSpecimenException ose) {
		ensureNotEmptyAndValid(SPECIMEN_CLASS, specimenClass, SPECIMEN_CLASS_REQUIRED, INVALID_SPECIMEN_CLASS, ose);
		sr.setSpecimenClass(specimenClass);
	}
	
	private void setSpecimenType(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		setSpecimenType(detail.getSpecimenClass(), detail.getType(), sr, ose);
	}
	
	private void setSpecimenType(String specimenClass, String type, SpecimenRequirement sr, OpenSpecimenException ose) {
		if (StringUtils.isBlank(type)) {
			ose.addError(SPECIMEN_TYPE_REQUIRED);
			return;
		}
		
		if (!isValid(SPECIMEN_CLASS, specimenClass, type)) {
			ose.addError(INVALID_SPECIMEN_TYPE);
			return;
		}
				
		sr.setSpecimenType(type);		
	}
	
	private void setAnatomicSite(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		String anatomicSite = detail.getAnatomicSite();
		if (StringUtils.isBlank(anatomicSite)) {
			ose.addError(ANATOMIC_SITE_REQUIRED);
			return;
		}
		
		if (!isValid(SPECIMEN_ANATOMIC_SITE, anatomicSite, true)) {
			ose.addError(INVALID_ANATOMIC_SITE, anatomicSite);
			return;
		}
		
		sr.setAnatomicSite(anatomicSite);
	}
	
	private void setLaterality(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		String laterality = detail.getLaterality();
		ensureNotEmptyAndValid(SPECIMEN_LATERALITY, laterality, LATERALITY_REQUIRED, INVALID_LATERALITY, ose);
		sr.setLaterality(laterality);
	}
	
	private void setPathologyStatus(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		setPathologyStatus(detail.getPathology(), sr, ose);
	}
	
	private void setPathologyStatus(String pathology, SpecimenRequirement sr, OpenSpecimenException ose) {
		if (StringUtils.isBlank(pathology) && sr.isDerivative()) {
			//
			// If pathology status is not specified for derivative requirement
			// then its value is picked from parent requirement
			//
			return;
		}

		ensureNotEmptyAndValid(PATH_STATUS, pathology, PATHOLOGY_STATUS_REQUIRED, INVALID_PATHOLOGY_STATUS, ose);
		sr.setPathologyStatus(pathology);
	}
	
	private void setStorageType(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		setStorageType(detail.getStorageType(), sr, ose);
	}

	private void setStorageType(String storageType, SpecimenRequirement sr, OpenSpecimenException ose) {
		storageType = ensureNotEmpty(storageType, SrErrorCode.STORAGE_TYPE_REQUIRED, ose);
		sr.setStorageType(storageType);
		// TODO: check for valid storage type
	}
	
	private void setInitialQty(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		setInitialQty(detail.getInitialQty(), sr, ose);
	}
		
	private void setInitialQty(BigDecimal initialQty, SpecimenRequirement sr, OpenSpecimenException ose) {
		if (initialQty == null || NumUtil.lessThanZero(initialQty)) {
			ose.addError(INVALID_QTY);
			return;
		}
		
		sr.setInitialQuantity(initialQty);
	}

	private void setConcentration(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		setConcentration(detail.getConcentration(), sr, ose);
	}
	
	private void setConcentration(BigDecimal concentration, SpecimenRequirement sr, OpenSpecimenException ose) {
		if (concentration != null && NumUtil.lessThanZero(concentration)) {
			ose.addError(CONCENTRATION_MUST_BE_POSITIVE);
			return;
		}
		
		sr.setConcentration(concentration);
	}
	
	private void setCollector(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		sr.setCollector(ensureValidUser(detail.getCollector(), COLLECTOR_NOT_FOUND, ose));
	}

	private void setCollectionProcedure(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		String collProc = detail.getCollectionProcedure();
		ensureNotEmptyAndValid(COLL_PROC, collProc, COLL_PROC_REQUIRED, INVALID_COLL_PROC, ose);
		sr.setCollectionProcedure(collProc);
	}

	private void setCollectionContainer(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		String collCont = detail.getCollectionContainer();
		ensureNotEmptyAndValid(CONTAINER, collCont, COLL_CONT_REQUIRED, INVALID_COLL_CONT, ose);		
		sr.setCollectionContainer(collCont);
	}

	private void setReceiver(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		sr.setReceiver(ensureValidUser(detail.getReceiver(), RECEIVER_NOT_FOUND, ose));
	}

	private void setCpe(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		Long eventId = detail.getEventId();
		if (eventId == null) {
			ose.addError(CPE_REQUIRED);
			return;
		}
		
		CollectionProtocolEvent cpe = daoFactory.getCollectionProtocolDao().getCpe(eventId);
		if (cpe == null) {
			ose.addError(CpeErrorCode.NOT_FOUND);
		}
		
		sr.setCollectionProtocolEvent(cpe);
	}

	private void setSpecimenPoolReqs(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		if (sr.getParentSpecimenRequirement() != null || sr.getPooledSpecimenRequirement() != null) {
			return;
		}

		if (CollectionUtils.isEmpty(detail.getSpecimensPool())) {
			return;
		}

		for (SpecimenRequirementDetail specimenPoolReqDetail : detail.getSpecimensPool()) {
			sr.getSpecimenPoolReqs().add(createSpecimenPoolReq(sr, specimenPoolReqDetail, ose));
			ose.checkAndThrow();
		}
	}

	private SpecimenRequirement createSpecimenPoolReq(SpecimenRequirement pooledSpmnReq, SpecimenRequirementDetail req, OpenSpecimenException ose) {
		SpecimenRequirement specimenPoolReq = pooledSpmnReq.copy();
		setInitialQty(req, specimenPoolReq, ose);
		setCode(req, specimenPoolReq, ose);
		setLabelAutoPrintMode(req, specimenPoolReq, ose);
		specimenPoolReq.setPooledSpecimenRequirement(pooledSpmnReq);
		return specimenPoolReq;
	}
		
	private String ensureNotEmptyAndValid(String attr, String value, ErrorCode req, ErrorCode invalid, OpenSpecimenException ose) {
		value = ensureNotEmpty(value, req, ose);
		if (value != null) {
			value = ensureValid(attr, value, invalid, ose);
		}
		
		return value;
	}
	
	private String ensureValid(String attr, String value, ErrorCode invalid, OpenSpecimenException ose) {
		if (!isValid(attr, value)) {
			ose.addError(invalid, value);
			return null;
		}
		
		return value;
	}
	
	private String ensureNotEmpty(String value, ErrorCode required, OpenSpecimenException ose) {
		if (StringUtils.isBlank(value)) {
			ose.addError(required);
			return null;
		}
		
		return value;
	}
	
	private User ensureValidUser(UserSummary userSummary, ErrorCode notFound, OpenSpecimenException ose) {
		if (userSummary == null) {
			return null;
		}
		
		User user = null;
		if (userSummary.getId() != null) {
			user = daoFactory.getUserDao().getById(userSummary.getId());
		} else if (userSummary.getLoginName() != null && userSummary.getDomain() != null) {
			user = daoFactory.getUserDao().getUser(userSummary.getLoginName(), userSummary.getDomain());
		}
		
		if (user == null) {
			ose.addError(notFound);
		}
		
		return user;		
	}	
}
