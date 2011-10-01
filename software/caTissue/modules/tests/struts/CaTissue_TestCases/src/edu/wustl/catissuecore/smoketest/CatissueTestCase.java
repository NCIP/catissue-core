package edu.wustl.catissuecore.smoketest;

import junit.framework.Test;
import edu.wustl.testframework.StrutsTestAutomation;

public class CatissueTestCase
{
	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(CatissueTestCase.class);
	}

	public static Test suite()
	{
//		System.out.println("Count:  "+StrutsTestAutomation.getSuite().countTestCases());
		//strutsProp.home
		return StrutsTestAutomation.getSuite();
	}

}
