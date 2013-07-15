
package krishagni.catissueplus.handler;

import krishagni.catissueplus.bizlogic.DeriveBizLogic;
import krishagni.catissueplus.bizlogic.SpecimenBizLogic;
import krishagni.catissueplus.dto.DerivedDTO;
import krishagni.catissueplus.dto.SpecimenDTO;
import edu.wustl.catissuecore.util.PrintUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.HibernateDAO;

public class SpecimenHandler
{

	public SpecimenDTO createSpecimen(SpecimenDTO specimenDTO,
			SessionDataBean sessionDataBean) throws ApplicationException
	{
		HibernateDAO hibernateDao = null;
		try
		{
			hibernateDao = (HibernateDAO) AppUtility
					.openDAOSession(sessionDataBean);
			SpecimenBizLogic specimenBizLogic = new SpecimenBizLogic();
			specimenDTO = specimenBizLogic.insert(specimenDTO, hibernateDao,
					sessionDataBean);
			hibernateDao.commit();
			if (specimenDTO.isToPrintLabel())
			{
				specimenDTO.setToPrintLabel(PrintUtil.printSpecimenLabel(null,
						null, sessionDataBean, specimenDTO.getId()));
			}
		}
		finally
		{
   			AppUtility.closeDAOSession(hibernateDao);
		}

		return specimenDTO;
	}

	public SpecimenDTO updateSpecimen(SpecimenDTO specimenDTO,
			SessionDataBean sessionDataBean) throws ApplicationException
	{
		HibernateDAO hibernateDao = null;
		try
		{
			hibernateDao = (HibernateDAO) AppUtility
					.openDAOSession(sessionDataBean);
			SpecimenBizLogic specimenBizLogic = new SpecimenBizLogic();
			specimenDTO = specimenBizLogic.updateSpecimen(hibernateDao,
					specimenDTO, sessionDataBean);
			hibernateDao.commit();
			if (specimenDTO.isToPrintLabel())
			{
				specimenDTO.setToPrintLabel(PrintUtil.printSpecimenLabel(null,
						null, sessionDataBean, specimenDTO.getId()));
			}
		}
		finally
		{
			AppUtility.closeDAOSession(hibernateDao);
		}

		return specimenDTO;
	}

	public SpecimenDTO createDerivative(DerivedDTO derivedDTO,
			SessionDataBean sessionDataBean) throws ApplicationException
	{
		HibernateDAO hibernateDao = null;
		SpecimenDTO specimenDTO = new SpecimenDTO();
		try
		{
			hibernateDao = (HibernateDAO) AppUtility
					.openDAOSession(sessionDataBean);
			DeriveBizLogic deriveBizlogic = new DeriveBizLogic();
			specimenDTO = deriveBizlogic.insertDeriveSpecimen(hibernateDao,
					derivedDTO, sessionDataBean);
			hibernateDao.commit();
			if (derivedDTO.getIsToPrintLabel())
			{
				specimenDTO.setToPrintLabel(PrintUtil.printSpecimenLabel(null,
						null, sessionDataBean, specimenDTO.getId()));
				//				boolean printStatus = PrintUtil.printSpecimenLabel(" ", " ", request,
				//						specimenDTO.getId());
				//				if (printStatus)
				//				{
				//					returnMap.put("printLabelSuccess",
				//							ApplicationProperties.getValue("specimen.label.print.success"));
				//					returnMap.put("printLabel", "success.");
				//				}
				//				else
				//				{
				//					returnMap.put("printLabelError",
				//							ApplicationProperties.getValue("specimen.label.print.fail"));
				//				}
			}
		}
		finally
		{
			AppUtility.closeDAOSession(hibernateDao);
		}
		return specimenDTO;
	}

	public SpecimenDTO getSpecimenDetails(String label,
			SessionDataBean sessionDataBean) throws ApplicationException
	{
		HibernateDAO hibernateDao = null;
		SpecimenDTO specimenDTO = null;
		try
		{
			hibernateDao = (HibernateDAO) AppUtility
					.openDAOSession(sessionDataBean);
			SpecimenBizLogic specimenBizLogic = new SpecimenBizLogic();
			String[] arr = label.split("=");
			if("label".equals(arr[0]))
			{
				specimenDTO = specimenBizLogic.getSpecimenDTO(specimenBizLogic.getParentByLabel(
					arr[1], hibernateDao));
			}
			else if("barcode".equals(arr[0]))
			{
				specimenDTO = specimenBizLogic.getSpecimenDTO(specimenBizLogic.getParentByBarcode(
						arr[1], hibernateDao));
			}
		}
		finally
		{
			AppUtility.closeDAOSession(hibernateDao);
		}

		return specimenDTO;
	}

}
