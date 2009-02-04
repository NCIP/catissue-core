/*package edu.wustl.catissuecore.bizlogic.test;

import java.util.ArrayList;
import java.util.Date;

import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.wustl.catissuecore.bizlogic.querysuite.GenerateHtmlForAddLimitsBizLogic;
import edu.wustl.common.test.BaseTestCase;

*//**
 * Test case for GenerateHtmlForAddLimitsBizLogic.
 * @author deepti_shelar
 *
 *//*
public class QueryModuleSearchProcessorTest extends BaseTestCase
{
	DomainObjectFactory factory = DomainObjectFactory.getInstance();
	public static String PARTICIPANT_NAME = "edu.wustl.catissuecore.domain.Participant";
	public static Long PARTICIPANT_ID = new Long(1);
	public QueryModuleSearchProcessorTest(String name)
	{
		super(name);		
	}
	*//**
	 * Test if the entity passed to generateHTML method is Participant with no attributes,
	 *//*	
	public void testGenerateHTMLForParticipant()
	{
		StringBuffer generatedHTML = new StringBuffer();
		EntityInterface entity = createParticipantEntity();
		int lastIndex = entity.getName().lastIndexOf(".");
		String entityName = entity.getName().substring(lastIndex + 1);
		String header = "Define Search Rules For";
		generatedHTML.append("<table border=\"0\" width=\"100%\" height=\"100%\" callspacing=\"0\" cellpadding=\"0\">");
		generatedHTML.append("\n<tr>");
		generatedHTML
		.append("<td height=\"4%\" colspan=\"6\" bgcolor=\"#EAEAEA\" style=\"border:solid 1px\"><font face=\"Arial\" size=\"2\" color=\"#000000\"><b>");
		generatedHTML.append(header + " '" + entityName + "'</b></font>");
		generatedHTML.append("\n</td></tr>");
		generatedHTML.append("\n<tr><td height=\"3%\" colspan=\"4\" bgcolor=\"#FFFFFF\">&nbsp;</td></tr>");
		generatedHTML.append(generateHTMLForButton(entityName,""));
		generatedHTML.append("</table>");
		GenerateHtmlForAddLimitsBizLogic qmp = new GenerateHtmlForAddLimitsBizLogic();
		String html = qmp.generateHTML(entity,null);
		assertEquals(html,generatedHTML.toString());
		
	}
	*//**
	 * Test if the entity passed to generateHTML method is Participant with no attributes,
	 *//*	
	public void testGenerateHTMLForParticipantWithNoAttribute()
	{
		StringBuffer generatedHTML = new StringBuffer();
		EntityInterface entity = createParticipantEntityWithNoAttribute();
		int lastIndex = entity.getName().lastIndexOf(".");
		String entityName = entity.getName().substring(lastIndex + 1);
		String header = "Define Search Rules For";
		generatedHTML.append(getInitialHTML());
		generatedHTML.append(header + " '" + entityName + "'</b></font>");
		generatedHTML.append("\n</td></tr>");
		generatedHTML.append("\n<tr><td height=\"3%\" colspan=\"4\" bgcolor=\"#FFFFFF\">&nbsp;</td></tr>");
		generatedHTML.append(generateHTMLForButton(entityName,""));
		generatedHTML.append("</table>");
		GenerateHtmlForAddLimitsBizLogic qmp = new GenerateHtmlForAddLimitsBizLogic();
		String html = qmp.generateHTML(entity,null);
		assertEquals(html,generatedHTML.toString());
	}
	*//**
	 * Generates html for button.
	 * @param name button's name
	 * @param attributesStr attributesStr
	 * @return
	 *//*
	private String generateHTMLForButton(String entityName, String attributesStr)
	{
		String buttonId = "addLimit";
		StringBuffer html = new StringBuffer();
		html.append("\n<tr>");
		html.append("\n<td valign=\"bottom\">");
		html.append("\n<input type=\"button\" name=\"" + buttonId + "\" onClick=\"produceQuery('addToLimitSet.do', 'categorySearchForm', '"
				+ entityName + "','" + attributesStr + "')\" value=\"Add Limit\"></input>");
		html.append("\n</td>");
		html.append("\n</tr>");
		return html.toString();
	}
	*//**
	 * Test if the entity passed to generateHTML method is null, it will return a empty string. 
	 *
	 *//*	
	public void testGenerateHTMLForNullEntity()
	{
		GenerateHtmlForAddLimitsBizLogic qmp = new GenerateHtmlForAddLimitsBizLogic();
		String html = qmp.generateHTML(null,null);
		assertEquals(html,"");
		
	}
	
	 * @param name
	 * Creates a participant entity, sets the attributes collection and
	 * table properties for the entity.
	 
	private EntityInterface createParticipantEntity()
	{
		EntityInterface e = factory.createEntity();
		e.setName(PARTICIPANT_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a participant entity");
		e.setId(PARTICIPANT_ID);
		e.setLastUpdated(new Date());
		((Entity)e).setAbstractAttributeCollection(getParticipantAttributes());
		return e;
	}
	
	 * @param name
	 * Creates a participant entity, sets the attributes collection and
	 * table properties for the entity.
	 
	private EntityInterface createParticipantEntityWithNoAttribute()
	{
		EntityInterface e = factory.createEntity();
		e.setName(PARTICIPANT_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a participant entity");
		e.setId(PARTICIPANT_ID);
		e.setLastUpdated(new Date());
		//((Entity)e).setAbstractAttributeCollection(getParticipantAttributes());
		return e;
	}
	
	 * Creates attributes for participant entity, creates and sets a 
	 * column property for each attribute and adds all the attributes to
	 * a collection.
	 
	private ArrayList getParticipantAttributes()
	{
		ArrayList<AttributeInterface> participantAttributes = new ArrayList<AttributeInterface>();
		
		AttributeInterface att1 = factory.createStringAttribute();
		//att1.setDefaultValue("activityStatus");
		att1.setName("activityStatus");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("ACTIVITY_STATUS");
		((Attribute)att1).setColumnProperties(c1);

		AttributeInterface att2 = factory.createDateAttribute();
		//att2.setDefaultValue(new Date(12 - 03 - 1995));
		att2.setName("birthDate");
		ColumnPropertiesInterface c2 = factory.createColumnProperties();
		c2.setName("BIRTH_DATE");
		((Attribute)att2).setColumnProperties(c2);

		AttributeInterface att3 =  factory.createDateAttribute();
		//att3.setDefaultValue(new Date(12 - 03 - 2005));
		att3.setName("deathDate");
		ColumnPropertiesInterface c3 = factory.createColumnProperties();
		c3.setName("DEATH_DATE");
		((Attribute)att3).setColumnProperties(c3);

		AttributeInterface att4 =  factory.createStringAttribute();
		//att4.setDefaultValue("ethnicity");
		att4.setName("ethnicity");
		ColumnPropertiesInterface c4 = factory.createColumnProperties();
		c4.setName("ETHNICITY");
		((Attribute)att4).setColumnProperties(c4);

		AttributeInterface att5 =  factory.createStringAttribute();
		//att5.setDefaultValue("firstName");
		att5.setName("firstName");
		ColumnPropertiesInterface c5 = factory.createColumnProperties();
		c5.setName("FIRST_NAME");
		((Attribute)att5).setColumnProperties(c5);

		AttributeInterface att6 = factory.createStringAttribute();
		//att6.setDefaultValue("gender");
		att6.setName("gender");
		ColumnPropertiesInterface c6 = factory.createColumnProperties();
		c6.setName("GENDER");
		((Attribute)att6).setColumnProperties(c6);

		AttributeInterface att7 = factory.createLongAttribute();
		//att7.setDefaultValue(20L);
		att7.setName("id");
		
		ColumnPropertiesInterface c7 = factory.createColumnProperties();
		c7.setName("IDENTIFIER");
		((Attribute)att7).setColumnProperties(c7);
		(att7).setIsPrimaryKey(new Boolean(true));

		AttributeInterface att8 = factory.createStringAttribute();;
		//att8.setDefaultValue("lastName");
		att8.setName("lastName");
		ColumnPropertiesInterface c8 = factory.createColumnProperties();
		c8.setName("LAST_NAME");
		((Attribute)att8).setColumnProperties(c8);

		AttributeInterface att9 = factory.createStringAttribute();
		//att9.setDefaultValue("middleName");
		att9.setName("middleName");
		ColumnPropertiesInterface c9 = factory.createColumnProperties();
		c9.setName("MIDDLE_NAME");
		((Attribute)att9).setColumnProperties(c9);

		AttributeInterface att10 = factory.createStringAttribute();
		//att10.setDefaultValue("sexGenotype");
		att10.setName("sexGenotype");
		ColumnPropertiesInterface c10 = factory.createColumnProperties();
		c10.setName("GENOTYPE");
		((Attribute)att10).setColumnProperties(c10);

		AttributeInterface att11 = factory.createStringAttribute();
		//att11.setDefaultValue("socialSecurityNumber");
		att11.setName("socialSecurityNumber");
		ColumnPropertiesInterface c11 = factory.createColumnProperties();
		c11.setName("SOCIAL_SECURITY_NUMBER");
		((Attribute)att11).setColumnProperties(c11);

		AttributeInterface att12 = factory.createStringAttribute();
		//att12.setDefaultValue("vitalStatus");
		att12.setName("vitalStatus");
		ColumnPropertiesInterface c12 = factory.createColumnProperties();
		c12.setName("VITAL_STATUS");
		((Attribute)att12).setColumnProperties(c12);

		participantAttributes.add(0, att1);
		participantAttributes.add(1, att2);
		participantAttributes.add(2, att3);
		participantAttributes.add(3, att4);
		participantAttributes.add(4, att5);
		participantAttributes.add(5, att6);
		participantAttributes.add(6, att7);
		participantAttributes.add(7, att8);
		participantAttributes.add(8, att9);
		participantAttributes.add(9, att10);
		participantAttributes.add(10, att11);
		participantAttributes.add(11, att12);

		return participantAttributes;
	}
	private String getInitialHTML()
	{
		StringBuffer generatedHTML = new StringBuffer();
		
		generatedHTML.append("<table border=\"0\" width=\"100%\" height=\"100%\" callspacing=\"0\" cellpadding=\"0\">");
		generatedHTML.append("\n<tr>");
		generatedHTML.append("<td height=\"4%\" colspan=\"6\" bgcolor=\"#DDEEFF\" style=\"border:solid 1px\"><font face=\"Arial\" size=\"2\" color=\"#000000\"><b>");
		return generatedHTML.toString();
	}



}
*/