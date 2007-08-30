package edu.wustl.catissuecore.caties.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.caties.util.SiteInfoHandler;
import edu.wustl.catissuecore.reportloader.HL7Parser;
import edu.wustl.catissuecore.reportloader.HL7ParserUtil;
import edu.wustl.common.test.BaseTestCase;

/**
 * @author sandeep_ranade
 * JUnit test case class for HL7FileParser
 */
public class HL7FileParserTest extends BaseTestCase 
{
	public HL7FileParserTest()
	{
		super();
	}
	
	public HL7FileParserTest(String name)
	{
		super(name);
	}
	
	/**
	 * This method makes an initial set up for test cases.
	 * It initializes the HL7 parser. 
	 */
	
	protected void setUp()
	{
		try
		{
			CaTIESProperties.initBundle("caTIES");
			SiteInfoHandler.init(CaTIESProperties.getValue(CaTIESConstants.SITE_INFO_FILENAME));
		}catch(Exception ex)
		{
			
		}
	}

	/**
	 * This method tests the validation for null value in filename
	 * as input to the HL parser.
	 */
	public void testPathRptLoadForNullFileForParse()
	{
		try
		{
			HL7Parser parser=new HL7Parser();
			parser.parse(null);
			fail("When null filename is passed, it should throw NullPointerException");
		}catch(Exception ex)
		{
			assertTrue("File CaTIESConstants throws null pointer exception successfully",true);
		}
	}
	
	/**
	 * This method tests validation for null report as input to
	 * HL7parser  
	 *
	 */
	public void testPathRptLoadForNullForValidateReport()
	{
		try
		{
			HL7ParserUtil.validateReportMap(null);
			fail("When null report map is , it should throw NullPointerException");
		}catch(Exception ex)
		{
			assertTrue("Report validator throws null pointer exception successfully",true);
		}
	}
	
	/**
	 * This method tests the validation check for existance of participant
	 * information in pathology report. 
	 *
	 */
	public void testPathRptLoadForParticipantInformationExistance()
	{
		Map reportMap=null;
		boolean validReport=false;
		reportMap= new HashMap<String, Set>();
		reportMap.put(CaTIESConstants.PID, null);
		reportMap.put(CaTIESConstants.OBR, "Report Info");
		reportMap.put(CaTIESConstants.OBX, "Report Observations");
		try
		{
			validReport =HL7ParserUtil.validateReportMap(reportMap);
		}catch(Exception ex)
		{
			
		}
		if(validReport)
		{
			fail("Report is not valid wothout participant information, System should return validity=false");
		}else
		{
			assertTrue("Report validator return validity=false successfully without participant information",true);
		}
	}
	
	/**
	 * This method tests the validation check for existance of observations
	 * in pathology report. 
	 */
	public void testPathRptLoadForReportObservationsExistance()
	{
		Map reportMap=null;
		boolean validReport=false;
		reportMap= new HashMap<String, Set>();
		reportMap.put(CaTIESConstants.PID, "Participant Info");
		reportMap.put(CaTIESConstants.OBR, "Report Info");
		reportMap.put(CaTIESConstants.OBX, null);
		try
		{
			validReport =HL7ParserUtil.validateReportMap(reportMap);
		}catch(Exception ex)
		{
			
		}
		if(validReport)
		{
			fail("Report is not valid without observations section, System should return validity=false");
		}else
		{
			assertTrue("Report validator return validity=false successfully without observations",true);
		}
	}
	
	/**
	 * This method tests the validation check for existance of OBR
	 * section in pathology report. 
	 */
	public void testPathRptLoadForReportExistance()
	{
		Map reportMap=null;
		boolean validReport=false;
		reportMap= new HashMap();
		reportMap.put(CaTIESConstants.PID, "Participant Info");
		reportMap.put(CaTIESConstants.OBR, null);
		reportMap.put(CaTIESConstants.OBX, "Report Observations");
		try
		{
			validReport =HL7ParserUtil.validateReportMap(reportMap);
		}catch(Exception ex)
		{
			
		}
		if(validReport)
		{
			fail("Report is not valid without OBR section, System should return validity=false");
		}else
		{
			assertTrue("Report validator return validity=false successfully without OBR section",true);
		}
	}
	
	/**
	 * This method tests the validation check for existance of OBR
	 * site in pathology report. 
	 */
	public void testPathRptLoadForSiteExistance()
	{
		Map reportMap=null;
		boolean validReport=false;
		reportMap= new HashMap<String, Set>();
		reportMap.put(CaTIESConstants.PID, "PID|1||953896^^^df|bjc6279701|HARRY^POTTER^J||19490523|M||2|PO BOX 23117^^SAINT LOUIS^MO^631563117^United States||(123)727-2339|(123)221-3731|||||498524267|||||||||||N");
		reportMap.put(CaTIESConstants.OBR, null);
		reportMap.put(CaTIESConstants.OBX, "Report Observations");
		try
		{
			validReport =HL7ParserUtil.validateReportMap(reportMap);
		}
		catch(Exception ex)
		{
			fail("Exception thrown:" + ex.getMessage());
			ex.printStackTrace();
		}
		if(validReport)
		{
			fail("Report is not valid without site, System should return validity=false");
		}else
		{
			assertTrue("Report validator return validity=false successfully without site",true);
		}
	}
}
