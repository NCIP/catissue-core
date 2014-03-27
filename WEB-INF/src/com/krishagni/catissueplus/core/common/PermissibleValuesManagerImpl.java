package com.krishagni.catissueplus.core.common;

import java.util.List;


public class PermissibleValuesManagerImpl implements PermissibleValuesManager{

	@Override
	public List<String> getPermissibleValueList(String itemName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean validate(String itemName, String item){
		//TODO Auto-generated method stub
		return true;
	}
	
	public boolean validate(String itemName, String[] item){
		//TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean validate(String type, String parentValue, String value) {
		// TODO Auto-generated method stub
		return true;
	}
}
