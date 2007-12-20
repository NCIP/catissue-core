/**
 * <p>Title:NightlyBuildReportGenerator Class>
 * <p>Description:This class parses from CaTissueJMeterTests.xml and TESTS-TestSuites.xml file using DOM parser.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author kalpana thakur
 * @version 1.00
 * Created on Dec 17 ,2007
 */


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.PropertyConfigurator;
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

	private static Document document = null;
	public static String TOTAL_NO_OF_FAILURES =null;
	public static String TOTAL_NO_OF_TESTS =null;
	public static String JMETER_TEST_FILE = "";
	public static String JUNIT_TEST_FILE = ""; 
	public static String MAIL_SETTING_PROPERTY_FILE ="";
	public static String KEY_EMAIL_MESSAGE = "email.message";
	public static String DATABASE_ORACLE = "Oracle";
	public static String DATABASE_MYSQL = "MySQL";
	
	static StringBuffer nightlyBuildReport=null;
	

	public static void main(String[] args) throws Exception
	{
		
		if (args.length>0)
		{
			JMETER_TEST_FILE=args[0];
			JUNIT_TEST_FILE=args[1];
			MAIL_SETTING_PROPERTY_FILE=args[2];
					
		}
		nightlyBuildReport = new StringBuffer("");
		Properties property =new Properties();
		FileInputStream fileInputStream = new FileInputStream(MAIL_SETTING_PROPERTY_FILE);
		property.load(fileInputStream);
		
		//to update the report
		if(property.getProperty(KEY_EMAIL_MESSAGE).equals("") || property.getProperty(KEY_EMAIL_MESSAGE).contains(DATABASE_ORACLE))
		{
			nightlyBuildReport.append(DATABASE_MYSQL+"\n\n");
			
		}
		else
		{
			nightlyBuildReport.append(property.getProperty(KEY_EMAIL_MESSAGE));
			nightlyBuildReport.append("\n\n"+DATABASE_ORACLE+"\n\n");
		}
		
		NightlyBuildReportGenerator.init(JUNIT_TEST_FILE);
		NightlyBuildReportGenerator.getJUnitTestResults();
		NightlyBuildReportGenerator.init(JMETER_TEST_FILE);
		NightlyBuildReportGenerator.getJMeterTestResults();
	
		property.setProperty(KEY_EMAIL_MESSAGE, nightlyBuildReport.toString());
		fileInputStream.close();	
		FileOutputStream fileOutputStream = new FileOutputStream(MAIL_SETTING_PROPERTY_FILE);
		property.store(fileOutputStream, "");
		
		
	}

	public static void init(String path) throws Exception
	{
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();// throws
			// ParserConfigurationException
			if (path != null)
			{
				document = dbuilder.parse(path);
				// throws SAXException,IOException,IllegalArgumentException(if path is null
			}
		}
		catch (SAXException e)
		{
			throw e;
		}
		catch (IOException e)
		{
			throw e;
		}
		catch (ParserConfigurationException e)
		{
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

	public static void getJUnitTestResults()
	{
		
		
		// it gives the rootNode of the xml file
		Element root = document.getDocumentElement();
		int totalNoOfFailures=0;
		
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
		
	}
	
	public static void getJMeterTestResults()
	{

		// it gives the rootNode of the xml file
		Element root = document.getDocumentElement();
		int totalNoOfTest=0;
		int totalNoOfFailures=0;
		List listOfFailedTestCases = new ArrayList();
		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			Node child = children.item(i);
			if(child.getNodeName().equals("httpSample"))//iterate total tests "httpSample" tag specifies the test
			{
				totalNoOfTest++;
				NodeList subChildNodes = child.getChildNodes();
				for (int j = 0; j < subChildNodes.getLength(); j++)
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
							}
						}
					}
					
				}	
				
			}
		}
		
		
		nightlyBuildReport.append("\n\n\tFailed Jmeter Tests :  "+totalNoOfFailures+"/"+totalNoOfTest);
		System.out.println("----------------------------------------------------\n\nFailed Jmeter Tests !!!!!!! ->     "+totalNoOfFailures+"/"+totalNoOfTest);
		
		
			if(listOfFailedTestCases!=null && listOfFailedTestCases.size()>0)
			{
				Iterator listIter = listOfFailedTestCases.iterator();
				while(listIter.hasNext())
				{
					String nextTestCase = (String)listIter.next();
					nightlyBuildReport.append("\n\t\t"+nextTestCase);
					System.out.println("Failed Test case ---->"+nextTestCase);
				}
			}
			
	}
	
	
	
	
}