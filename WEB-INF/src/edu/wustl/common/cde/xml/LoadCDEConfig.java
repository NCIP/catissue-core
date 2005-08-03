/*
 * Created on Jun 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.cde.xml;

/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import edu.wustl.common.util.logger.Logger;

//import edu.wustl.caTISSUECore.util.global.ApplicationProperties;

public class LoadCDEConfig
{

    Document document;

    public static void main(String argv[])
    {
        LoadCDEConfig obj = new LoadCDEConfig();

        System.setProperty("catissue.home", "Logs/");
        Logger.configure("ApplicationResources.properties");

        obj.getXML();
    }// main

    private void getXML()
    {

        //    String configFile = ApplicationProperties.getValue("cde.configurationfilename");
        String configFile = "d:\\prg\\xml\\CDEConfig.xml";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        // Set the validation mode to either: no validation, DTD
        // validation 
        factory.setValidating(false);


        // Optional: set various configuration options
        factory.setIgnoringComments(false);
        factory.setIgnoringElementContentWhitespace(false);
        factory.setCoalescing(false);
        // The opposite of creating entity ref nodes is expanding them inline
        factory.setExpandEntityReferences(!false);


        System.out.println(configFile);
        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(configFile));

            Element rootNode = document.getDocumentElement();
            System.out.println("Root Node : " + rootNode.getTagName());
            System.out.println("Root Node Attributes : "
                    + rootNode.hasAttributes());

            long refreshTime;
            boolean lazyLoading;

            if (rootNode.hasAttributes())
            {
                NamedNodeMap attributes = rootNode.getAttributes();

                Node refreshRate = attributes.getNamedItem("REFRESH_RATE");
                Node lazyLoad = attributes.getNamedItem("LAZY_LOADING");

                refreshTime = Long.parseLong(refreshRate.getNodeValue());
                lazyLoading = Boolean.getBoolean(lazyLoad.getNodeValue());
            } // attributes present	
            else
            {
                Logger.out.error("Root Node Not set properly.");
                throw new Exception("Root Node not set properly");
            } // no attributes
            System.out.println("\n____________________________________\n");
            
            HashMap xmlData = getNodeData(rootNode );
            System.out.println("\n____________________________________\n");
            
            printHashMap(xmlData );
            System.out.println("\n_____________ E N D _______________________\n");
            
//            List xmlCdeList = getXMLCDE(rootNode);
//            //			List xmlCDE  = null;
//
//            /**
//             * Main XML CDE Cache Object
//             */
//            XMLCDECache mainObject = new XMLCDECache(refreshTime, lazyLoading,
//                    xmlCdeList);

        } // try

        catch (SAXException sxe)
        {
            // Error generated during parsing
            Exception x = sxe;
            if (sxe.getException() != null)
                x = sxe.getException();
            x.printStackTrace();
        }
        catch (ParserConfigurationException pce)
        {
            // Parser with specified options can't be built
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            // I/O error
            ioe.printStackTrace();
        }
        catch (Exception e)
        {
            Logger.out.error(e);
        }
    } // getXML

    private List getXMLCDE(Element rootNode)
    {
        List xmlCdeList = new ArrayList();

        try
        {
            NodeList childCDENodes = rootNode.getElementsByTagName("CDE");

            for (int cnt = 0; cnt < childCDENodes.getLength(); cnt++)
            {
                Node xmlCDE = childCDENodes.item(cnt);
                boolean cdeCache;
                boolean lazyLoading;
                String name="";
                String publicId="";
                List xmlPerVal = null;

                if (xmlCDE.hasAttributes())
                {
                    NamedNodeMap cdeattr = xmlCDE.getAttributes();
                    Node cache = cdeattr.getNamedItem("CACHE");
                    Node lazyLoad = cdeattr.getNamedItem("LAZY_LOADING");
                    cdeCache = Boolean.getBoolean(cache.getNodeValue());
                    lazyLoading = Boolean.getBoolean(lazyLoad.getNodeValue());
                } // attributes
                else
                {
                    throw new Exception(
                            "Attributes of CDE Tag not set properly.");
                } // no attributes
                //---------
                if (xmlCDE.hasChildNodes())
                {
                    NodeList cdeDef = xmlCDE.getChildNodes();
                    
                    int checkType=0;
                    for (int childcnt = 0; childcnt < cdeDef.getLength(); childcnt++)
                    {
                        Node child = cdeDef.item(childcnt);

                        String tagName = child.getNodeName();
                        System.out.println(tagName + " : " + checkType  );
                        
                        if (tagName.equals("NAME"))
                        {
                            checkType = 1; 
                        } // tag = name
                        else if (tagName.equals("PUBLICID"))
                        {
                            checkType = 2; 
                        } // tag = publicid
                        else if (tagName.equals("PERMISSIBLE_VALUE"))
                        {
                            checkType = 3;
                        } // tag = permissiblevalue
                        else 
                        {
                            switch(checkType)
                            {
                                case 1:
                                    name = child.getNodeValue();
                                    Text tx = (Text)child;
                                    
                                    System.out.println("Type : " + child.getNodeName());
                                    System.out.println("Name : " + tx.getData().trim() );
                                    checkType = 0; 
                                    break;
                                    
                                case 2:
                                    publicId = child.getNodeValue();
                                    System.out.println("Public ID : " + publicId);
                                    checkType = 0; 
                                    break;
                                    
                                case 3:
                                    xmlPerVal = getXMLPermissibleValue(child);
                                    checkType = 0; 
                                    break;
                                  
                                 default :
                                     checkType = 0 ;
                            } // switch
                            
                        }
                        
                    } // for child nodes
                } // child nodes
                else
                {
                    throw new Exception("Child Nodes for CDE Tag are not set.");
                } // no child nodes
                XMLCDE cde = new XMLCDE(cdeCache, lazyLoading, name, publicId,
                        xmlPerVal);
                xmlCdeList.add(cde);
            } // for childnodes  ie : xmlcde
        } // try
        catch (Exception e)
        {
            System.out.println("Error : " + e);
        }
        return xmlCdeList;
    }// getXMLCDE

    private List getXMLPermissibleValue(Node cde)
    {
        List xmlPermissibleValue = new ArrayList();

        try
        {

        } // try
        catch (Exception exp)
        {

        } // catch

        return xmlPermissibleValue;
    } // getXMLPermissibleValue

    public HashMap getNodeData(Node root)
    {
    	HashMap hmData = new HashMap();
    	try
		{
    		if (root.hasChildNodes()) 
    		{
    			
    			for (Node child = root.getFirstChild(); child != null;
                child = child.getNextSibling()) 
    			{
    				hmData.putAll(getNodeData(child) );

    				hmData.put(child.getNodeName(),child.getNodeValue());
					System.out.println(child.getNodeName()+ " \t:\t " + child.getNodeValue());

    			} // for
    		} // if childnodes
    		else
    		{
    			if (root.hasAttributes())
    			{
    				NamedNodeMap nnm =  root.getAttributes();
    				for(int z=0;z<nnm.getLength();z++ )
    				{
    					Node nd = nnm.item( z);
    					hmData.put(nd.getNodeName(),nd.getNodeValue());
    					System.out.println(nd.getNodeName()+ " \t:\t " + nd.getNodeValue());
    				}
    			} // attr
    			hmData.put(root.getNodeName(),root.getNodeValue());
    			System.out.println(root.getLocalName()+ " \t:\t " + root.getNodeValue());
    			
    		}
    		
		} // try
        catch (Exception exp)
        {
        	System.out.println("Error: " + exp);
        } // catch
    	return hmData;
    } // getNodeData
    
    private void printHashMap(HashMap hm)
    {
    	try
		{
    		java.util.Set keySet = hm.keySet();
    		java.util.Iterator itr = keySet.iterator();
    		while(itr.hasNext())
    		{
    			Object o = itr.next(); 
    			System.out.println(o + " \t:\t " + hm.get(o ));
    		}
		} // try
        catch (Exception exp)
        {
        	System.out.println("Error: " + exp);
        } // catch

    } //printHashMap
    
}// LoadCDEConfig 

