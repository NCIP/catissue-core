
package krishagni.catissueplus.handler;

import java.util.List;

import krishagni.catissueplus.Exception.CatissueException;
import krishagni.catissueplus.Exception.SpecimenErrorCodeEnum;
import krishagni.catissueplus.bizlogic.AliquotBizLogic;
import krishagni.catissueplus.bizlogic.StorageContainerBizlogic;
import krishagni.catissueplus.dao.SpecimenDAO;
import krishagni.catissueplus.dto.AliquotContainerDetailsDTO;
import krishagni.catissueplus.dto.AliquotDetailsDTO;
import krishagni.catissueplus.dto.ContainerInputDetailsDTO;
import krishagni.catissueplus.dto.SpecimenDTO;
import krishagni.catissueplus.util.CatissuePlusCommonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.sun.jersey.api.client.ClientResponse.Status;

import edu.wustl.catissuecore.util.PrintUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.HibernateDAO;

public class AliquotHandler
{

    public static final String ALIQUOT_GRID_VM_TEMPLATE = "aliquotGridTemplate.vm";
    public static final String AVAILABEL_CONTAINER_NAME = "availabelContainerName";
    public static final String ALIQUOT_GRID_XML = "aliquotGridXml";
    public static final String ALIQUOTS_DETAILS_DTO = "aliquotDetailsDTO";

    public String getAliquotDetails(SessionDataBean sessionDataBean, String lable, String aliquotJson) throws Exception
    {
        HibernateDAO hibernateDAO = null;
        JSONObject returnJsonObject = new JSONObject();

        try
        {
            hibernateDAO = (HibernateDAO) AppUtility.openDAOSession(sessionDataBean);
            final AliquotBizLogic aliquotBizLogic = new AliquotBizLogic();
            List<SpecimenDTO> specimenDTOList;
            JSONObject jsonObject = new JSONObject(aliquotJson);
            if (jsonObject.get("validated").toString().equals("false"))
            {
                specimenDTOList = aliquotBizLogic.getAvailabelSpecimenList(lable, hibernateDAO);
                JSONArray jsonArr = new JSONArray();
                if (specimenDTOList.size() > 1)
                {
                    for (int i = 0; i < specimenDTOList.size(); i++)
                    {
                        SpecimenDTO dtoObj = specimenDTOList.get(i);
                        JSONObject specJsonObject = new JSONObject();
                        specJsonObject.put("label", dtoObj.getLabel());
                        specJsonObject.put("barcode", dtoObj.getBarcode());
                        jsonArr.put(specJsonObject);
                    }
                    returnJsonObject.put("conflictingSpecimens", jsonArr);
                    return returnJsonObject.toString();
                }

            }
            if (jsonObject.get("count").toString() == null || jsonObject.get("count").toString().equals(""))
            {
                returnJsonObject.put(Constants.SUCCESS, "false");
                returnJsonObject.put("message", Constants.VALID_ALIQUOT_COUNT_ERROR);
                throw new CatissueException(SpecimenErrorCodeEnum.INVALID_ALIQUOT_COUNT.getDescription(),
                        SpecimenErrorCodeEnum.INVALID_ALIQUOT_COUNT.getCode());

            }
            else
            {

                int aliquotCount = Integer.parseInt(jsonObject.get("count").toString());
                Double quantityPerAliquot = null;

                if (jsonObject.get("quantity").toString() != null && !jsonObject.get("quantity").toString().equals(""))
                {
                    quantityPerAliquot = Double.parseDouble(jsonObject.get("quantity").toString());
                }
                AliquotDetailsDTO aliquotDetailsDTO = aliquotBizLogic.getAliquotDetailsDTO(lable, aliquotCount,
                        quantityPerAliquot, hibernateDAO, sessionDataBean);

                ContainerInputDetailsDTO containerInputDetails = new ContainerInputDetailsDTO();
                containerInputDetails.aliquotCount = aliquotCount;
                SpecimenDAO specimenDAO = new SpecimenDAO();
                containerInputDetails.cpId = specimenDAO.getCpIdFromSpecimenLabel(aliquotDetailsDTO.getParentLabel(),
                        hibernateDAO);
                containerInputDetails.isAdmin = sessionDataBean.isAdmin();
                containerInputDetails.userId = sessionDataBean.getUserId();
                containerInputDetails.specimenClass = aliquotDetailsDTO.getSpecimenClass();
                containerInputDetails.specimenType = aliquotDetailsDTO.getType();
                StorageContainerBizlogic storageContainerBizlogic = new StorageContainerBizlogic();
                List<AliquotContainerDetailsDTO> aliquotContainerDetailsDTOList = storageContainerBizlogic
                        .getStorageContainerList(containerInputDetails, null, hibernateDAO, 5);

                StringBuffer containerNameStr = new StringBuffer();
                for (int i = 0; i < aliquotContainerDetailsDTOList.size(); i++)
                {
                    containerNameStr.append(aliquotContainerDetailsDTOList.get(i).containerName).append(",");
                }
                containerNameStr.append(Constants.STORAGE_TYPE_POSITION_VIRTUAL);

                returnJsonObject.put(AVAILABEL_CONTAINER_NAME, containerNameStr.toString());
                Gson gson = CatissuePlusCommonUtil.getGson();

                returnJsonObject.put(ALIQUOTS_DETAILS_DTO, gson.toJson(aliquotDetailsDTO));
                returnJsonObject.put(Constants.CP_ID,
                        specimenDAO.getCpIdFromSpecimenId(aliquotDetailsDTO.getParentId(), hibernateDAO));
                boolean isLabelGenerationOn = false;
                if ((Variables.isSpecimenLabelGeneratorAvl || Variables.isTemplateBasedLblGeneratorAvl))
                {
                    isLabelGenerationOn = true;
                }
                returnJsonObject.put("isLabelGenerationOn", isLabelGenerationOn);
                boolean isBarGenerationOn = false;
                if ((Variables.isSpecimenBarcodeGeneratorAvl || Variables.isTemplateBasedLblGeneratorAvl))
                {
                    isBarGenerationOn = true;
                }
                returnJsonObject.put("isBarGenerationOn", isBarGenerationOn);
                returnJsonObject.put("unit",
                        AppUtility.getUnit(aliquotDetailsDTO.getSpecimenClass(), aliquotDetailsDTO.getType()));
                returnJsonObject.put(Constants.SUCCESS, "true");

            }

        }
        catch (NumberFormatException ex)
        {

            throw new CatissueException(SpecimenErrorCodeEnum.INVALID_ALIQUOT_COUNT.getDescription(),
                    SpecimenErrorCodeEnum.INVALID_ALIQUOT_COUNT.getCode());

        }
        finally
        {
            AppUtility.closeDAOSession(hibernateDAO);
        }

        return returnJsonObject.toString();
    }

