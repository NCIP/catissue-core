package com.krishagni.catissueplus.core.biospecimen.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol.SpecimenLabelAutoPrintMode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.NumUtil;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

@Audited
public class SpecimenRequirement extends BaseEntity implements Comparable<SpecimenRequirement> {
	private String name;
	
	private String code;
	
	private String lineage;
		
	private String specimenClass;

	private String specimenType;
	
	private String anatomicSite;

	private String laterality;
			
	private String pathologyStatus;
	
	private String storageType;
	
	private BigDecimal initialQuantity;
	
	private BigDecimal concentration;
	
	private User collector;

	private String collectionProcedure;

	private String collectionContainer;

	private User receiver;

	private CollectionProtocolEvent collectionProtocolEvent;

	private String labelFormat;
	
	private SpecimenLabelAutoPrintMode labelAutoPrintMode;
	
	private Integer labelPrintCopies;
	
	private Integer sortOrder;

	private String activityStatus;
			
	private SpecimenRequirement parentSpecimenRequirement;
	
	private Set<SpecimenRequirement> childSpecimenRequirements = new LinkedHashSet<SpecimenRequirement>();

	private SpecimenRequirement pooledSpecimenRequirement;

	private Set<SpecimenRequirement> specimenPoolReqs = new LinkedHashSet<SpecimenRequirement>();

