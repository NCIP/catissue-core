
package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.domain.integration.EntityMapCondition;
import edu.common.dynamicextensions.domain.integration.FormContext;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.util.AssociatesCps;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author suhas_khot
 * Test cases for the Show_hide_forms based on collection protocol
 *
 */
public class TestShowHideForms extends TestCase
{

	/**
	 * Specify the name of the XML file. This file must be present at the path ShowHideForms/ under the project root directory
	 */
	private String XMLFileName = "./ShowHideForms/Show_Hide_Forms.xml";
	private String XMLFileName_ALL = "./ShowHideForms/Show_Hide_Forms_ALL.xml";
	private String XMLFileName_NONE = "./ShowHideForms/Show_Hide_Forms_NONE.xml";

	/**
	 * Test Show_hide_forms_based_on_CP for the normal XML file.
	 * throws DynamicExtensionsSystemException when it fails to show/hide forms based on Cps
	 */
	public void testShowHideForms()
	{
		try
		{
			//takes XML file name as argument
			String[] args = {XMLFileName_ALL};
			//calls the main method of AssociatesCps class 
			AssociatesCps.main(args);
			System.out
					.println("--------------- Test Case of ShowHideForms executed successful ------------");
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
			fail("Exception occured");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("Exception occured");
		}
	}

	/**
	 * Test case for associating all forms to CPs
	 */
	public void testForAssociateAllFormsToCP()
	{
		try
		{
			//takes XML file name as argument
			String[] args = {XMLFileName_ALL};
			XMLParser xmlParser = new XMLParser(XMLFileName);
			Collection<String> overrideColl=xmlParser.getCpIdVsOverride().values();
			for(String override : overrideColl)
			{
				assertEquals(override,"true");
			}	
			//calls the main method of AssociatesCps class
			AssociatesCps.main(args);
			DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
			Map<Long, Long> entityIdsVsContId = new HashMap<Long, Long>();
			//store containerIds and its Corresponding entityId
			entityIdsVsContId = Utility.getAllContainers();;
			if (entityIdsVsContId != null)
			{
				for (Long containerId : entityIdsVsContId.values())
				{
					if (containerId != null)
					{
						//retrives the entityMapList from the database based on container Id
						List<EntityMap> entityMapList = defaultBizLogic.retrieve(EntityMap.class
								.getName(), Constants.CONTAINERID, containerId);
						if (entityMapList != null && entityMapList.size() > 0)
						{
							EntityMap entityMap = entityMapList.get(0);
							Collection<FormContext> formContextColl = entityMap
									.getFormContextCollection();
							if (formContextColl != null)
							{
								for (FormContext formContext : formContextColl)
								{
									Collection<EntityMapCondition> entityMapCondColl = formContext
											.getEntityMapConditionCollection();
									if (!entityMapCondColl.isEmpty()
											|| entityMapCondColl.size() > 0)
									{
										int count=0;
//										throw new DynamicExtensionsSystemException(XMLFileName);
										for(EntityMapCondition condition: entityMapCondColl)
										{
											if(condition.getStaticRecordId()==Long.valueOf(-1) || condition.getStaticRecordId()==-1)
											{
												count=count+1;
											}
										}
										if(count==0)
										{
											throw new DynamicExtensionsSystemException(XMLFileName);
										}
									}
									if (entityMapCondColl.isEmpty()
											|| entityMapCondColl.size() <= 0)
									{
										throw new DynamicExtensionsSystemException(XMLFileName);
									}
								}
							}
						}
					}
				}
			}
			System.out
					.println("--------------- Test Case of ShowHideForms_ALL executed successful ------------");
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
			fail("Exception occured");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("Exception occured");
		}
	}

