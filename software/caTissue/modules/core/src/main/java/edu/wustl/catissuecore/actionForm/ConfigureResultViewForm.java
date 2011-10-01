/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Poornima Govindrao
 *@version 1.0
 */

package edu.wustl.catissuecore.actionForm;

import org.apache.struts.action.ActionForm;

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Poornima Govindrao
 *@version 1.0
 */

public class ConfigureResultViewForm extends ActionForm implements IPrinterTypeLocation
{

	private static final long serialVersionUID = 1L;

	private String tableName;
	private String[] selectedColumnNames;
	private String[] columnNames;
	private String nextAction;
	private Long distributionId;
	private boolean reportAction = true;
	private String printerType;
	private String printerLocation;
	private String specimenIdString;
	
	
	/**
	 * @return distributionId Returns the distributionId.
	 */
	public Long getDistributionId()
	{
		return this.distributionId;
	}

	/**
	 * @param distributionId The distributionId to set.
	 */
	public void setDistributionId(final Long distributionId)
	{
		this.distributionId = distributionId;
	}

	/**
	 * @return Returns the columnNames.
	 */
	public String[] getColumnNames()
	{
		return this.columnNames;
	}

	/**
	 * @param columnNames The columnNames to set.
	 */
	public void setColumnNames(String[] columnNames)
	{
		this.columnNames = columnNames;
	}

	/**
	 * @return Returns the selectedColumnNames.
	 */
	public String[] getSelectedColumnNames()
	{
		return this.selectedColumnNames;
	}

	/**
	 * @param selectedColumnNames The selectedColumnNames to set.
	 */
	public void setSelectedColumnNames(String[] selectedColumnNames)
	{
		this.selectedColumnNames = selectedColumnNames;
	}

	/**
	 * @return Returns the tableName.
	 */
	public String getTableName()
	{
		return this.tableName;
	}

	/**
	 * @param tableName The tableName to set.
	 */
	public void setTableName(final String tableName)
	{
		this.tableName = tableName;
	}

	/**
	 * @return Returns the nextAction.
	 */
	public String getNextAction()
	{
		return this.nextAction;
	}

	/**
	 * @param nextAction The nextAction to set.
	 */
	public void setNextAction(final String nextAction)
	{
		this.nextAction = nextAction;
	}

	/**
	 * @return Returns the reportAction.
	 */
	public boolean isReportAction()
	{
		return this.reportAction;
	}

	/**
	 * @param reportAction The reportAction to set.
	 */
	public void setReportAction(final boolean reportAction)
	{
		this.reportAction = reportAction;
	}
	public String getPrinterLocation()
	{
		return printerLocation;
	}

	public void setPrinterLocation(String printerLocation)
	{
		this.printerLocation = printerLocation;
	}

	public String getPrinterType()
	{
		return printerType;
	}

	public void setPrinterType(String printerType)
	{
		this.printerType = printerType;
	}
	public String getSpecimenIdString()
	{
		return specimenIdString;
	}

	
	public void setSpecimenIdString(String specimenIdString)
	{
		this.specimenIdString = specimenIdString;
	}

}
