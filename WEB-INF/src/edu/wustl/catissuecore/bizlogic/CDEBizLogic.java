/*
 * Created on Jul 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.bizlogic;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.tissuesite.TissueSiteTreeNode;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEImpl;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.cde.PermissibleValueImpl;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

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
        
        //Delete the previous CDE data from the database.
        delete(cde, dao);
        
        //Insert the new CDE data in teh database.
        dao.insert(cde, sessionDataBean, false,false);
        Iterator iterator = cde.getPermissibleValues().iterator();
        while (iterator.hasNext())
        {
            PermissibleValueImpl permissibleValue = (PermissibleValueImpl) iterator.next();
            dao.insert(permissibleValue, sessionDataBean, false,false);
        }
    }	
    
    /**
     * Deletes the CDE and the corresponding permissible values from the database.
     * @param obj the CDE to be deleted.
     * @param dao the DAO object. 
     */
    protected void delete(Object obj, DAO dao) throws DAOException,
            UserNotAuthorizedException
    {
        CDE cde = (CDE) obj;
        List list = dao.retrieve(CDEImpl.class.getName(), "publicId", cde.getPublicId());
        if (!list.isEmpty())
        {
            CDEImpl cde1 = (CDEImpl)list.get(0);
            dao.delete(cde1);
        }
    }
    
    /**
     * (non-Javadoc)
     * @see edu.wustl.catissuecore.bizlogic.TreeDataInterface#getTreeViewData()
     */
    public Vector getTreeViewData() throws DAOException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Vector getTreeViewData(String cdeName) throws DAOException
    {

        try
        {
            cdeName = URLDecoder.decode(cdeName, "UTF-8");
        }catch(UnsupportedEncodingException encExp)
        {
            throw new DAOException("Could not generate tree : CDE name not proper.");
        }
        
        CDE cde = CDEManager.getCDEManager().getCDE(cdeName);
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
	    Long id = permissibleValueImpl.getIdentifier();
	    String val = permissibleValueImpl.getValue();
	    Long parentId = null;
	    
	    if (permissibleValueImpl.getParentPermissibleValue() != null)
        {
            PermissibleValueImpl parentPermissibleValue = (PermissibleValueImpl) permissibleValueImpl
                    .getParentPermissibleValue();
            parentId = parentPermissibleValue.getIdentifier();
        }
	    
	    String parentIdStr = null;
	    if(parentId!=null)
	    	parentIdStr = parentId.toString();
	    
	    TissueSiteTreeNode treeNode = new TissueSiteTreeNode(id.toString(),val,parentIdStr);
        return treeNode; 
	}
	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.TreeDataInterface#getTreeViewData(edu.wustl.common.beans.SessionDataBean, java.util.Map)
	 */
	public Vector getTreeViewData(SessionDataBean sessionData, Map map,List list) throws DAOException {

		return null;
	}
}