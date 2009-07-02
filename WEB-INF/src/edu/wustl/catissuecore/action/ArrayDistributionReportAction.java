
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.actionForm.DistributionReportForm;
import edu.wustl.catissuecore.bizlogic.DistributionBizLogic;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.vo.ArrayDistributionReportEntry;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.Utility;

/**
 * This is the action class for displaying the Distribution report.
 * @author Rahul Ner
 *
 */

public class ArrayDistributionReportAction extends BaseDistributionReportAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ConfigureResultViewForm configForm = (ConfigureResultViewForm) form;

		//Retrieve the distribution ID which is set in CommonAddEdit Action
		Long distributionId = (Long) request.getAttribute(Constants.DISTRIBUTION_ID);

		//retrieve from configuration form if it is null
		if (distributionId == null)
			distributionId = configForm.getDistributionId();

		//retireve the distribution id from forward to hasmap
		Map forwardToHashMap = (Map) request.getAttribute("forwardToHashMap");
		if (forwardToHashMap != null && forwardToHashMap.get("distributionId") != null)
		{
			distributionId = (Long) forwardToHashMap.get("distributionId");
		}
		/*Retrieve from request attribute if it null.
		 */
		if (distributionId == null)
		{
			distributionId = (Long) request.getAttribute(Constants.SYSTEM_IDENTIFIER);
		}

		/*Retrieve from request parameter if it null. This request parameter is set
		 *  in Distribution page incase the Report button
		 *is clicked from Distribution Edit page
		 */
		if (distributionId == null)
		{
			distributionId = new Long(request.getParameter(Constants.SYSTEM_IDENTIFIER));
		}

		//Set it in configuration form if it is not null
		if (distributionId != null)
			configForm.setDistributionId(distributionId);

		Distribution dist = getDistribution(distributionId, getSessionData(request),
				edu.wustl.security.global.Constants.CLASS_LEVEL_SECURE_RETRIEVE);

		//Retrieve the distributed items data
		DistributionReportForm distributionReportForm = getDistributionReportForm(dist);
		distributionReportForm.setDistributionType(new Integer(
				Constants.SPECIMEN_ARRAY_DISTRIBUTION_TYPE));
		//SessionDataBean sessionData = getSessionData(request);
		String action = configForm.getNextAction();

		/*    	//Set the columns for Distribution report
		 String action = configForm.getNextAction();
		 String selectedColumns[] = getSelectedColumns(action,configForm,true);
		 String []columnNames = getColumnNames(selectedColumns);
		 */

		String selectedColumns[] = getSelectedColumns(action, configForm, true);
		String[] columnNames = getColumnNames(selectedColumns);

		String[] specimenColumns = Constants.SPECIMEN_IN_ARRAY_SELECTED_COLUMNS;
		String[] specimenColumnNames = getColumnNames(specimenColumns);

		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		DistributionBizLogic bizLogic = (DistributionBizLogic) factory
				.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
		List listOfData = bizLogic.getListOfArray(dist);

		//Set the request attributes for the Distribution report data
		request.setAttribute(Constants.DISTRIBUTION_REPORT_FORM, distributionReportForm);
		request.setAttribute(Constants.COLUMN_NAMES_LIST, columnNames);
		request.setAttribute(Constants.DISTRIBUTED_ITEMS_DATA, listOfData);
		request.setAttribute(Constants.SPECIMEN_COLUMN_NAMES_LIST, specimenColumnNames);

		setSelectedMenuRequestAttribute(request);
		return (mapping.findForward("Success"));
	}

	/**
	 * @param dist : dist
	 * @param configForm : configForm
	 * @param sessionData : sessionData
	 * @return List : List
	 */
	protected List getListOfArrayData(Distribution dist, ConfigureResultViewForm configForm,
			SessionDataBean sessionData)
	{
		//Get the list of data for Distributed items data for the report.
		List listOfData = new ArrayList();
		Collection specimenArrayCollection = listOfData;
		//For Code model sync
		//dist.getSpecimenArrayCollection();
		//Specimen Ids which are getting distributed.
		//String[] specimenArrayIds = new String[specimenArrayCollection.size()];
		int i = 0;
		Iterator itr = specimenArrayCollection.iterator();

		while (itr.hasNext())
		{
			SpecimenArray array = (SpecimenArray) itr.next();
			List tempList = new ArrayList();
			tempList.add(array.getId().toString());
			tempList.add(Utility.toString(array.getBarcode()));
			i++;
			List tempList1 = new ArrayList();
			tempList1.add(tempList);
			listOfData.add(tempList1);
		}
		return listOfData;
	}

	/**
	 * @param array : array
	 * @param specimenColumns : specimenColumns
	 * @param sessionData : sessionData
	 * @param arrayEntry : arrayEntry
	 * @throws BizLogicException : BizLogicException
	 */
	private void getSpecimenDetails(SpecimenArray array, String[] specimenColumns,
			SessionDataBean sessionData, ArrayDistributionReportEntry arrayEntry)
			throws BizLogicException
	{
		List specimensDetails = new ArrayList();
		List gridInfo = new ArrayList();
		int dimensionOne = array.getCapacity().getOneDimensionCapacity().intValue();
		int dimensionTwo = array.getCapacity().getTwoDimensionCapacity().intValue();

		for (int i = 0; i < dimensionOne; i++)
		{
			List temp = new ArrayList(dimensionTwo);

			for (int j = 0; j < dimensionTwo; j++)
			{
				temp.add(Constants.UNUSED);
			}

			gridInfo.add(i, temp);
		}
		/**
		 * Name : Virender
		 * Reviewer: Prafull
		 * Retriving collection of Specimen Type.
		 * Replaced array.getSpecimenArrayContentCollection().iterator();
		 */
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		DefaultBizLogic bizLogic = (DefaultBizLogic) factory
				.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		Collection specimenArrayContentCollection = (Collection) bizLogic.retrieveAttribute(
				SpecimenArray.class.getName(), array.getId(),
				"elements(specimenArrayContentCollection)");
		Iterator itr = specimenArrayContentCollection.iterator();

		while (itr.hasNext())
		{
			SpecimenArrayContent arrayContent = (SpecimenArrayContent) itr.next();
			/**
			 * Name : Virender
			 * Reviewer: Prafull
			 * Retriving specimenObject
			 * replaced arrayContent.getSpecimen()
			 */
			Specimen specimen = null;
			specimen = (Specimen) bizLogic.retrieveAttribute(SpecimenArrayContent.class.getName(),
					arrayContent.getId(), "specimen");
			List specimenDetails = new ArrayList();

			if (arrayContent.getPositionDimensionOne() != null
					&& arrayContent.getPositionDimensionTwo() != null)
			{
				int postionOneInArray = arrayContent.getPositionDimensionOne().intValue();
				int postionTwoInArray = arrayContent.getPositionDimensionTwo().intValue();
				if (postionOneInArray > 0)
				{
					((List) gridInfo.get(postionOneInArray - 1)).set(postionTwoInArray - 1,
							specimen.getLabel());
				}
			}

			specimenDetails.add(specimen.getLabel());
			specimenDetails.add(Utility.toString(specimen.getBarcode()));
			specimenDetails.add(Utility.toString(arrayContent.getPositionDimensionOne()));
			specimenDetails.add(Utility.toString(arrayContent.getPositionDimensionTwo()));
			specimenDetails.add(specimen.getClassName());
			specimenDetails.add(specimen.getSpecimenType());
			specimenDetails.add(specimen.getSpecimenCharacteristics().getTissueSide());
			specimenDetails.add(specimen.getSpecimenCharacteristics().getTissueSite());

			specimensDetails.add(specimenDetails);
		}
		arrayEntry.setGridInfo(gridInfo);
		arrayEntry.setSpecimenEntries(specimensDetails);
	}

	/**
	 *
	 * @param array : array
	 * @param selectedColumns : selectedColumns
	 * @param sessionData : sessionData
	 * @return List : List
	 * @throws Exception : Exception
	 */
	private List getArrayDetails(SpecimenArray array, String[] selectedColumns,
			SessionDataBean sessionData) throws Exception
	{
		List arrayDetails = new ArrayList();
		arrayDetails.add(array.getName());
		arrayDetails.add(Utility.toString(array.getBarcode()));
		/**
		 * Name : Virender
		 * Reviewer: Prafull
		 * Retriving collection of Distributed Items.
		 * array.getSpecimenArrayType().getname()
		 * array.getSpecimenArrayType().getSpecimenClass()
		 * array.getSpecimenArrayType().getSpecimenTypeCollection()
		 */
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.SPECIMEN_ARRAY_TYPE_FORM_ID);
		SpecimenArrayType specimenArrayType = (SpecimenArrayType) bizLogic.retrieveAttribute(
				SpecimenArray.class.getName(), array.getId(), "specimenArrayType");
		arrayDetails.add(Utility.toString(specimenArrayType.getName()));
		if (array != null && array.getLocatedAtPosition() != null)
		{
			arrayDetails.add(Utility.toString(array.getLocatedAtPosition()
					.getPositionDimensionOne()));
			arrayDetails.add(Utility.toString(array.getLocatedAtPosition()
					.getPositionDimensionTwo()));
		}
		arrayDetails.add(Utility.toString(array.getCapacity().getOneDimensionCapacity()));
		arrayDetails.add(Utility.toString(array.getCapacity().getTwoDimensionCapacity()));
		arrayDetails.add(Utility.toString(specimenArrayType.getSpecimenClass()));
		/**
		 * Name : Virender
		 * Reviewer: Prafull
		 * Retriving collection of Specimen Type.
		 * specimenArrayType.getSpecimenTypeCollection();
		 */
		Collection specimenTypeCollection = (Collection) bizLogic.retrieveAttribute(
				SpecimenArrayType.class.getName(), specimenArrayType.getId(),
				"elements(specimenTypeCollection)");
		arrayDetails.add(Utility.toString(specimenTypeCollection));
		arrayDetails.add(Utility.toString(array.getComment()));
		return arrayDetails;
	}

	/**
	 * @param dist : dist
	 * @param configForm : configForm
	 * @param sessionData : sessionData
	 * @param arrayColumns : arrayColumns
	 * @param specimenColumns : specimenColumns
	 * @return List : List
	 * @throws Exception : Exception
	 */
	protected List getListOfArrayDataForSave(Distribution dist, ConfigureResultViewForm configForm,
			SessionDataBean sessionData, String[] arrayColumns, String[] specimenColumns)
			throws Exception
	{
		List arrayEntries = new ArrayList();
		Iterator itr = getSpecimenArrayCollection(dist).iterator();
		while (itr.hasNext())
		{
			SpecimenArray array = (SpecimenArray) itr.next();
			ArrayDistributionReportEntry arrayEntry = new ArrayDistributionReportEntry();
			arrayEntry.setArrayInfo(getArrayDetails(array, arrayColumns, sessionData));
			getSpecimenDetails(array, specimenColumns, sessionData, arrayEntry);

			arrayEntries.add(arrayEntry);
		}
		return arrayEntries;
	}

	/**
	 * @param dist : dist
	 * @param configForm : configForm
	 * @param sessionData : sessionData
	 * @param arrayColumns : arrayColumns
	 * @param specimenColumns : specimenColumns
	 * @return List : List
	 * @throws Exception : Exception
	 */
	protected List getListOfArrayData(Distribution dist, ConfigureResultViewForm configForm,
			SessionDataBean sessionData, String[] arrayColumns, String[] specimenColumns)
			throws Exception
	{
		List arrayEntries = new ArrayList();
		Iterator itr = getSpecimenArrayCollection(dist).iterator();
		while (itr.hasNext())
		{
			SpecimenArray array = (SpecimenArray) itr.next();
			List arrayEntry = new ArrayList();
			arrayEntry.add(getArrayDetails(array, arrayColumns, sessionData));

			arrayEntries.add(arrayEntry);
		}
		return arrayEntries;
	}

	/**
	 * Retrive SpecimenArrayCollection from parent Specimen.
	 * @param dist : dist
	 * @return Collection : Collection
	 * @throws BizLogicException : BizLogicException
	 */
	private Collection getSpecimenArrayCollection(Distribution dist) throws BizLogicException
	{
		Collection specimenArrayCollection = new HashSet();
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
		Collection distributedItemCollection = (Collection) bizLogic.retrieveAttribute(
				Distribution.class.getName(), dist.getId(), "elements(distributedItemCollection)");
		Iterator itr = distributedItemCollection.iterator();
		while (itr.hasNext())
		{
			DistributedItem distributedItem = (DistributedItem) itr.next();
			String[] selectColumnName = {"specimenArray"};
			String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER};
			Object[] whereColumnValue = {distributedItem.getId()};
			String[] whereColumnCond = {"="};
			List list = bizLogic.retrieve(DistributedItem.class.getName(), selectColumnName,
					whereColumnName, whereColumnCond, whereColumnValue,
					Constants.AND_JOIN_CONDITION);
			if (list != null && list.size() > 0)
			{
				Iterator listItr = list.iterator();
				while (listItr.hasNext())
				{
					SpecimenArray array = (SpecimenArray) listItr.next();
					if (array != null)
						specimenArrayCollection.add(array);
				}
			}
		}

		return specimenArrayCollection;
	}
}