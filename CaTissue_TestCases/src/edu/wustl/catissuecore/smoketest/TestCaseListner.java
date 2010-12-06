package edu.wustl.catissuecore.smoketest;

import java.util.List;

import junit.framework.TestListener;
import junit.framework.TestResult;

public class TestCaseListner extends  TestResult
{
public List<TestListener> getErr()
{
	return this.fListeners;
}


}
