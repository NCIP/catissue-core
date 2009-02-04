
package edu.wustl.catissuecore.action;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.CollectionEventParametersForm;
import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ReceivedEventParametersForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CreateSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.MultipleSpecimenValidationUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.factory.AbstractDomainObjectFactory;
import edu.wustl.common.factory.MasterFactory;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This action contains methods that are called by MultipleSpecimenApplet
 * Currently it includes:
 * 
 * 1. initData 
 * 2. submitSpecimens
 * 3. getResult
 * 
 * @author Rahul Ner
 * @author Mandar Deshmukh
 * 
 */
public class MultipleSpecimenAppletAction extends BaseAppletAction
{

	/**
	 * This map contains the mapping for the each specimen and its selected class.
	 * key - specimen no in the applet
	 * values - selected class.
	 * 
	 */
	Map classMap;

	/**
	 * This method is called by Multiple specimen data model during initialization to 
	 * set the list that are displayed in the drop down form.
	 * It returns in following list
	 * 
	 *  1. Tissue site list
	 *  2. Tissue side list
	 *  3. Pathological status list
	 *  4. Map of specimen class and type . refer to getSpecimenClassTypeMap method.
	 * 
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initData(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		Map DataListsMap = new HashMap();

		Map specimenClassTypeMap = getSpecimenClassTypeMap();

		List specimenClassList = new ArrayList();
		specimenClassList.add(Constants.SELECT_OPTION);
		specimenClassList.addAll(specimenClassTypeMap.keySet());
		specimenClassTypeMap.put(Constants.SELECT_OPTION, new String[]{Constants.SELECT_OPTION});

		DataListsMap.put(Constants.SPECIMEN_TYPE_MAP, specimenClassTypeMap);
		DataListsMap.put(Constants.SPECIMEN_CLASS_LIST, specimenClassList.toArray());
		DataListsMap.put(Constants.TISSUE_SITE_LIST, Utility.getListForCDE(Constants.CDE_NAME_TISSUE_SITE).toArray());
		DataListsMap.put(Constants.TISSUE_SIDE_LIST, Utility.getListForCDE(Constants.CDE_NAME_TISSUE_SIDE).toArray());
		DataListsMap.put(Constants.PATHOLOGICAL_STATUS_LIST, Utility.getListForCDE(Constants.CDE_NAME_PATHOLOGICAL_STATUS).toArray());

		//------------specimen collection group
		NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);

		String sourceObjectName = SpecimenCollectionGroup.class.getName();
		String[] displayNameFields = {"name"};
		String valueField = Constants.SYSTEM_IDENTIFIER;

		List specimenGroupList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, true);
		ArrayList specimenGroupArrayList = new ArrayList();
		specimenGroupArrayList = getNameStringArray(specimenGroupList);
		DataListsMap.put(Constants.SPECIMEN_COLLECTION_GROUP_LIST, specimenGroupArrayList.toArray());
		if (request.getSession().getAttribute(Constants.SPECIMEN_COLL_GP_NAME) != null)
		{
			DataListsMap.put(Constants.SPECIMEN_COLL_GP_NAME, request.getSession().getAttribute(Constants.SPECIMEN_COLL_GP_NAME));
			request.getSession().removeAttribute(Constants.SPECIMEN_COLL_GP_NAME);
		}
		// ------------------------------------

		// Mandar : to set columns per page ----- start
		String columns = XMLPropertyHandler.getValue(Constants.MULTIPLE_SPECIMEN_COLUMNS_PER_PAGE);
		DataListsMap.put(Constants.MULTIPLE_SPECIMEN_COLUMNS_PER_PAGE, columns);
		// Mandar : to set columns per page ----- end

		writeMapToResponse(response, DataListsMap);
		return null;
	}

	/**
	 * This method saves multiple specimens.
	 * 
	 * 1. It preprocess specimen map (map from paplet table model).
	 * 2. Converts each specimen to specific type of specimen by appending its class name.
	 * 3. Adds Associates objects which are present in the session to the specimen.
	 * 4. Parses specimen map to convert it to specimen domain object using map data parser.
	 * 5. post Processes each specimen to add inital values like acfivity status. TODO: can be clubed into first step.   
	 * 6. Saves Specimens using bizLogic layer. 
	 * 7. Send success or failure to the applet. In case of failure, messages are also send to applet.
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	public ActionForward submitSpecimens(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception

	{
		String target;
		Map resultMap = new HashMap();

		try
		{
			Map specimenMap = (Map) request.getAttribute(Constants.INPUT_APPLET_DATA);

			Logger.out.debug("Submitting the specimen : " + specimenMap);
			String pageOf = request.getParameter("pageOf");
			preprocessSpecimanMap(specimenMap);
			Map fixedSpecimenMap = appendClassValue(specimenMap);
			Map multipleSpecimenSessionMap = (Map) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_MAP_KEY);

			processAssociatedObjectsMap(fixedSpecimenMap, multipleSpecimenSessionMap);
			MapDataParser specimenParser = new MapDataParser("edu.wustl.catissuecore.domain");
			Collection specimenCollection = specimenParser.generateData(fixedSpecimenMap,true);
			//Read session form bean map to associate derived specimens
			Map multipleSpecimenFormBeanMap = (Map) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_FORM_BEAN_MAP_KEY);

			Map multipleSpecimenEventsFormBean = (Map) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_EVENT_MAP_KEY);
			if (multipleSpecimenEventsFormBean != null)
				processEvents(specimenCollection, multipleSpecimenEventsFormBean);
			Map finalMap = processFormBeansMap(specimenCollection, multipleSpecimenFormBeanMap);

		
			IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			
			
            LinkedHashMap linkedMap = getLinkedHashMap(finalMap);
			MultipleSpecimenValidationUtil.validateMultipleSpecimen(linkedMap,bizLogic,Constants.ADD);
			//dao.commit();
			// end
			// ------------------------------------
			//if success return to report page		
			request.getSession().setAttribute(Constants.SAVED_SPECIMEN_COLLECTION, specimenCollection);
			target = Constants.SUCCESS;

			//clean up activity.
			multipleSpecimenSessionMap = new HashMap();
			request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_MAP_KEY, new HashMap());
			// Populate storage positions -- Ashwin
			Map sessionContainerMap = populateStorageLocations(bizLogic,linkedMap,request);
			addMapToSession(request,linkedMap,Constants.SPECIMEN_MAP_KEY);
			addMapToSession(request,sessionContainerMap,Constants.CONTAINER_MAP_KEY);
			// End
			//request.getSession().removeAttribute(Constants.SPECIMEN_COLL_GP_NAME );  
		}
		catch (Exception e)
		{
			//return to same applet page incase of failure.		
			target = Constants.FAILURE;
			String errorMsg = e.getMessage();
			resultMap.put(Constants.ERROR_DETAIL, errorMsg);
			e.printStackTrace();
		}

		//send response to the applet.
		resultMap.put(Constants.MULTIPLE_SPECIMEN_RESULT, target);
		writeMapToResponse(response, resultMap);
		Logger.out.debug("In MultipleSpecimenAppletAction :- resultMap : " + resultMap);
		return null;
	}

	/**
	 * This method creates a linked map from random hashmap
	 * @param finalMap
	 * @return linked hashmap
	 */
	private LinkedHashMap getLinkedHashMap(Map hashMap)
	{
		Iterator specimenIterator = hashMap.keySet().iterator();
		int mapSize = hashMap.keySet().size();
		int count = 0;
		LinkedHashMap linkedMap = new LinkedHashMap();
		List tempList = new ArrayList();
		while(specimenIterator.hasNext())
		{   
			int i=0;
			Specimen specimen = (Specimen) specimenIterator.next();
			if(tempList.size() == 0)
			{
				tempList.add(specimen);
			}
			else 
			{
				for(;i<tempList.size();i++)
				{
					Specimen tempSpecimen = (Specimen) tempList.get(i);
					if(tempSpecimen.getId().longValue()>specimen.getId().longValue())
					{
						tempList.add(i,specimen);
						break;
					}
					
				}
				if(i==tempList.size())
				{
					tempList.add(i,specimen);
				}
			}
		}
		
		for(int i=0;i<tempList.size();i++)
		{
			Specimen tempSpecimen = (Specimen)tempList.get(i);
			linkedMap.put(tempSpecimen,hashMap.get(tempSpecimen));
		}
		return linkedMap;
	}

