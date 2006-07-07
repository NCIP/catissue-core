/**
 * <p>Title: StorageTypeHDAO Class>
 * <p>Description:	StorageTypeHDAO is used to add site type information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 21, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * StorageTypeHDAO is used to add site type information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class StorageTypeBizLogic extends DefaultBizLogic
{
	/**
     * Saves the storageType object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException  
	{
		StorageType type = (StorageType)obj;
		dao.insert(type.getDefaultStorageCapacity(),sessionDataBean, true, true);
		//setStorageTypeCollection(dao,type);
		//setSpecimenClassTypeCollection(dao,type);
	    dao.insert(type,sessionDataBean, true, true);
	}
	
//  This method sets the collection Storage Types.
	/*private void setStorageTypeCollection(DAO dao,StorageType storageType) throws DAOException
	{
		
		
		Collection storageTypeColl = new HashSet();
		
		Iterator it = storageType.getStorageTypeCollection().iterator();
		while(it.hasNext())
		{
			StorageType storageTypeHold  =(StorageType)it.next();
			
				Logger.out.debug("storage Type ID :"+storageTypeHold.getSystemIdentifier());
				Object obj = dao.retrieve(StorageType.class.getName(),  storageTypeHold.getSystemIdentifier());
				if (obj != null)
				{
					StorageType storageTypeHold1 = (StorageType) obj;//list.get(0);
					
					//checkStatus(dao, coordinator, "coordinator");
					
					storageTypeColl.add(storageTypeHold1);
					//storageTypeHold1.getStorageTypeCollection().add(storageType);
				}
			
		}
		storageType.setStorageTypeCollection(storageTypeColl);
	}

	*/
//  This method sets the collection Specimen Class Types.
	/*private void setSpecimenClassTypeCollection(DAO dao,StorageType storageType) throws DAOException
	{
		Logger.out.debug("Specimen Class Type Size "+storageType.getSpecimenClassTypeCollection().size());
		Collection specimenClassTypeColl = new HashSet();
		
		Iterator it = storageType.getSpecimenClassTypeCollection().iterator();
		while(it.hasNext())
		{
			SpecimenClassType specimenClassType  =(SpecimenClassType)it.next();
			
				Logger.out.debug("Specimen Class Type ID :"+specimenClassType.getSystemIdentifier());
				Object obj = dao.retrieve(SpecimenClassType.class.getName(),  specimenClassType.getSystemIdentifier());
				if (obj != null)
				{
					SpecimenClassType specimenClassType1 = (SpecimenClassType) obj;//list.get(0);
					specimenClassTypeColl.add(specimenClassType1);

				}
			
		}
		storageType.setSpecimenClassTypeCollection(specimenClassTypeColl);
	}
 
*/
	/**
     * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException 
    {
		StorageType type = (StorageType)obj;

		dao.update(type.getDefaultStorageCapacity(), sessionDataBean, true, true, false);
		//setStorageTypeCollection(dao,type);
	    dao.update(type, sessionDataBean, true, true, false);
	    
	    //Audit of update.
	    StorageType oldStorageType = (StorageType) oldObj;
	    dao.audit(type.getDefaultStorageCapacity(), oldStorageType.getDefaultStorageCapacity(), sessionDataBean, true);
	    dao.audit(obj, oldObj, sessionDataBean, true);
    }
}