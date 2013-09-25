/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore;

import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.xmi.XmiFileType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
  *  XMIParser
  *  Parses XMI into a Domain Model
  * 
  * @author Patrick McConnell
  * @author David Ervin
  * 
  * @created Oct 22, 2007 10:19:59 AM
  * @version $Id: XMIParser.java,v 1.5 2008-04-22 19:41:23 dervin Exp $
 */
public class XMIParser {
    // regex that matches variations on the "value domain" package name for exclusion
    public static final String DEFAULT_PACKAGE_EXCLUDE_REGEX = ".*?[V|v]alue.?domain.*";
    // maps XMI data types to Java type class names
    public static final Hashtable<String, String> DATATYPE_MAP = new Hashtable<String, String>();
    static {
        DATATYPE_MAP.put("Date", "java.util.Date");
        DATATYPE_MAP.put("Short", "java.lang.Short");
        DATATYPE_MAP.put("Integer", "java.lang.Integer");
        DATATYPE_MAP.put("Long", "java.lang.Long");
        DATATYPE_MAP.put("Float", "java.lang.Float");
        DATATYPE_MAP.put("Double", "java.lang.Double");
        DATATYPE_MAP.put("Boolean", "java.lang.Boolean");
        DATATYPE_MAP.put("Byte", "java.lang.Byte");
        DATATYPE_MAP.put("String", "java.lang.String");
        DATATYPE_MAP.put("Character", "java.lang.Character");
    }
    
    private DomainModel model = null;
    private int errorLineNumber;
    private String projectDescription = null;
    private String projectLongName = null;
    private String projectShortName = null;
    private String projectVersion = null;
    private String packageExcludeRegex = null;
    private float attributeVersion = 1.0f;

    public XMIParser(String projectShortName, String projectVersion) {
        super();
        this.errorLineNumber = -1;
        this.projectShortName = projectShortName;
        this.projectVersion = projectVersion;
        this.packageExcludeRegex = DEFAULT_PACKAGE_EXCLUDE_REGEX;
    }
    
    
    public DomainModel parse(InputStream xmiStream) throws SAXException, IOException, ParserConfigurationException {
        return parse(xmiStream, XmiFileType.SDK_32_EA);
    }
    
    
    public DomainModel parse(File file) throws SAXException, IOException, ParserConfigurationException {
        return parse(file, XmiFileType.SDK_32_EA);
    }
    
    
    public synchronized DomainModel parse(InputStream xmiStream, XmiFileType type) throws SAXException, IOException, ParserConfigurationException {
        this.errorLineNumber = -1;
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        BaseXMIHandler handler = new Sdk4NetbeansXMIHandler(this);   
        try {
            parser.parse(xmiStream, handler);
        } catch (SAXException ex) {
            this.errorLineNumber = handler.getCurrentLine();
            String message = "Error handling " + (errorLineNumber == -1 ? "unknown line" : "line " + errorLineNumber);
            System.err.println(message);
            throw ex;
        }
        return model;
    }
    
    
    /**
     * Returns the line number of the XMI file on which a parsing error
     * occurred.  If no parsing error occurred, or the line number could
     * not be determined, -1 is returned.
     * @return
     *      The error line number of -1 if no line number could be determined
     */
    public int getErrorLineNumber() {
        return this.errorLineNumber;
    }


    public DomainModel parse(File file, XmiFileType type) throws SAXException, IOException, ParserConfigurationException {
        FileInputStream fis = new FileInputStream(file);
        parse(fis, type);
        fis.close();
        return model;
    }


    public String getProjectDescription() {
        return projectDescription;
    }


    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }


    public String getProjectLongName() {
        return projectLongName;
    }


    public void setProjectLongName(String projectLongName) {
        this.projectLongName = projectLongName;
    }


    public String getProjectShortName() {
        return projectShortName;
    }


    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }


    public String getProjectVersion() {
        return projectVersion;
    }


    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }


    public float getAttributeVersion() {
        return attributeVersion;
    }


    public void setAttributeVersion(float attributeVersion) {
        this.attributeVersion = attributeVersion;
    }
    
    
    public String getPackageExcludeRegex() {
        return this.packageExcludeRegex;
    }
    
    
    public void setPackageExcludeRegex(String regex) {
        this.packageExcludeRegex = regex;
    }
    
    
    void setModel(DomainModel model) {
        this.model = model;
    }
}
