/**
 * 
 */
package edu.wustl.catissuecore;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jdom.DefaultJDOMFactory;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;

/**
 * Modifies caGrid service project created by Introduce by:
 * 
 * 1. Adding data types from the given XSD, 2. Re-syncing the source and
 * re-creating the skeletons.
 * 
 * @author Denis G. Krylov
 * @author Ion C. Olaru
 * 
 */
public final class IntroduceFacade {

	private File serviceDir;
	private String serviceName;
	private static String serverWsddFile = "server-config.wsdd";
	private static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public IntroduceFacade(File serviceDir, String serviceName) {
		this.serviceDir = serviceDir;
		this.serviceName = serviceName;
	}

	private File getSchemasLocation() {
		return new File(serviceDir, "schema/" + serviceName);
	}

	public void addSchemaTypes(File schemaFile) throws Exception {

        log.info("Adding a schema: " + schemaFile.getAbsolutePath());
        log.info("to service: " + serviceDir.getAbsolutePath());

        preprocessSchema(schemaFile);
        
		XSOMParser parser = new XSOMParser();
		parser.parse(schemaFile);
		XSSchemaSet set = parser.getResult();
		XSSchema schema = set.getSchema(1);

		Utils.copyFile(schemaFile, new File(getSchemasLocation(), schemaFile.getName()));

		final File introduceXML = new File(serviceDir, "introduce.xml");
		ServiceDescription introService = (ServiceDescription) Utils.deserializeDocument(introduceXML.getAbsolutePath(), ServiceDescription.class);

		NamespaceType namespace = new NamespaceType();
		namespace.setLocation("." + File.separator + schemaFile.getName());
		namespace.setNamespace(schema.getTargetNamespace());

        String packageName = FilenameUtils.getBaseName(schemaFile.getName()) + ".ws";
		namespace.setPackageName(packageName);

		Map<String, XSComplexType> typeMap = schema.getComplexTypes();
		List<SchemaElementType> typeList = new ArrayList<SchemaElementType>();

        Set<String> types = new HashSet<String>();

		for (String typeName : typeMap.keySet()) {
			SchemaElementType etype = new SchemaElementType();
			etype.setType(typeName);
			typeList.add(etype);
            types.add(typeName);
		}

		namespace.setSchemaElement(typeList.toArray(new SchemaElementType[0]));
		CommonTools.addNamespace(introService, namespace);

		Utils.serializeDocument(introduceXML.getAbsolutePath(), introService, IntroduceConstants.INTRODUCE_SKELETON_QNAME);

		SyncTools sync = new SyncTools(serviceDir);
		sync.sync();

        updateServerWSDDFile(types, schema.getTargetNamespace(), packageName);

		// buildStep();

	}

    private void preprocessSchema(File schemaFile) throws SAXException, IOException {
    	String schema = FileUtils.readFileToString(schemaFile);
    	String updatedSchema = schema.replaceAll("<xs:attribute\\s+name=\"(id)\"", "<xs:attribute name=\"identifier\"");
    	FileUtils.writeStringToFile(schemaFile, updatedSchema);
    	
    	/**
    	XSOMParser parser = new XSOMParser();
		parser.parse(schemaFile);
		XSSchemaSet set = parser.getResult();
		XSSchema schema = set.getSchema(1);
		
		Map<String, XSComplexType> typeMap = schema.getComplexTypes();
		for (String typeName : typeMap.keySet()) {
			XSComplexType type = typeMap.get(typeName);
			for (XSAttributeUse attr : type.getAttributeUses()) {
				if (attr.getDecl().getName().equals("id")) {
					log.info("Renaming 'id' attribute of type "+type.getName()+" to 'identifier'");
					attr.getDecl().
				}
			}
		}		
		**/
	}

	/**
     * Updates server-config.wsdd file to contain serializer and deserializer information for new inserted types.
     * @param types set of types to be added to the mappings list
     * @param targetNamespace target namespace to use for the element's namespaces
     * @param packageName type's package name
     * @return void
     * @throws RuntimeException throws RuntimeException if writing to the target file fails for any reason
     *
     * */
    private synchronized void updateServerWSDDFile(Set<String> types, String targetNamespace, String packageName) {
        String wsddServerFile = serviceDir.getAbsolutePath() + File.separator + serverWsddFile;
        DefaultJDOMFactory f = new DefaultJDOMFactory();

        try {
            Element wsddRoot = XMLUtilities.fileNameToDocument(wsddServerFile).getRootElement();
            for (String s:types) {
                Element e = new Element("typeMapping");
                Namespace ns1 = Namespace.getNamespace("ns1", "http://xml.apache.org/axis/wsdd/providers/java");
                Namespace ns2 = Namespace.getNamespace("ns2", targetNamespace);

                e.addNamespaceDeclaration(ns1);
                e.addNamespaceDeclaration(ns2);
                e.setAttribute("serializer", "org.cagrid.data.sdkquery42.encoding.SDK42SerializerFactory");
                e.setAttribute("deserializer", "org.cagrid.data.sdkquery42.encoding.SDK42DeserializerFactory");
                e.setAttribute("encodingStyle", "");
                e.setAttribute("type", "ns1:" + packageName + "." + s);
                e.setAttribute("qname", "ns2:" + s);

                wsddRoot.getChildren().add(e);
            }

            XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
            out.output(wsddRoot, new FileOutputStream(wsddServerFile));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Can't update <%s> file.", serverWsddFile), e);
        }
    }

/**
 * Program parameters:
 * ../caTissueStaticDataService/ Catissue_cacore ./src/test/resources/withIdentifier/clinical_annotation.xsd
 *
 * */
	public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Pass 3 arguments. See Usage.");
            System.exit(0);
        }

        byte i = 0;
        for (String s:args) {
            System.out.println(new Character((char)(97 + i++)).toString() + ". " + s);
        }
        IntroduceFacade _if = new IntroduceFacade(new File(args[0]), args[1]);
        _if.addSchemaTypes(new File(args[2]));
	}

}
