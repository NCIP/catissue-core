package krishagni.catissueplus.dao;

import java.util.List;

import edu.wustl.bulkoperator.metadata.HookingInformation;

import krishagni.catissueplus.dto.FormDetailsDTO;
import krishagni.catissueplus.dto.FormRecordDetailsDTO;

public interface FormDao {
	Long getContainerId(Long formId);
	
	FormDetailsDTO getFormByContainerId(Long cpId, String entity, Long containerId);
	
	List<FormDetailsDTO> getForms(Long cpId, String entity, Long objectId);
	
	List<FormRecordDetailsDTO> getFormRecords(Long formId, Long objectId);
	
	void insertFormRecord(Long formId, Long objectId, Long recordId, Long userId);
	
	void updateFormRecord(Long formId, Long objectId, Long recordId, Long userId);
	
	// These should not have been in this interface. But because of
	// data access framework difference, I'm putting this method here	
	Long getCpIdByScgId(Long scgId);
	
	Long getCpIdBySpecimenId(Long specimenId);
	
	Long getCpIdByRegistrationId(Long cprId);
	
	void insertForm(FormDetailsDTO form);

	void insertFormRecord(Long containerId, Long recordId, HookingInformation recEntryInfo);
}
