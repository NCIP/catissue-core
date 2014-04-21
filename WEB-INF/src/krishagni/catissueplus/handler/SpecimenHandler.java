
package krishagni.catissueplus.handler;

import java.util.List;

import krishagni.catissueplus.bizlogic.DeriveBizLogic;
import krishagni.catissueplus.bizlogic.SpecimenBizLogic;
import krishagni.catissueplus.dao.SpecimenDAO;
import krishagni.catissueplus.dto.DerivedDTO;
import krishagni.catissueplus.dto.SpecimenDTO;
import krishagni.catissueplus.util.CommonUtil;
import krishagni.catissueplus.util.DAOUtil;

import org.json.JSONObject;

import edu.common.dynamicextensions.xmi.AnnotationUtil;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.PrintUtil;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;

public class SpecimenHandler
{

	private static final Logger LOGGER = Logger.getCommonLogger(SpecimenHandler.class);
	public SpecimenDTO createSpecimen(SpecimenDTO specimenDTO,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		HibernateDAO hibernateDao = null;
		try
		{
			boolean printFlag = specimenDTO.isToPrintLabel();
			hibernateDao = DAOUtil
					.openDAOSession(sessionDataBean);
			SpecimenBizLogic specimenBizLogic = new SpecimenBizLogic();
			specimenDTO = specimenBizLogic.insert(specimenDTO, hibernateDao,
					sessionDataBean);
			hibernateDao.commit();
			if (printFlag)
			{
				specimenDTO.setToPrintLabel(PrintUtil.printSpecimenLabel(null,
						null, sessionDataBean, specimenDTO.getId()));
			}
		}
		catch (ApplicationException exception)
		{
			String errMssg = CommonUtil.getErrorMessage(exception,new Specimen(),"Inserting");
			LOGGER.error(errMssg, exception);
			throw new BizLogicException(exception.getErrorKey(),
					exception,exception.getMsgValues(),errMssg);
			
		}
		finally
		{
   			DAOUtil.closeDAOSession(hibernateDao);
		}

		return specimenDTO;
	}
	
	
    
	public SpecimenDTO updateSpecimen(SpecimenDTO specimenDTO,
			SessionDataBean sessionDataBean) throws BizLogicException 
	{
		HibernateDAO hibernateDao = null;
		try
		{
			boolean printFlag = specimenDTO.isToPrintLabel();
			hibernateDao = DAOUtil
					.openDAOSession(sessionDataBean);
			SpecimenBizLogic specimenBizLogic = new SpecimenBizLogic();
			
			specimenDTO = specimenBizLogic.updateSpecimen(hibernateDao,
					specimenDTO, sessionDataBean);
			hibernateDao.commit();
			if (printFlag)
			{
				specimenDTO.setToPrintLabel(PrintUtil.printSpecimenLabel(null,
						null, sessionDataBean, specimenDTO.getId()));
			}
		}
		catch (ApplicationException exception)
		{
			String errMssg = CommonUtil.getErrorMessage(exception,new Specimen(),"Updating");
			LOGGER.error(errMssg, exception);
			throw new BizLogicException(exception.getErrorKey(),
					exception,exception.getMsgValues(),errMssg);
			
		}
		finally
		{
			DAOUtil.closeDAOSession(hibernateDao);
		}

		return specimenDTO;
	}

	public SpecimenDTO createDerivative(DerivedDTO derivedDTO,
			SessionDataBean sessionDataBean) throws BizLogicException 
	{
		HibernateDAO hibernateDao = null;
		SpecimenDTO specimenDTO = new SpecimenDTO();
		try
		{
			hibernateDao = DAOUtil
					.openDAOSession(sessionDataBean);
			DeriveBizLogic deriveBizlogic = new DeriveBizLogic();
			specimenDTO = deriveBizlogic.insertDeriveSpecimen(hibernateDao,
					derivedDTO, sessionDataBean);
			hibernateDao.commit();
			if (derivedDTO.getIsToPrintLabel())
			{
				specimenDTO.setToPrintLabel(PrintUtil.printSpecimenLabel(null,
						null, sessionDataBean, specimenDTO.getId()));
			}
		}
		catch (ApplicationException exception)
		{
			String errMssg = CommonUtil.getErrorMessage(exception,new Specimen(),"Inserting");
			LOGGER.error(errMssg, exception);
			throw new BizLogicException(exception.getErrorKey(),
					exception,exception.getMsgValues(),errMssg);
			
		}
		finally
		{
			DAOUtil.closeDAOSession(hibernateDao);
		}
		return specimenDTO;
	}

