/*
 * Created on May 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.dao;

import edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.SendEmail;
import edu.wustl.catissuecore.util.global.Variables;
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
    public void insert(Object obj) throws DAOException
    {
        ReportedProblem reportedProblem = (ReportedProblem) obj;
        AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
        dao.openSession();

        dao.insert(obj);

        dao.closeSession();
        
        //Send the reported problem to administrator and the user who reported it.
        SendEmail email = new SendEmail();
        String body = ApplicationProperties.getValue("email.reportProblem.body.start") + 
        			  "\n " + ApplicationProperties.getValue("reportedProblem.from") + " : " + reportedProblem.getFrom() + 
        			  "\n " + ApplicationProperties.getValue("reportedProblem.title") + " : " + reportedProblem.getSubject() + 
        			  "\n " + ApplicationProperties.getValue("reportedProblem.message") + " : " + reportedProblem.getMessageBody() +
        			  "\n\n" + ApplicationProperties.getValue("email.catissuecore.team");
        
        String subject = ApplicationProperties.getValue("email.reportProblem.subject");
        
        boolean mailStatus = email.sendmail(Variables.emailAddress,reportedProblem.getFrom(),null,
                			 reportedProblem.getFrom(),Variables.mailServer,subject,
                			 body);
        
    }
    
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.dao.AbstractBizLogic#update(java.lang.Object)
     */
    public void update(Object obj) throws DAOException
    {
        ReportedProblem reportedProblem = (ReportedProblem) obj;
        AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
        
        dao.openSession();

        dao.update(obj);

        dao.closeSession();
    }
}