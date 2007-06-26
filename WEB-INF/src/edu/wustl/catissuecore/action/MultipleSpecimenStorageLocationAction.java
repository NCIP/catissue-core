/*
 * This class populates the data for the storage location page of multiple specimens.
 * 
 * Created on Nov 6, 2006
 * @author mandar_deshmukh
 */
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.MultipleSpecimenStorageLocationForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


/*
 * This class populates the data for the storage location page of multiple specimens.
 * 
 * Created on Nov 6, 2006
 * @author mandar_deshmukh
 */
public class MultipleSpecimenStorageLocationAction extends BaseAction
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// TODO Auto-generated method stub		
		// -----------------------
		String target = "success";
		String pageOf = request.getParameter(Constants.PAGEOF);
		request.setAttribute(Constants.PAGEOF,pageOf);
		MultipleSpecimenStorageLocationForm aForm = (MultipleSpecimenStorageLocationForm)form;
		
		HashMap specimenMap = getSpecimenMap(request);
		request.setAttribute(Constants.MULTIPLE_SPECIMEN_STORAGE_LOCATION_SPECIMEN_MAP,specimenMap);
		aForm.setSpecimenMap( specimenMap);
		populateSpecimenOnUIMap(request,aForm);  

		/*if(!(Constants.PAGEOF_MULTIPLE_SPECIMEN_STORAGE_LOCATION.equals(request.getParameter(Constants.PAGEOF))))
		{
			MultipleSpecimenStorageLocationForm aForm = (MultipleSpecimenStorageLocationForm)form;
			
			HashMap specimenMap = getSpecimenMap(request);
			request.setAttribute(Constants.MULTIPLE_SPECIMEN_STORAGE_LOCATION_SPECIMEN_MAP,specimenMap);
			aForm.setSpecimenMap( specimenMap);
			aForm.populateSpecimenOnUIMap(request);  
			
		}
		else
		{
			//TODO
			
		} */
		// -----------------------
		return mapping.findForward(target ) ;
	}
	/**
	 * @param request
	 * @param stLocationForm
	 * @throws DAOException
	 */
	private void populateSpecimenOnUIMap(HttpServletRequest request,MultipleSpecimenStorageLocationForm stLocationForm) throws DAOException
	{
		Map specimenMap = stLocationForm.getSpecimenMap();
		Map specimenOnUIMap = stLocationForm.getSpecimenOnUIMap();
		List specimenList = stLocationForm.getSpecimenList();
		
		if (specimenMap != null)
		{
			Iterator specimenKeys = specimenMap.keySet().iterator();
			int cnt = 1;
			while (specimenKeys.hasNext())
			{
				Specimen specimen = (Specimen) specimenKeys.next();
				//hold order of specimen
				specimenList.add(specimen);
				String labelKey = "Specimen:" + cnt + "_Label";
				String typeKey = "Specimen:" + cnt + "_Type";
				String barKey = "Specimen:" + cnt + "_Barcode";
				String storageContainerKey = "Specimen:" + cnt + "_StorageContainer";
				String positionOneKey = "Specimen:" + cnt + "_PositionOne";
				String positionTwoKey = "Specimen:" + cnt + "_PositionTwo";

				//key for location map
				String classKey = "Specimen:" + cnt + "_ClassName";
				String collectionProtocolKey = "Specimen:" + cnt + "_CollectionProtocol";
				specimenOnUIMap.put(classKey, specimen.getClassName());

//				Ashish ---  6th June 07 --- retriving cp Id from specimen for performance improvement	
				IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
				Long collectionProtocolId = (Long)bizLogic.retrieveAttribute(SpecimenCollectionGroup.class.getName(), specimen.getSpecimenCollectionGroup().getId(), Constants.COLUMN_NAME_CPR_CP_ID); 
				//	getCollectionProtocolId(specimen.getSpecimenCollectionGroup().getId(),bizLogic);	
				specimenOnUIMap.put(collectionProtocolKey, collectionProtocolId.toString());

//				specimenOnUIMap.put(collectionProtocolKey, specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
//						.getCollectionProtocol().getId().toString());

				specimenOnUIMap.put(labelKey, specimen.getLabel());
				specimenOnUIMap.put(typeKey, specimen.getType());
				specimenOnUIMap.put(barKey, specimen.getBarcode());
				if (specimen.getStorageContainer() != null)
					specimenOnUIMap.put(storageContainerKey, specimen.getStorageContainer().getId().toString());
				if (specimen.getPositionDimensionOne() != null)
					specimenOnUIMap.put(positionOneKey, specimen.getPositionDimensionOne().toString());
				if (specimen.getPositionDimensionTwo() != null)
					specimenOnUIMap.put(positionTwoKey, specimen.getPositionDimensionTwo().toString());
				addDerivedSpecimens(stLocationForm,specimen, cnt);
				cnt++;
			}
			String specimenCountKey = "Specimen_Count";
			specimenOnUIMap.put(specimenCountKey, new Integer(specimenMap.size()));
		}
		else
		{
			String specimenCountKey = "Specimen_Count";
			specimenOnUIMap.put(specimenCountKey, new Integer(0));
		}
		Logger.out.debug("specimenOnUIMap after population : \n------------\n" + specimenOnUIMap + "\n-------\n");
		request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_SPECIMEN_ORDER_LIST, specimenList);
		Logger.out.debug("\n---------------\n List set in session.\n---------------\n");
	}
	
	/**
	 * @param specimenId
	 * @param bizLogic
	 * @return
	 * @throws DAOException
	 * Retriving collection protocol id from specimen.
	 */
