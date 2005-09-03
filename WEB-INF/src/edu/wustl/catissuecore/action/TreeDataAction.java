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
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.CDEBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.TreeDataInterface;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * TreeDataAction creates a tree from the temporary query results table 
 * and passes it to applet.
 * @author gautam_shetty
 */
public class TreeDataAction extends Action
{

    /**
     * Overrides the execute method of the Action class.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        ObjectOutputStream out = null;

        try
        {
            //Builds the tree from the result set.
//            ResultData resultData = new ResultData();
//            Vector dataList = resultData.getTreeViewData();
            
//            AbstractBizLogic bizlLogic = BizLogicFactory.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
            
            String pageOf = request.getParameter(Constants.PAGEOF);
            TreeDataInterface bizLogic = new StorageContainerBizLogic();
            
            if (pageOf.equals(Constants.PAGEOF_TISSUE_SITE))
                bizLogic = new CDEBizLogic();
//            else if (pageOf.equals(Constants.PAGEOF_QUERY_RESULTS))
//                bizLogic = new CDEBizLogic();	
            
            Vector dataList = bizLogic.getTreeViewData();
            String contentType = "application/x-java-serialized-object";
            response.setContentType(contentType);
            out = new ObjectOutputStream(response.getOutputStream());
            out.writeObject(dataList);
        }
        catch (Exception exp)
        {
            Logger.out.error(exp.getMessage(), exp);
        }
        finally
        {
            out.flush();
            out.close();
        }
        
        return mapping.findForward(Constants.SUCCESS);
    }
}