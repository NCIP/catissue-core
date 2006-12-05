
package edu.wustl.catissuecore.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.util.MapDataParser;

/**
 * This class handles all the requests related to multiple specimen 
 * 
 * @author Rahul ner
 *
 */
public class MultipleSpecimenAction extends BaseAppletAction
{

	/**
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	public ActionForward submitSpecimens(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		MultipleSpecimenTableModel tableModel = (MultipleSpecimenTableModel) request
				.getAttribute("tableModel");

		Map SpecimenCollectionMap = (Map) request.getAttribute("MultipleSpecimenCollectionMap");

		MapDataParser specimenParser = new MapDataParser("edu.wustl.catissuecore.domain");
		MapDataParser biohazardsParser = new MapDataParser("edu.wustl.catissuecore.domain");

		Collection specimenCollection = specimenParser.generateData(tableModel.getMap());

		Iterator specimenCollectionIterator = specimenCollection.iterator();

		while (specimenCollectionIterator.hasNext())
		{
			Specimen specimen = (Specimen) specimenCollectionIterator.next();
			Long id = specimen.getId();

			Map biohazardsMap = (Map) SpecimenCollectionMap.get(id.toString() + "_" + "bioHazards");
			specimen.setBiohazardCollection(biohazardsParser.generateData(biohazardsMap));;
		}

		//validate specimen

		//call bizLogic to save specimenCollection

		//return to report page		

		return null;
	}

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
		CDEManager cDEManager = CDEManager.getCDEManager();

		DataListsMap.put(Constants.SPECIMEN_TYPE_MAP, getSpecimenClassTypeMap());
		DataListsMap.put(Constants.SPECIMEN_CLASS_LIST, Utility.getListForCDE(
				Constants.CDE_NAME_SPECIMEN_CLASS).toArray());
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
		List specimentypeSelectOption = new ArrayList();
		specimentypeSelectOption.add(Constants.SELECT_OPTION);

		specimenClassTypeMap.put(Constants.SELECT_OPTION, specimentypeSelectOption);

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
