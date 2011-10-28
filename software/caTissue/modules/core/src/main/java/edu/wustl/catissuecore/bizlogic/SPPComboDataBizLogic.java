
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * @author sagar_baldwa
 */
public class SPPComboDataBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger.getCommonLogger(SPPComboDataBizLogic.class);

	/**
	 * This method would return the Clinical Diagnosis List
	 * @return List which contains the Clinical Diagnosis Data
	 * @throws BizLogicException
	 * @throws BizLogicException
	 */
	public List<NameValueBean> getSPPNameList(String query,boolean showSubset) throws BizLogicException
	{

		final List<NameValueBean> sppList = new ArrayList<NameValueBean>();
		DAO dao = null;
		try
		{
			new DefaultBizLogic();
			dao = this.openDAOSession(null);
			String hql = "Select name from edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure";
			List<String> dataList = dao.executeQuery(hql);
			this.closeDAOSession(dao);
			final Iterator<String> iterator = dataList.iterator();
			sppList.add(new NameValueBean(Constants.SELECT_OPTION, ""
					+ Constants.SELECT_OPTION_VALUE));
			if(showSubset)
			{
				sppList.add(new NameValueBean(Constants.SHOW_SUBSET+"start", Constants.SHOW_SUBSET));
			}
			while (iterator.hasNext())
			{
				final String sppValue = iterator.next();
				sppList.add(new NameValueBean(sppValue,
						sppValue));
			}
			if(showSubset)
			{
				sppList.add(new NameValueBean(Constants.SHOW_SUBSET+"end",Constants.SHOW_SUBSET));
			}
		}
		catch (final DAOException exp)
		{
			this.logger.error(exp.getMessage(),exp);
			exp.printStackTrace();
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return sppList;
	}
}
