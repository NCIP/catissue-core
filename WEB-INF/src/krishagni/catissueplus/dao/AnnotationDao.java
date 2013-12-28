package krishagni.catissueplus.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.ResultExtractor;
import edu.wustl.common.beans.NameValueBean;

public class AnnotationDao {

	private JdbcDao jdbcDao = null;

	private static final String GET_ANNOTATIONS_SQL = 
			"SELECT C.NAME, C.IDENTIFIER " + 
			"	FROM CATISSUE_FORM_CONTEXT FC " +
			"INNER JOIN DYEXTN_CONTAINERS C ON C.IDENTIFIER = FC.CONTAINER_ID " +
			"	WHERE ENTITY_TYPE = ? AND (CP_TITLE IS NULL OR CP_TITLE = ?) ";
	
	
	public AnnotationDao(JdbcDao dao) {
		this.jdbcDao = dao;
	}

	public List<NameValueBean> getAnnotationList(String entityType, String cpTitle) throws Exception {
		List<Object> params = new ArrayList<Object>();				
		params.add(entityType);
		params.add(cpTitle);
		
		return jdbcDao.getResultSet(GET_ANNOTATIONS_SQL, params,  new ResultExtractor<List<NameValueBean>>() {
			@Override
			public List<NameValueBean> extract(ResultSet rs)
			throws SQLException {
				List<NameValueBean> result = new ArrayList<NameValueBean>();
				while (rs.next()) {
					result.add(new NameValueBean( rs.getString("NAME"), rs.getInt("IDENTIFIER")));				
				}
				
				return result;
			}				
		});
	}

	public int getNoOfFormRecords(Long formId) {
		Container c = Container.getContainer(formId);
		String tableName = c.getDbTableName();	

		String query = "SELECT IDENTIFIER FROM " + tableName;
		Integer noOfRecords = jdbcDao.getResultSet(query, null, new ResultExtractor<Integer>() {
			@Override
			public Integer extract(ResultSet rs)
			throws SQLException {
				List<Long> ids = new ArrayList<Long>();
				while (rs.next()) {
					ids.add(rs.getLong("IDENTIFIER"));
				}
				return ids.size();
			}	
		});
		return noOfRecords.intValue();
	}
}
