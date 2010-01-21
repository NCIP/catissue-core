/**
 * <p>Title:NightlyBuildReportGenerator Class>
 * <p>Description:This class parses from CaTissueJMeterTests.xml and TESTS-TestSuites.xml file using DOM parser.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author kalpana thakur
 * @version 1.00
 * Created on Dec 17 ,2007
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class used to generate nightly build report
 * 
 * @author kalpana_thakur
 */
public class NightlyBuildReportGenerator
{
	//private static final Logger logger = Logger.getCommonLogger(NightlyBuildReportGenerator.class);
	private static Document document = null;
	public static String TOTAL_NO_OF_FAILURES ="0";
	public static String TOTAL_NO_OF_TESTS ="0";
	public static String JUNIT_API_MYSQL_FRESH_FILE = "";
	public static String JUNIT_API_MYSQL_UPGRADE_FILE = ""; 
	public static String JUNIT_API_ORACLE_FRESH_FILE = "";
	public static String JUNIT_API_ORACLE_UPGRADE_FILE = "";
	public static String JUNIT_STRUTS_MYSQL_FRESH_FILE = "";
	public static String JUNIT_STRUTS_MYSQL_UPGRADE_FILE = ""; 
	public static String JUNIT_STRUTS_ORACLE_FRESH_FILE = "";
	public static String JUNIT_STRUTS_ORACLE_UPGRADE_FILE = "";
	
	
	public static String JUNIT_API_MYSQL_FRESH_REPORT = "mysql_fresh_report";
	public static String JUNIT_API_MYSQL_UPGRADE_REPORT = "mysql_upgrade_report"; 
	public static String JUNIT_API_ORACLE_FRESH_REPORT = "oracle_fresh_report";
	public static String JUNIT_API_ORACLE_UPGRADE_REPORT = "oracle_upgrade_report";
	public static String JUNIT_STRUTS_MYSQL_FRESH_REPORT = "mysql_fresh_struts_report";
	public static String JUNIT_STRUTS_MYSQL_UPGRADE_REPORT = "mysql_upgrade_struts_report"; 
	public static String JUNIT_STRUTS_ORACLE_FRESH_REPORT = "oracle_fresh_struts_report";
	public static String JUNIT_STRUTS_ORACLE_UPGRADE_REPORT = "oracle_upgrade_struts_report";
	
	//public static String JMETER_TEST_FILE = "";
	public static String MAIL_SETTING_PROPERTY_FILE ="";
	public static String KEY_EMAIL_MESSAGE = "email.message";
	public static String DATABASE_ORACLE = "Oracle";
	public static String DATABASE_MYSQL = "MySQL";
	public static String TestResult="./XmlReport/TestResult.htm" ;
	public static String TestResultHtml="./TestResult.htm" ;
	public static String date="";
	static StringBuffer nightlyBuildReport=null;
	public static boolean FileExist=true;

