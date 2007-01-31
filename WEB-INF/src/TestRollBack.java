import org.apache.log4j.PropertyConfigurator;

import edu.wustl.catissuecore.domain.Department;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.HibernateDAOImpl;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/*
 * Created on Aug 25, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestRollBack 
{
    public static void main(String[] args) throws Exception 
    {
    	System.out.println("Head TEST");
        Variables.applicationHome = System.getProperty("user.dir");
		Logger.out = org.apache.log4j.Logger.getLogger("");
		PropertyConfigurator.configure(Variables.applicationHome+"\\WEB-INF\\src\\"+"ApplicationResources.properties");
		
		Logger.out.debug("here");
        
        Department dept = new Department();
        dept.setName("ABCD");
        
        HibernateDAO dao = new HibernateDAOImpl();
        dao.openSession(null);
        dao.insert(dept,null,false,false);
        dao.rollback();
        dao.closeSession();
    }
}
