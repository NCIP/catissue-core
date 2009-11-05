
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.bizlogic.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.printserviceclient.LabelPrinter;
import edu.wustl.catissuecore.printserviceclient.LabelPrinterFactory;
import edu.wustl.catissuecore.printservicemodule.SpecimenLabelPrinterImpl;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.SecurityManagerFactory;
import gov.nih.nci.security.authorization.domainobjects.User;

/**
 * This class is for Print Action.
 *
 * @author falguni_sachde
 */
public class PrintAction extends Action
{
	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(PrintAction.class);

	/**
	 * Overrides the execute method of Action class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws IOException
	 *             I/O exception
	 * @throws ServletException
	 *             servlet exception
	 *  @throws     BizLogicException BizLogicException
	 * @return value for ActionForward object
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException,BizLogicException
	{
		final AdvanceSearchForm searchForm = (AdvanceSearchForm) form;
		String nextforwardTo = request.getParameter("nextForwardTo");
		String printerType = request.getParameter("printerType");
		String printerLocation = request.getParameter("printerLocation");
		if (printerType == null || printerType.equals(""))
		{
			printerType = " ";
		}
		if (printerLocation == null || printerLocation.trim().equals(""))
		{
			printerLocation = " ";
		}
		final SessionDataBean objBean = (SessionDataBean) request.getSession().getAttribute(
				"sessionData");
		final String strIpAddress = objBean.getIpAddress();
		try
		{
			final gov.nih.nci.security.authorization.domainobjects.User objUser = this
					.getUserObject(request, objBean);
			final HashMap forwardToPrintMap = (HashMap) request.getAttribute("forwardToPrintMap");
			// SCG Label printing
			if (forwardToPrintMap != null && forwardToPrintMap.size() > 0
					&& forwardToPrintMap.get("specimenCollectionGroupId") != null)
			{
				final String scgId = (String) forwardToPrintMap.get("specimenCollectionGroupId");
				final DAO dao = DAOConfigFactory.getInstance().getDAOFactory(
						Constants.APPLICATION_NAME).getDAO();
				SpecimenCollectionGroup objSCG = null;
				boolean printStauts = false;
				try
				{
					dao.openSession(null);
					final Object object =
					dao.retrieveById(SpecimenCollectionGroup.class.getName(),new Long(scgId));

					if (object != null)
					{
						objSCG = (SpecimenCollectionGroup) object;
					}

					final LabelPrinter labelPrinter = LabelPrinterFactory
							.getInstance("specimencollectiongroup");
					printStauts = labelPrinter.printLabel(objSCG, strIpAddress, objUser,
							printerType, printerLocation);

				}
				catch (final DAOException exception)
				{
					this.logger.error(exception.getMessage(), exception);
					exception.printStackTrace();
					throw exception;
				}
				finally
				{
					dao.commit();
					dao.closeSession();
				}
				this.setStatusMessage(printStauts, request);

			}
			// For Specimen
			// Check for Specimen type of object.Retrieve object based on Id and
			// call printerimpl class
			if (forwardToPrintMap != null && forwardToPrintMap.size() > 0
					&& forwardToPrintMap.get("specimenId") != null)
			{
				final String specimenId = (String) forwardToPrintMap.get("specimenId");
				final DAO dao = DAOConfigFactory.getInstance().getDAOFactory(
						Constants.APPLICATION_NAME).getDAO();
				boolean printStauts = false;
				try
				{
					dao.openSession(null);
					final Specimen objSpecimen = (Specimen) dao.retrieveById(Specimen.class
							.getName(), new Long(specimenId));

					final LabelPrinter labelPrinter =
						LabelPrinterFactory.getInstance("specimen");
					printStauts = labelPrinter.printLabel(objSpecimen, strIpAddress, objUser,
							printerType, printerLocation);

				}
				catch (final DAOException exception)
				{
					this.logger.error(exception.getMessage(), exception);
					exception.printStackTrace() ;
					throw exception;
				}
				finally
				{
					dao.closeSession();
				}
				this.setStatusMessage(printStauts, request);
			}

			// nextforwardTo = printAliquotLabel(form, request,
			// nextforwardTo,objBean);
			// nextforwardTo - coming null now CPQueryPrintSpecimenEdit
			// For multiple specimen page
			if (forwardToPrintMap != null && forwardToPrintMap.size() > 0
					&& request.getAttribute("printMultiple") != null
					&& request.getAttribute("printMultiple").equals("1"))
			{

				final LinkedHashSet specimenDomainCollection = (LinkedHashSet) forwardToPrintMap
						.get("printMultipleSpecimen");
				final Iterator iterator = specimenDomainCollection.iterator();
				final List<AbstractDomainObject> specimenList = new ArrayList();
				while (iterator.hasNext())
				{
					final Specimen objSpecimen = (Specimen) iterator.next();
					specimenList.add(objSpecimen);
				}
				final LabelPrinter labelPrinter = LabelPrinterFactory.getInstance("specimen");
				final boolean printStauts = labelPrinter.printLabel(specimenList, strIpAddress,
						objUser, printerType, printerLocation);
				this.setStatusMessage(printStauts, request);

				if (request.getAttribute("pageOf") != null)
				{
					nextforwardTo = request.getAttribute("pageOf").toString();
				}
				else
				{
					nextforwardTo = Constants.SUCCESS;
				}
			}
			// For Anti. specimen page
			if (forwardToPrintMap != null && forwardToPrintMap.size() > 0
					&& request.getAttribute("AntiSpecimen") != null
					&& request.getAttribute("AntiSpecimen").equals("1"))
			{

				final HashSet specimenDomainCollection = (HashSet) forwardToPrintMap
						.get("printAntiSpecimen");
				final Iterator iterator = specimenDomainCollection.iterator();
				final List<AbstractDomainObject> specimenList = new ArrayList();
				while (iterator.hasNext())
				{
					final Specimen objSpecimen = (Specimen) iterator.next();
					specimenList.add(objSpecimen);
				}
				final LabelPrinter labelPrinter = LabelPrinterFactory.getInstance("specimen");
				final boolean printStauts = labelPrinter.printLabel(specimenList, strIpAddress,
						objUser, printerType, printerLocation);
				this.setStatusMessage(printStauts, request);

				nextforwardTo = "printAntiSuccess";
			}
			// added for Storage Container Printing
			if (forwardToPrintMap != null && forwardToPrintMap.size() > 0
					&& forwardToPrintMap.get("StorageContainerObjID") != null)
			{
				final DAO dao = DAOConfigFactory.getInstance().getDAOFactory(
						Constants.APPLICATION_NAME).getDAO();
				boolean printStauts = false;
				List similarContainerList = (List) request.getAttribute("similarContainerList");
				final LabelPrinter labelPrinter = LabelPrinterFactory
						.getInstance("storagecontainer");
				if (similarContainerList == null || similarContainerList.size() == 0)
				{
					final String containerId = (String) forwardToPrintMap
							.get("StorageContainerObjID");
					if (containerId != null)
					{
						final NameValueBean bean = new
						NameValueBean(containerId, containerId);
						similarContainerList = new ArrayList();
						similarContainerList.add(bean);
					}
				}
				try
				{
					dao.openSession(null);
					final List<AbstractDomainObject> containerList =
						new ArrayList<AbstractDomainObject>();
					for (int i = 0; i < similarContainerList.size(); i++)
					{
						final NameValueBean bean = (NameValueBean)
						similarContainerList.get(i);
						final String value = bean.getValue();
						final Object object = dao.retrieveById
						(StorageContainer.class.getName(),new Long(value));
						if (object != null)
						{
							final StorageContainer containerObj =
								(StorageContainer) object;
							containerList.add(containerObj);
						}
					}
					printStauts = labelPrinter.printLabel(containerList, strIpAddress, objUser,
							printerType, printerLocation);
				}
				catch (final DAOException exception)
				{
					this.logger.error(exception.getMessage(), exception);
					exception.printStackTrace();
					throw exception;
				}
				finally
				{
					dao.closeSession();
				}
				this.setStatusMessage(printStauts, request);
			}
			else if (forwardToPrintMap != null && forwardToPrintMap.size() > 0
					&& forwardToPrintMap.get( Constants.PRINT_SPECIMEN_FROM_LISTVIEW )!= null)
			{

				boolean printStauts = false;
				final HttpSession session = request.getSession();
				final QueryShoppingCart cart = (QueryShoppingCart) session
				.getAttribute(Constants.QUERY_SHOPPING_CART);
				final QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
				final List<String> gridSspecimenIds = new LinkedList<String>
				(bizLogic.getEntityIdsList(cart,
				Arrays.asList(Constants.specimenNameArray), getGridValue(searchForm)));
				final DAO dao = DAOConfigFactory.getInstance().getDAOFactory(
						Constants.APPLICATION_NAME).getDAO();
				try
				{
				dao.openSession(null);
				List < AbstractDomainObject > specimenList = this.getSpecimenList
				(dao,gridSspecimenIds );
				SpecimenLabelPrinterImpl labelPrinter = new SpecimenLabelPrinterImpl();
				printStauts = labelPrinter.printLabel(specimenList, strIpAddress,
						objUser, printerType,
						printerLocation, Constants.PRINT_SPECIMEN_FROM_LISTVIEW);
				nextforwardTo = Constants.SUCCESS ;
				}
				catch (final DAOException exception)
				{
					this.logger.error(exception.getMessage(), exception);
					exception.printStackTrace();
					throw exception;
				}
				finally
				{
					dao.closeSession();
				}
				this.setStatusMessage(printStauts, request);

			}
			else if (forwardToPrintMap != null && forwardToPrintMap.size() > 0
					&& forwardToPrintMap.get
					(Constants.PRINT_SPECIMEN_DISTRIBUTION_REPORT )!= null)
			{
				 boolean printStauts = false;
				List<String> specimenIds = this.getSpecimenIDListFromMap
				(Constants.PRINT_SPECIMEN_DISTRIBUTION_REPORT, forwardToPrintMap );
				final DAO dao = DAOConfigFactory.getInstance().getDAOFactory(
						Constants.APPLICATION_NAME).getDAO();
				try
				{
				dao.openSession(null);
				List < AbstractDomainObject > specimenList = this.getSpecimenList
				(dao,specimenIds );
				final LabelPrinter labelPrinter = LabelPrinterFactory.getInstance("specimen");
				 printStauts = labelPrinter.printLabel(specimenList, strIpAddress,
						objUser, printerType, printerLocation);

				nextforwardTo = Constants.SUCCESS;
				}
				catch (final DAOException exception)
				{
					this.logger.error(exception.getMessage(), exception);
					exception.printStackTrace();
					throw exception;
				}
				finally
				{
					dao.closeSession();
				}
				this.setStatusMessage(printStauts, request);
			}
		}
		catch (final Exception e)
		{
			// Any other exception
			// e.printStackTrace();
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
			ActionMessages messages = (ActionMessages) request.getAttribute(MESSAGE_KEY);
			if (messages == null)
			{
				messages = new ActionMessages();

			}
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("print.failure"));
			this.saveMessages(request, messages);
		}

		return mapping.findForward(nextforwardTo);
	}
	/**
	 * This method will return gridValues.
	 * @param adSearchForm AdvanceSearch object
	 * @return gridValues
	 */
	private List<Integer> getGridValue(final AdvanceSearchForm adSearchForm)
	{
		List<Integer> gridValues = new LinkedList<Integer>();
		if (adSearchForm.getOrderedString() != null && !"".equals(adSearchForm.getOrderedString().trim()))
		{
			final String[] gridOrdered = (adSearchForm.getOrderedString()).split(",");
			for (final String element : gridOrdered)
			{
				final int gridOrderedIndex = Integer.parseInt(element);
				gridValues.add(gridOrderedIndex - 1);
			}
		}
		return gridValues;
	}
	/**
	 * This method will return list of Specimen objects.
	 * @param specimenDomainCollection - specimenDomainCollection
	 * @return list of AbstractDomainObject objects
	 */
	private List < AbstractDomainObject > getSpecimenList(HashSet specimenDomainCollection)
	{
		Iterator iterator = specimenDomainCollection.iterator();
		List < AbstractDomainObject > specimenList = new ArrayList();
		while (iterator.hasNext())
		{
			Specimen objSpecimen = (Specimen) iterator.next();
			specimenList.add(objSpecimen);
		}
		return specimenList;
	}
	/**
	 * Print labels.
	 * @param specimenIdList - specimenIdList
	 * @param dao DAO object
	 * @return boolean
	 * @throws Exception - Exception
	 */
	private List<AbstractDomainObject> getSpecimenList(DAO dao,List<String> specimenIdList) throws Exception
	{

		List < AbstractDomainObject > specimenList = new ArrayList < AbstractDomainObject >();
		try
		{
			for(String id : specimenIdList)
			{
				Object object = dao.retrieveById(Specimen.class.getName(),
						new Long(id));
				if (object != null)
				{
					Specimen specimenObj = (Specimen) object;
					specimenList.add(specimenObj);
				}
			}
		}
		catch (DAOException daoException)
		{
			this.logger.error(daoException.getMessage(), daoException);
			daoException.printStackTrace();
			throw new BizLogicException(daoException.getErrorKey(), daoException, daoException
					.getMsgValues());
		}
		return specimenList;
	}
	/**
	 * This method is used to get specimen ids from Map.
	 * @param key - Map key
	 * @param forwardToPrintMap - Map
	 * @return List of specimen id's
	 */
	private List<String> getSpecimenIDListFromMap(String key,HashMap forwardToPrintMap)
	{
		List<String> specimenList = new ArrayList<String>();
		if(key.equals( Constants.PRINT_SPECIMEN_FROM_LISTVIEW ))
		{
			LinkedHashSet<String> specimenIds = (LinkedHashSet) forwardToPrintMap
			.get(key);
			Iterator it = specimenIds.iterator();
			while(it.hasNext())
			{
				specimenList.add(it.next().toString());
			}
		}
		else
		{
			specimenList = (List) forwardToPrintMap.get(key);
		}
		return specimenList;
	}

