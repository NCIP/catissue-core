package edu.wustl.catissuecore.smoketest.admin;

import edu.wustl.catissuecore.actionForm.TitliSearchForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.testframework.util.DataObject;

public class KeywordSearchTestCase extends CaTissueSuiteSmokeBaseTest
{
	public KeywordSearchTestCase(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public KeywordSearchTestCase(String name)
	{
		super(name);
	}
	public KeywordSearchTestCase()
	{
		super();
	}
	public void testKeywordSearch()
	{
		String[] arr = getDataObject().getValues();
		TitliSearchForm titliSearchForm = new TitliSearchForm();
		titliSearchForm.setSearchString(arr[0]);
		setRequestPathInfo("/TitliInitialiseSearch");
		setActionForm(titliSearchForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}
}
