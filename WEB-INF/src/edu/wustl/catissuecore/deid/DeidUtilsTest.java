package edu.wustl.catissuecore.deid;

import org.jdom.output.Format;

import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.common.test.BaseTestCase;

/**
 * @author vijay_pande
 * Test cases for the DeidUtils class
 */
public class DeidUtilsTest extends BaseTestCase 
{
	IdentifiedSurgicalPathologyReport ispr;
	DeidReportThread deidReport;
	/**
	 * Default constructor
	 */
	public DeidUtilsTest()
	{
		super();
	}
	/**
	 * @param name
	 * constructor which takes String as an input
	 */
	public DeidUtilsTest(String name)
	{
		super(name);
	}
	/**
	 * @param ispr identified surgical pathology report
	 * constructor which takes IdentifiedSurgicalPathologyReport as an input
	 */
	public DeidUtilsTest(IdentifiedSurgicalPathologyReport ispr)
	{
		this.ispr=ispr;
	}
	/** (non-Javadoc)
	 * @see edu.wustl.common.test.BaseTestCase#setUp()
	 */
	protected void setUp()
	{
		try
		{
			CaTIESProperties.initBundle("caTIES");
			deidReport=new DeidReportThread(ispr);
		}
		catch(Exception ex)
		{
			
		}
	}
	
	/**
	 * This test case is to check buildReportElement for null participant
	 */
	public void testForBuildReportElement()
	{
		try
		{
			DeidUtils.buildReportElement(null, new IdentifiedSurgicalPathologyReport(), "This is Synthesized text");
			fail("When particiant is null, it should throw Null pointer exception");
		}
		catch(Exception ex)
		{
			assertTrue("buildReportElement throws null pointer exception successfully",true);
		}
	}	
	
	/**
	 * This test case is to check buildReportElement for null participant and null report
	 */
	public void testForBuildHeaderPersonElement()
	{
		try
		{
			DeidUtils.buildHeaderPersonElement(null, null, null);
			fail("When input parameters are null, it should throw Null pointer exception");
		}
		catch(Exception ex)
		{
			assertTrue("BuildHeaderPersonElement throws null pointer exception successfully",true);
		}
	}
	
	/**
	 * This test case is to check buildHeaderPersonElementVariable for null value and null role
	 */
	public void testForBuildHeaderPersonElementVariable()
	{
		try
		{
			DeidUtils.buildHeaderPersonElement("Person", null, null);
			fail("When input string 'value' and 'role' is null, it should throw Null pointer exception");
		}
		catch(Exception ex)
		{
			assertTrue("BuildHeaderPersonElement throws null pointer exception successfully",true);
		}
	}
	
	/**
	 * This test case is to check buildHeaderPersonElementVariable for null role
	 */
	public void testForBuildHeaderPersonElementVariableValue()
	{
		try
		{
			DeidUtils.buildHeaderPersonElement("Person", "pName", null);
			fail("When input string 'role' is null, it should throw Null pointer exception");
		}
		catch(Exception ex)
		{
			assertTrue("BuildHeaderPersonElement throws null pointer exception successfully",true);
		}
	}
	
	/**
	 * This test case is to check buildHeaderPersonElementVariable for null variable
	 */
	public void testForBuildHeaderPersonElementValue()
	{
		try
		{
			DeidUtils.buildHeaderPersonElement(null, "pName", null);
			fail("When input string ''variable' is null, it should throw Null pointer exception");
		}
		catch(Exception ex)
		{
			assertTrue("extractDate throws null pointer exception successfully",true);
		}
	}
	
	/**
	 * This test case is to check extractDate for null report
	 */
	public void testForExtractDate()
	{
		try
		{
			DeidUtils.extractDate(null);
			assertTrue("extractDate should not throw null pointer exception",true);
		}
		catch(NullPointerException ex)
		{
			fail("When input string is null, it should not throw Null pointer exception");
		}
		catch(Exception ex)
		{
			fail("Though input string is null, it should not throw exception");
		}
	}	
	
	/**
	 * This test case is to check convertDocumentToString when Document is null
	 */
	public void testForConvetDocumentToString()
	{
		try
		{
			org.jdom.Document doc =null;	
			DeidUtils.convertDocumentToString(doc,Format.getPrettyFormat());
			fail("When XML documet is null , it should throw NullPointerException");
		}
		catch(Exception ex)
		{
			assertTrue("convertDocumentToString throws null pointer exception successfully",true);
		}
	}
	
	/**
	 * This test case is to check extractReport for null report Document
	 */
	public void testForExtractReport()
	{
		try
		{	
			DeidUtils.extractReport("This is some bla..bla text", null);
			fail("When dtd filename is null , it should throw JDOMException");
		}
		catch(Exception ex)
		{
			assertTrue("extractReport throws JDOMException successfully",true);
		}
	}	
	
	
}
