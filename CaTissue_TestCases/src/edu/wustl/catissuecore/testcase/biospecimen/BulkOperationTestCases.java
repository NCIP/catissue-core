/**
 * 
 */
package edu.wustl.catissuecore.testcase.biospecimen;

import java.io.File;

import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.bizlogic.bulkOperations.ImportBulkOperationTemplate;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;

/**
 * @author sagar_baldwa
 *
 */
public class BulkOperationTestCases extends CaTissueSuiteBaseTest
{
	/**
	 * testAddBulkOperationTemplateForUI.
	 */
	public void testAddBulkOperationTemplateForUI()
	{
		try
		{
			String operationName = "createMolecularSpecimenEvent";
			String dropdownName = "Molecular Specimen Events";
			System.out.println(System.getProperty("bulk.operation.add.csv.file1"));
			String csvFilePath = System.getProperty("bulk.operation.add.csv.file1");				
			String xmlFilePath = System.getProperty("bulk.operation.add.xml.file1");
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
	 * testEditBulkOperationTemplateForUI.
	 */
	public void testEditBulkOperationTemplateForUI()
	{
		try
		{
			String operationName = "createMolecularSpecimenEvent";
			String dropdownName = "Molecular Specimen Events";
			System.out.println(System.getProperty("bulk.operation.add.csv.file1"));
			String csvFilePath = System.getProperty("bulk.operation.add.csv.file1");				
			String xmlFilePath = System.getProperty("bulk.operation.add.xml.file1");
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
	/**
	 * Mismatch operation name with the template specified
	 * in the XML.
	 */
	public void testWithIncorrectOperationName()
	{
		try
		{
			String operationName = "createMolecularSpecimenEvent1";
			String dropdownName = "Molecular Specimen Events";
			System.out.println(System.getProperty("bulk.operation.add.csv.file1"));
			String csvFilePath = System.getProperty("bulk.operation.add.csv.file1");				
			String xmlFilePath = System.getProperty("bulk.operation.add.xml.file1");
			new ImportBulkOperationTemplate(operationName, dropdownName,
						csvFilePath, xmlFilePath);
			System.out.println("Bulk Operation template edited successfully");
			assertFalse("Bulk Operation template edited successfully.", true);
		}
		catch (Exception e)
		{
			System.out.println("Exception in testWithIncorrectOperationName");
			System.out.println("Test case successfully executed.");
			e.printStackTrace();
			assertTrue("Could Not Add Bulk Operation template. Operation Name mismatch.", true);
		}
	}
	/**
	 * Test Bulk Operation With Incorrect XML Attribute Names.
	 */
	public void testBulkOperationWithIncorrectXMLAttributeNames()
	{
		try
		{
			String operationName = "editSpecimen";
			String dropdownName = "Edit Specimen";
			System.out.println(System.getProperty("bulk.operation.add.csv.file2"));
			String csvFilePath = System.getProperty("bulk.operation.add.csv.file2");				
			String xmlFilePath = System.getProperty("bulk.operation.add.xml.file2");
			new ImportBulkOperationTemplate(operationName, dropdownName,
						csvFilePath, xmlFilePath);
			System.out.println("Bulk Operation template added successfully");
			assertFalse("Error: Bulk Operation template added successfully.", true);
		}
		catch (Exception e)
		{
			System.out.println("Exception in testBulkOperationWithIncorrectXMLAttributeNames");
			System.out.println("Test case successfully executed.");
			e.printStackTrace();
			assertTrue("Incorrect XML validated properly.", true);
		}
	}
	/**
	 * Test Bulk Operation With Incorrect class name specified in XML.
	 */
	public void testBulkOperationWithIncorrectClassName()
	{
		try
		{
			String operationName = "createDerivative";
			String dropdownName = "Create Derivative";
			System.out.println(System.getProperty("bulk.operation.add.xml.file3"));
			String csvFilePath = System.getProperty("bulk.operation.add.csv.file2");				
			String xmlFilePath = System.getProperty("bulk.operation.add.xml.file3");
			new ImportBulkOperationTemplate(operationName, dropdownName,
						csvFilePath, xmlFilePath);
			System.out.println("Bulk Operation template added successfully");
			assertFalse("Error: Bulk Operation template added successfully.", true);
		}
		catch (Exception e)
		{
			System.out.println("Exception in testBulkOperationWithIncorrectClassName");
			System.out.println("Test case successfully executed.");
			e.printStackTrace();
			assertTrue("Incorrect classname validated properly in XML.", true);
		}
	}
	/**
	 * testpopulateBulkOperationDropDown.
	 */
	public void testpopulateBulkOperationDropDown()
	{
		try
		{
			setRequestPathInfo("/BulkOperation");
			addRequestParameter("pageOf", "pageOfBulkOperation");
			actionPerform();
			verifyForward("pageOfBulkOperation");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * testAjaxCodeForLoadingOutputReportFileAfterBulkOperation.
	 */
	public void testAjaxCodeForLoadingOutputReportFileAfterBulkOperation()
	{
		try
		{
			File file = new File("test.csv");
			file.createNewFile();
			HttpSession session = request.getSession();
			session.setAttribute("resultFile", file);
			addRequestParameter("report", "report");
			setRequestPathInfo("/BulkOperation");
			actionPerform();
			verifyForward(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
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
			factory.setRepository(new File("tempFile.csv"));
			String csvFilePath = System.getProperty("user.dir");
			FileItem fileItem = factory.createItem("file", "text/csv",
						true, csvFilePath + "/CaTissue_TestCases/createMolecularSpecimenEvent.csv");
			//fileItem.isInMemory();
			FormFile file = (FormFile)c.newInstance(new Object[] {fileItem});
			c.setAccessible(false);
			//file.getInputStream();
			BulkOperationForm bulkOperationForm = new BulkOperationForm();
			bulkOperationForm.setOperationName("addSpecimen");
			bulkOperationForm.setFile(file);
			setRequestPathInfo("/FileUpload");
			setActionForm(bulkOperationForm);			
			addRequestParameter("operation", "addSpecimen");			
			actionPerform();
			verifyForward("success");
			verifyNoActionErrors();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}*/
}