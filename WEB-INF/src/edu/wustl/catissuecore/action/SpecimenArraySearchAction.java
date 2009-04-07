
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenArrayForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.util.SpecimenArrayAppletUtil;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.CommonSearchAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.common.util.logger.Logger;


/**
 * <p>This class initializes the fields of SpecimenArraySearchAction.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArraySearchAction extends CommonSearchAction {

	/**
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
						throws IOException, ServerException 
	{
		ActionForward forward = super.execute(mapping, form, request, response);
		SpecimenArrayForm specimenArrayForm = (SpecimenArrayForm) form;
		try
		{
			
			List specimenTypeList = new ArrayList();
			
			String[] specimenTypeArr = specimenArrayForm.getSpecimenTypes();
			String specimenType = null;
			NameValueBean nameValueBean = null;
			
			for (int i = 0; i < specimenTypeArr.length; i++) 
			{
				specimenType = specimenTypeArr[i];
				nameValueBean = new NameValueBean(specimenType,specimenType);
				specimenTypeList.add(nameValueBean);
			}
			
			request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
			Collection arrayContentCollection = specimenArrayForm.getSpecArrayContentCollection();
			SpecimenArrayContent arrayContent = null;
			//int rowCount = specimenArrayForm.getOneDimensionCapacity();
			int columnCount = specimenArrayForm.getTwoDimensionCapacity();
			int rowNo = 0;
			int columnNo = 0;
			Map arrayContentMap = new HashMap(); 
			String key = null;
			String value = null;
			
			for (Iterator iter = arrayContentCollection.iterator(); iter.hasNext();) 
			{
				arrayContent = (SpecimenArrayContent) iter.next();
				
				// decrement by 1 because of array index starts from 0 (-1 from stored value).
				rowNo = arrayContent.getPositionDimensionOne().intValue() - 1;
				columnNo = arrayContent.getPositionDimensionTwo().intValue() - 1;
				/**
				 * Name : Virender
				 * Reviewer: Prafull
				 * Retriving specimenObject
				 * replaced arrayContent.getSpecimen()
				 */
				DefaultBizLogic bizLogic =(DefaultBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
				Specimen specimen = null;
				specimen = (Specimen)bizLogic.retrieveAttribute(SpecimenArrayContent.class.getName(),arrayContent.getId(),"specimen");
				if (specimen != null) 
				{
					if ((specimenArrayForm.getEnterSpecimenBy() != null) && (specimenArrayForm.getEnterSpecimenBy().equalsIgnoreCase("Barcode")))
					{
						key = SpecimenArrayAppletUtil.getArrayMapKey(rowNo,columnNo,columnCount,AppletConstants.ARRAY_CONTENT_ATTR_BARCODE_INDEX);
						value = specimen.getBarcode();
					}
					else
					{
						key = SpecimenArrayAppletUtil.getArrayMapKey(rowNo,columnNo,columnCount,AppletConstants.ARRAY_CONTENT_ATTR_LABEL_INDEX);
						value = specimen.getLabel();
					}
					arrayContentMap.put(key,value);
					if (specimen instanceof MolecularSpecimen) 
					{
						// check qunatity not null
						if (arrayContent.getInitialQuantity() != null) 
						{
							key = SpecimenArrayAppletUtil.getArrayMapKey(rowNo,columnNo,columnCount,AppletConstants.ARRAY_CONTENT_ATTR_QUANTITY_INDEX);
							value = arrayContent.getInitialQuantity().toString();
							arrayContentMap.put(key,value);
						}
						
						// check concentration not null
						if (arrayContent.getConcentrationInMicrogramPerMicroliter() != null) 
						{
							key = SpecimenArrayAppletUtil.getArrayMapKey(rowNo,columnNo,columnCount,AppletConstants.ARRAY_CONTENT_ATTR_CONC_INDEX);
							value = arrayContent.getConcentrationInMicrogramPerMicroliter().toString();
							arrayContentMap.put(key,value);
						}
						
	/*					key = SpecimenArrayAppletUtil.getArrayMapKey(rowNo,columnNo,columnCount,AppletConstants.ARRAY_CONTENT_ATTR_SPECIMEN_ID_INDEX);
						value = specimen.getId().toString();
						arrayContentMap.put(key,value);
	*/				}
					
					key = SpecimenArrayAppletUtil.getArrayMapKey(rowNo,columnNo,columnCount,AppletConstants.ARRAY_CONTENT_ATTR_ID_INDEX);
					value = arrayContent.getId().toString();
					arrayContentMap.put(key,value);
					
					key = SpecimenArrayAppletUtil.getArrayMapKey(rowNo,columnNo,columnCount,AppletConstants.ARRAY_CONTENT_ATTR_POS_DIM_ONE_INDEX);
					value = String.valueOf(rowNo);
					arrayContentMap.put(key,value);
					
					key = SpecimenArrayAppletUtil.getArrayMapKey(rowNo,columnNo,columnCount,AppletConstants.ARRAY_CONTENT_ATTR_POS_DIM_TWO_INDEX);
					value = String.valueOf(columnNo);
					arrayContentMap.put(key,value);
				}
			}
			specimenArrayForm.setCreateSpecimenArray("yes");
			// set sub operation :: solution for problem with specimen type selection 
			specimenArrayForm.setSubOperation("");
			request.getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,arrayContentMap);
		}
		catch(DAOException excp)
		{
			Logger.out.error(excp.getMessage(), excp);
			ActionErrors errors = new ActionErrors();
			String objName = SpecimenArray.class.getName();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.unknown", AbstractDomainObject.parseClassName(objName)));
            saveErrors(request,errors);
			
            return mapping.findForward(Constants.FAILURE);
		}
		return forward;
	}
}
