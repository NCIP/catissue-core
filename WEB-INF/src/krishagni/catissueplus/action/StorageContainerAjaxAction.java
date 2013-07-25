
package krishagni.catissueplus.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krishagni.catissueplus.bizlogic.StorageContainerBizlogic;
import krishagni.catissueplus.dto.StorageContainerStoredSpecimenDetailsDTO;
import krishagni.catissueplus.dto.StorageContainerUtilizationDetailsDTO;
import krishagni.catissueplus.dto.StorageContainerViewDTO;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.HibernateDAO;

public class StorageContainerAjaxAction extends DispatchAction
{

    /**
     * Method returns container details DTO to be populated in dhtmlxDataView
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ApplicationException
     * @throws IOException
     * @throws JSONException 
     */
    public ActionForward getStorageContainerForDataView(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws ApplicationException, IOException,
            JSONException
    {

        String containerName = request.getParameter(Constants.CONTAINER_NAME);//container name selected

        Gson gson = new Gson();

        StorageContainerViewDTO storageContainerViewDTO = new StorageContainerViewDTO();

        HibernateDAO dao = null;
        dao = (HibernateDAO) AppUtility.openDAOSession(null);

        StorageContainerBizlogic storageContainerBizlogic = new StorageContainerBizlogic();

        storageContainerViewDTO = storageContainerBizlogic.getContainerDetailsForDataView(containerName, dao);//get the container details DTO

        AppUtility.closeDAOSession(dao);

        JSONObject dimensionOneLabels = new JSONObject();
        JSONObject dimensionTwoLabels = new JSONObject();

        for (int i = 0; i < storageContainerViewDTO.getOneDimensionCapacity(); i++)
        {
            //DimensionOne label with respective dimension label scheming
            dimensionOneLabels.put(String.valueOf(i + 1),
                    AppUtility.getPositionValue(storageContainerViewDTO.getOneDimensionLabellingScheme(), i + 1));
        }

        for (int i = 0; i < storageContainerViewDTO.getTwoDimensionCapacity(); i++)
        {
            //DimensionOne label with respective dimension label scheming
            dimensionTwoLabels.put(String.valueOf(i + 1),
                    AppUtility.getPositionValue(storageContainerViewDTO.getTwoDimensionLabellingScheme(), i + 1));
        }

        JSONObject returnedJObject = new JSONObject();
        returnedJObject.put(Constants.POS1_CONTROL_NAME, request.getSession().getAttribute(Constants.POS1));
        returnedJObject.put(Constants.POS2_CONTROL_NAME, request.getSession().getAttribute(Constants.POS2));
        returnedJObject.put(Constants.CONTROL_NAME, request.getSession().getAttribute(Constants.CONTROL_NAME));
        returnedJObject.put(Constants.PAGEOF, request.getSession().getAttribute(Constants.PAGEOF));
        returnedJObject.put(Constants.DIMENSION_ONE_LABELS, dimensionOneLabels.toString());
        returnedJObject.put(Constants.DIMENSION_TWO_LABELS, dimensionTwoLabels.toString());
        returnedJObject.put(Constants.CONTAINER_DTO, gson.toJson(storageContainerViewDTO));

        response.setContentType("application/json");
        response.getWriter().write(returnedJObject.toString());

        return null;
    }

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
    public ActionForward getStorageContainerSpecCountDataForGraph(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ApplicationException,
            SQLException, JSONException
    {
        HibernateDAO hibernateDAO = (HibernateDAO) AppUtility.openDAOSession(null);

        String siteName = request.getParameter(Constants.SITE_NAME);

        ArrayList<StorageContainerUtilizationDetailsDTO> storageContainerUtilizationDetailsDTOList = new StorageContainerBizlogic()
                .getStorageContainerUtilizationDetailsDTOList(hibernateDAO, siteName);

        AppUtility.closeDAOSession(hibernateDAO);

        response.setContentType("application/json");
        response.getWriter().write(
                getJSONFromStorageContainerUtilizationDetailsDTOList(storageContainerUtilizationDetailsDTOList));

        return null;
    }

    /**
     * Returns JSON string for storageContainerUtilizationDetailsDTOList
     * @param storageContainerUtilizationDetailsDTOList
     * @return
     * @throws JSONException 
     */
    private String getJSONFromStorageContainerUtilizationDetailsDTOList(
            ArrayList<StorageContainerUtilizationDetailsDTO> storageContainerUtilizationDetailsDTOList)
            throws JSONException
    {
        JSONArray storageContainerSpecCountGraphDataArray = new JSONArray();
        for (StorageContainerUtilizationDetailsDTO storageContainerUtilizationDetailsDTO : storageContainerUtilizationDetailsDTOList)
        {

            ArrayList<StorageContainerStoredSpecimenDetailsDTO> storageContainerStoredSpecimenDetailsDTOList = storageContainerUtilizationDetailsDTO
                    .getStorageContainerStoredSpecimenDetailsDTOList();
            JSONArray jsonArrayForData = new JSONArray();
            for (StorageContainerStoredSpecimenDetailsDTO storageContainerStoredSpecimenDetailsDTO : storageContainerStoredSpecimenDetailsDTOList)
            {
                JSONArray jsonArrayForXYPoints = new JSONArray();
                jsonArrayForXYPoints.put(storageContainerStoredSpecimenDetailsDTO.getDateOfSpecimenCount().getTime());
                jsonArrayForXYPoints.put(storageContainerStoredSpecimenDetailsDTO.getSpecimenCount());
                jsonArrayForData.put(jsonArrayForXYPoints);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.CHART_SERIES_NAME, storageContainerUtilizationDetailsDTO.getContainerName());
            jsonObject.put(Constants.CHART_SERIES_DATA, jsonArrayForData);
            storageContainerSpecCountGraphDataArray.put(jsonObject);
        }
        return storageContainerSpecCountGraphDataArray.toString();
    }
}