	/**
	 *  This method checks whether parent with given parent specimen label exists
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */

	public ActionForward checkParentPresent(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception

	{

		Map specimenMap = (Map) request.getAttribute(Constants.INPUT_APPLET_DATA);

		Boolean isParentPresent = new Boolean(false);
		Map resultMap = new HashMap();
		String parentSpecimenLabel = (String) specimenMap.get("parentSpecimenLabel");
		CreateSpecimenBizLogic dao = (CreateSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.CREATE_SPECIMEN_FORM_ID);
		String collectionGroupName="";
		if (parentSpecimenLabel != null && !parentSpecimenLabel.equals("null") && !parentSpecimenLabel.equals(""))
		{
			List spList = dao.retrieve(Specimen.class.getName(), Constants.SYSTEM_LABEL, parentSpecimenLabel.trim());
			if (spList != null && !spList.isEmpty())
			{
				isParentPresent = new Boolean(true);
				Specimen specimen = (Specimen)spList.get(0);
				collectionGroupName = specimen.getSpecimenCollectionGroup().getName();  
			}
		}

		resultMap.put(Constants.MULTIPLE_SPECIMEN_RESULT, isParentPresent);
		resultMap.put(Constants.MULTIPLE_SPECIMEN_PARENT_COLLECTION_GROUP, collectionGroupName);
		writeMapToResponse(response, resultMap);
		return null;
	}

	private Map processFormBeansMap(Collection specimenCollection, Map multipleSpecimenFormBeanMap) throws Exception
	{
		Map finalSpecimenMap = new HashMap();
		AbstractDomainObjectFactory abstractDomainObjectFactory = (AbstractDomainObjectFactory) MasterFactory.getFactory(ApplicationProperties
				.getValue("app.domainObjectFactory"));

		Iterator specimenCollectionIterator = specimenCollection.iterator();

		//set default values for the specimen.
		while (specimenCollectionIterator.hasNext())
		{
			Specimen specimen = (Specimen) specimenCollectionIterator.next();
			specimen.setAvailable(new Boolean(true));
			specimen.setAvailableQuantity(specimen.getInitialquantity());

			List derivedFormBeans = null;
			//Associate derived specimens.
			if (multipleSpecimenFormBeanMap != null)
			{
				derivedFormBeans = (List) multipleSpecimenFormBeanMap.get(AppletConstants.SPECIMEN_PREFIX + specimen.getId() + "_" + "derive");
			}
			List derivedSpecimens = new ArrayList();

			//  if no derived, continue with next
			if (derivedFormBeans != null)
			{
				for (int i = 0; i < derivedFormBeans.size(); i++)
				{
					CreateSpecimenForm derivedSpecimenFormBean = (CreateSpecimenForm) derivedFormBeans.get(i);
					derivedSpecimenFormBean.setParentSpecimenId(specimen.getId().toString());

					Map externalIdentifiersMap = derivedSpecimenFormBean.getExternalIdentifier();

					/**
					 *  Done to ensure all blank values are removed from externalIdentifiersMap
					 * 
					 */
					if (externalIdentifiersMap != null)
					{
						for (int j = 0; j < externalIdentifiersMap.size(); j++)
						{
							String exIdKey = "ExternalIdentifier:" + (j + 1) + "_id";
							String exNameKey = "ExternalIdentifier:" + (j + 1) + "_name";
							String exNameValue = "ExternalIdentifier:" + (j + 1) + "_value";
							if (externalIdentifiersMap != null && externalIdentifiersMap.get(exNameKey) != null
									&& externalIdentifiersMap.get(exNameKey).toString().equals(""))
							{
								externalIdentifiersMap.remove(exNameKey);
							}
							if (externalIdentifiersMap != null && externalIdentifiersMap.get(exIdKey) != null
									&& externalIdentifiersMap.get(exIdKey).toString().equals(""))
							{
								externalIdentifiersMap.remove(exIdKey);
							}
							if (externalIdentifiersMap != null && externalIdentifiersMap.get(exNameValue) != null
									&& externalIdentifiersMap.get(exNameValue).toString().equals(""))
							{
								externalIdentifiersMap.remove(exNameValue);
							}
						}
					}
					Specimen derivedSpecimen = (Specimen) abstractDomainObjectFactory.getDomainObject(Constants.CREATE_SPECIMEN_FORM_ID,
							derivedSpecimenFormBean);
					derivedSpecimen.setSpecimenCollectionGroup(null);
					derivedSpecimens.add(derivedSpecimen);
				}

			}
			finalSpecimenMap.put(specimen, derivedSpecimens);
		}

		return finalSpecimenMap;
	}

	/**
	 * This method puts all the values defined for the associated object.
	 *  
	 * @param specimenMap
	 * @param multipleSpecimenSessionMap
	 */
	private void processAssociatedObjectsMap(Map specimenMap, Map multipleSpecimenSessionMap)
	{
		if (multipleSpecimenSessionMap == null)
		{
			return;
		}
		Iterator sessionMapItr = multipleSpecimenSessionMap.keySet().iterator();

		while (sessionMapItr.hasNext())
		{
			String key = (String) sessionMapItr.next();

			if (key==null || key.endsWith(Constants.APPEND_COUNT))
			{
				//ignore counts keys
				continue;
			}

			Object associatedObject = multipleSpecimenSessionMap.get(key);

			if (associatedObject instanceof String)
			{
				// e.g. comments objects
				String newKeyInSpecimenMap = getUpdatedKey(key);
				specimenMap.put(newKeyInSpecimenMap, associatedObject);
			}
			else if (associatedObject instanceof Map)
			{
				//e.g. external Identifier			
				Map associatedObjectMap = (Map) associatedObject;

				// remove corrosponding key from main map.
				specimenMap.remove(getUpdatedKey(key));

				Iterator associatedObjectsMapItr = (associatedObjectMap).keySet().iterator();

				while (associatedObjectsMapItr.hasNext())
				{

					String objectKey = (String) associatedObjectsMapItr.next();

					String newKeyInSpecimenMap = getConsolidatedKey(key, objectKey);

					specimenMap.put(newKeyInSpecimenMap, associatedObjectMap.get(objectKey));
				}
			}
		}
	}

	/**
	 * e.g 
	 * if Session map contains key   "Specimen:1_externalIdentifierCollection" and value as a another map,
	 * so key will be "Specimen:1_externalIdentifierCollection" 
	 * and associatedObjectKey is   "ExternalIdentifier:1_id" and to this key some value is associated in that another map.
	 * And if for specimen 1 , "Molecular" class is selected,
	 * 
	 * it will return  MolecularSpecimen:1_ExternalIdentifier:1_id
	 * 
	 */
	private String getConsolidatedKey(String key, String associatedObjectKey)
	{
		String keyWithClass = getUpdatedKey(key);
		String tempKey = keyWithClass.split("_")[0];
		return tempKey + "_" + associatedObjectKey;
	}

	/**
	 * This method is called by applet when submit method return success. It forwards to the report page.
	 */
	public ActionForward getResult(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String target = (String) request.getParameter(Constants.MULTIPLE_SPECIMEN_RESULT);

		Collection specimenCollection = (Collection) request.getSession().getAttribute(Constants.SAVED_SPECIMEN_COLLECTION);
		request.getSession().setAttribute(Constants.SAVED_SPECIMEN_COLLECTION, null);

		request.setAttribute(Constants.SAVED_SPECIMEN_COLLECTION, specimenCollection);

		ActionMessages msgs = new ActionMessages();
		msgs.add("success", new ActionMessage("multipleSpecimen.add.success", String.valueOf(specimenCollection.size())));
		saveMessages(request, msgs);
		return actionMapping.findForward(target);
	}

	/**
	 *  This method processes input specimens map.
	 *  
	 *  1. initialize map which contains for each specimen  its selected class.
	 *  2. If class is not selected for any specimen it throws exception
	 *  3. It removes unneccessary keys(keys that are no longer required) from the map.
	 *   
	 * @param specimenMap inputMap
	 */
	private void preprocessSpecimanMap(Map specimenMap) throws Exception
	{

		classMap = new HashMap();

		int noOfSpecimens = Integer.parseInt((String) specimenMap.get(AppletConstants.NO_OF_SPECIMENS));
		specimenMap.remove(AppletConstants.NO_OF_SPECIMENS);
		// Mandar:20Nov06 : enable Parent Specimen ----------------------- start
		Map radioMap = (Map) specimenMap.get(AppletConstants.MULTIPLE_SPECIMEN_COLLECTION_GROUP_RADIOMAP);
		specimenMap.remove(AppletConstants.MULTIPLE_SPECIMEN_COLLECTION_GROUP_RADIOMAP);
		// Mandar:20Nov06 : enable Parent Specimen ----------------------- end
		
		Validator validator = new Validator();

		for (int i = 1; i <= noOfSpecimens; i++)
		{
			//			 Mandar:20Nov06 : enable Parent Specimen ----------------------- start
			String radiokey = "Specimen:"+i+"_collectionGroupRadioSelected";
			boolean iscollectionGroupSelected = ((Boolean)radioMap.get(radiokey)).booleanValue();
			
			//if collection group selected remove any parent specimen entry
			//else remove any collection group entry
			if(iscollectionGroupSelected)
			{
				//			Specimen Collection Group
				String parentKey = getKey(AppletConstants.SPECIMEN_PARENT_ROW_NO, i);
				validateField(AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO, i, specimenMap, "Specimen Group Name", 2);

				//			Parent Specimen
			    specimenMap.remove(parentKey);
			}
			else
			{
				//			Parent Specimen
				String collectionGroupKey = getKey(AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO, i);
				validateField(AppletConstants.SPECIMEN_PARENT_ROW_NO, i, specimenMap, "Parent Label", 1);

				//			Specimen Collection Group
			    specimenMap.remove(collectionGroupKey);
			    
			    //verify if parent exists
			    String parentKey = getKey(AppletConstants.SPECIMEN_PARENT_ROW_NO, i);
			    String parentLabel = (String)specimenMap.get(parentKey); 
			    verifyParent(parentLabel,specimenMap, i );
			}
			//			 Mandar:20Nov06 : enable Parent Specimen ----------------------- end
			
			//			Label
			//			Label
			validateField(AppletConstants.SPECIMEN_LABEL_ROW_NO, i, specimenMap, "Label", 1);

			//			Specimen Class
			validateField(AppletConstants.SPECIMEN_CLASS_ROW_NO, i, specimenMap, "Class", 2);

			String classKey = getKey(AppletConstants.SPECIMEN_CLASS_ROW_NO, i);
			String classValue = (String) specimenMap.get(classKey);
			
			String typeKey = getKey(AppletConstants.SPECIMEN_TYPE_ROW_NO, i);
			String typeValue = (String) specimenMap.get(typeKey);

			//			Specimen Type
			validateField(AppletConstants.SPECIMEN_TYPE_ROW_NO, i, specimenMap, "Type", 2);

			//			TissueSite
			validateField(AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO, i, specimenMap, "TissueSite", 2);

			//			TissueSide
			validateField(AppletConstants.SPECIMEN_TISSUE_SIDE_ROW_NO, i, specimenMap, "TissueSide", 2);

			//			PathologicalStatus
			validateField(AppletConstants.SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO, i, specimenMap, "Pathological Status", 2);

			//			Quantity
			String quantityKey = getKey(AppletConstants.SPECIMEN_QUANTITY_ROW_NO, i);
			String quantityValue = (String) specimenMap.get(quantityKey);
			try
			{
				//Bug- 2834 : 
				if (validator.isEmpty(quantityValue))
				{
					specimenMap.put(quantityKey,"0");
//					String quantityString = ApplicationProperties.getValue("specimen.quantity");
//					throw new DAOException(ApplicationProperties.getValue("errors.item.required", quantityString));
				}
				else
				{
					try
					{
						quantityValue = new BigDecimal(quantityValue).toPlainString();
						
						if(Utility.isQuantityDouble(classValue,typeValue))
		    			{
		    		        if(!validator.isDouble(quantityValue,true))
		    		        {   		        	
		    		        	String quantityString = ApplicationProperties.getValue("specimen.quantity");
		    					throw new DAOException(ApplicationProperties.getValue("errors.item.format", quantityString));
		    		        }
		    			}
		    			else
		    			{
		    				if(!validator.isNumeric(quantityValue,0))
		    		        {
		    					String quantityString = ApplicationProperties.getValue("specimen.quantity");
		    					throw new DAOException(ApplicationProperties.getValue("errors.item.format", quantityString));        		        	
		    		        }
		    			}
					}
					catch (NumberFormatException exp)
			        {    		  
						String quantityString = ApplicationProperties.getValue("specimen.quantity");
    					throw new DAOException(ApplicationProperties.getValue("errors.item.format", quantityString));
					}
				}
//				Long.parseLong(quantityValue);
//				if (!validator.isPositiveNumeric(quantityValue, 1))
//				{
//					throw new Exception("Quantity should be greater than zero" + " "
//							+ ApplicationProperties.getValue("multiplespecimen.error.forspecimen") + " " + i);
//				}
			}
			catch (NumberFormatException e)
			{
				throw new Exception("Please enter valid Quantity" + " " + ApplicationProperties.getValue("multiplespecimen.error.forspecimen") + " "
						+ i);
			}

			/**
			 *  commented as storage location will be autopopulated
			 */
			//			validateField(AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO, i, specimenMap, "Storage Position", 1);

			classMap.put(String.valueOf(i), classValue);

			if (!classValue.equals("Molecular"))
			{
				specimenMap.remove(AppletConstants.SPECIMEN_PREFIX + i + "_" + "concentrationInMicrogramPerMicroliter");
			}

			specimenMap.remove(AppletConstants.SPECIMEN_PREFIX + i + "_" + "class");
			specimenMap.remove(AppletConstants.SPECIMEN_PREFIX + i + "_" + "StorageContainer_temp");
			specimenMap.remove(AppletConstants.SPECIMEN_PREFIX + i + "_" + "derive");
			specimenMap.remove(AppletConstants.SPECIMEN_PREFIX + i + "_" + AppletConstants.MULTIPLE_SPECIMEN_LOCATION_LABEL);
			specimenMap.put(AppletConstants.SPECIMEN_PREFIX + i + "_" + "activityStatus", Constants.ACTIVITY_STATUS_ACTIVE);

		}
	}

	/**
	 * This method saves collection of specimens to the database.
	 *  TODO Error handling. 
	 * @param request
	 * @param specimenCollection
	 */
	private void insertSpecimens(HttpServletRequest request, Map specimenMap) throws Exception
	{
		IBizLogic bizLogic;

		bizLogic = AbstractBizLogicFactory.getBizLogic(ApplicationProperties.getValue("app.bizLogicFactory"), "getBizLogic",
				Constants.NEW_SPECIMEN_FORM_ID);
		SessionDataBean sessionBean = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);

		bizLogic.insert(specimenMap, sessionBean, Constants.HIBERNATE_DAO);

	}