	/**
	 *
	 * @param form : form
	 * @param request : request
	 * @param nextforwardTo : nextforwardTo
	 * @param objBean : objBean
	 * @return String : String
	 * @throws Exception : Exception
	 */
	public String printAliquotLabel(ActionForm form, HttpServletRequest request,
			String nextforwardTo, SessionDataBean objBean) throws Exception
	{
		User objUser = null;
		try
		{
			objUser = this.getUserObject(request, objBean);
		}
		catch (final SMException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw AppUtility.handleSMException(e);
		}

		final List<AbstractDomainObject> listofAliquot = (List<AbstractDomainObject>) request
				.getAttribute("specimenList");
		boolean printStauts = false;
		LabelPrinter labelPrinter = null;
		try
		{
			labelPrinter = LabelPrinterFactory.getInstance("specimen");
		}
		catch (final Exception e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new Exception("Error Printing label");
		}
		if (listofAliquot != null)
		{
			final String printerType = request.getParameter("printerType");
			final String printerLocation = request.getParameter("printerLocation");
			printStauts = labelPrinter.printLabel(listofAliquot, objBean.getIpAddress(), objUser,
					printerType, printerLocation);
		}
		this.setStatusMessage(printStauts, request);
		nextforwardTo = request.getParameter("forwardTo");

		return nextforwardTo;
	}

	/**
	 * @param printStauts : printStauts
	 * @param request : request
	 */
	private void setStatusMessage(boolean printStauts, HttpServletRequest request)
	{
		if (printStauts)
		{
			// printservice returns true ,Printed Successfully
			ActionMessages messages = (ActionMessages) request.getAttribute(MESSAGE_KEY);
			if (messages == null)
			{
				messages = new ActionMessages();

			}
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("print.success"));
			this.saveMessages(request, messages);
		}
		else
		{

			// If any case print service return false ,it means error while
			// printing.
			ActionMessages messages = (ActionMessages) request.getAttribute(MESSAGE_KEY);
			if (messages == null)
			{
				messages = new ActionMessages();

			}
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("print.failure"));
			this.saveMessages(request, messages);
		}

	}

	/**
	 *
	 * @param request : request
	 * @param objBean : objBean
	 * @return User : User
	 * @throws SMException : SMException
	 */
	private User getUserObject(HttpServletRequest request, SessionDataBean objBean)
			throws SMException
	{
		final String strUserId = objBean.getCsmUserId().toString();
		final User objUser = SecurityManagerFactory.getSecurityManager().getUserById(strUserId);
		return objUser;
	}

}