package com.krishagni.core.tests.testdata;

import java.util.List;
import java.util.ArrayList;

import com.krishagni.catissueplus.core.de.events.ListSavedQueriesCriteria;


public class SavedQueryTestData {
	public static ListSavedQueriesCriteria getCriteria() {
		ListSavedQueriesCriteria crit = new ListSavedQueriesCriteria()
			.startAt(0)
			.countReq(true);
			
		return crit;
	}
}