package edu.wustl.catissuecore.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.exception.DAOException;


public class CaTissueAjaxAction extends DispatchAction
{

    public ActionForward logout(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            BizLogicException, IOException, DAOException
    {
        HttpSession session = request.getSession();
        session.invalidate();
        return null;
    }
}
