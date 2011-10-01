package edu.wustl.catissuecore.smoketest.admin;

import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.testframework.util.DataObject;

public class InstitutionTestCases extends CaTissueSuiteSmokeBaseTest {

	public InstitutionTestCases(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public InstitutionTestCases(String name)
	{
		super(name);
	}
	public InstitutionTestCases()
	{
		super();
	}
	public void testInstituteAdd(){

		InstitutionForm institutionForm = new InstitutionForm();
		String arr[] = getDataObject().getValues();
		institutionForm.setName(arr[0]);
		institutionForm.setOperation("add");
		setRequestPathInfo("/InstitutionAdd");
		setActionForm(institutionForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
		assertEquals(InstitutionForm.class.getName(),getActionForm().getClass().getName());
	}

	/**
	 * Test Institution Edit.
	 */
	public void testInstitutionEdit()
	{
		String arr[] = getDataObject().getValues();
		Long institutionId = new Long(arr[0]);
		setRequestPathInfo("/InstitutionSearch");
		addRequestParameter("id", "" + institutionId);
		addRequestParameter("pageOf", "pageOfInstitution" );
		actionPerform();
		verifyForward("pageOfInstitution");
		verifyNoActionErrors();

		setRequestPathInfo(getActualForward());
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfInstitution");
		addRequestParameter("menuSelected", "2");
		actionPerform();
		verifyNoActionErrors();

		InstitutionForm form = (InstitutionForm)getActionForm();
		/*Edit Action*/
		form.setName(arr[1]);
		form.setOperation("edit");
		setRequestPathInfo("/InstitutionEdit");
		setActionForm(form);
		actionPerform();
		verifyNoActionErrors();

		verifyForward("success");
		verifyActionMessages(new String[]{"object.edit.successOnly"});
		assertEquals(InstitutionForm.class.getName(),getActionForm().getClass().getName());

	}

	/**
	 * Test Institution Add.
	 */

	public void testInstitutionAddByPopUp()
	{
		String arr[] = getDataObject().getValues();
		setRequestPathInfo("/Institution");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfPopUpInstitution");
		actionPerform();
		verifyForward("pageOfPopUpInstitution");
		verifyNoActionErrors();

		setRequestPathInfo("/AddInstitution");
		addRequestParameter("pageOf", "pageOfInstitution");
		addRequestParameter("instituteName", arr[0]);
		addRequestParameter("operation", "add");
		actionPerform();
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
		assertEquals(InstitutionForm.class.getName(),getActionForm().getClass().getName());

	}

}
