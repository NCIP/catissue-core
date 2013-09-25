/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.gridgrouper;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.action.BaseAddEditAction;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;

/**
 * Allows invoking Grid Grouper synchronization from the UI.
 * 
 * @author Denis G. Krylov
 * @see SRS at
 *      https://ncisvn.nci.nih.gov/svn/catissue/catissuecore/trunk/docs/GridGrouper
 * 
 */
public class GridGroupSyncAction extends BaseAddEditAction {

    private static final edu.wustl.common.util.logger.Logger LOGGER = Logger
            .getCommonLogger(GridGroupSyncAction.class);
    
	@Override
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ApplicationException {
		
		LOGGER.info("********Sync ... START");
		String result = "OK";
		GridGroupSync sync = new GridGroupSync();
		try {
			sync.syncAllMigratedUsers();
		} catch (Exception e) {
		    result = "Failed";
		    LOGGER.error(e.getMessage(), e);
		}
		try {
		    PrintWriter pw = response.getWriter();
		    pw.write(result);
		    pw.flush();			
		} catch (IOException e) {
		    LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("********Sync ... END");
		return null;
	}

}
