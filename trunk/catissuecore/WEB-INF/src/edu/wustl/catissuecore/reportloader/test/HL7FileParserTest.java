package edu.wustl.catissuecore.reportloader.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import edu.wustl.catissuecore.reportloader.HL7Parser;
import edu.wustl.catissuecore.reportloader.Parser;
import edu.wustl.catissuecore.reportloader.SiteInfoHandler;
import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.XMLPropertyHandler;


public class HL7FileParserTest extends BaseTestCase 
{
	HL7Parser parser=null;
	
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
			XMLPropertyHandler.init("./catissuecore-properties"+File.separator+"caTissueCore_Properties.xml");
			parser=new HL7Parser();
			SiteInfoHandler.init(XMLPropertyHandler.getValue("site.info.filename"));
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
			parser.parse(null);
			fail("When null filename is passed, it should throw NullPointerException");
		}catch(Exception ex)
		{
			assertTrue("File Parser throws null pointer exception successfully",true);
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
			parser.validateReportMap(null);
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
		reportMap= new HashMap();
		reportMap.put(Parser.PID, null);
		reportMap.put(Parser.OBR, "Report Info");
		reportMap.put(Parser.OBX, "Report Observations");
		try
		{
			validReport =parser.validateReportMap(reportMap);
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
		reportMap= new HashMap();
		reportMap.put(Parser.PID, "Participant Info");
		reportMap.put(Parser.OBR, "Report Info");
		reportMap.put(Parser.OBX, null);
		try
		{
			validReport =parser.validateReportMap(reportMap);
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
		reportMap.put(Parser.PID, "Participant Info");
		reportMap.put(Parser.OBR, null);
		reportMap.put(Parser.OBX, "Report Observations");
		try
		{
			validReport =parser.validateReportMap(reportMap);
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
		reportMap= new HashMap();
		reportMap.put(Parser.PID, "PID|1||953896^^^df|bjc6279701|AARON^DORIS^J||19490523|F||2|PO BOX 23117^^SAINT LOUIS^MO^631563117^United States||(314)727-2339|(314)221-3731|||||498524267|||||||||||N");
		reportMap.put(Parser.OBR, null);
		reportMap.put(Parser.OBX, "Report Observations");
		try
		{
			validReport =parser.validateReportMap(reportMap);
		}catch(Exception ex)
		{
			
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
