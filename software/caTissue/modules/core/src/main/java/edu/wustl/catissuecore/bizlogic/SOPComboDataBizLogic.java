
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
public class SOPComboDataBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger.getCommonLogger(SOPComboDataBizLogic.class);

	/**
	 * This method would return the Clinical Diagnosis List
	 * @return List which contains the Clinical Diagnosis Data
	 * @throws BizLogicException
	 * @throws BizLogicException
	 */
	public List<NameValueBean> getSOPNameList(String query,boolean showSubset) throws BizLogicException
	{

		final List<NameValueBean> sopList = new ArrayList<NameValueBean>();
		DAO dao = null;
		try
		{
			new DefaultBizLogic();
			dao = this.openDAOSession(null);
			String hql = "Select name from edu.wustl.catissuecore.domain.sop.SOP";
			List<String> dataList = dao.executeQuery(hql);
			this.closeDAOSession(dao);
			final Iterator<String> iterator = dataList.iterator();
			sopList.add(new NameValueBean(Constants.SELECT_OPTION, ""
					+ Constants.SELECT_OPTION_VALUE));
			if(showSubset)
			{
				sopList.add(new NameValueBean(Constants.SHOW_SUBSET+"start", Constants.SHOW_SUBSET));
			}
			while (iterator.hasNext())
			{
				final String sopValue = iterator.next();
				sopList.add(new NameValueBean(sopValue,
						sopValue));
			}
			if(showSubset)
			{
				sopList.add(new NameValueBean(Constants.SHOW_SUBSET+"end",Constants.SHOW_SUBSET));
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
		return sopList;
	}
}
