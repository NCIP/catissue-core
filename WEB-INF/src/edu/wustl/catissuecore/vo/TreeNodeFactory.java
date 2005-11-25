/*
 * Created on Aug 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.vo;

import edu.wustl.catissuecore.query.TreeNodeData;
import edu.wustl.catissuecore.storage.StorageContainerTreeNode;
import edu.wustl.catissuecore.tissuesite.TissueSiteTreeNode;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeNodeFactory
{
    public static TreeNode getTreeNode(int treeType)
    {
        TreeNode treeNode = null;
        switch(treeType)
        {
            case Constants.TISSUE_SITE_TREE_ID:
                treeNode = new TissueSiteTreeNode();
            	break;
            case Constants.STORAGE_CONTAINER_TREE_ID:
                treeNode = new StorageContainerTreeNode();
            	break;
            case Constants.QUERY_RESULTS_TREE_ID:
            	treeNode = new TreeNodeData();
                break;
        }
        
        return treeNode;
    }

}
