
package edu.wustl.catissuecore.action.shippingtracking;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.shippingtracking.DashboardForm;
import edu.wustl.catissuecore.bizlogic.shippingtracking.BaseShipmentBizLogic;
import edu.wustl.catissuecore.bizlogic.shippingtracking.ShipmentBizLogic;
import edu.wustl.catissuecore.bizlogic.shippingtracking.ShipmentRequestBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * this class implements dashboard viewing action.
 */
public class ShowDashboardAction extends SecureAction
{
	private static final String CREATED_DATE = "createdDate";
	private static final String RECEIVERSITE_ID = "receiverSite.id";
	private static final String REQUEST_FOR = "requestFor";
	private static final String SEND_DATE = "sendDate";
	private static final String SENDER_SITE_ID = "senderSite.id";
	private static final String INCOMING_SHIP_PAGE_NUM = "incomingShipCurrentPageNo";
	private static final String OUTGOING_SHIP_PAGE_NUM = "outgoingShipCurrentPageNo";
	private static final String REQUEST_SENT_PAGE_NUM = "reqSentCurrentPageNo";
	

	/**
	 * action method for shipment add/edit.
	 * @param mapping object of ActionMapping class.
	 * @param form object of ActionForm class.
	 * @param request object of HttpServletRequest class.
	 * @param response object of HttpServletResponse class.
	 * @return forward mapping.
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		//		Create DAO for passing as an argument to bizlogic's validate
		final Logger logger = Logger.getCommonLogger(ShowDashboardAction.class);
		DAO dao = null;
		try
		{
			dao = AppUtility.openDAOSession(null);
			final ShipmentBizLogic bizLogic = new ShipmentBizLogic();
			final ShipmentRequestBizLogic requestBizLogic = new ShipmentRequestBizLogic();
			// Get logged in user's site id
			final SessionDataBean sessionDataBean = this.getSessionData(request);
			Long loggedInUserId = null;
			Long[] loggedInUserSiteId = null;
			int recordsPerPage = 5;
			DashboardForm dashboardForm = null;
			if (form instanceof DashboardForm)
			{
				dashboardForm = ((DashboardForm) form);
				if (dashboardForm.getRecordsPerPage() != 0)
				{
					recordsPerPage = dashboardForm.getRecordsPerPage();
				}
			}
			if (sessionDataBean != null)
			{
				loggedInUserId = sessionDataBean.getUserId();
				if (loggedInUserId == null)
				{
					logger.debug("user is not authorized for the operation");
					throw new UserNotAuthorizedException(ErrorKey
							.getErrorKey("access.execute.action.denied"), null,
							"user is not authorized for the operation");					
				}
				else
				{
					final List<Long> siteIds = bizLogic.getPermittedSiteIdsForUser(loggedInUserId,
							sessionDataBean.isAdmin());
					if (siteIds != null && !siteIds.isEmpty())
					{
						loggedInUserSiteId = new Long[siteIds.size()];
						loggedInUserSiteId = siteIds.toArray(loggedInUserSiteId);
						//bug 12809 start
						this.getTotalRecordsAndPagesForRequest(dashboardForm, requestBizLogic,
								recordsPerPage, loggedInUserSiteId, request);
						this.getTotalRecordsAndPagesForShipment(dashboardForm, bizLogic,
								recordsPerPage, loggedInUserSiteId, request);
						if (!sessionDataBean.isAdmin())
						{
							//incoming shipment requests
							this.setRequestsReceivedInfo(request, requestBizLogic,
									loggedInUserSiteId, recordsPerPage, dashboardForm,
									sessionDataBean.isAdmin());
							//incoming shipments
							this.setShipmentsReceivedInfo(request, bizLogic, loggedInUserSiteId,
									recordsPerPage, dashboardForm);
							//Outgoing Shipments
							this.setOutgoingShipmentsInfo(request, bizLogic, loggedInUserSiteId,
									recordsPerPage, dashboardForm);
							//Outgoing Shipment Requests
							this.setRequestsSentInfo(request, requestBizLogic, loggedInUserSiteId,
									recordsPerPage, dashboardForm);
						}
						else
						{
							this.setShipmentsReceivedInfo(request, bizLogic, loggedInUserSiteId,
									recordsPerPage, dashboardForm);
							this.setRequestsReceivedInfo(request, requestBizLogic,
									loggedInUserSiteId, recordsPerPage, dashboardForm,
									sessionDataBean.isAdmin());
						}
						//bug 12809 end
						request.setAttribute("identifierFieldIndex", 0);
						request.setAttribute("isAdmin", sessionDataBean.isAdmin());
						this.setRecordsPerPageToRequest(request, recordsPerPage);
						final String requestFor = request.getParameter(REQUEST_FOR);
						if (requestFor != null)
						{
							request.setAttribute(REQUEST_FOR, requestFor);
						}
					}
					else
					{
						this.setEmptyDashboardData(request);
					}
				}
			}
		}
		//		catch(DAOException e)
		//		{
		//			e.printStackTrace();
		//		}
		catch (final BizLogicException e)
		{
			logger.error(e.getMessage(), e);
		}
		catch (final ApplicationException appException)
		{
			logger.error(appException.getMessage(), appException);
		}
		finally
		{
			try
			{
				AppUtility.closeDAOSession(dao);
			}
			catch (final ApplicationException e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		return mapping.findForward(edu.wustl.catissuecore.util.global.Constants.SUCCESS);
	}

	/**
	 * This method calculates total number of records and pages of incoming and outgoing shipment
	 *  and sets these vales in form.
	 * @param form - DashboardForm
	 * @param bizLogic - ShipmentBizLogic
	 * @param recordsPerPage - records Per Page
	 * @param loggedInUserSiteId - site ids of logged in users
	 * @param request - request
	 * @throws BizLogicException - BizLogicException
	 */
	private void getTotalRecordsAndPagesForShipment(DashboardForm form, ShipmentBizLogic bizLogic,
			Integer recordsPerPage, Long[] loggedInUserSiteId, HttpServletRequest request)
			throws BizLogicException
	{
		Integer totalRecords = (Integer) request.getSession().getAttribute(
				"incomingShipTotalRecords");
		Integer totalPages = (Integer) request.getSession().getAttribute("incomingShipTotalPages");
		final String columnNamesShipmentsReceived[] = {RECEIVERSITE_ID, CREATED_DATE};
		totalRecords = this.getTotalNumberOfRecords(bizLogic, loggedInUserSiteId, recordsPerPage,
				columnNamesShipmentsReceived);
		int numOfRecords = 1;
		if (recordsPerPage == null)
		{
			numOfRecords = 5;
		}
		else
		{
			numOfRecords = recordsPerPage.intValue();
		}
		totalPages = this.getTotalNumberOfPages(numOfRecords, totalRecords);
		form.setIncomingShipmentsTotalPages(totalPages);
		form.setIncomingShipmentsTotalRecords(totalRecords);

		Integer totalRecordsOutgoing = (Integer) request.getSession().getAttribute(
				"outgoingShipTotalRecords");
		Integer totalPagesOutgoing = (Integer) request.getSession().getAttribute(
				"outgoingShipTotalPages");
		final String columnNamesOutgoingShipments[] = {SENDER_SITE_ID, CREATED_DATE};
		totalRecordsOutgoing = this.getTotalNumberOfRecords(bizLogic, loggedInUserSiteId,
				recordsPerPage, columnNamesOutgoingShipments);
		totalPagesOutgoing = this.getTotalNumberOfPages(numOfRecords, totalRecordsOutgoing);
		form.setOutgoingShipmentsTotalPages(totalPagesOutgoing);
		form.setOutgoingShipmentsTotalRecords(totalRecordsOutgoing);

	}

