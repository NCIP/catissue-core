
package edu.wustl.catissuecore.actionForm;

import org.apache.struts.action.ActionForm;

import edu.wustl.catissuecore.bean.SummaryAdminDetails;
import edu.wustl.catissuecore.bean.SummaryPartDetails;
import edu.wustl.catissuecore.bean.SummarySpDetails;

/**
 * This form holds the data of the summary page of caTissue 
 * @author sagar_baldwa
 */
public class SummaryForm extends ActionForm
{

	private static final long serialVersionUID = 12345;

	private String totalSpCount = "0";

	//Mandar : for new summary page ---------------
	private SummarySpDetails specDetails;
	private SummaryAdminDetails adminDetails;
	private SummaryPartDetails partDetails;

	/**
	 * Default Constructor
	 *
	 */
	//	ConstructorDeclaration
	public SummaryForm()
	{
	}

	/**
	 * Returns the Total Specimen Count of caTissue
	 * @return String
	 */
	public String getTotalSpCount()
	{
		return this.totalSpCount;
	}

	/**
	 * Set the Total Specimen Count of caTissue
	 * @param String
	 * @return void
	 */
	public void setTotalSpCount(final String totSpCnt)
	{
		this.totalSpCount = totSpCnt;
	}

	public SummaryAdminDetails getAdminDetails()
	{
		return this.adminDetails;
	}

	public void setAdminDetails(final SummaryAdminDetails adminDetails)
	{
		this.adminDetails = adminDetails;
	}

	public SummaryPartDetails getPartDetails()
	{
		return this.partDetails;
	}

	public void setPartDetails(final SummaryPartDetails partDetails)
	{
		this.partDetails = partDetails;
	}

	public SummarySpDetails getSpecDetails()
	{
		return this.specDetails;
	}

	public void setSpecDetails(final SummarySpDetails specDetails)
	{
		this.specDetails = specDetails;
	}

}
