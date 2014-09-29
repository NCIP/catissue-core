package krishagni.catissueplus.upgrade;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;

public class UpgradeUtil {
	public static Long getUserId(String username) 
	throws Exception {
		JdbcDao jdbcDao = JdbcDaoFactory.getJdbcDao();
		List<Object> params = new ArrayList<Object>();
		params.add(username);
		
		Long userId = jdbcDao.getResultSet(GET_USER_ID_SQL, params, new ResultExtractor<Long>() {
			@Override
			public Long extract(ResultSet rs) throws SQLException {
				return rs.next() ? rs.getLong(1) : null;
			}			
		});
		
		if (userId == null) {
			logger.error("No active user with login name: " + username);
			return null;
		}
		
		return userId;
	}
	
	private static final String GET_USER_ID_SQL = 
			"select" +
			"	identifier " +
			"from catissue_user " +
			"	where login_name = ? AND activity_status = 'Active'";
	
	private static final Logger logger = Logger.getLogger(UpgradeUtil.class);
}
