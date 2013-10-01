package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhtmlx.connector.ConnectorServlet;
import com.dhtmlx.connector.DBType;
import com.dhtmlx.connector.GridConnector;

import edu.wustl.catissuecore.gridImpl.AbstractGridImpl;
import edu.wustl.catissuecore.gridImpl.GridSpecimenImpl;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.connectionmanager.IConnectionManager;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;

public class LoadGridServlet extends ConnectorServlet
{
	private String pageOf;
	private String aliasName;
	private SessionDataBean sessionDataBean;

	public String getPageOf() {
		return pageOf;
	}

	public void setPageOf(String pageOf) {
		this.pageOf = pageOf;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public SessionDataBean getSessionDataBean() {
		return sessionDataBean;
	}

	public void setSessionDataBean(SessionDataBean sessionDataBean) {
		this.sessionDataBean = sessionDataBean;
	}

	@Override
	protected void configure() 
	{
		AbstractGridImpl gridImplObj  = null;
		
		
		IConnectionManager connectionManager = null;
		String appName = CommonServiceLocator.getInstance().getAppName();
        IDAOFactory daoFactory = DAOConfigFactory.getInstance().getDAOFactory(appName);
        try {
        	gridImplObj  =(AbstractGridImpl) Class.forName(GridSpecimenImpl.class.getName()).newInstance();
//        	conn = daoFactory.getConnection();
        	 connectionManager = daoFactory.getJDBCDAO().getConnectionManager();
        	Connection conn = connectionManager.getConnection();
        	GridConnector connector = null;
			if (Constants.ORACLE_DATABASE.equals(DAOConfigFactory.getInstance().getDAOFactory(
					Constants.APPLICATION_NAME).getDataBaseType())){
				connector = new GridConnector(conn, DBType.Oracle);
			}else if(Constants.MYSQL_DATABASE.equals(DAOConfigFactory.getInstance()
					.getDAOFactory(Constants.APPLICATION_NAME).getDataBaseType())){
				connector = new GridConnector(conn, DBType.MySQL);
			}
			connector.event.attach(gridImplObj);
			String sql = gridImplObj.getGridQuery(aliasName,getSessionDataBean());
			String tableColString =gridImplObj.getTableColumnString();
			connector.dynamic_loading(true);
			connector.dynamic_loading(Integer.MAX_VALUE); 
			connector.render_sql(sql, gridImplObj.getTableColumnString(), tableColString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(connectionManager != null)
				{
					connectionManager.closeSession();
				}
			}
			catch (Exception e)
			{
				Logger.getLogger(LoadGridServlet.class).error(e.getMessage(), e);
			}
		}
	}	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,
			IOException
	{
		setAliasName(req.getParameter("reqParam"));
		setPageOf(req.getParameter("pageOf"));
		setSessionDataBean((SessionDataBean)req.getSession().getAttribute(Constants.SESSION_DATA));
		super.doGet(req, res);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,
			IOException
	{
		super.doGet(req, res);

	}
}
