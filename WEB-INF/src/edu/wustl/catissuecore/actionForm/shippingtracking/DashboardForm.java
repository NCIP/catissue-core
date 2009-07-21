/**
 * <p>Title: DashboardForm Class</p>
 * <p>Description:DashboardForm class is the subclass of he AbstractActionForm bean classes. </p>
 * Copyright: Copyright (c) 2008
 * Company:
 * @author vijay_chittem
 * @version 1.00
 * Created on July 16, 2008
 */

package edu.wustl.catissuecore.actionForm.shippingtracking;

import org.apache.struts.action.ActionForm;

/**
 * ShippingTracking dashboard form. Dashboard contains received requests,
 * received shipments and outgoing shipments.
 */
public class DashboardForm extends ActionForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7193695200162617786L;

	/**
	 * integer containing records per page on the dashboard.
	 */
	protected int recordsPerPage = 0;

	protected int incomingShipmentsTotalRecords = 0;

	protected int outgoingShipmentsTotalRecords = 0;

	protected int incomingShipmentReqsTotalRecords = 0;

	protected int outgoingShipmentReqsTotalRecords = 0;
	
	protected int incomingShipmentsTotalPages = 1;

	protected int outgoingShipmentsTotalPages = 1;

	protected int incomingShipmentReqsTotalPages = 1;

	protected int outgoingShipmentReqsTotalPages = 1;

	
	public int getIncomingShipmentsTotalPages()
	{
		return incomingShipmentsTotalPages;
	}

	
	public void setIncomingShipmentsTotalPages(int incomingShipmentsTotalPages)
	{
		this.incomingShipmentsTotalPages = incomingShipmentsTotalPages;
	}

	
	public int getOutgoingShipmentsTotalPages()
	{
		return outgoingShipmentsTotalPages;
	}

	
	public void setOutgoingShipmentsTotalPages(int outgoingShipmentsTotalPages)
	{
		this.outgoingShipmentsTotalPages = outgoingShipmentsTotalPages;
	}

	
	public int getIncomingShipmentReqsTotalPages()
	{
		return incomingShipmentReqsTotalPages;
	}

	
	public void setIncomingShipmentReqsTotalPages(int incomingShipmentReqsTotalPages)
	{
		this.incomingShipmentReqsTotalPages = incomingShipmentReqsTotalPages;
	}

	
	public int getOutgoingShipmentReqsTotalPages()
	{
		return outgoingShipmentReqsTotalPages;
	}

	
	public void setOutgoingShipmentReqsTotalPages(int outgoingShipmentReqsTotalPages)
	{
		this.outgoingShipmentReqsTotalPages = outgoingShipmentReqsTotalPages;
	}

	public int getIncomingShipmentsTotalRecords()
	{
		return this.incomingShipmentsTotalRecords;
	}

	public void setIncomingShipmentsTotalRecords(int incomingShipmentsTotalRecords)
	{
		this.incomingShipmentsTotalRecords = incomingShipmentsTotalRecords;
	}

	public int getOutgoingShipmentsTotalRecords()
	{
		return this.outgoingShipmentsTotalRecords;
	}

	public void setOutgoingShipmentsTotalRecords(int outgoingShipmentsTotalRecords)
	{
		this.outgoingShipmentsTotalRecords = outgoingShipmentsTotalRecords;
	}

	public int getIncomingShipmentReqsTotalRecords()
	{
		return this.incomingShipmentReqsTotalRecords;
	}

	public void setIncomingShipmentReqsTotalRecords(int incomingShipmentReqsTotalRecords)
	{
		this.incomingShipmentReqsTotalRecords = incomingShipmentReqsTotalRecords;
	}

	public int getOutgoingShipmentReqsTotalRecords()
	{
		return this.outgoingShipmentReqsTotalRecords;
	}

	public void setOutgoingShipmentReqsTotalRecords(int outgoingShipmentReqsTotalRecords)
	{
		this.outgoingShipmentReqsTotalRecords = outgoingShipmentReqsTotalRecords;
	}

	public int getTotalRecords()
	{
		return this.incomingShipmentsTotalRecords;
	}

	public void setTotalRecords(int totalRecords)
	{
		this.incomingShipmentsTotalRecords = totalRecords;
	}

	/**
	 * sets the records per page of the dashboard.
	 * @return recordsPerPage.
	 */
	public int getRecordsPerPage()
	{
		return this.recordsPerPage;
	}

	/**
	 * sets the records per page of the dashboard.
	 * @param recordsPerPage containing count of pages.
	 */
	public void setRecordsPerPage(int recordsPerPage)
	{
		this.recordsPerPage = recordsPerPage;
	}
}
