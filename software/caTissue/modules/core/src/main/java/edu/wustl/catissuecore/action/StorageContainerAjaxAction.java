
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.StorageContainerForSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.tag.NLevelCustomCombo;
import edu.wustl.common.util.tag.ScriptGenerator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;

public class StorageContainerAjaxAction extends SecureAction
{
	/**
	 * @param mapping object of ActionMapping class.
	 * @param form object of ActionForm class.
	 * @param request object of HttpServletRequest class.
	 * @param response object of HttpServletResponse class.
	 * @return forward mapping.
	 * @throws Exception if some problem occurs.
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
    {
		Map specimenLocMap=new TreeMap();
		String rowNumber = request.getParameter("rowNumber");
		Map containerMap = new TreeMap();
		final StorageContainerForSpecimenBizLogic bizLogic = new StorageContainerForSpecimenBizLogic();
		final SessionDataBean sessionDataBean = this.getSessionData(request);
		// Uncommented due to addition of 'auto' functionality.
		// Bug 14263
		if (sessionDataBean != null)
		{
			DAO dao = null;
			try
			{
				dao = AppUtility.openDAOSession(sessionDataBean);
				String stContSelection = request.getParameter("stContSelection");
				request.setAttribute("stContSelection", stContSelection);
				String spClass = null;
				String spType = null;
				String specimenId = request.getParameter("specimenId");
				if (specimenId != null)
				{
					/*String specimenClass = (String) shipmentBizLogic.retrieveAttribute(
							Specimen.class.getName(), Long.valueOf(specimenId), "specimenClass");*/
					final String sourceObjectName = Specimen.class.getName();
					final String[] selectColumnName = {"specimenClass", "specimenType"};
					final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
					queryWhereClause.addCondition(new EqualClause("id", Long.valueOf(specimenId)));
					final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
					if (list.size() != 0)
					{
						final Object[] valArr = (Object[]) list.get(0);
						if (valArr != null)
						{
							spClass = ((String) valArr[0]);
							spType = ((String) valArr[1]);
						}
					}
					String collectionProtocolId = CollectionProtocolUtil.getCPIdFromSpecimen(specimenId, dao);
					request.setAttribute(edu.wustl.catissuecore.util.global.Constants.COLLECTION_PROTOCOL_ID,
							collectionProtocolId);

