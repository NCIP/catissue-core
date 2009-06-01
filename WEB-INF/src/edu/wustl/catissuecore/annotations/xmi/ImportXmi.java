					   /**
 * 
 */
package edu.wustl.catissuecore.annotations.xmi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

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
import edu.common.dynamicextensions.xmi.XMIConfiguration;
import edu.common.dynamicextensions.xmi.XMIUtilities;
import edu.common.dynamicextensions.xmi.importer.XMIImportProcessor;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.annotations.PathObject;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.bizlogic.AnnotationUtil;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * @author ashish_gupta
 *
 */
public class ImportXmi
{
	private static Logger logger = Logger.getCommonLogger(ImportXmi.class);
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
			validate(args);
			
			//Ist parameter is fileName
	//		Fully qualified Name of the xmi file to be imported
			String fileName = args[0]; 
			String hookEntity = args[1];
			
				//"C://Documents and Settings//ashish_gupta//Desktop//XMLs//caTissueCore_1.4_Edited.xmi";	
			
			File file = new File(fileName);

			logger.info("--------------------------------------------------\n");
			logger.info("Filename = " +file.getName());
			logger.info("Hook Entity = " +hookEntity);
			
			String packageName = "";			
			String conditionRecordObjectCsvFileName = "";
			String pathCsvFileName = "";
							
			int indexOfExtension = file.getName().lastIndexOf(".");
			String domainModelName = "";

			if(indexOfExtension == -1)
			{
				domainModelName = file.getName();
			}
			else
			{
				domainModelName = file.getName().substring(0,indexOfExtension);
			}
			
			logger.info("Name of the file = " +domainModelName);
			logger.info("\n--------------------------------------------------\n");
			// get the default repository
			rep = MDRManager.getDefault().getDefaultRepository();
			// create an XMIReader
			reader = (XmiReader) Lookup.getDefault().lookup(XmiReader.class);
			
			init();
	
			in = new FileInputStream(file);
	
			// start a read-only transaction
			rep.beginTrans(true);
			
			// read the document
			reader.read(in, null, uml);
			
			if(args.length > 2)
			{
				pathCsvFileName = args[2];
			}
			if(args.length > 3)
			{
				packageName = args[3];
			}
			if(args.length > 4)
			{				
				conditionRecordObjectCsvFileName = args[4];
			}
			if(packageName.equals(""))
			{
				throw new Exception("Package name is mandatory parameter. If no package is present please specify 'Default'.");
			}
			
			List<Long> conditionObjectIds = new ArrayList<Long>();
			if(!conditionRecordObjectCsvFileName.equals(""))
			{
				List<String> conditionObjectNames = readFile(conditionRecordObjectCsvFileName);				
				for(String conditionObjName :conditionObjectNames)
				{
					logger.info("conditionObjName = " + conditionObjName);
					Long cpId =(Long) getObjectIdentifier(conditionObjName,CollectionProtocol.class.getName(),Constants.TITLE);
					if(cpId == null)
					{
						throw new DynamicExtensionsSystemException("Specified Collection Protocol does not exist.");
					}
					conditionObjectIds.add(cpId);
				}
			}
			
			EntityInterface staticEntity = null;
			if(!hookEntity.equalsIgnoreCase(AnnotationConstants.NONE))
			{
				DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
				List staticEntityList = defaultBizLogic.retrieve(AbstractMetadata.class.getName(), Constants.NAME, hookEntity);			
				if(staticEntityList == null || staticEntityList.size() == 0)
				{
					throw new DynamicExtensionsSystemException("Please enter correct Hook Entity name.");
				}
				staticEntity = (EntityInterface) staticEntityList.get(0);
			}
			
			boolean isEntityGroupSystemGenerated = false;
			if(hookEntity.equalsIgnoreCase(AnnotationConstants.NONE))
			{
				isEntityGroupSystemGenerated = true;
			}
			
			List<String> containerNames = readFile(pathCsvFileName);
			XMIImportProcessor xmiImportProcessor = new XMIImportProcessor();
			xmiImportProcessor.setXmiConfigurationObject(getXMIConfiguration(isEntityGroupSystemGenerated));
			List<ContainerInterface> mainContainerList = xmiImportProcessor.processXmi(uml, domainModelName,packageName, containerNames);
			
