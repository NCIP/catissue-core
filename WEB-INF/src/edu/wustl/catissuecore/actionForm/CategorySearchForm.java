package edu.wustl.catissuecore.actionForm;


import java.util.List;

import org.apache.struts.action.ActionForm;

import edu.wustl.common.beans.NameValueBean;

/**
 * FormBean representing the QueryModule's properties.
 * @author Mandar Shidhore
 * @author deepti_shelar
 * @version 1.0
 * @created 06-Nov-2006 10.40.04 AM
 */

public class CategorySearchForm extends ActionForm
{
	private static final long serialVersionUID = 1L;
	/**
	 * String to store the text field value.
	 */
	private String textField = null;
	/**
	 * String to store the classChecked checkbox's  value.
	 */
	private String classChecked = null;
	/**
	 * String to store the attributeChecked checkbox's value.
	 */
	private String attributeChecked = null;
	/**
	 * String to store the permissibleValuesChecked checkbox's value.
	 */
	private String permissibleValuesChecked = null;
	/**
	 * String to store the the IncludeDescription checkbox's value.
	 */
	private String includeDescriptionChecked = null;
	/**
	 * String to store the radio button selected value.
	 */
	private String selected = null;
	/**
	 * String to store the radio button selected value.
	 */
	private String options = null;
	/**
	 * String to store the entityName value.
	 */
	private String entityName = null;
	/**
	 * String to store the string used To Create Query Object.
	 */
	private String stringToCreateQueryObject = null;
	/**
	 * String to store the errors value.
	 */
	private List errors = null;
	/**
	 * String to store the searchButton value.
	 */
	private String searchButton = null;
	/**
	 * String to store the nextOperation value.
	 */
	private String nextOperation = null;
	/**
	 * String to store the text field value.
	 */
	private String nodeId = null;
	/**
	 * String to store the text field value.
	 */
	private String currentPage = null;
	/**
	 * 
	 */
    private String booleanAttribute = null;
    
    private String []selectedColumnNames;
    private String []columnNames;
    private List<NameValueBean> selectedColumnNameValueBeanList;
    /**
	 * String to store currentSelectedObject
	 */
	private String currentSelectedObject = null;
	 /**
	 * String to store currentSelectedObject
	 */
	private String currentSelectedNodeInTree = null;
	
