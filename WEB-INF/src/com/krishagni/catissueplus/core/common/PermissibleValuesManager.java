package com.krishagni.catissueplus.core.common;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.PvDetail;


public interface PermissibleValuesManager {
	public  List<PvDetail>  getPermissibleValueList(String itemName);
	public boolean validate(String itemName, String item);
	public boolean validate(String itemName, String[] item);
	public boolean validate(String type, String parentValue, String value);

}
