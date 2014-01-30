
package krishagni.catissueplus.handler;

import krishagni.catissueplus.bizlogic.DeriveBizLogic;
import krishagni.catissueplus.bizlogic.SpecimenBizLogic;
import krishagni.catissueplus.dao.SpecimenDAO;
import krishagni.catissueplus.dto.DerivedDTO;
import krishagni.catissueplus.dto.SpecimenDTO;
import krishagni.catissueplus.util.CommonUtil;
import krishagni.catissueplus.util.DAOUtil;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.PrintUtil;
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
			System.out.println("###### Inside Specimen class just before calling print API");
			System.out.println("########## Print Label Flag: "+printFlag);
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

}
