
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * AddAssociation class.
 */
public class AddAssociation
{

	/**
	 * Specify Connection instance.
	 */
	private Connection connection = null;
	/**
	 * Specify Statement instance.
	 */
	private Statement stmt = null;

	/**
	 * adds Association.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void addAssociation() throws SQLException, IOException
	{
		final AddAssociations addAssociations = new AddAssociations(this.connection);
		String entityName = "edu.wustl.catissuecore.domain.Specimen";
		String targetEntityName = "edu.wustl.catissuecore.domain.SpecimenPosition";
		addAssociations.addAssociation(entityName, targetEntityName, "specimen_specimenPosition",
				"ASSOCIATION", "specimen", true, "specimenPosition", "SPECIMEN_ID", null, 1, 1,
				"BI_DIRECTIONAL");
		addAssociations.addAssociation(targetEntityName, entityName, "specimenPosition_specimen",
				"ASSOCIATION", "specimenPosition", false, "", "SPECIMEN_ID", null, 1, 0,
				"BI_DIRECTIONAL");

		entityName = "edu.wustl.catissuecore.domain.User";
		targetEntityName = "edu.wustl.catissuecore.domain.CollectionProtocol";
		addAssociations.addAssociation(entityName, targetEntityName, "CATISSUE_USER_CP",
				"ASSOCIATION", "assignedProtocolCollection", true,
				"assignedProtocolUserCollection", "COLLECTION_PROTOCOL_ID", "USER_ID", 2, 1,
				"BI_DIRECTIONAL");
		addAssociations.addAssociation(targetEntityName, entityName, "CATISSUE_USER_CP",
				"ASSOCIATION", "assignedProtocolUserCollection", false,
				"assignedProtocolCollection", "COLLECTION_PROTOCOL_ID", "USER_ID", 2, 0,
				"BI_DIRECTIONAL");

		entityName = "edu.wustl.catissuecore.domain.CollectionProtocol";
		targetEntityName = "edu.wustl.catissuecore.domain.Site";
		addAssociations.addAssociation(entityName, targetEntityName, "CATISSUE_SITE_CP",
				"ASSOCIATION", "siteCollection", true, "collectionProtocolCollection", "SITE_ID",
				"COLLECTION_PROTOCOL_ID", 2, 1, "BI_DIRECTIONAL");
		addAssociations.addAssociation(targetEntityName, entityName, "CATISSUE_SITE_CP",
				"ASSOCIATION", "collectionProtocolCollection", false, "siteCollection", "SITE_ID",
				"COLLECTION_PROTOCOL_ID", 2, 0, "BI_DIRECTIONAL");

		entityName = "edu.wustl.catissuecore.domain.Site";
		targetEntityName = "edu.wustl.catissuecore.domain.User";
		addAssociations.addAssociation(entityName, targetEntityName, "CATISSUE_SITE_USERS",
				"ASSOCIATION", "siteCollection", true, "assignedSiteUserCollection", "USER_ID",
				"SITE_ID", 2, 1, "BI_DIRECTIONAL");
		addAssociations.addAssociation(targetEntityName, entityName, "CATISSUE_SITE_USERS",
				"ASSOCIATION", "assignedSiteUserCollection", false, "siteCollection", "USER_ID",
				"SITE_ID", 2, 0, "BI_DIRECTIONAL");

		entityName = "edu.wustl.catissuecore.domain.Site";
		targetEntityName = "edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup";
		addAssociations.addAssociation(entityName, targetEntityName, "site_abstractSCG",
				"ASSOCIATION", "abstractSpecimenCollectionGroupCollection", false, null, null,
				"SITE_ID", 2, 1, "BI_DIRECTIONAL");

		entityName = "edu.wustl.catissuecore.domain.OrderItem";
		targetEntityName = "edu.wustl.catissuecore.domain.DistributedItem";
		addAssociations.addAssociation(entityName, targetEntityName, "CATISSUE_ORDER_ITEM",
				"ASSOCIATION", "edu.wustl.catissuecore.domain.OrderItem", true, "distributedItem",
				null, "DISTRIBUTED_ITEM_ID", 1, 1, "SRC_DESTINATION");

		entityName = "edu.wustl.catissuecore.domain.Container";
		targetEntityName = "edu.wustl.catissuecore.domain.ContainerPosition";
		addAssociations.addAssociation(entityName, targetEntityName, "container_containerPosition",
				"CONTAINTMENT", "occupiedPositions", true, "occupiedContainer",
				"PARENT_CONTAINER_ID", null, 2, 1, "BI_DIRECTIONAL");
		addAssociations.addAssociation(targetEntityName, entityName, "containerPosition_container",
				"ASSOCIATION", "occupiedContainer", false, "", "PARENT_CONTAINER_ID", null, 2, 1,
				"BI_DIRECTIONAL");

		entityName = "edu.wustl.catissuecore.domain.StorageContainer";
		targetEntityName = "edu.wustl.catissuecore.domain.SpecimenPosition";
		addAssociations.addAssociation(entityName, targetEntityName,
				"storageContainer_specimenPosition", "CONTAINTMENT", "specimenPositionCollection",
				true, "storageContainer", "CONTAINER_ID", null, 2, 1, "BI_DIRECTIONAL");
		addAssociations.addAssociation(targetEntityName, entityName,
				"specimenPosition_storageContainer", "ASSOCIATION", "storageContainer", false, "",
				"CONTAINER_ID", null, 2, 1, "BI_DIRECTIONAL");

		entityName = "edu.wustl.catissuecore.domain.shippingtracking.Shipment";
		targetEntityName = "edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest";
		addAssociations.addAssociation(entityName, targetEntityName, "CATISSUE_SHIPMENT",
				"ASSOCIATION", "edu.wustl.catissuecore.domain.shippingtracking.Shipment", true,
				"shipmentRequest", null, "SHIPMENT_REQUEST_ID", 1, 1, "SRC_DESTINATION");

		entityName = "edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest";
		targetEntityName = "edu.wustl.catissuecore.domain.Specimen";
		addAssociations.addAssociation(entityName, targetEntityName,
				"CATISSUE_SPECI_SHIPMNT_REQ_REL", "ASSOCIATION",
				"edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest", true,
				"specimenCollection", "SPECIMEN_ID", "SHIPMENT_REQ_ID", 2, 0, "SRC_DESTINATION");

	}

	/**
	 * Constructor.
	 * @param connection connection object.
	 * @throws SQLException SQL Exception
	 */
	public AddAssociation(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
}