			boolean isEditedXmi = xmiImportProcessor.isEditedXmi;
			logger.info("\n--------------------------------------------------\n");
			logger.info("Package name = " +packageName);
			logger.info("isEditedXmi = "+isEditedXmi);
			logger.info("Forms have been created !!!!");
			logger.info("Associating with hook entity.");
			logger.info("\n--------------------------------------------------\n");
			//List<ContainerInterface> mainContainerList = getMainContainerList(pathCsvFileName,entityNameVsContainers);
			if(!hookEntity.equalsIgnoreCase(AnnotationConstants.NONE))
			{//Integrating with hook entity
				associateHookEntity(mainContainerList,conditionObjectIds,staticEntity,isEditedXmi);
			}
			else
			{				
				Set<PathObject> processedPathList = new HashSet<PathObject>();				
				for(ContainerInterface mainContainer : mainContainerList)
				{		
					AnnotationUtil.addQueryPathsForAllAssociatedEntities(((EntityInterface)mainContainer.getAbstractEntity()), null, null,null, processedPathList);
				}
			}
			logger.info("--------------- Done ------------");
		
		}
		catch (Exception e)
		{
			logger.info("Fatal error reading XMI.");
			logger.info("------------------------ERROR:--------------------------------\n");
			logger.info(e.getMessage(),e);		
			logger.info("\n--------------------------------------------------------------");
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
				logger.info("Error. Specified file does not exist.");
			}
			XMIUtilities.cleanUpRepository();
		
		}
	}
	/**
	 * 
	 * @param isEntityGroupSystemGenerated true if entity group is system generated
	 * @return XMIConfiguration object.
	 */
	private static XMIConfiguration getXMIConfiguration(boolean isEntityGroupSystemGenerated)
	{
		XMIConfiguration xmiConfiguration = XMIConfiguration.getInstance();
		xmiConfiguration.setCreateTable(true);
		xmiConfiguration.setAddIdAttr(true);
		xmiConfiguration.setAddColumnForInherianceInChild(false);
		xmiConfiguration.setAddInheritedAttribute(false);
		xmiConfiguration.setEntityGroupSystemGenerated(isEntityGroupSystemGenerated);
		return xmiConfiguration;
	}
	/**
	 * @param args
	 * @throws Exception
	 */
	private static void validate(String[] args) throws Exception
	{
		if(args.length == 0)
		{
			logger.debug("Please Specify the file name to be imported");
			throw new Exception("Please Specify the file name to be imported");
		}
		if(args.length < 2)
		{
			logger.debug("Please Specify the hook entity name");
			throw new Exception("Please Specify the hook entity name");
		}
		if(args.length < 3)
		{
			logger.debug("Please Specify the main container csv file name");
			throw new Exception("Please Specify the main container csv file name");
		}
		if(args.length < 4)
		{
			logger.debug("Please Specify the name of the Package to be imported");
			throw new Exception("Please Specify the name of the Package to be imported");
		}	
	}
	/**
	 * @param path
	 * @param entityNameVsContainers
	 * @return
	 * @throws IOException
	 */
