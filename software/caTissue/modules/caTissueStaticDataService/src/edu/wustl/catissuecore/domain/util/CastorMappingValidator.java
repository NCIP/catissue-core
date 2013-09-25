/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.domain.util;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import java.util.*;

/**
 * @author Ion C. Olaru
 *         Date: 2/21/12 -10:06 AM
 */
public class CastorMappingValidator {

    private static Logger log = Logger.getLogger(CastorMappingValidator.class);
    CastorMappingMerger cmm = new CastorMappingMerger();
    private String sourceFile;
    private Map<String, Element> m = new HashMap<String, Element>();
    private Map<String, Set<String>> fieldsMap = new HashMap<String, Set<String>>();

    public static void main(String[] args) {

        args = new String[] {"src/edu/wustl/catissuecore/domain/Catissue_cacore-unmarshaller-xml-mapping.xml"};

        if (args.length < 1) {
            log.debug("USAGE: java CastorMappingValidator CASTOR_MAPPING_SOURCE_FILE");
            System.exit(0);
        }

        CastorMappingValidator cmv = new CastorMappingValidator();
        cmv.readArguments(args);
        cmv.doValidate();
    }

    private void readArguments(String[] args) {
        sourceFile = args[0];
    }

    public Set<String> extractFields(Element e, String fieldName) {
        String elementName = e.getAttribute("name").getValue();
        String baseName = null;
        Attribute baseAttribute = e.getAttribute("extends");
        if (baseAttribute != null)
            baseName = baseAttribute.getValue();

        if (fieldsMap.containsKey(elementName)) {
            return fieldsMap.get(elementName);
        }

        Set<String> fields = new HashSet<String>();
        Set<String> parentFields = new HashSet<String>();

        // parent fields
        if (baseName != null) {
            // System.out.println("Extracting parent fields: " + baseName);
            parentFields = extractFields(m.get(baseName), fieldName);
        }

        fields.addAll(parentFields);

        // System.out.println("Extracting fields: " + elementName);
        // own fields
        for (Object child : e.getChildren()) {
            Element ce = (Element)child;
            if (!ce.getName().equals("field")) continue;
            if (!fields.add(ce.getAttribute("name").getValue())) {
                // throw new RuntimeException(String.format("%s already contains a fields by name: %s", elementName, ce.getAttribute("name").getValue()));
                System.out.println(String.format("%s already contains a field by name: %s", elementName, ce.getAttribute("name").getValue()));
            }
        }

        fieldsMap.put(elementName, fields);

        return fields;
    }

    public void doValidate() {

        Document fileA = cmm.readXML(sourceFile);
        cmm.readElements(fileA.getRootElement(), m);

        for (Map.Entry entry : m.entrySet()) {
            if (fieldsMap.get(entry.getKey()) != null) continue;

            Element e = (Element)entry.getValue();
            String name = e.getAttribute("name").getValue();

            // Collect the fields
            Set<String> fields = extractFields(e, "field");

        }

//        System.out.println(m.size());
    }
}
