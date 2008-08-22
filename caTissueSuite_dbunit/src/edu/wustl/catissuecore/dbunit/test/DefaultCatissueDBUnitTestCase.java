/**
 * @Class CatissueBaseUnitTestCase.java
 * @Author abhijit_naik
 * @Created on Aug 22, 2008
 */
package edu.wustl.catissuecore.dbunit.test;

import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.global.Constants;


/**
 * @author abhijit_naik
 *
 */
public class DefaultCatissueDBUnitTestCase extends CaTissueBaseDBUnitTestCase
{
	public DefaultCatissueDBUnitTestCase()
	{
		super();
		
	}
	protected final DatabaseOperation getSetUpOperation() 
	  throws Exception {
	   return DatabaseOperation.NONE;
	 }
	protected final IDataSet getDataSet() throws Exception
	{

		// TODO Auto-generated method stub
		return null;
	}


}
