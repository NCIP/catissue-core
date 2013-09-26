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

import gov.nih.nci.cagrid.metadata.common.SemanticMetadata;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelUmlGeneralizationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** 
 *  BaseXMIHandler
 *  Base class to organize XMI handlers
 * 
 * @author David Ervin
 * 
 * @created Apr 22, 2008 3:44:03 PM
 * @version $Id: BaseXMIHandler.java,v 1.5 2009-02-04 14:55:45 dervin Exp $ 
 */
public abstract class BaseXMIHandler extends DefaultHandler {
    
    private static Log logger = LogFactory.getLog(BaseXMIHandler.class);
    
    // parser contains configuration options and information for the handler
    private XMIParser parser = null;

    // element text content
    private StringBuffer chars = null;
    
    // SAX locator tells us where we are in the document
    private Locator locator = null;

    // lists of domain model components
    private List<UMLClass> classList = null;
    private List<UMLAttribute> attribList = null;
    private List<UMLAssociation> assocList = null;
    private List<UMLGeneralization> generalizationList = null;
    
    // maps from XMI name to domain model component
    private Map<String, UMLClass> classTable = null; // class ID to class instance
    private Map<String, UMLAttribute> attribTable = null; // attribute ID to attribute instance
    private Map<String, List<SemanticMetadata>> semanticMetadataTable = null; // element ID to semantic metadata list
    private Map<String, String> typeTable = null; // type ID to type name
    
