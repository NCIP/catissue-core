
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PvDetail {
	private Long id;

	private String value;
	
	private Long parentId;
	
	private String parentValue;
	
	private String conceptCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentValue() {
		return parentValue;
	}

	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}

	public static PvDetail from(PermissibleValue pv) {
		return from(pv, false);
	}
	
	public String getConceptCode() {
		return conceptCode;
	}

	public void setConceptCode(String conceptCode) {
		this.conceptCode = conceptCode;
	}

	public static PvDetail from(PermissibleValue pv, boolean incParent) {
		PvDetail result = new PvDetail();
		result.setId(pv.getId());
		result.setValue(pv.getValue());
		result.setConceptCode(pv.getConceptCode());
		
		if (incParent && pv.getParent() != null) {
			result.setParentId(pv.getParent().getId());
			result.setParentValue(pv.getParent().getValue());
		}
		
		return result;		
	}
	
	public static List<PvDetail> from(Collection<PermissibleValue> pvs) {
		return from(pvs, false);
	}
	
	public static List<PvDetail> from(Collection<PermissibleValue> pvs, boolean incParent) {
		List<PvDetail> result = new ArrayList<PvDetail>();
		for (PermissibleValue pv : pvs) {
			result.add(from(pv, incParent));
		}
		
		return result;		
	}
	
}
