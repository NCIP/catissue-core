package com.krishagni.catissueplus.core.common;

import java.util.List;


public interface PermissibleValuesManager {
	public List<String> getPermissibleValueList(String itemName);
	public boolean validate(String itemName, String item);
	public boolean validate(String itemName, String[] item);

}
