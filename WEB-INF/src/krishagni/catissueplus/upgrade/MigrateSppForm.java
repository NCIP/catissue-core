package krishagni.catissueplus.upgrade;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import krishagni.catissueplus.dto.FormDetailsDTO;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;
import edu.emory.mathcs.backport.java.util.Collections;

public class MigrateSppForm extends MigrateForm {
	public MigrateSppForm(UserContext usrCtx) {
		super(usrCtx);
	}

	@Override
	protected List<RecordObject> getRecordAndObjectIds(Long oldCtxId) {										
		return JdbcDaoFactory.getJdbcDao().getResultSet(
				GET_RECORD_AND_SPECIMEN_IDS_SQL, 
				Collections.singletonList(oldCtxId),
				
				new ResultExtractor<List<RecordObject>>() {
					@Override
					public List<RecordObject> extract(ResultSet rs)
					throws SQLException {
						List<RecordObject> recAndObjectIds = new ArrayList<RecordObject>();

						while (rs.next()) {
							RecordObject recObj = new RecordObject();
							recObj.recordId = rs.getLong(1);
							recObj.objectId = rs.getLong(2);
							if (recObj.objectId == -1) {
								continue;
							}

							recAndObjectIds.add(recObj);
						}

						return recAndObjectIds;
					}
		});
	}

	@Override
	protected void attachForm(JdbcDao jdbcDao, Long newFormId, List<FormInfo> formInfos)
	throws Exception {
		Long formCtxId = null;
		String entityType = "SpecimenEvent";
		Long cpId = -1L;
		
		for (FormInfo info : formInfos) {			
			if (formCtxId == null) {
				FormDetailsDTO dto = new FormDetailsDTO();
				dto.setContainerId(newFormId);
				dto.setEntityType(entityType);
				dto.setCpId(cpId);
								
				insertForm(dto);
				formCtxId = dto.getId();
			} 
						
			info.setNewFormCtxId(formCtxId);
			migrateFormData(info);			
		}
	}	
	
			
	private static final String GET_RECORD_AND_SPECIMEN_IDS_SQL =
			"select " +
			"	re.identifier, spe_re.specimen_id " +
			"from " +
			"   dyextn_abstract_form_context afc " +
			"	inner join dyextn_abstract_record_entry re on re.abstract_form_context_id = afc.identifier " +
			"   inner join catissue_action_application spe_re on spe_re.action_app_record_entry_id = re.identifier " +
			"where " + 
			"   afc.identifier = ?";
	
}
