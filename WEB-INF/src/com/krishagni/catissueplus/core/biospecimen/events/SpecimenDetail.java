package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.util.CollectionUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;

@ListenAttributeChanges
public class SpecimenDetail extends SpecimenInfo {

	private static final long serialVersionUID = -752005520158376620L;

	private CollectionEventDetail collectionEvent;
	
	private ReceivedEventDetail receivedEvent;
	
	private String labelFmt;
	
	private String labelAutoPrintMode;
	
	private Set<String> biohazards;
	
	private String comments;
	
	private Boolean closeAfterChildrenCreation;  
	
	private List<SpecimenDetail> children;

	private Long pooledSpecimenId;
	
	private String pooledSpecimenLabel;

	private List<SpecimenDetail> specimensPool;

	// This is needed for creation of derivatives from BO for closing parent specimen.
	private Boolean closeParent;
	
	private Boolean poolSpecimen;
	
	private String reqCode;

	private ExtensionDetail extensionDetail;

	//
	// transient variables specifying action to be performed
	//
	private boolean forceDelete;

	private boolean printLabel;

	private Integer incrParentFreezeThaw;

	public CollectionEventDetail getCollectionEvent() {
		return collectionEvent;
	}

	public void setCollectionEvent(CollectionEventDetail collectionEvent) {
		this.collectionEvent = collectionEvent;
	}

	public ReceivedEventDetail getReceivedEvent() {
		return receivedEvent;
	}

	public void setReceivedEvent(ReceivedEventDetail receivedEvent) {
		this.receivedEvent = receivedEvent;
	}

	public String getLabelFmt() {
		return labelFmt;
	}

	public void setLabelFmt(String labelFmt) {
		this.labelFmt = labelFmt;
	}

	public String getLabelAutoPrintMode() {
		return labelAutoPrintMode;
	}

	public void setLabelAutoPrintMode(String labelAutoPrintMode) {
		this.labelAutoPrintMode = labelAutoPrintMode;
	}

	public List<SpecimenDetail> getChildren() {
		return children;
	}

	public void setChildren(List<SpecimenDetail> children) {
		this.children = children;
	}

	public Long getPooledSpecimenId() {
		return pooledSpecimenId;
	}

	public void setPooledSpecimenId(Long pooledSpecimenId) {
		this.pooledSpecimenId = pooledSpecimenId;
	}

	public String getPooledSpecimenLabel() {
		return pooledSpecimenLabel;
	}

	public void setPooledSpecimenLabel(String pooledSpecimenLabel) {
		this.pooledSpecimenLabel = pooledSpecimenLabel;
	}

	public List<SpecimenDetail> getSpecimensPool() {
		return specimensPool;
	}

	public void setSpecimensPool(List<SpecimenDetail> specimensPool) {
		this.specimensPool = specimensPool;
	}

	public Set<String> getBiohazards() {
		return biohazards;
	}

