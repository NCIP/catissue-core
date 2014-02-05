package krishagni.catissueplus.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse.Status;

import krishagni.catissueplus.bizlogic.QueryExecService;
import krishagni.catissueplus.bizlogic.impl.QueryExecServiceImpl;
import krishagni.catissueplus.dto.QueryExecReq;
import krishagni.catissueplus.dto.QueryExecResp;
import edu.common.dynamicextensions.ndao.TransactionManager;
import edu.common.dynamicextensions.ndao.TransactionManager.Transaction;
import edu.common.dynamicextensions.query.QueryParserException;

@Path("/query")
public class QueryResource {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeQuery(QueryExecReq queryReq) {
		TransactionManager txnMgr = TransactionManager.getInstance();
		Transaction txn = null;

		try {
			txn = txnMgr.startTxn();
			QueryExecService querySvc = new QueryExecServiceImpl();
			QueryExecResp resp = querySvc.execute(queryReq);		
			return Response.ok(resp).build();					
		} catch (QueryParserException qpe) {
			return Response.status(Status.BAD_REQUEST).entity(qpe.getMessage()).build();
		} catch (Exception e) {
			return Response.serverError().build();
		} finally {
			if (txn != null) {
				txnMgr.rollback(txn);
			}
		}				
	}
}
																																																																																																																																						