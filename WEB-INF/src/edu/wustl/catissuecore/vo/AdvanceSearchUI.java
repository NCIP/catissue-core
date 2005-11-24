/**
 * Generic class uses all the HTMLfield value required in jsp page
 * @author namita_srivastava
 *
 */
package edu.wustl.catissuecore.vo;
public class AdvanceSearchUI 
{
	//Source of the icon for a page
	public String iconSrc;
	
	//Alternate text for images
	public String iconAltText;
	
	//Title key of page displayed as table's title
	public String titleKey;
	
	//class used for setting value of field's data of each row 
	public SearchFieldData[] searchFieldData;
	
	//Form Name used in javascript function only in case of date field
	public String formName = "";
	
	
	/**
	 * @param iconSrc Source of the icon   
	 * @param iconAltText Alternate text for images
	 * @param titleKey Title key of page displayed as table's title
	 * @param searchFieldData setting value of field's data of each row 
	 * @param formName Form Name used in javascript function only in case of date field
	 */
	public AdvanceSearchUI(String iconSrc,String iconAltText,String titleKey,SearchFieldData[] searchFieldData,String formName)
	{
		this.iconSrc = iconSrc;
		this.iconAltText = iconAltText;
		this.titleKey = titleKey;
		this.searchFieldData = searchFieldData;
		this.formName = formName;
	}
}