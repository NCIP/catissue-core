//package krishagni.catissueplus.upgrade;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import krishagni.catissueplus.dto.FormDetailsDTO;
//import au.com.bytecode.opencsv.CSVWriter;
//
//import com.krishagni.catissueplus.core.de.ui.UserControl;
//
//import edu.common.dynamicextensions.domain.nui.Container;
//import edu.common.dynamicextensions.domain.nui.Control;
//import edu.common.dynamicextensions.domain.nui.DatePicker;
//import edu.common.dynamicextensions.domain.nui.TextArea;
//import edu.common.dynamicextensions.domain.nui.UserContext;
//import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
//import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
//import edu.common.dynamicextensions.napi.ControlValue;
//import edu.common.dynamicextensions.napi.FormData;
//import edu.common.dynamicextensions.ndao.JdbcDao;
//import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
//import edu.common.dynamicextensions.ndao.ResultExtractor;
//import edu.emory.mathcs.backport.java.util.Collections;
//
//public class MigrateSppForm extends MigrateForm {
//	public MigrateSppForm(UserContext usrCtx) {
//		super(usrCtx);
//	}
//	
//	public MigrateSppForm(UserContext usrCtx, CSVWriter recordsLog) {
//		super(usrCtx, recordsLog);
//	}
//	
//	@Override
//	protected FormMigrationCtxt getNewFormDefinition(ContainerInterface oldForm) 
//	throws Exception {
//		FormMigrationCtxt formMigrationCtxt = super.getNewFormDefinition(oldForm);
//		
//		Container newForm = formMigrationCtxt.newForm;
//		addCommonSppFields(newForm);
//		newForm.setCaption(newForm.getCaption() + " - SPP");
//		return formMigrationCtxt;
//	}
//	
//	@Override
//	protected FormData getFormData(
//			JdbcDao jdbcDao, RecordObject recObj, 
//			FormMigrationCtxt formMigrationCtxt, ContainerInterface oldForm,
//			Map<BaseAbstractAttributeInterface, Object> oldFormData) {
//		FormData formData = super.getFormData(jdbcDao, recObj, formMigrationCtxt, oldForm, oldFormData);
//		
//		Map<String, Object> fieldValues = jdbcDao.getResultSet(
//				GET_SPP_COMMON_FIELDS_SQL, 
//				Collections.singletonList(recObj.recordId), 
//				new ResultExtractor<Map<String, Object>>() {
//
//					@Override
//					public Map<String, Object> extract(ResultSet rs) throws SQLException {
//						Map<String, Object> fields = new HashMap<String, Object>();
//						
//						if (!rs.next()) {
//							return fields;
//						}
//												
//						fields.put(DEVIATION, rs.getString(1));
//						fields.put(DATETIME, rs.getTimestamp(2));
//						fields.put(USER, rs.getLong(3));
//						fields.put(COMMENTS, rs.getString(4));
//						return fields;
//					}
//				}
//		);
//		
//		addCommonSppFieldValues(formData, fieldValues);
//		return formData;
//	}
//
//	@Override
//	protected List<RecordObject> getRecordAndObjectIds(Long oldCtxId) {										
//		return JdbcDaoFactory.getJdbcDao().getResultSet(
//				GET_RECORD_AND_SPECIMEN_IDS_SQL, 
//				Collections.singletonList(oldCtxId),
//				
//				new ResultExtractor<List<RecordObject>>() {
//					@Override
//					public List<RecordObject> extract(ResultSet rs)
//					throws SQLException {
//						List<RecordObject> recAndObjectIds = new ArrayList<RecordObject>();
//
//						while (rs.next()) {
//							RecordObject recObj = new RecordObject();
//							recObj.recordId = rs.getLong(1);
//							recObj.objectId = rs.getLong(2);
//							if (recObj.objectId == -1) {
//								continue;
//							}
//
//							recAndObjectIds.add(recObj);
//						}
//
//						return recAndObjectIds;
//					}
//		});
//	}
//
//	@Override
//	protected void attachForm(JdbcDao jdbcDao, Long newFormId, List<FormInfo> formInfos)
//	throws Exception {
//		Long formCtxId = null;
//		String entityType = "SpecimenEvent";
//		Long cpId = -1L;
//		
//		for (FormInfo info : formInfos) {			
//			if (formCtxId == null) {
//				FormDetailsDTO dto = new FormDetailsDTO();
//				dto.setContainerId(newFormId);
//				dto.setEntityType(entityType);
//				dto.setCpId(cpId);
//								
//				insertForm(dto);
//				formCtxId = dto.getId();
//			} 
//						
//			info.setNewFormCtxId(formCtxId);
//			migrateFormData(info);			
//		}
//	}	
//	
//	private void addCommonSppFields(Container newForm) {		
//		for (Control ctrl : newForm.getControls()) {
//			ctrl.setSequenceNumber(ctrl.getSequenceNumber() + 5);
//		}
//		
//		for (Control ctrl : getCommonSppFields()) {
//			newForm.addControl(ctrl);
//		}		
//	}
//	
//	private Control[] getCommonSppFields() {
//		return new Control[] {
//				getUserField(1),
//				getDateTimeField(2),
//				getReasonForDeviationField(3),
//				getCommentsField(4)
//		};
//	}
//	
//	private Control getUserField(int rowNo) {
//		Control user = new UserControl();
//		user.setName(USER);
//		user.setUserDefinedName(USER);
//		user.setCaption("User");
//		user.setSequenceNumber(rowNo);
//		user.setxPos(0);
//		user.setMandatory(true);
//		return user;		
//	}
//	
//	private Control getDateTimeField(int rowNo) {
//		DatePicker dateTime = new DatePicker();
//		dateTime.setName(DATETIME);
//		dateTime.setUserDefinedName(DATETIME);
//		dateTime.setCaption("Date and Time");
//		dateTime.setSequenceNumber(rowNo);
//		dateTime.setxPos(0);
//		dateTime.setFormat("MM-dd-yyyy HH:mm");
//		dateTime.setMandatory(true);
//		return dateTime;		
//	}
//	
//	private Control getReasonForDeviationField(int rowNo) {
//		TextArea deviation = new TextArea();
//		deviation.setName(DEVIATION);
//		deviation.setUserDefinedName(DEVIATION);
//		deviation.setCaption("Reason for Deviation");
//		deviation.setSequenceNumber(rowNo);
//		deviation.setxPos(0);
//		deviation.setNoOfRows(2);
//		return deviation;		
//	}
//	
//	private Control getCommentsField(int rowNo) {
//		TextArea comments = new TextArea();
//		comments.setName(COMMENTS);
//		comments.setUserDefinedName(COMMENTS);
//		comments.setCaption("Comments");
//		comments.setSequenceNumber(rowNo);
//		comments.setxPos(0);
//		comments.setNoOfRows(2);
//		return comments;
//	}
//		
//	private void addCommonSppFieldValues(FormData formData, Map<String, Object> fieldValues) {
//		for (Map.Entry<String, Object> fieldValue : fieldValues.entrySet()) {
//			addCommonSppFieldValue(formData, fieldValue.getKey(), fieldValue.getValue());
//		}
//	}
//	
//	private void addCommonSppFieldValue(FormData formData, String fieldName, Object value) {
//		Control ctrl = formData.getContainer().getControl(fieldName);
//		formData.addFieldValue(new ControlValue(ctrl, ctrl.toString(value)));
//	}
//	
//	private static final String GET_RECORD_AND_SPECIMEN_IDS_SQL =
//			"select " +
//			"	re.identifier, spe_re.specimen_id " +
//			"from " +
//			"   dyextn_abstract_form_context afc " +
//			"   inner join dyextn_abstract_record_entry re on re.abstract_form_context_id = afc.identifier " +
//			"   inner join catissue_action_application spe_re on spe_re.action_app_record_entry_id = re.identifier " +
//			"where " + 
//			"   afc.identifier = ?";
//	
//	private static final String GET_SPP_COMMON_FIELDS_SQL =
//			"select " +
//			"  aa.reason_deviation, aa.timestamp, aa.user_details, aa.comments " +
//			"from " +
//			"  catissue_abstract_application aa " +
//			"  inner join catissue_action_application spe_re on spe_re.identifier = aa.identifier " +
//			"where " +
//			"  spe_re.action_app_record_entry_id = ?";
//	
//	private static final String DEVIATION = "deviationReason";
//	
//	private static final String USER = "user";
//	
//	private static final String DATETIME = "dateTime";
//	
//	private static final String COMMENTS = "comments";
//}
