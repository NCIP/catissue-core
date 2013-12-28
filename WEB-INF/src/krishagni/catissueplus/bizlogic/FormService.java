package krishagni.catissueplus.bizlogic;

import java.util.List;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.napi.FormData;

import krishagni.catissueplus.dto.FormDetailsDTO;
import krishagni.catissueplus.dto.FormRecordDetailsDTO;

public interface FormService {
	Container getFormDefinition(Long formId);
			
	FormData getFormData(Long formId, Long recordId);
	
	List<FormDetailsDTO> getForms(String entity, Long objectId);
	
	List<FormDetailsDTO> getForms(Long cpId, String entity, Long objectId);
	
	List<FormRecordDetailsDTO> getFormRecords(Long formId, Long objectId);
	
	Long saveFormData(Long formId, Long recordId, String formDataJson);
	
	Long saveForm(FormDetailsDTO form);
}
