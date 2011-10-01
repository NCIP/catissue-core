/**
 *
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhtmlx.connector.ConnectorServlet;
import com.dhtmlx.connector.DBType;
import com.dhtmlx.connector.GridConnector;

import edu.wustl.catissuecore.gridImpl.AbstractGridImpl;
import edu.wustl.catissuecore.gridImpl.GridScgImpl;
import edu.wustl.catissuecore.gridImpl.GridSpecimenImpl;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;

/**
 * @author mosin_kazi
 *
 */
public class LoadGridServlet extends ConnectorServlet
{

	private static final long serialVersionUID = 1L;
	private String jsonString;
	private String gridType;
	private SessionDataBean sessionData;

	public synchronized String getGridType()
	{
		return gridType;
	}

	public synchronized void setGridType(String gridType)
	{
		this.gridType = gridType;
	}

	public synchronized String getJsonString()
	{
		return jsonString;
	}

	public synchronized void setJsonString(String jsonString)
	{
		this.jsonString = jsonString;
	}

	public synchronized void setSessionData(SessionDataBean sessionData)
	{
		this.sessionData = sessionData;
	}

	/**
	 * @return the sessionData
	 */
	public synchronized SessionDataBean getSessionData()
	{
		return sessionData;
	}

	protected void configure()
	{

		Connection conn = null;


		IDAOFactory daoFactory = null;
		try
		{
			AbstractGridImpl gridImplObj  =(AbstractGridImpl) Class.forName(GridScgImpl.class.getName()).newInstance();
			if(getGridType().equals("scg")){
			 gridImplObj  =(AbstractGridImpl) Class.forName(GridScgImpl.class.getName()).newInstance();
			}else{
				gridImplObj  =(AbstractGridImpl) Class.forName(GridSpecimenImpl.class.getName()).newInstance();
			}
			String appName = CommonServiceLocator.getInstance().getAppName();
            daoFactory = DAOConfigFactory.getInstance().getDAOFactory(appName);
            conn = daoFactory.getConnection();
			GridConnector connector = null;
			if (Constants.ORACLE_DATABASE.equals(DAOConfigFactory.getInstance().getDAOFactory(
					Constants.APPLICATION_NAME).getDataBaseType())){
				connector = new GridConnector(conn, DBType.Oracle);
			}else if(Constants.MYSQL_DATABASE.equals(DAOConfigFactory.getInstance()
					.getDAOFactory(Constants.APPLICATION_NAME).getDataBaseType())){
				connector = new GridConnector(conn, DBType.MySQL);
			}
			connector.event.attach(gridImplObj);
			String query = gridImplObj.getGridQuery(getJsonString(),getSessionData());
			String tableColString =gridImplObj.getTableColumnString();
			connector.dynamic_loading(true);
			connector.dynamic_loading(10);
			connector.render_sql(query, "table_a_id", tableColString);
		}
		catch (Exception ex)
		{
			Logger.getLogger(LoadGridServlet.class).error(ex.getMessage(), ex);

		}
		finally
		{
			try
			{
				if(conn != null)
				{
					daoFactory.closeConnection(conn);
				}
			}
			catch (DAOException e)
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
		setJsonString(req.getParameter("paramJson"));
		setGridType(req.getParameter("gridType"));
		setSessionData((SessionDataBean)req.getSession().getAttribute(Constants.SESSION_DATA));
		super.doGet(req, res);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,
			IOException
	{
		setJsonString(req.getParameter("paramJson"));
		setGridType(req.getParameter("gridType"));
		setSessionData((SessionDataBean)req.getSession().getAttribute(Constants.SESSION_DATA));
		super.doGet(req, res);

	}

    /**
     *
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
/*	protected AbstractGridImpl getImplObj() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
	    InputStream inputStream = LoadGridServlet.class.getClassLoader().getResourceAsStream(
	            Constants.GRID_SETUP_PROP_FILE);
	    Properties props = new Properties();
        props.load(inputStream);
        String className = props.getProperty(gridType);
        AbstractGridImpl abstractGridobj = (AbstractGridImpl) Class.forName(className)
                .newInstance();
        return abstractGridobj;

	}*/


}