    public BaseXMIHandler(XMIParser parser) {
        super();
        this.parser = parser;
        this.chars = new StringBuffer();
        // initialize lists
        this.classList = new ArrayList<UMLClass>();
        this.attribList = new ArrayList<UMLAttribute>();
        this.assocList = new ArrayList<UMLAssociation>();
        this.generalizationList = new ArrayList<UMLGeneralization>();
        // initialize tables
        this.classTable = new HashMap<String, UMLClass>();
        this.attribTable = new HashMap<String, UMLAttribute>();
        this.semanticMetadataTable = new HashMap<String, List<SemanticMetadata>>();
        this.typeTable = new HashMap<String, String>();
    }
    
    
    /**
     * If the SAX parser supports a Locator, this will get invoked before startDocument()
     */
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }
    
    
    /**
     * Gets the current line of the XMI document being parsed
     * 
     * @return
     *      The line number, or -1 if no locator was supplied
     */
    public int getCurrentLine() {
        int line = -1;
        if (locator != null) {
            line = locator.getLineNumber();
        }
        return line;
    }
    
    
    public void addAssociation(UMLAssociation assoc) {
        assocList.add(assoc);
    }
    
    
    public UMLAssociation getLastAssociation() {
        return assocList.get(assocList.size() - 1);
    }
    
    
    public void addClass(UMLClass clazz) {
        classList.add(clazz);
        classTable.put(clazz.getId(), clazz);
    }
    
    
    public UMLClass getLastClass() {
        return classList.get(classList.size() - 1);
    }
    
    
    public UMLClass getClassById(String id) {
        return classTable.get(id);
    }
    
    
    public void addAttribute(UMLAttribute attrib) {
        attribList.add(attrib);
        attribTable.put(String.valueOf(attrib.getPublicID()), attrib);
    }
    
    
    public UMLAttribute getLastAttribute() {
        return attribList.get(attribList.size() - 1);
    }
    
    
    public void clearAttributeList() {
        this.attribList.clear();
    }
    
    
    public UMLAttribute[] getAttributes() {
        UMLAttribute[] array = new UMLAttribute[attribList.size()];
        attribList.toArray(array);
        return array;
    }
    
    
    public UMLAttribute getAttributeById(String id) {
        return attribTable.get(id);
    }
    
    
    public void addGeneralization(UMLGeneralization gen) {
        generalizationList.add(gen);
    }
    
    
    public UMLGeneralization getLastGeneralization() {
        return generalizationList.get(generalizationList.size() - 1);
    }
    
    
    public StringBuffer getChars() {
        return chars;
    }
    

    public XMIParser getParser() {
        return parser;
    }


    public Map<String, List<SemanticMetadata>> getSemanticMetadataTable() {
        return semanticMetadataTable;
    }


    public Map<String, String> getTypeTable() {
        return typeTable;
    }


    public void characters(char[] ch, int start, int length) throws SAXException {
        chars.append(ch, start, length);
    }
    
    
    public void clearChars() {
        chars.delete(0, chars.length());
    }
    
    
    public void endDocument() throws SAXException {
        applySemanticMetadata();
        applyDataTypes();
        flattenAttributes();
        applyFilters();

        DomainModel model = new DomainModel();
        
        model.setProjectShortName(getParser().getProjectShortName());
        model.setProjectLongName(getParser().getProjectLongName());
        model.setProjectVersion(getParser().getProjectVersion());
        model.setProjectDescription(getParser().getProjectDescription());

        // convert base UML classes to data UML classes
        gov.nih.nci.cagrid.metadata.dataservice.UMLClass[] dataClasses = 
            new gov.nih.nci.cagrid.metadata.dataservice.UMLClass[classList.size()];
        int i = 0;
        for (UMLClass commonClass : classList) {
            gov.nih.nci.cagrid.metadata.dataservice.UMLClass dataClass = 
                new gov.nih.nci.cagrid.metadata.dataservice.UMLClass();
            dataClass.setClassName(commonClass.getClassName());
            dataClass.setDescription(commonClass.getDescription());
            dataClass.setId(commonClass.getId());
            dataClass.setPackageName(commonClass.getPackageName());
            dataClass.setProjectName(commonClass.getProjectName());
            dataClass.setProjectVersion(commonClass.getProjectVersion());
            dataClass.setSemanticMetadata(commonClass.getSemanticMetadata());
            dataClass.setUmlAttributeCollection(commonClass.getUmlAttributeCollection());
            dataClass.setAllowableAsTarget(true); // NEW attribute for data classes
            dataClasses[i++] = dataClass;
        }
        model.setExposedUMLClassCollection(
            new DomainModelExposedUMLClassCollection(dataClasses));
        model.setExposedUMLAssociationCollection(
            new DomainModelExposedUMLAssociationCollection(assocList.toArray(new UMLAssociation[0])));
        model.setUmlGeneralizationCollection(
            new DomainModelUmlGeneralizationCollection(generalizationList.toArray(new UMLGeneralization[0])));
        
        getParser().setModel(model);
    }
    
    
    // -------------------------------
    // end of document cleanup tooling
    // -------------------------------
    
    
    private void applySemanticMetadata() {
        for (String id : semanticMetadataTable.keySet()) {
            if (classTable.containsKey(id)) {
                classTable.get(id).setSemanticMetadata(
                    semanticMetadataTable.get(id).toArray(new SemanticMetadata[0]));
            } else if (attribTable.containsKey(id)) {
                attribTable.get(id).setSemanticMetadata(
                    semanticMetadataTable.get(id).toArray(new SemanticMetadata[0]));
            }
        }
    }


    private void applyDataTypes() {
        for (String id : attribTable.keySet()) {
            UMLAttribute att = attribTable.get(id);
            String typeRef = att.getDataTypeName();

            String umlDataType = null;
            String javaDataType = null;

            // check for class
            if (umlDataType == null) {
                UMLClass typeCl = classTable.get(typeRef);
                if (typeCl != null) {
                    umlDataType = typeCl.getClassName();
                }
            }

            // check type table
            if (umlDataType == null) {
                umlDataType = typeTable.get(typeRef);
            }

            // perform mapping from UML to Java
            logger.debug("UML Data Type: " + umlDataType);
            if (umlDataType != null && XMIParser.DATATYPE_MAP.containsKey(umlDataType)) {
                javaDataType = XMIParser.DATATYPE_MAP.get(umlDataType);
            }
            logger.debug("Java Data Type: " + javaDataType);
            
            // set data type
            att.setDataTypeName(javaDataType);
        }
    }


    private void flattenAttributes() {
        // build table of class reference IDs from subclass -> superclass
        Map<String, String> parentTable = new HashMap<String, String>();
        for (UMLGeneralization gen : generalizationList) {
            parentTable.put(gen.getSubClassReference().getRefid(), 
                gen.getSuperClassReference().getRefid());
        }

        // flatten attributes of each class
        for (String classId : classTable.keySet()) {
            UMLClass clazz = classTable.get(classId);
            List<UMLAttribute> flatAttributes = 
                flattenAttributesOfClass(parentTable, classId);
            clazz.getUmlAttributeCollection().setUMLAttribute(
                flatAttributes.toArray(new UMLAttribute[0]));
        }
    }


    private List<UMLAttribute> flattenAttributesOfClass(
        Map<String, String> parentTable, String classId) {
        // if no class ID, don't return any attributes
        if (classId == null) {
            return new ArrayList<UMLAttribute>(0);
        }
        List<UMLAttribute> flatAttributes = new ArrayList<UMLAttribute>();
        
        // attributes of this class directly
        UMLClass cl = classTable.get(classId);
        if (cl.getUmlAttributeCollection() != null && cl.getUmlAttributeCollection().getUMLAttribute() != null) {
            for (UMLAttribute att : cl.getUmlAttributeCollection().getUMLAttribute()) {
                flatAttributes.add(att);
            }
        }
        
        // attributes of my parent class (and so on...)
        for (UMLAttribute att : flattenAttributesOfClass(parentTable, parentTable.get(classId))) {
            if (!flatAttributes.contains(att)) {
                flatAttributes.add(att);
            }
        }

        return flatAttributes;
    }
    
    
    private boolean shouldExcludeClass(UMLClass clazz, Pattern excludePattern) {
        String pack = clazz.getPackageName();
        return pack.equals("") || pack.startsWith("java") 
            || (excludePattern != null && excludePattern.matcher(pack).matches());
    }


    private void applyFilters() {
        // build a set of class IDs which are valid to keep references to
        // from other components of the model
        Set<String> validClassIds = new HashSet<String>();
        List<UMLClass> filteredClasses = new ArrayList<UMLClass>();
        
        // exclude java.* packages, blank package names, and potentially anything matching the exclude regex
        String excludeRegex = getParser().getPackageExcludeRegex();
        Pattern excludePattern = null;
        if (excludeRegex != null) {
            logger.debug("Excluding packages matching " + excludeRegex);
            excludePattern = Pattern.compile(excludeRegex);
        }
        for (UMLClass clazz : classList) {
            if (!shouldExcludeClass(clazz, excludePattern)) {
                validClassIds.add(clazz.getId());
                filteredClasses.add(clazz);
            }
        }
        this.classList = filteredClasses;

        // filter associations
        List<UMLAssociation> filteredAssociations = 
            new ArrayList<UMLAssociation>(this.assocList.size());
        for (UMLAssociation assoc : this.assocList) {
            if (validClassIds.contains(assoc.getSourceUMLAssociationEdge()
                .getUMLAssociationEdge().getUMLClassReference().getRefid())
                && validClassIds.contains(assoc.getTargetUMLAssociationEdge()
                    .getUMLAssociationEdge().getUMLClassReference().getRefid())) {
                filteredAssociations.add(assoc);
            }
        }
        this.assocList = filteredAssociations;

        // filter generalizations
        List<UMLGeneralization> filteredGeneralizations = 
            new ArrayList<UMLGeneralization>(this.generalizationList.size());
        for (UMLGeneralization gen : this.generalizationList) {
            if (validClassIds.contains(gen.getSubClassReference().getRefid())
                && validClassIds.contains(gen.getSuperClassReference().getRefid())) {
                filteredGeneralizations.add(gen);
            }
        }
        this.generalizationList = filteredGeneralizations;
    }
}
