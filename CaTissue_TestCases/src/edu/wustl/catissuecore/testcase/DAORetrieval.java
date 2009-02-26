package src.edu.wustl.catissuecore.testcase;

import org.junit.Test;

import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.util.global.Constants;
/**
 * This class retrieves the DAO object from DAOFactory and sets it in the map
 * @author Himanshu Aseeja
 */
public class DAORetrieval extends CaTissueSuiteBaseTest
{
	@Test
	public void testDatabaseOperation() 
	{
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		TestCaseUtility.setNameObjectMap("DAO",dao); 
	}
}
