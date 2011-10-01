
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * AddAssociation class.
 */
public class UpdateQueryMetadata extends BaseMetadata
{

	/**
	 * Specify Connection instance.
	 */
	private static Connection connection;

	private static final String NAME = "name";

	/**
	 * adds Association.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void addAssociation() throws SQLException, IOException
	{
		final AddAssociations addAssociations = new AddAssociations(connection);
		String entityName = "edu.wustl.catissuecore.domain.SpecimenArray";
		String targetEntityName = "edu.wustl.catissuecore.domain.SpecimenArrayContent";
		addAssociations.addAssociation(entityName, targetEntityName,
				"specimenArray_specimenArrayContent", "ASSOCIATION", "specimenArrayContentCollection",
				false, "specimenArray", null, "SPECIMEN_ARRAY_ID", 2, 1, "BI_DIRECTIONAL", false);

		entityName = "edu.wustl.catissuecore.domain.CollectionProtocol";
		targetEntityName = "edu.wustl.catissuecore.domain.ClinicalDiagnosis";
		addAssociations.addAssociation(entityName, targetEntityName,
				"collectionProtocol_clinicalDiagnosis", "ASSOCIATION", "clinicalDiagnosisCollection",
				true, "collectionProtocol", "COLLECTION_PROTOCOL_ID", null, 100, 1, "BI_DIRECTIONAL", false);
		addAssociations.addAssociation(targetEntityName,entityName,
				"clinicalDiagnosis_collectionProtocol", "ASSOCIATION", "collectionProtocol",
				false, "", "COLLECTION_PROTOCOL_ID", null, 1, 0, "BI_DIRECTIONAL", false);
	}

	/**
	 * Adds metadata related to Clinical Diagnosis.
	 * @throws SQLException SQLException
	 * @throws IOException IOException
	 */
	public void addClinicalDiagnosisMetadata() throws SQLException, IOException
	{
		this.populateEntityList();
		this.populateEntityAttributeMap();
		this.populateAttributeColumnNameMap();
		this.populateAttributeDatatypeMap();
		this.populateAttributePrimaryKeyMap();
		connection.createStatement();
		final AddEntity addEntity = new AddEntity(connection);
		addEntity.addEntity(this.entityList, "CATISSUE_CLINICAL_DIAGNOSIS", "NULL", 3, 0);
		final AddAttribute addAttribute = new AddAttribute(connection,
				this.entityNameAttributeNameMap, this.attributeColumnNameMap,
				this.attributeDatatypeMap, this.attributePrimarkeyMap, this.entityList);
		addAttribute.addAttribute();
	}

	private void populateEntityAttributeMap()
	{
		final List<String> attributes = new ArrayList<String>();
		attributes.add("id");
		attributes.add(NAME);

		this.entityNameAttributeNameMap.put(
				"edu.wustl.catissuecore.domain.ClinicalDiagnosis", attributes);
	}

	/**
	 * This method populates Attribute Column Name Map.
	 */
	private void populateAttributeColumnNameMap()
	{
		this.attributeColumnNameMap.put("id", "IDENTIFIER");
		this.attributeColumnNameMap.put(NAME, "CLINICAL_DIAGNOSIS");
	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put("id", "long");
		this.attributeDatatypeMap.put(NAME, "string");
	}

	/**
	 * This method populates Attribute Primary Key Map.
	 */
	private void populateAttributePrimaryKeyMap()
	{
		this.attributePrimarkeyMap.put("id", "1");
		this.attributePrimarkeyMap.put(NAME, "0");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityList.add("edu.wustl.catissuecore.domain.ClinicalDiagnosis");
	}

	public static void main(String [] args) throws SQLException, IOException, ClassNotFoundException
	{
		try
		{
			connection = DBConnectionUtil.getDBConnection(args);
			connection.setAutoCommit(true);
			UpdateMetadataUtil.isExecuteStatement = true;
			UpdateMetadata.DATABASE_TYPE = args[2];
			UpdateQueryMetadata queryMetadata = new UpdateQueryMetadata();
			queryMetadata.addClinicalDiagnosisMetadata();
			queryMetadata.addAssociation();
			queryMetadata.addPath();
		}
		finally
		{
			if (connection != null)
			{
				connection.close();
			}
		}
	}

	/**
	 * Copy paths of parent class to child classes.
	 * @throws SQLException SQLException
	 * @throws IOException IOException
	 */
	private void addPath() throws SQLException, IOException
	{
		AddPath.superClassAndAssociationsMap.clear();
		AddPath.superClassAndSubClassesMap.clear();
		AddPath.superClassDescMap.clear();

		List<String> subClassesList = new ArrayList<String>();
		subClassesList.add("RadRXAnnotationSet");
		subClassesList.add("RadRXAnnotation");
		subClassesList.add("ChemoRXAnnotation");
		AddPath.superClassAndSubClassesMap.put("TreatmentAnnotation", subClassesList);

		List<String> associationsList = new ArrayList<String>();
		associationsList.add("Duration");
		AddPath.superClassAndAssociationsMap
				.put("TreatmentAnnotation", associationsList);
		AddPath.superClassDescMap.put("Duration", "Duration");
		AddPath.superClassDescMap.put("RadRXAnnotation", "RadRXAnnotationSet");
		AddPath.superClassDescMap.put("RadRXAnnotationSet", " ");
		AddPath.superClassDescMap.put("ChemoRXAnnotation", "ChemoRXAnnotation");
		Statement stmt = connection.createStatement();
		AddPath addPath = new AddPath(stmt, connection);
		List<String> insertPathSQL = addPath.getInsertPathStatements( true);
		UpdateMetadataUtil.executeSQLs(insertPathSQL, connection.createStatement(), false);
		stmt.close();
	}
}
