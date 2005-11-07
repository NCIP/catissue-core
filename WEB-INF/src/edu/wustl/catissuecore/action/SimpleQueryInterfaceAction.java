/**
 * <p>Title: SimpleQueryInterfaceAction Class>
 * <p>Description:	SimpleQueryInterfaceAction initializes the fields in the Simple Query Interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.catissuecore.dao.DAOFactory;
import edu.wustl.catissuecore.dao.JDBCDAO;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * SimpleQueryInterfaceAction initializes the fields in the Simple Query Interface.
 * @author gautam_shetty
 */
public class SimpleQueryInterfaceAction extends SecureAction
{
    
    /**
     * Overrides the execute method of Action class.
     */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;
        int counter = Integer.parseInt(simpleQueryInterfaceForm.getCounter());
        
        for (int i=1;i<=counter;i++)
        {
            //Key of previous object.
            String prevKey = "SimpleConditionsNode:"+(i-1)+"_Condition_DataElement_table";
            String prevValue = (String)simpleQueryInterfaceForm.getValue(prevKey);
            
            //Key of present object.
            String key = "SimpleConditionsNode:"+i+"_Condition_DataElement_table";
            String value = (String)simpleQueryInterfaceForm.getValue(key);
            
            //Key of the next operator (AND/OR).
            String nextOperatorKey = "SimpleConditionsNode:"+i+"_Operator_operator";
            String nextOperatorValue = (String)simpleQueryInterfaceForm.getValue(nextOperatorKey);
            
            if (value != null)
            {
                if (!value.equals(Constants.SELECT_OPTION))
                {
                    setColumnNames(request, i, value);
                }
            }
            
            String sql = " select TABLE_A.ALIAS_NAME, TABLE_A.DISPLAY_NAME " +
            			 " from catissue_table_relation TABLE_R, " +
            			 " CATISSUE_QUERY_INTERFACE_TABLE_DATA TABLE_A, " +
            			 " CATISSUE_QUERY_INTERFACE_TABLE_DATA TABLE_B " +
            			 " where TABLE_R.PARENT_TABLE_ID = TABLE_A.TABLE_ID and " +
            			 " TABLE_R.CHILD_TABLE_ID = TABLE_B.TABLE_ID ";
            
			Logger.out.debug("Check sql....................."+sql);
			
			JDBCDAO jdbcDao = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
			jdbcDao.openSession(null);
			List checkList = jdbcDao.executeQuery(sql,null,Constants.INSECURE_RETRIEVE,null,null);
			jdbcDao.closeSession();
            if (i == counter) 
            {
                if (prevValue != null)
                	setNextTableNames(request, i, prevValue, checkList);
                else
                    setAllTableNames(request);
            }
            else
            {
                if (nextOperatorValue != null && !"".equals(nextOperatorValue))
                {
                    String prevValueDisplayName = null;
                    String objectNameValueBeanList = "objectList"+i;
            		
                    JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
                    jdbcDAO.openSession(null);
                    sql = "select DISPLAY_NAME from CATISSUE_QUERY_INTERFACE_TABLE_DATA where ALIAS_NAME='"+value+"'";
                    List list = jdbcDAO.executeQuery(sql,null,Constants.INSECURE_RETRIEVE,null,null);
                    jdbcDAO.closeSession();
                    
                    if (!list.isEmpty())
                    {
                        List rowList = (List)list.get(0);
                        prevValueDisplayName = (String)rowList.get(0);
                    }
                    
                    NameValueBean nameValueBean = new NameValueBean();
                    nameValueBean.setName(prevValueDisplayName);
                    nameValueBean.setValue(value);
                    
                    List objectList = new ArrayList();
                    objectList.add(nameValueBean);
                    
                    request.setAttribute(objectNameValueBeanList, objectList);
                }
            }
        }
        
        request.setAttribute(Constants.ATTRIBUTE_NAME_LIST, Constants.ATTRIBUTE_NAME_ARRAY);
        request.setAttribute(Constants.ATTRIBUTE_CONDITION_LIST, Constants.ATTRIBUTE_CONDITION_ARRAY);
        
        HttpSession session =request.getSession();
        session.setAttribute(Constants.SIMPLE_QUERY_ALIAS_NAME,null);
        session.setAttribute(Constants.SIMPLE_QUERY_COUNTER,null);
        session.setAttribute(Constants.SIMPLE_QUERY_MAP,null);
        
        String pageOf = request.getParameter(Constants.PAGEOF);
        request.setAttribute(Constants.PAGEOF, pageOf);
        
