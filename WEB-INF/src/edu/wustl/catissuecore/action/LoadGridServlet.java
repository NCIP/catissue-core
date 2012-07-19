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

import edu.wustl.catissuecore.namegenerator.PropertyHandler;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
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
		Connection conn = null;
		System.out.println();
		String appName = CommonServiceLocator.getInstance().getAppName();
        IDAOFactory daoFactory = DAOConfigFactory.getInstance().getDAOFactory(appName);
        try {
        	conn = daoFactory.getConnection();
        	GridConnector connector = null;
			if (Constants.ORACLE_DATABASE.equals(DAOConfigFactory.getInstance().getDAOFactory(
					Constants.APPLICATION_NAME).getDataBaseType())){
				connector = new GridConnector(conn, DBType.Oracle);
			}else if(Constants.MYSQL_DATABASE.equals(DAOConfigFactory.getInstance()
					.getDAOFactory(Constants.APPLICATION_NAME).getDataBaseType())){
				connector = new GridConnector(conn, DBType.MySQL);
			}
			String sql = "Select 0 as CHKBOX, Specimen1.BARCODE  Barcode , Specimen1.LABEL  Label , " +
"ExternalIdentifier1.NAME  ExternalIdentifier_Name , ExternalIdentifier1.VALUE  ExternalIdentifier_Value , " +
"Specimen1.SPECIMEN_COLLECTION_GROUP_ID  SCG_ID , AbstractSpecimen1.SPECIMEN_CLASS  Class , " +
"AbstractSpecimen1.SPECIMEN_TYPE  Type , AbstractSpecimen1.LINEAGE  Lineage , " +
"AbstractSpecimen1.PARENT_SPECIMEN_ID  Parent_Specimen_Id , SpecimenCharacteristics1.TISSUE_SIDE  Tissue_Side ," +
" SpecimenCharacteristics1.TISSUE_SITE  Tissue_Site , AbstractSpecimen1.PATHOLOGICAL_STATUS  Path_Status , " +
"AbstractSpecimen1.INITIAL_QUANTITY  Init_Quantity , Specimen1.AVAILABLE_QUANTITY  Avl_Quantity , " +
"Specimen1.AVAILABLE  Available , Specimen1.CREATED_ON_DATE  Created_On , Specimen1.IDENTIFIER  Identifier "+ 
" FROM  CATISSUE_SPECIMEN_CHAR SpecimenCharacteristics1 , CATISSUE_ABSTRACT_SPECIMEN AbstractSpecimen1 , " +
"CATISSUE_EXTERNAL_IDENTIFIER ExternalIdentifier1 , CATISSUE_SPECIMEN Specimen1 , catissue_spec_tag_items atg "+  
" WHERE SpecimenCharacteristics1.IDENTIFIER   =  AbstractSpecimen1.SPECIMEN_CHARACTERISTICS_ID  AND " +
"Specimen1.IDENTIFIER   =  ExternalIdentifier1.SPECIMEN_ID  AND AbstractSpecimen1.IDENTIFIER   =  " +
"Specimen1.IDENTIFIER  AND Specimen1.IDENTIFIER   =  ExternalIdentifier1.SPECIMEN_ID   AND atg.tag_id="+aliasName+" and " +
"( ( Specimen1.IDENTIFIER  =atg.obj_id  )   AND UPPER(Specimen1.ACTIVITY_STATUS ) != UPPER('Disabled')  ) "+
" ORDER BY Specimen1.IDENTIFIER ,SpecimenCharacteristics1.IDENTIFIER ,ExternalIdentifier1.IDENTIFIER ," +
"AbstractSpecimen1.IDENTIFIER "; 
			connector.dynamic_loading(true);
			connector.dynamic_loading(10);
			//assigntag atg where atg.tag_id=1 and sp.identifier = atg.obj_id
			String tableColString = "CHKBOX,Barcode,Label,ExternalIdentifier_Name,ExternalIdentifier_Value,SCG_ID,Class,Type,Lineage,Parent_Specimen_Id," +
									"Tissue_Side,Tissue_Site,Pathology_Status,Init_Quantity,Avl_Quantity,Available,Created_On,Identifier";
			//PropertyHandler.getSimpleSearchProperty("Specimen_columns");
//			sql = "";//PropertyHandler.getSimpleSearchProperty("Specimen_sql");
//			String tableColString = "Identifier, Name";
			connector.render_sql(sql, "table_a_id", tableColString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
//				Logger.getLogger(LoadGridServlet.class).error(e.getMessage(), e);

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
//		setJsonString(req.getParameter("paramJson"));
//		setGridType(req.getParameter("gridType"));
		setAliasName(req.getParameter("ppi"));
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
//		setJsonString(req.getParameter("paramJson"));
//		setGridType(req.getParameter("gridType"));
//		setSessionData((SessionDataBean)req.getSession().getAttribute(Constants.SESSION_DATA));
		super.doGet(req, res);

	}
}
