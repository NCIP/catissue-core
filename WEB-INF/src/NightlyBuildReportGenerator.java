/**
 * <p>Title:NightlyBuildReportGenerator Class>
 * <p>Description:This class parses from CaTissueJMeterTests.xml and TESTS-TestSuites.xml file using DOM parser.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author kalpana thakur
 * @version 1.00
 * Created on Dec 17 ,2007
 */


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
	public static String JUNIT_TEST_MYSQL_FRESH_FILE = "";
	public static String JUNIT_TEST_MYSQL_UPGRADE_FILE = ""; 
	public static String JUNIT_TEST_ORACLE_FRESH_FILE = "";
	public static String JUNIT_TEST_ORACLE_UPGRADE_FILE = "";
	public static String JMETER_TEST_MYSQL_FRESH_FILE = "";
	public static String JMETER_TEST_MYSQL_UPGRADE_FILE = ""; 
	public static String JMETER_TEST_ORACLE_FRESH_FILE = "";
	public static String JMETER_TEST_ORACLE_UPGRADE_FILE = "";
	public static String JMETER_TEST_FILE = "";
	public static String MAIL_SETTING_PROPERTY_FILE ="";
	public static String KEY_EMAIL_MESSAGE = "email.message";
	public static String DATABASE_ORACLE = "Oracle";
	public static String DATABASE_MYSQL = "MySQL";
	public static String TestResult="./XmlReport/TestResult.txt" ;
	public static String date="";
	static StringBuffer nightlyBuildReport=null;
	public static boolean FileExist=true;

	public static void main(String[] args) throws Exception
	{
		
		if (args.length>0)
		{
			
			JUNIT_TEST_MYSQL_FRESH_FILE=args[0];
			JUNIT_TEST_MYSQL_UPGRADE_FILE=args[1];
			JUNIT_TEST_ORACLE_FRESH_FILE=args[2];
			JUNIT_TEST_ORACLE_UPGRADE_FILE=args[3];
			JMETER_TEST_MYSQL_FRESH_FILE=args[4];
			JMETER_TEST_MYSQL_UPGRADE_FILE=args[5];
			JMETER_TEST_ORACLE_FRESH_FILE=args[6];
			JMETER_TEST_ORACLE_UPGRADE_FILE=args[7];
					
		}
		nightlyBuildReport = new StringBuffer("");
				
		FileOutputStream  fileOutputStream = new FileOutputStream(TestResult);
		fileOutputStream.flush();
		
		NightlyBuildReportGenerator.init(JUNIT_TEST_MYSQL_FRESH_FILE);
		NightlyBuildReportGenerator.getJUnitTestResults(fileOutputStream);
		NightlyBuildReportGenerator.init(JUNIT_TEST_MYSQL_UPGRADE_FILE);
		NightlyBuildReportGenerator.getJUnitTestResults(fileOutputStream);
		NightlyBuildReportGenerator.init(JUNIT_TEST_ORACLE_FRESH_FILE);
		NightlyBuildReportGenerator.getJUnitTestResults(fileOutputStream);
		NightlyBuildReportGenerator.init(JUNIT_TEST_ORACLE_UPGRADE_FILE);
		NightlyBuildReportGenerator.getJUnitTestResults(fileOutputStream);
		
		NightlyBuildReportGenerator.init(JMETER_TEST_MYSQL_FRESH_FILE);
		NightlyBuildReportGenerator.getJMeterTestResults(fileOutputStream);
		NightlyBuildReportGenerator.init(JMETER_TEST_MYSQL_UPGRADE_FILE);
		NightlyBuildReportGenerator.getJMeterTestResults(fileOutputStream);
		NightlyBuildReportGenerator.init(JMETER_TEST_ORACLE_FRESH_FILE);
		NightlyBuildReportGenerator.getJMeterTestResults(fileOutputStream);
		NightlyBuildReportGenerator.init(JMETER_TEST_ORACLE_UPGRADE_FILE);
		NightlyBuildReportGenerator.getJMeterTestResults(fileOutputStream);
				
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
				// throws SAXException,IOException,IllegalArgumentException(if path is null
			}
		}
		catch (SAXException e)
		{
			//NightlyBuildReportGenerator.logger.error(e.getMessage(),e);
			e.printStackTrace();
			throw e;
		}
		catch (IOException e)
		{
			//NightlyBuildReportGenerator.logger.error(e.getMessage(),e);
			e.printStackTrace();
			FileExist=false;
		}
		catch (ParserConfigurationException e)
		{
			//NightlyBuildReportGenerator.logger.error(e.getMessage(),e);
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

	public static void getJUnitTestResults(FileOutputStream fileOutputStream)throws IOException
	{
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
		String name="JUnitTest"+",";
		name=name+TOTAL_NO_OF_TESTS+","+TOTAL_NO_OF_PASS+","+TotalFailures+","+date+","+"-"+"\r\n";
		fileOutputStream.write(name.getBytes());
		}
		else // If XML file does not exists
		{
			String name="JUnitTest"+","+"-"+","+"-"+","+"-"+","+"-"+","+"XML Report File does not exists"+"\r\n";
			fileOutputStream.write(name.getBytes());
		}
		
	}
	
	public static void getJMeterTestResults(FileOutputStream fileOutputStream)throws IOException
	{
		if(FileExist)
		{
		int TOTAL_NO_OF_PASS=0;
		// it gives the rootNode of the xml file
		Element root = document.getDocumentElement();
		int totalNoOfTest=0;
		int totalNoOfFailures=0,totalNoOfPasses=0;
				
		List listOfFailedTestCases = new ArrayList();
		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			Node child = children.item(i);
					
			
			if(child.getNodeName().equals("httpSample"))//iterate total tests "httpSample" tag specifies the test
			{
				NamedNodeMap attributeMap =child.getAttributes();
				
				//to retrieve the total no of failures and count of test cases.
				if(attributeMap!=null)
				{
					for(int l=0;l<attributeMap.getLength();l++)
					{
						Node attributeNode =attributeMap.item(l);
						if(attributeNode.getNodeName().equals("s"))//this is to retrieve the name of the test case which is failed.
						{
							if(attributeNode.getNodeValue().equals("true"))
								totalNoOfPasses++;
							if(attributeNode.getNodeValue().equals("false"))
								totalNoOfFailures++;
							
						}
															
									
					}
				}	
				
				totalNoOfTest++;
				NodeList subChildNodes = child.getChildNodes();
				/*for (int j = 0; j < subChildNodes.getLength(); j++)
				{
					Node subchildNode = subChildNodes.item(j);
					if(subchildNode.getNodeName().equals("assertionResult"))
					{
						NodeList subChildChildNodes = subchildNode.getChildNodes();
						for(int k=0;k<subChildChildNodes.getLength();k++)
						{
							Node subChildChildNode = subChildChildNodes.item(k);
							if(subChildChildNode.getNodeName().equals("failure"))//Test failed if the value of failure tag is true.
							{
								
								if(subChildChildNode.getFirstChild().getNodeValue().equals("true"))
								{
									totalNoOfFailures++;
									NamedNodeMap attributeMap =child.getAttributes();
									
									//to retrieve the total no of failures and count of test cases.
									if(attributeMap!=null)
									{
										for(int l=0;l<attributeMap.getLength();l++)
										{
											Node attributeNode =attributeMap.item(l);
											if(attributeNode.getNodeName().equals("lb"))//this is to retrieve the name of the test case which is failed.
											{
												listOfFailedTestCases.add(totalNoOfFailures+"."+attributeNode.getNodeValue());
												
											}
																				
														
										}
									}	
									
								}	
							}// end of the failure
							
							
						}
					}
					
				}	*/
				
			}
		}
		
		
		nightlyBuildReport.append("\n\n\tFailed Jmeter Tests :  "+totalNoOfFailures+"/"+totalNoOfTest);
		System.out.println("----------------------------------------------------\n\nFailed Jmeter Tests !!!!!!! ->     "+totalNoOfFailures+"/"+totalNoOfTest);
		
	
			
			// Add pass fail result to Result file 
			System.out.println("Total No of Pass->"+totalNoOfPasses);
			System.out.println("Total No of Failure->"+totalNoOfFailures);
			TOTAL_NO_OF_PASS = totalNoOfTest - totalNoOfFailures;
			String name="JMeterTest"+",";
			name=name+totalNoOfTest+","+totalNoOfPasses+","+totalNoOfFailures+","+date+","+"-"+"\r\n";
			fileOutputStream.write(name.getBytes());
		}
		else // If XML file does not exists
		{
			String name="JMeterTest"+","+"-"+","+"-"+","+"-"+","+"-"+","+"XML Report File does not exists"+"\r\n";
			fileOutputStream.write(name.getBytes());
		}
			
	}
	
		
}