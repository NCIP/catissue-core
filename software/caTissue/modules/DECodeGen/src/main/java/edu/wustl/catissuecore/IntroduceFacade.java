/**
 * 
 */
package edu.wustl.catissuecore;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

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
 * 
 */
public final class IntroduceFacade {

	private File serviceDir;
	private String serviceName;

	public IntroduceFacade(File serviceDir, String serviceName) {
		this.serviceDir = serviceDir;
		this.serviceName = serviceName;
	}

	private File getSchemasLocation() {
		return new File(serviceDir, "schema/" + serviceName);
	}

	public void addSchemaTypes(File schemaFile) throws Exception {
		System.out.println("Adding a schema: " + schemaFile.getAbsolutePath());

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
		namespace.setPackageName(FilenameUtils.getBaseName(schemaFile.getName()));

		Map<String, XSComplexType> typeMap = schema.getComplexTypes();
		List<SchemaElementType> typeList = new ArrayList<SchemaElementType>();
		for (String typeName : typeMap.keySet()) {
			SchemaElementType etype = new SchemaElementType();
			etype.setType(typeName);
			typeList.add(etype);
		}
		namespace.setSchemaElement(typeList.toArray(new SchemaElementType[0]));

		CommonTools.addNamespace(introService, namespace);

		Utils.serializeDocument(introduceXML.getAbsolutePath(), introService, IntroduceConstants.INTRODUCE_SKELETON_QNAME);

		SyncTools sync = new SyncTools(serviceDir);
		sync.sync();

		// buildStep();

	}

	public static void main(String[] args) throws Exception {
        IntroduceFacade _if = new IntroduceFacade(new File("C:\\caTissue\\software\\caTissue\\modules\\caTissueStaticDataService"), "Catissue_cacore");
        _if.addSchemaTypes(new File("c:\\caTissue\\!\\gov.nih.nci.cacoresdk.domain.inheritance.abstrakt.xsd"));
	}

}