	public void setBiohazards(Set<String> biohazards) {
		this.biohazards = biohazards;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Boolean getCloseAfterChildrenCreation() {
		return closeAfterChildrenCreation;
	}

	public void setCloseAfterChildrenCreation(Boolean closeAfterChildrenCreation) {
		this.closeAfterChildrenCreation = closeAfterChildrenCreation;
	}

	public Boolean getCloseParent() {
		return closeParent;
	}

	public void setCloseParent(Boolean closeParent) {
		this.closeParent = closeParent;
	}

	public Boolean getPoolSpecimen() {
		return poolSpecimen;
	}

	public void setPoolSpecimen(Boolean poolSpecimen) {
		this.poolSpecimen = poolSpecimen;
	}
	
	public String getReqCode() {
		return reqCode;
	}

	public void setReqCode(String reqCode) {
		this.reqCode = reqCode;
	}

	public boolean closeParent() {
		return closeParent == null ? false : closeParent;
	}

	public ExtensionDetail getExtensionDetail() {
		return extensionDetail;
	}

	public void setExtensionDetail(ExtensionDetail extensionDetail) {
		this.extensionDetail = extensionDetail;
	}

	@JsonIgnore
	public boolean isForceDelete() {
		return forceDelete;
	}

	public void setForceDelete(boolean forceDelete) {
		this.forceDelete = forceDelete;
	}
	
	//
	// Do not serialise printLabel from interaction object to response JSON. Therefore @JsonIgnore
	// However, deserialise, if present, from input request JSON to interaction object. Hence @JsonProperty
	//
	@JsonIgnore
	public boolean isPrintLabel() {
		return printLabel;
	}

	@JsonProperty
	public void setPrintLabel(boolean printLabel) {
		this.printLabel = printLabel;
	}

	@JsonIgnore
	public Integer getIncrParentFreezeThaw() {
		return incrParentFreezeThaw;
	}

	@JsonProperty
	public void setIncrParentFreezeThaw(Integer incrParentFreezeThaw) {
		this.incrParentFreezeThaw = incrParentFreezeThaw;
	}

	public static SpecimenDetail from(Specimen specimen) {
		return from(specimen, true, true);
	}

	public static SpecimenDetail from(Specimen specimen, boolean partial, boolean excludePhi) {
		SpecimenDetail result = new SpecimenDetail();
		SpecimenInfo.fromTo(specimen, result);
		
		SpecimenRequirement sr = specimen.getSpecimenRequirement();
		if (sr == null) {
			List<SpecimenDetail> children = from(specimen.getChildCollection());
			sort(children);
			result.setChildren(children);
		} else {
			if (sr.isPooledSpecimenReq()) {
				result.setSpecimensPool(getSpecimens(sr.getSpecimenPoolReqs(), specimen.getSpecimensPool()));
			}
			result.setPoolSpecimen(sr.isSpecimenPoolReq());

			result.setChildren(getSpecimens(sr.getChildSpecimenRequirements(), specimen.getChildCollection()));
		}
		
		if (specimen.getPooledSpecimen() != null) {
			result.setPooledSpecimenId(specimen.getPooledSpecimen().getId());
			result.setPooledSpecimenLabel(specimen.getPooledSpecimen().getLabel());
		}
		
		result.setLabelFmt(specimen.getLabelTmpl());
		if (sr != null && sr.getLabelAutoPrintModeToUse() != null) {
			result.setLabelAutoPrintMode(sr.getLabelAutoPrintModeToUse().name());
		}

		result.setReqCode(sr != null ? sr.getCode() : null);
		result.setBiohazards(new HashSet<>(specimen.getBiohazards()));
		result.setComments(specimen.getComment());

		if (!partial) {
			result.setExtensionDetail(ExtensionDetail.from(specimen.getExtension(), excludePhi));
		}
		
		return result;
	}
	
	public static List<SpecimenDetail> from(Collection<Specimen> specimens) {
		List<SpecimenDetail> result = new ArrayList<SpecimenDetail>();
		
		if (CollectionUtils.isEmpty(specimens)) {
			return result;
		}
		
		for (Specimen specimen : specimens) {
			result.add(SpecimenDetail.from(specimen));
		}
		
		return result;
	}
	
	public static SpecimenDetail from(SpecimenRequirement anticipated) {
		SpecimenDetail result = new SpecimenDetail();		
		SpecimenInfo.fromTo(anticipated, result);
		
		if (anticipated.isPooledSpecimenReq()) {
			result.setSpecimensPool(fromAnticipated(anticipated.getSpecimenPoolReqs()));
		}
		
		result.setPoolSpecimen(anticipated.isSpecimenPoolReq());
		result.setChildren(fromAnticipated(anticipated.getChildSpecimenRequirements()));
		result.setLabelFmt(anticipated.getLabelTmpl());
		if (anticipated.getLabelAutoPrintModeToUse() != null) {
			result.setLabelAutoPrintMode(anticipated.getLabelAutoPrintModeToUse().name());
		}
		result.setReqCode(anticipated.getCode());
		return result;		
	}

	public static List<SpecimenDetail> fromAnticipated(Collection<SpecimenRequirement> anticipatedSpecimens) {
		List<SpecimenDetail> result = new ArrayList<SpecimenDetail>();
		
		if (CollectionUtils.isEmpty(anticipatedSpecimens)) {
			return result;
		}
		
		for (SpecimenRequirement anticipated : anticipatedSpecimens) {
			result.add(SpecimenDetail.from(anticipated));
		}
		
		return result;
	}	
	
	public static void sort(List<SpecimenDetail> specimens) {
		Collections.sort(specimens);
		
		for (SpecimenDetail specimen : specimens) {
			if (specimen.getChildren() != null) {
				sort(specimen.getChildren());
			}
		}
	}
	
	public static List<SpecimenDetail> getSpecimens(
			Collection<SpecimenRequirement> anticipated, 
			Collection<Specimen> specimens) {
		
		List<SpecimenDetail> result = SpecimenDetail.from(specimens);
		merge(anticipated, result, null, getReqSpecimenMap(result));

		SpecimenDetail.sort(result);
		return result;
	}

	private static Map<Long, SpecimenDetail> getReqSpecimenMap(List<SpecimenDetail> specimens) {
		Map<Long, SpecimenDetail> reqSpecimenMap = new HashMap<Long, SpecimenDetail>();
						
		List<SpecimenDetail> remaining = new ArrayList<SpecimenDetail>();
		remaining.addAll(specimens);
		
		while (!remaining.isEmpty()) {
			SpecimenDetail specimen = remaining.remove(0);
			Long srId = (specimen.getReqId() == null) ? -1 : specimen.getReqId();
			reqSpecimenMap.put(srId, specimen);
			
			remaining.addAll(specimen.getChildren());
		}
		
		return reqSpecimenMap;
	}
	
	private static void merge(
			Collection<SpecimenRequirement> anticipatedSpecimens, 
			List<SpecimenDetail> result, 
			SpecimenDetail currentParent,
			Map<Long, SpecimenDetail> reqSpecimenMap) {
		
		for (SpecimenRequirement anticipated : anticipatedSpecimens) {
			SpecimenDetail specimen = reqSpecimenMap.get(anticipated.getId());
			if (specimen != null) {
				merge(anticipated.getChildSpecimenRequirements(), result, specimen, reqSpecimenMap);
			} else {
				specimen = SpecimenDetail.from(anticipated);
				
				if (currentParent == null) {
					result.add(specimen);
				} else {
					specimen.setParentId(currentParent.getId());
					currentParent.getChildren().add(specimen);
				}				
			}						
		}
	}
}
