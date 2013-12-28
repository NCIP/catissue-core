package krishagni.catissueplus.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;

import krishagni.catissueplus.dao.CollectionProtocolDao;
import krishagni.catissueplus.dto.CollectionProtocolDTO;

public class CollectionProtocolDaoImpl implements CollectionProtocolDao {

	@Override
	public List<CollectionProtocolDTO> getCollectionProtocols() {
		return JdbcDaoFactory.getJdbcDao().getResultSet(
				GET_CP_SQL, null, 
				new ResultExtractor<List<CollectionProtocolDTO>>() {
					@Override
					public List<CollectionProtocolDTO> extract(ResultSet rs)
					throws SQLException {
						List<CollectionProtocolDTO> cps = new ArrayList<CollectionProtocolDTO>();
						while (rs.next()) {
							CollectionProtocolDTO cp = new CollectionProtocolDTO();
							cp.setId(rs.getLong(1));
							cp.setShortTitle(rs.getString(2));
							cp.setTitle(rs.getString(3));
							cps.add(cp);
						}
						
						return cps;
					}
				});
	}
	
	private static final String GET_CP_SQL = 
			"select identifier, short_title, title from catissue_specimen_protocol";
}