	/**
	 * This method changes map given by table model and updated following things.
	 * 1. Appends each key with the selected class value.
	 * 
	 * @param specimenMap
	 * @return
	 */
	private Map appendClassValue(Map inputMap)
	{
		Map newMap = new HashMap();

		if (inputMap == null)
		{
			return newMap;
		}

		Iterator it = inputMap.keySet().iterator();
		while (it.hasNext())
		{
			String key = (String) it.next();
			String newKey = getUpdatedKey(key);

			String value = String.valueOf(inputMap.get(key));
			newMap.put(newKey, value);
		}
		return newMap;
	}

	/**
	 * This method returns updated key depending on class selected for a specimen 
	 * 
	 * if user has selected Fluid as specimen class for specimen no 2 then for the key "Specimen:2_pathologicalStatus"
	 * this method will return "FluidSpecimen:2_pathologicalStatus".
	 * 
	 * @param key   e.g. 
	 * @return
	 */
	private String getUpdatedKey(String key)
	{
		String specimenNo = key.substring(key.indexOf(":") + 1, key.indexOf("_"));
		return (String) classMap.get(specimenNo) + key;
	}

	/**
	 * This method returns a map where
	 * 
	 * key - Specimen Class in String form
	 *    |
	 *    |___value arrayList of corrosponding Specimen types.
	 *    
	 *     
	 * @return map
	 */
	private Map getSpecimenClassTypeMap()
	{

		Map specimenClassTypeMap = new HashMap();

		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
		Set setPV = specimenClassCDE.getPermissibleValues();
		Iterator specimenClassItr = setPV.iterator();

		while (specimenClassItr.hasNext())
		{
			PermissibleValue pValue = (PermissibleValue) specimenClassItr.next();
			Set subPV = pValue.getSubPermissibleValues();
			Iterator specimenTypeItr = subPV.iterator();
			List specimenType = new ArrayList();
			specimenType.add(Constants.SELECT_OPTION);
			while (specimenTypeItr.hasNext())
			{
				PermissibleValue specimenTypePV = (PermissibleValue) specimenTypeItr.next();
				specimenType.add(specimenTypePV.getValue());
			}
			Collections.sort(specimenType); 
			specimenClassTypeMap.put(pValue.getValue(), specimenType.toArray());
		}
		return specimenClassTypeMap;
	}

