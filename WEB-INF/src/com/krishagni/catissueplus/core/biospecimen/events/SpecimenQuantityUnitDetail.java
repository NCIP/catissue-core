package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenQuantityUnit;

public class SpecimenQuantityUnitDetail {
	private Long id;
	
	private String specimenClass;
	
	private String type;
	
	private String unit;
	
	private String htmlDisplayCode;
	
	private String activityStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSpecimenClass() {
		return specimenClass;
	}

	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getHtmlDisplayCode() {
		return htmlDisplayCode;
	}

	public void setHtmlDisplayCode(String htmlDisplayCode) {
		this.htmlDisplayCode = htmlDisplayCode;
	}
	
	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static SpecimenQuantityUnitDetail from(SpecimenQuantityUnit qtyUnit) {
		SpecimenQuantityUnitDetail detail = new SpecimenQuantityUnitDetail();
		BeanUtils.copyProperties(qtyUnit, detail);
		return detail;
	}
	
	public static List<SpecimenQuantityUnitDetail> from(Collection<SpecimenQuantityUnit> qtyUnits) {
		List<SpecimenQuantityUnitDetail> result = new ArrayList<SpecimenQuantityUnitDetail>();
		for (SpecimenQuantityUnit qtyUnit : qtyUnits) {
			result.add(SpecimenQuantityUnitDetail.from(qtyUnit));
		}
		
		return result;
	}
}