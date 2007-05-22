package edu.wustl.catissuecore.deid;

import java.io.File;

import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.XMLPropertyHandler;

public class DeidReportTest extends BaseTestCase 
{
	IdentifiedSurgicalPathologyReport ispr;
	DeidReport deidReport;
	/**
	 *  Default constructor
	 */
	public DeidReportTest()
	{
		super();
	}
	/**
	 * @param name
	 * constructor which takes String as an input
	 */
	public DeidReportTest(String name)
	{
		super(name);
	}
	/**
	 * @param ispr identified surgical pathology report
	 * constructor which takes IdentifiedSurgicalPathologyReport as an input
	 */
	public DeidReportTest(IdentifiedSurgicalPathologyReport ispr)
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
			XMLPropertyHandler.init("./catissuecore-properties"+File.separator+"caTissueCore_Properties.xml");
			deidReport=new DeidReport(ispr);
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
