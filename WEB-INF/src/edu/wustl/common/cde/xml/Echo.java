/*
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.cde.xml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Echo extends DefaultHandler
{
	// member variables
	String tagName = "";
	StringBuffer textBuffer;
	private static Writer out;
	
	// xml related user objects
	XMLCDECache rootNode;
	XMLCDE node;
	XMLPermissibleValue leaf;
	
	public static void main(String[] argv)
	{
		System.out.println("Reading and displaying an XML File");
		String fileName = "CDEConfig.xml";

		// Use an instance of ourselves as the SAX event handler
		DefaultHandler handler = new Echo();
		// Use the default (non-validating) parser
		SAXParserFactory factory = SAXParserFactory.newInstance();

		try
		{
			// Set up output stream
			out = new OutputStreamWriter(System.out, "UTF8");
			// Parse the input 
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(new File(fileName), handler);

		} // try 
		catch (Throwable t)
		{
			t.printStackTrace();
		} // catch
	} // main

	public void startDocument() throws SAXException
	{
		rootNode =  new XMLCDECache(); 
		
	} // startDocument

	public void endDocument() throws SAXException
	{
		printFile();
	} // endDocument

	

	// simple name  // qualified name
	public void startElement(String namespaceURI, String sName,
								String qName, Attributes attrs)
			throws SAXException
	{
		echoText();
		String eName = sName; // element name
		if ("".equals(eName))
			eName = qName; // not namespace-aware

		tagName = eName;
		
		// to check the tag being read and set the values accordingly.
		
		// setting the CDE_CACHE
		if (tagName.equals("CDE_CACHE" ))
		{
			for (int i = 0; i < attrs.getLength(); i++)
			{
				String aName = attrs.getLocalName(i); // Attr name
				if ("".equals(aName))
					aName = attrs.getQName(i);
				
				// setting the refresh rate
				if (aName.equals("REFRESH_RATE"))
				{
					rootNode.setRefreshTime(Long.parseLong(attrs.getValue(i))); 
				} // refresh rate
	
				// setting the lazy loading
				if (aName.equals("LAZY_LOADING"))
				{
					rootNode.setLazyLoading(Boolean.getBoolean((attrs.getValue(i)))); 
				} // lazy loading
			} // for
		} // tag = cdecache
		
		// setting the cde
		if (tagName.equals("CDE" ))
		{
			node = new XMLCDE();
			for (int i = 0; i < attrs.getLength(); i++)
			{
				String aName = attrs.getLocalName(i); // Attr name
				if ("".equals(aName))
					aName = attrs.getQName(i);
				
				// setting the cache
				if (aName.equals("CACHE"))
				{
					 node.setCache(Boolean.getBoolean(attrs.getValue(i)));
				} // cache
	
				// setting the lazy loading
				if (aName.equals("LAZY_LOADING"))
				{
					node.setLazyLoading(Boolean.getBoolean(attrs.getValue(i))); 
				} // lazy loading
			} // for
		} // tag = cde

		// setting the Permissible value
		if (tagName.equals("PERMISSIBLE_VALUE" ))
		{
			leaf = new XMLPermissibleValue(); 
		} // tag = permissible_value

	} // startElement

	// simple name  // qualified name
	public void endElement(String namespaceURI, 
							String sName, String qName)
						throws SAXException
	{
		echoText();
		String eName = sName; // element name
		if ("".equals(eName))
			eName = qName; // not namespace-aware

		// Adding the PermissibleValue to the XMLCDE list
		if (eName.equals("PERMISSIBLE_VALUE" ))
		{
			node.addXmlPermissibleValue(leaf);
		} // permissiblevalue addition

		// Adding the CDE to the XMLCDECache list
		if (eName.equals("CDE" ))
		{
			rootNode.addXmlCDE(node );   
		} // cde addition 

	} // endElement



	public void characters(char buf[], int offset, int len) throws SAXException
	{
		String s = new String(buf, offset, len);
		if (textBuffer == null)
		{
			textBuffer = new StringBuffer(s);
		}
		else
		{
			textBuffer.append(s);
		}
	} // characters 

	private void echoText() throws SAXException
	{
		if (textBuffer == null)
			return;

		//---- Setting the CDE ------
		// setting the name of the cde
		if (tagName.equals("NAME"))
		{
			String s = "" + textBuffer;
			node.setName(s.trim());
		} // name of cde
		
		// setting the publicid of the cde
		if (tagName.equals("PUBLICID"))
		{
			String s = "" + textBuffer;
			node.setPublicId(s.trim());
		} // publicid of cde
		
		// ---- CDE setup Complete ----
		
		//-------------------------------------------------------
		
		// ----Setting PErmissibleValue
		
		// setting the evs terminology
		if (tagName.equals("EVS_TERMINOLOGY"))
		{
			String s = "" + textBuffer;
			leaf.setEvsTerminology(s.trim() );
		} // evs terminology of the Permissiblevalue
		
		// setting the conceptcode
		if (tagName.equals("CONCEPT_CODE"))
		{
			String s = "" + textBuffer;
			leaf.setConceptCode( s.trim() );
		} // conceptcode of the Permissiblevalue

		// setting the depth of hierarchy
		if (tagName.equals("TREE_DEPTH"))
		{
			String s = "" + textBuffer;
			leaf.setDepthOfHierarchyTree(Integer.parseInt(s.trim()) );
		} // depth of hierarchy of the Permissiblevalue

		textBuffer = null;
		tagName = "";

	} // echoText


	// methods for printing the values 
	// to be deleted
	
	private void emit(String s) throws SAXException
	{
		try
		{
			out.write(s);
			out.flush();
		} // try
		catch (IOException e)
		{
			throw new SAXException("I/O error", e);
		} // catch
	} // emit
	
	private void printFile()
	{
		try
		{
			emit("CDE_CACHE :- RefreshTime : " + rootNode.getRefreshTime());
			emit("\nCDE_CACHE :- LazyLoading : " + rootNode.isLazyLoading() );
			emit("\n--------------------\n");
			
			java.util.List l1 = rootNode.getXmlCDEs() ;
			String s = "   ";
			for(int i=0;i<l1.size();i++ )
			{
				XMLCDE obj = (XMLCDE)l1.get(i);
				emit("\n" + s + "CDE :- Cache : " + obj.isCache());
				emit("\n" + s + "CDE :- LazyLoading : " + obj.isLazyLoading());
				emit("\n" + s + "CDE :- Name : " + obj.getName());
				emit("\n" + s + "CDE :- PublicId : " + obj.getPublicId() );
				emit("\n-- -- -- -- -- -- --");
				java.util.List l2 = obj.getXmlPermissibleValues();
				for(int j=0;j<l2.size();j++ )
				{
					XMLPermissibleValue pv = (XMLPermissibleValue)l2.get(j);
					emit("\n" + s + "- - " + "Permissible Value:");
					emit("\n" + s + s + "EVSTerm : " + pv.getEvsTerminology());
					emit("\n" + s + s + "ConceptCode : " + pv.getConceptCode());
					emit("\n" + s + s + "TreeDepth : " + pv.getDepthOfHierarchyTree());
					emit("\n=====================");
				} // for 2
			} // for 1
		} // try
		catch(Exception e)
		{
			System.out.println(e);
		}
	} // printFile
} // class echo

