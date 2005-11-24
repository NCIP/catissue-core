
package edu.wustl.catissuecore.vo;
/**
 * Class used to set the value of each field's value 
 * @author namita_srivastava
 *
 */
public class HTMLField
{
	//Name referred as property in field's attribute
	private String name = "";
	
	//Id of each field including label
	private String id = "";
	
	//Used for enabling & disabling field
	private boolean isDisabled = false;
	
	//Used for populating value in case of comboBox
	private String dataListName = "";
	
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

	/**
	 * For populating value in case of comboBox
	 * @return String
	 */
	public String getDataListName() {
		return dataListName;
	}

	/**
	 * @param dataListName to set
	 */
	public void setDataListName(String dataListName) {
		this.dataListName = dataListName;
	}

	/**
	 * Id associated with field
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Used for enabling & disabling field
	 * @return boolean
	 */
	public boolean isDisabled() {
		return isDisabled;
	}

	/**
	 * @param isDisabled to set
	 */
	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	/**
	 * Name of field of page
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
