package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

public class PrintVisitNameDetail {
	List<Long> visitIds = new ArrayList<Long>();

	List<String> visitNames = new ArrayList<String>();

	private int copies = 1;

	public List<Long> getVisitIds() {
		return visitIds;
	}

	public void setVisitIds(List<Long> visitIds) {
		this.visitIds = visitIds;
	}

	public List<String> getVisitNames() {
		return visitNames;
	}

	public void setVisitNames(List<String> visitNames) {
		this.visitNames = visitNames;
	}

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}
}