	private void processEvents(Collection specimenCollection, Map multipleSpecimenFormBeanMap) throws Exception
	{

		Iterator specimenCollectionIterator = specimenCollection.iterator();

		//set default values for the specimen.
		// commented for wrong key used to get form -- Ashwin
		//int i = 1;
		while (specimenCollectionIterator.hasNext())
		{
			Specimen specimen = (Specimen) specimenCollectionIterator.next();
			// use specimen.getId() as part of key... here id indicates column no.
			NewSpecimenForm form = (NewSpecimenForm) multipleSpecimenFormBeanMap.get("Specimen:" + specimen.getId().longValue() + "_specimenEventCollection");
			if (form == null)
			{
				throw new Exception("Please enter Events" + " " + ApplicationProperties.getValue("multiplespecimen.error.forspecimen") + " " + specimen.getId());
			}
			else
			{
				Collection specimenEventCollection = new HashSet();

				CollectionEventParametersForm collectionEvent = new CollectionEventParametersForm();
				collectionEvent.setCollectionProcedure(form.getCollectionEventCollectionProcedure());
				collectionEvent.setComments(form.getCollectionEventComments());
				collectionEvent.setContainer(form.getCollectionEventContainer());
				collectionEvent.setTimeInHours(form.getCollectionEventTimeInHours());
				collectionEvent.setTimeInMinutes(form.getCollectionEventTimeInMinutes());
				collectionEvent.setDateOfEvent(form.getCollectionEventdateOfEvent());
				collectionEvent.setUserId(form.getCollectionEventUserId());
				collectionEvent.setOperation(form.getOperation());

				CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
				collectionEventParameters.setAllValues(collectionEvent);
				collectionEventParameters.setSpecimen(specimen);
				
				specimenEventCollection.add(collectionEventParameters);

				//setting received event values
				ReceivedEventParametersForm receivedEvent = new ReceivedEventParametersForm();
				receivedEvent.setComments(form.getReceivedEventComments());
				receivedEvent.setDateOfEvent(form.getReceivedEventDateOfEvent());
				receivedEvent.setReceivedQuality(form.getReceivedEventReceivedQuality());
				receivedEvent.setUserId(form.getReceivedEventUserId());
				receivedEvent.setTimeInMinutes(form.getReceivedEventTimeInMinutes());
				receivedEvent.setTimeInHours(form.getReceivedEventTimeInHours());
				receivedEvent.setOperation(form.getOperation());

				ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
				receivedEventParameters.setAllValues(receivedEvent);
				
				receivedEventParameters.setSpecimen(specimen);
				specimenEventCollection.add(receivedEventParameters);
				
				specimen.setSpecimenEventCollection(specimenEventCollection);

			}
			//i++;

		}
	}

