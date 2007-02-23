
package edu.wustl.catissuecore.action;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.actionForm.DistributionReportForm;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.vo.ArrayDistributionReportEntry;
import edu.wustl.common.beans.SessionDataBean;

/**
 * This is the action class for displaying the Distribution report
 * @author Rahul Ner
 *  
 */

public class ArrayDistributionReportAction extends BaseDistributionReportAction
{
	protected ActionForward executeAction(ActionMapping mapping,ActionForm form, HttpServletRequest request,
															HttpServletResponse response) throws Exception
	{
		ConfigureResultViewForm configForm = (ConfigureResultViewForm)form;
		
		//Retrieve the distribution ID which is set in CommonAddEdit Action 
		Long distributionId = (Long)request.getAttribute(Constants.DISTRIBUTION_ID);
		
		//retrieve from configuration form if it is null
		if(distributionId==null)
    		distributionId = configForm.getDistributionId();
		
		/*Retrieve from request attribute if it null. 
		 */ 
		if(distributionId==null)
		{
			distributionId = (Long) request.getAttribute(Constants.SYSTEM_IDENTIFIER);
		}

		
		/*Retrieve from request parameter if it null. This request parameter is set in Distribution page incase the Report button 
		 *is clicked from Distribution Edit page
		 */ 
		if(distributionId==null)
		{
			distributionId = new Long(request.getParameter(Constants.SYSTEM_IDENTIFIER));
		}
		
		//Set it in configuration form if it is not null 
		if(distributionId!=null)
    		configForm.setDistributionId(distributionId);
			
    	Distribution dist =  getDistribution(distributionId, getSessionData(request), Constants.CLASS_LEVEL_SECURE_RETRIEVE);
    	
    	//Retrieve the distributed items data
    	DistributionReportForm distributionReportForm = getDistributionReportForm(dist);
    	distributionReportForm.setDistributionType(new Integer(Constants.SPECIMEN_ARRAY_DISTRIBUTION_TYPE));
    	SessionDataBean sessionData = getSessionData(request);
    	String action = configForm.getNextAction();
    	
/*    	//Set the columns for Distribution report
		String action = configForm.getNextAction();
		String selectedColumns[] = getSelectedColumns(action,configForm,true);
		String []columnNames = getColumnNames(selectedColumns);
*/		
		
		String selectedColumns[] = getSelectedColumns(action,configForm,true);
		String []columnNames = getColumnNames(selectedColumns);
		
		String[] specimenColumns = Constants.SPECIMEN_IN_ARRAY_SELECTED_COLUMNS;
		String []specimenColumnNames = getColumnNames(specimenColumns);
		
    	List listOfData = getListOfArrayData(dist,configForm,sessionData,selectedColumns,specimenColumns) ;

    	
		//Set the request attributes for the Distribution report data
		request.setAttribute(Constants.DISTRIBUTION_REPORT_FORM, distributionReportForm);
    	request.setAttribute(Constants.COLUMN_NAMES_LIST, columnNames);
    	request.setAttribute(Constants.DISTRIBUTED_ITEMS_DATA, listOfData);
    	request.setAttribute(Constants.SPECIMEN_COLUMN_NAMES_LIST, specimenColumnNames);

    	setSelectedMenuRequestAttribute(request);
		return (mapping.findForward("Success"));
	}