	/**
	 * This method calculates total number of records and pages of incoming and outgoing shipment request
	 *  and sets these vales in form.
	 * @param form - DashboardForm
	 * @param requestBizLogic - ShipmentRequestBizLogic
	 * @param recordsPerPage - records Per Page
	 * @param loggedInUserSiteId - site ids of logged in users
	 * @param request - request
	 * @throws BizLogicException - BizLogicException
	 */
	private void getTotalRecordsAndPagesForRequest(DashboardForm form,
			ShipmentRequestBizLogic requestBizLogic, Integer recordsPerPage,
			Long[] loggedInUserSiteId, HttpServletRequest request) throws BizLogicException
	{
		Integer totalRecords = (Integer) request.getSession().getAttribute(
				"reqReceivedTotalRecords");
		Integer totalPages = (Integer) request.getSession().getAttribute("reqReceivedTotalPages");
		final String columnNamesRequestsReceived[] = {RECEIVERSITE_ID, SEND_DATE};
		if (totalRecords == null)
		{
			totalRecords = this.getTotalNumberOfRecords(requestBizLogic, loggedInUserSiteId,
					recordsPerPage, columnNamesRequestsReceived);
		}
		int numOfRecords = 1;
		if (recordsPerPage == null)
		{
			numOfRecords = 5;			
		}
		else
		{
			numOfRecords = recordsPerPage.intValue();
		}
		if (totalPages == null)
		{
			totalPages = this.getTotalNumberOfPages(numOfRecords, totalRecords);
		}
		form.setIncomingShipmentReqsTotalPages(totalPages);
		form.setIncomingShipmentReqsTotalRecords(totalRecords);

		Integer totalRecordsSent = (Integer) request.getSession().getAttribute(
				"reqSentTotalRecords");
		Integer totalPagesSent = (Integer) request.getSession().getAttribute("reqSentTotalPages");
		final String columnNamesRequestsSent[] = {SENDER_SITE_ID, CREATED_DATE};
		if (totalRecordsSent == null)
		{
			totalRecordsSent = this.getTotalNumberOfRecords(requestBizLogic, loggedInUserSiteId,
					recordsPerPage, columnNamesRequestsSent);
		}
		if (totalPagesSent == null)
		{
			totalPagesSent = this.getTotalNumberOfPages(numOfRecords, totalRecordsSent);
		}
		form.setOutgoingShipmentReqsTotalPages(totalPagesSent);
		form.setOutgoingShipmentReqsTotalRecords(totalRecordsSent);
	}

