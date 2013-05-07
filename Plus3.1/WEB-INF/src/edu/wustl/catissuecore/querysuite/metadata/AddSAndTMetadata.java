
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pooja_deshpande
 * Class to add meta data for Shipping & Tracking
 */
public class AddSAndTMetadata extends BaseMetadata
{

	/**
	 * Connection instance.
	 */
	private Connection connection = null;
	/**
	 * Statement instance.
	 */
	private final Statement stmt = null;

	/**
	 * This method adds Meta data.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void addSAndTMetadata() throws SQLException, IOException
	{
		this.populateEntityList();
		this.populateEntityAttributeMap();
		this.populateAttributeColumnNameMap();
		this.populateAttributeDatatypeMap();
		this.populateAttributePrimaryKeyMap();
		this.connection.createStatement();
		final AddEntity addEntity = new AddEntity(this.connection);
		addEntity.addEntity(this.entityList, "CATISSUE_BASE_SHIPMENT", "NULL", 3, 0);
		final AddAttribute addAttribute = new AddAttribute(this.connection,
				this.entityNameAttributeNameMap, this.attributeColumnNameMap,
				this.attributeDatatypeMap, this.attributePrimarkeyMap, this.entityList);
		addAttribute.addAttribute();
		final AddAssociations addAssociations = new AddAssociations(this.connection);
		String entityName = "edu.wustl.catissuecore.domain.shippingtracking.BaseShipment";
		String targetEntityName = "edu.wustl.catissuecore.domain.User";
		addAssociations.addAssociation(entityName, targetEntityName, "CATISSUE_BASE_SHIPMENT",
				"ASSOCIATION", "senderContactPerson", true,
				"edu.wustl.catissuecore.domain.shippingtracking.BaseShipment", null,
				"SENDER_USER_ID", 1, 1, "SRC_DESTINATION", false);

		entityName = "edu.wustl.catissuecore.domain.shippingtracking.BaseShipment";
		targetEntityName = "edu.wustl.catissuecore.domain.User";
		addAssociations.addAssociation(entityName, targetEntityName, "CATISSUE_BASE_SHIPMENT",
				"ASSOCIATION", "receiverContactPerson", true,
				"edu.wustl.catissuecore.domain.shippingtracking.BaseShipment", null,
				"RECEIVER_USER_ID", 1, 1, "SRC_DESTINATION", false);

		entityName = "edu.wustl.catissuecore.domain.shippingtracking.BaseShipment";
		targetEntityName = "edu.wustl.catissuecore.domain.Site";
		addAssociations.addAssociation(entityName, targetEntityName, "CATISSUE_BASE_SHIPMENT",
				"ASSOCIATION", "senderSite", true,
				"edu.wustl.catissuecore.domain.shippingtracking.BaseShipment", null,
				"SENDER_SITE_ID", 1, 1, "SRC_DESTINATION", false);

		entityName = "edu.wustl.catissuecore.domain.shippingtracking.BaseShipment";
		targetEntityName = "edu.wustl.catissuecore.domain.Site";
		addAssociations.addAssociation(entityName, targetEntityName, "CATISSUE_BASE_SHIPMENT",
				"ASSOCIATION", "receiverSite", true,
				"edu.wustl.catissuecore.domain.shippingtracking.BaseShipment", null,
				"RECEIVER_SITE_ID", 1, 1, "SRC_DESTINATION", false);

		entityName = "edu.wustl.catissuecore.domain.shippingtracking.BaseShipment";
		targetEntityName = "edu.wustl.catissuecore.domain.StorageContainer";
		addAssociations.addAssociation(entityName, targetEntityName,
				"CATISSUE_SHIPMENT_CONTAINR_REL", "ASSOCIATION", "containerCollection", true,
				"edu.wustl.catissuecore.domain.shippingtracking.BaseShipment", "CONTAINER_ID",
				"BASE_SHIPMENT_ID", 2, 0, "SRC_DESTINATION", false);
	}

	/**
	 * This method populates Entity Attribute Map.
	 */
	private void populateEntityAttributeMap()
	{
		final List<String> attributes = new ArrayList<String>();
		attributes.add("id");
		attributes.add("label");
		attributes.add("createdDate");
		attributes.add("receiverComments");
		attributes.add("senderComments");
		attributes.add("sendDate");
		attributes.add("activityStatus");

		this.entityNameAttributeNameMap.put(
				"edu.wustl.catissuecore.domain.shippingtracking.BaseShipment", attributes);
	}

	/**
	 * This method populates Attribute Column Name Map.
	 */
	private void populateAttributeColumnNameMap()
	{
		this.attributeColumnNameMap.put("id", "IDENTIFIER");
		this.attributeColumnNameMap.put("label", "LABEL");
		this.attributeColumnNameMap.put("createdDate", "CREATED_DATE");
		this.attributeColumnNameMap.put("receiverComments", "RECEIVER_COMMENTS");
		this.attributeColumnNameMap.put("senderComments", "SENDER_COMMENTS");
		this.attributeColumnNameMap.put("sendDate", "SEND_DATE");
		this.attributeColumnNameMap.put("activityStatus", "ACTIVITY_STATUS");
	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put("id", "long");
		this.attributeDatatypeMap.put("label", "string");
		this.attributeDatatypeMap.put("createdDate", "date");
		this.attributeDatatypeMap.put("receiverComments", "string");
		this.attributeDatatypeMap.put("senderComments", "string");
		this.attributeDatatypeMap.put("sendDate", "date");
		this.attributeDatatypeMap.put("activityStatus", "string");

	}

	/**
	 * This method populates Attribute Primary Key Map.
	 */
	private void populateAttributePrimaryKeyMap()
	{
		this.attributePrimarkeyMap.put("id", "1");
		this.attributePrimarkeyMap.put("label", "0");
		this.attributePrimarkeyMap.put("createdDate", "0");
		this.attributePrimarkeyMap.put("receiverComments", "0");
		this.attributePrimarkeyMap.put("senderComments", "0");
		this.attributePrimarkeyMap.put("sendDate", "0");
		this.attributePrimarkeyMap.put("activityStatus", "0");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityList.add("edu.wustl.catissuecore.domain.shippingtracking.BaseShipment");
	}

	/**
	 * Constructor.
	 * @param connection Connection object.
	 * @throws SQLException SQL Exception
	 */
	public AddSAndTMetadata(Connection connection) throws SQLException
	{
		this.connection = connection;
	}
}