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

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
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
         	ActionError error = new ActionError("access.use.object.denied"
         	        				);
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
            
            //Mandar : Labels for Dimensions  
            String oneDimLabel = storageContainer.getStorageType().getOneDimensionLabel();
            String twoDimLabel = storageContainer.getStorageType().getTwoDimensionLabel();
            
            if(oneDimLabel == null )oneDimLabel = " ";
            if(twoDimLabel == null )twoDimLabel = " ";
            
            request.setAttribute(Constants.STORAGE_CONTAINER_DIM_ONE_LABEL ,oneDimLabel );
            request.setAttribute(Constants.STORAGE_CONTAINER_DIM_TWO_LABEL ,twoDimLabel );
            
            storageContainerGridObject.setId(storageContainer.getId().longValue());
            storageContainerGridObject.setType(storageContainer.getStorageType().getName());
            
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
                                                   = childStorageContainer.getStorageType().getName();
                                                  
                }
            }          
            
            IBizLogic specimenBizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
            
            String sourceObjectName = Specimen.class.getName();
			String[] selectColumnName = {"id","positionDimensionOne", "positionDimensionTwo","type"};
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
                    String specimenType = (String)obj[3];
                    
                    fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = 2;
                    childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo.intValue()]
                                                   = specimenID.intValue();
                    childContainerType[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] 
                                                                        = specimenType;
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
        
        return mapping.findForward(Constants.SUCCESS);
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