	protected List getListOfArrayData(Distribution dist, ConfigureResultViewForm configForm, SessionDataBean sessionData) {
    	//Get the list of data for Distributed items data for the report.
    	List listOfData = new ArrayList();
    	Collection specimenArrayCollection = dist.getSpecimenArrayCollection();		
    	//Specimen Ids which are getting distributed.
    	String []specimenArrayIds = new String[specimenArrayCollection.size()];
    	int i=0;
    	Iterator itr = specimenArrayCollection.iterator();
    	
    	while(itr.hasNext())
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
	 * @param array
	 * @param specimenColumns
	 * @param sessionData
	 * @param arrayEntry
	 * @param fillerList
	 */
	private void getSpecimenDetails(SpecimenArray array, String[] specimenColumns, SessionDataBean sessionData,ArrayDistributionReportEntry arrayEntry) {
		List specimensDetails = new ArrayList();
		List gridInfo = new ArrayList();
		int dimensionOne = array.getCapacity().getOneDimensionCapacity().intValue();
		int dimensionTwo = array.getCapacity().getOneDimensionCapacity().intValue();
		
		for(int i=0;i<dimensionOne;i++) {
			List temp = new ArrayList(dimensionTwo);
			
			for(int j=0;j<dimensionTwo;j++) {
				temp.add(Constants.UNUSED);
			}
			
			gridInfo.add(i,temp);
		}
		
		Iterator itr = array.getSpecimenArrayContentCollection().iterator();
		
		while(itr.hasNext()) {
			SpecimenArrayContent arrayContent = (SpecimenArrayContent) itr.next();
			Specimen specimen = arrayContent.getSpecimen();
			List specimenDetails = new ArrayList();
			

			if(arrayContent.getPositionDimensionOne() != null && arrayContent.getPositionDimensionTwo() != null) {
				int postionOneInArray =  arrayContent.getPositionDimensionOne().intValue();
				int postionTwoInArray =  arrayContent.getPositionDimensionTwo().intValue();
				if (postionOneInArray > 0)
				{
					((List) gridInfo.get(postionOneInArray - 1)).add(postionTwoInArray - 1,specimen.getLabel());
				}
			}
			
			specimenDetails.add(specimen.getLabel());
			specimenDetails.add(Utility.toString(specimen.getBarcode()));
			specimenDetails.add(Utility.toString(arrayContent.getPositionDimensionOne()));
			specimenDetails.add(Utility.toString(arrayContent.getPositionDimensionTwo()));
			specimenDetails.add(specimen.getClassName());
			specimenDetails.add(specimen.getType());
			specimenDetails.add(specimen.getSpecimenCharacteristics().getTissueSide());
			specimenDetails.add(specimen.getSpecimenCharacteristics().getTissueSite());
			
			specimensDetails.add(specimenDetails);
		}
		arrayEntry.setGridInfo(gridInfo);
		arrayEntry.setSpecimenEntries(specimensDetails);
	}

	/**
	 * @param array
	 * @param selectedColumns
	 * @param sessionData
	 * @return
	 * @throws Exception
	 */
	private List getArrayDetails(SpecimenArray array, String[] selectedColumns,SessionDataBean sessionData)throws Exception {
		List arrayDetails = new ArrayList();		
		arrayDetails.add(array.getName());
		arrayDetails.add(Utility.toString(array.getBarcode()));
		arrayDetails.add(Utility.toString(array.getSpecimenArrayType().getName()));
		arrayDetails.add(Utility.toString(array.getPositionDimensionOne()));
		arrayDetails.add(Utility.toString(array.getPositionDimensionTwo()));
		arrayDetails.add(Utility.toString(array.getCapacity().getOneDimensionCapacity()));
		arrayDetails.add(Utility.toString(array.getCapacity().getTwoDimensionCapacity()));
		arrayDetails.add(Utility.toString(array.getSpecimenArrayType().getSpecimenClass()));
		arrayDetails.add(Utility.toString(array.getSpecimenArrayType().getSpecimenTypeCollection()));
		arrayDetails.add(Utility.toString(array.getComment()));
		return arrayDetails;
	}
		

	/**
	 * @param dist
	 * @param configForm
	 * @param sessionData
	 * @param arrayColumns
	 * @param specimenColumns
	 * @return
	 * @throws Exception
	 */
	protected List getListOfArrayDataForSave(Distribution dist,ConfigureResultViewForm configForm,SessionDataBean sessionData,String[] arrayColumns,String[] specimenColumns) throws Exception {
		List arrayEntries = new ArrayList();	
		
		//Iterator itr = dist.getSpecimenArrayCollection().iterator();
		Iterator itr = dist.getDistributedItemCollection().iterator();
		while(itr.hasNext())
		{
			//SpecimenArray array = (SpecimenArray) itr.next();
			DistributedItem distributedItem = (DistributedItem)itr.next();
			SpecimenArray array = distributedItem.getSpecimenArray();
			ArrayDistributionReportEntry arrayEntry = new ArrayDistributionReportEntry();
			arrayEntry.setArrayInfo(getArrayDetails(array,arrayColumns,sessionData));
			getSpecimenDetails(array,specimenColumns,sessionData,arrayEntry);
			
			arrayEntries.add(arrayEntry);
		}
		return arrayEntries;
    }
	
	/**
	 * @param dist
	 * @param configForm
	 * @param sessionData
	 * @param arrayColumns
	 * @param specimenColumns
	 * @return
	 * @throws Exception
	 */
	protected List getListOfArrayData(Distribution dist,ConfigureResultViewForm configForm,SessionDataBean sessionData,String[] arrayColumns,String[] specimenColumns) throws Exception {
		List arrayEntries = new ArrayList();	
		
		//Iterator itr = dist.getSpecimenArrayCollection().iterator();
		Iterator itr = dist.getDistributedItemCollection().iterator();
		while(itr.hasNext())
		{
			DistributedItem distributedItem = (DistributedItem) itr.next();	
			SpecimenArray array = distributedItem.getSpecimenArray();
			List arrayEntry = new ArrayList();
			arrayEntry.add(getArrayDetails(array,arrayColumns,sessionData));
			arrayEntries.add(arrayEntry);
		}
		return arrayEntries;
    }
}