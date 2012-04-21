package edu.wustl.catissuecore.domain.util;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Ion C. Olaru
 * Merges dozer mapping file and other properties files related to dozer configuration.
 */
public class DozerMerger {

    private String operation;
    private String src1File;
    private String src2File;
    private String destFile;
    Properties staticClasses;

/*
    private static String DOZER_MAPPINGS_XPATH = "/mappings/mapping";
    private static String DOZER_PARENTS_REFS_FILENAME = "genericCustomConverterParentRef.properties";
    private static String DOZER_SETS_FILENAME = "genericCustomConverterSets.properties";
    private static String DOZER_MAPPINGS_FILENAME = "dozerBeanMapping.xml";
    private static String FILES_PATH = "c:/SB/models/dozer";
*/

    public static void main(String args[]) {
/*
        args = new String[] {"mappings", FILES_PATH + "/1.xml", FILES_PATH + "/" + DOZER_MAPPINGS_FILENAME, FILES_PATH + "/" + "results.xml"};
        args = new String[] {"properties", FILES_PATH + "/3.properties", FILES_PATH + "/" + DOZER_SETS_FILENAME, FILES_PATH + "/" + DOZER_SETS_FILENAME + "_results.properties"};
*/

/*
        args = new String[] {
            "mappings",
            "/SB/caTissue/LatestDEArtifacts/STATIC-Mapping.xml",
            "/SB/caTissue/LatestDEArtifacts/SOP-Mapping.xml",
            "/SB/caTissue/LatestDEArtifacts/results.xml"
        };
*/

        if (args.length < 4) {
            System.out.println("Usage: java DozerMerger [mappings | properties] SOURCE_FILE1 SOURCE_FILE2 DESTINATION_FILE");
            System.exit(0);
        }

        DozerMerger dm = new DozerMerger();
        dm.readArguments(args);
        dm.loadStaticDEIntegrationClassList();

        if (dm.operation.equals("mappings")) {
            dm.doMappings();
        }

        if (dm.operation.equals("properties")) {
            dm.mergeProperties();
        }
    }

    private void loadStaticDEIntegrationClassList() {
        staticClasses = new Properties();
        try {
            staticClasses.load(CastorMappingMerger.class.getClassLoader().getResourceAsStream("edu/wustl/catissuecore/domain/util/static_de_integration_classes.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Can't read Static DE Integration classes.");
        }
        System.out.println(">>> Found properties: " + staticClasses.size());
    }

    public void mergeProperties() {
        Properties pSrc1 = new Properties();
        Properties pSrc2 = new Properties();
        Properties pDest = new Properties();

        try {
            pSrc1.load(new FileInputStream(src1File));
            pSrc2.load(new FileInputStream(src2File));

            Enumeration en = pSrc1.propertyNames();
            while (en.hasMoreElements()) {
                String elName = (String)en.nextElement();
                String elValue = (String)pSrc1.get(elName);
                pSrc2.setProperty(elName, elValue);
            }

            File destP = new File(destFile);
            FileOutputStream fos = new FileOutputStream(destP);
            pSrc2.store(fos, "Merged with caCoreSDK generated files.");
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the file. " + e.getMessage());
        }
    }

    private void readArguments(String[] args) {
        operation = args[0];
        src1File = args[1];
        src2File = args[2];
        destFile = args[3];
    }

    private static Document readXML(String file) {
        System.out.println("Reading file: " + file);
        Document xml = null;
        SAXBuilder saxBuilder = new SAXBuilder(false);
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

    private Element getFirstElementByNodeName(Element element, String nodeName) {
        List chs = element.getChildren();
        for (Object o : chs) {
            if (o.getClass() != org.jdom.Element.class) continue;
            Element e = (Element)o;
            if (e.getName().equals(nodeName)) {
                return e;
            }
        }
        return null;
    }

    private Element getFirstClassAElement(Element element) {
        return getFirstElementByNodeName(element, "class-a");
    }

    private Element getFirstClassBElement(Element element) {
        return getFirstElementByNodeName(element, "class-b");
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

    /**
     * Takes two or more "mapping" nodes and merge all "field" elements.
     * @param elements List of "mapping" Nodes to do the merge for
     * @return Resulted "mapping" Node.
     */
    private Element mergeElements(Element ... elements) {
        if (elements.length < 2) throw new IllegalArgumentException("Pass at least 2 elements to be merged");

        Map<String, Element> fields = new LinkedHashMap<String, Element>();

        // ELEMENT ANALYSIS
        for (Element e : elements) {
            Element classA = getFirstClassAElement(e);
            Element classB = getFirstClassBElement(e);
            System.out.println(">>> ELEMENT A => B " + classA.getText() + " => " + classB.getText());

            for (Object childObject : e.getChildren()) {
                Element child = (Element)childObject;
                if (child.getName().equals("class-a") || child.getName().equals("class-b")) {
                    fields.put(child.getName(), child);
                    continue;
                }

                String childName = child.getName();
                if (childName.equals("field")) {

                    String childSubName = getFirstElementByNodeName(child, "a").getText();
                    System.out.println(">>> CHILD: " + childSubName);

                    Element element = fields.get(childSubName);
                    if (element == null) {
                        fields.put(childSubName, child);
                    }
                }
            }
        }

        // COMBINING THE CHILDREN
        Element e = elements[0];
        List children = e.getChildren();
        children.clear();

        for (Map.Entry<String, Element> s: fields.entrySet()) {
            Element chE = s.getValue();
//            System.out.println(">>> ADDING: " + getFirstElementByNodeName(chE, "a").getText());
            chE.detach();
            children.add(chE);
        }
        return e;
    }

    public void readElements(Element rootElement, Map<String, Element> m) {
        List<Element> childrenA = getChildElements(rootElement);
        for (Element e: childrenA) {
            System.out.println("Found element: " + e.getName());
            if (e.getName().equals("configuration")) {
                m.put("configuration", e);
            } else if (e.getName().equals("mapping")) {
                Element classA = getFirstClassAElement(e);
                String key = classA.getText();
                if (m.containsKey(classA.getText())) {
                    // If this is a class that belongs to the DE static model we have to skip it.
                    if (staticClasses.containsKey(key)) {
                        System.out.println("Skipping: " + key);
                        continue;
                    }
                    m.put(key, mergeElements(m.get(key), e));
                } else {
                    m.put(key, e);
                }
            } else {
                throw new RuntimeException("Unknown Dozer Node.");
            }
        }
    }

    public void doMappings() {
        Set<String> set = new HashSet<String>();
        Document fileA = readXML(src1File);
        Document fileB = readXML(src2File);

        Map<String, Element> m = new LinkedHashMap<String, Element>();

        readElements(fileA.getRootElement(), m);
        readElements(fileB.getRootElement(), m);

        Element e = fileA.getRootElement();
        List children = e.getChildren();
        children.clear();

        for (Map.Entry s: m.entrySet()) {
            System.out.println(">>> ADDING: " + s.getValue());
            Element che = (Element)s.getValue();
            che.detach();
            children.add(che);
        }

        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        System.out.println("Writing results to file: " + destFile);
        try {
            out.output(e, new FileOutputStream(destFile));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

}
