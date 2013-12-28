package krishagni.catissueplus.bizlogic.impl;

import java.util.List;

import krishagni.catissueplus.bizlogic.CollectionProtocolService;
import krishagni.catissueplus.dao.CollectionProtocolDao;
import krishagni.catissueplus.dao.impl.CollectionProtocolDaoImpl;
import krishagni.catissueplus.dto.CollectionProtocolDTO;

public class CollectionProtocolServiceImpl implements CollectionProtocolService {
	private CollectionProtocolDao cpDao = new CollectionProtocolDaoImpl();

	@Override
	public List<CollectionProtocolDTO> getCollectionProtocols() {
		return cpDao.getCollectionProtocols();
	}
}
