package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;

import edu.emory.mathcs.backport.java.util.Collections;

@ListenAttributeChanges
public class SpecimenDetail extends SpecimenInfo {
	private CollectionEventDetail collectionEvent;
	
	private ReceivedEventDetail receivedEvent;
	
	private String labelFmt;
	
	private Set<String> biohazards;
	
	private String comments;
	
	private Boolean closeAfterChildrenCreation;  
	
	private List<SpecimenDetail> children;
	
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

	public List<SpecimenDetail> getChildren() {
		return children;
	}

	public void setChildren(List<SpecimenDetail> children) {
		this.children = children;
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
	
	public static SpecimenDetail from(Specimen specimen) {
		SpecimenDetail result = new SpecimenDetail();
		SpecimenInfo.fromTo(specimen, result);
		
		SpecimenRequirement sr = specimen.getSpecimenRequirement();
		Collection<Specimen> children = specimen.getChildCollection();
		if (sr == null) {
			result.setChildren(from(children));
		} else {
			Collection<SpecimenRequirement> anticipated = sr.getChildSpecimenRequirements();
			result.setChildren(getSpecimens(anticipated, children));
		}
		
		result.setLabelFmt(specimen.getLabelTmpl());
		result.setBiohazards(new HashSet<String>(specimen.getBiohazards()));
		result.setComments(specimen.getComment());
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
		result.setChildren(fromAnticipated(anticipated.getChildSpecimenRequirements()));
		result.setLabelFmt(anticipated.getLabelTmpl());
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
