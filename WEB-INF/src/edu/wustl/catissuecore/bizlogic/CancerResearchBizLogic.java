/**
 * <p>Title: CancerResearchBizLogic Class>
 * <p>Description:	CancerResearchBizLogic </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.global.Variables;

public class CancerResearchBizLogic extends DefaultBizLogic
{

	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{	
		CancerResearchGroup cancerResearchGroup = (CancerResearchGroup) obj;
		if (cancerResearchGroup == null)
		{
			 String message = ApplicationProperties.getValue("app.cancerResearchGroup");
			 throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg",message));   			
			//throw new DAOException("domain.object.null.err.msg", new String[]{"Institution"});
		}
		
		Validator validate = new Validator();
		if (validate.isEmpty(cancerResearchGroup.getName()))
		{
			String message = ApplicationProperties.getValue("cancerResearchGroup.name");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));   			
			//throw new DAOException("errors.item.required", new String[]{message});
		}
		return true;
	}
	
	/**
	 * This method returns the Id of the CRG corresponding to the CRG name 
	 * @param crgName CancerResearchGroup name
	 * @return the id of the CancerResearchGroup
	 * @throws DAOException
	 */
	public String getLatestCRG(String crgName)throws DAOException
	{
		String sourceObjectName = CancerResearchGroup.class.getName();
    	String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER};
    	String[] whereColumnName = {Constants.NAME};
    	String[] whereColumnCondition = {Constants.EQUALS}; 
    	String[] whereColumnValue = {crgName};
		
    	List crgList = retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, null);
    	Long crgId = null;
    	if((crgList != null) && (crgList.size()>0))
    	{
    		crgId =(Long)crgList.get(0);
    	}
    	return crgId.toString();
	}
	
	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.AbstractDAO, java.lang.Object)
	 */
	public String getObjectId(AbstractDAO dao, Object domainObject) 
	{
		return Constants.ADMIN_PROTECTION_ELEMENT;
	}
	
	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeKey(Object domainObject)
    {

    	return Constants.ADD_EDIT_CRG;
    }
}