	/**
	 * this method sets the dashboard data.
	 * @param request request to be processed.
	 */
	private void setEmptyDashboardData(HttpServletRequest request)
	{
		this.setRecordsPerPageToRequest(request, 5);
		request.setAttribute("identifierFieldIndex", 0);
		request.setAttribute("isAdmin", false);
		final String requestFor = request.getParameter(REQUEST_FOR);
		final List<Object[]> emptyList = new ArrayList<Object[]>();
		request.setAttribute("requestsReceivedHeader", this.getRequestsReceivedHeader());
		request.setAttribute("requestsReceivedList", emptyList);
		request.setAttribute("receivedReqUserNameIndex", 4);
		request.setAttribute("requestsSentHeader", this.getRequestsSentHeader());
		request.setAttribute("requestsSentList", emptyList);
		request.setAttribute("sentReqUserNameIndex", 4);
		request.setAttribute("sentReqActivityStatusIndex", 6);
		request.setAttribute("sentReqReceiverSiteNameIndex", 3);
		request.setAttribute("incomingShipmentsHeader", this.getIncomingShipmentsHeader());
		request.setAttribute("incomingShipmentsList", emptyList);
		request.setAttribute("incomingShipUserNameIndex", 5);
		request.setAttribute("outgoingShipmentsHeader", this.getOutgoingShipmentsHeader());
		request.setAttribute("outgoingShipmentsList", emptyList);
		request.setAttribute("outgoingShipUserNameIndex", 5);
		if (requestFor != null)
		{
			request.setAttribute(REQUEST_FOR, requestFor);
		}
	}

