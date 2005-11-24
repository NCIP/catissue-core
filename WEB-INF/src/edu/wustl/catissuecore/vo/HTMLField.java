
package edu.wustl.catissuecore.vo;
/**
 * Class used to set the value of each field's value 
 * @author namita_srivastava
 *
 */
public class HTMLField
{
	//Name referred as property in field's attribute
	public String name = "";
	
	//Id of each field including label
	public String id = "";
	
	//Used for enabling & disabling field
	public boolean isDisabled = false;
	
	//Used for populating value in case of comboBox
	public String dataListName = "";
	
	public HTMLField(String name,String id,String dataListName)
	{
		this(name, id, dataListName, false);
	}
	
	/**
	 * @param name Name of field of page
	 * @param id Id associated with field
	 * @param dataListName For populating value in case of comboBox
	 * @param isDisabled Used for enabling & disabling field
	 */
	public HTMLField(String name, String id, String dataListName, boolean isDisabled)
	{
		this.name = name;
		this.id = id;
		this.dataListName = dataListName;
		this.isDisabled = isDisabled;
	}
}
