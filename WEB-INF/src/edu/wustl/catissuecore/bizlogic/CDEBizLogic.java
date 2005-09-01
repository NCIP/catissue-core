/*
 * Created on Jul 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.tissuesite.TissueSiteTreeNode;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEImpl;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.cde.PermissibleValueImpl;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author gautam_shetty
 */
public class CDEBizLogic extends DefaultBizLogic implements TreeDataInterface
{

    /**
     * Saves the storageType object in the database.
     * @param session The session in which the object is saved.
     * @param obj The storageType object to be saved.
     * @throws DAOException 
     */
    protected void insert(DAO dao, Object obj) throws DAOException
    {
        CDEImpl cde = (CDEImpl) obj;
        dao.insert(cde, false);

        Iterator iterator = cde.getPermissibleValues().iterator();
        while (iterator.hasNext())
        {
            PermissibleValueImpl permissibleValue = (PermissibleValueImpl) iterator
                    .next();
            permissibleValue.setCde(cde);
            dao.insert(permissibleValue, false);
        }
    }
    
    public Vector getTreeViewData() throws DAOException
    {
        
        CDE cde = CDEManager.getCDEManager().getCDE(
                Constants.CDE_NAME_TISSUE_SITE);
        Set set = cde.getPermissibleValues();
        Vector vector = new Vector();
        Iterator iterator = set.iterator();
        while (iterator.hasNext())
        {
            PermissibleValueImpl permissibleValueImpl = (PermissibleValueImpl) iterator
                    .next();
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
	    TissueSiteTreeNode treeNode = new TissueSiteTreeNode();
	    
        treeNode.setIdentifier(permissibleValueImpl.getIdentifier());
        treeNode.setValue(permissibleValueImpl.getValue());
        
        if (permissibleValueImpl.getParentPermissibleValue() != null)
        {
            PermissibleValueImpl parentPermissibleValue = (PermissibleValueImpl) permissibleValueImpl
                    .getParentPermissibleValue();

            treeNode.setParentIdentifier(parentPermissibleValue
                    .getIdentifier());
        }
        else
        {
            treeNode.setCdeLongName(permissibleValueImpl.getCde()
                    .getLongName());
            treeNode.setCdePublicId(permissibleValueImpl.getCde()
                    .getPublicId());
        }
        
        return treeNode; 
	}
}