	/**
	 * set request details.
	 * @param request request object to receive parameters.
	 * @param bizLogic object of ShipmentRequestBizLogic class.
	 * @param loggedInUserSiteId user ids array.
	 * @param recordsPerPage count of records per page.
	 * @param dashboardForm - DashboardForm
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	private void setRequestsSentInfo(HttpServletRequest request, ShipmentRequestBizLogic bizLogic,
			Long[] loggedInUserSiteId, Integer recordsPerPage, DashboardForm dashboardForm)
			throws BizLogicException
	{
		final List<String> requestsSentHeader = this.getRequestsSentHeader();
		List<Object[]> requestsSentList = null;
		final String pageNoFromReq = request.getParameter(REQUEST_SENT_PAGE_NUM);
		Integer currentPageNo = Integer.valueOf( 1 );//new Integer(1);
		if (pageNoFromReq != null)
		{
			currentPageNo = Integer.parseInt(pageNoFromReq);
		}
		else if (request.getSession().getAttribute(REQUEST_SENT_PAGE_NUM) != null)
		{
			currentPageNo = (Integer) request.getSession().getAttribute(REQUEST_SENT_PAGE_NUM);
		}
		else
		{
			currentPageNo = 1;
		}
		int startIndex = 0;
		int numOfRecords = 1;
		final Integer[] indexValues = this.getStartIndexAndRecordsPerPage(currentPageNo,
				recordsPerPage);
		startIndex = indexValues[0];
		numOfRecords = indexValues[1];
		currentPageNo = indexValues[2];
		// Pass the siteiID array of logged in user
		requestsSentList = this.getRequestsSentList(bizLogic, loggedInUserSiteId, startIndex,
				numOfRecords);
		recordsPerPage = numOfRecords;
		this.setPagenationInfoToSession(request, REQUEST_SENT_PAGE_NUM, currentPageNo,
				"reqSentTotalRecords", dashboardForm.getOutgoingShipmentReqsTotalRecords(),
				"reqSentTotalPages", dashboardForm.getOutgoingShipmentReqsTotalPages(),
				"reqSentRecordsPerPage", recordsPerPage);
		request.setAttribute("requestsSentHeader", requestsSentHeader);
		request.setAttribute("requestsSentList", requestsSentList);
		request.setAttribute("sentReqUserNameIndex", 4);
		request.setAttribute("sentReqActivityStatusIndex", 6);
		request.setAttribute("sentReqReceiverSiteNameIndex", 3);
	}

	/**
	 * 
	 * @param currentPageNo - current Page No
	 * @param recordsPerPage - records Per Page
	 * @return Integer array with values as startIndex,numOfRecords,currentPageNo
	 */
	private Integer[] getStartIndexAndRecordsPerPage(Integer currentPageNo, Integer recordsPerPage)
	{
		int startIndex = 0;
		int numOfRecords = 1;
		if (currentPageNo != null && recordsPerPage != null)
		{
			startIndex = (currentPageNo.intValue() - 1) * recordsPerPage.intValue();
			numOfRecords = recordsPerPage.intValue();
		}
		else
		{
			startIndex = 0;
			numOfRecords = 5;
			currentPageNo = 1;
		}
		final Integer[] returnValues = {startIndex, numOfRecords, currentPageNo};
		return returnValues;
	}

	/**
	 * gets the requests list.
	 * @param bizLogic object of ShipmentRequestBizLogic class.
	 * @param loggedInUserSiteId ids of users.
	 * @param startIndex the starting index.
	 * @param numOfRecords count of number of records per page.
	 * @return list if objects.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	private List<Object[]> getRequestsSentList(ShipmentRequestBizLogic bizLogic,
			Long[] loggedInUserSiteId, int startIndex, int numOfRecords) throws BizLogicException
	{
		final String selectColumnName = "shipment.id,shipment.senderContactPerson.lastName, shipment.label, shipment.receiverSite.name, shipment.senderContactPerson.firstName, shipment.sendDate, shipment.activityStatus";
		return bizLogic.getShipmentRequests(selectColumnName, SENDER_SITE_ID, CREATED_DATE,
				loggedInUserSiteId, startIndex, numOfRecords, " shipment.activityStatus <> '"
						+ Status.ACTIVITY_STATUS_CLOSED.toString() + "' AND ");
	}

	/**
	 * gets send requests header.
	 * @return list of strings.
	 */
	private List<String> getRequestsSentHeader()
	{
		final List<String> requestsSentHeader = new ArrayList<String>();
		requestsSentHeader.add("Label");
		requestsSentHeader.add("Receiver Site");
		requestsSentHeader.add("Requester");
		requestsSentHeader.add("Requested On");
		requestsSentHeader.add("Status");
		return requestsSentHeader;
	}

