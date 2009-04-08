/**
 * <p>Title: ConflictViewForm Class>
 * <p>Description:	This class contains attributes to display on ConflictView.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 * @author kalpana_thakur
 * Created on Feb 27,2007
 */
package edu.wustl.catissuecore.actionForm;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;



public class ConflictViewForm extends AbstractActionForm
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * To retrieve the filter value
	 * 0 to retrieve all
	 * 1 to retrieve partcipant conflicts
	 * 2 to retieve all scg conflicts
	 * default is "0"
	 */
	
	String selectedFilter = "0";

	
	
	
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
     * getting selectedFilter
     * @return selectedFilter
     */
	public String getSelectedFilter()
	{
		return selectedFilter;
	}

	/**
	 * @param selectedFilter Setting selectedFilter
	 */
	public void setSelectedFilter(String selectedFilter)
	{
		this.selectedFilter = selectedFilter;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub
		
	}
	
}
