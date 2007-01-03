/*
 * Created on Nov 9, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.CacheException;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.MultipleSpecimenStorageLocationForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MultipleSpecimenSubmitAction extends BaseAction
{

	private int specimenCounter = 1;

	/**
	 * 
	 */
	public MultipleSpecimenSubmitAction()
	{
		//super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception

	{
		Logger.out.debug("Inside : MultipleSpecimenSubmitAction");
		MultipleSpecimenStorageLocationForm aForm = (MultipleSpecimenStorageLocationForm) form;
		Logger.out.debug("\naForm.getSpecimenOnUIMap():\n---------------\n" + aForm.getSpecimenOnUIMap() + "\n-----------\n");
		String target = Constants.FAILURE;
		String pageOf = request.getParameter(Constants.PAGEOF);
		request.setAttribute(Constants.PAGEOF,pageOf);
		
		//		if(dataValidate())
		//		{
		specimenCounter = 1;
		Map specimenMap = null;
		try
		{
			specimenMap = setDataInSpecimens(aForm, request);
		}

		catch (DAOException e)
		{

			ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
			if (errors == null || errors.size() == 0)
			{
				errors = new ActionErrors();
			}
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties.getValue("specimen.storageContainer")));
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward(target);
		}

		List specimenOrderList = (List) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_SPECIMEN_ORDER_LIST);
		/**
		 *   Following code is added for giving x and y positions if not already given
		 */
		List positionsToBeAllocatedList = new ArrayList();
		List usedPositionsList = new ArrayList();
		for (int i = 0; i < specimenOrderList.size(); i++)
		{
			Specimen specimen = (Specimen) specimenOrderList.get(i);
			if(specimen.getStorageContainer()!=null)
			{
			if (specimen.getPositionDimensionOne() == null || specimen.getPositionDimensionTwo() == null)
			{
				positionsToBeAllocatedList.add(specimen);
			}
			else
			{
				usedPositionsList.add(specimen.getStorageContainer().getId() + Constants.STORAGE_LOCATION_SAPERATOR
						+ specimen.getPositionDimensionOne() + Constants.STORAGE_LOCATION_SAPERATOR + specimen.getPositionDimensionTwo());
			}
			}

        	List listOfDerivedSpecimen = (List) specimenMap.get(specimen);

        	if(listOfDerivedSpecimen == null)
        	{
        		specimenMap.put(specimen,new ArrayList());
        	}
        	else
        	{
			for (int j = 0; j < listOfDerivedSpecimen.size(); j++)
			{
				Specimen derivedSpecimen = (Specimen) listOfDerivedSpecimen.get(j);
				if(derivedSpecimen.getStorageContainer()!=null)
				{
				if (derivedSpecimen.getPositionDimensionOne() == null || derivedSpecimen.getPositionDimensionTwo() == null)
				{
					positionsToBeAllocatedList.add(derivedSpecimen);
				}
				else
				{

					usedPositionsList.add(derivedSpecimen.getStorageContainer().getId() + Constants.STORAGE_LOCATION_SAPERATOR
							+ derivedSpecimen.getPositionDimensionOne() + Constants.STORAGE_LOCATION_SAPERATOR
							+ derivedSpecimen.getPositionDimensionTwo());
				}
				}
			}
        	}

		}

		boolean isContainerFull = false;
		Map containerMapFromCache = null;
		try
		{
			containerMapFromCache = (TreeMap) StorageContainerUtil.getContainerMapFromCache();
		}
		catch (CacheException e)
		{
			e.printStackTrace();
		}

		for (int k = 0; k < positionsToBeAllocatedList.size(); k++)
		{
			Specimen specimen = (Specimen) positionsToBeAllocatedList.get(k);
			if (containerMapFromCache != null)
			{
				Iterator itr = containerMapFromCache.keySet().iterator();
				while (itr.hasNext())
				{   
					boolean flag = false;
					NameValueBean nvb = (NameValueBean) itr.next();
					String containerId = nvb.getValue().toString();

					// TODO
					if (containerId.equalsIgnoreCase(specimen.getStorageContainer().getId().toString()))
					{
						
						//String containerId = nvb.getValue();
						Map tempMap = (Map) containerMapFromCache.get(nvb);
						Iterator tempIterator = tempMap.keySet().iterator();

						while (tempIterator.hasNext())
						{
							NameValueBean nvb1 = (NameValueBean) tempIterator.next();
							List yPosList = (List) tempMap.get(nvb1);
							for (int i = 0; i < yPosList.size(); i++)
							{
								NameValueBean nvb2 = (NameValueBean) yPosList.get(i);
								String availaleStoragePosition = containerId + Constants.STORAGE_LOCATION_SAPERATOR + nvb1.getValue()
										+ Constants.STORAGE_LOCATION_SAPERATOR + nvb2.getValue();
								int j = 0;

								for (; j < usedPositionsList.size(); j++)
								{
									if (usedPositionsList.get(j).toString().equals(availaleStoragePosition))
										break;
								}
								if (j == usedPositionsList.size())
								{
									usedPositionsList.add(availaleStoragePosition);

									specimen.setPositionDimensionOne(new Integer(nvb1.getValue()));
									specimen.setPositionDimensionTwo(new Integer(nvb2.getValue()));
									flag = true;
									break;
									
								}

							}
							if(flag)
							break;
						}
					}
					if(flag)
				   break;
				}
			}
			if (specimen.getPositionDimensionOne() == null || specimen.getPositionDimensionTwo() == null)
			{
				ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
				if (errors == null || errors.size() == 0)
				{
					errors = new ActionErrors();
				}
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
						.getValue("specimen.positionInStorageContainerForMultiple")));
				saveErrors(request, errors);
				return mapping.findForward(target);
				//	TODO throw new DAOException("The container you specified does not have enough space to allocate storage position for Aliquot Number " + specimenNumber);
			}

		}

		try
		{
			List specimenList = insertSpecimens(request, specimenMap);
			request.setAttribute(Constants.MULTIPLE_SPECIMEN_SUBMIT_SUCCESSFUL, Constants.MULTIPLE_SPECIMEN_SUBMIT_SUCCESSFUL);
			target = Constants.SUCCESS;
			// ----------------- report page
			Collection specimenCollection = (Collection) request.getSession().getAttribute(Constants.SAVED_SPECIMEN_COLLECTION);
			// TODO Distribution
			request.getSession().setAttribute(Constants.SAVED_SPECIMEN_COLLECTION, null);

			//to display all inserted specimens Mandar: 16Nov06 
			//				request.setAttribute(Constants.SAVED_SPECIMEN_COLLECTION, specimenCollection);
			request.setAttribute(Constants.SAVED_SPECIMEN_COLLECTION, specimenList);

			ActionMessages msgs = new ActionMessages();
			msgs.add("success", new ActionMessage("multipleSpecimen.add.success", String.valueOf(specimenList.size())));
			saveMessages(request, msgs);

		}
		catch (Exception excp)
		{
			Logger.out.error(excp);
			ActionErrors errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("empty.message", excp.getMessage()));
			saveErrors(request, errors);
			target = Constants.FAILURE;
		}
		//		}

		return mapping.findForward(target);
	}

	private boolean dataValidate()
	{
		return true;
	}

	private Map setDataInSpecimens(MultipleSpecimenStorageLocationForm aForm, HttpServletRequest request) throws DAOException
	{
		Map specimenOnUIMap = aForm.getSpecimenOnUIMap();
		List specimenOrderList = (List) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_SPECIMEN_ORDER_LIST);
		Map originalSpecimenMap = (HashMap) request.getSession().getAttribute(Constants.SPECIMEN_MAP_KEY);

		//		printMap(originalSpecimenMap);		
		if (specimenOrderList != null && !specimenOrderList.isEmpty())
		{
			for (int count = 0; count < specimenOrderList.size(); count++)
			{
				Specimen specimenFromList = (Specimen) specimenOrderList.get(count);

				Iterator specimenKeys = originalSpecimenMap.keySet().iterator();
				int cnt = 1;
				while (specimenKeys.hasNext())
				{
					Specimen originalSpecimen = (Specimen) specimenKeys.next();

					if (originalSpecimen.equals(specimenFromList))
					{
						String keyPrefix = "Specimen:" + (count + 1) + "_";
						String labelKey = keyPrefix + "Label";
						String barKey = keyPrefix + "Barcode";
						String storageContainerKey = keyPrefix + "StorageContainer";
						String positionOneKey = keyPrefix + "PositionOne";
						String positionTwoKey = keyPrefix + "PositionTwo";
						String virtuallyLocatedKey = keyPrefix + "virtuallyLocated";
						String deriveCountKey = keyPrefix + "DeriveCount";

						setSpecimenValues(specimenOnUIMap, originalSpecimen, labelKey, barKey, storageContainerKey, positionOneKey, positionTwoKey,
								virtuallyLocatedKey);

						String deriveCount = (String) specimenOnUIMap.get(deriveCountKey);
						int deriveCountValue = Integer.parseInt(deriveCount);
						if (deriveCountValue > 0)
						{
							setDerivedSpecimens(originalSpecimenMap, originalSpecimen, keyPrefix, specimenOnUIMap);
						}
					}
				}
			}
			Logger.out.debug(">>>>>>>>>>>>>>>>>>>> Specimen Data Set >>>>>>>>>>>>>>");
		}
		Logger.out.debug("Updated Map : ");
		//		printMap(originalSpecimenMap);
		return originalSpecimenMap;

	}

	private void setDerivedSpecimens(Map originalSpecimenMap, Specimen mainSpecimen, String parentKey, Map specimenOnUIMap) throws DAOException
	{
		List derivedList = (List) originalSpecimenMap.get(mainSpecimen);
		if (derivedList != null && !derivedList.isEmpty())
		{
			for (int cnt = 1; cnt <= derivedList.size(); cnt++)
			{
				String derivedPrefix = parentKey + "DerivedSpecimen:";
				Specimen derivedSpecimen = (Specimen) derivedList.get(cnt - 1);
				String labelKey = derivedPrefix + cnt + "_Label";
				String barKey = derivedPrefix + cnt + "_Barcode";
				String storageContainerKey = derivedPrefix + cnt + "_StorageContainer";
				String positionOneKey = derivedPrefix + cnt + "_PositionOne";
				String positionTwoKey = derivedPrefix + cnt + "_PositionTwo";
				String virtuallyLocatedKey = derivedPrefix + cnt + "_virtuallyLocated";

				setSpecimenValues(specimenOnUIMap, derivedSpecimen, labelKey, barKey, storageContainerKey, positionOneKey, positionTwoKey,
						virtuallyLocatedKey);

			}
		}
	}

	private void setSpecimenValues(Map specimenOnUIMap, Specimen specimen, String labelKey, String barKey, String storageContainerKey,
			String positionOneKey, String positionTwoKey, String virtuallyLocatedKey) throws DAOException
	{
		//fetch from UI map
		String label = (String) specimenOnUIMap.get(labelKey);
		String barcode = (String) specimenOnUIMap.get(barKey);
		String virtuallyLocated = (String) specimenOnUIMap.get(virtuallyLocatedKey);
		String radioButonKey = "radio_" + specimenCounter;

		//setting values in main specimen
		specimen.setLabel(label);
		specimen.setBarcode(barcode);

		//get the container values based on user selection from dropdown or map
		if (specimenOnUIMap.get(radioButonKey).equals("1"))
		{
			specimen.setStorageContainer(null);
			specimen.setPositionDimensionOne(null);
			specimen.setPositionDimensionTwo(null);
		}
		if (specimenOnUIMap.get(radioButonKey).equals("2"))
		{
			String containerId = (String) specimenOnUIMap.get(storageContainerKey);
			String posDim1 = (String) specimenOnUIMap.get(positionOneKey);
			String posDim2 = (String) specimenOnUIMap.get(positionTwoKey);
			if (specimen.getStorageContainer() == null)
			{
				specimen.setStorageContainer(new StorageContainer());
			}
			specimen.getStorageContainer().setId(new Long(containerId));
			specimen.setPositionDimensionOne(new Integer(posDim1));
			specimen.setPositionDimensionTwo(new Integer(posDim2));
		}
		else if (specimenOnUIMap.get(radioButonKey).equals("3"))
		{
			String containerName = (String) specimenOnUIMap.get("Specimen:" + specimenCounter + "_StorageContainer_name" + "_fromMap");

			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = {"id"};
			String[] whereColumnName = {"name"};
			String[] whereColumnCondition = {"="};
			Object[] whereColumnValue = {containerName};
			String joinCondition = null;

			StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.STORAGE_CONTAINER_FORM_ID);

			List list = bizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
			Long containerId = null;
			if (!list.isEmpty())
			{
				containerId = (Long) list.get(0);
			}
			else
			{
				String message = ApplicationProperties.getValue("specimen.storageContainer");
				throw new DAOException(ApplicationProperties.getValue("errors.invalid", message));
			}
			String posDim1 = (String) specimenOnUIMap.get("Specimen:" + specimenCounter + "_positionDimensionOne" + "_fromMap");
			String posDim2 = (String) specimenOnUIMap.get("Specimen:" + specimenCounter + "_positionDimensionTwo" + "_fromMap");
			specimen.setStorageContainer(new StorageContainer());
			if (specimen.getStorageContainer() == null)
			{
				specimen.setStorageContainer(new StorageContainer());
			}
			specimen.getStorageContainer().setId(containerId);
			if (posDim1 != null && !posDim1.trim().equals(""))
			{
				specimen.setPositionDimensionOne(new Integer(posDim1));
			}
			else
			{
				specimen.setPositionDimensionOne(null);
			}
			if (posDim2 != null && !posDim2.trim().equals(""))
			{
				specimen.setPositionDimensionTwo(new Integer(posDim2));
			}
			else
			{
				specimen.setPositionDimensionTwo(null);
			}

		}
		specimenCounter++;
	}

	/**
	 * This method saves collection of specimens to the database.
	 *  TODO Error handling. 
	 * @param request
	 * @param specimenCollection
	 */
	private List insertSpecimens(HttpServletRequest request, Map specimenMap) throws Exception
	{
		IBizLogic bizLogic;

		bizLogic = AbstractBizLogicFactory.getBizLogic(ApplicationProperties.getValue("app.bizLogicFactory"), "getBizLogic",
				Constants.NEW_SPECIMEN_FORM_ID);
		SessionDataBean sessionBean = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);

		bizLogic.insert(specimenMap, sessionBean, Constants.HIBERNATE_DAO);

		//specimen list to display 
		List specimenList = createList(specimenMap);
		return specimenList;
	}

	/*
	 * This method creates a list of specimen that are inserted.
	 * This list will be used to display specimen labels on the report's page.
	 */
	private List createList(Map specimenMap)
	{
		List specimenList = new ArrayList();

		Iterator specimenIterator = specimenMap.keySet().iterator();
		while (specimenIterator.hasNext())
		{
			Specimen specimen = (Specimen) specimenIterator.next();
			specimenList.add(specimen);
			List derivedSpecimens = (List) specimenMap.get(specimen);

			if (derivedSpecimens != null)
			{
				for (int i = 0; i < derivedSpecimens.size(); i++)
				{

					Specimen derivedSpecimen = (Specimen) derivedSpecimens.get(i);
					specimenList.add(derivedSpecimen);
				}
			}
		}
		return specimenList;
	}
}

