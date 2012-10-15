/**
 * AddEMPIMetaData.java
 * Purpose:
 */

package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.dao.exception.DAOException;

/**
 * The Class AddEMPIMetaData.
 * 
 * @author skhot
 */
public class AddEMPIMetaData
{
	
	private Connection connection = null;

	/** The entity list. */
	private List<String> entityList = new ArrayList<String>();
	
	/** The entity attr map. */
	private Map<String, List<String>> entityAttrMap = new HashMap<String, List<String>>();
	
	/** The attr column map. */
	private Map<String, String> attrColumnMap = new HashMap<String, String>();
	
	/** The attribute type map. */
	private Map<String, String> attributeTypeMap = new HashMap<String, String>();
	
	/** The attr primarkey map. */
	private Map<String, String> attrPrimarkeyMap = new HashMap<String, String>();


	/**
	 * Adds the empi metadata.
	 * 
	 * @throws SQLException the SQL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws DAOException the DAO exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public void addEMPIMetadata() throws SQLException, IOException, DAOException
	{
		populateEntityList();
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		populateAttributePrimaryKeyMap();

		AddAttribute addAttribute = new AddAttribute(connection, entityAttrMap, attrColumnMap,
				attributeTypeMap, attrPrimarkeyMap, entityList);
		UpdateMetadataUtil.isExecuteStatement = true;
		addAttribute.addAttribute();
	}

	/**
	 * Populate entity list.
	 */
	private void populateEntityList()
	{
		entityList.add("edu.wustl.catissuecore.domain.Participant");
		entityList.add("edu.wustl.catissuecore.domain.Site");
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol");
	}

	/**
	 * Populate entity attribute map.
	 */
	private void populateEntityAttributeMap()
	{
		List<String> participantAttrs = new ArrayList<String>();
		participantAttrs.add("empiId");
		participantAttrs.add("empiIdStatus");
		participantAttrs.add("metaPhoneCode");
		entityAttrMap.put("edu.wustl.catissuecore.domain.Participant", participantAttrs);

		List<String> siteAttrs = new ArrayList<String>();
		siteAttrs.add("facilityId");
		entityAttrMap.put("edu.wustl.catissuecore.domain.Site", siteAttrs);

		List<String> collectionProtocolAttrs = new ArrayList<String>();
		collectionProtocolAttrs.add("isEMPIEnabled");
		entityAttrMap.put("edu.wustl.catissuecore.domain.CollectionProtocol", collectionProtocolAttrs);

	}

	/**
	 * Populate attribute column name map.
	 */
	private void populateAttributeColumnNameMap()
	{
		attrColumnMap.put("empiId", "EMPI_ID");
		attrColumnMap.put("empiIdStatus", "EMPI_ID_STATUS");
		attrColumnMap.put("metaPhoneCode", "LNAME_METAPHONE");

		attrColumnMap.put("facilityId", "FACILITY_ID");

		attrColumnMap.put("isEMPIEnabled", "IS_EMPI_ENABLE");
	}

	/**
	 * Populate attribute datatype map.
	 */
	private void populateAttributeDatatypeMap()
	{
		attributeTypeMap.put("empiId", "string");
		attributeTypeMap.put("empiIdStatus", "string");
		attributeTypeMap.put("metaPhoneCode", "string");
		
		attributeTypeMap.put("facilityId", "string");

		attributeTypeMap.put("isEMPIEnabled", "int");
	}

	/**
	 * Populate attribute primary key map.
	 */
	private void populateAttributePrimaryKeyMap()
	{
		attrPrimarkeyMap.put("empiId", "0");
		attrPrimarkeyMap.put("empiIdStatus", "0");
		attrPrimarkeyMap.put("metaPhoneCode", "0");

		attrPrimarkeyMap.put("facilityId", "0");

		attrPrimarkeyMap.put("isEMPIEnabled", "0");
	}

	/**
	 * The main method.
	 * 
	 * @param args the args
	 * 
	 * @throws SQLException the SQL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 * @throws DAOException the DAO exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public static void main(String[] args) throws SQLException, IOException,
			ClassNotFoundException, DAOException
	{
		AddEMPIMetaData obj = new AddEMPIMetaData();
		obj.connection = DBConnectionUtil.getDBConnection(args);
		obj.addEMPIMetadata();
	}
}
