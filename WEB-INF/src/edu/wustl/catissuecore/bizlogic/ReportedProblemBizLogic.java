/*
 * Created on May 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.SendEmail;
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
	protected void insert(DAO dao, Object obj) throws DAOException
    {
        ReportedProblem reportedProblem = (ReportedProblem) obj;

        dao.insert(obj,true);

        //Send the reported problem to administrator and the user who reported it.
        SendEmail email = new SendEmail();
        String body = ApplicationProperties.getValue("email.reportProblem.body.start") + 
        			  "\n " + ApplicationProperties.getValue("reportedProblem.from") + " : " + reportedProblem.getFrom() + 
        			  "\n " + ApplicationProperties.getValue("reportedProblem.title") + " : " + reportedProblem.getSubject() + 
        			  "\n " + ApplicationProperties.getValue("reportedProblem.message") + " : " + reportedProblem.getMessageBody() +
        			  "\n\n" + ApplicationProperties.getValue("email.catissuecore.team");
        
        String adminEmailAddress = ApplicationProperties.getValue("email.administrative.emailAddress");
        String subject = ApplicationProperties.getValue("email.reportProblem.subject");
        String technicalSupportEmailAddress = ApplicationProperties.getValue("email.technicalSupport.emailAddress");
        String mailServer = ApplicationProperties.getValue("email.mailServer");
        
        boolean mailStatus = email.sendmail(adminEmailAddress,reportedProblem.getFrom(),null,
                			 technicalSupportEmailAddress,mailServer,subject,
                			 body);
    }
    
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.dao.AbstractBizLogic#update(java.lang.Object)
     */
	protected void update(DAO dao, Object obj) throws DAOException
    {
        ReportedProblem reportedProblem = (ReportedProblem) obj;
        dao.update(obj);
    }
}