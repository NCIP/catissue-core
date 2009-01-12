package edu.wustl.catissuecore.actionForm;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.catissuecore.util.global.Constants;

public class BulkEventOperationsForm extends EventParametersForm
{
	
	
	private final Map<String, String> eventSpecificData= new HashMap<String, String>(); 
	private final Map<String, String> specimenIds= new HashMap<String, String>(); 
	private String operation;

	public String getFieldValue(String key)
	{
		return eventSpecificData.get(key);
	}
	
	public void setFieldValue(String key, String value)
	{
		eventSpecificData.put(key, value);
	}
	
	public Map getEventSpecificData()
	{
		return eventSpecificData;
	}
	
	@Override
	public int getFormId()
	{
		return Constants.BULK_OPERATIONS_FORM_ID;
	}
	
	public Map getSpecimenIds()
	{
		return specimenIds;
	}
	
	public String getSpecimenId(String key)
	{
		return specimenIds.get(key);
	}
	
	public void setSpecimenId(String key, String value)
	{
		specimenIds.put(key, value);
	}

	public String getOperation()
	{
		return operation;
	}

	public void setOperation(String operation)
	{
		this.operation = operation;
	}
	
	
}
