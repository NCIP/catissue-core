
package edu.wustl.catissuecore.test;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.common.bizlogic.IBizLogic;

/**
 * This is a mock class for BizLogicFactory.This class returns mock for  BizLogicInterface.
 * Through testcase we put bizLogicInterfaceMock in the MockBizLogicFactory.
 * @author sujay_narkar
 *
 */
public class MockBizLogicFactory extends BizLogicFactory
{

	private IBizLogic bizLogic;

	/**
	 * Empty Constructor
	 */
	public MockBizLogicFactory()
	{
	}

	/**
	 * This method returns particular business interface
	 * @param businessAction business action
	 * @return BusinessInterface business interface
	 */
	@Override
	public IBizLogic getBizLogic(int FORM_TYPE)
	{
		return this.bizLogic;
	}

	/**
	 * sets business interface
	 * @param businessInterface business interface to be set
	 */
	public void setBizLogic(IBizLogic bizLogic)
	{
		this.bizLogic = bizLogic;
	}
}