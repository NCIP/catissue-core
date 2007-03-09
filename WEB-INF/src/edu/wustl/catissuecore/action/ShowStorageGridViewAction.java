/**
 * <p>Title: ShowStorageGridViewAction Class>
 * <p>Description:	ShowStorageGridViewAction shows the grid view of the map 
 * according to the storage container selected from the tree view.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.storage.StorageContainerGridObject;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.logger.Logger;
/**
 * ShowStorageGridViewAction shows the grid view of the map according to the
 * storage container selected from the tree view.
 * 
 * @author gautam_shetty
 */
public class ShowStorageGridViewAction  extends BaseAction
{

    /**
	 * Overrides the execute method of Action class.
	 */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        String id = request.getParameter(Constants.SYSTEM_IDENTIFIER);
        
        // Aarti: Check whether user has use permission on the storage container
		// or not
        if(!SecurityManager.getInstance(this.getClass()).isAuthorized(getUserLoginName(request)
        		,StorageContainer.class.getName()+"_"+id,Permissions.USE))
		{
        	ActionErrors errors = new ActionErrors();
         	ActionError error = new ActionError("access.use.object.denied");
         	errors.add(ActionErrors.GLOBAL_ERROR,error);
         	saveErrors(request,errors);
        	return mapping.findForward(Constants.FAILURE);
		}
        
        String pageOf = request.getParameter(Constants.PAGEOF);
        

        //Sri: Added to get the position of the storage container map
        String position = request.getParameter(Constants.STORAGE_CONTAINER_POSITION);
        if((null != position) && ("" != position))
        {
        	Long positionOne;
        	Long positionTwo;
        	try
			{
	        	//The two positions are separated by :
	        	StringTokenizer strToken = new StringTokenizer(position,":");
	        	positionOne = Long.valueOf(strToken.nextToken());
	        	positionTwo = Long.valueOf(strToken.nextToken());
			}
        	catch (Exception ex)
			{
			    //Will not select anything
        		positionOne = null;
        		positionTwo = null;
			}
        	request.setAttribute(Constants.POS_ONE,positionOne);
        	request.setAttribute(Constants.POS_TWO,positionTwo);

        }
        
        StorageContainerBizLogic bizLogic = (StorageContainerBizLogic)BizLogicFactory
        .getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
        
        List list = bizLogic.retrieve(StorageContainer.class.getName(),
                "id", id);
        StorageContainerGridObject storageContainerGridObject = null;
        int [][]fullStatus = null;
        int [][] childContainerIds = null;
        String [][] childContainerType = null;
        
        if ((list != null) && (list.size() > 0))
        {
        	storageContainerGridObject = new StorageContainerGridObject();
            StorageContainer storageContainer = (StorageContainer) list.get(0);

            setEnablePageAttributeIfRequired(request, storageContainer);
            
            request.setAttribute("siteName",storageContainer.getSite().getName());
            request.setAttribute("storageTypeName",storageContainer.getStorageType().getName());
            
            
            //Mandar : Labels for Dimensions  
            String oneDimLabel = storageContainer.getStorageType().getOneDimensionLabel();
            String twoDimLabel = storageContainer.getStorageType().getTwoDimensionLabel();
            
            if(oneDimLabel == null )oneDimLabel = " ";
            if(twoDimLabel == null )twoDimLabel = " ";
            
            request.setAttribute(Constants.STORAGE_CONTAINER_DIM_ONE_LABEL ,oneDimLabel );
            request.setAttribute(Constants.STORAGE_CONTAINER_DIM_TWO_LABEL ,twoDimLabel );
            
            storageContainerGridObject.setId(storageContainer.getId().longValue());
            storageContainerGridObject.setType(storageContainer.getStorageType().getName());
            storageContainerGridObject.setName(storageContainer.getName());
            
            Integer oneDimensionCapacity = storageContainer
            				.getCapacity().getOneDimensionCapacity();
            Integer twoDimensionCapacity = storageContainer
 							.getCapacity().getTwoDimensionCapacity();
            
            childContainerIds = new int[oneDimensionCapacity.intValue()+1][twoDimensionCapacity.intValue()+1];
            storageContainerGridObject.setOneDimensionCapacity(oneDimensionCapacity);
            storageContainerGridObject.setTwoDimensionCapacity(storageContainer
                    		.getCapacity().getTwoDimensionCapacity());
            
            fullStatus = new int[oneDimensionCapacity.intValue()+1][twoDimensionCapacity.intValue()+1];
            childContainerType = new String[oneDimensionCapacity.intValue()+1][twoDimensionCapacity.intValue()+1];
            
            //Showing Containers in the Container map.
            if (storageContainer.getChildren() != null)
            {
                Iterator iterator = storageContainer.getChildren().iterator();
                while(iterator.hasNext())
                {
                    StorageContainer childStorageContainer = (StorageContainer)iterator.next();
                    Integer positionDimensionOne = childStorageContainer.getPositionDimensionOne();
                    Integer positionDimensionTwo = childStorageContainer.getPositionDimensionTwo();
                    fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = 1;
                    childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo.intValue()]
                                                   = childStorageContainer.getId().intValue();
                    childContainerType[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] 
                                                   = Constants.CONTAINER_LABEL_CONTAINER_MAP + childStorageContainer.getName();
                                                  
                }
            }          
            