	private Set<Specimen> specimens = new HashSet<Specimen>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
	}

	public String getSpecimenClass() {
		return specimenClass;
	}

	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	public String getAnatomicSite() {
		return anatomicSite;
	}

	public void setAnatomicSite(String anatomicSite) {
		this.anatomicSite = anatomicSite;
	}

	public String getLaterality() {
		return laterality;
	}

	public void setLaterality(String laterality) {
		this.laterality = laterality;
	}

	public String getPathologyStatus() {
		return pathologyStatus;
	}

	public void setPathologyStatus(String pathologyStatus) {
		this.pathologyStatus = pathologyStatus;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public BigDecimal getInitialQuantity() {
		return initialQuantity;
	}

	public void setInitialQuantity(BigDecimal initialQuantity) {
		this.initialQuantity = initialQuantity;
	}

	public BigDecimal getConcentration() {
		return concentration;
	}

	public void setConcentration(BigDecimal concentration) {
		this.concentration = concentration;
	}

	public User getCollector() {
		return collector;
	}

	public void setCollector(User collector) {
		this.collector = collector;
	}

	public String getCollectionProcedure() {
		return collectionProcedure;
	}

	public void setCollectionProcedure(String collectionProcedure) {
		this.collectionProcedure = collectionProcedure;
	}

	public String getCollectionContainer() {
		return collectionContainer;
	}

	public void setCollectionContainer(String collectionContainer) {
		this.collectionContainer = collectionContainer;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	@NotAudited
	public CollectionProtocolEvent getCollectionProtocolEvent() {
		return collectionProtocolEvent;
	}

	public void setCollectionProtocolEvent(CollectionProtocolEvent collectionProtocolEvent) {
		this.collectionProtocolEvent = collectionProtocolEvent;
	}
	
	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocolEvent != null ? collectionProtocolEvent.getCollectionProtocol() : null;
	}

	public String getLabelFormat() {
		return labelFormat;
	}

	public void setLabelFormat(String labelFormat) {
		this.labelFormat = labelFormat;
	}
	
	public SpecimenLabelAutoPrintMode getLabelAutoPrintMode() {
		return labelAutoPrintMode;
	}
	
	public SpecimenLabelAutoPrintMode getLabelAutoPrintModeToUse() {
		if (labelAutoPrintMode != null) {
			return labelAutoPrintMode;
		}

		CpSpecimenLabelPrintSetting setting = getCollectionProtocol().getSpmnLabelPrintSetting(getLineage());
		return setting != null ? setting.getPrintMode() : null;
	}

	public void setLabelAutoPrintMode(SpecimenLabelAutoPrintMode labelAutoPrintMode) {
		this.labelAutoPrintMode = labelAutoPrintMode;
	}

	public Integer getLabelPrintCopies() {
		return labelPrintCopies;
	}

	public void setLabelPrintCopies(Integer labelPrintCopies) {
		this.labelPrintCopies = labelPrintCopies;
	}
	
	public Integer getLabelPrintCopiesToUse() {
		if (labelPrintCopies != null) {
			return labelPrintCopies;
		}
		
		CpSpecimenLabelPrintSetting setting = getCollectionProtocol().getSpmnLabelPrintSetting(getLineage());
		return setting != null ? setting.getCopies() : null;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	@NotAudited
	public SpecimenRequirement getParentSpecimenRequirement() {
		return parentSpecimenRequirement;
	}

	public void setParentSpecimenRequirement(SpecimenRequirement parentSpecimenRequirement) {
		this.parentSpecimenRequirement = parentSpecimenRequirement;
	}

	@NotAudited
	public Set<SpecimenRequirement> getChildSpecimenRequirements() {
		return childSpecimenRequirements;
	}

	public void setChildSpecimenRequirements(Set<SpecimenRequirement> childSpecimenRequirements) {
		this.childSpecimenRequirements = childSpecimenRequirements;
	}

	public List<SpecimenRequirement> getOrderedChildRequirements() {
		List<SpecimenRequirement> childReqs = new ArrayList<SpecimenRequirement>(getChildSpecimenRequirements());
		Collections.sort(childReqs);
		return childReqs;
	}

	@NotAudited
	public SpecimenRequirement getPooledSpecimenRequirement() {
		return pooledSpecimenRequirement;
	}

	public void setPooledSpecimenRequirement(SpecimenRequirement pooledSpecimenRequirement) {
		this.pooledSpecimenRequirement = pooledSpecimenRequirement;
	}

	@NotAudited
	public Set<SpecimenRequirement> getSpecimenPoolReqs() {
		return specimenPoolReqs;
	}

	public void setSpecimenPoolReqs(Set<SpecimenRequirement> specimenPoolReqs) {
		this.specimenPoolReqs = specimenPoolReqs;
	}

	public List<SpecimenRequirement> getOrderedSpecimenPoolReqs() {
		List<SpecimenRequirement> pool = new ArrayList<SpecimenRequirement>(getSpecimenPoolReqs());
		Collections.sort(pool);
		return pool;
	}

	@NotAudited
	public Set<Specimen> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(Set<Specimen> specimens) {
		this.specimens = specimens;
	}
	
	public boolean isPrimary() {
		return getLineage().equals(Specimen.NEW);
	}
	
	public boolean isAliquot() {
		return getLineage().equals(Specimen.ALIQUOT);
	}
	
	public boolean isDerivative() {
		return getLineage().equals(Specimen.DERIVED);
	}
	
	public boolean isPooledSpecimenReq() {
		return CollectionUtils.isNotEmpty(getSpecimenPoolReqs());
	}

	public boolean isSpecimenPoolReq() {
		return getPooledSpecimenRequirement() != null;
	}

	public void update(SpecimenRequirement sr) {
		if (isPrimary() && !isSpecimenPoolReq()) {
			updateRequirementAttrs(sr);
		}

		if (StringUtils.isNotBlank(sr.getCode()) && !sr.getCode().equals(getCode())) {
			if (getCollectionProtocolEvent().getSrByCode(sr.getCode()) != null) {
				throw OpenSpecimenException.userError(SrErrorCode.DUP_CODE, sr.getCode());
			}
		}
		
		if (!StringUtils.equals(getName(), sr.getName())) {
			updateName(sr.getName());
		}

		setCode(sr.getCode());
		setSortOrder(sr.getSortOrder());
		setInitialQuantity(sr.getInitialQuantity());
		setStorageType(sr.getStorageType());
		setLabelFormat(sr.getLabelFormat());
		setLabelAutoPrintMode(sr.getLabelAutoPrintMode());
		setLabelPrintCopies(sr.getLabelPrintCopies());

		if (!isAliquot() && !isSpecimenPoolReq()) {
			update(sr.getAnatomicSite(), sr.getLaterality(), sr.getConcentration(),
				sr.getSpecimenClass(), sr.getSpecimenType(), sr.getPathologyStatus());
		}

		if (NumUtil.lessThanZero(getQtyAfterAliquotsUse())) {
			throw OpenSpecimenException.userError(SrErrorCode.INSUFFICIENT_QTY);
		}
		
		if (isAliquot() && NumUtil.lessThanZero(getParentSpecimenRequirement().getQtyAfterAliquotsUse())) {
			throw OpenSpecimenException.userError(SrErrorCode.INSUFFICIENT_QTY);
		}
	}
				
	public SpecimenRequirement copy() {
		SpecimenRequirement copy = new SpecimenRequirement();
		BeanUtils.copyProperties(this, copy, EXCLUDE_COPY_PROPS); 
		return copy;
	}

	public SpecimenRequirement deepCopy(CollectionProtocolEvent cpe) {
		if (cpe == null) {
			cpe = getCollectionProtocolEvent();
		}
		
		if (isAliquot()) {
			if (NumUtil.greaterThan(getInitialQuantity(), getParentSpecimenRequirement().getQtyAfterAliquotsUse())) {
				throw OpenSpecimenException.userError(SrErrorCode.INSUFFICIENT_QTY);
			}
		}
		
		return deepCopy(cpe, getParentSpecimenRequirement(), getPooledSpecimenRequirement());
	}
		
	public void addChildRequirement(SpecimenRequirement childReq) {
		childReq.setParentSpecimenRequirement(this);
		getChildSpecimenRequirements().add(childReq);
	}
	
	public void addChildRequirements(Collection<SpecimenRequirement> children) {
		for (SpecimenRequirement childReq : children) {
			addChildRequirement(childReq);
		}
	}
	
	public void addSpecimenPoolReq(SpecimenRequirement spmnPoolReq) {
		spmnPoolReq.setPooledSpecimenRequirement(this);
		getSpecimenPoolReqs().add(spmnPoolReq);
	}

	public void addSpecimenPoolReqs(Collection<SpecimenRequirement> spmnPoolReqs) {
		for (SpecimenRequirement req : spmnPoolReqs) {
			addSpecimenPoolReq(req);
		}
	}

	public BigDecimal getQtyAfterAliquotsUse() {
		BigDecimal available = getInitialQuantity();
		for (SpecimenRequirement childReq : getChildSpecimenRequirements()) {
			if (childReq.isAliquot() && childReq.getInitialQuantity() != null) {
				available =  available.subtract(childReq.getInitialQuantity());
			}
		}
		
		return available;
	}
	
	public Specimen getSpecimen() {
		Specimen specimen = new Specimen();
		specimen.setCollectionProtocol(getCollectionProtocol());
		specimen.setLineage(getLineage());
		specimen.setSpecimenClass(getSpecimenClass());
		specimen.setSpecimenType(getSpecimenType());
		specimen.setTissueSite(getAnatomicSite());
		specimen.setTissueSide(getLaterality());
		specimen.setPathologicalStatus(getPathologyStatus());
		specimen.setInitialQuantity(getInitialQuantity());
		specimen.setAvailableQuantity(getInitialQuantity());
		specimen.setConcentration(getConcentration());
		specimen.setSpecimenRequirement(this);
		return specimen;
	}
	
	public String getLabelTmpl() {
		if (StringUtils.isNotBlank(labelFormat)) {
			return labelFormat;
		}
		
		CollectionProtocol cp = getCollectionProtocolEvent().getCollectionProtocol();
		if (isAliquot()) {
			return cp.getAliquotLabelFormat();
		} else if (isDerivative()) {
			return cp.getDerivativeLabelFormat();
		} else {
			return cp.getSpecimenLabelFormat();
		}
	}
	
	public void delete() {
		for (SpecimenRequirement childReq : getChildSpecimenRequirements()) {
			childReq.delete();
		}
		
		for (SpecimenRequirement poolReq : getSpecimenPoolReqs()) {
			poolReq.delete();
		}

		setCode(Utility.getDisabledValue(getCode(), 32));
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}
		
	@Override
	public int compareTo(SpecimenRequirement other) {
		if (getSortOrder() != null && other.getSortOrder() != null) {
			return getSortOrder().compareTo(other.getSortOrder());
		} else if (getSortOrder() != null) {
			return -1;
		} else if (other.getSortOrder() != null) {
			return 1;
		} else if (getId() != null && other.getId() != null) {
			return getId().compareTo(other.getId());
		} else if (getId() != null) {
			return -1;
		} else if(other.getId() != null) {
			return 1;
		} else {
			return 0;
		}
	}

	private SpecimenRequirement deepCopy(CollectionProtocolEvent cpe, SpecimenRequirement parent, SpecimenRequirement pooledReq) {
		SpecimenRequirement result = copy();
		result.setCollectionProtocolEvent(cpe);
		result.setParentSpecimenRequirement(parent);
		result.setPooledSpecimenRequirement(pooledReq);
		
		if (isSafeToCopyCode(cpe)) {
			result.setCode(getCode());
		}

		Set<SpecimenRequirement> childSrs = new LinkedHashSet<SpecimenRequirement>();
		int order = 1;
		for (SpecimenRequirement childSr : getOrderedChildRequirements()) {
			SpecimenRequirement copiedSr = childSr.deepCopy(cpe, result, null);
			copiedSr.setSortOrder(order++);
			childSrs.add(copiedSr);
		}
		
		result.setChildSpecimenRequirements(childSrs);

		if (!Specimen.NEW.equals(getLineage())) {
			return result;
		}

		order = 1;
		Set<SpecimenRequirement> specimenPoolReqs = new LinkedHashSet<SpecimenRequirement>();
		for (SpecimenRequirement specimenPoolReq : getSpecimenPoolReqs()) {
			SpecimenRequirement copiedSr = specimenPoolReq.deepCopy(cpe, null, result);			
			copiedSr.setSortOrder(order++);
			specimenPoolReqs.add(copiedSr);
		}

		result.setSpecimenPoolReqs(specimenPoolReqs);
		return result;
	}
	
	private boolean isSafeToCopyCode(CollectionProtocolEvent cpe) {
		if (StringUtils.isBlank(getCode())) {
			return false;
		}

		if (cpe.equals(getCollectionProtocolEvent())) {
			return false;
		}

		return cpe.getSrByCode(getCode()) == null;
	}

	private void updateName(String name) {
		setName(name);

		getChildSpecimenRequirements().stream()
			.filter(SpecimenRequirement::isAliquot)
			.forEach(child -> child.updateName(name));
	}

	private void updateRequirementAttrs(SpecimenRequirement sr) {
		setCollector(sr.getCollector());
		setCollectionContainer(sr.getCollectionContainer());
		setCollectionProcedure(sr.getCollectionProcedure());
		setReceiver(sr.getReceiver());
		
		for (SpecimenRequirement childSr : getChildSpecimenRequirements()) {
			childSr.updateRequirementAttrs(sr);
		}
		
		for (SpecimenRequirement poolSr : getSpecimenPoolReqs()) {
			poolSr.updateRequirementAttrs(sr);
			poolSr.setStorageType(sr.getStorageType());
			poolSr.setLabelFormat(sr.getLabelFormat());
		}
	}
	
	private void update(String anatomicSite, String laterality, BigDecimal concentration,
		String specimenClass, String specimenType, String pathologyStatus) {

		setAnatomicSite(anatomicSite);
		setLaterality(laterality);
		setConcentration(concentration);
		setSpecimenClass(specimenClass);
		setSpecimenType(specimenType);
		setPathologyStatus(pathologyStatus);

		for (SpecimenRequirement childSr : getChildSpecimenRequirements()) {
			if (childSr.isAliquot()) {
				childSr.update(anatomicSite, laterality, concentration, specimenClass, specimenType, pathologyStatus);
			}
		}
		
		for (SpecimenRequirement poolSr : getSpecimenPoolReqs()) {
			poolSr.update(anatomicSite, laterality, concentration, specimenClass, specimenType, pathologyStatus);
		}
	}

	private static final String[] EXCLUDE_COPY_PROPS = {
		"id",
		"sortOrder",
		"code",
		"parentSpecimenRequirement",
		"childSpecimenRequirements",
		"pooledSpecimenRequirement",
		"specimenPoolReqs",
		"specimens"		
	};
}
