package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddSpecimenClassAttributeMetadata 
{
	private Connection connection = null;
	private static HashMap<String, List<String>> entityNameAttributeNameMap = new HashMap<String, List<String>>();
	private static HashMap<String, String> attributeColumnNameMap = new HashMap<String, String>();
	private static HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	private static HashMap<String, String> attributePrimarkeyMap = new HashMap<String, String>();
	private static List<String> entityList = new ArrayList<String>();

	/*public static void main(String[] args)
	throws Exception
	{
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(true);
		AddSpecimenClassAttributeMetadata addSpecimenClass = new AddSpecimenClassAttributeMetadata(connection);
		addSpecimenClass.addSpecimenClass();
	}*/

	public void addSpecimenClass() throws SQLException, IOException
	{
		populateEntityList();
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		populateAttributePrimaryKeyMap();
		
		AddAttribute addAttribute = new AddAttribute(connection,entityNameAttributeNameMap,attributeColumnNameMap,attributeDatatypeMap,attributePrimarkeyMap,entityList);
		addAttribute.addAttribute();
	}

	private void populateEntityAttributeMap() 
	{
		List<String> attributes = new ArrayList<String>();
		attributes.add("specimenClass");

		entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.AbstractSpecimen",attributes);
	}

	private void populateAttributeColumnNameMap()
	{
		attributeColumnNameMap.put("specimenClass", "SPECIMEN_CLASS");
	}

	private void populateAttributeDatatypeMap()
	{
		attributeDatatypeMap.put("specimenClass", "string");
	}
	private void populateAttributePrimaryKeyMap() 
	{
		attributePrimarkeyMap.put("specimenClass", "0");
	}
	private void populateEntityList()
	{
		entityList.add("edu.wustl.catissuecore.domain.AbstractSpecimen");
	}

	public AddSpecimenClassAttributeMetadata(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
	}
}
