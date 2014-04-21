package krishagni.catissueplus.dao.impl;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//import krishagni.catissueplus.dao.FormDao;
//import krishagni.catissueplus.dto.FormDetailsDTO;
//import krishagni.catissueplus.dto.FormRecordDetailsDTO;
//
//import org.hibernate.SessionFactory;
//import org.springframework.stereotype.Repository;
//
//import edu.common.dynamicextensions.ndao.JdbcDao;
//import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
//import edu.common.dynamicextensions.ndao.ResultExtractor;
//import edu.wustl.bulkoperator.metadata.HookingInformation;
//
//@Repository("formDao")
public class FormDaoImpl { //implements FormDao {
}
//	
//	private SessionFactory sessionFactory;
//
//	public SessionFactory getSessionFactory() {
//		return sessionFactory;
//	}
//
//	public void setSessionFactory(SessionFactory sessionFactory) {
//		this.sessionFactory = sessionFactory;
//	}
//	
//	@Override
//	public Long getContainerId(Long formId) {
//		return JdbcDaoFactory.getJdbcDao().getResultSet(
//				GET_CONTAINER_ID_SQL, 
//				Collections.singletonList(formId), 
//				new ResultExtractor<Long>() {
//					@Override
//					public Long extract(ResultSet rs) throws SQLException {
//						return rs.next() ? rs.getLong(1) : null;
//					}
//				});
//	}
//	
//	@Override
//	public FormDetailsDTO getFormByContainerId(Long cpId, String entity, Long containerId) {
//		List<Object> params = new ArrayList<Object>();
//		params.add(entity);
//		params.add(cpId);
//		params.add(containerId);
//		
//		return JdbcDaoFactory.getJdbcDao().getResultSet(
//				GET_FORM_BY_CONTAINER_SQL, params,
//				new ResultExtractor<FormDetailsDTO>() {
//					@Override
//					public FormDetailsDTO extract(ResultSet rs)
//					throws SQLException {
//						if (!rs.next()) {
//							return null;
//						}
//						
//						FormDetailsDTO formDetails = new FormDetailsDTO();
//						formDetails.setId(rs.getLong(1));
//						formDetails.setName(rs.getString(2));
//						formDetails.setCaption(rs.getString(3));
//						formDetails.setContainerId(rs.getLong(4));
//						formDetails.setEntityType(rs.getString(5));
//						
//						return formDetails;
//					}					
//				});
//	}
//	
//	
//	@Override
//	public List<FormDetailsDTO> getForms(Long cpId, String entity, Long objectId) {
//		List<Object> params = new ArrayList<Object>();
//		params.add(objectId);
//		params.add(entity);
//		params.add(cpId);
//		
//		return JdbcDaoFactory.getJdbcDao().getResultSet(GET_FORMS_SQL, params, getFormDetailsDTOExtractor());
//	}
//	
//
//
//	@Override
//	public List<FormRecordDetailsDTO> getFormRecords(Long formId, Long objectId) {
//		List<Object> params = new ArrayList<Object>();
//		params.add(objectId);
//		params.add(formId);
//		
//		return JdbcDaoFactory.getJdbcDao().getResultSet(GET_FORM_RECORDS_SQL, params, getFormRecordsDTOExtractor());
//	}
//	
//	@Override
//	public void insertForm(FormDetailsDTO form) {
//		try {
//			List<Object> params = new ArrayList<Object>();
//			params.add(form.getContainerId());
//			params.add(form.getEntityType());
//			params.add(form.getCpId());
//			
//			Number id = JdbcDaoFactory.getJdbcDao().executeUpdateAndGetKey(INSERT_FORM_SQL, params, "identifier");
//			form.setId(id.longValue());							
//		} catch (Exception e) {
//			throw new RuntimeException("Error inserting form context", e);
//		}
//	}
//
//	@Override
//	public void insertFormRecord(Long formId, Long objectId, Long recordId, Long userId) {
//		List<Object> params = new ArrayList<Object>();
//		params.add(formId);
//		params.add(objectId);
//		params.add(recordId);
//		params.add(userId);
//		
//		JdbcDaoFactory.getJdbcDao().executeUpdate(INSERT_RECORD_SQL, params);
//	}
//
//	@Override
//	public void insertFormRecord(Long containerId, Long recordId, HookingInformation recEntryInfo) {
//		Map<String,Object> recIntegrationInfo = recEntryInfo.getDataHookingInformation();
//
//		Long objectId = getObjectId(recIntegrationInfo);
//		Long formId = getFormId(containerId, recIntegrationInfo);
//
//		insertFormRecord(formId, objectId, recordId, recEntryInfo.getSessionDataBean().getUserId());
//	
//	}
//	
//	@Override
//	public void updateFormRecord(Long formId, Long objectId, Long recordId, Long userId) {
//		List<Object> params = new ArrayList<Object>();
//		params.add(userId);
//		params.add(formId);
//		params.add(objectId);
//		params.add(recordId);
//				
//		JdbcDaoFactory.getJdbcDao().executeUpdate(UPDATE_RECORD_SQL, params);
//	}
//
//	@Override
//	public Long getCpIdByScgId(Long scgId) {
//		return JdbcDaoFactory.getJdbcDao().getResultSet(
//				GET_CP_ID_BY_SCG_SQL, Collections.singletonList(scgId), getLongExtractor()); 
//	}
//	
//		
//	@Override
//	public Long getCpIdBySpecimenId(Long specimenId) {
//		return JdbcDaoFactory.getJdbcDao().getResultSet(
//				GET_CP_ID_BY_SPECIMEN_ID_SQL, Collections.singletonList(specimenId), getLongExtractor()); 
//	}
//	
//	@Override
//	public Long getCpIdByRegistrationId(Long cprId) {
//		return JdbcDaoFactory.getJdbcDao().getResultSet(
//				GET_CP_ID_BY_REGISTRATION_ID_SQL, Collections.singletonList(cprId), getLongExtractor()); 
//	}
//	
//	private ResultExtractor<Long> getLongExtractor() {
//		return new ResultExtractor<Long>() {
//			@Override
//			public Long extract(ResultSet rs) throws SQLException {
//				return rs.next() ? rs.getLong(1) : null;
//			}			
//		};
//	}
//
//	
//	private ResultExtractor<List<FormDetailsDTO>> getFormDetailsDTOExtractor() {
//		return new ResultExtractor<List<FormDetailsDTO>>() {
//			@Override
//			public List<FormDetailsDTO> extract(ResultSet rs) throws SQLException {
//				List<FormDetailsDTO> formDtos = new ArrayList<FormDetailsDTO>();
//
//				while (rs.next()) {
//					FormDetailsDTO formDto = new FormDetailsDTO();
//					formDto.setId(rs.getLong(1));
//					formDto.setName(rs.getString(2));
//					formDto.setCaption(rs.getString(3));
//					formDto.setNoOfRecords(rs.getLong(4));
//					
//					formDtos.add(formDto);							
//				}
//				
//				return formDtos;
//			}
//		};
//	}
//	
//	private ResultExtractor<List<FormRecordDetailsDTO>> getFormRecordsDTOExtractor() {
//		return new ResultExtractor<List<FormRecordDetailsDTO>>() {
//			@Override
//			public List<FormRecordDetailsDTO> extract(ResultSet rs) throws SQLException {
//				List<FormRecordDetailsDTO> recsDto = new ArrayList<FormRecordDetailsDTO>();
//				
//				while (rs.next()) {
//					FormRecordDetailsDTO recDto = new FormRecordDetailsDTO();
//					recDto.setRecordId(rs.getLong(1));					
//					recDto.setUpdateTime(rs.getDate(2));
//					recDto.setUpdatedBy(rs.getString(3));
//					
//					recsDto.add(recDto);
//				}
//				
//				return recsDto;
//			}			
//		};
//	}
//	
//	private Long getObjectId(Map<String, Object> dataHookingInformation) {
//		JdbcDao jdbcDao = JdbcDaoFactory.getJdbcDao();
//		
//		Long objectId = null;
//		if (dataHookingInformation.get("entityType").equals("Participant") ) {
//			objectId = getObjectIdForParticipant(jdbcDao, dataHookingInformation);
//		} else if (dataHookingInformation.get("entityType").equals("Specimen") ) {
//			objectId = getObjectIdForSpecimen(jdbcDao, dataHookingInformation);
//		} else {
//			objectId = getObjectIdForSCG(jdbcDao, dataHookingInformation);
//		}
//		
//		return objectId;
//	}
//
//	private Long getFormId(Long containerId, Map<String, Object> dataHookingInformation) {
//		JdbcDao jdbcDao = JdbcDaoFactory.getJdbcDao();
//		List<Object> params = new ArrayList<Object>();
//		
//		params.add(containerId);
//		params.add(dataHookingInformation.get("entityType"));
//		
//		return jdbcDao.getResultSet(GET_FORM_CTX_ID_SQL, params, new ResultExtractor<Long>() {
//			@Override
//			public Long extract(ResultSet rs) throws SQLException {
//				return rs.next() ? rs.getLong(1) : null;
//			}
//		});
//	}
//
//	private Long getObjectIdForParticipant(	JdbcDao jdbcDao, Map<String, Object> dataHookingInformation) {
//		List<Object> params = new ArrayList<Object>();
//		String cpTitle = (String) dataHookingInformation.get("collectionProtocol");
//		String ppi = (String) dataHookingInformation.get("ppi");
//		
//		params.add(ppi);
//		params.add(cpTitle);
//		
//		return jdbcDao.getResultSet(GET_PARTICIPANT_OBJ_ID_SQL, params, new ResultExtractor<Long>() {
//			@Override
//			public Long extract(ResultSet rs) throws SQLException {
//				return rs.next() ? rs.getLong(1) : null;
//			}
//		});
//	}
//
//	private Long getObjectIdForSpecimen(JdbcDao jdbcDao,
//			Map<String, Object> dataHookingInformation) {
//		String specimenId = (String) dataHookingInformation.get("specimenId");
//		String specimenLabel = (String) dataHookingInformation.get("specimenLabel");
//		String specimenBarcode = (String) dataHookingInformation.get("specimenBarcode");
//		
//		List<Object> params = new ArrayList<Object>();
//		params.add(specimenId != null ? Long.parseLong(specimenId) : null);
//		params.add(specimenLabel);
//		params.add(specimenBarcode);
//		
//		
//		return jdbcDao.getResultSet(GET_SPECIMEN_OBJ_ID_SQL, params, new ResultExtractor<Long>() {
//			@Override
//			public Long extract(ResultSet rs) throws SQLException {
//				return rs.next() ? rs.getLong(1) : null;
//			}
//		});	
//	}
//	
//	private Long getObjectIdForSCG(JdbcDao jdbcDao,
//			Map<String, Object> dataHookingInformation) {
//		String scgId = (String) dataHookingInformation.get("scgId");
//		String scgName = (String) dataHookingInformation.get("scgName");
//		String scgBarcode = (String) dataHookingInformation.get("scgBarcode");
//		
//		List<Object> params = new ArrayList<Object>();
//		params.add(scgId != null ? Long.parseLong(scgId) : null);
//		params.add(scgName);
//		params.add(scgBarcode);
//		
//		return jdbcDao.getResultSet(GET_SCG_OBJ_ID_SQL, params, new ResultExtractor<Long>() {
//			@Override
//			public Long extract(ResultSet rs) throws SQLException {
//				return rs.next() ? rs.getLong(1) : null;
//			}
//		});	
//	}
//	
//	private static final String INSERT_FORM_SQL =
//			"insert into catissue_form_context values(default, ?, ?, ?)";
//	
//	private static final String INSERT_RECORD_SQL = 
//			"insert into catissue_form_record_entry values(default, ?, ?, ?, ?, current_timestamp())";
//	
//	private static final String UPDATE_RECORD_SQL =
//			"update " +
//			"catissue_form_record_entry " +					
//			"  set update_time = current_timestamp(), updated_by = ? " +
//			"where " +
//			"  form_ctxt_id = ? and object_id = ? and record_id = ?";
//	
//	
//	private static final String GET_CONTAINER_ID_SQL = 
//			"select " +
//			"  ctxt.container_id " +
//			"from " +
//			"  catissue_form_context ctxt " +
//			"where " +
//			"  ctxt.identifier = ?";
//			
//	
//	private static final String GET_FORMS_SQL =
//			"select " +
//			"  ctxt.identifier, c.name, c.caption, count(re.identifier) " +
//			"from " +
//			"  catissue_form_context ctxt " +
//			"  inner join dyextn_containers c on ctxt.container_id = c.identifier " +
//			"  left join catissue_form_record_entry re on re.form_ctxt_id = ctxt.identifier and re.object_id = ? " +
//			"where " +
//			"  ctxt.entity_type = ? and " +
//			"  (ctxt.cp_id = -1 or ctxt.cp_id = ?) " +
//			"group by " +
//			"  ctxt.identifier, c.name, c.caption";
//	
//	private static final String GET_FORM_BY_CONTAINER_SQL = 
//			"select " +
//			"  ctxt.identifier, c.name, c.caption, c.identifier, ctxt.entity_type " +
//			"from " +
//			"  catissue_form_context ctxt " +
//			"  inner join dyextn_containers c on ctxt.container_id = c.identifier " +
//			"where " +
//			"  ctxt.entity_type = ? and " +
//			"  (ctxt.cp_id = -1 or ctxt.cp_id = ?) and " +
//			"  c.identifier = ?"; 
//	
//	private static final String GET_FORM_RECORDS_SQL =
//			"select " +
//			"  re.record_id, re.update_time, re.updated_by " +
//			"from " +
//			"  catissue_form_record_entry re " +
//			"where " +
//			"  re.object_id = ? and " +
//			"  re.form_ctxt_id = ? " +
//			"order by " +
//			"  re.update_time";
//	
//	private static final String GET_CP_ID_BY_SCG_SQL = 
//			"select " +
//			"  event.collection_protocol_id " +
//			"from " +
//			"  catissue_specimen_coll_group scg " +
//			"  inner join catissue_coll_prot_event event on event.identifier = scg.collection_protocol_event_id " +
//			"where " +
//			"  scg.identifier = ?";	
//		
//	private static final String GET_CP_ID_BY_SPECIMEN_ID_SQL =
//			"select " +
//			"  cpe.collection_protocol_id " +
//			"from " +
//			"  catissue_specimen specimen " +
//			"  inner join catissue_specimen_coll_group scg on scg.identifier = specimen.specimen_collection_group_id " +
//			"  inner join catissue_coll_prot_event cpe on cpe.identifier = scg.collection_protocol_event_id " +
//			"where " +
//			"  specimen.identifier = ?";
//	
//	private static final String GET_CP_ID_BY_REGISTRATION_ID_SQL =
//			"select " +
//			"  cpr.collection_protocol_id " +
//			"from " +
//			"  catissue_coll_prot_reg cpr " +
//			"where " +
//			"  cpr.identifier = ?";
//
//	private static final String GET_PARTICIPANT_OBJ_ID_SQL = 
//			" select reg.identifier " +
//			" 	from catissue_coll_prot_reg reg " + 
//			" inner join catissue_specimen_protocol cp on cp.identifier = reg.collection_protocol_id " +
//			" 	where reg.protocol_participant_id = ? and cp.title = ? ";
//	
//	private static final String GET_SPECIMEN_OBJ_ID_SQL = 
//			" select sp.identifier " +
//			" 	from catissue_coll_prot_reg reg " + 
//			" inner join catissue_specimen_coll_group scg on scg.collection_protocol_reg_id = reg.identifier " + 
//			" inner join catissue_specimen sp on sp.specimen_collection_group_id = scg.identifier " +
//			"	where sp.identifier = ? or sp.label = ? or sp.barcode = ? "; 
//	
//	private static final String GET_SCG_OBJ_ID_SQL = 
//			" select scg.identifier " +
//			" 	from catissue_coll_prot_reg reg " + 
//			" inner join catissue_specimen_coll_group scg on scg.collection_protocol_reg_id = reg.identifier " + 
//			"	where scg.identifier = ? or scg.name = ? or scg.barcode = ? "; 
//	
//	private static final String GET_FORM_CTX_ID_SQL = 
//			" select identifier " +
//			"	from catissue_form_context " +
//			" where container_id = ? and entity_type = ? ";
//
//	
//}
