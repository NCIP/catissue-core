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
	private static Map<String, String> PARTICIPANT_INTEGRATOR_FIELDS = new HashMap<String, String>();

	private static Map<String, String> SPECIMEN_INTEGRATOR_FIELDS = new HashMap<String, String>();

	private static Map<String, String> SPE_INTEGRATOR_FIELDS = new HashMap<String, String>();

	private static Map<String, String> SCG_INTEGRATOR_FIELDS = new HashMap<String, String>();

	static {
		PARTICIPANT_INTEGRATOR_FIELDS.put("Collection Protocol Title", "collectionProtocol");
		PARTICIPANT_INTEGRATOR_FIELDS.put("Participant Protcol ID",    "ppi");

		SPECIMEN_INTEGRATOR_FIELDS.put("Specimen Name",    "specimenLabel");
		SPECIMEN_INTEGRATOR_FIELDS.put("Specimen ID",      "specimenId");
		SPECIMEN_INTEGRATOR_FIELDS.put("Specimen Barcode", "specimenBarcode");

		SPE_INTEGRATOR_FIELDS.put("Specimen Name",    "specimenLabelForEvent");
		SPE_INTEGRATOR_FIELDS.put("Specimen ID",      "specimenIdForEvent");
		SPE_INTEGRATOR_FIELDS.put("Specimen Barcode", "specimenBarcodeForEvent");

		SCG_INTEGRATOR_FIELDS.put("SCG Name",    "scgName");
		SCG_INTEGRATOR_FIELDS.put("SCG ID",      "scgId");
		SCG_INTEGRATOR_FIELDS.put("SCG Barcode", "scgBarcode");
	}

	public String generateAndUploadTemplate(Long formId, String level)
	throws Exception {
		Map<String, String> integratorCtxtFields = getIntegratorCtrxt(level);

		Container c = Container.getContainer(formId);
		if (c == null) {
			throw new RuntimeException("There is no container with Id "	+ formId);
		}

		String tmplName = new StringBuilder().append(c.getCaption())
				.append("_").append(c.getId()).append("_").append(level)
				.toString();

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
			integratorCtxtFields = PARTICIPANT_INTEGRATOR_FIELDS;
		} else if (level.equalsIgnoreCase("SpecimenCollectionGroup")) {
			integratorCtxtFields = SCG_INTEGRATOR_FIELDS;
		} else if (level.equalsIgnoreCase("Specimen")) {
			integratorCtxtFields = SPECIMEN_INTEGRATOR_FIELDS;
		} else if (level.equalsIgnoreCase("SpecimenEvent")) {
			integratorCtxtFields = SPE_INTEGRATOR_FIELDS;
		}

		return integratorCtxtFields;
	}
}