	public SpecimenDTO getSpecimenDetails(String label,
			SessionDataBean sessionDataBean) throws BizLogicException 
	{
		HibernateDAO hibernateDao = null;
		SpecimenDTO specimenDTO = null;
		try
		{
			hibernateDao = DAOUtil
					.openDAOSession(sessionDataBean);
			SpecimenBizLogic specimenBizLogic = new SpecimenBizLogic();
			SpecimenDAO specimenDAO = new SpecimenDAO();
			String[] arr = label.split("=");
			if("label".equals(arr[0]))
			{
				specimenDTO = specimenBizLogic.getSpecimenDTOFromSpecimen(specimenDAO.getSpecimenByLabelOrBarcode(arr[1], null, hibernateDao));
			}
			else if("barcode".equals(arr[0]))
			{
				specimenDTO = specimenBizLogic.getSpecimenDTOFromSpecimen(specimenDAO.getSpecimenByLabelOrBarcode(null,
						arr[1], hibernateDao));
			}
		}
		catch (ApplicationException exception)
		{
			String errMssg = CommonUtil.getErrorMessage(exception,new Specimen(),"Inserting");
			LOGGER.error(errMssg, exception);
			throw new BizLogicException(exception.getErrorKey(),
					exception,exception.getMsgValues(),errMssg);
			
		}
		finally
		{
			DAOUtil.closeDAOSession(hibernateDao);
		}

		return specimenDTO;
	}
	
	public String getSpecimenTabDetails(SessionDataBean sessionDataBean, String specimenId) throws Exception
    {

        HibernateDAO hibernateDao = null;
        JSONObject returnJsonObject = new JSONObject();
        try
        {
            hibernateDao = DAOUtil.openDAOSession(sessionDataBean);
            SpecimenBizLogic specimenBizLogic = new SpecimenBizLogic();
            Long specId= Long.parseLong(specimenId);
            Long reportId = specimenBizLogic.getAssociatedIdentifiedReportId(specId, hibernateDao);
            List<Object> list = specimenBizLogic.getcpIdandPartId(specId,hibernateDao);
            Object[] objArr = (Object[]) list.get(0);
            Long cpId = Long.valueOf(objArr[0].toString());
            boolean hasConsents =specimenBizLogic.hasConsents(cpId, hibernateDao);
            Long specimenEntityId = null;
//            if (CatissueCoreCacheManager.getInstance().getObjectFromCache(
//                    AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID) != null)
//            {
//                specimenEntityId = (Long) CatissueCoreCacheManager.getInstance()
//                        .getObjectFromCache(AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID);
//            }
//            else
//            {
//                specimenEntityId = AnnotationUtil
//                        .getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY);
//                CatissueCoreCacheManager.getInstance().addObjectToCache(
//                        AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID, specimenEntityId);
//            }
            
            
            returnJsonObject.put("success", true);
            returnJsonObject.put("identifiedReportId", reportId);
//            returnJsonObject.put(AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID,specimenEntityId);
//            returnJsonObject.put("entityName", AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY);
            returnJsonObject.put("hasConsents", hasConsents);
            returnJsonObject.put("isImageEnabled", Variables.isImagingConfigurred);
           
        }
        catch (ApplicationException exception)
        {
            String errMssg = CommonUtil.getErrorMessage(exception,new Specimen(),"Inserting");
            LOGGER.error(errMssg, exception);
            throw new BizLogicException(exception.getErrorKey(),
                    exception,exception.getMsgValues(),errMssg);
            
        }
      

        finally
        {
            DAOUtil.closeDAOSession(hibernateDao);
        }
        return returnJsonObject.toString();

    }


}
