
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * This action contains methods that are called by MultipleSpecimenApplet
 * Currently it includes:
 * 
 * 1. initData 
 * 2. submitSpecimens
 * 
 * @author Rahul Ner
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
	public ActionForward initData(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Map DataListsMap = new HashMap();

		Map specimenClassTypeMap = getSpecimenClassTypeMap();

		List specimenClassList = new ArrayList();
		specimenClassList.add(Constants.SELECT_OPTION);
		specimenClassList.addAll(specimenClassTypeMap.keySet());
		specimenClassTypeMap.put(Constants.SELECT_OPTION, new String[]{Constants.SELECT_OPTION});

		DataListsMap.put(Constants.SPECIMEN_TYPE_MAP, specimenClassTypeMap);
		DataListsMap.put(Constants.SPECIMEN_CLASS_LIST, specimenClassList.toArray());
		DataListsMap.put(Constants.TISSUE_SITE_LIST, Utility.getListForCDE(
				Constants.CDE_NAME_TISSUE_SITE).toArray());
		DataListsMap.put(Constants.TISSUE_SIDE_LIST, Utility.getListForCDE(
				Constants.CDE_NAME_TISSUE_SIDE).toArray());
		DataListsMap.put(Constants.PATHOLOGICAL_STATUS_LIST, Utility.getListForCDE(
				Constants.CDE_NAME_PATHOLOGICAL_STATUS).toArray());

		writeMapToResponse(response, DataListsMap);
		return null;
	}

	/**
	 * This method saves multiple specimens.
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	public ActionForward submitSpecimens(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception

	{
		String target;
		Map resultMap = new HashMap();
		
		try
		{
			Map specimenMap = (Map) request.getAttribute(Constants.INPUT_APPLET_DATA);

			System.out.println("Submitting the specimen : " + specimenMap);

			prepareSpecimanClassMap(specimenMap);

			Map fixedSpecimenMap = appendClassValue(specimenMap);

			//Map SpecimenCollectionMap = (Map) request.getAttribute("MultipleSpecimenCollectionMap");
			//Add Associated Objects.
			Map multipleSpecimenSessionMap = (Map) request.getSession().getAttribute(
					Constants.MULTIPLE_SPECIMEN_MAP_KEY);

			fixedSpecimenMap.putAll(appendClassValue(multipleSpecimenSessionMap));

			MapDataParser specimenParser = new MapDataParser("edu.wustl.catissuecore.domain");

			MapDataParser biohazardsParser = new MapDataParser("edu.wustl.catissuecore.domain");

			Collection specimenCollection = specimenParser.generateData(fixedSpecimenMap);

			Iterator specimenCollectionIterator = specimenCollection.iterator();

			while (specimenCollectionIterator.hasNext())
			{
				Specimen specimen = (Specimen) specimenCollectionIterator.next();

				specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
				specimen.setAvailable(new Boolean(true));
				specimen.setAvailableQuantity(specimen.getQuantity());

				//process associated objects.
				//specimen.setStorageContainer(null);
				//specimen.setType("Frozen Tissue");

				/*				Long id = specimen.getId();
				 Map biohazardsMap = (Map) SpecimenCollectionMap.get(id.toString() + "_" + "bioHazards");
				 specimen.setBiohazardCollection(biohazardsParser.generateData(biohazardsMap));;
				 */
			}

			//validate specimen

			//call bizLogic to save specimenCollection
			
			insertSpecimens(request, specimenCollection);
			
			//clean up activity.
			multipleSpecimenSessionMap = null;
			request.getSession().setAttribute(Constants.SAVED_SPECIMEN_COLLECTION,specimenCollection);
			target = Constants.SUCCESS;

		}
		//return to report page		
		catch (Exception e)
		{
			target = Constants.FAILURE;
			String errorMsg = e.getMessage();
			resultMap.put(Constants.ERROR_DETAIL,errorMsg);
			e.printStackTrace();
		}

		
		resultMap.put(Constants.MULTIPLE_SPECIMEN_RESULT, target);
		writeMapToResponse(response, resultMap);

		return null;
		//		return actionMapping.findForward(target);
	}

	public ActionForward getResult(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String target = (String) request.getParameter(Constants.MULTIPLE_SPECIMEN_RESULT);
		
		Collection specimenCollection = (Collection) request.getSession().getAttribute(Constants.SAVED_SPECIMEN_COLLECTION);
		request.getSession().setAttribute(Constants.SAVED_SPECIMEN_COLLECTION,null);
		
		request.setAttribute(Constants.SAVED_SPECIMEN_COLLECTION,specimenCollection);
	
		ActionMessages msgs = new ActionMessages();
		msgs.add("success",new ActionMessage("multipleSpecimen.add.success",String.valueOf(specimenCollection.size())));
		saveMessages(request,msgs);
		return actionMapping.findForward(target);
	}

	/**
	 *  This method initialize map which contains for each specimen  its selected class.
	 * @param specimenMap inputMap
	 */
	private void prepareSpecimanClassMap(Map specimenMap)
	{

		classMap = new HashMap();

		int noOfSpecimens = Integer.parseInt((String) specimenMap
				.get(AppletConstants.NO_OF_SPECIMENS));
		specimenMap.remove(AppletConstants.NO_OF_SPECIMENS);

		for (int i = 1; i <= noOfSpecimens; i++)
		{
			String classValue = (String) specimenMap.get(AppletConstants.SPECIMEN_PREFIX + i + "_"
					+ "class");

			classMap.put(String.valueOf(i), classValue);

			if (!classValue.equals("Molecular"))
			{
				specimenMap.remove(AppletConstants.SPECIMEN_PREFIX + i + "_"
						+ "concentrationInMicrogramPerMicroliter");
			}

			specimenMap.remove(AppletConstants.SPECIMEN_PREFIX + i + "_" + "class");
			specimenMap.remove(AppletConstants.SPECIMEN_PREFIX + i + "_" + "StorageContainer_temp");
		}
	}

	/**
	 * This method saves collection of specimens to the database.
	 *  TODO Error handling. 
	 * @param request
	 * @param specimenCollection
	 */
	private void insertSpecimens(HttpServletRequest request, Collection specimenCollection)
			throws Exception
	{
		IBizLogic bizLogic;

		bizLogic = AbstractBizLogicFactory.getBizLogic(ApplicationProperties
				.getValue("app.bizLogicFactory"), "getBizLogic", Constants.NEW_SPECIMEN_FORM_ID);
		SessionDataBean sessionBean = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
		// need to remove
/*		sessionBean = new SessionDataBean();
		sessionBean.setUserName("admin@admin.com");
*/
		bizLogic.insert(specimenCollection, sessionBean, Constants.HIBERNATE_DAO);

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
			String newKey = getUpdatedKey(classMap, key);

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
	 * @param classMap map that contains specimen no and its specimen class 
	 * @param key   e.g. 
	 * @return
	 */
	private String getUpdatedKey(Map classMap, String key)
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
			specimenClassTypeMap.put(pValue.getValue(), specimenType.toArray());
		}
		return specimenClassTypeMap;
	}

}
