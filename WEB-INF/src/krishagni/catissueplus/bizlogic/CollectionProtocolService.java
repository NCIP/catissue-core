package krishagni.catissueplus.bizlogic;

import java.util.List;

import krishagni.catissueplus.dto.CollectionProtocolDTO;
import krishagni.catissueplus.dto.FormDetailsDTO;

public interface CollectionProtocolService {
	List<CollectionProtocolDTO> getCollectionProtocols();
	
	List<FormDetailsDTO> getCollectionProtocolForms(Long cpId);
	
	List<FormDetailsDTO> getQueryForms(Long cpId);

}
