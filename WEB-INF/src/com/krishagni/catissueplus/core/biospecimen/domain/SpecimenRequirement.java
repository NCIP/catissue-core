package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

@Audited
public class SpecimenRequirement {
	private Long id;
	
	private String name;
	
	private String lineage;
		
	private String specimenClass;

	private String specimenType;
	
	private String anatomicSite;

	private String laterality;
			
	private String pathologyStatus;
	
	private String storageType;
	
	private Double initialQuantity;
	
	private Double concentration;
	
	private User collector;

	private String collectionProcedure;

	private String collectionContainer;

	private User receiver;

	private CollectionProtocolEvent collectionProtocolEvent;

	private String labelFormat;
	
	private Integer sortOrder;

	private String activityStatus;
			
	private SpecimenRequirement parentSpecimenRequirement;
	
	private Set<SpecimenRequirement> childSpecimenRequirements = new HashSet<SpecimenRequirement>();

	private Set<Specimen> specimens = new HashSet<Specimen>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Double getInitialQuantity() {
		return initialQuantity;
	}

	public void setInitialQuantity(Double initialQuantity) {
		this.initialQuantity = initialQuantity;
	}

	public Double getConcentration() {
		return concentration;
	}

	public void setConcentration(Double concentration) {
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

	@NotAudited
	public Set<Specimen> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(Set<Specimen> specimens) {
		this.specimens = specimens;
	}
	
	public boolean isAliquot() {
		return getLineage().equals(Specimen.ALIQUOT);
	}
	
	public boolean isDerivative() {
		return getLineage().equals(Specimen.DERIVED);
	}
	
	public void update(SpecimenRequirement sr) {
		if (!isAliquot() && !isDerivative()) {
			updateRequirementAttrs(sr);
		}

		setName(sr.getName());
		setSortOrder(sr.getSortOrder());
		setInitialQuantity(sr.getInitialQuantity());
		setStorageType(sr.getStorageType());
		setLabelFormat(sr.getLabelFormat());
		setConcentration(sr.getConcentration());
		
		if (getQtyAfterAliquotsUse() < 0) {
			throw OpenSpecimenException.userError(SrErrorCode.INSUFFICIENT_QTY);
		}
		
		if (isAliquot() && getParentSpecimenRequirement().getQtyAfterAliquotsUse() < 0) {
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
			cpe = this.getCollectionProtocolEvent();
		}
		
		if (isAliquot()) {
			if (this.getInitialQuantity() > this.parentSpecimenRequirement.getQtyAfterAliquotsUse()) {
				throw OpenSpecimenException.userError(SrErrorCode.INSUFFICIENT_QTY);
			}
		}
				
		return deepCopy(cpe, parentSpecimenRequirement);
	}
		
	public void addChildRequirement(SpecimenRequirement childReq) {
		childReq.setParentSpecimenRequirement(this);
		childSpecimenRequirements.add(childReq);
	}
	
	public void addChildRequirements(Collection<SpecimenRequirement> children) {
		for (SpecimenRequirement childReq : children) {
			addChildRequirement(childReq);
		}
	}
	
	public Double getQtyAfterAliquotsUse() {
		Double available = initialQuantity;
		for (SpecimenRequirement childReq : childSpecimenRequirements) {
			if (childReq.isAliquot() && childReq.getInitialQuantity() != null) {
				available -= childReq.getInitialQuantity();
			}
		}
		
		return available;
	}
	
	public Specimen getSpecimen() {
		Specimen specimen = new Specimen();
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
		this.activityStatus = Status.ACTIVITY_STATUS_DISABLED.getStatus();
		for (SpecimenRequirement childReq : getChildSpecimenRequirements()) {
			childReq.delete();
		}
	}
		
	private SpecimenRequirement deepCopy(CollectionProtocolEvent cpe, SpecimenRequirement parent) {
		SpecimenRequirement result = copy();
		result.setCollectionProtocolEvent(cpe);
		result.setParentSpecimenRequirement(parent);
		
		Set<SpecimenRequirement> childSrs = new HashSet<SpecimenRequirement>();
		for (SpecimenRequirement childSr : childSpecimenRequirements) {
			childSrs.add(childSr.deepCopy(cpe, result));
		}
		
		result.setChildSpecimenRequirements(childSrs);
		return result;
	}
	
	private void updateRequirementAttrs(SpecimenRequirement sr) {
		setAnatomicSite(sr.getAnatomicSite());
		setLaterality(sr.getLaterality());
		setPathologyStatus(sr.getPathologyStatus());
		setCollector(sr.getCollector());
		setCollectionContainer(sr.getCollectionContainer());
		setCollectionProcedure(sr.getCollectionProcedure());
		setReceiver(sr.getReceiver());
		
		for (SpecimenRequirement childSr : sr.getChildSpecimenRequirements()) {
			childSr.updateRequirementAttrs(sr);
		}		
	}
	
	private static final String[] EXCLUDE_COPY_PROPS = {
		"id",
		"sortOrder",
		"parentSpecimenRequirement",
		"childSpecimenRequirements",
		"specimens"		
	};
}
