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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
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
	private transient Logger logger = Logger.getCommonLogger(TreeDataAction.class);
    /**
     * Overrides the execute method of the Action class.
     */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        ObjectOutputStream out = null;
        Map columnIdsMap = new HashMap();
        
        try
        {
            String pageOf = URLDecoder.decode(request.getParameter(Constants.PAGE_OF));
            TreeDataInterface bizLogic = new StorageContainerBizLogic();
            List dataList =  new Vector();
            List disableSpecimenIdsList = new ArrayList();
            Map containerMap = new HashMap(), containerRelationMap = new HashMap();
            if (pageOf.equals(Constants.PAGE_OF_TISSUE_SITE))
            {
                	bizLogic = new CDEBizLogic();
                	CDEBizLogic cdeBizLogic = (CDEBizLogic) bizLogic;
                	String cdeName = request.getParameter(Constants.CDE_NAME);
                	dataList= cdeBizLogic.getTreeViewData(cdeName);
            }
            else if (pageOf.equals(Constants.PAGE_OF_STORAGE_LOCATION) || pageOf.equals(Constants.PAGE_OF_MULTIPLE_SPECIMEN) || pageOf.equals(Constants.PAGE_OF_SPECIMEN) ||
            		pageOf.equals(Constants.PAGE_OF_ALIQUOT))
            {
                dataList = bizLogic.getTreeViewData();
            }
            
            String contentType = "application/x-java-serialized-object";
            response.setContentType(contentType);
            out = new ObjectOutputStream(response.getOutputStream());
            
            if (pageOf.equals(Constants.PAGE_OF_STORAGE_LOCATION) || pageOf.equals(Constants.PAGE_OF_SPECIMEN)
                    || pageOf.equals(Constants.PAGE_OF_TISSUE_SITE) || pageOf.equals(Constants.PAGE_OF_MULTIPLE_SPECIMEN)
                    || pageOf.equals(Constants.PAGE_OF_SPECIMEN_TREE) || pageOf.equals(Constants.PAGE_OF_ALIQUOT))
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
        	logger.error(exp.getMessage(), exp);
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