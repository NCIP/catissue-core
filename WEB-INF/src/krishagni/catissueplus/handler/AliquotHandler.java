
package krishagni.catissueplus.handler;

import java.util.ArrayList;
import java.util.List;

import krishagni.catissueplus.Exception.CatissueException;
import krishagni.catissueplus.Exception.SpecimenErrorCodeEnum;
import krishagni.catissueplus.bizlogic.AliquotBizLogic;
import krishagni.catissueplus.dao.SpecimenDAO;
import krishagni.catissueplus.dto.AliquotContainerDetailsDTO;
import krishagni.catissueplus.dto.AliquotDetailsDTO;
import krishagni.catissueplus.dto.SpecimenDTO;
import krishagni.catissueplus.util.CatissuePlusCommonUtil;
import krishagni.catissueplus.util.CommonUtil;
import krishagni.catissueplus.util.DAOUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import edu.wustl.catissuecore.bizlogic.SpecimenBizlogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.PrintUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;

public class AliquotHandler
{

    private static final String AVAILABEL_CONTAINER_NAME = "availabelContainerName";
    private static final String ALIQUOTS_DETAILS_DTO = "aliquotDetailsDTO";
    private static final Logger LOGGER = Logger.getCommonLogger(AliquotHandler.class);

    public String getAliquotDetails(SessionDataBean sessionDataBean, String label, String aliquotJson) throws Exception
    {
        HibernateDAO hibernateDAO = null;
        JSONObject returnJsonObject = new JSONObject();

        try
        {
            hibernateDAO = DAOUtil.openDAOSession(sessionDataBean);
            final AliquotBizLogic aliquotBizLogic = new AliquotBizLogic();
            List<SpecimenDTO> specimenDTOList;
            JSONObject jsonObject = new JSONObject(aliquotJson);
            if (jsonObject.get("validated").toString().equals("false"))
            {
                specimenDTOList = aliquotBizLogic.getAvailabelSpecimenList(label, hibernateDAO);
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
                LOGGER.error(SpecimenErrorCodeEnum.INVALID_ALIQUOT_COUNT.getDescription());
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
//                ContainerInputDetailsDTO containerInputDetails = new ContainerInputDetailsDTO();
//                containerInputDetails.aliquotCount = aliquotCount;
//                SpecimenDAO specimenDAO = new SpecimenDAO();
//                containerInputDetails.cpId = specimenDAO.getCpIdFromSpecimenLabel(lable,
//                        hibernateDAO);
//                containerInputDetails.isAdmin = sessionDataBean.isAdmin();
//                containerInputDetails.userId = sessionDataBean.getUserId();
//                containerInputDetails.specimenClass = aliquotDetailsDTO.getSpecimenClass();
//                containerInputDetails.specimenType = aliquotDetailsDTO.getType();
//                StorageContainerBizlogic storageContainerBizlogic = new StorageContainerBizlogic();
                List<AliquotContainerDetailsDTO> aliquotContainerDetailsDTOList = new ArrayList<AliquotContainerDetailsDTO>(); 
//                		storageContainerBizlogic
//                        .getStorageContainerList(containerInputDetails, null, hibernateDAO, 5);
                AliquotDetailsDTO aliquotDetailsDTO = aliquotBizLogic.getAliquotDetailsDTO(label, aliquotCount,
                        quantityPerAliquot, hibernateDAO, sessionDataBean,aliquotContainerDetailsDTOList);

                
                
//                List<AliquotContainerDetailsDTO> aliquotContainerDetailsDTOList = storageContainerBizlogic
//                        .getStorageContainerList(containerInputDetails, null, hibernateDAO, 5);

                StringBuffer containerNameStr = new StringBuffer();
                for (int i = 0; i < aliquotContainerDetailsDTOList.size(); i++)
                {
                    containerNameStr.append(aliquotContainerDetailsDTOList.get(i).containerName).append(",");
                }
                containerNameStr.append(Constants.STORAGE_TYPE_POSITION_VIRTUAL);

                returnJsonObject.put(AVAILABEL_CONTAINER_NAME, containerNameStr.toString());
                Gson gson = CatissuePlusCommonUtil.getGson();
                SpecimenDAO specimenDAO = new SpecimenDAO();
                returnJsonObject.put(ALIQUOTS_DETAILS_DTO, gson.toJson(aliquotDetailsDTO));
                returnJsonObject.put(Constants.CP_ID,
                        specimenDAO.getCpId(aliquotDetailsDTO.getParentId(), hibernateDAO));
                boolean isLabelGenerationOn = false;
                if (new SpecimenBizlogic().isSpecimenLabelGeneratorAvl(aliquotDetailsDTO.getParentId(), hibernateDAO))
                {
                    isLabelGenerationOn = true;
                }
                returnJsonObject.put("isLabelGenerationOn", isLabelGenerationOn);
                returnJsonObject.put("parentLabel", aliquotDetailsDTO.getParentLabel());
                boolean isBarGenerationOn = false;
                if ((Variables.isSpecimenBarcodeGeneratorAvl))
                {
                    isBarGenerationOn = true;
                }
                returnJsonObject.put("specimenChildCount",new SpecimenBizlogic().getCollectedChildSpecimenCountByLabel(aliquotDetailsDTO.getParentId(),hibernateDAO));
                returnJsonObject.put("isBarGenerationOn", isBarGenerationOn);
                returnJsonObject.put("unit",
                        AppUtility.getUnit(aliquotDetailsDTO.getSpecimenClass(), aliquotDetailsDTO.getType()));
                returnJsonObject.put(Constants.SUCCESS, "true");

            }

        }
        catch (NumberFormatException ex)
        {
        	LOGGER.error(ex);
            throw new CatissueException(SpecimenErrorCodeEnum.INVALID_ALIQUOT_COUNT.getDescription(),
                    SpecimenErrorCodeEnum.INVALID_ALIQUOT_COUNT.getCode());

        }
        finally
        {
        	DAOUtil.closeDAOSession(hibernateDAO);
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
            hibernateDao = DAOUtil.openDAOSession(sessionDataBean);

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
        	LOGGER.error(ex);
            throw new CatissueException(SpecimenErrorCodeEnum.INVALID_ALIQUOT_COUNT.getDescription(),
                    SpecimenErrorCodeEnum.INVALID_ALIQUOT_COUNT.getCode());

        }
        catch (ApplicationException exception)
		{
			String errMssg = CommonUtil.getErrorMessage(exception,new Specimen(),"Inserting");
			LOGGER.error(errMssg, exception);
			throw new BizLogicException(exception.getErrorKey(),
					exception,exception.getMsgValues(),errMssg);
			
		}
        catch (JsonParseException ex)
        {
        	LOGGER.error(ex);
            throw new CatissueException(SpecimenErrorCodeEnum.PARSE_ERROR.getDescription(),
                    SpecimenErrorCodeEnum.PARSE_ERROR.getCode());

        }

        finally
        {
            DAOUtil.closeDAOSession(hibernateDao);
        }
        return returnJsonObject.toString();

    }
    

}
