package edu.wustl.catissuecore.domain.util;

import org.apache.log4j.Logger;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.EntityResolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Ion C. Olaru
 *         Date: 1/19/2012 - 3:38 PM
 * Merges castor mapping files
 */
public class CastorMappingMerger {
    private static Logger log = Logger.getLogger(CastorMappingMerger.class);
    private String src1File;
    private String src2File;
    private String destFile;
    private static EntityResolver resolver;

    public static void main(String args[]) {

        args = new String[] {"./temp/pathology_scg-schema.jar/xml-mapping.xml", "./temp/pathology_specimen-schema.jar/xml-mapping.xml", "./CASTOR_MERGED.xml"};
//        args = new String[] {"properties", FILES_PATH + "/3.properties", FILES_PATH + "/" + DOZER_SETS_FILENAME, FILES_PATH + "/" + DOZER_SETS_FILENAME + "_results.properties"};

        if (args.length < 3) {
            log.debug("USAGE: java CastorMappingMerger SOURCE_FILE1 SOURCE_FILE2 DESTINATION_FILE");
            System.exit(0);
        }

        CastorMappingMerger m = new CastorMappingMerger();
        m.readArguments(args);
        m.initializeResolver();
        m.doMerge();

    }

    private void initializeResolver() {
        String catalogLocation;
        catalogLocation = new File("src/edu/wustl/catissuecore/domain/dtd/catalog.xml").getAbsolutePath();
        if (System.getProperty("os.name").startsWith("Windows")) {
            catalogLocation = catalogLocation.substring(2);
        }
        log.debug(">>> CATALOG FILE: " + catalogLocation);
        System.setProperty("xml.catalog.files", catalogLocation);
        resolver = new CatalogResolver();
    }

    private void readArguments(String[] args) {
        src1File = args[0];
        src2File = args[1];
        destFile = args[2];
    }

    private static Document readXML(String file) {
        log.debug(">>> Reading file: " + file);
        Document xml = null;
        SAXBuilder saxBuilder = new SAXBuilder(false);
        saxBuilder.setEntityResolver(resolver);
        try {
            xml = saxBuilder.build(new File(file));
        } catch (JDOMException e) {
            e.printStackTrace();
            throw new RuntimeException("XML File is not well formatted. " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the file. " + e.getMessage());
        }
        return xml;
    }

    private Element getFirstClassAElement(Element element) {
        List chs = element.getChildren();
        for (Object o : chs) {
            if (o.getClass() != Element.class) continue;
            Element e = (Element)o;
            if (e.getName().equals("class-a")) {
                return e;
            }
        }
        return null;
    }

    private List<Element> getChildElements(Element element) {
        List chs = element.getChildren();
        List<Element> children = new ArrayList<Element>();
        for (Object o : chs) {
            if (o.getClass() != Element.class) continue;
            Element e = (Element)o;
            children.add(e);
        }
        return children;
    }

    private Element mergeElements(Element ... elements) {
        if (elements.length < 2) throw new IllegalArgumentException("Pass at least 2 elements to be merged");

        Map<String, Element> fields = new HashMap<String, Element>();

        // ELEMENT ANALYSIS
        for (Element e : elements) {
            log.debug(">>> ELEMENT: " + e.getAttribute("name").getValue());

            for (Object childObject : e.getChildren()) {

                Element child = (Element)childObject;
                String childName = child.getName();
                log.debug(">>> CHILD: " + childName);

                if (childName.equals("map-to")) {
                    Element element = fields.get(childName);

                    if (element != null && !element.getAttribute("xml").getValue().equals(child.getAttribute("xml").getValue())) {
                        throw new RuntimeException(String.format("Two elements of the same class %s are trying to map to different XML Elements: %s, %s.",
                                e.getAttribute("name").getValue(),
                                element.getAttribute("xml"),
                                child.getAttribute("xml")));
                    } else {
                        fields.put(childName, child);
                    }
                } else if (childName.equals("field")) {
                    String fieldName = child.getAttribute("name").getValue();
                    String fieldType = child.getAttribute("type").getValue();
                    String lookupName = childName + ":" + fieldName;
                    Element element = fields.get(lookupName);

                    if (element != null && !element.getAttribute("type").getValue().equals(fieldType)) {
                        throw new RuntimeException(String.format("Two elements with the same name %s are trying to use the same type: %s, %s.",
                                fieldName,
                                element.getAttribute("type"),
                                child.getAttribute("type")));
                    } else {
                        fields.put(lookupName, child);
                    }
                }

            }
        }

        // COMBINING THE CHILDREN
        Element e = elements[0];
        List children = e.getChildren();
        children.clear();

        for (Map.Entry s: fields.entrySet()) {
            log.debug(">>> ADDING: " + s.getValue());
            Element che = (Element)s.getValue();
            che.detach();
            children.add(che);
        }
        return e;
    }

    private void readElements(Element rootElement, Map<String, Element> m) {
        List<Element> childrenA = getChildElements(rootElement);
        for (Element e: childrenA) {
            String key = e.getAttribute("name").getValue();
            if (m.containsKey(key)) {
                m.put(key, mergeElements(m.get(key), e));
            } else {
                m.put(key, e);
            }
        }
    }

    public void doMerge() {

        Map<String, Element> m = new HashMap<String, Element>();

        Document fileA = readXML(src1File);
        Document fileB = readXML(src2File);

        readElements(fileA.getRootElement(), m);
        readElements(fileB.getRootElement(), m);

        // COMBINING ALL ELEMENTS INTO ONE DOCUMENT

        Element e = fileA.getRootElement();
        List children = e.getChildren();
        children.clear();

        for (Map.Entry s: m.entrySet()) {
            log.debug(">>> ADDING: " + s.getValue());
            Element che = (Element)s.getValue();
            che.detach();
            children.add(che);
        }

        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        log.debug("Writing results to file: " + destFile);
        try {
            out.output(e, new FileOutputStream(destFile));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

}
