
package krishagni.catissueplus.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krishagni.catissueplus.bizlogic.StorageContainerBizlogic;
import krishagni.catissueplus.bizlogic.StorageContainerGraphBizlogic;
import krishagni.catissueplus.dao.StorageContainerDAO;
import krishagni.catissueplus.dao.StorageContainerGraphDAO;
import krishagni.catissueplus.dto.StorageContainerStoredSpecimenDetailsDTO;
import krishagni.catissueplus.dto.StorageContainerUtilizationDetailsDTO;
import krishagni.catissueplus.dto.StorageContainerViewDTO;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.wustl.common.action.SecureAction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;

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
    protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        HibernateDAO hibernateDAO = null;
        String resultString = "";
        try
        {
            hibernateDAO = (HibernateDAO) AppUtility.openDAOSession(null);

            String siteName = request.getParameter(Constants.SITE_NAME);
            String type = request.getParameter("graphType");

            if (siteName.equalsIgnoreCase(""))
            {
                List<StorageContainerStoredSpecimenDetailsDTO> StorageContainerStoredSpecimenDetailsDTOList = new StorageContainerGraphDAO()
                        .getSiteUtilizationList(hibernateDAO);
                resultString = getJSONStringForSiteUtilization(StorageContainerStoredSpecimenDetailsDTOList);
            }
            else
            {
                Map<String, List<StorageContainerStoredSpecimenDetailsDTO>> containerStoredSpecimenDetailsMap = new StorageContainerGraphDAO()
                        .getContainerUtilizationDTOMap(hibernateDAO, siteName);
                resultString = getJSONFromStorageContainerUtilizationDetailsDTOList(containerStoredSpecimenDetailsMap,
                        type);
            }

        }
        finally
        {
            AppUtility.closeDAOSession(hibernateDAO);
        }
        response.setContentType("application/json");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", resultString);
        jsonObject.put("redLineValue", XMLPropertyHandler.getValue(Constants.RED_LINE_VALUE));
        response.getWriter().write(jsonObject.toString());

        return null;
    }

    /**
    * Returns JSON string for storageContainerUtilizationDetailsDTOList
    * @param storageContainerUtilizationDetailsDTOList
    * @return
    * @throws JSONException 
    */
    private String getJSONFromStorageContainerUtilizationDetailsDTOList(
            Map<String, List<StorageContainerStoredSpecimenDetailsDTO>> containerStoredSpecimenDetailsMap, String type)
            throws JSONException
    {
        JSONArray storageContainerSpecCountGraphDataArray = new JSONArray();
        Iterator<Entry<String, List<StorageContainerStoredSpecimenDetailsDTO>>> itr = containerStoredSpecimenDetailsMap
                .entrySet().iterator();

        while (itr.hasNext())
        {
            Entry<String, List<StorageContainerStoredSpecimenDetailsDTO>> utilizationEntry = itr.next();
            String containerName = utilizationEntry.getKey();
            List<StorageContainerStoredSpecimenDetailsDTO> storageContainerStoredSpecimenDetailsDTOList = utilizationEntry
                    .getValue();
            JSONArray jsonArrayForData = new JSONArray();
            for (StorageContainerStoredSpecimenDetailsDTO storageContainerStoredSpecimenDetailsDTO : storageContainerStoredSpecimenDetailsDTOList)
            {
                JSONArray jsonArrayForXYPoints = new JSONArray();
                jsonArrayForXYPoints.put(storageContainerStoredSpecimenDetailsDTO.getDateOfSpecimenCount().getTime());
                jsonArrayForXYPoints.put(type.equals("specimenCount") ? storageContainerStoredSpecimenDetailsDTO
                        .getSpecimenCount() : storageContainerStoredSpecimenDetailsDTO.getPercentUtilization());
                jsonArrayForData.put(jsonArrayForXYPoints);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.CHART_SERIES_NAME, containerName);
            jsonObject.put(Constants.CHART_SERIES_DATA, jsonArrayForData);
            storageContainerSpecCountGraphDataArray.put(jsonObject);
        }
        return storageContainerSpecCountGraphDataArray.toString();
    }

    private String getJSONStringForSiteUtilization(
            List<StorageContainerStoredSpecimenDetailsDTO> storageContainerUtilizationDetailsDTOList)
            throws JSONException
    {
        JSONArray jsonArrayForSiteNameList = new JSONArray();
        JSONArray jsonArrayForOcupiedPosition = new JSONArray();
        JSONArray jsonArrayForAvailablePosition = new JSONArray();
        for (StorageContainerStoredSpecimenDetailsDTO storageContainerUtilizationDetailsDTO : storageContainerUtilizationDetailsDTOList)
        {
            jsonArrayForAvailablePosition.put(storageContainerUtilizationDetailsDTO.getCapacity()
                    - storageContainerUtilizationDetailsDTO.getSpecimenCount());
            jsonArrayForOcupiedPosition.put(storageContainerUtilizationDetailsDTO.getSpecimenCount());
            jsonArrayForSiteNameList.put(storageContainerUtilizationDetailsDTO.getSiteName());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("siteName", jsonArrayForSiteNameList);
        jsonObject.put("ocupiedPosition", jsonArrayForOcupiedPosition);
        jsonObject.put("availablePosition", jsonArrayForAvailablePosition);

        return jsonObject.toString();

    }

}
