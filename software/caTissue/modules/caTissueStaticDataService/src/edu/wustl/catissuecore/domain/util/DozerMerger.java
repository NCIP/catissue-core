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

        if (args.length < 4) {
            System.out.println("Usage: java DozerMerger [mapping | properties] SOURCE_FILE1 SOURCE_FILE2 DESTINATION_FILE");
            System.exit(0);
        }

        DozerMerger dm = new DozerMerger();
        dm.readArguments(args);

        if (dm.operation.equals("mappings")) {
            dm.doMappings();
        }

        if (dm.operation.equals("properties")) {
            dm.mergeProperties();
        }
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

    private Element getFirstClassAElement(Element element) {
        List chs = element.getChildren();
        for (Object o : chs) {
            if (o.getClass() != org.jdom.Element.class) continue;
            Element e = (Element)o;
            if (e.getName().equals("class-a")) {
                return e;
            }
        }
        return null;
    }

    public void doMappings() {
        Set<String> set = new HashSet<String>();
        Document fileA = readXML(src1File);
        Document fileB = readXML(src2File);

        try {
            List<Element> list1 = fileA.getRootElement().getChildren();
            List<Element> list2 = fileB.getRootElement().getChildren();

            for (Element e : list2) {
                if (e.getName().equals("mapping")) {
                    Element ch0 = getFirstClassAElement(e);
                    set.add(ch0.getText());
                }
            }

            System.out.println("Existing <mapping>: " + set.size());

            for (Element e : list1) {
                Element clonedE = (Element)e.clone();
                if (clonedE.getName().equals("mapping")) {
                    Element ch0 = getFirstClassAElement(clonedE);
                    if (ch0 != null && set.add(ch0.getText())) {
                        list2.add(clonedE);
                        System.out.println("Added <" + ch0.getText() + "> to the list.");
                    }
                }
            }

            System.out.println("Total <mapping> elements now: " + set.size());
            XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
            System.out.println("Writing results to file: " + destFile);
            out.output(fileB, new FileOutputStream(destFile));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't write to file. " + e.getMessage());
        }
    }

}
