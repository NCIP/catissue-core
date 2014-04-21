package krishagni.catissueplus.bizlogic.impl;

import java.util.List;

import krishagni.catissueplus.bizlogic.CollectionProtocolService;
import krishagni.catissueplus.dao.CollectionProtocolDao;
import krishagni.catissueplus.dao.impl.CollectionProtocolDaoImpl;
import krishagni.catissueplus.dto.CollectionProtocolDTO;
import krishagni.catissueplus.dto.FormDetailsDTO;

public class CollectionProtocolServiceImpl implements CollectionProtocolService {
	private CollectionProtocolDao cpDao = new CollectionProtocolDaoImpl();

	@Override
	public List<CollectionProtocolDTO> getCollectionProtocols() {
		return cpDao.getCollectionProtocols();
	}

	@Override
	public List<FormDetailsDTO> getCollectionProtocolForms(Long cpId) {
		return cpDao.getForms(cpId);
	}

	@Override
	public List<FormDetailsDTO> getQueryForms(Long cpId) {
		List<FormDetailsDTO> queryForms = cpDao.getStaticQueryForms(cpId);
//		List<FormDetailsDTO> deForms = cpDao.getForms(cpId);
//		queryForms.addAll(deForms);
		return queryForms;
	}
}
