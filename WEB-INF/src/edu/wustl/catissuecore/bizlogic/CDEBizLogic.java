/*
 * Created on Jul 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.bizlogic;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.tissuesite.TissueSiteTreeNode;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEImpl;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.cde.PermissibleValueImpl;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author gautam_shetty
 */
public class CDEBizLogic extends DefaultBizLogic implements TreeDataInterface
{

    /**
     * Saves the storageType object in the database.
     * @param obj The storageType object to be saved.
     * @param session The session in which the object is saved.
     * @throws DAOException
     */
    protected void insert(Object obj,DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
        CDEImpl cde = (CDEImpl) obj;
        dao.insert(cde, sessionDataBean, false,false);

        Iterator iterator = cde.getPermissibleValues().iterator();
        while (iterator.hasNext())
        {
            PermissibleValueImpl permissibleValue = (PermissibleValueImpl) iterator
                    .next();
            permissibleValue.setCde(cde);
            dao.insert(permissibleValue, sessionDataBean, false,false);
        }
    }
    
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.bizlogic.TreeDataInterface#getTreeViewData()
     */
    public Vector getTreeViewData() throws DAOException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Vector getTreeViewData(String cdeName) throws DAOException
    {
        Logger.out.debug("CDE BIZ LOGIC................................"+cdeName);
//        if (cdeName.equals("TissueSite"))
//            cdeName = "Tissue Site";
        try{
        cdeName = URLDecoder.decode(cdeName, "UTF-8");
        }catch(UnsupportedEncodingException encExp)
        {
        }
        CDE cde = CDEManager.getCDEManager().getCDE(cdeName);
//        CDE cde = CDEManager.getCDEManager().getCDE("Clinical Diagnosis");
        Set set = cde.getPermissibleValues();
        Vector vector = new Vector();
        Iterator iterator = set.iterator();
        while (iterator.hasNext())
        {
            PermissibleValueImpl permissibleValueImpl = (PermissibleValueImpl) iterator.next();
            TissueSiteTreeNode treeNode = getTissueSiteTreeNode(permissibleValueImpl);
            vector.add(treeNode);
            List subPVList = getSubPermissibleValues(permissibleValueImpl);
			vector.addAll(subPVList);
        }
        return vector;
    }
    
	private List getSubPermissibleValues(PermissibleValue permissibleValue)
	{
		List pvList = new ArrayList();
		
		Iterator iterator = permissibleValue.getSubPermissibleValues().iterator();
		while(iterator.hasNext())
		{
			PermissibleValueImpl subPermissibleValueImpl = (PermissibleValueImpl)iterator.next();
			TissueSiteTreeNode tissueSiteTreeNode = getTissueSiteTreeNode(subPermissibleValueImpl);
			pvList.add(tissueSiteTreeNode);
			List subPVList = getSubPermissibleValues(subPermissibleValueImpl);
			pvList.addAll(subPVList);
		}
		return pvList;
	}
	
	private TissueSiteTreeNode getTissueSiteTreeNode(PermissibleValueImpl permissibleValueImpl)
	{
	    String id = permissibleValueImpl.getIdentifier();
	    String val = permissibleValueImpl.getValue();
	    String parentId = null;
	    
	    if (permissibleValueImpl.getParentPermissibleValue() != null)
        {
            PermissibleValueImpl parentPermissibleValue = (PermissibleValueImpl) permissibleValueImpl
                    .getParentPermissibleValue();
            parentId = parentPermissibleValue.getIdentifier();
        }
	    
	    TissueSiteTreeNode treeNode = new TissueSiteTreeNode(id,val,parentId);
        return treeNode; 
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.TreeDataInterface#getTreeViewData(edu.wustl.common.beans.SessionDataBean, java.util.Map)
	 */
	public Vector getTreeViewData(SessionDataBean sessionData, Map map,List list) throws DAOException {

		return null;
	}
}