//		private Long getCollectionProtocolId(Long scgId, IBizLogic bizLogic) throws DAOException
//		{
//			String[] selectColumnName = {Constants.COLUMN_NAME_CPR_CP_ID};
//			String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER};
//			String[] whereColumnCondition = {Constants.EQUALS};
//			Object[] whereColumnValue = {scgId};
//			String sourceObjectName = SpecimenCollectionGroup.class.getName();
//			
//			List collectionProtCollection = bizLogic.retrieve(sourceObjectName,selectColumnName,
//		            whereColumnName, whereColumnCondition,
//		            whereColumnValue,Constants.AND_JOIN_CONDITION );
//			Long collectionProtocolId = (Long)collectionProtCollection.get(0);
//			return collectionProtocolId;
//		}
	/**
	 * @param stLocationForm
	 * @param mainSpecimen
	 * @param specimenID
	 * @throws DAOException
	 */
	private void addDerivedSpecimens(MultipleSpecimenStorageLocationForm stLocationForm,Specimen mainSpecimen, int specimenID)throws DAOException
	{
		Map specimenMap = stLocationForm.getSpecimenMap();
		Map specimenOnUIMap = stLocationForm.getSpecimenOnUIMap();
		
		List derivedList = (List) specimenMap.get(mainSpecimen);
		String parentKey = "Specimen:" + specimenID + "_";
		if (derivedList != null && !derivedList.isEmpty())
		{
			for (int cnt = 1; cnt <= derivedList.size(); cnt++)
			{
				String derivedPrefix = parentKey + "DerivedSpecimen:";
				Specimen specimen = (Specimen) derivedList.get(cnt - 1);
				String labelKey = derivedPrefix + cnt + "_Label";
				String typeKey = derivedPrefix + cnt + "_Type";
				String barKey = derivedPrefix + cnt + "_Barcode";
				String storageContainerKey = derivedPrefix + cnt + "_StorageContainer";
				String positionOneKey = derivedPrefix + cnt + "_PositionOne";
				String positionTwoKey = derivedPrefix + cnt + "_PositionTwo";
				//key for location map
				String classKey = derivedPrefix + cnt + "_ClassName";
				String collectionProtocolKey = derivedPrefix + cnt + "_CollectionProtocol";
				specimenOnUIMap.put(classKey, specimen.getClassName());
				
//				Ashish ---  6th June 07 --- retriving cp Id from derived specimen for performance improvement
				IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
				Long collectionProtocolId = (Long)bizLogic.retrieveAttribute(SpecimenCollectionGroup.class.getName(), specimen.getSpecimenCollectionGroup().getId(), Constants.COLUMN_NAME_CPR_CP_ID); 
					//getCollectionProtocolId(specimen.getSpecimenCollectionGroup().getId(),bizLogic);	
				specimenOnUIMap.put(collectionProtocolKey, collectionProtocolId.toString());
				
//				specimenOnUIMap.put(collectionProtocolKey, specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
//						.getCollectionProtocol().getId().toString());

				specimenOnUIMap.put(labelKey, specimen.getLabel());
				specimenOnUIMap.put(typeKey, specimen.getType());
				specimenOnUIMap.put(barKey, specimen.getBarcode());
				if (specimen.getStorageContainer() != null)
					specimenOnUIMap.put(storageContainerKey, specimen.getStorageContainer().getId().toString());
				if (specimen.getPositionDimensionOne() != null)
					specimenOnUIMap.put(positionOneKey, specimen.getPositionDimensionOne().toString());
				if (specimen.getPositionDimensionTwo() != null)
					specimenOnUIMap.put(positionTwoKey, specimen.getPositionDimensionTwo().toString());
			}
			specimenOnUIMap.put(parentKey + "DeriveCount", new Integer(derivedList.size()));
		}
		else
		{
			specimenOnUIMap.put(parentKey + "DeriveCount", new Integer(0));
		}

	}
	/**
	 * @param deriveCount
	 * @return
	 */
	private List getDerivedSpecimenList(int deriveCount)
	{
		List derivedList = new ArrayList();
		for(int i = 1;i<=deriveCount;i++)
		{
			derivedList.add(getSpecimen(i,"Derived"));
		}
		return derivedList; 
	}
	
	private TissueSpecimen getSpecimen(int i,String prefix)
	{
		TissueSpecimen specimen = new TissueSpecimen();
		specimen.setId(new Long(i) );
		specimen.setLabel(prefix+"Specimen Label "+i); 
		specimen.setBarcode(prefix+"Barcode "+i );
		specimen.setType("Fresh Tissue" );
		
		StorageContainer sc = new StorageContainer();
		sc.setId(new Long(i));
		specimen.setStorageContainer(sc );
		specimen.setPositionDimensionOne(new Integer((i+i)) ); 
		specimen.setPositionDimensionTwo(new Integer((i+i)) ); 
		
		return specimen ;
	}

	private HashMap getSpecimenMap(HttpServletRequest request)
	{
		HashMap specimenMap = new HashMap();
		specimenMap = (HashMap)request.getSession().getAttribute(Constants.SPECIMEN_MAP_KEY);  
		return specimenMap; 
	}

// ------------------------------------------	
	//to test the flow creates dummy data
	private HashMap getSpecimenMap1()
	{
		HashMap specimenMap = new HashMap();
		for(int i=1;i<5;i++)
		{
			Specimen specimen = getSpecimen(i,"");
			int dc = i; 
			Logger.out.debug("\ndc: "+dc);
			List derivedList = getDerivedSpecimenList(dc );
			specimenMap.put(specimen,derivedList);
			
		}
		return specimenMap; 
	}
}
