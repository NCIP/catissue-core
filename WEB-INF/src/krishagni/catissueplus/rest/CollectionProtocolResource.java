package krishagni.catissueplus.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.common.dynamicextensions.ndao.TransactionManager;
import edu.common.dynamicextensions.ndao.TransactionManager.Transaction;

import krishagni.catissueplus.bizlogic.CollectionProtocolService;
import krishagni.catissueplus.bizlogic.impl.CollectionProtocolServiceImpl;
import krishagni.catissueplus.dto.CollectionProtocolDTO;
import krishagni.catissueplus.dto.FormDetailsDTO;


@Path("collection-protocols")
public class CollectionProtocolResource {
	
	private CollectionProtocolService cpSvc = new CollectionProtocolServiceImpl();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCollectionProtocols() {
		TransactionManager txnMgr = TransactionManager.getInstance();
		Transaction txn = null;

		try {
			txn = txnMgr.startTxn();
			List<CollectionProtocolDTO> cps = cpSvc.getCollectionProtocols();		
			return Response.ok(cps).build();					
		} catch (Exception e) {
			return Response.serverError().build();
		} finally {
			if (txn != null) {
				txnMgr.rollback(txn);
			}
		}		
	}
	
	@Path("{id}/forms")
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	public Response getCollectionProtocolForms(@PathParam("id") Long cpId) {
		TransactionManager txnMgr = TransactionManager.getInstance();
		Transaction txn = null;

		try {
			txn = txnMgr.startTxn();
			List<FormDetailsDTO> forms = cpSvc.getCollectionProtocolForms(cpId);		
			return Response.ok(forms).build();					
		} catch (Exception e) {
			return Response.serverError().build();
		} finally {
			if (txn != null) {
				txnMgr.rollback(txn);
			}
		}				
	}
	
	@Path("{id}/query-forms")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCollectionProtocolQueryForms(@PathParam("id") Long cpId) {
		TransactionManager txnMgr = TransactionManager.getInstance();
		Transaction txn = null;

		try {
			txn = txnMgr.startTxn();
			List<FormDetailsDTO> forms = cpSvc.getQueryForms(cpId);		
			return Response.ok(forms).build();					
		} catch (Exception e) {
			return Response.serverError().build();
		} finally {
			if (txn != null) {
				txnMgr.rollback(txn);
			}
		}		
	}
}