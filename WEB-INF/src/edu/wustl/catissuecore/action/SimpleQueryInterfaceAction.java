/**
 * <p>Title: SimpleQueryInterfaceAction Class>
 * <p>Description:	SimpleQueryInterfaceAction initializes the fields in the Simple Query Interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.dao.DAOFactory;
import edu.wustl.catissuecore.dao.JDBCDAO;
import edu.wustl.catissuecore.util.global.Constants;
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
        AbstractBizLogic dao = BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
        
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
            
            Logger.out.debug(" i : "+i);
            Logger.out.debug(" nextOperatorValue ...................."+nextOperatorValue);
            
            if (value != null)
            {
                if (!value.equals(Constants.SELECT_OPTION))
                {
                    setColumnNames(request, i, value);
                }
            }

            if (prevValue != null)
                setNextTableNames(request, i, prevValue, nextOperatorValue);
            else
            {
                if (nextOperatorValue != null && !nextOperatorValue.equals(""))
                {
                    String prevValueDisplayName = null;
                    String objectNameList = "objectNameList"+i;
            		String objectDisplayNameList = "objectDisplayNameList"+i;

                    JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
                    jdbcDAO.openSession(null);
                    String sql = "select DISPLAY_NAME from CATISSUE_QUERY_INTERFACE_TABLE_DATA where ALIAS_NAME='"+value+"'";
                    List list = jdbcDAO.executeQuery(sql,null,Constants.INSECURE_RETRIEVE,null,null);
                    jdbcDAO.closeSession();
                    
                    if (!list.isEmpty())
                    {
                        List rowList = (List)list.get(0);
                        prevValueDisplayName = (String)rowList.get(0);
                    }
                    
                    String [] aliasNameList = {value};
                    String [] displayNameList = {prevValueDisplayName};
                    
                    request.setAttribute(objectNameList, aliasNameList);
                    request.setAttribute(objectDisplayNameList, displayNameList);
                }
            }
        }
        
        String aliasName = request.getParameter(Constants.TABLE_ALIAS_NAME);
        String sql = " select distinct tableData.DISPLAY_NAME, tableData.ALIAS_NAME " +
        			 " from CATISSUE_TABLE_RELATION tableRelation join CATISSUE_QUERY_INTERFACE_TABLE_DATA " +
        			 " tableData on tableRelation.PARENT_TABLE_ID = tableData.TABLE_ID ";
        
        if ((aliasName != null) && (!"".equals(aliasName)))
        {
            sql = sql + " where tableData.ALIAS_NAME = '"+ aliasName +"'";
            request.setAttribute(Constants.TABLE_ALIAS_NAME,aliasName);
        }
        
       
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
        
        request.setAttribute(Constants.ATTRIBUTE_NAME_LIST, Constants.ATTRIBUTE_NAME_ARRAY);
        request.setAttribute(Constants.ATTRIBUTE_CONDITION_LIST, Constants.ATTRIBUTE_CONDITION_ARRAY);
        
        String pageOf = request.getParameter(Constants.PAGEOF);
        request.setAttribute(Constants.PAGEOF, pageOf);
        
//        //Fix as per Bug : 521
//        String title = "SIMPLE SEARCH";
//        request.setAttribute(Constants.SIMPLE_QUERY_INTERFACE_TITLE, title);
        
        return mapping.findForward(pageOf);
    }
    
    /**
     * @param request
     * @param i
     * @param value
     * @throws DAOException
     * @throws ClassNotFoundException
     */
    private void setColumnNames(HttpServletRequest request, int i, String value) throws DAOException, ClassNotFoundException
    {
        String attributeNameList = "attributeNameList"+i;
        String attributeDisplayNameList = "attributeDisplayNameList"+i;

        String sql = 	" SELECT tableData2.ALIAS_NAME, temp.COLUMN_NAME, temp.ATTRIBUTE_TYPE, temp.DISPLAY_NAME " +
				        " from CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData2 join " +
				        " ( SELECT  columnData.COLUMN_NAME, columnData.TABLE_ID, columnData.ATTRIBUTE_TYPE, displayData.DISPLAY_NAME " +
				        " FROM CATISSUE_QUERY_INTERFACE_COLUMN_DATA columnData, " +
				        " CATISSUE_TABLE_RELATION relationData, " +
				        " CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData, " +
				        " CATISSUE_SEARCH_DISPLAY_DATA displayData " +
				        " where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and " +
				        " relationData.PARENT_TABLE_ID = tableData.TABLE_ID and " +
				        " relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and " +
				        " columnData.IDENTIFIER = displayData.COL_ID and " +
				        " tableData.ALIAS_NAME = '"+value+"') as temp " +
				        " on temp.TABLE_ID = tableData2.TABLE_ID";
        
        Logger.out.debug("SQL*****************************"+sql);
        
        JDBCDAO jdbcDao = new JDBCDAO();
        jdbcDao.openSession(null);
        List list = jdbcDao.executeQuery(sql, null, Constants.INSECURE_RETRIEVE, null,null);
        jdbcDao.closeSession();
        
        String [] columnNameList = new String[list.size()];
        String [] columnDisplayNameList = new String[list.size()];
        
        Iterator iterator = list.iterator();
        int j = 0,k=0;
        while (iterator.hasNext())
        {
            List rowList = (List)iterator.next();
            columnNameList[k] = (String)rowList.get(j++)+"."+(String)rowList.get(j++)+"."+(String)rowList.get(j++);
            columnDisplayNameList[k] = (String)rowList.get(j++);
            j = 0;
            k++;
        }
        
        request.setAttribute(attributeNameList, columnNameList);
        request.setAttribute(attributeDisplayNameList, columnDisplayNameList);    
    }
    
    private void setNextTableNames(HttpServletRequest request, int i, String prevValue, String nextOperatorValue) throws DAOException, ClassNotFoundException
    {
        String objectNameList = "objectNameList"+i;
		String objectDisplayNameList = "objectDisplayNameList"+i;
		
		String [] aliasNameList = null;
        String [] displayNameList = null;
        
        Logger.out.debug("Count i :............. "+i);
        Logger.out.debug("nextOperatorValue........................"+nextOperatorValue);
        
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
			        " where tableData3.ALIAS_NAME = '"+prevValue+"') ";		
            
            Logger.out.debug("TABLE SQL*****************************"+sql);
            
            JDBCDAO jdbcDao = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
            jdbcDao.openSession(null);
            List list = jdbcDao.executeQuery(sql,null,Constants.INSECURE_RETRIEVE,null,null);
            jdbcDao.closeSession();
            
            aliasNameList = new String[list.size()+2];
            displayNameList = new String[list.size()+2];
            
            aliasNameList[0] = "-1";
            displayNameList[0] = Constants.SELECT_OPTION;
            
            JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
            jdbcDAO.openSession(null);
            sql = "select DISPLAY_NAME from CATISSUE_QUERY_INTERFACE_TABLE_DATA where ALIAS_NAME='"+prevValue+"'";
            Logger.out.debug("DISPLAY_NAME sql.........................."+sql);
            List prevValueDisplayNameList = jdbcDAO.executeQuery(sql,null,Constants.INSECURE_RETRIEVE,null,null);
            jdbcDAO.closeSession();
            
            String prevValueDisplayName = null;
            if (!prevValueDisplayNameList.isEmpty())
            {
                List rowList = (List)prevValueDisplayNameList.get(0);
                prevValueDisplayName = (String)rowList.get(0);
            }
            
            aliasNameList[1] = prevValue;
            displayNameList[1] = prevValueDisplayName;
            
            Iterator iterator = list.iterator();
            int k=2;
            while (iterator.hasNext())
            {
                int j=0;
                List rowList = (List)iterator.next();
                aliasNameList[k] = (String)rowList.get(j++);
                displayNameList[k] = (String)rowList.get(j);
                k++;
            }
            
            request.setAttribute(objectNameList, aliasNameList);
            request.setAttribute(objectDisplayNameList, displayNameList);
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
}