        return mapping.findForward(pageOf);
    }
    
    /**
     * Sets column names depending on the table name selected for that condition.
     * @param request HttpServletRequest
     * @param i number of row.
     * @param value table name.
     * @throws DAOException
     * @throws ClassNotFoundException
     */
    private void setColumnNames(HttpServletRequest request, int i, String value) throws DAOException, ClassNotFoundException
    {
        String attributeNameList = "attributeNameList"+i;
        String attributeDisplayNameList = "attributeDisplayNameList"+i;
        
        String sql = 	" SELECT tableData2.ALIAS_NAME, temp.COLUMN_NAME, temp.ATTRIBUTE_TYPE, temp.TABLES_IN_PATH, temp.DISPLAY_NAME " +
				        " from CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData2 join " +
				        " ( SELECT  columnData.COLUMN_NAME, columnData.TABLE_ID, columnData.ATTRIBUTE_TYPE, " +
				        " displayData.DISPLAY_NAME, relationData.TABLES_IN_PATH " +
				        " FROM CATISSUE_QUERY_INTERFACE_COLUMN_DATA columnData, " +
				        " CATISSUE_TABLE_RELATION relationData, " +
				        " CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData, " +
				        " CATISSUE_SEARCH_DISPLAY_DATA displayData " +
				        " where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and " +
				        " relationData.PARENT_TABLE_ID = tableData.TABLE_ID and " +
				        " relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and " +
				        " columnData.IDENTIFIER = displayData.COL_ID and " +
				        " tableData.ALIAS_NAME = '"+value+"') as temp " +
				        " on temp.TABLE_ID = tableData2.TABLE_ID ";
        
        Logger.out.debug("SQL*****************************"+sql);
        
        JDBCDAO jdbcDao = new JDBCDAO();
        jdbcDao.openSession(null);
        List list = jdbcDao.executeQuery(sql, null, Constants.INSECURE_RETRIEVE, null,null);
        jdbcDao.closeSession();
        
        String [] columnNameList = new String[list.size()];
        String [] columnDisplayNameList = new String[list.size()];
        
        Iterator iterator = list.iterator();
        int j = 0, k=0;
        while (iterator.hasNext())
        {
            List rowList = (List)iterator.next();
            columnNameList[k] = (String)rowList.get(j++)+"."+(String)rowList.get(j++)
            					+"."+(String)rowList.get(j++);
            String tablesInPath = (String)rowList.get(j++);
            if ((tablesInPath != null) && ("".equals(tablesInPath) == false))
            {
                columnNameList[k] = columnNameList[k]+"."+tablesInPath;
            }
            
            columnDisplayNameList[k] = (String)rowList.get(j++);
            j = 0;
            k++;
        }
        
        request.setAttribute(attributeNameList, columnNameList);
        request.setAttribute(attributeDisplayNameList, columnDisplayNameList);    
    }
    
    /**
     * Sets the next table names depending on the table in the previous row. 
     * @param request
     * @param i
     * @param prevValue previous table name.
     * @param nextOperatorValue
     * @param checkList
     * @throws DAOException
     * @throws ClassNotFoundException
     */
    private void setNextTableNames(HttpServletRequest request, int i, String prevValue, List checkList) throws DAOException, ClassNotFoundException
    {
        String objectNameList = "objectList"+i;
        
        List objectList = new ArrayList();
        
        String sql =" (select temp.ALIAS_NAME, temp.DISPLAY_NAME " +
        			" from " +
			        " (select relationData.FIRST_TABLE_ID, tableData.ALIAS_NAME, tableData.DISPLAY_NAME " +
			        " from CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData join " +
			        " CATISSUE_RELATED_TABLES_MAP relationData " +
			        " on tableData.TABLE_ID = relationData.SECOND_TABLE_ID) as temp join CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData2 " +
			        " on temp.FIRST_TABLE_ID = tableData2.TABLE_ID " +
			        " where tableData2.ALIAS_NAME = '"+prevValue+"') " +
			        " union " +
			        " (select temp1.ALIAS_NAME, temp1.DISPLAY_NAME " +
			        " from " +
			        " (select relationData1.SECOND_TABLE_ID, tableData4.ALIAS_NAME, tableData4.DISPLAY_NAME " +
			        " from CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData4 join " +
			        " CATISSUE_RELATED_TABLES_MAP relationData1 " +
			        " on tableData4.TABLE_ID = relationData1.FIRST_TABLE_ID) as temp1 join CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData3 " +
			        " on temp1.SECOND_TABLE_ID = tableData3.TABLE_ID " +
			        " where tableData3.ALIAS_NAME = '"+prevValue+"')";		
            
            Logger.out.debug("TABLE SQL*****************************"+sql);
            
            JDBCDAO jdbcDao = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
            jdbcDao.openSession(null);
            List list = jdbcDao.executeQuery(sql,null,Constants.INSECURE_RETRIEVE,null,null);
            jdbcDao.closeSession();
            
            //Adding NameValueBean of select option.
            NameValueBean nameValueBean = new NameValueBean();
            nameValueBean.setName(Constants.SELECT_OPTION);
            nameValueBean.setValue("-1");
            objectList.add(nameValueBean);
            
            //Adding the NameValueBean of previous selected object.
            JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
            jdbcDAO.openSession(null);
            
            sql = "select DISPLAY_NAME from CATISSUE_QUERY_INTERFACE_TABLE_DATA where ALIAS_NAME='"+prevValue+"'";
            List prevValueDisplayNameList = jdbcDAO.executeQuery(sql,null,Constants.INSECURE_RETRIEVE,null,null);
            jdbcDAO.closeSession();
            
            if (!prevValueDisplayNameList.isEmpty())
            {
                List rowList = (List)prevValueDisplayNameList.get(0);
                nameValueBean = new NameValueBean();
                nameValueBean.setName((String)rowList.get(0));
                nameValueBean.setValue(prevValue);
                objectList.add(nameValueBean);
            }
            
            Iterator iterator = list.iterator();
            while (iterator.hasNext())
            {
                int j=0;
                List rowList = (List)iterator.next();
                if (checkForTable(rowList, checkList))
                {
                    nameValueBean = new NameValueBean();
                    nameValueBean.setValue((String)rowList.get(j++));
                    nameValueBean.setName((String)rowList.get(j));
                    objectList.add(nameValueBean);
                }
            }
            
            request.setAttribute(objectNameList, objectList);
    }
    
    /**
     * Returns the object id of the protection element that represents
     * the Action that is being requested for invocation.
     * @param clazz
     * @return
     */
    protected String getObjectIdForSecureMethodAccess(HttpServletRequest request)
    {
        String aliasName = request.getParameter("aliasName");
        if(aliasName!=null && !aliasName.equals(""))
        {
            return this.getClass().getName()+"_"+aliasName;
        }
        else
        {
            return super.getObjectIdForSecureMethodAccess(request);
        }
    }
    
    /**
     * @param mapping
     * @return
     */
    protected ActionForward getActionForward(HttpServletRequest request,ActionMapping mapping)
    {
        String aliasName = request.getParameter("aliasName");
        if(aliasName.equals("User") || aliasName.equals("Institution") || aliasName.equals("Department")|| aliasName.equals("CancerResearchGroup")|| aliasName.equals("Site")|| aliasName.equals("StorageType")|| aliasName.equals("StorageContainer")|| aliasName.equals("BioHazard")|| aliasName.equals("CollectionProtocol")|| aliasName.equals("DistributionProtocol"))
        {
            return mapping.findForward(Constants.ACCESS_DENIED_ADMIN);
        }
        else if(aliasName.equals("Participant") ||aliasName.equals("CollectionProtocolRegistration") ||aliasName.equals("SpecimenCollectionGroup") ||aliasName.equals("Specimen") ||aliasName.equals("Distribution"))
        {
            return mapping.findForward(Constants.ACCESS_DENIED_BIOSPECIMEN);
        }
        else
        {
            return mapping.findForward(Constants.ACCESS_DENIED);
        }
    }
    
    private boolean checkForTable(List rowList, List checkList)
    {
        String aliasName = (String)rowList.get(0), displayName = (String)rowList.get(1);
        Iterator iterator = checkList.iterator();
        while (iterator.hasNext())
        {
            List row = (List) iterator.next();
            if (aliasName.equals((String) row.get(0)) && displayName.equals((String) row.get(1)))
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Sets all the tables in the simple query interface.
     * @param request
     * @throws DAOException
     * @throws ClassNotFoundException
     */
    private void setAllTableNames(HttpServletRequest request)throws DAOException, ClassNotFoundException
    {
        	String sql = " select distinct tableData.DISPLAY_NAME, tableData.ALIAS_NAME " +
        				 " from CATISSUE_TABLE_RELATION tableRelation join CATISSUE_QUERY_INTERFACE_TABLE_DATA " +
        				 " tableData on tableRelation.PARENT_TABLE_ID = tableData.TABLE_ID ";

			String aliasName = request.getParameter(Constants.TABLE_ALIAS_NAME);
			if ((aliasName != null) && (!"".equals(aliasName)))
			{
				sql = sql + " where tableData.ALIAS_NAME = '"+ aliasName +"'";
				request.setAttribute(Constants.TABLE_ALIAS_NAME,aliasName);
			}
			sql = sql + " ORDER BY tableData.DISPLAY_NAME ";
			
			JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
			jdbcDAO.openSession(null);
			List tableList = jdbcDAO.executeQuery(sql,null,Constants.INSECURE_RETRIEVE,null,null);
			jdbcDAO.closeSession();
			
			String [] objectDisplayNames = null;
			String [] objectAliasNames = null;
			int i = 0;
			
			if ((aliasName != null) && (!"".equals(aliasName)))
			{
				objectDisplayNames = new String[tableList.size()];
				objectAliasNames = new String[tableList.size()];
				setColumnNames(request,1,aliasName);
			}
			else
			{
				objectDisplayNames = new String[tableList.size()+1];
				objectAliasNames = new String[tableList.size()+1];
				
				objectAliasNames[i] = "-1";
				objectDisplayNames[i] = Constants.SELECT_OPTION;
				i++;
			}
			
			Iterator objIterator = tableList.iterator();
			while (objIterator.hasNext())
			{
				List row = (List) objIterator.next();
				objectDisplayNames[i] = (String)row.get(0);
				objectAliasNames[i] = (String)row.get(1);
				i++;
			}
			
			request.setAttribute(Constants.OBJECT_DISPLAY_NAME_LIST, objectDisplayNames);
			request.setAttribute(Constants.OBJECT_ALIAS_NAME_LIST, objectAliasNames);
	}
}