					if (!collectionProtocolId.trim().equals("") && stContSelection != null)
					{
						if(stContSelection.equals("2"))
						{
							List<Object> parameterList = AppUtility.setparameterList(Long.valueOf(collectionProtocolId).longValue(),spClass,0,spType);
							containerMap = bizLogic.getAllocatedContainerMapForSpecimen(parameterList, sessionDataBean, dao);
							this.setContainerStorageInformation(containerMap, request,specimenLocMap);
							this.setSpecimenStorageInformation(containerMap, request, specimenLocMap);
							createResponseData(containerMap, response, specimenId, specimenLocMap,rowNumber);
						}
						else if(stContSelection.equals("3"))
						{
							StringBuffer sb = new StringBuffer();
							sb.append(specimenId);
							sb.append("#");
							sb.append(spClass);
							sb.append("#");
							sb.append(collectionProtocolId);
							response.setContentType("text/html");
							response.setHeader("Cache-Control", "no-cache");
							final String msg = sb.toString();
							response.getWriter().write(msg);
						}
					}
				}
			}
			finally
			{
				if (dao != null)
				{
					dao.closeSession();
				}
			}

		}
		return null;

	}

	/**
	 * sets the storage container information.
	 * @param containerMap the treemap.
	 * @param bizLogic object of StorageContainerBizLogic class.
	 * @param request the request to be processed.
	 * @param shipmentReceivingForm form containing all values.
	 */
	private void setContainerStorageInformation(Map containerMap, HttpServletRequest request,
			Map specimenLocMap)
	{

		// For ParentStorageContainer Container
		final String parentContainerSelected = request.getParameter("parentContainerSelected");
		request.setAttribute("parentContainerSelected", parentContainerSelected);

		final String containerId = request.getParameter("containerObjectId");

		int containerCount = 0;
		this.checkForSufficientAvailablePositions(request, containerMap, containerCount,
				ApplicationProperties.getValue("shipment.contentsContainers"));

		this.populateContainerStorageLocations(specimenLocMap, containerMap);
		if (containerId != null && !containerId.trim().equals(""))
		{
			request.setAttribute("containerObjectId", containerId);
		}
	}

	/**
	 * This function populates the availability map with available storage locations.
	 * @param specimenLocMap object of AliquotForm
	 * @param containerMap Map containing data for container
	 */
	private void populateContainerStorageLocations(Map specimenLocMap, Map containerMap)
	{
		final Map map = new TreeMap();
		final String keyName = "Container:";
		int number = 0;
		this.populateStorageLocations(containerMap, map, keyName, number);
		specimenLocMap.putAll(map);
		
	}

	/**
	 * @param containerMap Map containing data for container
	 * @param map Map
	 * @param keyName key Name Specimen or Container
	 * @param number Collection size.
	 */
	private void populateStorageLocations(Map containerMap, Map map, String keyName, int number)
	{
		if (!containerMap.isEmpty())
		{
			int counter = 1;
			final Object[] containerId = containerMap.keySet().toArray();
			for (int i = 0; i < containerId.length; i++)
			{
				final Map xDimMap = (Map) containerMap.get(containerId[i]);
				if (!xDimMap.isEmpty())
				{
					final Object[] xDim = xDimMap.keySet().toArray();
					for (int countJ = 0; countJ < xDim.length; countJ++)
					{
						final List yDimList = (List) xDimMap.get(xDim[countJ]);
						if (yDimList != null && yDimList.size() > 0)
						{
							final String initailValuesKey = keyName + counter + "_initialValues";
							final String[] initialValues = new String[3];
							initialValues[0] = ((NameValueBean) containerId[i]).getValue();
							initialValues[1] = ((NameValueBean) xDim[countJ]).getValue();
							initialValues[2] = ((NameValueBean) yDimList.get(0)).getValue();
							map.put(initailValuesKey, initialValues);
							counter++;
						}
						
					}
				}
			}
		}
		else
		{
			for (int commonCounter = 0; commonCounter < number; commonCounter++)
			{
				final String initailValueKey = keyName + (commonCounter + 1) + "_initialValues";
				final String[] initialValues = new String[3];
				initialValues[0] = "";
				initialValues[1] = "";
				initialValues[2] = "";
				map.put(initailValueKey, initialValues);
			}
		}
	}

	/**
	 * This method checks whether there are sufficient storage locations are available or not.
	 * @param request object of HttpServletRequest.
	 * @param storableCount integer containing count.
	 * @param containerMap Map containing data for contaner.
	 * @param objectName string containing the name of the object.
	 */
	private void checkForSufficientAvailablePositions(HttpServletRequest request, Map containerMap,
			int storableCount, String objectName)
	{
		// Uncommented due to addition of 'auto' functionality.  Bug 14263
		int counter = 0;
		if (!containerMap.isEmpty())
		{
			counter = StorageContainerUtil.checkForLocation(containerMap, storableCount, counter);
		}
		if (counter < storableCount)
		{
			ActionErrors errors = CollectionProtocolUtil.getActionErrors(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"errors.locations.notSufficient.shipmentReceive", objectName));
			saveErrors(request, errors);
		}

	}

	/**
	 * sets the storage container information.
	 * @param containerMap the treemap.
	 * @param bizLogic object of StorageContainerBizLogic class.
	 * @param shipment in which specimen storage info is to be set.
	 * @param request the request to be processed.
	 * @param specimenLocMap form containing all values.
	 */
	private void setSpecimenStorageInformation(Map containerMap, HttpServletRequest request,
			Map specimenLocMap)
	{
		List<NameValueBean> storagePositionList = AppUtility.getStoragePositionTypeList();
		request.setAttribute("storageList", storagePositionList);
		final String exceedingMaxLimit = "";
		final List initialValues = null;
		this.populateSpecimenStorageLocations(specimenLocMap, containerMap);
		request.setAttribute("initValues", initialValues);
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.EXCEEDS_MAX_LIMIT,
				exceedingMaxLimit);
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.AVAILABLE_CONTAINER_MAP,
				containerMap);

	}

	/**
	 * This function populates the availability map with available storage locations.
	 * @param specimenLocMap object of AliquotForm.
	 * @param containerMap Map containing data for container
	 */
	private void populateSpecimenStorageLocations(Map specimenLocMap, Map containerMap)
	{
		final Map map = new TreeMap();
		final String keyName = "Specimen:";
		int number = 0;
    	this.populateStorageLocations(containerMap, map, keyName, number);
		specimenLocMap.putAll(map);
	}
    /**
     * This method creates response data.
     * @param containerMap - containerMap
     * @param response - HttpServletResponse
     * @param specimenId - specimen id
     * @param specimenLocMap - specimenLocMap
     * @param rowNumber - rowNumber
     * @throws IOException - IOException
     */
	private void createResponseData(Map containerMap, HttpServletResponse response,
			String specimenId, Map specimenLocMap, String rowNumber)
			throws IOException
	{
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		final String msg = getResponseDataString(containerMap, specimenId, specimenLocMap,rowNumber);
		response.getWriter().write(msg);
	}
    /**
     * This method returns html representation of nlevelcombo tag.
     * @param containerMap - containerMap
     * @param specimenId - specimenId
     * @param specimenLocMap - specimenLocMap
     * @param rowNumber - rowNumber
     * @return String - html representation of nlevelcombo tag
     * @throws IOException - IOException
     */
	private String getResponseDataString(Map containerMap, String specimenId,
			Map specimenLocMap, String rowNumber) throws IOException
	{
		InputStream stream = Utility.getCurrClassLoader().getResourceAsStream("Tag.properties");
		Properties props = new Properties();
		props.load(stream);
		String[] labelNames = new String[]{"ID", "Pos1", "Pos2"};
		String[] initValues = new String[]{"", "", ""};
		String[] attrNames = {"specimenDetails(containerId_" + specimenId + ")",
				"specimenDetails(pos1_" + specimenId + ")",
				"specimenDetails(pos2_" + specimenId + ")"};
		String[] tdStyleClassArray = {"formFieldSized15", "customFormField", "customFormField"};
		String noOfEmptyCombos = "3";
		if (specimenLocMap != null && !specimenLocMap.isEmpty())
		{
			if (specimenLocMap.get("Specimen:" + rowNumber + "_initialValues") != null)
			{
				initValues = (String[]) specimenLocMap.get("Specimen:" + rowNumber
						+ "_initialValues");
			}
		}
		NLevelCustomCombo nlevelCombo = new NLevelCustomCombo();
		nlevelCombo.setAttributeNames(attrNames);
		nlevelCombo.setLabelNames(labelNames);
		nlevelCombo.setInitialValues(initValues);
		nlevelCombo.setTdStyleClassArray(tdStyleClassArray);
		nlevelCombo.setStyleClass("black_new");
		nlevelCombo.setTdStyleClass("black_new");
		nlevelCombo.setNoOfEmptyCombos(noOfEmptyCombos);
		nlevelCombo.setFormLabelStyle("nComboGroup");
		nlevelCombo.setRowNumber(rowNumber);
		nlevelCombo.setComboProperties(containerMap, props);
		String htmlTag = nlevelCombo.getCombosHTMLStr();
		StringBuffer sb = new StringBuffer();
		String dataTable = ScriptGenerator.getJSEquivalentFor(containerMap, rowNumber);
		int start1 = dataTable.indexOf(">");
		int end1 = dataTable.indexOf("</script>");
		sb.append(dataTable.substring((start1 + 1), end1));
		sb.append("#");
		sb.append("<table summary=" + "testing" + "><tr><td>");
		sb.append(htmlTag);
		sb.append("</td></tr></table>");
		return sb.toString();
	}

}
