package krishagni.catissueplus.bizlogic.impl;

import edu.common.dynamicextensions.query.Query;
import edu.common.dynamicextensions.query.QueryResultData;
import krishagni.catissueplus.bizlogic.QueryExecService;
import krishagni.catissueplus.dto.QueryExecReq;
import krishagni.catissueplus.dto.QueryExecResp;

public class QueryExecServiceImpl implements QueryExecService {
	private static final String cpForm = "CollectionProtocol";
	
	private static final String cprForm = "CollectionProtocolRegistration";
	
	private static final String dateFormat = "MM/dd/yyyy";

	@Override
	public QueryExecResp execute(QueryExecReq req) {
		String restriction = null;
		Long cpId = req.getCpId();
		if (cpId != null && cpId != -1) {
			restriction = "CollectionProtocol.id = " + cpId;
		}
		 
		Query query = Query.createQuery();
		query.wideRows(req.isWideRows()).ic(true).dateFormat(dateFormat).compile(cprForm, req.getAql(), restriction);
		QueryResultData queryResult = query.getData();
		
		QueryExecResp result = new QueryExecResp();
		result.setColumnLabels(queryResult.getColumnLabels());
		result.setRows(queryResult.getRows());
		
		return result;
	}
}
