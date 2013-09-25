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
import gov.nih.nci.cagrid.metadata.common.UMLClassUmlAttributeCollection;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference;
import gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization;
import gov.nih.nci.cagrid.metadata.xmi.Sdk4EaXMIConstants;
import gov.nih.nci.cagrid.metadata.xmi.XMIConstants;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
  *  XMIHandler
  *  SAX handler for caCORE SDK 4.0 EA XMI -> Domain Model
  * 
  * @author Patrick McConnell
  * @author David Ervin
  * 
  * @created Oct 22, 2007 10:26:25 AM
  * @version $Id: Sdk4EaXMIHandler.java,v 1.8 2009/06/11 19:01:44 dervin Exp $
 */
public class Sdk4NetbeansXMIHandler extends BaseXMIHandler {
    private static final Log LOG = LogFactory.getLog(Sdk4NetbeansXMIHandler.class);   
    
    private UMLAssociationEdge edge = null;
    private boolean sourceNavigable = false;
    private boolean targetNavigable = false;
    private String pkg = "";
    private boolean handlingAttribute = false;
    private boolean handlingClass = false;
    private UMLAttribute currentAttribute = null;

    public Sdk4NetbeansXMIHandler(XMIParser parser) {
        super(parser);
    }

    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals(XMIConstants.XMI_UML_PACKAGE)) {
            // unwind the package name stack one level
            int index = pkg.lastIndexOf('.');
            if (index == -1) {
                pkg = "";
            } else {
                pkg = pkg.substring(0, index);
            }
        } else if (qName.equals(XMIConstants.XMI_UML_CLASS)) {
            // done with the current class, attach attributes to it and clear them out of the buffer
            UMLClass cl = getLastClass();
            cl.setUmlAttributeCollection(
                new UMLClassUmlAttributeCollection(getAttributes()));
            clearAttributeList();
            handlingClass = false;
        } else if (qName.equals(XMIConstants.XMI_UML_ASSOCIATION)) {
            // close up the association, figure out bidirectionality
            UMLAssociation assoc = getLastAssociation();
            if (sourceNavigable && !targetNavigable) {
                UMLAssociationEdge assocEdge = assoc.getSourceUMLAssociationEdge().getUMLAssociationEdge();
                assoc.getSourceUMLAssociationEdge().setUMLAssociationEdge(
                    assoc.getTargetUMLAssociationEdge().getUMLAssociationEdge());
                assoc.getTargetUMLAssociationEdge().setUMLAssociationEdge(assocEdge);
            }
            assoc.setBidirectional(sourceNavigable && targetNavigable);
        } else if (qName.equals(XMIConstants.XMI_UML_ATTRIBUTE)) {
            // attribute complete, add it to the list for the current class
            addAttribute(currentAttribute);
            currentAttribute = null;
            handlingAttribute = false;
        }

        clearChars();
    }


    @Override
    public void startElement(
        String uri, String localName, String qName, Attributes atts) throws SAXException {
        clearChars();

        if (qName.equals(XMIConstants.XMI_UML_PACKAGE)) {
            handlePackage(atts);
        } else if (qName.equals(XMIConstants.XMI_UML_CLASS)) {
            handleClass(atts);
        } else if (qName.equals(XMIConstants.XMI_UML_ATTRIBUTE)) {
            handleAttribute(atts);
        } else if (qName.equals(Sdk4EaXMIConstants.XMI_UML_CLASSIFIER)) {
            handleClassifier(atts);
        } else if (qName.equals(XMIConstants.XMI_UML_ASSOCIATION)) {
            // start the new association
            UMLAssociation ass = new UMLAssociation();
            addAssociation(ass);
        } else if (qName.equals(XMIConstants.XMI_UML_ASSOCIATION_END)) {
            handleAssociationEnd(atts);
        } else if (qName.equals(XMIConstants.XMI_UML_GENERALIZATION)) {
            handleGeneralization(atts);
        } else if (qName.equals(XMIConstants.XMI_UML_TAGGED_VALUE)) {
            handleTag(atts);
        } else if (qName.equals(XMIConstants.XMI_UML_DATA_TYPE)) {
            handleDataType(atts);
        } else if (qName.equals(XMIConstants.XMI_FOUNDATION_CORE_CLASSIFIER)) {
            if (!handlingAttribute) {
                LOG.info("Ignoring " + XMIConstants.XMI_FOUNDATION_CORE_CLASSIFIER);
            } else {
                getLastAttribute().setDataTypeName(atts.getValue(XMIConstants.XMI_IDREF));
            }
        }
    }

    
    // ------------------
    // XMI type handlers
    // ------------------
    
    
    private void handleDataType(Attributes atts) {
        getTypeTable().put(atts.getValue(XMIConstants.XMI_ID_ATTRIBUTE), 
            atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE));
    }
    
    
    private void handleGeneralization(Attributes atts) {
        UMLGeneralization gen = new UMLGeneralization();
        String subId = atts.getValue(XMIConstants.XMI_UML_GENERALIZATION_CHILD);
        String superId = atts.getValue(XMIConstants.XMI_UML_GENERALIZATION_PARENT);
        if (subId == null) {
            subId = atts.getValue(XMIConstants.XMI_UML_GENERALIZATION_SUBTYPE);
        }
        if (superId == null) {
            superId = atts.getValue(XMIConstants.XMI_UML_GENERALIZATION_SUPERTYPE);
        }
        gen.setSubClassReference(new UMLClassReference(subId));
        gen.setSuperClassReference(new UMLClassReference(superId));
        addGeneralization(gen);
    }
    
    
    private void handleAssociationEnd(Attributes atts) {
        // get the most recently found association
        UMLAssociation assoc = getLastAssociation();
        boolean isNavigable = "true".equals(atts.getValue(XMIConstants.XMI_UML_ASSOCIATION_IS_NAVIGABLE));

        edge = new UMLAssociationEdge();
        if (assoc.getSourceUMLAssociationEdge() == null) {
            assoc.setSourceUMLAssociationEdge(new UMLAssociationSourceUMLAssociationEdge(edge));
            sourceNavigable = isNavigable;
        } else {
            assoc.setTargetUMLAssociationEdge(new UMLAssociationTargetUMLAssociationEdge(edge));
            targetNavigable = isNavigable;
        }
        edge.setRoleName(atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE));
        edge.setUMLClassReference(new UMLClassReference(atts.getValue(XMIConstants.XMI_TYPE_ATTRIBUTE)));
        
        // multiplicity of the edge
        String multiplicity = atts.getValue(Sdk4EaXMIConstants.MULTIPLICITY_ATTRIBUTE);
        int minMult = 0;
        int maxMult = -1; // flag for unbounded
        if (multiplicity == null) {
            // this is probably an error!
            LOG.warn("WARNING!!! NO MULTIPLICITY DEFINED FOR AN ASSOCIATION EDGE!!!!");
        } else if (multiplicity.contains(Sdk4EaXMIConstants.MULTIPLICITY_RANGE_SEPARATOR)) {
            // distinct min and max
            String min = multiplicity.substring(0, multiplicity.indexOf(Sdk4EaXMIConstants.MULTIPLICITY_RANGE_SEPARATOR));
            String max = multiplicity.substring(min.length() + Sdk4EaXMIConstants.MULTIPLICITY_RANGE_SEPARATOR.length());
            minMult = Integer.parseInt(min);
            if (!max.equals("*")) {
                maxMult = Integer.parseInt(max);
            }
        } else if (multiplicity.equals("*")) {
            // unbounded in general?? (AIM has this, but I have no idea why)
            minMult = -1;
        } else {
            // single value for min and max
            minMult = Integer.parseInt(multiplicity);
            maxMult = minMult;
        }
        edge.setMinCardinality(minMult);
        edge.setMaxCardinality(maxMult);
    }
    
    
    private void handleAttribute(Attributes atts) {
        handlingAttribute = true;
        currentAttribute = new UMLAttribute();
        currentAttribute.setName(atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE));
        currentAttribute.setVersion(getParser().getAttributeVersion());
    }
    
    
    private void handleClassifier(Attributes atts) {
        if (handlingAttribute) {
            String typeIdRef = atts.getValue(XMIConstants.XMI_IDREF);
            currentAttribute.setDataTypeName(typeIdRef);
        }
    }
    
    
    private void handleClass(Attributes atts) {
        handlingClass = true;
        UMLClass cl = new UMLClass();
        cl.setClassName(atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE));
        cl.setId(atts.getValue(XMIConstants.XMI_ID_ATTRIBUTE));
        cl.setPackageName(pkg);
        cl.setProjectName(getParser().getProjectShortName());
        cl.setProjectVersion(getParser().getProjectVersion());
        addClass(cl);
    }
    
    
    private void handlePackage(Attributes atts) {
        String name = atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE);
        if (!name.equals(XMIConstants.XMI_LOGICAL_VIEW) 
            && !name.equals(XMIConstants.XMI_LOGICAL_MODEL)
            && !name.equals(XMIConstants.XMI_DATA_MODEL)) {
            if (!pkg.equals("")) {
                pkg += ".";
            }
            pkg += atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE);
        }
    }
    
    
    private void handleAttributeTag(String tag, String value) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Handling tag for attribute");
            LOG.debug("\ttag=" + tag);
            LOG.debug("\tvalue=" + value);
        }
        // if a CADSR_DE_ID is provided, prefer it over the
        // autogenerated EA ID.  EA IDs will all be converted to be 
        // negative values so they're out of the way of valid
        // caDSR IDs
        if (handlingAttribute && "ea_guid".equals(tag) 
            && currentAttribute.getPublicID() == 0) {
            long idValue = Long.MIN_VALUE + Math.abs(value.hashCode());
            currentAttribute.setPublicID(idValue);
        } else if (handlingAttribute && XMIConstants.XMI_TAG_CADSR_DE_ID.equals(tag)) {
            currentAttribute.setPublicID(Long.parseLong(value));
        } else if (handlingAttribute && "id".equals(tag) 
                && currentAttribute.getPublicID() == 0) {
            currentAttribute.setPublicID(Long.parseLong(value));
        } else if (handlingAttribute && XMIConstants.XMI_TAG_CADSR_DE_VERSION.equals(tag)) {
            currentAttribute.setVersion(Float.parseFloat(value));
        } else if (tag.equals(XMIConstants.XMI_TAG_DESCRIPTION)) {
            // set the attribute's description
            LOG.debug("Setting attribute's description: " + value);
            currentAttribute.setDescription(value);
        } else if (tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_CONCEPT_CODE)
            || tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_QUALIFIER_CONCEPT_CODE)
            || tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_CONCEPT_PREFERRED_NAME)
            || tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_QUALIFIER_CONCEPT_PREFERRED_NAME)
            || (tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_CONCEPT_DEFINITION) 
                && !tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_CONCEPT_DEFINITION_SOURCE))
            || (tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_QUALIFIER_CONCEPT_DEFINITION) 
                && !tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_QUALIFIER_CONCEPT_DEFINITION_SOURCE))) {
            addSemanticMetadata(tag, String.valueOf(currentAttribute.getPublicID()), value);
        }
    }
    
    
    private void handleClassTag(String tag, String value, String modelElement) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Handling tag for class");
            LOG.debug("\ttag=" + tag);
            LOG.debug("\tvalue=" + value);
            LOG.debug("\tmodelElement=" + modelElement);
        }
        if (tag.equals(XMIConstants.XMI_TAG_DESCRIPTION)) {
            // class description
            UMLClass refedClass = getClassById(modelElement);
            if (refedClass != null) {
                refedClass.setDescription(value);
            }
        } else if (tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_CONCEPT_CODE)
            || tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_QUALIFIER_CONCEPT_CODE)
            || tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_CONCEPT_PREFERRED_NAME)
            || tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_QUALIFIER_CONCEPT_PREFERRED_NAME)
            || (tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_CONCEPT_DEFINITION) 
                && !tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_CONCEPT_DEFINITION_SOURCE))
            || (tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_QUALIFIER_CONCEPT_DEFINITION) 
                && !tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_QUALIFIER_CONCEPT_DEFINITION_SOURCE))) {
            addSemanticMetadata(tag, modelElement, value);
        }
    }
    
    
    private void handleTag(Attributes atts) {
        String tag = atts.getValue(XMIConstants.XMI_UML_TAGGED_VALUE_TAG);
        String modelElement = atts.getValue(XMIConstants.XMI_UML_TAGGED_VALUE_MODEL_ELEMENT);
        String value = atts.getValue(XMIConstants.XMI_UML_TAGGED_VALUE_VALUE);
        
        if (handlingAttribute) {
            handleAttributeTag(tag, value);
        } else if (handlingClass) {
            handleClassTag(tag, value, modelElement);
        }
    }
    
    
    //---------------------
    // general helpers
    //---------------------
    

    private int getSemanticMetadataOrder(String tag) {
        char c = tag.charAt(tag.length() - 1);
        if (Character.isDigit(c)) {
            return Integer.parseInt(String.valueOf(c));
        }
        return 0;
    }


    private void addSemanticMetadata(String tag, String elementId, String value) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding semantic metadata");
            UMLClass clazz = getClassById(elementId);
            UMLAttribute attr = getAttributeById(elementId);
            if (clazz != null) {
                LOG.debug("\tReferenced Class: " + clazz.getPackageName() 
                    + "." + clazz.getClassName());
            } else if (attr != null) {
                LOG.debug("\tReferenced Attribute: " + attr.getName() + " : " 
                    + attr.getPublicID() + " : " + attr.getDataTypeName());
            }
        }
        int order = getSemanticMetadataOrder(tag);

        List<SemanticMetadata> smList = getSemanticMetadataTable().get(elementId);
        if (smList == null) {
            getSemanticMetadataTable().put(elementId, 
                smList = new ArrayList<SemanticMetadata>(2));
        }

        int size = smList.size();
        if (size <= order) {
            for (int i = smList.size(); i <= order; i++) {
                smList.add(new SemanticMetadata());
            }
        }

        SemanticMetadata sm = smList.get(order);
        if (tag.indexOf(XMIConstants.XMI_TAG_CONCEPT_CODE) != -1) {
            sm.setOrder(Integer.valueOf(order));
            sm.setConceptCode(value);
        } else if (tag.indexOf(XMIConstants.XMI_TAG_PREFERRED_NAME) != -1) {
            sm.setConceptName(value);
        } else if (tag.indexOf(XMIConstants.XMI_TAG_CONCEPT_DEFINITION) != -1) {
            sm.setConceptDefinition(value);
        }
    }
}