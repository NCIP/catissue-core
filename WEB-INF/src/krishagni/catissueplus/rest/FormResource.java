package krishagni.catissueplus.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.google.gson.Gson;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.ndao.TransactionManager;
import edu.common.dynamicextensions.ndao.TransactionManager.Transaction;
import edu.common.dynamicextensions.nutility.ContainerJsonSerializer;
import edu.common.dynamicextensions.nutility.ContainerSerializer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

import krishagni.catissueplus.bizlogic.FormService;
import krishagni.catissueplus.bizlogic.impl.FormServiceImpl;
import krishagni.catissueplus.dto.FormDetailsDTO;
import krishagni.catissueplus.dto.FormFieldSummary;
import krishagni.catissueplus.dto.FormRecordDetailsDTO;

@Path("/forms")
public class FormResource {
	private @Context HttpServletRequest request;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getForms(
			@QueryParam("cpId") Long cpId,
			@QueryParam("entity") String entity,			
			@QueryParam("entityObjId") Long objectId) {
		
		TransactionManager txnMgr = TransactionManager.getInstance();
		Transaction txn = null;

		try {
			txn = txnMgr.startTxn();
			List<FormDetailsDTO> forms = getFormService().getForms(cpId, entity, objectId);		
			return Response.ok(forms).build();					
		} catch (Exception e) {
			return Response.serverError().build();
		} finally {
			if (txn != null) {
				txnMgr.rollback(txn);
			}
		}
	}
	
	@Path("{formId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFormDefinition(@PathParam("formId") Long formId) {
		TransactionManager txnMgr = TransactionManager.getInstance();
		Transaction txn = null;

		try {
			txn = txnMgr.startTxn();
			final Container container = getFormService().getFormDefinition(formId);
			return Response.ok(new StreamingOutput() {				
				@Override
				public void write(OutputStream out) 
				throws IOException, WebApplicationException {
					ContainerSerializer serializer = new ContainerJsonSerializer(container, out);
					serializer.serialize();
					out.flush();					
				}
			}).build();					
		} catch (Exception e) {
			return Response.serverError().build();
		} finally {
			if (txn != null) {
				txnMgr.rollback(txn);
			}
		}		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveFormContext(String formJson) {
		FormDetailsDTO formDto = new Gson().fromJson(formJson, FormDetailsDTO.class);
				
		TransactionManager txnMgr = TransactionManager.getInstance();
		Transaction txn = null;
		boolean success = false;

		try {
			txn = txnMgr.startTxn();
			Long id = getFormService().saveForm(formDto);
			success = true;
			return Response.ok(Collections.singletonMap("id", id)).build();
		} catch (Exception e) {
			return Response.serverError().build();
		} finally {
			if (txn != null) {
				if (success) {
					txnMgr.commit(txn);
				} else {
					txnMgr.rollback(txn);
				}				
			}
		}		
	}
	
	@Path("{formId}/fields")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFormRecords(@PathParam("formId") Long formId) {
		TransactionManager txnMgr = TransactionManager.getInstance();
		Transaction txn = null;

		try {
			txn = txnMgr.startTxn();
			List<FormFieldSummary> fields = getFormService().getFormFields(formId);
			return Response.ok(fields).build();
		} catch (Exception e) {
			return Response.serverError().build();
		} finally {
			if (txn != null) {
				txnMgr.rollback(txn);
			}
		}			
	}
	
	@Path("{formId}/records")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFormRecords(@PathParam("formId")Long formId, @QueryParam("entityObjId") Long objectId) {
		TransactionManager txnMgr = TransactionManager.getInstance();
		Transaction txn = null;

		try {
			txn = txnMgr.startTxn();
			List<FormRecordDetailsDTO> records = getFormService().getFormRecords(formId, objectId);
			
			Map<String, Object> resp = new HashMap<String, Object>();
			resp.put("records", records);
			resp.put("id", formId);
			return Response.ok(resp).build();					
		} catch (Exception e) {
			return Response.serverError().build();
		} finally {
			if (txn != null) {
				txnMgr.rollback(txn);
			}
		}		
	}
	
	@Path("{formId}/records/{recordId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getData(@PathParam("formId") Long formId, @PathParam("recordId") Long recordId) {
		TransactionManager txnMgr = TransactionManager.getInstance();
		Transaction txn = null;

		try {
			txn = txnMgr.startTxn();
			FormData formData = getFormService().getFormData(formId, recordId);
			return Response.ok(formData.toJson()).build();					
		} catch (Exception e) {
			return Response.serverError().build();
		} finally {
			if (txn != null) {
				txnMgr.rollback(txn);
			}
		}				
    }

	@Path("{formId}/records/")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveData(@PathParam("formId") Long formId, String formDataJson) {
		TransactionManager txnMgr = TransactionManager.getInstance();
		Transaction txn = null;
		boolean success = false;

		try {
			txn = txnMgr.startTxn();
			Long recordId = getFormService().saveFormData(formId, null, formDataJson);
			success = true;
			return Response.ok(Collections.singletonMap("id", recordId)).build();					
		} catch (Exception e) {
			return Response.serverError().build();
		} finally {
			if (txn != null) {
				if (success) {
					txnMgr.commit(txn);
				} else {
					txnMgr.rollback(txn);
				}
			}
		}						
	}
	
	@Path("{formId}/records/{recordId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateData(@PathParam("formId") Long formId, @PathParam("recordId") Long recordId, String formDataJson) {
		TransactionManager txnMgr = TransactionManager.getInstance();
		Transaction txn = null;
		boolean success = false;

		try {
			txn = txnMgr.startTxn();
			recordId = getFormService().saveFormData(formId, recordId, formDataJson);
			success = true;
			return Response.ok(Collections.singletonMap("id", recordId)).build();					
		} catch (Exception e) {
			return Response.serverError().build();
		} finally {
			if (txn != null) {
				if (success) {
					txnMgr.commit(txn);
				} else {
					txnMgr.rollback(txn);
				}				
			}
		}						
	}

	private FormService getFormService() {
		SessionDataBean session = (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		return new FormServiceImpl(session);
	}
}