package com.krishagni.catissueplus.bulkoperator.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.ResultExtractor;
import com.krishagni.catissueplus.bulkoperator.bizlogic.BulkOperationBizLogic;
import com.krishagni.catissueplus.bulkoperator.generatetemplate.BulkOperationTemplate;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationConstants;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.query.generator.ColumnValueBean;

public class BulkOperationDao {
	
	private static final Logger logger = Logger.getLogger(BulkOperationBizLogic.class);

	private JdbcDao jdbcDao = null;
	
	public BulkOperationDao(JdbcDao jdbcDao) {
	    this.jdbcDao = jdbcDao;
	}

    public void uploadTemplate(BulkOperationTemplate boTemplate, String dbType) {
		try {
			
			if(doesTemplateExists(boTemplate.getTemplateName())) {
				updateTemplate(boTemplate);
			} else {
				insertTemplate(boTemplate, dbType);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error occured in persisting the template",e);
		}
	}

	private void insertTemplate(BulkOperationTemplate template, String dbType)
	throws Exception {
		List<Object> params = new ArrayList<Object>();

		try {
			params.add(template.getOperationName());
			params.add(template.getTemplateName());
			params.add(template.getCsvTemplate());
			params.add(template.getXmlTemplate());
			
			if (dbType == null) {
				if (BulkOperationConstants.ORACLE_DATABASE.equalsIgnoreCase(BulkOperationUtility.getDatabaseType())) {
					jdbcDao.executeUpdate(INSERT_TEMPLATE_ORA_SQL, params);
				} else if (BulkOperationConstants.MYSQL_DATABASE.equalsIgnoreCase(BulkOperationUtility.getDatabaseType())) {
					jdbcDao.executeUpdate(INSERT_TEMPLATE_MYSQL_SQL, params);
				}
			} else if (BulkOperationConstants.ORACLE_DATABASE.equalsIgnoreCase(dbType)) {
				jdbcDao.executeUpdate(INSERT_TEMPLATE_ORA_SQL, params);
			} else if (BulkOperationConstants.MYSQL_DATABASE.equalsIgnoreCase(dbType)) {
				jdbcDao.executeUpdate(INSERT_TEMPLATE_MYSQL_SQL, params);
			}
			logger.info("Template is Inserted: " + template.getTemplateName());
		} catch (Exception e) {
			throw new RuntimeException("Error inserting bulk operation template", e);
		} 
	}
	
	private void updateTemplate(BulkOperationTemplate template) 
	throws Exception {
		List<Object> params = new ArrayList<Object>();

		try {
			params.add(template.getCsvTemplate());
			params.add(template.getXmlTemplate());
			params.add(template.getTemplateName());

			jdbcDao.executeUpdate(UPDATE_TEMPLATE, params);
			logger.info("Template is Updated: " + template.getTemplateName());
		} catch (Exception e) {
			throw new RuntimeException("Error updating bulk operation template", e);
		} 
	}

    public boolean doesTemplateExists(String templateName) throws BulkOperationException {
        return jdbcDao.getResultSet(GET_TEMPLATE_SQL, Collections.singletonList(templateName), new ResultExtractor<Boolean>() {
            @Override
            public Boolean extract(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    if (rs.getString(1) != null) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private static final String GET_TEMPLATE_SQL=
            "SELECT " +
                    "	OPERATION " +
                    "FROM" +
                    "	CATISSUE_BULK_OPERATION " +
                    "WHERE " +
                    "	OPERATION = ?";


    private static final String INSERT_TEMPLATE_ORA_SQL =
            "INSERT INTO " +
                    "	CATISSUE_BULK_OPERATION " +
                    "(IDENTIFIER, OPERATION, DROPDOWN_NAME, CSV_TEMPLATE, XML_TEMPALTE) " +
                    "	VALUES (CATISSUE_BULK_OPERATION_SEQ.NEXTVAL, ?, ?, ?, ?) ";

    private static final String INSERT_TEMPLATE_MYSQL_SQL =
            "INSERT INTO " +
                    "	CATISSUE_BULK_OPERATION " +
                    "(IDENTIFIER, OPERATION, DROPDOWN_NAME, CSV_TEMPLATE, XML_TEMPALTE) " +
                    "	VALUES (default, ?, ?, ?, ?) ";


    private static final String UPDATE_TEMPLATE =
            "UPDATE " +
                    "	CATISSUE_BULK_OPERATION " +
                    "SET  " +
                    "	CSV_TEMPLATE = ?, XML_TEMPALTE = ? " +
                    "WHERE " +
                    "	DROPDOWN_NAME= ? ";

}
