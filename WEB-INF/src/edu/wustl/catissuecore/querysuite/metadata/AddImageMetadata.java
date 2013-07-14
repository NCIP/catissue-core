
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author deepti_shelar
 * Class to add metadata for Race and participant
 */
public class AddImageMetadata extends BaseMetadata
{

	/**
	 * Connection instance.
	 */
	private Connection connection = null;
	/**
	 * Statement instance.
	 */
	private Statement stmt = null;

	/**
	 * This method adds Race Meta data.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void addImageMetadata() throws SQLException, IOException
	{
		this.populateEntityList();
		this.populateEntityAttributeMap();
		this.populateAttributeColumnNameMap();
		this.populateAttributeDatatypeMap();
		this.populateAttributePrimaryKeyMap();

		this.stmt = this.connection.createStatement();
		final AddEntity addEntity = new AddEntity(this.connection);
		addEntity.addEntity(this.entityList, "catissue_image", "NULL", 3, 0);

		final AddAttribute addAttribute = new AddAttribute(this.connection,
				this.entityNameAttributeNameMap, this.attributeColumnNameMap,
				this.attributeDatatypeMap, this.attributePrimarkeyMap, this.entityList);
		addAttribute.addAttribute();

		final String entityName = "edu.wustl.catissuecore.domain.Specimen";
		final String targetEntityName = "krishagni.catissueplus.imaging.domain.Image";

		final AddAssociations addAssociations = new AddAssociations(this.connection);
		addAssociations.addAssociation(targetEntityName, entityName, "image_specimen",
				"ASSOCIATION", "specimen", false, "", "SPECIMEN_ID", null, 2, 1,
				"BI_DIRECTIONAL", false);
		
	}

	/**
	 * This method populates Entity Attribute Map.
	 */
	private void populateEntityAttributeMap()
	{
		final List<String> imageAttributes = new ArrayList<String>();
		imageAttributes.add("id");
		imageAttributes.add("description");
		imageAttributes.add("equipmentImageId");
		imageAttributes.add("height");
		imageAttributes.add("lastUpdateDate");
		imageAttributes.add("fullLocationUrl");
		imageAttributes.add("quality");
		imageAttributes.add("resolution");
		imageAttributes.add("scanDate");
		imageAttributes.add("stainName");
		imageAttributes.add("status");
		imageAttributes.add("imageType");
		imageAttributes.add("width");
		imageAttributes.add("activityStatus");
		
		this.entityNameAttributeNameMap.put("krishagni.catissueplus.imaging.domain.Image", imageAttributes);
	}

	/**
	 * This method populates Attribute ColumnName Map.
	 */
	private void populateAttributeColumnNameMap()
	{
		this.attributeColumnNameMap.put("id", "IDENTIFIER");
		this.attributeColumnNameMap.put("description", "DESCRIPTION");
		this.attributeColumnNameMap.put("equipmentImageId", "EQP_IMAGE_ID");
		this.attributeColumnNameMap.put("height", "HEIGHT");
		this.attributeColumnNameMap.put("lastUpdateDate", "LAST_UPDATE_DATE");
		this.attributeColumnNameMap.put("fullLocationUrl", "FULL_LOC_URL");
		this.attributeColumnNameMap.put("quality", "QUALITY");
		this.attributeColumnNameMap.put("resolution", "RESOLUTION");
		this.attributeColumnNameMap.put("scanDate", "SCAN_DATE");
		this.attributeColumnNameMap.put("stainName", "STAIN_NAME");
		this.attributeColumnNameMap.put("status", "STATUS");
		this.attributeColumnNameMap.put("imageType", "IMAGE_TYPE");
		this.attributeColumnNameMap.put("width", "WIDTH");
		this.attributeColumnNameMap.put("activityStatus", "ACTIVITY_STATUS");
	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put("id", "long");
		this.attributeDatatypeMap.put("description", "string");
		this.attributeDatatypeMap.put("equipmentImageId", "string");
		this.attributeDatatypeMap.put("height", "long");
		this.attributeDatatypeMap.put("lastUpdateDate", "date");
		this.attributeDatatypeMap.put("fullLocationUrl", "string");
		this.attributeDatatypeMap.put("quality", "double");
		this.attributeDatatypeMap.put("resolution", "string");
		this.attributeDatatypeMap.put("scanDate", "date");
		this.attributeDatatypeMap.put("stainName", "string");
		this.attributeDatatypeMap.put("status", "string");
		this.attributeDatatypeMap.put("imageType", "string");
		this.attributeDatatypeMap.put("width", "long");
		this.attributeDatatypeMap.put("activityStatus", "string");
	}

	/**
	 * This method populates Attribute Primary Key Map.
	 */
	private void populateAttributePrimaryKeyMap()
	{
		this.attributePrimarkeyMap.put("id", "1");
		this.attributePrimarkeyMap.put("description", "0");
		this.attributePrimarkeyMap.put("equipmentImageId", "0");
		this.attributePrimarkeyMap.put("height", "0");
		this.attributePrimarkeyMap.put("lastUpdateDate", "0");
		this.attributePrimarkeyMap.put("fullLocationUrl", "0");
		this.attributePrimarkeyMap.put("quality", "0");
		this.attributePrimarkeyMap.put("resolution", "0");
		this.attributePrimarkeyMap.put("scanDate", "0");
		this.attributePrimarkeyMap.put("stainName", "0");
		this.attributePrimarkeyMap.put("status", "0");
		this.attributePrimarkeyMap.put("imageType", "0");
		this.attributePrimarkeyMap.put("width", "0");
		this.attributePrimarkeyMap.put("activityStatus", "0");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityList.add("krishagni.catissueplus.imaging.domain.Image");
	}

	/**
	 * Constructor.
	 * @param connection Connection object.
	 * @throws SQLException SQL Exception
	 */
	public AddImageMetadata(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
}
