package edu.wustl.catissuecore.vo;

/**
 * class used for setting each row of page
 * @author namita_srivastava
 *
 */
public class SearchFieldData
{
	//Used for checking next field(text or comboBox) in the same row
	public String dataType;
	
	//Dispalying text for the row used in 1st column
	public String labelKey;
	
	//Instance of class used for setting field's attribute of 2nd column of the same row (operation field)
	public HTMLField oprationField;
	
	//Instance of class used for setting field's attribute of 3rd column of the same row(value field) 
	public HTMLField valueField;
	
	//Used for calling appropriate function according to dataType
	public String functionName;
	
	//Used for specimen page only ****CHANGE REQUIRED****
	public String unitFieldKey = "";
	
	
	/**
	 * @param dataType Datatype of field of page
	 * @param labelKey Dispalying text for the row in page
	 * @param oprationField setting field's attribute of 2nd column of table
	 * @param valueField setting field's attribute of 3rd column of table
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
}

