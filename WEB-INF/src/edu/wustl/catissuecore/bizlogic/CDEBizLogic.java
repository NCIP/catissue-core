/*
 * Created on Jul 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.List;
import java.util.Vector;

import edu.wustl.catissuecore.dao.AbstractDAO;
import edu.wustl.catissuecore.dao.DAOFactory;
import edu.wustl.catissuecore.tissuesite.TissueSiteTreeNode;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.PermissibleValueImpl;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author gautam_shetty
 */
public class CDEBizLogic extends DefaultBizLogic implements TreeDataInterface
{

    /**
     *  
     */
    public Vector getTreeViewData() throws DAOException
    {
        AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
        dao.openSession();
        List list = dao.retrieve(PermissibleValueImpl.class.getName());
        dao.commit();
        dao.closeSession();
        Vector vector = new Vector();
        if (list != null)
        {
            for (int i = 0; i < list.size(); i++)
            {
                PermissibleValueImpl permissibleValueImpl = (PermissibleValueImpl) list
                        .get(i);
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
                vector.add(treeNode);
            }
        }
        return vector;
    }

}