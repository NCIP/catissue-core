package krishagni.catissueplus.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;
import edu.wustl.common.beans.SessionDataBean;

import krishagni.catissueplus.dao.FormDao;
import krishagni.catissueplus.dto.FormDetailsDTO;
import krishagni.catissueplus.dto.FormRecordDetailsDTO;

public class FormDaoImpl implements FormDao {
	@Override
	public Long getContainerId(Long formId) {
		return JdbcDaoFactory.getJdbcDao().getResultSet(
				GET_CONTAINER_ID_SQL, 
				Collections.singletonList(formId), 
				new ResultExtractor<Long>() {
					@Override
					public Long extract(ResultSet rs) throws SQLException {
						return rs.next() ? rs.getLong(1) : null;
					}
				});
	}
	
	@Override
	public FormDetailsDTO getFormByContainerId(Long cpId, String entity, Long containerId) {
		List<Object> params = new ArrayList<Object>();
		params.add(entity);
		params.add(cpId);
		params.add(containerId);
		
		return JdbcDaoFactory.getJdbcDao().getResultSet(
				GET_FORM_BY_CONTAINER_SQL, params,
				new ResultExtractor<FormDetailsDTO>() {
					@Override
					public FormDetailsDTO extract(ResultSet rs)
					throws SQLException {
						if (!rs.next()) {
							return null;
						}
						
						FormDetailsDTO formDetails = new FormDetailsDTO();
						formDetails.setId(rs.getLong(1));
						formDetails.setName(rs.getString(2));
						formDetails.setCaption(rs.getString(3));
						formDetails.setContainerId(rs.getLong(4));
						formDetails.setEntityType(rs.getString(5));
						
						return formDetails;
					}					
				});
	}
	
	
	@Override
	public List<FormDetailsDTO> getForms(Long cpId, String entity, Long objectId) {
		List<Object> params = new ArrayList<Object>();
		params.add(objectId);
		params.add(entity);
		params.add(cpId);
		
		return JdbcDaoFactory.getJdbcDao().getResultSet(GET_FORMS_SQL, params, getFormDetailsDTOExtractor());
	}
	
	@Override
	public void insertForm(FormDetailsDTO form) {
		try {
			List<Object> params = new ArrayList<Object>();
			params.add(form.getContainerId());
			params.add(form.getEntityType());
			params.add(form.getCpId());
			
			Number id = JdbcDaoFactory.getJdbcDao().executeUpdateAndGetKey(INSERT_FORM_SQL, params, "identifier");
			form.setId(id.longValue());							
		} catch (Exception e) {
			throw new RuntimeException("Error inserting form context", e);
		}
	}


	@Override
	public List<FormRecordDetailsDTO> getFormRecords(Long formId, Long objectId) {
		List<Object> params = new ArrayList<Object>();
		params.add(objectId);
		params.add(formId);
		
		return JdbcDaoFactory.getJdbcDao().getResultSet(GET_FORM_RECORDS_SQL, params, getFormRecordsDTOExtractor());
	}
	
	@Override
	public void insertFormRecord(Long formId, Long objectId, Long recordId, Long userId) {
		List<Object> params = new ArrayList<Object>();
		params.add(formId);
		params.add(objectId);
		params.add(recordId);
		params.add(userId);
		
		JdbcDaoFactory.getJdbcDao().executeUpdate(INSERT_RECORD_SQL, params);
	}
	
	@Override
	public void updateFormRecord(Long formId, Long objectId, Long recordId, Long userId) {
		List<Object> params = new ArrayList<Object>();
		params.add(userId);
		params.add(formId);
		params.add(objectId);
		params.add(recordId);
				
		JdbcDaoFactory.getJdbcDao().executeUpdate(UPDATE_RECORD_SQL, params);
	}
	
	@Override
	public Long getCpIdByScgId(Long scgId) {
		return JdbcDaoFactory.getJdbcDao().getResultSet(
				GET_CP_ID_BY_SCG_SQL, Collections.singletonList(scgId), getLongExtractor()); 
	}
	
		
	@Override
	public Long getCpIdBySpecimenId(Long specimenId) {
		return JdbcDaoFactory.getJdbcDao().getResultSet(
				GET_CP_ID_BY_SPECIMEN_ID_SQL, Collections.singletonList(specimenId), getLongExtractor()); 
	}
	
	@Override
	public Long getCpIdByRegistrationId(Long cprId) {
		return JdbcDaoFactory.getJdbcDao().getResultSet(
				GET_CP_ID_BY_REGISTRATION_ID_SQL, Collections.singletonList(cprId), getLongExtractor()); 
	}
	
