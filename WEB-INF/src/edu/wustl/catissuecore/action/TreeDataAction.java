/**
 * <p>Title: TreeDataAction Class>
 * <p>Description: TreeDataAction creates a tree from the temporary query results 
 * table and passes it to applet.</p>
 * Copyright: Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.AdvanceQueryBizlogic;
import edu.wustl.catissuecore.bizlogic.SpecimenTreeBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.CDEBizLogic;
import edu.wustl.common.tree.TreeDataInterface;
import edu.wustl.common.util.logger.Logger;

/**
 * TreeDataAction creates a tree from the temporary query results table 
 * and passes it to applet.
 * @author gautam_shetty
 */
public class TreeDataAction extends BaseAction
{
	 /**
     * Overrides the execute method in Action class.
     * @param mapping ActionMapping object
     * @param form ActionForm object
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @return ActionForward object
     * @throws Exception object
     */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        ObjectOutputStream out = null;
        Map columnIdsMap = new HashMap();
        
        try
        {
            String pageOf = URLDecoder.decode(request.getParameter(Constants.PAGEOF));
            TreeDataInterface bizLogic = new StorageContainerBizLogic();
            Vector dataList =  new Vector();
            List disableSpecimenIdsList = new ArrayList();
            Map containerMap = new HashMap(), containerRelationMap = new HashMap();
            if (pageOf.equals(Constants.PAGEOF_TISSUE_SITE))
            {
                	bizLogic = new CDEBizLogic();
                	CDEBizLogic cdeBizLogic = (CDEBizLogic) bizLogic;
                	String cdeName = request.getParameter(Constants.CDE_NAME);
                	dataList= cdeBizLogic.getTreeViewData(cdeName);
            }
            else if (pageOf.equals(Constants.PAGEOF_QUERY_RESULTS))
            {
                bizLogic = new AdvanceQueryBizlogic();
                HttpSession session = request.getSession();
                columnIdsMap = (Map)session.getAttribute(Constants.COLUMN_ID_MAP);
                SessionDataBean sessionData = getSessionData(request);
            	dataList = bizLogic.getTreeViewData(sessionData,columnIdsMap, disableSpecimenIdsList);
            }
            else if (pageOf.equals(Constants.PAGEOF_STORAGE_LOCATION) || pageOf.equals(Constants.PAGEOF_MULTIPLE_SPECIMEN) || pageOf.equals(Constants.PAGEOF_SPECIMEN) ||
            		pageOf.equals(Constants.PAGEOF_ALIQUOT))
            {
                dataList = bizLogic.getTreeViewData();
            }
            //Added By Ramya.Instantiate biz logic for Specimen Tree to display in RequestDetails.jsp
            else if(pageOf.equals(Constants.PAGEOF_SPECIMEN_TREE))
            {
            	HttpSession session = request.getSession();
            	if(session.getAttribute(Constants.SPECIMEN_TREE_SPECIMEN_ID) != null)
            	{
            		String strSpecimenId = (String) session.getAttribute(Constants.SPECIMEN_TREE_SPECIMEN_ID);
            		Long specimenId = new Long(strSpecimenId);
            		bizLogic = new SpecimenTreeBizLogic(specimenId,false);
            	}
            	if(session.getAttribute(Constants.SPECIMEN_TREE_SPECCOLLGRP_ID) != null)
            	{
            		String strSpecimenCollgrpId = (String) session.getAttribute(Constants.SPECIMEN_TREE_SPECCOLLGRP_ID);
            		Long specimenCollgrpId = new Long(strSpecimenCollgrpId);
            		bizLogic = new SpecimenTreeBizLogic(specimenCollgrpId,true);
            	}
            	dataList = bizLogic.getTreeViewData();
            }
            
            String contentType = "application/x-java-serialized-object";
            response.setContentType(contentType);
            out = new ObjectOutputStream(response.getOutputStream());
            
            if (pageOf.equals(Constants.PAGEOF_STORAGE_LOCATION) || pageOf.equals(Constants.PAGEOF_SPECIMEN)
                    || pageOf.equals(Constants.PAGEOF_TISSUE_SITE) || pageOf.equals(Constants.PAGEOF_MULTIPLE_SPECIMEN)
                    || pageOf.equals(Constants.PAGEOF_SPECIMEN_TREE) || pageOf.equals(Constants.PAGEOF_ALIQUOT))
            {
                out.writeObject(dataList);
            }
            else
            {
                out.writeObject(dataList);
                out.writeObject(disableSpecimenIdsList);
            }
        }
        catch (Exception exp)
        {
            Logger.out.error(exp.getMessage(), exp);
        }
        finally
        {
            if (out != null)
            {
                out.flush();
                out.close();
            }
        }
        
        return mapping.findForward(Constants.SUCCESS);
    }
}