	/**
	 * Test case for disAssociating all forms from CPs
	 */
	public void testForAssociateNoFormsToCP()
	{
		try
		{
			//takes XML file name as argument
			String[] args = {XMLFileName_NONE};
			XMLParser xmlParser = new XMLParser(XMLFileName);
			Collection<String> overrideColl=xmlParser.getCpIdVsOverride().values();
			for(String override : overrideColl)
			{
				assertEquals(override,"true");
			}			
			//calls the main method of AssociatesCps class
			AssociatesCps.main(args);
			DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
			Map<Long, Long> entityIdsVsContId = new HashMap<Long, Long>();
			
			entityIdsVsContId = Utility.getAllContainers();
			if (entityIdsVsContId != null)
			{
				for (Long containerId : entityIdsVsContId.values())
				{
					if (containerId != null)
					{
						//retrives the entityMapList from the database based on container Id
						List<EntityMap> entityMapList = defaultBizLogic.retrieve(EntityMap.class
								.getName(), Constants.CONTAINERID, containerId);
						if (entityMapList != null && entityMapList.size() > 0)
						{
							EntityMap entityMap = entityMapList.get(0);
							Collection<FormContext> formContextColl = entityMap
									.getFormContextCollection();
							if (formContextColl != null)
							{
								for (FormContext formContext : formContextColl)
								{
									Collection<EntityMapCondition> entityMapCondColl = formContext
											.getEntityMapConditionCollection();
									if (!entityMapCondColl.isEmpty()
											|| entityMapCondColl.size() > 0)
									{
										int count=0;
										for(EntityMapCondition condition: entityMapCondColl)
										{
											if(condition.getStaticRecordId()==Long.valueOf(0) || condition.getStaticRecordId()==0)
											{
												count=count+1;
											}
										}
										if(count==0)
										{
											throw new DynamicExtensionsSystemException(XMLFileName);
										}
									}
								}
							}
						}
					}
				}
			}
			System.out
					.println("--------------- Test Case of AssociateNoFormsToCP executed successful ------------");
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
			fail("Exception occured");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("Exception occured");
		}
	}

	/**
	 * Testing for the Forms associated with existing CPs
	 */
	public void testForExistingCp()
	{
		try
		{
			String[] args = {XMLFileName};
			XMLParser xmlParser = new XMLParser(XMLFileName);
			Collection<String> overrideColl=xmlParser.getCpIdVsOverride().values();
			for(String override : overrideColl)
			{
				assertEquals(override,"true");
			}	
			AssociatesCps.main(args);
			EntityManagerInterface entityManager = EntityManager.getInstance();
			List<Long> staticRecordIdColl = retrieveEntityGroup();			
			List<Long> entityMapCondColl=new ArrayList<Long>();
			entityMapCondColl.addAll(xmlParser.getCpIdVsOverride().keySet());
			for (Long staticRecordId : staticRecordIdColl)
			{
				if (staticRecordId != 0)
				{
					Collection<EntityMapCondition> entMapCondColl = entityManager
							.getAllConditionsByStaticRecordId(staticRecordId);
					for(EntityMapCondition entityMapCond : entMapCondColl)
					{
						if(entityMapCond.getStaticRecordId()!=null)
						{
							if(!entityMapCondColl.contains(entityMapCond.getStaticRecordId()))
							{
								throw new DynamicExtensionsSystemException(null);
							}
						}
					}
				}

			}
			System.out.println("--------------- Test Case of TestForNewCp executed successful ------------");
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
			fail("Exception occured");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("Exception occured");
		}

	}

	/**
	 * @return collection of static record Id
	 * @throws DAOException if fails to retrieve the objects form database
	 */
	@SuppressWarnings("unchecked")
	private List<Long> retrieveEntityGroup() throws DAOException
	{
		List<Long> staticRecordIdColl = null;
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		String[] colName = {"staticRecordId"};
		staticRecordIdColl = defaultBizLogic.retrieve(EntityMapCondition.class.getName(), colName);//retrieve(EntityMapCondition.class.getName(),"staticRecordId");
		return staticRecordIdColl;
	}
}
