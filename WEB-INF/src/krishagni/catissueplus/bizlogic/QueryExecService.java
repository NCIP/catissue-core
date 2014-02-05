package krishagni.catissueplus.bizlogic;

import krishagni.catissueplus.dto.QueryExecReq;
import krishagni.catissueplus.dto.QueryExecResp;

public interface QueryExecService {
	public QueryExecResp execute(QueryExecReq req);
}
