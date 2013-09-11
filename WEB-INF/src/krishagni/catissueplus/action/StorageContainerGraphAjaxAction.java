
package krishagni.catissueplus.action;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;

import edu.wustl.common.action.SecureAction;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;

public class StorageContainerGraphAjaxAction extends SecureAction
{
	private static final Logger LOGGER = Logger.getCommonLogger(StorageContainerGraphAjaxAction.class);
    
    /**
     * Method to get data for displaying storage container graph of specimen count
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException 
     * @throws ApplicationException 
     * @throws SQLException 
     * @throws JSONException 
     */
    @Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
    {
 
             return null;
    }
        
}
