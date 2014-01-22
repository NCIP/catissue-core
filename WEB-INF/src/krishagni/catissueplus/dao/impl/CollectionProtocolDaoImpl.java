package krishagni.catissueplus.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;

import krishagni.catissueplus.dao.CollectionProtocolDao;
import krishagni.catissueplus.dto.CollectionProtocolDTO;
import krishagni.catissueplus.dto.FormDetailsDTO;

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
	
	@Override
	public List<FormDetailsDTO> getForms(final Long cpId) {
		return JdbcDaoFactory.getJdbcDao().getResultSet(
				GET_CP_FORMS_SQL, Collections.singletonList(cpId), 
				new ResultExtractor<List<FormDetailsDTO>>() {
					@Override
					public List<FormDetailsDTO> extract(ResultSet rs)
					throws SQLException {
						List<FormDetailsDTO> forms = new ArrayList<FormDetailsDTO>();
						while (rs.next()) {
							FormDetailsDTO form = new FormDetailsDTO();
							form.setCpId(cpId);
							form.setId(rs.getLong(1));
							form.setContainerId(rs.getLong(2));
							form.setName(rs.getString(3));
							form.setCaption(rs.getString(4));
							forms.add(form);
						}
						
						return forms;
					}
				});
	}
	
	private static final String GET_CP_SQL = 
			"select identifier, short_title, title from catissue_specimen_protocol";
	
	private static final String GET_CP_FORMS_SQL =
			"select " +
			"  ctxt.identifier, c.identifier, c.name, c.caption " +
			"from " +
			"  catissue_form_context ctxt " +
			"  inner join dyextn_containers c on ctxt.container_id = c.identifier " +
			"where " +
			"  ctxt.cp_id = -1 or ctxt.cp_id = ?";
}
