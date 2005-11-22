/*
 * Created on May 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ReportedProblemBizLogic extends DefaultBizLogic
{

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.dao.HibernateDAO#add(java.lang.Object)
     */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
        ReportedProblem reportedProblem = (ReportedProblem) obj;
        
        dao.insert(obj,sessionDataBean, true, false);
        
        // Send the reported problem to the administrator and the user who reported it.
        EmailHandler emailHandler = new EmailHandler();
        emailHandler.sendReportedProblemEmail(reportedProblem);
    }
    
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.dao.AbstractBizLogic#update(java.lang.Object)
     */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
        ReportedProblem reportedProblem = (ReportedProblem) obj;
        dao.update(obj, sessionDataBean, true,true, false);
        
        //Audit.
        dao.audit(obj, oldObj, sessionDataBean, true);
    }
}