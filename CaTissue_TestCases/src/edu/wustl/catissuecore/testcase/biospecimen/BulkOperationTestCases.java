/**
 * 
 */
package edu.wustl.catissuecore.testcase.biospecimen;

import edu.wustl.bulkoperator.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.bizlogic.bulkOperations.ImportBulkOperationTemplate;
/**
 * @author sagar_baldwa
 *
 */
public class BulkOperationTestCases extends CaTissueSuiteBaseTest
{
	public void testAddBulkOperationTemplateForUI() throws Exception
	{
		try
		{
			String operationName = "createMolecularSpecimenEvent";
			String dropdownName = "Molecular Specimen Events";
			System.out.println(System.getProperty("bulk.operation.add.csv.file"));
			String csvFilePath = System.getProperty("bulk.operation.add.csv.file");				
			String xmlFilePath = System.getProperty("bulk.operation.add.xml.file");
			ImportBulkOperationTemplate importBulkOperationTemplate =
				new ImportBulkOperationTemplate(operationName, dropdownName,
						csvFilePath, xmlFilePath);
			System.out.println("Bulk Operation template added successfully");
			assertTrue("Bulk Operation template added successfully", true);
		}
		catch (Exception e)
		{
			System.out.println("Exception in AddBulkOperationTemplate");
			e.printStackTrace();
			assertFalse("Could Not Add Bulk Operation template.", true);
		}		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void testEditBulkOperationTemplateForUI() throws Exception
	{
		try
		{
			String operationName = "createMolecularSpecimenEvent";
			String dropdownName = "Molecular Specimen Events";
			System.out.println(System.getProperty("bulk.operation.add.csv.file"));
			String csvFilePath = System.getProperty("bulk.operation.add.csv.file");				
			String xmlFilePath = System.getProperty("bulk.operation.add.xml.file");
			ImportBulkOperationTemplate importBulkOperationTemplate =
				new ImportBulkOperationTemplate(operationName, dropdownName,
						csvFilePath, xmlFilePath);
			System.out.println("Bulk Operation template edited successfully");
			assertTrue("Bulk Operation template edited successfully", true);
		}
		catch (Exception e)
		{
			System.out.println("Exception in EditBulkOperationTemplate");
			e.printStackTrace();
			assertFalse("Could Not Edit Bulk Operation template.", true);
		}
	}
	/*public void testpopulateBulkOperationDropDown() throws Exception
	{
		try
		{
			setRequestPathInfo("/BulkOperationTest");
			actionPerform();
			verifyForward("pageOfBulkOperation");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void testAjaxCodeForLoadingOutputReportFileAfterBulkOperation() throws Exception
	{
		try
		{
			File file = new File("test.csv");
			HttpSession session = request.getSession();
			session.setAttribute("resultFile", file);
			addRequestParameter("report", "report");
			setRequestPathInfo("/BulkOperationTest");
			actionPerform();
			verifyForward(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}*/
	
	/*public void testRunBulkOperationWithCorrectCSVData() throws Exception
	{
		try
		{	
			Class parentClass = Class.forName("org.apache.struts.upload.CommonsMultipartRequestHandler");
			Class childClass = parentClass .getDeclaredClasses()[0];
			Constructor c = childClass .getConstructors()[0];
			c.setAccessible(true);

			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1000*1000*10);
			factory.setRepository(new File("createAliSameCont_report.csv"));
			FileItem fileItem = factory.createItem("file", "Content-Type", true, "E:/createAliSameCont.csv");
			FormFile file = (FormFile)c.newInstance(new Object[] {fileItem});
			c.setAccessible(false);

			BulkOperationForm bulkOperationForm = new BulkOperationForm();
			bulkOperationForm.setOperationName("addSpecimen");
//			bulkOperationForm.setFile(file);
			addRequestParameter("operation", "addSpecimen");
			setRequestPathInfo("/FileUpload");
			setActionForm(bulkOperationForm);
			actionPerform();
			verifyForward("failure");
			verifyNoActionErrors();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}*/
}