	/**
	 * @return the currentPage
	 */
	public String getCurrentPage()
	{
		return currentPage;
	}
	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(final String currentPage)
	{
		this.currentPage = currentPage;
	}
	/**
	 * @return the nextOperation
	 */
	public String getNextOperation()
	{
		return nextOperation;
	}
	/**
	 * @param nextOperation the nextOperation to set
	 */
	public void setNextOperation(final String nextOperation)
	{
		this.nextOperation = nextOperation;
	}
	/**
	 * @return the searchButton
	 */
	public String getSearchButton()
	{
		return searchButton;
	}
	/**
	 * @param searchButton the searchButton to set
	 */
	public void setSearchButton(final String searchButton)
	{
		this.searchButton = searchButton;
	}
	/**
	 * @return the errors
	 */
	public List getErrors()
	{
		return errors;
	}
	/**
	 * @param errors the errors to set
	 */
	public void setErrors(final List errors)
	{
		this.errors = errors;
	}
	/**
	 * @return the stringToCreateQueryObject
	 */
	public String getStringToCreateQueryObject()
	{
		return stringToCreateQueryObject;
	}
	/**
	 * @param stringToCreateQueryObject the stringToCreateQueryObject to set
	 */
	public void setStringToCreateQueryObject(String stringToCreateQueryObject)
	{
		this.stringToCreateQueryObject = stringToCreateQueryObject;
	}
	/**
	 * @return the selected
	 */
	public String getSelected()
	{
		return selected;
	}
	/**
	 * @param selected the selected to set
	 */
	public void setSelected(final String selected)
	{
		this.selected = selected;
	}
	/**
	 * @return the textField
	 */
	public String getTextField()
	{
		return textField;
	}
	/**
	 * @param textField the textField to set
	 */
	public void setTextField(final String textField)
	{
		this.textField = textField;
	}
	/**
	 * @return the attributeChecked
	 */
	public String getAttributeChecked()
	{
		return attributeChecked;
	}
	/**
	 * @param attributeChecked the attributeChecked to set
	 */
	public void setAttributeChecked(final String attributeChecked)
	{
		this.attributeChecked = attributeChecked;
	}
	/**
	 * @return the classChecked
	 */
	public String getClassChecked()
	{
		return classChecked;
	}
	/**
	 * @param classChecked the classChecked to set
	 */
	public void setClassChecked(final String classChecked)
	{
		this.classChecked = classChecked;
	}
	/**
	 * @return the permissibleValuesChecked
	 */
	public String getPermissibleValuesChecked()
	{
		return permissibleValuesChecked;
	}
	/**
	 * @param permissibleValuesChecked the permissibleValuesChecked to set
	 */
	public void setPermissibleValuesChecked(String permissibleValuesChecked)
	{
		this.permissibleValuesChecked = permissibleValuesChecked;
	}
	/**
	 * @return the entityName
	 */
	public String getEntityName()
	{
		return entityName;
	}
	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(final String entityName)
	{
		this.entityName = entityName;
	}
	/**
	 * @return the nodeId
	 */
	public String getNodeId()
	{
		return nodeId;
	}
	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(final String nodeId)
	{
		this.nodeId = nodeId;
	}
	/**
	 * @return the booleanAttribute
	 */
	public String getBooleanAttribute()
	{
		return booleanAttribute;
	}
	/**
	 * @param booleanAttribute the booleanAttribute to set
	 */
	public void setBooleanAttribute(final String booleanAttribute)
	{
		this.booleanAttribute = booleanAttribute;
	}
	public String getIncludeDescriptionChecked() 
	{
		return includeDescriptionChecked;
	}
	public void setIncludeDescriptionChecked(String includeDescriptionChecked) 
	{
		this.includeDescriptionChecked = includeDescriptionChecked;
	}
	/**
	 * @return the columnNames
	 */
	public String[] getColumnNames()
	{
		return columnNames;
	}
	/**
	 * @param columnNames the columnNames to set
	 */
	public void setColumnNames(final String[] columnNames)
	{
		this.columnNames = columnNames;
	}
	/**
	 * @return the selectedColumnNames
	 */
	public String[] getSelectedColumnNames()
	{
		return selectedColumnNames;
	}
	/**
	 * @param selectedColumnNames the selectedColumnNames to set
	 */
	public void setSelectedColumnNames(String[] selectedColumnNames)
	{
		this.selectedColumnNames = selectedColumnNames;
	}
	/**
	 * @return the currentSelectedObject
	 */
	public String getCurrentSelectedObject()
	{
		return currentSelectedObject;
	}
	/**
	 * @param currentSelectedObject the currentSelectedObject to set
	 */
	public void setCurrentSelectedObject(String currentSelectedObject)
	{
		this.currentSelectedObject = currentSelectedObject;
	}
	/**
	 * @return the currentSelectedNodeInTree
	 */
	public String getCurrentSelectedNodeInTree()
	{
		return currentSelectedNodeInTree;
	}
	/**
	 * @param currentSelectedNodeInTree the currentSelectedNodeInTree to set
	 */
	public void setCurrentSelectedNodeInTree(String currentSelectedNodeInTree)
	{
		this.currentSelectedNodeInTree = currentSelectedNodeInTree;
	}
	/**
	 * @return the selectedColumnNameValueBeanList
	 */
	public List<NameValueBean> getSelectedColumnNameValueBeanList()
	{
		return selectedColumnNameValueBeanList;
	}
	/**
	 * @param selectedColumnNameValueBeanList the selectedColumnNameValueBeanList to set
	 */
	public void setSelectedColumnNameValueBeanList(List<NameValueBean> selectedColumnNameValueBeanList)
	{
		this.selectedColumnNameValueBeanList = selectedColumnNameValueBeanList;
	}
	/**
	 * @return the options
	 */
	public String getOptions()
	{
		return options;
	}
	/**
	 * @param options the options to set
	 */
	public void setOptions(final String options)
	{
		this.options = options;
	}
}