//	private static List<ContainerInterface> getMainContainerList(String path,Map<String, List<ContainerInterface>> entityNameVsContainers) throws IOException
//	{
//		
//		List<ContainerInterface> mainContainerList = getContainerObjectList(containerNames,entityNameVsContainers);
//		return mainContainerList;
//	}
	/**
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private static  List<String> readFile(String path) throws IOException
	{
		 List<String> containerNames = new ArrayList<String>();
		File file = new File(path);
	 
		BufferedReader bufRdr  = new BufferedReader(new FileReader(file));
		String line = null;
		 
		//read each line of text file
		while((line = bufRdr.readLine()) != null)
		{	
			StringTokenizer st = new StringTokenizer(line,",");
			while (st.hasMoreTokens())
			{
				//get next token and store it in the array
				containerNames.add(st.nextToken());
			}		
		}
		return containerNames;
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
		logger.info("Here");
		for (Iterator it = umlMM.getMofPackage().refAllOfClass().iterator(); it.hasNext();)
		{
			MofPackage pkg = (MofPackage) it.next();
			logger.info("\n\nName = " + pkg.getName());			
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
	private static void associateHookEntity(List<ContainerInterface> mainContainerList,List<Long> conditionObjectIds,EntityInterface staticEntity,boolean isEditedXmi) throws DAOException, DynamicExtensionsSystemException, BizLogicException, DynamicExtensionsApplicationException
	{		
		Object typeId = getObjectIdentifier("edu.wustl.catissuecore.domain.CollectionProtocol",AbstractMetadata.class.getName(),Constants.NAME);
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();		
		//Set<String> keySet = entityNameVsContainers.keySet();
//		for(String key : keySet)
		if(conditionObjectIds == null || conditionObjectIds.isEmpty())
		{
			conditionObjectIds=new ArrayList<Long>();
			conditionObjectIds.add(Long.valueOf(Constants.DEFAULT_CONDITION));
		}
		for(ContainerInterface container: mainContainerList)
		{
//			List<ContainerInterface> containerList = entityNameVsContainers.get(key);
//			ContainerInterface container = containerList.get(0);
						
			if(isEditedXmi)
			{//Retrieve entity map
				List<EntityMap> entityMapList = defaultBizLogic.retrieve(EntityMap.class.getName(), "containerId", container.getId());
				if(entityMapList != null && entityMapList.size() > 0)
				{
					EntityMap entityMap = entityMapList.get(0);
					if(conditionObjectIds != null)
					{
						editConditions(entityMap,conditionObjectIds,typeId);
					}
					AnnotationBizLogic annotation = new AnnotationBizLogic();
					annotation.updateEntityMap(entityMap);
					
					AnnotationUtil.addNewPathsForExistingMainContainers(staticEntity.getId(), container.getId(), true);
				}
				else
				{//Create new Entity Map
					createIntegrationObjects(container,staticEntity,conditionObjectIds,typeId);
				}
			}
			else
			{//Create new Entity Map
				createIntegrationObjects(container,staticEntity,conditionObjectIds,typeId);
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
	private static void editConditions(EntityMap entityMap,List<Long> conditionObjectIds,Object typeId) throws DynamicExtensionsSystemException, DAOException
	{
		Collection<FormContext> formContexts = new HashSet<FormContext>(AppUtility.getFormContexts(entityMap.getId()));
		for(FormContext formContext : formContexts)
		{
			Collection<EntityMapCondition> entityMapConditions = AppUtility.getEntityMapConditions(formContext.getId());
			
			for(Long collectionProtocolId : conditionObjectIds)
			{
				int temp = 0;
				for(EntityMapCondition condition : entityMapConditions)
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
					entityMapConditions.add(entityMapCondition);
				}				
			}			
			formContext.setEntityMapConditionCollection(entityMapConditions);
		}
		entityMap.setFormContextCollection(formContexts);
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
	private static void createIntegrationObjects(ContainerInterface container,EntityInterface staticEntity,List<Long> conditionObjectIds,Object typeId) throws DynamicExtensionsSystemException, DAOException, BizLogicException, DynamicExtensionsApplicationException
	{
		EntityMap entityMap = getEntityMap(container,staticEntity.getId());
					
		Collection<FormContext> formContextColl = getFormContext(entityMap,conditionObjectIds,typeId);
					
		entityMap.setFormContextCollection(formContextColl);
		AnnotationBizLogic annotation = new AnnotationBizLogic();
		annotation.insert(entityMap, 0);
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
	private static Collection<FormContext> getFormContext(EntityMap entityMap,List<Long> conditionObjectIds,Object typeId) throws DynamicExtensionsSystemException, DAOException
	{
		Collection<FormContext> formContextColl = new HashSet<FormContext>();
		FormContext formContext = new FormContext();
		formContext.setEntityMap(entityMap);
		
		Collection<EntityMapCondition> entityMapConditionColl = new HashSet<EntityMapCondition>();
		if(conditionObjectIds != null)
		{
			for(Long cpId: conditionObjectIds)
			{
				entityMapConditionColl.add(getEntityMapCondition(formContext,cpId,typeId));
			}
			
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
	private static EntityMapCondition getEntityMapCondition(FormContext formContext,Long conditionObjectId,Object typeId) throws DynamicExtensionsSystemException, DAOException
	{	
//		Collection<EntityMapCondition> entityMapCondColl = new HashSet<EntityMapCondition>();
//		for(Long cpId : conditionObjectIds)
//		{
			EntityMapCondition entityMapCondition = new EntityMapCondition();		
			entityMapCondition.setStaticRecordId((conditionObjectId));
						
			entityMapCondition.setTypeId(((Long)typeId));
			entityMapCondition.setFormContext(formContext);	
//			entityMapCondColl.add(entityMapCondition);
//		}
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
	 * @throws BizLogicException 
	 */
	private static Object getObjectIdentifier(String whereColumnValue,String selectObjName,String whereColumnName) throws BizLogicException
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
