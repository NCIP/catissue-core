package krishagni.catissueplus.dao;

import java.util.List;

import krishagni.catissueplus.dto.CollectionProtocolDTO;
import krishagni.catissueplus.dto.FormDetailsDTO;

public interface CollectionProtocolDao {
	List<CollectionProtocolDTO> getCollectionProtocols();
	
	List<FormDetailsDTO> getForms(Long cpId);
	
	public List<FormDetailsDTO> getStaticQueryForms(Long cpId);
}
