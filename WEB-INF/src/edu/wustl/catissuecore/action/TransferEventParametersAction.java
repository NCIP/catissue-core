/**
 * <p>
 * Title: TransferEventParametersAction Class>
 * <p>
 * Description: This class initializes the fields in the TransferEventParameters
 * Add/Edit webpage.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Mandar Deshmukh
 * @version 1.00 Created on Aug 05, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.actionForm.TransferEventParametersForm;
import edu.wustl.catissuecore.bizlogic.StorageContainerForSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.tag.ScriptGenerator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * @author mandar_deshmukh This class initializes the fields in the
 *         TransferEventParameters Add/Edit webpage.
 */
public class TransferEventParametersAction extends SpecimenEventParametersAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(TransferEventParametersAction.class);

	/**
	* @param request object of HttpServletRequest
	* @param eventParametersForm : eventParametersForm
	* @throws Exception : Exception
	*/
	@Override
	protected void setRequestParameters(HttpServletRequest request,
			EventParametersForm eventParametersForm) throws Exception
	{
		final TransferEventParametersForm transferEventParametersForm = (TransferEventParametersForm) eventParametersForm;

		final List<NameValueBean> storagePositionListForTransferEvent = AppUtility
				.getStoragePositionTypeListForTransferEvent();

		request.setAttribute("storageListForTransferEvent", storagePositionListForTransferEvent);

		final StorageContainerForSpecimenBizLogic scbizLogic = new StorageContainerForSpecimenBizLogic();
		TreeMap containerMap = new TreeMap();
		final String exceedingMaxLimit = "false";
		List initialValues = null;

		final String operation = (String) request.getAttribute(Constants.OPERATION);
		String formName = null;
		if (operation.equals(Constants.EDIT))
		{
			formName = Constants.TRANSFER_EVENT_PARAMETERS_EDIT_ACTION;
		}
		else
		{
			formName = Constants.TRANSFER_EVENT_PARAMETERS_ADD_ACTION;
		}
		request.setAttribute("formName", formName);
		final String getJSForOutermostDataTable = ScriptGenerator.getJSForOutermostDataTable();
		request.setAttribute("getJSForOutermostDataTable", getJSForOutermostDataTable);

		System.out.println("************" + getJSForOutermostDataTable);

		request.setAttribute("posOne", Constants.POS_ONE);
		request.setAttribute("posTwo", Constants.POS_TWO);
		request.setAttribute("storContId", Constants.STORAGE_CONTAINER_ID);
		request.setAttribute("add", Constants.ADD);

		final SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
		final DAO dao = AppUtility.openDAOSession(sessionData);
		try
		{
			if (transferEventParametersForm.getOperation().equals(Constants.ADD))
			{

				String identifier = (String) request.getAttribute(Constants.SPECIMEN_ID);
				if (identifier == null)
				{
					identifier = request.getParameter(Constants.SPECIMEN_ID);
				}

				this.logger.debug("\t\t*******************************SpecimenID : " + identifier);
				final Object object = dao.retrieveById(Specimen.class.getName(), new Long(
						identifier));
				if (object != null)
				{

					try
					{

						final Specimen specimen = (Specimen) object;

						String positionOne = null;
						String positionTwo = null;
						String storageContainerID = null;
						String fromPositionData = "virtual Location";
						final Long id = specimen.getId();
						final String attributeName = "specimenPosition.storageContainer";
						final List stContainerList = dao.retrieveAttribute(Specimen.class, "id",
								id, attributeName);
						if ((stContainerList != null) && (stContainerList.size() > 0))
						{
							final StorageContainer stContainer = (StorageContainer) stContainerList
									.get(0);
							if (stContainer != null)
							{
								if (specimen != null
										&& specimen.getSpecimenPosition() != null
										&& specimen.getSpecimenPosition().getPositionDimensionOne() != null
										&& specimen.getSpecimenPosition().getPositionDimensionTwo() != null)
								{
									positionOne = specimen.getSpecimenPosition()
											.getPositionDimensionOne().toString();
									positionTwo = specimen.getSpecimenPosition()
											.getPositionDimensionTwo().toString();
									// StorageContainer container =
									// specimen.getStorageContainer();
									storageContainerID = stContainer.getId().toString();
									fromPositionData = stContainer.getName() + ":" + " Pos("
											+ positionOne + "," + positionTwo + ")";
								}
							}
						}
						// The fromPositionData(storageContainer Info) of
						// specimen
						// of this event.
						transferEventParametersForm.setFromPositionData(fromPositionData);

						// POSITION 1
						request.setAttribute(Constants.POS_ONE, positionOne);

						// POSITION 2
						request.setAttribute(Constants.POS_TWO, positionTwo);
						request.setAttribute(Constants.STORAGE_CONTAINER_ID, storageContainerID);
						final Long collectionProtocolId = this.getCollectionProtocolId(specimen
								.getId());
						final long cpId = collectionProtocolId.longValue();
						final String className = specimen.getClassName();
						final String typeName = specimen.getSpecimenType();

						this.logger.info("COllection Protocol Id :" + cpId);
						request.setAttribute(Constants.COLLECTION_PROTOCOL_ID, cpId + "");
						request.setAttribute(Constants.SPECIMEN_CLASS_NAME, className);
						this.logger.info("Spcimen Class:" + className);
						containerMap = scbizLogic.
						getAllocatedContainerMapForSpecimen(
						AppUtility.setparameterList(cpId,className,0,typeName), sessionData, dao);
						initialValues = this.setInitialValue(request, transferEventParametersForm,
								containerMap);
					}
					catch (final DAOException daoException)
					{
						this.logger.error(daoException.getMessage(),daoException);
						throw AppUtility.getApplicationException(daoException, daoException
								.getErrorKeyName(), daoException.getMsgValues());
					}
				}
			} // operation=add
			else
			{

				final Integer id = new Integer(transferEventParametersForm.getStorageContainer());
				String parentContainerName = "";

				final Object object = dao.retrieveById(StorageContainer.class.getName(), new Long(
						transferEventParametersForm.getStorageContainer()));
				if (object != null)
				{
					final StorageContainer container = (StorageContainer) object;
					parentContainerName = container.getName();

				}
				final Integer pos1 = new Integer(transferEventParametersForm
						.getPositionDimensionOne());
				final Integer pos2 = new Integer(transferEventParametersForm
						.getPositionDimensionTwo());

				final List pos2List = new ArrayList();
				pos2List.add(new NameValueBean(pos2, pos2));

				final Map pos1Map = new TreeMap();
				pos1Map.put(new NameValueBean(pos1, pos1), pos2List);
				containerMap.put(new NameValueBean(parentContainerName, id), pos1Map);

				final String[] startingPoints = new String[]{"-1", "-1", "-1"};
				if (transferEventParametersForm.getStorageContainer() != null
						&& !transferEventParametersForm.getStorageContainer().equals("-1"))
				{
					startingPoints[0] = transferEventParametersForm.getStorageContainer();

				}
				if (transferEventParametersForm.getPositionDimensionOne() != null
						&& !transferEventParametersForm.getPositionDimensionOne().equals("-1"))
				{
					startingPoints[1] = transferEventParametersForm.getPositionDimensionOne();
				}
				if (transferEventParametersForm.getPositionDimensionTwo() != null
						&& !transferEventParametersForm.getPositionDimensionTwo().equals("-1"))
				{
					startingPoints[2] = transferEventParametersForm.getPositionDimensionTwo();
				}
				initialValues = new ArrayList();
				this.logger.info("Starting points[0]" + startingPoints[0]);
				this.logger.info("Starting points[1]" + startingPoints[1]);
				this.logger.info("Starting points[2]" + startingPoints[2]);
				initialValues.add(startingPoints);

			}
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}

		request.setAttribute("initValues", initialValues);
		request.setAttribute(Constants.EXCEEDS_MAX_LIMIT, exceedingMaxLimit);
		request.setAttribute("dataMap", containerMap);
		request.setAttribute("transeferEventParametersAddAction",
				Constants.TRANSFER_EVENT_PARAMETERS_ADD_ACTION);

		// scriplet shifted from TransferEventParameters.jsp
		// Map dataMap = (Map)
		// request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);

		request.setAttribute("labelNames", Constants.STORAGE_CONTAINER_LABEL);

		final String[] attrNames = {"storageContainer", "positionDimensionOne",
				"positionDimensionTwo"};
		request.setAttribute("attrNames", attrNames);
		final String[] tdStyleClassArray = {"formFieldSized15", "customFormField",
				"customFormField"};
		request.setAttribute("tdStyleClassArray", tdStyleClassArray);
		String[] initValues = new String[3];
		final List initValuesList = (List) request.getAttribute("initValues");
		if (initValuesList != null)
		{
			initValues = (String[]) initValuesList.get(0);
		}

		request.setAttribute("initValues", initValues);

		final String rowNumber = "1";
		final String styClass = "formFieldSized5";
		final String tdStyleClass = "customFormField";
		// boolean disabled = true;
		final String onChange = "onCustomListBoxChange(this)";

		request.setAttribute("onChange", onChange);
		request.setAttribute("rowNumber", rowNumber);
		request.setAttribute("styClass", styClass);
		request.setAttribute("tdStyleClass", tdStyleClass);

		final String getJSEquivalentFor = ScriptGenerator.getJSEquivalentFor(containerMap,
				rowNumber);
		request.setAttribute("getJSEquivalentFor", getJSEquivalentFor);

		System.out.println("###########################" + containerMap);
		System.out.println("###########################" + getJSEquivalentFor);

		// boolean buttonDisabled = true;

		String className = (String) request.getAttribute(Constants.SPECIMEN_CLASS_NAME);
		if (className == null)
		{
			className = "";
		}

		String collectionProtocolId = (String) request
				.getAttribute(Constants.COLLECTION_PROTOCOL_ID);
		if (collectionProtocolId == null)
		{
			collectionProtocolId = "";
		}

		final String url = "ShowFramedPage.do?pageOf=pageOfSpecimen&amp;selectedContainerName="
				+ "selectedContainerName&amp;pos1=pos1&amp;pos2=pos2&amp;containerId=containerId"
				+ "&" + Constants.CAN_HOLD_SPECIMEN_CLASS + "=" + className + "&"
				+ Constants.CAN_HOLD_COLLECTION_PROTOCOL + "=" + collectionProtocolId;

		final String buttonOnClicked = "mapButtonClickedOnNewSpecimen('" + url
				+ "','transferEvents')";
		// String buttonOnClicked =
		// "javascript:NewWindow('"+url+"','name','810','320','yes');return false"
		// ;
		request.setAttribute("buttonOnClicked", buttonOnClicked);
		final String noOfEmptyCombos = "3";

		request.setAttribute("noOfEmptyCombos", noOfEmptyCombos);

		final int radioSelected = transferEventParametersForm.getStContSelection();
		boolean dropDownDisable = false;
		boolean textBoxDisable = false;
		if (radioSelected == 1)
		{
			textBoxDisable = true;
		}
		else if (radioSelected == 2)
		{
			dropDownDisable = true;
		}
		request.setAttribute("dropDownDisable", dropDownDisable);
		request.setAttribute("textBoxDisable", textBoxDisable);
	}

	/**
	 * @param request : request
	 * @param transferEventParametersForm : transferEventParametersForm
	 * @param containerMap : containerMap
	 * @return List : List
	 */
	private List setInitialValue(HttpServletRequest request,
			TransferEventParametersForm transferEventParametersForm, TreeMap containerMap)
	{
		List initialValues = null;
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		if (containerMap.isEmpty())
		{
			if (errors == null || errors.size() == 0)
			{
				errors = new ActionErrors();
			}
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("storageposition.not.available"));
			this.saveErrors(request, errors);
		}
		if (errors == null || errors.size() == 0)
		{
			initialValues = StorageContainerUtil.checkForInitialValues(containerMap);
		}
		else
		{
			final String[] startingPoints = new String[3];
			startingPoints[0] = transferEventParametersForm.getStorageContainer();
			startingPoints[1] = transferEventParametersForm.getPositionDimensionOne();
			startingPoints[2] = transferEventParametersForm.getPositionDimensionTwo();
			initialValues = new ArrayList();
			initialValues.add(startingPoints);

		}
		return initialValues;
	}

	/**
	 *
	 * @param specimenId : specimenId
	 * @param bizLogic : bizLogic
	 * @return Long : Long
	 * @throws ApplicationException : ApplicationException
	 */
	private Long getCollectionProtocolId(Long specimenId) throws ApplicationException
	{
		// Changed by Falguni.
		// Find collectionprotocol id using HQL
		final String colProtHql = "select scg.collectionProtocolRegistration.collectionProtocol.id"
				+ " from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg,"
				+ " edu.wustl.catissuecore.domain.Specimen as spec "
				+ " where spec.specimenCollectionGroup.id=scg.id and spec.id="
				+ specimenId.longValue();
		List collectionProtocolIdList;
		collectionProtocolIdList = AppUtility.executeQuery(colProtHql);
		final Long collectionProtocolId = (Long) collectionProtocolIdList.get(0);
		return collectionProtocolId;
	}

}