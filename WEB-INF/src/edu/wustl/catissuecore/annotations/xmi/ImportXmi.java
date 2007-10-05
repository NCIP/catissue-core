					   /**
 * 
 */
package edu.wustl.catissuecore.annotations.xmi;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.xmi.XmiReader;

import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.omg.uml.UmlPackage;
import org.openide.util.Lookup;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.AbstractMetadata;
import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.domain.integration.EntityMapCondition;
import edu.common.dynamicextensions.domain.integration.FormContext;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.xmi.XMIUtilities;
import edu.common.dynamicextensions.xmi.importer.XMIImportProcessor;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.bizlogic.AnnotationUtil;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author ashish_gupta
 *
 */
public class ImportXmi
{
//	 name of a UML extent (instance of UML metamodel) that the UML models will be loaded into
	private static final String UML_INSTANCE = "UMLInstance";
	// name of a MOF extent that will contain definition of UML metamodel
	private static final String UML_MM = "UML";

	// repository
	private static MDRepository rep;
	// UML extent
	private static UmlPackage uml;

	// XMI reader
	private static XmiReader reader;
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		FileInputStream in  = null;
		try
		{
			if(args.length == 0)
			{
				throw new Exception("Please Specify the file name to be imported");
			}
			if(args.length < 2)
			{
				throw new Exception("Please Specify the hook entity name");
			}
			
			//Ist parameter is fileName
	//		Fully qualified Name of the xmi file to be imported
			String fileName = args[0]; 
			String hookEntity = args[1];
				//"C://Documents and Settings//ashish_gupta//Desktop//XMLs//caTissueCore_1.4_Edited.xmi";	
			
			fileName = fileName.replaceAll("\\\\", "//");		
			System.out.println("Filename = " +fileName);
			System.out.println("Hook Entity = " +hookEntity);
			String packageName = "";			
			String conditionRecordObjectName = "";
			
						
			
			
			int beginIndex = fileName.lastIndexOf("//");
			int endIndex = fileName.lastIndexOf(".");
			String domainModelName = fileName.substring(beginIndex+2, endIndex);
			
			System.out.println("Name of the file = " +domainModelName);
			
			// get the default repository
			rep = MDRManager.getDefault().getDefaultRepository();
			// create an XMIReader
			reader = (XmiReader) Lookup.getDefault().lookup(XmiReader.class);
			
			init();
	
			in = new FileInputStream(fileName);
	
			// start a read-only transaction
			rep.beginTrans(true);
			
			// read the document
			reader.read(in, null, uml);
						
			if(args.length > 2)
			{
				conditionRecordObjectName = args[2];
			}
			if(args.length > 3)
			{
				packageName = args[3];
			}
			
			Long cpId =(Long) getObjectIdentifier(conditionRecordObjectName,CollectionProtocol.class.getName(),Constants.TITLE);
			if(args.length > 2 && cpId == null)
			{
				throw new DynamicExtensionsSystemException("Specified Collection Protocol does not exist.");
			}				
			
			DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
			List staticEntityList = defaultBizLogic.retrieve(AbstractMetadata.class.getName(), Constants.NAME, hookEntity);			
			if(staticEntityList == null || staticEntityList.size() == 0)
			{
				throw new DynamicExtensionsSystemException("Please enter correct Hook Entity name.");
			}
			EntityInterface staticEntity = (EntityInterface) staticEntityList.get(0);
			
			
			XMIImportProcessor xmiImportProcessor = new XMIImportProcessor();
			Map<String, List<ContainerInterface>> entityNameVsContainers = xmiImportProcessor.processXmi(uml, domainModelName,packageName);
			
			boolean isEditedXmi = xmiImportProcessor.isEditedXmi;
			System.out.println("Package name = " +packageName);
			System.out.println("isEditedXmi = "+isEditedXmi);
			System.out.println("Forms have been created !!!!");
			System.out.println("Associating with hook entity.");
			
			//Integrating with hook entity
			associateHookEntity(entityNameVsContainers,cpId,staticEntity,isEditedXmi);
			System.out.println("--------------- Done ------------");
		
		}
		catch (Exception e)
		{
			System.out.println("Fatal error reading XMI.");
			e.printStackTrace();
		}
		finally
		{
			// release the transaction
			rep.endTrans();
			MDRManager.getDefault().shutdownAll();
			try
			{
				in.close();
			}
			catch(IOException io)
			{
				System.out.println("Error. Specified file does not exist.");
			}
			XMIUtilities.cleanUpRepository();
		
		}
	}

	private static void init() throws Exception
	{
		uml = (UmlPackage) rep.getExtent(UML_INSTANCE);

		if (uml == null)
		{
			// UML extent does not exist -> create it (note that in case one want's to instantiate
			// a metamodel other than MOF, they need to provide the second parameter of the createExtent
			// method which indicates the metamodel package that should be instantiated)
			uml = (UmlPackage) rep.createExtent(UML_INSTANCE, getUmlPackage());			
		}
	}

	/** Finds "UML" package -> this is the topmost package of UML metamodel - that's the
	 * package that needs to be instantiated in order to create a UML extent
	 */
	private static MofPackage getUmlPackage() throws Exception
	{
		// get the MOF extent containing definition of UML metamodel
		ModelPackage umlMM = (ModelPackage) rep.getExtent(UML_MM);
		if (umlMM == null)
		{
			// it is not present -> create it
			umlMM = (ModelPackage) rep.createExtent(UML_MM);
		}
		// find package named "UML" in this extent
		MofPackage result = getUmlPackage(umlMM);		
		if (result == null)
		{
			// it cannot be found -> UML metamodel is not loaded -> load it from XMI
			reader.read(UmlPackage.class.getResource("resources/01-02-15_Diff.xml").toString(),
					umlMM);
			// try to find the "UML" package again
			result = getUmlPackage(umlMM);			
		}
		return result;
	}

	/** Finds "UML" package in a given extent
	 * @param umlMM MOF extent that should be searched for "UML" package.
	 */
	private static MofPackage getUmlPackage(ModelPackage umlMM)
	{
		// iterate through all instances of package
		System.out.println("Here");
		for (Iterator it = umlMM.getMofPackage().refAllOfClass().iterator(); it.hasNext();)
		{
			MofPackage pkg = (MofPackage) it.next();
			System.out.println("\n\nName = " + pkg.getName());			
			// is the package topmost and is it named "UML"?
			if (pkg.getContainer() == null && "UML".equals(pkg.getName()))
			{				
				// yes -> return it
				return pkg;
			}
		}
		// a topmost package named "UML" could not be found
		return null;
	}
	
	/**
	 * @throws DAOException 
	 * @throws DynamicExtensionsSystemException 
	 * @throws UserNotAuthorizedException 
	 * @throws BizLogicException 
	 * @throws DynamicExtensionsApplicationException 
	 * 
	 */
	private static void associateHookEntity(Map<String, List<ContainerInterface>> entityNameVsContainers,Long collectionProtocolId,EntityInterface staticEntity,boolean isEditedXmi) throws DAOException, DynamicExtensionsSystemException, BizLogicException, UserNotAuthorizedException, DynamicExtensionsApplicationException
	{		
		Object typeId = getObjectIdentifier("edu.wustl.catissuecore.domain.CollectionProtocol",AbstractMetadata.class.getName(),Constants.NAME);
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();		
		Set<String> keySet = entityNameVsContainers.keySet();
		for(String key : keySet)
		{
			List<ContainerInterface> containerList = entityNameVsContainers.get(key);
			ContainerInterface container = containerList.get(0);
						
			if(isEditedXmi)
			{//Retrieve entity map
				List<EntityMap> entityMapList = defaultBizLogic.retrieve(EntityMap.class.getName(), "containerId", container.getId());
				if(entityMapList != null && entityMapList.size() > 0)
				{
					EntityMap entityMap = entityMapList.get(0);
					if(collectionProtocolId != null)
					{
						editConditions(entityMap,collectionProtocolId,typeId);
					}
					AnnotationBizLogic annotation = new AnnotationBizLogic();
					annotation.updateEntityMap(entityMap);
				}
				else
				{//Create new Entity Map
					createIntegrationObjects(container,staticEntity,collectionProtocolId,typeId);
				}
			}
			else
			{//Create new Entity Map
				createIntegrationObjects(container,staticEntity,collectionProtocolId,typeId);
			}				
		}
	}
			/**
	 * @param entityMap
	 * @param clinicalStudyId
	 * @param typeId
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException
	 */
	private static void editConditions(EntityMap entityMap,Long collectionProtocolId,Object typeId) throws DynamicExtensionsSystemException, DAOException
	{
		Collection<FormContext> formContextColl = entityMap.getFormContextCollection();
		for(FormContext formContext : formContextColl)
		{
			Collection<EntityMapCondition> entityMapCondColl = formContext.getEntityMapConditionCollection();
			int temp = 0;
			for(EntityMapCondition condition : entityMapCondColl)
			{
				if(condition.getStaticRecordId().compareTo(collectionProtocolId) == 0)
				{
					temp++;
					break;								
				}
			}
			if(temp == 0)
			{
				EntityMapCondition entityMapCondition = getEntityMapCondition(formContext,collectionProtocolId,typeId);
				entityMapCondColl.add(entityMapCondition);
			}
		}
	}
	/**
	 * @param container
	 * @param staticEntity
	 * @param clinicalStudyId
	 * @param typeId
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 * @throws DynamicExtensionsApplicationException
	 */
	private static void createIntegrationObjects(ContainerInterface container,EntityInterface staticEntity,Long collectionProtocolId,Object typeId) throws DynamicExtensionsSystemException, DAOException, BizLogicException, UserNotAuthorizedException, DynamicExtensionsApplicationException
	{
		EntityMap entityMap = getEntityMap(container,staticEntity.getId());
					
		Collection<FormContext> formContextColl = getFormContext(entityMap,collectionProtocolId,typeId);
					
		entityMap.setFormContextCollection(formContextColl);
		AnnotationBizLogic annotation = new AnnotationBizLogic();
		annotation.insert(entityMap, Constants.HIBERNATE_DAO);
		AnnotationUtil.addAssociation(staticEntity.getId(), container.getId(), true);
		
	}
	/**
	 * @param entityMap
	 * @param collectionProtocolName
	 * @param typeId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException
	 */
	private static Collection<FormContext> getFormContext(EntityMap entityMap,Long cpId,Object typeId) throws DynamicExtensionsSystemException, DAOException
	{
		Collection<FormContext> formContextColl = new HashSet<FormContext>();
		FormContext formContext = new FormContext();
		formContext.setEntityMap(entityMap);
		
		Collection<EntityMapCondition> entityMapConditionColl = new HashSet<EntityMapCondition>();
		 if(cpId != null)
		{
			entityMapConditionColl.add(getEntityMapCondition(formContext,cpId,typeId));
		}
		
		formContext.setEntityMapConditionCollection(entityMapConditionColl);
					
		formContextColl.add(formContext);
		
		return formContextColl;
	}
	/**
	 * @param formContext
	 * @param collectionProtocolName
	 * @param typeId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException
	 */
	private static EntityMapCondition getEntityMapCondition(FormContext formContext,Long cpId,Object typeId) throws DynamicExtensionsSystemException, DAOException
	{		
		EntityMapCondition entityMapCondition = new EntityMapCondition();		
		entityMapCondition.setStaticRecordId((cpId));
					
		entityMapCondition.setTypeId(((Long)typeId));
		entityMapCondition.setFormContext(formContext);		
		
		return entityMapCondition;
	}
	/**
	 * @param container
	 * @param hookEntityName
	 * @return
	 * @throws DAOException
	 * @throws DynamicExtensionsSystemException
	 */
	private static EntityMap getEntityMap(ContainerInterface container,Object staticEntityId) throws DAOException, DynamicExtensionsSystemException
	{
		EntityMap entityMap = new EntityMap();
		entityMap.setContainerId(container.getId());
		entityMap.setCreatedBy("");
		entityMap.setCreatedDate(new Date());
		entityMap.setLinkStatus(AnnotationConstants.STATUS_ATTACHED);
				
		entityMap.setStaticEntityId(((Long)staticEntityId));
		
		return entityMap;
	}
	/**
	 * @param hookEntityName
	 * @return
	 * @throws DAOException
	 */
	private static Object getObjectIdentifier(String whereColumnValue,String selectObjName,String whereColumnName) throws DAOException
	{
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		String[] selectColName = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColName = {whereColumnName};
		Object[] whereColValue = {whereColumnValue};
		String[] whereColCondition = {Constants.EQUALS};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		List id = defaultBizLogic.retrieve(selectObjName, selectColName, whereColName, whereColCondition, whereColValue, joinCondition);
		if(id != null && id.size() > 0)
		{
			return id.get(0);
		}
		return null;
	}

}
