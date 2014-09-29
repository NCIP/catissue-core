
package krishagni.catissueplus.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

import krishagni.catissueplus.Exception.CatissueException;
import krishagni.catissueplus.Exception.SpecimenErrorCodeEnum;
import krishagni.catissueplus.dto.DerivedDTO;
import krishagni.catissueplus.dto.SpecimenDTO;
import krishagni.catissueplus.handler.AliquotHandler;
import krishagni.catissueplus.handler.SpecimenHandler;

import com.google.gson.Gson;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;

@Path("/specimens")
public class SpecimenResource
{

    @Context
    private HttpServletRequest httpServletRequest;

    private static final Logger LOGGER = Logger.getCommonLogger(SpecimenResource.class);

    @Path("/{id}/derivatives")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createDerive(@PathParam("id") Long id, String derivedDetails)
    {
        Gson gson = AppUtility.initGSONBuilder().create();
        Response response = null;
        try
        {
            DerivedDTO derivedDTO = gson.fromJson(derivedDetails, DerivedDTO.class);
            SpecimenHandler specimenHandler = new SpecimenHandler();
            SpecimenDTO specimenDTO = specimenHandler.createDerivative(derivedDTO, getSessionDataBean());
            response = Response.status(Status.CREATED.getStatusCode()).entity(specimenDTO)
                    .type(MediaType.APPLICATION_JSON).build();
        }
        catch (CatissueException e)
        {
            LOGGER.error(e);
            response = getResponse(e);
        }
        catch (Exception e)
        {
            LOGGER.error(e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
                    .header("errorMsg", e.getMessage()).type(MediaType.APPLICATION_JSON).build();
        }
        return response;
    }

    private SessionDataBean getSessionDataBean()
    {
        return (SessionDataBean) httpServletRequest.getSession().getAttribute(
                edu.wustl.catissuecore.util.global.Constants.SESSION_DATA);
    }

//    @Path("{label}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getSpecimenDetails(@PathParam("label") String label)
//    {
//        Response response = null;
//        try
//        {
//            SpecimenHandler specimenHandler = new SpecimenHandler();
//            SpecimenDTO specimenDTO = specimenHandler.getSpecimenDetails(label, getSessionDataBean());
//            response = Response.ok(specimenDTO, MediaType.APPLICATION_JSON).build();
//        }
//        catch (CatissueException e)
//        {
//            LOGGER.error(e);
//            response = getResponse(e);
//        }
//        catch (Exception e)
//        {
//            LOGGER.error(e);
//            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
//                    .header("errorMsg", e.getMessage()).type(MediaType.APPLICATION_JSON).build();
//        }
//        return response;
//    }
    
    @Path("/getDetail")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpecimenDetailsByLabel(String label)
    {
        Response response = null;
        try
        {
            SpecimenHandler specimenHandler = new SpecimenHandler();
            SpecimenDTO specimenDTO = specimenHandler.getSpecimenDetails(label, getSessionDataBean());
            response = Response.ok(specimenDTO, MediaType.APPLICATION_JSON).build();
        }
        catch (CatissueException e)
        {
            LOGGER.error(e);
            response = getResponse(e);
        }
        catch (Exception e)
        {
            LOGGER.error(e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
                    .header("errorMsg", e.getMessage()).type(MediaType.APPLICATION_JSON).build();
        }
        return response;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createSpecimen(String specimenDetails)
    {
        Response response = null;
        try
        {
            Gson gson = AppUtility.initGSONBuilder().create();
            SpecimenDTO specimenDTO = gson.fromJson(specimenDetails, SpecimenDTO.class);
            SpecimenHandler specimenHandler = new SpecimenHandler();
            specimenDTO = specimenHandler.createSpecimen(specimenDTO, getSessionDataBean());
            response = Response.status(Status.CREATED.getStatusCode()).entity(specimenDTO)
                    .type(MediaType.APPLICATION_JSON).build();
        }
        catch (CatissueException e)
        {
            LOGGER.error(e);
            response = getResponse(e);
        }
        catch (Exception e)
        {
            LOGGER.error(e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
                    .header("errorMsg", e.getMessage()).type(MediaType.APPLICATION_JSON).build();
        }
        return response;
    }
    
    @Path("{id}/TabDetails")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpecimenTabDetails(@PathParam("id") String id)
    {
        String tabtDetailJOSNString = null;
        Response response = null;
        try
        {
            SpecimenHandler specimenHandler = new SpecimenHandler();
            tabtDetailJOSNString = specimenHandler.getSpecimenTabDetails(getSessionDataBean(), id);
            response = Response.status(Status.CREATED.getStatusCode()).entity(tabtDetailJOSNString)
                    .type(MediaType.APPLICATION_JSON).build();
        }
        catch (CatissueException e)
        {
            LOGGER.error(e);
            response = getResponse(e);
        }
        catch (Exception e)
        {
            LOGGER.error(e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
                    .header("errorMsg", e.getMessage()).type(MediaType.APPLICATION_JSON).build();
        }
        return response;
    }


    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateSpecimen(String specimenDetails)
    {
        Gson gson = AppUtility.initGSONBuilder().create();
        Response response = null;
        try
        {
            SpecimenDTO specimenDTO = gson.fromJson(specimenDetails, SpecimenDTO.class);
            SpecimenHandler specimenHandler = new SpecimenHandler();
            specimenDTO = specimenHandler.updateSpecimen(specimenDTO, getSessionDataBean());

            response = Response.status(Status.CREATED.getStatusCode()).entity(specimenDTO)
                    .type(MediaType.APPLICATION_JSON).build();
        }
        catch (CatissueException e)
        {
            LOGGER.error(e);
            response = getResponse(e);
        }
        catch (Exception e)
        {
            LOGGER.error(e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
                    .header("errorMsg", e.getMessage()).type(MediaType.APPLICATION_JSON).build();
        }
        return response;
    }
    
    @Path("{id}/updateStatus")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateSpecimenStatus(@PathParam("id") String id, String specimenDetails)
    {
        Gson gson = AppUtility.initGSONBuilder().create();
        Response response = null;
        try
        {
            edu.wustl.catissuecore.dto.SpecimenDTO specimenDTO = gson.fromJson(specimenDetails, edu.wustl.catissuecore.dto.SpecimenDTO.class);
            SpecimenHandler specimenHandler = new SpecimenHandler();
            specimenDTO = specimenHandler.updateSpecimenStatus(specimenDTO, getSessionDataBean());

            response = Response.status(Status.CREATED.getStatusCode()).entity(specimenDTO)
                    .type(MediaType.APPLICATION_JSON).build();
        }
        catch (CatissueException e)
        {
            LOGGER.error(e);
            response = getResponse(e);
        }
        catch (Exception e)
        {
            LOGGER.error(e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
                    .header("errorMsg", e.getMessage()).type(MediaType.APPLICATION_JSON).build();
        }
        return response;
    }

    @Path("{id}/fetchAliquots/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAliquotsDetails(String paramJson)
    {
        String aliquotDetailJOSNString = null;
        Response response = null;
        try
        {
        	JSONObject object = new JSONObject(paramJson);
            AliquotHandler aliquotHandler = new AliquotHandler();
            aliquotDetailJOSNString = aliquotHandler.getAliquotDetails(getSessionDataBean(), object.getString("label"), paramJson);
            response = Response.status(Status.CREATED.getStatusCode()).entity(aliquotDetailJOSNString)
                    .type(MediaType.APPLICATION_JSON).build();
        }
        catch (CatissueException e)
        {
            LOGGER.error(e);
            response = getResponse(e);
        }
        catch (Exception e)
        {
            LOGGER.error(e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
                    .header("errorMsg", e.getMessage()).type(MediaType.APPLICATION_JSON).build();
        }
        return response;
    }

    @Path("{id}/aliquots")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createAliquot(String aliquotJson)
    {

        String responseString = null;
        Response response = null;
        try
        {
            AliquotHandler aliquotHandler = new AliquotHandler();
            responseString = aliquotHandler.createAliquot(getSessionDataBean(), aliquotJson);
            response = Response.status(Status.CREATED.getStatusCode()).entity(responseString)
                    .type(MediaType.APPLICATION_JSON).build();
        }
        catch (CatissueException e)
        {
            LOGGER.error(e);
            response = getResponse(e);
        }
        catch (Exception e)
        {
            LOGGER.error(e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
                    .header("errorMsg", e.getMessage()).type(MediaType.APPLICATION_JSON).build();
        }
        return response;

    }

    private Response getResponse(CatissueException e)
    {
        SpecimenErrorCodeEnum errorCodeEnum = SpecimenErrorCodeEnum.getStatus(e.getErrorCode());
        int responseCode = getCorrespondingHTTPResponse(e.getErrorCode());
        Response response = Response.status(responseCode)
                .entity(e.getErrorCode() + ":" + errorCodeEnum.getDescription())
                .header("errorMsg", errorCodeEnum.getDescription()).type(MediaType.APPLICATION_JSON).build();
        return response;
    }

    private int getCorrespondingHTTPResponse(int errorCode)
    {
        int httpCode;
        switch (errorCode)
        {
            case 1004 :
                httpCode = Status.FORBIDDEN.getStatusCode();
                break;
            case 1003 :
            case 1011 :
            case 1013 :
            case 1014 :
            case 1015 :
            case 1016 :
                httpCode = Status.NOT_FOUND.getStatusCode();
                break;

            case 1005 :
            case 1006 :
            case 1007 :
            case 1008 :
            case 1009 :
            case 1010 :
            case 1012 :
            case 1029 :
            case 1040 :
            case 1050 :
                httpCode = Status.BAD_REQUEST.getStatusCode();
                break;
            default :
                httpCode = Status.INTERNAL_SERVER_ERROR.getStatusCode();
                break;
        }

        return httpCode;
    }
}
