/**
 * 
 */
package edu.wustl.catissuecore.domain.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

/**
 * @author Denis G. Krylov
 * 
 */
public class XSDConflictsResolver {

	private File pathToSchemas;

	private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private static final List<String> types = Arrays.asList("SCGRecordEntry", "SpecimenRecordEntry",
            "ParticipantRecordEntry", "ActionApplicationRecordEntry");
	
	private static final Map<String, String> schemaMap = new HashMap<String, String>();
	static {
		schemaMap.put("gme://caCORE.caCORE/3.2/edu.wustl.catissuecore.domain.deintegration", "gme://caCORE.caCORE/3.2/edu.wustl.catissuecore.domain.deintegration.ws");
		schemaMap.put("gme://caCORE.caCORE/3.2/edu.common.dynamicextensions.domain.integration", "gme://caCORE.caCORE/3.2/edu.common.dynamicextensions.domain.integration.ws");
		schemaMap.put("gme://caCORE.caCORE/3.2/edu.wustl.catissuecore.domain", "gme://caCORE.caCORE/3.2/edu.wustl.catissuecore.domain.ws");		
		schemaMap.put("edu.wustl.catissuecore.domain.xsd", "edu.wustl.catissuecore.domain.ws.xsd");
		schemaMap.put("edu.common.dynamicextensions.domain.integration.xsd", "edu.common.dynamicextensions.domain.integration.ws.xsd");
	}

	public XSDConflictsResolver(String path) {
		this.pathToSchemas = new File(path);
	}

	public void resolve() throws SAXException, IOException {
		log.info("Resolving XML Schema conflicts: " + pathToSchemas.getAbsolutePath());
		Collection<File> schemas = FileUtils.listFiles(pathToSchemas, new String[] { "xsd" }, false);
		for (File schemaFile : schemas) {
			String schema = FileUtils.readFileToString(schemaFile, null);
			for (String type: types) {
				if (schema.contains("<xs:complexType name=\""+type+"\">")) {
					resolve(schemaFile, type);
				}
			}
			/**
			log.info("Parsing " + schemaFile.getName());
			XSOMParser parser = new XSOMParser();
			parser.setErrorHandler(new ErrorHandler() {
				
				public void warning(SAXParseException exception) throws SAXException {	
					log.severe(exception.getMessage());
				}
				
				public void fatalError(SAXParseException exception) throws SAXException {
					log.severe(exception.getMessage());
				}
				
				public void error(SAXParseException exception) throws SAXException {
					log.severe(exception.getMessage());
				}
			});
			parser.parse(schemaFile);
			XSSchemaSet set = parser.getResult();
			if (set != null) {
				XSSchema schema = set.getSchema(1);

				Map<String, XSComplexType> typeMap = schema.getComplexTypes();
				for (String typeName : typeMap.keySet()) {
					if (types.contains(typeName)) {
						resolve(schemaFile, typeName);
						return;
					}
				}
			} else {
				log.severe("Unable to parse!");
			}
			**/
		}
		
	}

	private void resolve(File schemaFile, String typeName) throws IOException {
		String newFileName = typeName + ".xsd";
		log.info("This XML Schema needs special handling: "
				+ schemaFile.getName()
				+ ". It conflicts with an existing schema in the grid service. We will rename it to "
				+ newFileName + " before copying to the grid service.");
		final File newSchemaFile = new File(schemaFile.getParentFile(),
				newFileName);
		
		FileUtils.copyFile(schemaFile, newSchemaFile);
		
		/**
		if (!schemaFile.renameTo(newSchemaFile)) {
			log.severe("Unable to rename the schema!");
			throw new IOException("Unable to rename the schema! "
					+ schemaFile.getAbsolutePath());
		}
		**/
		
		/**
		Collection<File> schemas = FileUtils.listFiles(pathToSchemas,
				new String[] { "xsd" }, false);
		for (File file : schemas) {
			if (!file.equals(newSchemaFile)) {
				log.info("Replacing references in " + file.getName() + ": "
						+ schemaFile.getName() + " --> " + newFileName);
				FileUtils.writeStringToFile(
						file,
						FileUtils.readFileToString(file, null).replace(
								schemaFile.getName(), newFileName), null);
			}
		}
		**/
		
		log.info("Updating "+newSchemaFile.getName()+" to adjust namespaces to be '.ws'");
		String schema = FileUtils.readFileToString(newSchemaFile, null);
		for (String key : schemaMap.keySet()) {
			String repl = schemaMap.get(key);
			schema = schema.replace('\"'+key+'\"', '\"'+repl+'\"');
		}
		FileUtils.writeStringToFile(newSchemaFile, schema, null);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws SAXException
	 */
	public static void main(String[] args) throws SAXException, IOException {
		XSDConflictsResolver resolver = new XSDConflictsResolver(args[0]);
		resolver.resolve();
	}

}