	public static void main(String[] args) throws Exception
	{
		
		if (args.length>0)
		{
			
			JUNIT_API_MYSQL_FRESH_FILE=args[0];
			JUNIT_API_MYSQL_UPGRADE_FILE=args[1];
			JUNIT_API_ORACLE_FRESH_FILE=args[2];
			JUNIT_API_ORACLE_UPGRADE_FILE=args[3];
			JUNIT_STRUTS_MYSQL_FRESH_FILE=args[4];
			JUNIT_STRUTS_MYSQL_UPGRADE_FILE=args[5];
			JUNIT_STRUTS_ORACLE_FRESH_FILE=args[6];
			JUNIT_STRUTS_ORACLE_UPGRADE_FILE=args[7];
					
		}
		nightlyBuildReport = new StringBuffer();
		String os = "win_";
		String operatingSystem = System.getProperty("os.name");
		if(operatingSystem.equalsIgnoreCase("Linux"))
		{
			os = "linux_";
		}
		
		FileOutputStream  fileOutputStream = new FileOutputStream(TestResult);
//		String header = "Type,Scenario,Total,Pass,Fail,Last Run Date,Comment\n";
//		fileOutputStream.write(header.getBytes());
		fileOutputStream.flush();
		List<String[]> l = new ArrayList<String[]>();
		String scenarion[] = null;
		NightlyBuildReportGenerator.init(JUNIT_STRUTS_MYSQL_FRESH_FILE);
		String reportDir = null;
		String anchorStart = "<a href=\"";
		String anchorend = "</a>";
		
		/**
		 * MySQL FRESH SCENARIO
		 */
		reportDir = os +JUNIT_STRUTS_MYSQL_FRESH_REPORT + "/Html_reports/index.html\">";
		scenarion = NightlyBuildReportGenerator.getJUnitTestResults("MySQL Fresh",anchorStart+reportDir+operatingSystem+" Struts test cases"+anchorend);
		l.add(scenarion);
		
		reportDir = os +JUNIT_API_MYSQL_FRESH_REPORT + "/Html_reports/index.html\">";
		NightlyBuildReportGenerator.init(JUNIT_API_MYSQL_FRESH_FILE);
		scenarion  = NightlyBuildReportGenerator.getJUnitTestResults("MySQL Fres",anchorStart+reportDir+operatingSystem+" API test cases"+anchorend);
		l.add(scenarion);
		
		/**
		 * MYSQL UPGRADE
		 */
		reportDir = os +JUNIT_STRUTS_MYSQL_UPGRADE_REPORT + "/Html_reports/index.html\">";
		NightlyBuildReportGenerator.init(JUNIT_STRUTS_MYSQL_UPGRADE_FILE);
		scenarion = NightlyBuildReportGenerator.getJUnitTestResults("MySQL Upgrade",anchorStart+reportDir+operatingSystem+" Struts test cases"+anchorend);
		l.add(scenarion);
		
		reportDir = os +JUNIT_API_MYSQL_UPGRADE_REPORT + "/Html_reports/index.html\">";
		NightlyBuildReportGenerator.init(JUNIT_API_MYSQL_UPGRADE_FILE);
		scenarion = NightlyBuildReportGenerator.getJUnitTestResults("MySQL Upgrade",anchorStart+reportDir+operatingSystem+" API test cases"+anchorend);
		l.add(scenarion);

		/**
		 * ORACLE FRESH
		 */
		reportDir = os +JUNIT_STRUTS_ORACLE_FRESH_REPORT + "/Html_reports/index.html\">";
		NightlyBuildReportGenerator.init(JUNIT_STRUTS_ORACLE_FRESH_FILE);
		scenarion = NightlyBuildReportGenerator.getJUnitTestResults("Oracle Fresh",anchorStart+reportDir+operatingSystem+" Struts test cases"+anchorend);
		l.add(scenarion);
		
		reportDir = os +JUNIT_API_ORACLE_FRESH_REPORT + "/Html_reports/index.html\">";
		NightlyBuildReportGenerator.init(JUNIT_API_ORACLE_FRESH_FILE);
		scenarion = NightlyBuildReportGenerator.getJUnitTestResults("Oracle Fresh",anchorStart+reportDir+operatingSystem+" API test cases"+anchorend);
		l.add(scenarion);

		/**
		 * ORACLE UPGRADE
		 */
		reportDir = os +JUNIT_STRUTS_ORACLE_UPGRADE_REPORT + "/Html_reports/index.html\">";
		NightlyBuildReportGenerator.init(JUNIT_STRUTS_ORACLE_UPGRADE_FILE);
		scenarion = NightlyBuildReportGenerator.getJUnitTestResults("Oracle Upgrade",anchorStart+reportDir+operatingSystem+" Struts test cases"+anchorend);
		l.add(scenarion);
		
		reportDir = os +JUNIT_API_ORACLE_UPGRADE_REPORT + "/Html_reports/index.html\">";
		NightlyBuildReportGenerator.init(JUNIT_API_ORACLE_UPGRADE_FILE);
		scenarion = NightlyBuildReportGenerator.getJUnitTestResults("Oracle Upgrade",anchorStart+reportDir+operatingSystem+" API test cases"+anchorend);
		l.add(scenarion);
		StringBuffer buf = new StringBuffer();
		
		FileReader f = new FileReader(TestResultHtml);
		BufferedReader reader = new BufferedReader(f);
		String line=null;
		while((line = reader.readLine())!=null)
		{				
				buf.append(line);
		}
		StringBuffer valueBuff = new StringBuffer();
		String tr = "<tr class=\"tabletd2\">\n";
		String td = "<td align=\"left\">\n";
		for(int i=0;i<l.size();i++)
		{
			String values[] = l.get(i);
			
			valueBuff.append(tr);
			for(int j=0;j<values.length;j++)
			{
				valueBuff.append(td);
				valueBuff.append(values[j]+"\n");
				valueBuff.append("</td>\n");
				
			}
			valueBuff.append("</tr>\n");
		}
		int index = buf.indexOf("REPLACE");
		System.out.println(valueBuff.toString());
		buf.replace(index, index+7, valueBuff.toString());
		fileOutputStream.write(buf.toString().getBytes());
		fileOutputStream.close();
				
	}

