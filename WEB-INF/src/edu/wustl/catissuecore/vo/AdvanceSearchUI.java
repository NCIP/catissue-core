/**
 * Generic class uses all the HTMLfield value required in jsp page
 * @author namita_srivastava
 *
 */
package edu.wustl.catissuecore.vo;
public class AdvanceSearchUI 
{
	//Source of the icon for a page
	private String iconSrc;
	
	//Alternate text for images
	private String iconAltText;
	
	//Title key of page displayed as table's title
	private String titleKey;
	
	//class used for setting value of field's data of each row 
	private SearchFieldData[] searchFieldData;
	
	//Form Name used in javascript function only in case of date field
	private String formName = "";
	
	
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


	/**
	 * Form Name used in javascript function only in case of date field
	 * @return String
	 */
	public String getFormName() {
		return formName;
	}


	/**
	 * @param formName to set
	 */
	public void setFormName(String formName) {
		this.formName = formName;
	}


	/**
	 * Alternate text for images
	 * @return String
	 */
	public String getIconAltText() {
		return iconAltText;
	}


	/**
	 * @param iconAltText to set
	 */
	public void setIconAltText(String iconAltText) {
		this.iconAltText = iconAltText;
	}


	/**
	 * Source of the icon   
	 * @return String
	 */
	public String getIconSrc() {
		return iconSrc;
	}


	/**
	 * @param iconSrc to set
	 */
	public void setIconSrc(String iconSrc) {
		this.iconSrc = iconSrc;
	}


	/**
	 * setting value of field's data of each row 
	 * @return SearchFieldData[]
	 */
	public SearchFieldData[] getSearchFieldData() {
		return searchFieldData;
	}


	/**
	 * @param searchFieldData to set
	 */
	public void setSearchFieldData(SearchFieldData[] searchFieldData) {
		this.searchFieldData = searchFieldData;
	}


	/**
	 * Title key of page displayed as table's title
	 * @return String
	 */
	public String getTitleKey() {
		return titleKey;
	}


	/**
	 * @param titleKey to set
	 */
	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}
}