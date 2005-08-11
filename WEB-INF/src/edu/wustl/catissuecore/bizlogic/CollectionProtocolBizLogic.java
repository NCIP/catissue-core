/**
 * <p>Title: CollectionProtocolBizLogic Class>
 * <p>Description:	CollectionProtocolBizLogic is used to add CollectionProtocol information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 09, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.common.beans.UserGroupRoleProtectionGroupBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


/**
 * CollectionProtocolBizLogic is used to add CollectionProtocol information into the database using Hibernate.
 * @author Mandar Deshmukh
 */
public class CollectionProtocolBizLogic extends DefaultBizLogic implements Roles
{
	/**
     * Saves the CollectionProtocol object in the database.
     * @param session The session in which the object is saved.
     * @param obj The CollectionProtocol object to be saved.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
	protected void insert(DAO dao, Object obj) throws DAOException
	{
		CollectionProtocol collectionProtocol = (CollectionProtocol)obj;
		
		List list = dao.retrieve(User.class.getName(), "systemIdentifier", collectionProtocol.getPrincipalInvestigator().getSystemIdentifier());
		if (list.size() != 0)
		{
			User pi = (User) list.get(0);
			collectionProtocol.setPrincipalInvestigator(pi);
		}
		
		Collection coordinatorColl = new HashSet();
		Iterator it = collectionProtocol.getUserCollection().iterator();
		while(it.hasNext())
		{
			User aUser  =(User)it.next();
			list = dao.retrieve(User.class.getName(), "systemIdentifier", aUser.getSystemIdentifier());
			if (list.size() != 0)
			{
				User coordinator = (User) list.get(0);
				coordinatorColl.add(coordinator);
				coordinator.getCollectionProtocolCollection().add(collectionProtocol);
				dao.update(coordinator);
			}
		}
		collectionProtocol.setUserCollection(coordinatorColl);
		
		dao.insert(collectionProtocol,true);
		it = collectionProtocol.getCollectionProtocolEventCollection().iterator();
		while(it.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)it.next();
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			dao.insert(collectionProtocolEvent,true);
			
			Iterator srIt = collectionProtocolEvent.getSpecimenRequirementCollection().iterator();
			while(srIt.hasNext())
			{
				SpecimenRequirement specimenRequirement = (SpecimenRequirement)srIt.next();
				specimenRequirement.getCollectionProtocolEventCollection().add(collectionProtocolEvent);
				dao.insert(specimenRequirement,true);
			}
		}
		
		try
        {
            SecurityManager.getInstance(this.getClass()).insertAuthorizationData((AbstractDomainObject) obj,this);
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
	}
	
	/**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
    protected void update(DAO dao,Object obj) throws DAOException
    {
    	
    }

    /**
     * This method returns collection of UserGroupRoleProtectionGroup objects that speciefies the 
     * user group protection group linkage through a role. It also specifies the groups the protection  
     * elements returned by this class should be added to.
     * @return
     */
    public Vector getAuthorizationData(AbstractDomainObject obj)
    {
        Logger.out.debug("--------------- In here ---------------");
        Vector authorizationData = new Vector();
        HashSet group = new HashSet();
       
        
        CollectionProtocol collectionProtocol = (CollectionProtocol)obj;
        
        try
        {
            group.add(SecurityManager.getInstance(this.getClass()).getUserById(String.valueOf(collectionProtocol.getPrincipalInvestigator().getSystemIdentifier())));
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
        String protectionGroupName = new String("COLLECTION_PROTOCOL_"+collectionProtocol.getSystemIdentifier());
        UserGroupRoleProtectionGroupBean userGroupRoleProtectionGroupBean = new UserGroupRoleProtectionGroupBean();
        userGroupRoleProtectionGroupBean.setUser(String.valueOf(collectionProtocol.getPrincipalInvestigator().getSystemIdentifier()));
        userGroupRoleProtectionGroupBean.setRoleName(PI);
        userGroupRoleProtectionGroupBean.setGroupName("PI_"+collectionProtocol.getPrincipalInvestigator().getSystemIdentifier()+"COLLECTION_PROTOCOL_"+collectionProtocol.getSystemIdentifier());
        userGroupRoleProtectionGroupBean.setProtectionGroupName(protectionGroupName);
        userGroupRoleProtectionGroupBean.setGroup(group);
        authorizationData.add(userGroupRoleProtectionGroupBean);
        Logger.out.debug(authorizationData.toString());
        return authorizationData;
    }
    
    public Set getProtectionObjects(AbstractDomainObject obj)
    {
        Set protectionObjects = new HashSet();
        
        CollectionProtocol collectionProtocol = (CollectionProtocol)obj;
        protectionObjects.add(collectionProtocol);
        Collection collectionProtocolEventCollection = collectionProtocol.getCollectionProtocolEventCollection();
        if(collectionProtocolEventCollection != null)
        {
           for(Iterator it = collectionProtocolEventCollection.iterator(); it.hasNext();)
           {
               protectionObjects.add(it.next());
           }
        }
        Logger.out.debug(protectionObjects.toString());
        return protectionObjects;
    }
    
    
}