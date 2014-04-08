package edu.wustl.catissuecore.action;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;


public class UploadConsentDocumentAction extends SecureAction {
    
    private transient final Logger logger = Logger
            .getCommonLogger(ExportCollectionProtocolAction.class);

    @Override
    /**
     * Execute Secure Action.
     */
    protected ActionForward executeSecureAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {


        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        String consnentDocumentPath = "";
        /*
         * Set the size threshold, above which content will be stored on disk.
         */
        fileItemFactory.setSizeThreshold(1 * 1024 * 1024); // 1 MB
        /*
         * Set the temporary directory to store the uploaded files of size above
         * threshold.
         */
        JSONObject returnedJObject= new JSONObject();
        String successMessage = "";
        String errorMessage = "";
        HibernateDAO hibernateDAO = null;
        
        try {
            consnentDocumentPath = XMLPropertyHandler.getValue(Constants.PARTICIPANT_CONSENT_DOC_DIR_LOCATION);
            fileItemFactory.setRepository(new File(consnentDocumentPath));
            String cprId = request.getParameter("cprID");
            ServletFileUpload uploadHandler = new ServletFileUpload(
                    fileItemFactory);
            List items = uploadHandler.parseRequest(request);
            Iterator itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                /*
                 * Handle Form Fields.
                 */
                if (item.isFormField()) {

                } else {
                    SessionDataBean sessionDataBean = (SessionDataBean) request
                            .getSession().getAttribute(Constants.SESSION_DATA);
                    hibernateDAO = (HibernateDAO) AppUtility.openDAOSession(sessionDataBean);
                    CollectionProtocolRegistrationBizLogic cprBizLogic = new CollectionProtocolRegistrationBizLogic();

                    
                    cprBizLogic.uploadConsentDocument(Long.parseLong(cprId),
                            item ,hibernateDAO
                            );
                  
                }

            }
            successMessage = "true";
        } catch (ApplicationException e) {
            errorMessage = e.getMessage();
        } catch (FileUploadException ex) {
            errorMessage = Constants.UPLOAD_ERROR_MESSAGE;
        } catch (Exception ex) {
            errorMessage = Constants.UPLOAD_ERROR_MESSAGE;
        }
        finally{
            AppUtility.closeDAOSession(hibernateDAO);
        }
        
        
        try {
            if(!"".equals(successMessage)){
                returnedJObject.put("message", successMessage);
            }else{
                returnedJObject.put("errorMessage", errorMessage);
                
            }
            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        response.setContentType("text/html");
        response.getWriter().write(returnedJObject.toString());

        return mapping.findForward(null);
    }
    
}

