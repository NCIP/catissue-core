/**
 * <p>Title: ResultData Class>
 * <p>Description:	ResultData is used to generate the data required for the result view of query interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.catissuecore.query;

import java.util.List;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * ResultData is used to generate the data required for the result view of query interface.
 * @author gautam_shetty
 */
public class ResultData
{

    /**
     * Query results temporary table.
     */
    private String tmpResultsTableName = new String();

    public List getSpreadsheetViewData(String[] whereColumnName, String[] whereColumnValue, String[] whereColumnCondition,String[] columnList, SessionDataBean sessionDataBean, int securityParam)
    {
        
    	tmpResultsTableName = Constants.QUERY_RESULTS_TABLE+"_"+sessionDataBean.getUserId();
        
        if (whereColumnName[0].equals(Constants.ROOT))
        {
        	Logger.out.debug("inside root condition........."+whereColumnName[0]);
        	//columnList = null;
        	whereColumnName = null;
            whereColumnCondition = null;
            whereColumnValue = null;
        }
        /*if(whereColumnName.length==2)
        	Logger.out.debug("arrayvalues:"+whereColumnName[0]+":"+whereColumnName[1]+":"+whereColumnValue[0]+":"+whereColumnValue[1]);*/
        List dataList = null;
        try
        {
            AbstractDAO dao = DAOFactory.getDAO(Constants.JDBC_DAO);
            dao.openSession(sessionDataBean);
            dataList = dao.retrieve(tmpResultsTableName,columnList,
                    				 whereColumnName,whereColumnCondition,
                    				 whereColumnValue,null);
            Logger.out.debug("List of spreadsheet data for advance search:"+dataList);
             dao.closeSession();
        }
        catch (DAOException sqlExp)
        {
            Logger.out.error(sqlExp.getMessage(),sqlExp);
        }
        catch (Exception exp)
        {
            Logger.out.error(exp.getMessage(),exp);
        }
        
        return dataList;
    }
}