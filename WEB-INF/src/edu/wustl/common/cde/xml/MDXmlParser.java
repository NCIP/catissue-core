/*
 * Created on Jun 17, 2005
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
//JAXP packages
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.w3c.dom.*;

import java.io.*;
import java.util.*;

public class MDXmlParser {
    /** All output will use this encoding */
    static final String outputEncoding = "UTF-8";

    /** Output goes here */
    private PrintWriter out;

    /** Indent level */
    private int indent = 0;

    /** Indentation will be in multiples of basicIndent  */
    private final String basicIndent = "  ";

    // stores data of a particular node
    private List dataList;
    
    // will store the tagname and the datalist
    private HashMap dataMap = new HashMap();
    
    // index of the tag
    int tagIndex = 0;
    private String tagType="";
    
    MDXmlParser(PrintWriter out) {
        this.out = out;
    }

    /**
     * Echo common attributes of a DOM2 Node and terminate output with an
     * EOL character.
     */
    private void printlnCommon(Node n,int parentid) {
               
        if (n.getNodeName().equals("#text") && (!(n.getNodeValue().trim().equals("")) ))
        {
//        	out.println("\n\t\t " + tagName + " : " + n.getNodeValue()+ "\n");
        	
//        out.println(tagName + " : " + tagIndex + " : " + n.getNodeValue() + " : " + tagType + " : " + parentid);
        
        dataList = new ArrayList();        	
        dataList.add(tagName); 
        dataList.add(new Integer(tagIndex));
        dataList.add(n.getNodeValue());
        dataList.add(tagType);
        dataList.add(new Integer(parentid) );  
        dataMap.put(tagName + ": "+tagIndex + " : "+ new Date().getTime(),dataList); 
        tagName="";
        }
        else if (n.getNodeName().equals("#text") && ((n.getNodeValue().trim().equals("")) ))
        {
        	//out.println("\n\t\t " + tagName + " : " + n.getNodeValue()+ "\n");
        	tagName="";
        }
        else
        {
        	tagName = n.getNodeName();

            dataList = new ArrayList();        	
            dataList.add(tagName); 
            dataList.add(new Integer(tagIndex));
            dataList.add(n.getNodeValue());
            dataList.add(tagType);
            dataList.add(new Integer(parentid));  
            dataMap.put(tagName + ":"+tagIndex,dataList); 

//	       	out.println(tagName + " : " + tagIndex + " : " + n.getNodeValue() + " : " + tagType + " : " + parentid);
        } // else

    }

    /**
     * Indent to the current level in multiples of basicIndent
     */
    private void outputIndentation() {
        for (int i = 0; i < indent; i++) {
            out.print(basicIndent);
        }
    }

    /**
     * Recursive routine to print out DOM tree nodes
     */
    String tagName;
    String tagVal;
    private void echo(Node n,int tpid) {
        // Indent to the current level before printing anything
        outputIndentation();
        int pid=tpid;
        int type = n.getNodeType();
        
        tagIndex++; 
        int tmpid = tagIndex ;
        switch (type) {
        case Node.ATTRIBUTE_NODE:
//            out.print("ATTR:");
        	tagType = "ATTR";
            printlnCommon(n,pid);
            break;
        case Node.CDATA_SECTION_NODE:
//            out.print("CDATA:");
        	tagType ="CDATA"; 
        	printlnCommon(n,pid);
            break;
        case Node.COMMENT_NODE:
//            out.print("COMM:");
        	tagType = "COMM";
            printlnCommon(n,pid);
            break;
        case Node.DOCUMENT_FRAGMENT_NODE:
//            out.print("DOC_FRAG:");
        	tagType = "DOC_FRAG"; 
            printlnCommon(n,pid);
            break;
        case Node.DOCUMENT_NODE:
//            out.print("DOC:");
        	tagType = "DOC"; 
            printlnCommon(n,pid);
            break;
        case Node.DOCUMENT_TYPE_NODE:
//            out.print("DOC_TYPE:");
        	tagType = "DOC_TYPE"; 
            printlnCommon(n,pid);

            // Print entities if any
            NamedNodeMap nodeMap = ((DocumentType)n).getEntities();
            indent += 2;
            for (int i = 0; i < nodeMap.getLength(); i++) {
                Entity entity = (Entity)nodeMap.item(i);
                echo(entity,pid);
            }
            indent -= 2;
            break;
        case Node.ELEMENT_NODE:
//            out.print("ELEM:");
        	tagType = "ELEM"; 	
        	tagName =n.getNodeName() ;
	        printlnCommon(n,pid);
            pid = tmpid ;
            // Print attributes if any.  Note: element attributes are not
            // children of ELEMENT_NODEs but are properties of their
            // associated ELEMENT_NODE.  For this reason, they are printed
            // with 2x the indent level to indicate this.
            NamedNodeMap atts = n.getAttributes();
            indent += 2;
            for (int i = 0; i < atts.getLength(); i++) {
                Node att = atts.item(i);
//                out.println("\n \tParent ID : " + pid + " : ELEM-ATTR\n");
                echo(att,pid);
            }
            indent -= 2;
            break;
        case Node.ENTITY_NODE:
//            out.print("ENT:");
        	tagType = "ENT"; 
            printlnCommon(n,pid);
            break;
        case Node.ENTITY_REFERENCE_NODE:
//            out.print("ENT_REF:");
        	tagType = "ENT_REF"; 
            printlnCommon(n,pid);
            break;
        case Node.NOTATION_NODE:
//            out.print("NOTATION:");
        	tagType = "NOTATION"; 
            printlnCommon(n,pid);
            break;
        case Node.PROCESSING_INSTRUCTION_NODE:
//            out.print("PROC_INST:");
        	tagType = "PROC_INST";
            printlnCommon(n,pid);
            break;
        case Node.TEXT_NODE:
           // out.print("TEXT:");
        	tagIndex--; 
        	printlnCommon(n,pid);
            break;
        default:
//            out.print("UNSUPPORTED NODE: " + type);
        	tagType = "UNSUPPORTED NODE";
            printlnCommon(n,pid);
            break;
        }

        // Print children if any
        indent++;
        pid = tmpid ;
        for (Node child = n.getFirstChild(); child != null;
             child = child.getNextSibling()) {
  //      	out.println("\n\t " + "Parent Id : " + pid + " : Child Node\n");
            echo(child,pid);
        }
        indent--;
    }

    private static void usage() {
        System.err.println("Usage: MDXmlParser [-options] <file.xml>");
        System.err.println("       -dtd = DTD validation");
        System.err.println("       -ws = do not create element content whitespace nodes");
        System.err.println("       -co[mments] = do not create comment nodes");
        System.err.println("       -cd[ata] = put CDATA into Text nodes");
        System.err.println("       -e[ntity-ref] = create EntityReference nodes");
        System.err.println("       -usage or -help = this message");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        String filename = "d:\\prg\\xml\\CDEConfig.xml";
        boolean dtdValidate = false;

        boolean ignoreWhitespace = false;
        boolean ignoreComments = false;
        boolean putCDATAIntoText = false;
        boolean createEntityRefs = false;

        // Step 1: create a DocumentBuilderFactory and configure it
        DocumentBuilderFactory dbf =
            DocumentBuilderFactory.newInstance();

        // Set namespaceAware to true to get a DOM Level 2 tree with nodes
        // containing namesapce information.  This is necessary because the
        // default value from JAXP 1.0 was defined to be false.
        dbf.setNamespaceAware(true);

        // Set the validation mode to either: no validation, DTD
        // validation 
        dbf.setValidating(dtdValidate);


        // Optional: set various configuration options
        dbf.setIgnoringComments(ignoreComments);
        dbf.setIgnoringElementContentWhitespace(ignoreWhitespace);
        dbf.setCoalescing(putCDATAIntoText);
        // The opposite of creating entity ref nodes is expanding them inline
        dbf.setExpandEntityReferences(!createEntityRefs);

        // Step 2: create a DocumentBuilder that satisfies the constraints
        // specified by the DocumentBuilderFactory
        DocumentBuilder db = dbf.newDocumentBuilder();

        // Set an ErrorHandler before parsing
        OutputStreamWriter errorWriter =
            new OutputStreamWriter(System.err, outputEncoding);
        db.setErrorHandler(
            new MyErrorHandler(new PrintWriter(errorWriter, true)));

        // Step 3: parse the input file
        Document doc = db.parse(new File(filename));

        // Print out the DOM tree
        OutputStreamWriter outWriter =
            new OutputStreamWriter(System.out, outputEncoding);
        MDXmlParser obj = new MDXmlParser(new PrintWriter(outWriter, true));
        obj.echo(doc,0);
        obj.printMap() ;
    }

    private void printMap()
    {
    	try
		{
    		
    		Set ks = dataMap.keySet();
    		Iterator itr = ks.iterator() ;
    		while(itr.hasNext() )
    		{
    			Object o = itr.next() ;
    			Object res = dataMap.get(o );
    			List l = (List)res;
    			
   				System.out.println(o + " : " );
   				for(int z=0;z<l.size();z++ )
   				{
   					System.out.print(l.get(z ) + " | ");
   				}
   				System.out.println("\n------------");
    		}
    		
		}
    	catch(Exception e)
		{
    		
		}
    } // printMap
    
    // Error handler to report errors and warnings
    private static class MyErrorHandler implements ErrorHandler {
        /** Error handler output goes here */
        private PrintWriter out;

        MyErrorHandler(PrintWriter out) {
            this.out = out;
        }

        /**
         * Returns a string describing parse exception details
         */
        private String getParseExceptionInfo(SAXParseException spe) {
            String systemId = spe.getSystemId();
            if (systemId == null) {
                systemId = "null";
            }
            String info = "URI=" + systemId +
                " Line=" + spe.getLineNumber() +
                ": " + spe.getMessage();
            return info;
        }

        // The following methods are standard SAX ErrorHandler methods.
        // See SAX documentation for more info.

        public void warning(SAXParseException spe) throws SAXException {
            out.println("Warning: " + getParseExceptionInfo(spe));
        }
        
        public void error(SAXParseException spe) throws SAXException {
            String message = "Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }

        public void fatalError(SAXParseException spe) throws SAXException {
            String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
    }
}

