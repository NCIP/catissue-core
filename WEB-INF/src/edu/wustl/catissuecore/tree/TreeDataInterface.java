/*
 * Created on Jul 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.tree;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dao.exception.DAOException;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface TreeDataInterface
{

	/**
	 * @return treeData .
	 * @throws DAOException
	 */
	public abstract Vector getTreeViewData() throws DAOException;

	/**
	 * Called to get TreeView data.
	 * @param sessionData session data
	 * @param map map
	 * @param list list of data.
	 * @return
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public abstract Vector getTreeViewData(SessionDataBean sessionData, Map map, List list)
			throws DAOException, ClassNotFoundException;
}