	public static void init(String path) throws Exception
	{
		FileExist=true;
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();// throws
			// ParserConfigurationException
			if (path != null)
			{
				document = dbuilder.parse(path);
				File fin=new File(path);
				Date d=new Date(fin.lastModified());
				java.text.SimpleDateFormat format= new java.text.SimpleDateFormat("MM/dd/yyyy");
				date=format.format(d);
			}
		}
		catch (SAXException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			FileExist=false;
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * <p>
	 * Description:This method takes the property name as String argument and
	 * returns the properties value as String. Put the xml file in the path as
	 * you will provide the path
	 * </p>
	 */

	public static String[] getJUnitTestResults(String testScenario,String testCaseType)throws IOException
	{
		String columns[] = new String[7];
		columns[0] = testScenario;
		columns[1] = testCaseType;
		if(FileExist)
		{
			int TOTAL_NO_OF_PASS=0;
			// it gives the rootNode of the xml file
			Element root = document.getDocumentElement();
			int totalNoOfFailures=0,TotalFailures=0; 
			String TotalErrors="0";
			
			
			NodeList children = root.getChildNodes();
			for (int i = 0; i < children.getLength(); i++)
			{
				Node child = children.item(i);
				NamedNodeMap attributeMap =child.getAttributes();
				
				//to retrieve the total no of failures and count of test cases.
				if(attributeMap!=null && child.getNodeName().equals("testsuite"))
				{
					for(int k=0;k<attributeMap.getLength();k++)
					{
						Node attributeNode =attributeMap.item(k);
						if(attributeNode.getNodeName().equals("failures"))
						{
							TOTAL_NO_OF_FAILURES=(String)attributeNode.getNodeValue();
						
						}
						if(attributeNode.getNodeName().equals("errors"))
						{
							TotalErrors=(String)attributeNode.getNodeValue();
						
						}
						if(attributeNode.getNodeName().equals("tests"))
						{
							TOTAL_NO_OF_TESTS=(String)attributeNode.getNodeValue();
							nightlyBuildReport.append("\n\tFailed Junit Testcases :  "+TOTAL_NO_OF_FAILURES+"/"+TOTAL_NO_OF_TESTS );
							System.out.println("\nFailed Junit Testcases: !!!!!!!! ---->  "+TOTAL_NO_OF_FAILURES+"/"+TOTAL_NO_OF_TESTS);
						}
										
					}
				}	
			
				
				NodeList subChildNodes = child.getChildNodes();
				
				
					//Logger.out.debug("subchildNodes : "+subChildNodes.getLength()); 
					for (int j = 0; j < subChildNodes.getLength(); j++)
					{
						Node subchildNode = subChildNodes.item(j);
						NodeList subChildChildNodes = subchildNode.getChildNodes();
						//if(subChildChildNodes.getLength()>0)
						//{
							for(int iter =0;iter<subChildChildNodes.getLength();iter++)
							{	
								
								Node subchildChildNode = subChildChildNodes.item(iter);
								if(subchildChildNode!=null && subchildChildNode.getNodeName().equals("failure"))
								{	
									NamedNodeMap childAttributeMap =subchildNode.getAttributes();
									
									//to retrieve the total no of failures and count of test cases.
									if(childAttributeMap!=null)
									{
										for(int k=0;k<childAttributeMap.getLength();k++)
										{
											Node attributeNode =childAttributeMap.item(k);
											if(attributeNode.getNodeName().equals("name"))
											{
												totalNoOfFailures++;
												nightlyBuildReport.append("\n\t\t"+totalNoOfFailures+"."+(String)attributeNode.getNodeValue());
											  System.out.println("Failed Test case --->"+totalNoOfFailures+"."+(String)attributeNode.getNodeValue());
											}
											
														
										}
									}
								}
							}
							
						//}
										
					}
				
			}
			TotalFailures = Integer.parseInt(TOTAL_NO_OF_FAILURES)+ Integer.parseInt(TotalErrors);
			System.out.println("TotalFailures"+TotalFailures);
			// Add pass fail result to Result file 
			TOTAL_NO_OF_PASS=Integer.parseInt(TOTAL_NO_OF_TESTS) - TotalFailures;
			//fileOutputStream.write(name.getBytes());
			columns[2] = TOTAL_NO_OF_TESTS;
			columns[3] = ""+TOTAL_NO_OF_PASS;
			columns[4] = ""+TotalFailures;
			columns[5] = date;
			columns[6] = "-";
		}
		else // If XML file does not exists
		{

			//fileOutputStream.write(name.getBytes());
			columns[2] = "-";
			columns[3] = "-";
			columns[4] = "-";
			columns[5] = "-";
			columns[6] = "XML Report File does not exists";
		}
		return columns;
		
	}
}