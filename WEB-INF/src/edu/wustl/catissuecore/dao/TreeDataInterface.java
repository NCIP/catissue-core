/*
 * Created on Jul 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.dao;

import java.util.Vector;

import edu.wustl.common.util.dbManager.DAOException;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface TreeDataInterface
{
    public abstract Vector getTreeViewData() throws DAOException;
}