	private ArrayList getNameStringArray(List specimenGroupList)
	{
		ArrayList returnArrayList = new ArrayList();
		for (int cnt = 0; cnt < specimenGroupList.size(); cnt++)
		{
			NameValueBean bean = (NameValueBean) specimenGroupList.get(cnt);
			returnArrayList.add(bean.getName());
		}
		return returnArrayList;
	}

	//-----Mandar : for validation of non associated data           start

	private String getKey(int row, int col)
	{
		// attributes of the specimen.
		String[] specimenAttribute = {"","SpecimenCollectionGroup_name", "ParentSpecimen_label", "label", "barcode", "class", "type",
				"SpecimenCharacteristics_tissueSite", "SpecimenCharacteristics_tissueSide", "pathologicalStatus", "Quantity_value",
				"concentrationInMicrogramPerMicroliter", "StorageContainer_temp", "comments", "specimenEventCollection",
				"externalIdentifierCollection", "biohazardCollection", "derive"};
		String key = AppletConstants.SPECIMEN_PREFIX + col + "_" + specimenAttribute[row];

		return key;
	}

	private void validateField(int row, int col, Map specimenMap, String fieldName, int checkFor) throws Exception
	{
		String key = getKey(row, col);
		String value = (String) specimenMap.get(key);
		Validator validator = new Validator();
		if (checkFor == 1) //	only check for empty : used for textfields
		{
			if (validator.isEmpty(value))
			{
				throw new Exception("Please enter " + fieldName + " " + ApplicationProperties.getValue("multiplespecimen.error.forspecimen") + " "
						+ col);
			}
		}
		else if (checkFor == 2) //	check for empty and valid selections : used for combo box
		{
			if (validator.isEmpty(value) || !validator.isValidOption(value))
			{
				throw new Exception("Please enter " + fieldName + " " + ApplicationProperties.getValue("multiplespecimen.error.forspecimen") + " "
						+ col);
			}
		}

	}
	// -------------------------------------------------------- end
	
	
	// --- Start populate storage positions -- Ashwin
	
