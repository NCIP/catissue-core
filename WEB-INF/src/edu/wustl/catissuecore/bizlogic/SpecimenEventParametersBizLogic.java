/*
 * Created on Jul 29, 2005
 *<p>SpecimenEventParametersBizLogic Class</p>
 * This class contains the Biz Logic for all EventParameters Classes.
 * This will be the class which will be used for datatransactions of the EventParameters. 
 */
package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh</p>
 * This class contains the Business Logic for all EventParameters Classes.
 * This will be the class which will be used for data transactions of the EventParameters. 
 */
public class SpecimenEventParametersBizLogic extends DefaultBizLogic
{
	/**
     * Saves the FrozenEventParameters object in the database.
	 * @param obj The FrozenEventParameters object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException 
	{
//	    Set protectionObjects = new HashSet();
		SpecimenEventParameters specimenEventParametersObject = (SpecimenEventParameters)obj;

		List list = dao.retrieve(User.class.getName(), Constants.SYSTEM_IDENTIFIER, specimenEventParametersObject.getUser().getSystemIdentifier());
		if (!list.isEmpty())
		{
		    User user = (User) list.get(0);

		    // check for closed User
			checkStatus(dao, user, "User" );

		    specimenEventParametersObject.setUser(user);
		}
		Specimen specimen = (Specimen) dao.retrieve(Specimen.class.getName(), specimenEventParametersObject.getSpecimen().getSystemIdentifier());
		
		// check for closed Specimen
		checkStatus(dao, specimen, "Specimen" );

		
		if (specimen != null)
		{
		    specimenEventParametersObject.setSpecimen(specimen);
		    if (specimenEventParametersObject instanceof TransferEventParameters)
		    {
		        TransferEventParameters transferEventParameters = (TransferEventParameters)specimenEventParametersObject;
		        
			    specimen.setPositionDimensionOne(transferEventParameters.getToPositionDimensionOne());
			    specimen.setPositionDimensionTwo(transferEventParameters.getToPositionDimensionTwo());
			    
			    StorageContainer storageContainer = (StorageContainer)dao.retrieve(StorageContainer.class.getName(), transferEventParameters.getToStorageContainer().getSystemIdentifier());
			    
				// check for closed StorageContainer
				checkStatus(dao, storageContainer, "Storage Container" );

			    if (storageContainer != null)
			    {
			        specimen.setStorageContainer(storageContainer);
			    }
			    dao.update(specimen, sessionDataBean, true, true, false);
		    }
		}
		
		dao.insert(specimenEventParametersObject,sessionDataBean, true, true);
//		protectionObjects.add(specimenEventParametersObject);
		
//		Inserting data for Authorization
//		try
//        {
//            SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,protectionObjects,getDynamicGroups(specimenEventParametersObject));
//        }
//        catch (SMException e)
//        {
//            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
//        }
	}
	
	 public String[] getDynamicGroups(AbstractDomainObject obj)
	 {
        String[] dynamicGroups=null;
        SpecimenEventParameters specimenEventParametersObject = (SpecimenEventParameters)obj;
        dynamicGroups = new String[1];
        
        try
        {
            dynamicGroups[0] = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(specimenEventParametersObject.getSpecimen(),Constants.getCollectionProtocolPGName(null));
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
        Logger.out.debug("Dynamic Group name: "+dynamicGroups[0]);
        return dynamicGroups;
        
	 }
	 
	 // ---------------------------------------------------
	 
		/**
		 * Updates the persistent object in the database.
		 * @param obj The object to be updated.
		 * @param session The session in which the object is saved.
		 * @throws DAOException 
		 */
		protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
		{
			SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) obj;
			SpecimenEventParameters oldSpecimenEventParameters = (SpecimenEventParameters) oldObj;
			
			// ----- Check for Closed Specimen
			//checkStatus(dao, specimenEventParameters.getSpecimen(), "Specimen" );

			// Check for different User
			if(!specimenEventParameters.getUser().getSystemIdentifier().equals( oldSpecimenEventParameters.getUser().getSystemIdentifier()))
			{
				checkStatus(dao,specimenEventParameters.getUser(), "User" );
			}

			// check for transfer event
//			if (specimenEventParameters.getSpecimen() != null)
//			{
//			    if (specimenEventParameters instanceof TransferEventParameters)
//			    {
//			        TransferEventParameters transferEventParameters = (TransferEventParameters)specimenEventParameters;
//			        TransferEventParameters oldTransferEventParameters = (TransferEventParameters)oldSpecimenEventParameters;
//				    
//				    StorageContainer storageContainer = transferEventParameters.getToStorageContainer();
//				    StorageContainer oldstorageContainer = oldTransferEventParameters.getToStorageContainer();
//				    Logger.out.debug("StorageContainer match : " + storageContainer.equals(oldstorageContainer ) );
//				    
//				    // check for closed StorageContainer
//				    if(!storageContainer.getSystemIdentifier().equals(oldstorageContainer.getSystemIdentifier()) )
//				    	checkStatus(dao, storageContainer, "Storage Container" );
//			    }
//			}
			
			//Update registration
			dao.update(specimenEventParameters, sessionDataBean, true, true, false);
		}
}