    public String createAliquot(SessionDataBean sessionDataBean, String aliquotJson) throws Exception
    {

        HibernateDAO hibernateDao = null;
        JSONObject returnJsonObject = new JSONObject();
        try
        {
            Gson gson = CatissuePlusCommonUtil.getGson();
            JSONObject jsonObject = new JSONObject(aliquotJson);
            hibernateDao = (HibernateDAO) AppUtility.openDAOSession(sessionDataBean);

            AliquotDetailsDTO aliquotDetailsDTO = gson.fromJson(aliquotJson, AliquotDetailsDTO.class);
            final AliquotBizLogic aliquotBizLogic = new AliquotBizLogic();
            aliquotBizLogic.createAliquotSpecimen(aliquotDetailsDTO, hibernateDao, sessionDataBean);
            hibernateDao.commit();
            if (aliquotDetailsDTO.isPrintLabel())
            {
                for (int i = 0; i < aliquotDetailsDTO.getPerAliquotDetailsCollection().size(); i++)
                {
                    PrintUtil.printSpecimenLabel(" ", " ", sessionDataBean, aliquotDetailsDTO
                            .getPerAliquotDetailsCollection().get(i).getAliquotId());

                }
            }

            returnJsonObject.put("success", true);
            returnJsonObject.put("msg", Constants.ALIQUOTS_CREATION_SUCCESS_MSG);
            returnJsonObject.put(ALIQUOTS_DETAILS_DTO, gson.toJson(aliquotDetailsDTO));

            returnJsonObject.put("unit",
                    AppUtility.getUnit(aliquotDetailsDTO.getSpecimenClass(), aliquotDetailsDTO.getType()));
            returnJsonObject.put(Constants.SUCCESS, "true");
        }
        catch (NumberFormatException ex)
        {
            throw new CatissueException(SpecimenErrorCodeEnum.INVALID_ALIQUOT_COUNT.getDescription(),
                    SpecimenErrorCodeEnum.INVALID_ALIQUOT_COUNT.getCode());

        }
        catch (final ApplicationException exp)
        {
            String msgString = exp.getErrorKey().getMessageWithValues();
            throw new CatissueException(msgString, Status.INTERNAL_SERVER_ERROR.getStatusCode());

        }
        catch (JsonParseException ex)
        {
            throw new CatissueException(SpecimenErrorCodeEnum.PARSE_ERROR.getDescription(),
                    SpecimenErrorCodeEnum.PARSE_ERROR.getCode());

        }

        finally
        {
            AppUtility.closeDAOSession(hibernateDao);
        }
        return returnJsonObject.toString();

    }

}
