package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenUnit;

public class SpecimenUnitDetail {
	private Long id;
	
	private String specimenClass;
	
	private String type;
	
	private String qtyUnit;
	
	private String qtyHtmlDisplayCode;

	private String concUnit;

	private String concHtmlDisplayCode;
	
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

	public String getQtyUnit() {
		return qtyUnit;
	}

	public void setQtyUnit(String qtyUnit) {
		this.qtyUnit = qtyUnit;
	}

	public String getQtyHtmlDisplayCode() {
		return qtyHtmlDisplayCode;
	}

	public void setQtyHtmlDisplayCode(String qtyHtmlDisplayCode) {
		this.qtyHtmlDisplayCode = qtyHtmlDisplayCode;
	}

	public String getConcUnit() {
		return concUnit;
	}

	public void setConcUnit(String concUnit) {
		this.concUnit = concUnit;
	}

	public String getConcHtmlDisplayCode() {
		return concHtmlDisplayCode;
	}

	public void setConcHtmlDisplayCode(String concHtmlDisplayCode) {
		this.concHtmlDisplayCode = concHtmlDisplayCode;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static SpecimenUnitDetail from(SpecimenUnit specimenUnit) {
		SpecimenUnitDetail detail = new SpecimenUnitDetail();
		BeanUtils.copyProperties(specimenUnit, detail);
		return detail;
	}
	
	public static List<SpecimenUnitDetail> from(Collection<SpecimenUnit> specimenUnits) {
		List<SpecimenUnitDetail> result = new ArrayList<SpecimenUnitDetail>();
		for (SpecimenUnit specimenUnit : specimenUnits) {
			result.add(SpecimenUnitDetail.from(specimenUnit));
		}
		
		return result;
	}
}