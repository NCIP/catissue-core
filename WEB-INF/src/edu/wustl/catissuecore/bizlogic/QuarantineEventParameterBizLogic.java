package edu.wustl.catissuecore.bizlogic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * This class is used to store the QuarantineEventParameter object to database
 * @author vijay_pande
 * 
 */
public class QuarantineEventParameterBizLogic extends IntegrationBizLogic
{
	/**
	 * Saves the Pathology Report Quarantine Event Parameter object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 * @throws UserNotAuthorizedException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		QuarantineEventParameter quarantineParam = (QuarantineEventParameter) obj;
		
		String className;
		String colName=new String(Constants.SYSTEM_IDENTIFIER);
		className=User.class.getName();
		List userList=dao.retrieve(className, colName, sessionDataBean.getUserId());
		quarantineParam.setUser((User)userList.get(0));
		dao.insert(quarantineParam, sessionDataBean, true, true);
		Set protectionObjects = new HashSet();
		protectionObjects.add(quarantineParam);
		try
		{
			SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, null);
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		try{
			QuarantineEventParameter quarantineParam = (QuarantineEventParameter) obj;
			if(quarantineParam.getUser().getId()==null){
				dao.insert(quarantineParam.getUser(), sessionDataBean, false, false);	
			}
			dao.update(quarantineParam, sessionDataBean, true, false, false);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
