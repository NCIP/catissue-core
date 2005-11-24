package edu.wustl.catissuecore.vo;

/**
 * class used for setting each row of page
 * @author namita_srivastava
 *
 */
public class SearchFieldData
{
	//Used for checking next field(text or comboBox) in the same row
	private String dataType;
	
	//Dispalying text for the row used in 1st column
	private String labelKey;
	
	//Instance of class used for setting field's attribute of 2nd column of the same row (operation field)
	private HTMLField oprationField;
	
	//Instance of class used for setting field's attribute of 3rd column of the same row(value field) 
	private HTMLField valueField;
	
	//Used for calling appropriate function according to dataType
	private String functionName;
	
	//Used for specimen page only ****CHANGE REQUIRED****
	private String unitFieldKey = "";
	
	
	/**
	 * @param dataType Datatype of field of page
	 * @param labelKey Dispalying text for the row in page
	 * @param oprationField setting field's attribute of table
	 * @param valueField setting field's attribute of table
	 * @param functionName function according to dataType
	 * @param unitFieldKey for displaying text at the end of field (specimen page only)
	 */
	public SearchFieldData(String dataType, String labelKey,HTMLField oprationField,HTMLField valueField,String functionName,String unitFieldKey)
	{
		this.dataType = dataType;
		this.labelKey = labelKey;
		this.oprationField = oprationField;
		this.valueField = valueField;
		this.functionName = functionName;
		this.unitFieldKey = unitFieldKey;
		
	}


	/**
	 * Datatype of field of page
	 * @return String
	 */
	public String getDataType() {
		return dataType;
	}


	/**
	 * @param dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	/**
	 * function according to dataType
	 * @return String
	 */
	public String getFunctionName() {
		return functionName;
	}


	/**
	 * @param functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}


	/**
	 * Dispalying text for the row in page
	 * @return String
	 */
	public String getLabelKey() {
		return labelKey;
	}


	/**
	 * @param labelKey to set
	 */
	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}


	/**
	 * setting field's attribute of table
	 * @return String
	 */
	public HTMLField getOprationField() {
		return oprationField;
	}


	/**
	 * @param oprationField to set
	 */
	public void setOprationField(HTMLField oprationField) {
		this.oprationField = oprationField;
	}


	/**
	 * for displaying text at the end of field (for specimen page only)
	 * @return String
	 */
	public String getUnitFieldKey() {
		return unitFieldKey;
	}


	/**
	 * @param unitFieldKey to set
	 */
	public void setUnitFieldKey(String unitFieldKey) {
		this.unitFieldKey = unitFieldKey;
	}


	/**
	 * setting field's attribute of table
	 * @return String
	 */
	public HTMLField getValueField() {
		return valueField;
	}


	/**
	 * @param valueField to set
	 */
	public void setValueField(HTMLField valueField) {
		this.valueField = valueField;
	}
}

