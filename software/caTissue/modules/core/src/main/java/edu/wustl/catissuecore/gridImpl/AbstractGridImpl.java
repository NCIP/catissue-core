
package edu.wustl.catissuecore.gridImpl;

import com.dhtmlx.connector.ConnectorBehavior;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;

public abstract class AbstractGridImpl extends ConnectorBehavior
{

	private SessionDataBean sessionData;
	/**
	 * Gets the grid query.
	 *
	 * @param jsonString the json string
	 * @param sessionDataBean
	 * @return the grid query
	 * @throws ApplicationException the application exception
	 */
	public abstract String getGridQuery(String jsonString, SessionDataBean sessionDataBean) throws BizLogicException;

	/**
	 * Gets the display column string.
	 *
	 * @return the display column string
	 * @throws ApplicationException the application exception
	 */
	public abstract String getDisplayColumnString() throws BizLogicException;

	/**
	 * Gets the table column string.
	 *
	 * @return the table column string
	 * @throws ApplicationException the application exception
	 */
	public abstract String getTableColumnString() throws BizLogicException;

	public void setSessionData(SessionDataBean sessionData)
	{
		this.sessionData = sessionData;
	}

	/**
	 * @return the sessionData
	 */
	public SessionDataBean getSessionData()
	{
		return sessionData;
	}
}
