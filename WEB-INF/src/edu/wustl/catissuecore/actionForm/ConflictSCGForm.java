/**
 * <p>Title: ConflictSCGForm Class>
 * <p>Description:	This class contains attributes to display on ConflictSCGView.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 *@version 1.00
 *@author kalpana Thakur
 *Created on sep 18,2007
 */

package edu.wustl.catissuecore.actionForm;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author renuka_bajpai
 *
 */
public class ConflictSCGForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * This string variable is for existing Conflicted Report
	 */
	protected String existingConflictedReport = "";
	/**
	 * This string variable is for new Conflicted Report
	 */
	protected String newConflictedReport = "";

	public int getFormId()
	{
		return 0;
	}

	protected void reset()
	{

	}

	public void setAllValues(AbstractDomainObject abstractDomain)
	{

	}

	/**
	 * @return the existing Conflicted Report
	 */
	public String getExistingConflictedReport()
	{
		return existingConflictedReport;
	}

	/**
	 * This method used to set the existing conflicted report
	 * @param existingConflictedReport
	 */
	public void setExistingConflictedReport(String existingConflictedReport)
	{
		this.existingConflictedReport = existingConflictedReport;
	}

	/**
	 * @return the new conflicted report
	 */
	public String getNewConflictedReport()
	{
		return newConflictedReport;
	}

	/**
	 * This method used to set the new conflicted report
	 * @param newConflictedReport
	 */
	public void setNewConflictedReport(String newConflictedReport)
	{
		this.newConflictedReport = newConflictedReport;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}
