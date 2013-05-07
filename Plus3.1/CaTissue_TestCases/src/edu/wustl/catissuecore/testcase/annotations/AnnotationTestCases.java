
package edu.wustl.catissuecore.testcase.annotations;

import java.util.List;

import edu.wustl.catissuecore.actionForm.AnnotationDataEntryForm;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.JDBCDAO;

public class AnnotationTestCases extends CaTissueSuiteBaseTest
{

	private static Long partRecEntryEntityId = null;
	private static Long participantId = Long.valueOf(1);
	private static String partEntityName = "edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry";

	public void testDisplayAnnotations()
	{
		logger.info("DisplayAnnotationDataEntryPage------------------");
		setRequestPathInfo("/DisplayAnnotationDataEntryPage");
		//addRequestParameter("entityId", "1935");
		addRequestParameter("entityRecordId", "1");
		addRequestParameter("id", "1");
		addRequestParameter("staticEntityName", partEntityName);

		actionPerform();
		//verifyForward("pageOfParticipant");//pageOfParticipantCPQuery
		logger.info("LoadAnnotationDataEntryPage------------------");
		AnnotationDataEntryForm annotationDataEntryForm = new AnnotationDataEntryForm();
		setRequestPathInfo("/LoadAnnotationDataEntryPage");
		setActionForm(annotationDataEntryForm);

		JDBCDAO dao = null;

		try
		{
			dao = AppUtility.openJDBCSession();
			String sql = "select identifier from DYEXTN_ABSTRACT_METADATA where " + "name='"
					+ partEntityName + "'";

			List list = dao.executeQuery(sql);
			List objs = (List) list.get(0);
			partRecEntryEntityId = Long.valueOf(objs.get(0).toString());
			addRequestParameter("entityId", partRecEntryEntityId.toString());
			addRequestParameter("staticEntityName", partEntityName);
			addRequestParameter("operation", "null");
			addRequestParameter("entityRecordId", "1");

			actionPerform();
			verifyForward("success");
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
			fail("Error while querying DB");
		}
		finally
		{
			try
			{
				AppUtility.closeDAOSession(dao);
			}
			catch (ApplicationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail("Error while closing DB session");
			}
		}
		logger.info("LoadAnnotationDataEntryPage Done------------------");
	}

	public void testOpenDEPage()
	{
		logger.info("LoadDynamicExtentionsDataEntryPage------------------");
		setRequestPathInfo("/LoadDynamicExtentionsDataEntryPage");
		AnnotationDataEntryForm annotationDataEntryForm = new AnnotationDataEntryForm();
		annotationDataEntryForm.setActivityStatus("Active");
		annotationDataEntryForm.setForwardTo("success");
		annotationDataEntryForm.setSelectedStaticEntityId(partRecEntryEntityId.toString()); // PRE metadata id
		annotationDataEntryForm.setSelectedStaticEntityRecordId(participantId.toString());// Part ID
		annotationDataEntryForm.setSelectedAnnotation("37");
		addRequestParameter("selectedAnnotation", "37");
		setActionForm(annotationDataEntryForm);
		actionPerform();
		verifyForward("success");
		logger.info("LoadDynamicExtentionsDataEntryPage Done------------------");
	}

}
