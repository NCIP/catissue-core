package edu.wustl.catissuecore.action.bulkOperations;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.ndao.DbSettingsFactory;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.wustl.bulkoperator.dao.BulkOperationDao;
import edu.wustl.bulkoperator.generatetemplate.BOTemplateGenerator;
import edu.wustl.bulkoperator.generatetemplate.BulkOperationTemplate;
import edu.wustl.bulkoperator.util.BulkOperationConstants;

import java.util.HashMap;
import java.util.Map;

public class BOTemplateGeneratorUtil {
	 private static Map<String, String> participantIntegratorFields = new HashMap<String, String>();

	 private static Map<String, String> specimenIntegratorFields = new HashMap<String, String>();
	 
	 private static Map<String, String> scgIntegratorFields = new HashMap<String, String>();

	    static {
            participantIntegratorFields.put("Collection Protocol Title", "collectionProtocol");
            participantIntegratorFields.put("Participant Protcol ID", "ppi");

            specimenIntegratorFields.put("Specimen Name", "specimenLabel");
            specimenIntegratorFields.put("Specimen ID", "specimenId");
            specimenIntegratorFields.put("Specimen Barcode", "specimenBarcode");

            scgIntegratorFields.put("SCG Name", "scgName");
	    	scgIntegratorFields.put("SCG ID", "scgId");
	    	scgIntegratorFields.put("SCG Barcode", "scgBarcode");
	    }
	    
		public String generateAndUploadTemplate(Long formId, String level) throws Exception {
			Map<String, String> integratorCtxtFields = getIntegratorCtrxt(level);
			
			Container c = Container.getContainer(formId);
			if (c == null) {
				throw new RuntimeException("There is no container with Id "+formId);
	    	}
	            
			String tmplName = new StringBuilder().append(c.getCaption()).append("_")
	            		.append(c.getId()).append("_").append(level).toString();
			
			BOTemplateGenerator generator = new BOTemplateGenerator(tmplName, c, integratorCtxtFields);
			generator.generate();

			BulkOperationTemplate template = new BulkOperationTemplate();
			template.setOperationName(tmplName);
			template.setTemplateName(tmplName);
			template.setXmlTemplate(generator.getTemplateXml());
			template.setCsvTemplate(generator.getTemplateCsv());
			String product = DbSettingsFactory.getProduct();

			if (product.equals("Oracle")) {
				product = BulkOperationConstants.ORACLE_DATABASE;
			} else if (product.equals("MySQL")) {
				product = BulkOperationConstants.MYSQL_DATABASE;
			}
		
			BulkOperationDao boDao = new BulkOperationDao(JdbcDaoFactory.getJdbcDao());
			boDao.uploadTemplate(template, product);
	        return null;
	        
		}

		private Map<String, String> getIntegratorCtrxt(String level) {
			Map<String, String> integratorCtxtFields = new HashMap<String, String>();
			if (level.equalsIgnoreCase("Participant")) {
				integratorCtxtFields = participantIntegratorFields;
			} else if (level.equalsIgnoreCase("SpecimenCollectionGroup")) {
				integratorCtxtFields = scgIntegratorFields;
			} else if (level.equalsIgnoreCase("Specimen")) {
				integratorCtxtFields = specimenIntegratorFields;
			}
			
			return integratorCtxtFields;
		}
	
	
}