	/**
	 * This method populates SCG Id and storage locations for Multiple Specimen
	 * @param dao
	 * @param specimenMap
	 * @param request
	 * @throws DAOException
	 */
	private Map populateStorageLocations(IBizLogic bizLogic, Map specimenMap, HttpServletRequest request) throws DAOException, Exception
	{
		final String saperator = "$";
		Map tempSpecimenMap = new LinkedHashMap();
		Map sessionContainerMap = new LinkedHashMap();
		Iterator specimenIterator = specimenMap.keySet().iterator();
		while (specimenIterator.hasNext())
		{
			Specimen specimen = (Specimen) specimenIterator.next();
			//validate single specimen
			if (specimen.getSpecimenCollectionGroup() != null)
			{
				//TODO remove this code & get CPID from specimen
				/*
				String[] selectColumnName = {"collectionProtocolRegistration.id"};
				String[] whereColumnName = {Constants.NAME};
				String[] whereColumnCondition = {"="};
				String[] whereColumnValue = {specimen.getSpecimenCollectionGroup().getName()};
				List spCollGroupList = bizLogic.retrieve(SpecimenCollectionGroup.class.getName(), selectColumnName, whereColumnName, whereColumnCondition,
						whereColumnValue, null);
				// TODO saperate calls for SCG - ID and cpid
				// SCG - ID will be needed before populateStorageLocations
				
				// TODO test
				
				if (!spCollGroupList.isEmpty())
				{
				*/
					//Object idList[] = (Object[]) spCollGroupList.get(0); // Move up + here
					long cpId = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getId().longValue();
					//Long scgId = (Long) idList[0]; // Move up 
					//long cpId = ((Long) idList[0]).longValue();//here
					//specimen.getSpecimenCollectionGroup().setId(scgId); // Move up
						List tempListOfSpecimen = (ArrayList) tempSpecimenMap.get(cpId + saperator + specimen.getClassName());
						if (tempListOfSpecimen == null)
						{
							tempListOfSpecimen = new ArrayList();
						}
						int i = 0;
						for (; i < tempListOfSpecimen.size(); i++)
						{
							Specimen sp = (Specimen) tempListOfSpecimen.get(i);
							
							if ((sp.getId() != null) && (specimen.getId().longValue() < sp.getId().longValue()))
								break;
						}
						tempListOfSpecimen.add(i, specimen);
						tempSpecimenMap.put(cpId + saperator + specimen.getClassName(), tempListOfSpecimen);
						Logger.out.debug(" cpId + saperator + specimen.getClassName() " + cpId + saperator + specimen.getClassName());
						
						List listOfDerivedSpecimen = (ArrayList) specimenMap.get(specimen);
							// TODO
							if (listOfDerivedSpecimen != null)
							{
								for (int j = 0; j < listOfDerivedSpecimen.size(); j++)
								{
									Specimen tempDerivedSpecimen = (Specimen) listOfDerivedSpecimen.get(j);
									String derivedKey = cpId + saperator + tempDerivedSpecimen.getClassName();
									List listOfSpecimen = (ArrayList) tempSpecimenMap.get(derivedKey);
									if (listOfSpecimen == null)
									{
										listOfSpecimen = new ArrayList();
									}
									listOfSpecimen.add(tempDerivedSpecimen);
									tempSpecimenMap.put(derivedKey, listOfSpecimen);
								}
							}
				// } if statement end
			}
		}

		
		Iterator keyIterator = tempSpecimenMap.keySet().iterator();
		while (keyIterator.hasNext())
		{
			String key = (String) keyIterator.next();
			Logger.out.debug(" ---- Key --- " + key);
			StorageContainerBizLogic scbizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.STORAGE_CONTAINER_FORM_ID);
			String split[] = key.split("[$]");
			// TODO when moved to acion pass true
			SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
			TreeMap containerMap = scbizLogic.getAllocatedContaienrMapForSpecimen((Long.parseLong(split[0])), split[1], 0, "",sessionData, true);
			//mandar : to test for null container 
			if(containerMap == null || containerMap.isEmpty() )
			{
				List specimenList = (List)tempSpecimenMap.get( key);
				String message = "";
				if(!specimenList.isEmpty()  )
				{
					Specimen specimen = (Specimen)specimenList.get(0 );
					String className = specimen.getClassName();
					message = "Class : "+ className+ " and ";
					String collectionGroup="";
					if(specimen.getSpecimenCollectionGroup() != null)
						collectionGroup = specimen.getSpecimenCollectionGroup().getName();
					
					if(collectionGroup != null)
					{
						message = message + "CollectionGroup : "+collectionGroup; 
					}
					else
					{
						String parentId="";
						if(specimen.getParentSpecimen() != null)
							parentId = specimen.getParentSpecimen().getLabel();
						
						message = message + "ParentSpecimen : "+parentId;
					}
				}
				throw (new Exception("No storage container available for  specimen(s) with " + message +". System failed to auto allocate the position." ));
			}
				

			List listOfSpecimens = (ArrayList) tempSpecimenMap.get(key);
			allocatePositionToSpecimensList(specimenMap, listOfSpecimens, containerMap);
			// Added by Ashwin for session container map
			sessionContainerMap.put(key,containerMap);
		}
		return sessionContainerMap;
	}

	/**
	 * This function gets the default positions for list of specimens
	 * @param specimenMap
	 * @param listOfSpecimens
	 * @param containerMap
	 */
	private void allocatePositionToSpecimensList(Map specimenMap, List listOfSpecimens, Map containerMap) throws Exception
	{

		Iterator iterator = containerMap.keySet().iterator();
		int i = 0;
		while (iterator.hasNext())
		{
			NameValueBean nvb = (NameValueBean) iterator.next();
			Map tempMap = (Map) containerMap.get(nvb);
			if (tempMap.size() > 0)
			{
				boolean result = false;
				// commented by Ashwin
				//for (; i < newListOfSpecimen.size(); i++)
				for (; i < listOfSpecimens.size(); i++)
				{
//					 commented by Ashwin
					//Specimen tempSpecimen = (Specimen) newListOfSpecimen.get(i);
					Specimen tempSpecimen = (Specimen) listOfSpecimens.get(i);
					result = allocatePositionToSingleSpecimen(specimenMap, tempSpecimen, tempMap, nvb);
					if (result == false) // container is exhausted
					{ 
						break;
					}
						
				}
				if (result == true)
					break;
			}
		}
		
		if(i < listOfSpecimens.size())
		{
			Specimen tempSpecimen = (Specimen) listOfSpecimens.get(i);
			//there are still some specimens remaining to be allocated but all the applicable locations are exhausted.
			throw (new Exception("No storage position available for specimen " + tempSpecimen.getId() +". System failed to auto allocate the position." ));
		}
	}
	
	/**
	 *  This function gets the default position specimen,the position should not be used by any other specimen in specimenMap
	 *  This is required because we might have given the same position to another specimen.
	 * @param specimenMap
	 * @param tempSpecimen
	 * @param tempMap
	 * @param nvb
	 * @return
	 */
	private boolean allocatePositionToSingleSpecimen(Map specimenMap, Specimen tempSpecimen, Map tempMap, NameValueBean nvbForContainer)
	{
		Iterator itr = tempMap.keySet().iterator();
		String containerId = nvbForContainer.getValue(), xPos, yPos;
		while (itr.hasNext())
		{
			NameValueBean nvb = (NameValueBean) itr.next();
			xPos = nvb.getValue();
	
			List list = (List) tempMap.get(nvb);
			for (int i = 0; i < list.size(); i++)
			{
				nvb = (NameValueBean) list.get(i);
				yPos = nvb.getValue();
				boolean result = checkPositionValidForSpecimen(containerId, xPos, yPos, specimenMap);
				if (result == true)
				{
					StorageContainer tempStorageContainer = new StorageContainer();
					tempStorageContainer.setId(new Long(Long.parseLong(containerId)));
					tempSpecimen.setPositionDimensionOne(new Integer(Integer.parseInt(xPos)));
					tempSpecimen.setPositionDimensionTwo(new Integer(Integer.parseInt(yPos)));
					tempSpecimen.setStorageContainer(tempStorageContainer);
					return true;
				}
			}
	
		}
		return false;
	}
	
	/**
	 * This method checks whether the given parameters match with parameters in specimen Map
	 * @param containerId
	 * @param pos
	 * @param pos2
	 * @param specimenMap
	 * @return
	 */
	private boolean checkPositionValidForSpecimen(String containerId, String xpos, String ypos, Map specimenMap)
	{
	
	     // TODO can be optimised by passing list		
		Iterator specimenIterator = specimenMap.keySet().iterator();
		while (specimenIterator.hasNext())
		{
			Specimen specimen = (Specimen) specimenIterator.next();
			boolean matchFound = checkMatchingPosition(containerId, xpos, ypos, specimen);
			if (matchFound == true)
				return false;
	
			List derivedSpecimens = (List) specimenMap.get(specimen);
	
			if (derivedSpecimens != null)
			{
				for (int i = 0; i < derivedSpecimens.size(); i++)
				{
	
					Specimen derivedSpecimen = (Specimen) derivedSpecimens.get(i);
					matchFound = checkMatchingPosition(containerId, xpos, ypos, derivedSpecimen);
					if (matchFound == true)
						return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * This method checks whether the given parameters match with parameters of the specimen 
	 * @param containerId
	 * @param pos
	 * @param pos2
	 * @param specimen
	 * @return
	 */
	private boolean checkMatchingPosition(String containerId, String xpos, String ypos, Specimen specimen)
	{
		String storageContainerId = "";
		if (specimen.getStorageContainer() != null && specimen.getStorageContainer().getId() != null)
			storageContainerId += specimen.getStorageContainer().getId();
		else
			return false;
	
		String pos1 = specimen.getPositionDimensionOne() + "";
		String pos2 = specimen.getPositionDimensionTwo() + "";
		if (storageContainerId.equals(containerId) && xpos.equals(pos1) && ypos.equals(pos2))
			return true;
		return false;
	}
	
	// --- End -- Ashwin
	
	/**
	 * Add object to session.
	 * @param request request
	 * @param specimenMap specimen map
	 */
	private void addMapToSession(HttpServletRequest request,Map specimenMap,String key)
	{
		request.getSession().setAttribute(key,specimenMap);
	}
	
	
	// 13Nov06 : Mandar : for delete Last ----------------- start
	/**
	 * This method deletes the associated objects for the specified specimen.
	 */
	public ActionForward deleteAssociatedObjects(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Map dataMap = (Map) request.getAttribute(Constants.INPUT_APPLET_DATA);
		Long specimenId = (Long)dataMap.get(Constants.MULTIPLE_SPECIMEN_DELETELAST_SPECIMEN_ID);
		System.out.println("Associated objects to be deleted for Specimen ID: "+ specimenId );
		//events map
		Map eventsMap = (Map) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_EVENT_MAP_KEY);
		Logger.out.debug(">>>>>>>>>>>>>>>>>>>                Before Delete <<<<<<<<<<<<<<<<<");
		Logger.out.debug("Events Map : " );
		Logger.out.debug(eventsMap);

		//other associated objects
		Map multipleSpecimenMap = (Map) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_MAP_KEY);
		Logger.out.debug("Associated Map : " );
		Logger.out.debug(multipleSpecimenMap);

		//delete events object
		String key = "Specimen:"+specimenId.intValue() +"_specimenEventCollection";
		deleteEntry(eventsMap ,key);

		//comments
		key = "Specimen:"+specimenId.intValue() +"_comments";
		deleteEntry(multipleSpecimenMap ,key);
		
		//externalidentifiers
		key = "Specimen:"+specimenId.intValue() +"_externalIdentifierCollection";
		deleteEntry(multipleSpecimenMap ,key);
		key = "Specimen:"+specimenId.intValue() +"_externalIdentifierCollection_count";
		deleteEntry(multipleSpecimenMap ,key);

		//biohazards
		key = "Specimen:"+specimenId.intValue() +"_biohazardCollection";
		deleteEntry(multipleSpecimenMap ,key);
		key = "Specimen:"+specimenId.intValue() +"_biohazardCollection_count";
		deleteEntry(multipleSpecimenMap ,key);
		
		//derive
		//TODO

		Logger.out.debug(">>>>>>>>>>>>>>>>>>>                After Delete <<<<<<<<<<<<<<<<<");
		Logger.out.debug("Events Map : " );
		Logger.out.debug(eventsMap);
		Logger.out.debug("Associated Map : " );
		Logger.out.debug(multipleSpecimenMap);

		// -----------------------------	
		//result to be sent to applet.
		HashMap resultMap = new HashMap(); 
		String target = Constants.SUCCESS;
		
		resultMap.put(Constants.MULTIPLE_SPECIMEN_RESULT, target);
		writeMapToResponse(response, resultMap);
		
		return null;
	}

	private void deleteEntry(Map map, String key)
	{
		if(map.containsKey(key))
		{
			map.remove(key); 
		}
	}
	// 13Nov06 : Mandar : for delete Last ----------------- end
	
//	 Mandar:20Nov06 : enable Parent Specimen ----------------------- start
	private void verifyParent(String parentLabel, Map specimenMap, int count) throws Exception
	{
		//String parentSpecimenLabel = (String) specimenMap.get("parentSpecimenLabel");
		CreateSpecimenBizLogic dao = (CreateSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.CREATE_SPECIMEN_FORM_ID);

		if (parentLabel != null && !parentLabel.equals("null") && !parentLabel.equals(""))
		{
			List spList = dao.retrieve(Specimen.class.getName(), Constants.SYSTEM_LABEL, parentLabel.trim());
			if (spList != null && !spList.isEmpty())
			{
				Specimen specimen = (Specimen)spList.get(0);
				//keys
				// ParentSpecimen_label, ParentSpecimen_id
				String key = AppletConstants.SPECIMEN_PREFIX + count + "_ParentSpecimen_id";
				
				String collectionGroupKey  = getKey(AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO,count  ); 
				
				specimenMap.put(key,specimen.getId());
				specimenMap.put(collectionGroupKey,specimen.getSpecimenCollectionGroup().getName());
			}
			else
			{
				//TODO
				throw new Exception("Parent Specimen is invalid " + ApplicationProperties.getValue("multiplespecimen.error.forspecimen") + " "
						+ count);
			}
		}
	}
//   Mandar:20Nov06 : enable Parent Specimen ----------------------- end
	
	//================================
//	 --------- Changes By  Mandar : 05Dec06 for Bug 2866. ---  Extending SecureAction.  start
	   protected ActionForward invokeMethod(String methodName, ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception 
	   {
		   if(methodName.trim().length() > 0  )
		   {
			   Method method = getMethod(methodName,this.getClass());
			   if(method != null)
			   {
				   Object args[] = {mapping, form, request, response};
				   return (ActionForward) method.invoke(this, args);
			   }
			   else
			   	   return null;
		   }
		   return null;
	   }
//		 --------- Changes By  Mandar : 05Dec06 for Bug 2866. ---  Extending SecureAction.  end

}