/**
 * <p>Title: UserHDAO Class>
 * <p>Description:	UserHDAO is used to add user information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 13, 2005
 */

package edu.wustl.catissuecore.dao;

import java.util.List;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class UserBizLogic extends AbstractBizLogic
{
	/**
     * Saves the user object in the database.
     * @param session The session in which the object is saved.
     * @param obj The user object to be saved.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
	public void insert(Object obj) throws HibernateException,DAOException 
	{
		User user = (User)obj;
		Department department = null;
		Institution institution = null;
		CancerResearchGroup cancerResearchGroup = null;
//		Role role = null;
		
        AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
		dao.openSession();
		
		List list = dao.retrieve(Department.class.getName(), "name", user.getDepartment().getName());
		if (list.size() != 0)
		{
		    department = (Department) list.get(0);
		}
		 
		list = dao.retrieve(Institution.class.getName(), "name", user.getInstitution().getName());
		if (list.size() != 0)
		{
		    institution = (Institution) list.get(0);
		}
 
//		list = dao.retrieve(Role.class.getName(), "name", user.getRole().getName());
//		if (list.size() != 0)
//		{
//		    role = (Role) list.get(0);		    
//		}
		
		list = dao.retrieve(CancerResearchGroup.class.getName(), "name", user.getCancerResearchGroup().getName());
		if (list.size() != 0)
		{
		    cancerResearchGroup = (CancerResearchGroup) list.get(0);		    
		}
	    
//	    list = dao.retrieve(ActivityStatus.class.getName(), "status", user.getActivityStatus().getStatus());
//	    if (list.size() != 0)
//	    {
//	        activityStatus = (ActivityStatus) list.get(0);
//	    }

//	    try{
//	        user.setCommentClob(Hibernate.createClob(" "));
//	    }
//	    catch(SQLException sqlExp)
//	    {
//	        Logger.out.error(sqlExp.getMessage(),sqlExp);
//	    }
	    
	    user.setDepartment(department);
	    user.setInstitution(institution);
//	    user.setRole(role);
	    user.setCancerResearchGroup(cancerResearchGroup);
	    
	    dao.insert(user.getAddress());
	    dao.insert(user);
	    
	    dao.closeSession();
	}
	
	/**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
    public void update(Object obj) throws DAOException,
            HibernateException
    {
        User user = (User)obj;
//        ActivityStatus activityStatus = null;
        List list = null;
        
        HibernateDAO dao = new HibernateDAO();
        dao.openSession();
        
//        list = dao.retrieve(ActivityStatus.class.getName(),"status",user.getActivityStatus().getStatus());
//	    if (list.size() != 0)
//	    {
//	        activityStatus = (ActivityStatus) list.get(0);
//	    }
//
//	    user.setActivityStatus(activityStatus);
	    
//	    super.edit(user);
	    
	    if (user.getComments() != null)
	    {
	        System.out.println("In Clob...........................");
	        String comments = new String(user.getComments());
//	        try
//	        {
//	            
//	            CLOB clob = null;
//	            list = retrieveInSameSession(User.class.getName(),"systemIdentifier",user.getIdentifier());
//	            session.refresh(user,LockMode.UPGRADE);
//	            clob = (CLOB) user.getCommentClob();
//	            if (list.size() != 0)
//	            {
//	                user = (User)list.get(0);
//	                clob = (CLOB) user.getCommentClob();
//	            }
//		        Writer clobWriter = clob.getCharacterOutputStream();
//		        System.out.println("comments:::...................."+comments);
//		        clobWriter.write(comments);
//		        clobWriter.close();
//		        
//	        }
//	        catch(SQLException sqlExp)
//	        {
//	            Logger.out.error(sqlExp.getMessage(),sqlExp);
//	        }
//	        catch(IOException ioExp)
//	        {
//	            Logger.out.error(ioExp.getMessage(),ioExp);
//	        }
	        
//	        CLOB newClob = CLOB.createTemporary(session.connection(),true,CLOB.D);
	    }
	    dao.update(user);
	    dao.closeSession();
    }
	
//	public static void main(String[] args) throws Exception
//	{
//		UserBizLogic aUserHDAO = new UserBizLogic();
////		String colName[] = {"name"};
////		String colCondition[] = {"in"};
////		Object colValue[] = {"Cardiology, Pathology"};
////		
////		List aList = aUserHDAO.retrieve(Department.class.getName(),colName,colCondition,colValue,Constants.OR_JOIN_CONDITION);
////		//Object obj[]= aList.toArray();
////		for (int i = 0; i < aList.size(); i++)
////		{
////			System.out.println(aList.get(i));
////		}
//		User user = new User();
//		
//		user.setLoginName("abcd123");
//		user.setEmail("abcd@abcd.com");
//		user.getDepartment().setName("Cardiology");
//		user.getInstitute().setName("Washington University");
//		user.getRole().setName("Technician");
//		user.getActivityStatus().setStatus("New");
//		user.setCommentString("abcdefgh");
//		
//		aUserHDAO.update(user);
//	}
}