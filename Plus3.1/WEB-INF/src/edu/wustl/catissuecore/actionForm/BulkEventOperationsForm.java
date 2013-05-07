
package edu.wustl.catissuecore.actionForm;

import java.util.LinkedHashMap;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author renuka_bajpai
 *
 */
public class BulkEventOperationsForm extends EventParametersForm
{
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * eventSpecificData Map.
	 */
	private final LinkedHashMap<String, String> eventSpecificData =
		new LinkedHashMap<String, String>();
	/**
	 * Specimen Id's map.
	 */
	private final LinkedHashMap<String, String> specimenIds =
		new LinkedHashMap<String, String>();
	/**
	 * Operation Add/Edit.
	 */
	private String operation;
	/**
	 * Specify order of selected specimens.
	 */
	private String orderedString = "";

	/**
	 * This method get Ordered string.
	 * @return ordered String
	 */
	public String getOrderedString()
	{
		return this.orderedString;
	}

	/**
	 * This method set Ordered String.
	 * @param orderedString ordered String
	 */
	public void setOrderedString(String orderedString)
	{
		this.orderedString = orderedString;
	}
	/**
	 * @param key Map Key
	 * @return map value
	 */
	public String getFieldValue(final String key)
	{
		return this.eventSpecificData.get(key);
	}
	/**
	 * @param key Map Key
	 * @param value value
	 */
	public void setFieldValue(final String key, final String value)
	{
		this.eventSpecificData.put(key, value);
	}
	/**
	 * @return eventSpecificData
	 */
	public LinkedHashMap<String, String> getEventSpecificData()
	{
		return this.eventSpecificData;
	}

	/**
	 * @return BULK_OPERATIONS_FORM_ID
	 */
	public int getFormId()
	{
		return Constants.BULK_OPERATIONS_FORM_ID;
	}
	/**
	 * @return specimenIds
	 */
	public LinkedHashMap<String, String> getSpecimenIds()
	{
		return this.specimenIds;
	}
	/**
	 * @param key Map Key
	 * @return map value
	 */
	public String getSpecimenId(final String key)
	{
		return this.specimenIds.get(key);
	}
	/**
	 * @param key Map Key
	 * @param value value
	 */
	public void setSpecimenId(final String key, final String value)
	{
		this.specimenIds.put(key, value);
	}

	/**
	 * @return operation
	 */
	public String getOperation()
	{
		return this.operation;
	}

	/**
	 * @param operation operation add/edit
	 */
	public void setOperation(final String operation)
	{
		this.operation = operation;
	}

	/**
	 * @param arg0 Override argument
	 * @param arg1 Override argument
	 */
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{


	}
}
