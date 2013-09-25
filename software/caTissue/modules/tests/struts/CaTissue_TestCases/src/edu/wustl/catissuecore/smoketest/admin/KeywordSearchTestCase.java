/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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
