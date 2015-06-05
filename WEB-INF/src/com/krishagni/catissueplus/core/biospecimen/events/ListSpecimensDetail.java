package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;


public class ListSpecimensDetail {
	private List<SpecimenDetail> specimens = new ArrayList<SpecimenDetail>();
	
	private Long actualCount;

	public List<SpecimenDetail> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(List<SpecimenDetail> specimens) {
		this.specimens = specimens;
	}

	public Long getActualCount() {
		return actualCount;
	}

	public void setActualCount(Long actualCount) {
		this.actualCount = actualCount;
	}
	
	public static ListSpecimensDetail from(List<Specimen> specimens, Long actualCount) {
		ListSpecimensDetail detail = new ListSpecimensDetail();
		detail.setSpecimens(SpecimenDetail.from(new HashSet<Specimen>(specimens)));
		detail.setActualCount(actualCount); 
		
		return detail;
	}
}
