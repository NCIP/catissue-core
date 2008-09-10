package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pooja_deshpande
 * Class to add metadata for Shipping & Tracking 
 */
public class AddSAndTMetadata extends BaseMetadata
{	
	private Connection connection = null;
	private static Statement stmt = null;
	
	public void addSAndTMetadata() throws SQLException, IOException
	{
		populateEntityList();
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		populateAttributePrimaryKeyMap();
		
		Statement stmt = connection.createStatement();
		AddEntity addEntity = new AddEntity(connection);
		addEntity.addEntity(entityList, "CATISSUE_BASE_SHIPMENT", "NULL", 3,0);
		
		AddAttribute addAttribute = new AddAttribute(connection,entityNameAttributeNameMap,attributeColumnNameMap,attributeDatatypeMap,attributePrimarkeyMap,entityList);
		addAttribute.addAttribute();
		
		AddAssociations addAssociations = new AddAssociations(connection);
		String entityName = "edu.wustl.catissuecore.domain.shippingtracking.BaseShipment";
		String targetEntityName = "edu.wustl.catissuecore.domain.User";
		addAssociations.addAssociation(entityName,targetEntityName,"CATISSUE_BASE_SHIPMENT","ASSOCIATION","senderContactPerson",true,"edu.wustl.catissuecore.domain.shippingtracking.BaseShipment",null,"SENDER_USER_ID",1,1,"SRC_DESTINATION");
		
		entityName = "edu.wustl.catissuecore.domain.shippingtracking.BaseShipment";
	    targetEntityName = "edu.wustl.catissuecore.domain.User";
	    addAssociations.addAssociation(entityName,targetEntityName,"CATISSUE_BASE_SHIPMENT","ASSOCIATION","receiverContactPerson",true,"edu.wustl.catissuecore.domain.shippingtracking.BaseShipment",null,"RECEIVER_USER_ID",1,1,"SRC_DESTINATION");
	    
		entityName = "edu.wustl.catissuecore.domain.shippingtracking.BaseShipment";
	    targetEntityName = "edu.wustl.catissuecore.domain.Site";
	    addAssociations.addAssociation(entityName,targetEntityName,"CATISSUE_BASE_SHIPMENT","ASSOCIATION","senderSite",true,"edu.wustl.catissuecore.domain.shippingtracking.BaseShipment",null,"SENDER_SITE_ID",1,1,"SRC_DESTINATION");

		entityName = "edu.wustl.catissuecore.domain.shippingtracking.BaseShipment";
	    targetEntityName = "edu.wustl.catissuecore.domain.Site";
	    addAssociations.addAssociation(entityName,targetEntityName,"CATISSUE_BASE_SHIPMENT","ASSOCIATION","receiverSite",true,"edu.wustl.catissuecore.domain.shippingtracking.BaseShipment",null,"RECEIVER_SITE_ID",1,1,"SRC_DESTINATION");

		entityName = "edu.wustl.catissuecore.domain.shippingtracking.BaseShipment";
	    targetEntityName = "edu.wustl.catissuecore.domain.StorageContainer";
	    addAssociations.addAssociation(entityName,targetEntityName,"CATISSUE_SHIPMENT_CONTAINR_REL","ASSOCIATION","containerCollection",true,"edu.wustl.catissuecore.domain.shippingtracking.BaseShipment","CONTAINER_ID","BASE_SHIPMENT_ID",2,0,"SRC_DESTINATION");
	}
		
		private void populateEntityAttributeMap() 
		{
			List<String> attributes = new ArrayList<String>();
			attributes.add("id");
			attributes.add("label");
			attributes.add("createdDate");
			attributes.add("receiverComments");
			attributes.add("senderComments");
			attributes.add("sendDate");
			attributes.add("activityStatus");

			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.shippingtracking.BaseShipment",attributes);		
		}
		
		private void populateAttributeColumnNameMap()
		{
			attributeColumnNameMap.put("id", "IDENTIFIER");
			attributeColumnNameMap.put("label", "LABEL");
			attributeColumnNameMap.put("createdDate", "CREATED_DATE");
			attributeColumnNameMap.put("receiverComments", "RECEIVER_COMMENTS");
			attributeColumnNameMap.put("senderComments", "SENDER_COMMENTS");
			attributeColumnNameMap.put("sendDate", "SEND_DATE");
			attributeColumnNameMap.put("activityStatus", "ACTIVITY_STATUS");
		}
		
		private void populateAttributeDatatypeMap()
		{
			attributeDatatypeMap.put("id", "long");
			attributeDatatypeMap.put("label", "string");
			attributeDatatypeMap.put("createdDate", "date");
			attributeDatatypeMap.put("receiverComments", "string");
			attributeDatatypeMap.put("senderComments", "string");
			attributeDatatypeMap.put("sendDate", "date");
			attributeDatatypeMap.put("activityStatus", "string");

		}
		private void populateAttributePrimaryKeyMap() 
		{
			attributePrimarkeyMap.put("id", "1");
			attributePrimarkeyMap.put("label", "0");
			attributePrimarkeyMap.put("createdDate", "0");
			attributePrimarkeyMap.put("receiverComments", "0");
			attributePrimarkeyMap.put("senderComments", "0");
			attributePrimarkeyMap.put("sendDate", "0");
			attributePrimarkeyMap.put("activityStatus", "0");
		}
		private void populateEntityList()
		{
			entityList.add("edu.wustl.catissuecore.domain.shippingtracking.BaseShipment");
		}

		public AddSAndTMetadata(Connection connection) throws SQLException
		{
			this.connection = connection;
		}
}