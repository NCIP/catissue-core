
package edu.wustl.catissuecore.actionForm;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author renuka_bajpai
 *
 */
public class BulkEventOperationsForm extends EventParametersForm
{

	private static final long serialVersionUID = 1L;

	private final Map<String, String> eventSpecificData = new HashMap<String, String>();
	private final Map<String, String> specimenIds = new HashMap<String, String>();
	private String operation;

	public String getFieldValue(final String key)
	{
		return this.eventSpecificData.get(key);
	}

	public void setFieldValue(final String key, final String value)
	{
		this.eventSpecificData.put(key, value);
	}

	public Map getEventSpecificData()
	{
		return this.eventSpecificData;
	}

	@Override
	public int getFormId()
	{
		return Constants.BULK_OPERATIONS_FORM_ID;
	}

	public Map getSpecimenIds()
	{
		return this.specimenIds;
	}

	public String getSpecimenId(final String key)
	{
		return this.specimenIds.get(key);
	}

	public void setSpecimenId(final String key, final String value)
	{
		this.specimenIds.put(key, value);
	}

	@Override
	public String getOperation()
	{
		return this.operation;
	}

	@Override
	public void setOperation(final String operation)
	{
		this.operation = operation;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

	//	/**
	//	 * 
	//	 */
	//	public BulkEventOperationsForm()
	//	{
	//		super();
	//		// TODO Auto-generated constructor stub
	//	}
	//	

}