	private ResultExtractor<Long> getLongExtractor() {
		return new ResultExtractor<Long>() {
			@Override
			public Long extract(ResultSet rs) throws SQLException {
				return rs.next() ? rs.getLong(1) : null;
			}			
		};
	}

	
	private ResultExtractor<List<FormDetailsDTO>> getFormDetailsDTOExtractor() {
		return new ResultExtractor<List<FormDetailsDTO>>() {
			@Override
			public List<FormDetailsDTO> extract(ResultSet rs) throws SQLException {
				List<FormDetailsDTO> formDtos = new ArrayList<FormDetailsDTO>();

				while (rs.next()) {
					FormDetailsDTO formDto = new FormDetailsDTO();
					formDto.setId(rs.getLong(1));
					formDto.setName(rs.getString(2));
					formDto.setCaption(rs.getString(3));
					formDto.setNoOfRecords(rs.getLong(4));
					
					formDtos.add(formDto);							
				}
				
				return formDtos;
			}
		};
	}
	
	private ResultExtractor<List<FormRecordDetailsDTO>> getFormRecordsDTOExtractor() {
		return new ResultExtractor<List<FormRecordDetailsDTO>>() {
			@Override
			public List<FormRecordDetailsDTO> extract(ResultSet rs) throws SQLException {
				List<FormRecordDetailsDTO> recsDto = new ArrayList<FormRecordDetailsDTO>();
				
				while (rs.next()) {
					FormRecordDetailsDTO recDto = new FormRecordDetailsDTO();
					recDto.setRecordId(rs.getLong(1));					
					recDto.setUpdateTime(rs.getDate(2));
					recDto.setUpdatedBy(rs.getString(3));
					
					recsDto.add(recDto);
				}
				
				return recsDto;
			}			
		};
	}
	
	private static final String GET_CONTAINER_ID_SQL = 
			"select " +
			"  ctxt.container_id " +
			"from " +
			"  catissue_form_context ctxt " +
			"where " +
			"  ctxt.identifier = ?";
			
	
	private static final String GET_FORMS_SQL =
			"select " +
			"  ctxt.identifier, c.name, c.caption, count(re.identifier) " +
			"from " +
			"  catissue_form_context ctxt " +
			"  inner join dyextn_containers c on ctxt.container_id = c.identifier " +
			"  left join catissue_form_record_entry re on re.form_ctxt_id = ctxt.identifier and re.object_id = ? " +
			"where " +
			"  ctxt.entity_type = ? and " +
			"  (ctxt.cp_id = -1 or ctxt.cp_id = ?) " +
			"group by " +
			"  ctxt.identifier, c.name, c.caption";
	
	private static final String GET_FORM_BY_CONTAINER_SQL = 
			"select " +
			"  ctxt.identifier, c.name, c.caption, c.identifier, ctxt.entity_type " +
			"from " +
			"  catissue_form_context ctxt " +
			"  inner join dyextn_containers c on ctxt.container_id = c.identifier " +
			"where " +
			"  ctxt.entity_type = ? and " +
			"  (ctxt.cp_id = -1 or ctxt.cp_id = ?) and " +
			"  c.identifier = ?"; 
	
	private static final String GET_FORM_RECORDS_SQL =
			"select " +
			"  re.record_id, re.update_time, re.updated_by " +
			"from " +
			"  catissue_form_record_entry re " +
			"where " +
			"  re.object_id = ? and " +
			"  re.form_ctxt_id = ? " +
			"order by " +
			"  re.update_time";
	
	private static final String INSERT_FORM_SQL =
			"insert into catissue_form_context values(default, ?, ?, ?)";
	
	private static final String INSERT_RECORD_SQL = 
			"insert into catissue_form_record_entry values(default, ?, ?, ?, ?, current_timestamp())";
	
	private static final String UPDATE_RECORD_SQL =
			"update " +
			"catissue_form_record_entry " +					
			"  set update_time = current_timestamp(), updated_by = ? " +
			"where " +
			"  form_ctxt_id = ? and object_id = ? and record_id = ?";
	
	
	private static final String GET_CP_ID_BY_SCG_SQL = 
			"select " +
			"  event.collection_protocol_id " +
			"from " +
			"  catissue_specimen_coll_group scg " +
			"  inner join catissue_coll_prot_event event on event.identifier = scg.collection_protocol_event_id " +
			"where " +
			"  scg.identifier = ?";	
		
	private static final String GET_CP_ID_BY_SPECIMEN_ID_SQL =
			"select " +
			"  cpe.collection_protocol_id " +
			"from " +
			"  catissue_specimen specimen " +
			"  inner join catissue_specimen_coll_group scg on scg.identifier = specimen.specimen_collection_group_id " +
			"  inner join catissue_coll_prot_event cpe on cpe.identifier = scg.collection_protocol_event_id " +
			"where " +
			"  specimen.identifier = ?";
	
	private static final String GET_CP_ID_BY_REGISTRATION_ID_SQL =
			"select " +
			"  cpr.collection_protocol_id " +
			"from " +
			"  catissue_coll_prot_reg cpr " +
			"where " +
			"  cpr.identifier = ?";

}