	/**
	 * sets the records per page to the request.
	 * @param request to be accessed.
	 * @param recordsPerPage count.
	 */
	private void setRecordsPerPageToRequest(HttpServletRequest request, int recordsPerPage)
	{
		final List<NameValueBean> resultsPerPageList = new ArrayList<NameValueBean>();
		resultsPerPageList.add(new NameValueBean("5", "5"));
		resultsPerPageList.add(new NameValueBean("10", "10"));
		resultsPerPageList.add(new NameValueBean("15", "15"));
		resultsPerPageList.add(new NameValueBean("20", "20"));
		final DashboardForm dashboardForm = new DashboardForm();
		dashboardForm.setRecordsPerPage(recordsPerPage);
		request.setAttribute("dashboardForm", dashboardForm);
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.RESULTS_PER_PAGE,
				resultsPerPageList);
	}

	/**
	 * sets the shipment received info.
	 * @param request to fetch parameters.
	 * @param bizLogic ShipmentBizLogic object.
	 * @param loggedInUserSiteId site ids of logged in user.
	 * @param recordsPerPage count of records to be displayed.
	 * @param dashboardForm - DashboardForm
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	//For Incoming Shipments
	private void setShipmentsReceivedInfo(HttpServletRequest request, ShipmentBizLogic bizLogic,
			Long[] loggedInUserSiteId, Integer recordsPerPage, DashboardForm dashboardForm)
			throws BizLogicException
	{
		final List<String> incomingShipmentsHeader = this.getIncomingShipmentsHeader();
		// Pass the siteiID array of logged in user
		List<Object[]> incomingShipmentsList = null;
		final String pageNoFromReq = request.getParameter(INCOMING_SHIP_PAGE_NUM);
		Integer currentPageNo = Integer.valueOf( 1 );//new Integer(1);
		if (pageNoFromReq != null)
		{
			currentPageNo = Integer.parseInt(pageNoFromReq);
		}
		else if (request.getSession().getAttribute(INCOMING_SHIP_PAGE_NUM) != null)
		{
			currentPageNo = (Integer) request.getSession()
					.getAttribute(INCOMING_SHIP_PAGE_NUM);
		}
		else
		{
			currentPageNo = 1;
		}
		int startIndex = 0;
		int numOfRecords = 10;
		final Integer[] indexValues = this.getStartIndexAndRecordsPerPage(currentPageNo,
				recordsPerPage);
		startIndex = indexValues[0];
		numOfRecords = indexValues[1];
		currentPageNo = indexValues[2];
		// Pass the siteiID array of logged in user
		incomingShipmentsList = this.getIncomingShipmentsList(bizLogic, loggedInUserSiteId,
				startIndex, numOfRecords);
		recordsPerPage = numOfRecords;
		this.setPagenationInfoToSession(request, INCOMING_SHIP_PAGE_NUM, currentPageNo,
				"incomingShipTotalRecords", dashboardForm.getIncomingShipmentsTotalRecords(),
				"incomingShipTotalPages", dashboardForm.getIncomingShipmentsTotalPages(),
				"incomingShipRecordsPerPage", recordsPerPage);
		request.setAttribute("incomingShipmentsHeader", incomingShipmentsHeader);
		request.setAttribute("incomingShipmentsList", incomingShipmentsList);
		request.setAttribute("incomingShipUserNameIndex", 5);
	}

	/**
	 * This method is used to calculate total number of pages
	 * @param numOfRecords - Number of records per page
	 * @param totalRecords - total records
	 * @return totalPages
	 */
	private Integer getTotalNumberOfPages(Integer numOfRecords, Integer totalRecords)
	{
		Integer totalPages = 1;
		totalPages = totalRecords % numOfRecords == 0
				? totalRecords / numOfRecords
				: (totalRecords / numOfRecords) + 1;
		return totalPages;

	}

	/**
	 * This method is used to calculate total number of records
	 * @param bizLogic - BaseShipmentBizLogic object
	 * @param loggedInUserSiteId - site id
	 * @param recordsPerPage - records per page 
	 * @param columnNames - columns names
	 * @return Total number of records
	 * @throws BizLogicException - BizLogicException
	 */
	private Integer getTotalNumberOfRecords(BaseShipmentBizLogic bizLogic,
			Long[] loggedInUserSiteId, Integer recordsPerPage, String columnNames[])
			throws BizLogicException
	{
		Integer totalRecords = 0;
		final int startIndex = 0;
		int numOfRecords = 10;
		if (recordsPerPage != null)
		{
			numOfRecords = recordsPerPage.intValue();
		}
		else
		{
			numOfRecords = 5;
		}
		if (bizLogic instanceof ShipmentBizLogic)
		{
			final ShipmentBizLogic shipmentBizLogic = (ShipmentBizLogic) bizLogic;
			totalRecords = shipmentBizLogic.getShipmentsCount(columnNames[0], columnNames[1],
					loggedInUserSiteId, startIndex, numOfRecords);
		}
		else if (bizLogic instanceof ShipmentRequestBizLogic)
		{
			final ShipmentRequestBizLogic requestBizLogic = (ShipmentRequestBizLogic) bizLogic;
			totalRecords = requestBizLogic.getShipmentRequestsCount(columnNames[0], columnNames[1],
					loggedInUserSiteId, startIndex, numOfRecords);
		}
		return totalRecords;
	}

	/**
	 * gets the incoming shipment headers.
	 * @return list of string containing headers.
	 */
	private List<String> getIncomingShipmentsHeader()
	{
		final List<String> incomingShipmentsHeader = new ArrayList<String>();
		incomingShipmentsHeader.add("Sender Site");
		incomingShipmentsHeader.add("Label");
		incomingShipmentsHeader.add("Barcode");
		incomingShipmentsHeader.add("Sender");
		incomingShipmentsHeader.add("Sent On");
		//outgoingShipmentsHeader.add("Time");
		incomingShipmentsHeader.add("Status");
		return incomingShipmentsHeader;
	}

	/**
	 * gets the list of incoming shipments.
	 * @param bizLogic the ShipmentBizLogic object.
	 * @param siteId id of the site.
	 * @param startIndex the starting index of shipments.
	 * @param numOfRecords integer containing no. of records.
	 * @return list of objects.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	private List<Object[]> getIncomingShipmentsList(ShipmentBizLogic bizLogic, Long[] siteId,
			int startIndex, int numOfRecords) throws BizLogicException
	{
		final String selectColumnName = "shipment.id,shipment.senderContactPerson.lastName, shipment.senderSite.name, shipment.label, shipment.barcode, shipment.senderContactPerson.firstName, shipment.sendDate, shipment.activityStatus";
		return bizLogic.getShipments(selectColumnName, RECEIVERSITE_ID, SEND_DATE, siteId,
				startIndex, numOfRecords);
	}

	// For Outgoing Shipments
	/**
	 * sets the outgoing shipment details.
	 * @param request the request to be processed.
	 * @param bizLogic ShipmentBizlogic object.
	 * @param loggedInUserSiteId site ids of logged in users.
	 * @param recordsPerPage count containing records per page.
	 * @param dashboardForm - DashboardForm
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	private void setOutgoingShipmentsInfo(HttpServletRequest request, ShipmentBizLogic bizLogic,
			Long[] loggedInUserSiteId, Integer recordsPerPage, DashboardForm dashboardForm)
			throws BizLogicException
	{
		final List<String> outgoingShipmentsHeader = this.getOutgoingShipmentsHeader();
		List<Object[]> outgoingShipmentsList = null;
		final String pageNoFromReq = request.getParameter(OUTGOING_SHIP_PAGE_NUM);
		Integer currentPageNo = Integer.valueOf( 1 );
		if (pageNoFromReq != null)
		{
			currentPageNo = Integer.parseInt(pageNoFromReq);
		}
		else if (request.getSession().getAttribute(OUTGOING_SHIP_PAGE_NUM) != null)
		{
			currentPageNo = (Integer) request.getSession()
					.getAttribute(OUTGOING_SHIP_PAGE_NUM);
		}
		else
		{
			currentPageNo = 1;
		}
		int startIndex = 0;
		int numOfRecords = 10;
		final Integer[] indexValues = this.getStartIndexAndRecordsPerPage(currentPageNo,
				recordsPerPage);
		startIndex = indexValues[0];
		numOfRecords = indexValues[1];
		currentPageNo = indexValues[2];
		// Pass the siteiID array of logged in user
		outgoingShipmentsList = this.getOutgoingShipmentsList(bizLogic, loggedInUserSiteId,
				startIndex, numOfRecords);
		recordsPerPage = numOfRecords;
		this.setPagenationInfoToSession(request, OUTGOING_SHIP_PAGE_NUM, currentPageNo,
				"outgoingShipTotalRecords", dashboardForm.getOutgoingShipmentsTotalRecords(),
				"outgoingShipTotalPages", dashboardForm.getOutgoingShipmentsTotalPages(),
				"outgoingShipRecordsPerPage", recordsPerPage);
		request.setAttribute("outgoingShipmentsHeader", outgoingShipmentsHeader);
		request.setAttribute("outgoingShipmentsList", outgoingShipmentsList);
		request.setAttribute("outgoingShipUserNameIndex", 5);
	}

	/**
	 * gets the outgoing shipment list.
	 * @param bizLogic ShipmentBizLogic object.
	 * @param siteId the site id.
	 * @param startIndex starting index integer.
	 * @param numOfRecords count of records per page.
	 * @return list of objects.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	private List<Object[]> getOutgoingShipmentsList(ShipmentBizLogic bizLogic, Long[] siteId,
			int startIndex, int numOfRecords) throws BizLogicException
	{
		final String selectColumnName = "shipment.id,shipment.senderContactPerson.lastName, shipment.receiverSite.name, shipment.label, shipment.barcode, shipment.senderContactPerson.firstName, shipment.sendDate, shipment.activityStatus";
		return bizLogic.getShipments(selectColumnName, SENDER_SITE_ID, CREATED_DATE, siteId,
				startIndex, numOfRecords);
	}

	/**
	 * gets the outgoing shipment header.
	 * @return list of strings containing the header.
	 */
	private List<String> getOutgoingShipmentsHeader()
	{
		final List<String> outgoingShipmentsHeader = new ArrayList<String>();
		outgoingShipmentsHeader.add("Receiver Site");
		outgoingShipmentsHeader.add("Label");
		outgoingShipmentsHeader.add("Barcode");
		outgoingShipmentsHeader.add("Sender");
		outgoingShipmentsHeader.add("Sent On");
		//outgoingShipmentsHeader.add("Time");
		outgoingShipmentsHeader.add("Status");
		return outgoingShipmentsHeader;
	}

	// For Requests Shipments
	/**
	 * sets the receiving requests info.
	 * @param request request to process.
	 * @param bizLogic ShipmentRequestBizLogic object.
	 * @param loggedInUserSiteId site ids of logged in users.
	 * @param recordsPerPage integer containing records per page count.
	 * @param dashboardForm - DashboardForm
	 * @throws BizLogicException if some bizlogic error occurs.
	 */
	private void setRequestsReceivedInfo(HttpServletRequest request,
			ShipmentRequestBizLogic bizLogic, Long[] loggedInUserSiteId, Integer recordsPerPage,
			DashboardForm dashboardForm, boolean isAdmin) throws BizLogicException
	{
		final List<String> requestsReceivedHeader = this.getRequestsReceivedHeader();
		List<Object[]> requestsReceivedList = null;
		final String pageNoFromReq = request.getParameter("reqReceivedCurrentPageNo");
		//Integer currentPageNo = new Integer(1);
		Integer currentPageNo = Integer.valueOf( 1 );
		if (pageNoFromReq != null)
		{
			currentPageNo = Integer.parseInt(pageNoFromReq);
		}
		else if (request.getSession().getAttribute("reqReceivedCurrentPageNo") != null)
		{
			currentPageNo = (Integer) request.getSession().getAttribute("reqReceivedCurrentPageNo");
		}
		else
		{
			currentPageNo = 1;
		}
		int startIndex = 0;
		int numOfRecords = 1;
		final Integer[] indexValues = this.getStartIndexAndRecordsPerPage(currentPageNo,
				recordsPerPage);
		startIndex = indexValues[0];
		numOfRecords = indexValues[1];
		currentPageNo = indexValues[2];
		// Pass the siteiID array of logged in user
		requestsReceivedList = this.getRequestsReceivedList(bizLogic, loggedInUserSiteId,
				startIndex, numOfRecords, isAdmin);
		recordsPerPage = numOfRecords;
		this.setPagenationInfoToSession(request, "reqReceivedCurrentPageNo", currentPageNo,
				"reqReceivedTotalRecords", dashboardForm.getIncomingShipmentReqsTotalRecords(),
				"reqReceivedTotalPages", dashboardForm.getIncomingShipmentReqsTotalPages(),
				"reqReceivedRecordsPerPage", recordsPerPage);
		request.setAttribute("requestsReceivedHeader", requestsReceivedHeader);
		request.setAttribute("requestsReceivedList", requestsReceivedList);
		request.setAttribute("receivedReqUserNameIndex", 4);
	}

	/**
	 * sets the pagination information to session.
	 * @param request to be processed.
	 * @param currentPageNoName name of the current page.
	 * @param currentPageNo integer denoting current page.
	 * @param totalRecordsName names of records.
	 * @param totalRecords count of total records.
	 * @param totalPagesName name of total pages.
	 * @param totalPages total pages count.
	 * @param recordsPerPageName records per page.
	 * @param recordsPerPage count of records per page.
	 */
	private void setPagenationInfoToSession(HttpServletRequest request, String currentPageNoName,
			Integer currentPageNo, String totalRecordsName, Integer totalRecords,
			String totalPagesName, Integer totalPages, String recordsPerPageName,
			Integer recordsPerPage)
	{
		request.getSession().setAttribute(currentPageNoName, currentPageNo);
		request.getSession().setAttribute(totalRecordsName, totalRecords);
		request.getSession().setAttribute(totalPagesName, totalPages);
		request.getSession().setAttribute(recordsPerPageName, recordsPerPage);
	}

	/**
	 * gets the requests received list.
	 * @param bizLogic ShipmentRequestBizLogic object.
	 * @param loggedInUserSiteIds site ids of logged in user.
	 * @param startIndex starting index integer.
	 * @param numOfRecords count of numbar of records.
	 * @return list of objects.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	private List<Object[]> getRequestsReceivedList(ShipmentRequestBizLogic bizLogic,
			Long[] loggedInUserSiteIds, int startIndex, int numOfRecords, boolean isAdmin)
			throws BizLogicException
	{
		final String selectColumnName = "shipment.id, shipment.senderContactPerson.lastName, shipment.label, shipment.senderSite.name, shipment.senderContactPerson.firstName, shipment.sendDate, shipment.activityStatus";
		//bug 13572 start
		/**
		 * If last where clause is added to query, then query will not return proper results
		 * in case of super admin.But in case of site admin query will return proper results.
		 */
		if (isAdmin)
		{
			return bizLogic.getShipmentRequests(selectColumnName, RECEIVERSITE_ID, SEND_DATE,
					loggedInUserSiteIds, startIndex, numOfRecords, " shipment.activityStatus != '"
							+ Status.ACTIVITY_STATUS_CLOSED.toString() + "' AND ");
		}
		else
		{
			return bizLogic.getShipmentRequests(selectColumnName, RECEIVERSITE_ID, SEND_DATE,
					loggedInUserSiteIds, startIndex, numOfRecords,
					" shipment.senderSite <> shipment.receiverSite AND ");
		}
		//bug 13572 end
	}

	/**
	 * gets the requests received header.
	 * @return list of string containing headers.
	 */
	private List<String> getRequestsReceivedHeader()
	{
		final List<String> requestsReceivedHeader = new ArrayList<String>();
		requestsReceivedHeader.add("Label");
		requestsReceivedHeader.add("Requester Site");
		requestsReceivedHeader.add("Requester");
		requestsReceivedHeader.add("Requested On");
		requestsReceivedHeader.add("Status");
		return requestsReceivedHeader;
	}
}
