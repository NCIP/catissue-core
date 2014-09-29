
package krishagni.catissueplus.handler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import krishagni.catissueplus.bizlogic.DeriveBizLogic;
import krishagni.catissueplus.bizlogic.SpecimenBizLogic;
import krishagni.catissueplus.dao.SpecimenDAO;
import krishagni.catissueplus.dto.DerivedDTO;
import krishagni.catissueplus.dto.SpecimenDTO;
import krishagni.catissueplus.util.CommonUtil;
import krishagni.catissueplus.util.DAOUtil;
import krishagni.catissueplus.util.SpecimenUtil;

import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.User;
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
			JSONObject jsonObject = new JSONObject(label);
			String caption = String.valueOf(jsonObject.keys().next());
			String[] arr = label.split("=");
			if("label".equals(caption))
			{
				specimenDTO = specimenBizLogic.getSpecimenDTOFromSpecimen(specimenDAO.getSpecimenByLabelOrBarcode(jsonObject.getString(caption), null, hibernateDao));
			}
			else if("barcode".equals(caption))
			{
				specimenDTO = specimenBizLogic.getSpecimenDTOFromSpecimen(specimenDAO.getSpecimenByLabelOrBarcode(null,
						jsonObject.getString(caption), hibernateDao));
			}
		}
		catch (ApplicationException exception)
		{
			String errMssg = CommonUtil.getErrorMessage(exception,new Specimen(),"Inserting");
			LOGGER.error(errMssg, exception);
			throw new BizLogicException(exception.getErrorKey(),
					exception,exception.getMsgValues(),errMssg);
			
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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



	public edu.wustl.catissuecore.dto.SpecimenDTO updateSpecimenStatus(edu.wustl.catissuecore.dto.SpecimenDTO specimenDTO, SessionDataBean sessionDataBean) throws BizLogicException {
		NewSpecimenBizLogic sbizlogic = new NewSpecimenBizLogic();
		HibernateDAO hibernateDao = null;
		try
    {
			  String reason = specimenDTO.getComments();
			
        hibernateDao = DAOUtil.openDAOSession(sessionDataBean);
        Specimen  specimen = sbizlogic.getSpecimenObj(specimenDTO.getId(), hibernateDao);
        specimen.setActivityStatus(specimenDTO.getActivityStatus());
        SpecimenBizLogic specimenBizLogic = new SpecimenBizLogic();
        DisposalEventParameters disposalEvent = SpecimenUtil.createDisposeEvent(sessionDataBean, specimen,
        		reason);
        if(specimenDTO.getUserId() != null){
        	disposalEvent.getUser().setId(specimenDTO.getUserId());;
        }
        if(specimenDTO.getDisposalDate() != null){
        	Calendar cal = Calendar.getInstance();
        	cal.setTime(specimenDTO.getDisposalDate());
        	cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(specimenDTO.getDisposalHours()));
  				cal.set(Calendar.MINUTE, Integer.parseInt(specimenDTO.getDisposalMins()));
        }
        
        specimenBizLogic.disposeSpecimen(hibernateDao, sessionDataBean, specimen, disposalEvent);
        specimenDTO.setPos1("");
        specimenDTO.setPos2("");
        specimenDTO.setContainerId(null);
        specimenDTO.setContainerName("");
        specimenDTO.setAvailable(false);
        hibernateDao.commit();
    }catch (ApplicationException exception)
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
	
}
