package edu.wustl.catissuecore.deid;

import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.common.test.BaseTestCase;

/**
 * @author vijay_pande
 * JUnit test case class for DeidReportThread
 */
public class DeidReportThreadTest extends BaseTestCase 
{
	IdentifiedSurgicalPathologyReport ispr;
	DeidReportThread deidReport;
	/**
	 *  Default constructor
	 */
	public DeidReportThreadTest()
	{
		super();
	}
	/**
	 * constructor which takes String as an input
	 * @param name String name
	 */
	public DeidReportThreadTest(String name)
	{
		super(name);
	}
	/**
	 * constructor which takes IdentifiedSurgicalPathologyReport as an input
	 * @param ispr identified surgical pathology report
	 */
	public DeidReportThreadTest(IdentifiedSurgicalPathologyReport ispr)
	{
		this.ispr=ispr;
	}
	
	/** 
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
	 * This test case is to check deIdentify when library is not loaded
	 */
	public void testForDeIdentifyTextWithoutLibrary()
	{
		try
		{
			String str=deidReport.deIdentify(null);
			fail("If deid library is not loaded properly then deIdentify should throw an exception");
		}
		catch(Exception ex)
		{
			assertTrue("deIdentify throws exception successfully", true);
		}
	}
	/**
	 * This test case is to check deIdentify when input text is null
	 */
/*	public void testForDeIdentifyText()
	{
		try
		{
			
			Variables.applicationHome = System.getProperty("user.dir");
			DeIDPipelineManager.deid=new JniDeID();
			String str=deidReport.deIdentify(null);
			fail("If text for deid is null deIdentify should throw an exception");
		}
		catch(Exception ex)
		{
			  
		//	DeIDPipelineManager.deid.unloadDeidLibrary();
			assertTrue("deIdentify throws exception successfully", true);
		}
	}
	*/
}