            IBizLogic specimenBizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
            
            //Showing Specimens in the Container map.
            String sourceObjectName = Specimen.class.getName();
			String[] selectColumnName = {"id","positionDimensionOne", "positionDimensionTwo","label"};
			String[] whereColumnName = {"storageContainer.id"};
            String[] whereColumnCondition = {"="};
			Object[] whereColumnValue = {id};
            String joinCondition = Constants.AND_JOIN_CONDITION;
			
            list = specimenBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName,
                    whereColumnCondition, whereColumnValue, joinCondition);
            
			
            if (list != null)
            {
                Iterator iterator = list.iterator();
                while(iterator.hasNext())
                {
                    //Specimen specimen = (Specimen)iterator.next();
                	Object obj[] =  (Object[])iterator.next();
                	
                	Long specimenID = (Long)obj[0]; 
                    Integer positionDimensionOne = (Integer)obj[1];
                    Integer positionDimensionTwo = (Integer)obj[2];
                    String specimenLable = (String)obj[3];
                    
                    fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = 2;
                    childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo.intValue()]
                                                   = specimenID.intValue();
                    childContainerType[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] 
                                                                        = Constants.SPECIMEN_LABEL_CONTAINER_MAP + specimenLable;
                }
            }
            
            // Showing Specimen Arrays in the Container map.
            sourceObjectName = SpecimenArray.class.getName();
            selectColumnName[3] = "name";
            whereColumnName[0] = "storageContainer.id";
            
            list = specimenBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName,
                    whereColumnCondition, whereColumnValue, joinCondition);
			
            if (list != null)
            {
                Iterator iterator = list.iterator();
                while(iterator.hasNext())
                {
                	Object obj[] =  (Object[])iterator.next();
                	
                	Long specimenID = (Long)obj[0]; 
                    Integer positionDimensionOne = (Integer)obj[1];
                    Integer positionDimensionTwo = (Integer)obj[2];
                    String specimenArrayLable = obj[3].toString();
                    
                    fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = 2;
                    childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo.intValue()]
                                                   = specimenID.intValue();
                    childContainerType[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] 
                                                                        = Constants.SPECIMEN_ARRAY_LABEL_CONTAINER_MAP + specimenArrayLable;
                }
            }
        }
        
        if (pageOf.equals(Constants.PAGEOF_STORAGE_LOCATION))
        {
        	String storageContainerType = request.getParameter(Constants.STORAGE_CONTAINER_TYPE);
        	Logger.out.info("Id-----------------"+id);
        	Logger.out.info("storageContainerType:"+storageContainerType);
            int startNumber = bizLogic.getNextContainerNumber(Long.parseLong(id),
                    Long.parseLong(storageContainerType),false);
            request.setAttribute(Constants.STORAGE_CONTAINER_TYPE,storageContainerType);
            request.setAttribute(Constants.START_NUMBER,new Integer(startNumber));
        }
         
        request.setAttribute(Constants.PAGEOF, pageOf);
        request.setAttribute(Constants.CHILD_CONTAINER_SYSTEM_IDENTIFIERS, childContainerIds);
        request.setAttribute(Constants.CHILD_CONTAINER_TYPE, childContainerType);
        request.setAttribute(Constants.STORAGE_CONTAINER_CHILDREN_STATUS,fullStatus);
        request.setAttribute(Constants.STORAGE_CONTAINER_GRID_OBJECT,
                storageContainerGridObject);
        
        //Mandar : 29aug06 : to set collectionprotocol titles
        List collectionProtocolList = bizLogic.getCollectionProtocolList(id);
        request.setAttribute(Constants.MAP_COLLECTION_PROTOCOL_LIST, collectionProtocolList);

        //Mandar : 29aug06 : to set specimenclass 
        List specimenClassList = bizLogic.getSpecimenClassList(id);
        request.setAttribute(Constants.MAP_SPECIMEN_CLASS_LIST, specimenClassList);

        return mapping.findForward(Constants.SUCCESS);
    }

    /**
     * To enable or disable the Storage coctinaer links on the page depending on restriction criteria on Container.
	 * @param request Teh HttpServletRequest object reference.
	 * @param storageContainer The Storage container object reference.
	 */
	private void setEnablePageAttributeIfRequired(HttpServletRequest request, StorageContainer storageContainer)
	{
		boolean enablePage=true;
		String activityStatus = request.getParameter(Constants.ACTIVITY_STATUS);
		if (activityStatus!=null && activityStatus.equals(Constants.ACTIVITY_STATUS_CLOSED))
		{
			enablePage=false;
			ActionErrors errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.container.closed"));
			saveErrors(request, errors);
		}
		
		HttpSession session = request.getSession();
		// checking for container type.
		String holdContainerType = (String)session.getAttribute(Constants.CAN_HOLD_CONTAINER_TYPE);
		if (enablePage && holdContainerType!=null && !holdContainerType.equals(""))
		{
			int typeId = Integer.parseInt(holdContainerType);
			enablePage = canHoldContainerType(typeId, storageContainer);
		}
		
		//checking for collection Protocol. 
		String holdCollectionProtocol = (String)session.getAttribute(Constants.CAN_HOLD_COLLECTION_PROTOCOL);
		if (enablePage && holdCollectionProtocol!=null)
		{
			if (!holdCollectionProtocol.equals(""))
			{
				int collectionProtocolId = Integer.parseInt(holdCollectionProtocol);
				enablePage = canHoldCollectionProtocol(collectionProtocolId,storageContainer);
			}
			else
				enablePage=false;
		}
		
		//checking for sepecimen class.
		String holdspecimenClass = (String)session.getAttribute(Constants.CAN_HOLD_SPECIMEN_CLASS);
		if (enablePage && holdspecimenClass!=null)
		{
			if (!holdspecimenClass.equals(""))
				enablePage = canHoldSpecimenClass(holdspecimenClass,storageContainer);
			else
				enablePage = false;
		}
		
		//checking for specimen array type.
		String holdspecimenArrayType = (String)session.getAttribute(Constants.CAN_HOLD_SPECIMEN_ARRAY_TYPE);
		if (enablePage && holdspecimenArrayType!=null)
		{
			if (!holdspecimenArrayType.equals(""))
			{
				int specimenArrayTypeId = Integer.parseInt(holdspecimenArrayType);
				enablePage = canHoldSpecimenArrayType(specimenArrayTypeId,storageContainer);
			}
			else
				enablePage = false;
		}
		
		if (enablePage)
			request.setAttribute(Constants.ENABLE_STORAGE_CONTAINER_GRID_PAGE,Constants.TRUE);
	}

	/**
     * To check wether the Continer to display can holds the given type of container. 
	 * @param typeId ContinerType id of container
	 * @param storageContainer The StorageContainer reference to be displayed on the page.
	 * @return true if the given continer can hold the typet.
	 */
	private boolean canHoldContainerType(int typeId, StorageContainer storageContainer)
	{
		boolean canHold = false;
		Collection containerTypeCollection  = storageContainer.getHoldsStorageTypeCollection();
		if (!containerTypeCollection.isEmpty())
		{
			Iterator itr = containerTypeCollection.iterator();
			while (itr.hasNext())
			{
				StorageType type = (StorageType)itr.next();
				long storagetypeId = type.getId().longValue();
				if (storagetypeId == Constants.ALL_STORAGE_TYPE_ID ||  storagetypeId==typeId)
				{
					return true;
				}
			}
		}
		return canHold;
	}

    /**
     * To check wether the Continer to display can holds the given CollectionProtocol. 
	 * @param collectionProtocolId The collectionProtocol Id.
	 * @param storageContainer The StorageContainer reference to be displayed on the page.
	 * @return true if the given continer can hold the CollectionProtocol.
	 */
	private boolean canHoldCollectionProtocol(int collectionProtocolId, StorageContainer storageContainer)
	{
		boolean canHold = true;
		Collection collectionProtocols = storageContainer.getCollectionProtocolCollection();
		if (!collectionProtocols.isEmpty())
		{
			Iterator itr = collectionProtocols.iterator();
			canHold=false;
			while (itr.hasNext())
			{
				CollectionProtocol cp = (CollectionProtocol)itr.next();
				if (cp.getId().longValue()==collectionProtocolId)
				{
					return true;
				}
			}
		}
		return canHold;
	}
	/**
     * To check wether the Continer to display can holds the given specimenClass. 
	 * @param specimenClass The specimenClass Name.
	 * @param storageContainer The StorageContainer reference to be displayed on the page.
	 * @return true if the given continer can hold the specimenClass.
	 */
	private boolean canHoldSpecimenClass(String specimenClass,StorageContainer storageContainer)
	{
		Collection specimenClasses = storageContainer.getHoldsSpecimenClassCollection();
		Iterator itr = specimenClasses.iterator();
		while (itr.hasNext())
		{
			String className = (String)itr.next();
			if (className.equals(specimenClass))
				return true;
			
		}
		return false;
	}
	
	
	 /**
     * To check wether the Continer to display can holds the given specimenArrayTypeId. 
	 * @param specimenArrayTypeId The Specimen Array Type Id.
	 * @param storageContainer The StorageContainer reference to be displayed on the page.
	 * @return true if the given continer can hold the specimenArrayType.
	 */
	private boolean canHoldSpecimenArrayType(int specimenArrayTypeId, StorageContainer storageContainer)
	{
		boolean canHold = true;
		Collection specimenArrayTypes = storageContainer.getHoldsSpecimenArrayTypeCollection();
//		if (!specimenArrayTypes.isEmpty())
		{
			Iterator itr = specimenArrayTypes.iterator();
			canHold=false;
			while (itr.hasNext())
			{
				SpecimenArrayType specimenarrayType = (SpecimenArrayType)itr.next();
				long arraytypeId = specimenarrayType.getId().longValue();
				
				if (arraytypeId == Constants.ALL_SPECIMEN_ARRAY_TYPE_ID || arraytypeId==specimenArrayTypeId)
				{
					return true;
				}
			}
		}
		return canHold;
	}
	/**
	 * @param fullStatus
	 * @param childContainerIds
	 * @param storageContainer
	 */
    private void setStorageContainerStatus(boolean[][] fullStatus, int[][] childContainerIds, Collection collection)
    {
        Iterator iterator = collection.iterator();
        while(iterator.hasNext())
        {
            StorageContainer childStorageContainer = (StorageContainer)iterator.next();
            Integer positionDimensionOne = childStorageContainer.getPositionDimensionOne();
            Integer positionDimensionTwo = childStorageContainer.getPositionDimensionTwo();
            fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = true;
            childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo.intValue()]
                                           = childStorageContainer.getId().intValue();
        }
    }

}