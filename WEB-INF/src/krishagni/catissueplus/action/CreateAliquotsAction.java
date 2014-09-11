
package krishagni.catissueplus.action;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import krishagni.catissueplus.bizlogic.AliquotBizLogic;
import krishagni.catissueplus.dto.AliquotDetailsDTO;
import krishagni.catissueplus.dto.SingleAliquotDetailsDTO;
import krishagni.catissueplus.util.CatissuePlusCommonUtil;
import edu.wustl.catissuecore.dto.OrderGrid;
import edu.wustl.catissuecore.dto.OrderItemSubmissionDTO;
import edu.wustl.catissuecore.dto.OrderSubmissionDTO;
import edu.wustl.catissuecore.dto.RowDTO;
import edu.wustl.catissuecore.util.PrintUtil;
import edu.wustl.catissuecore.util.XmlRulesModule;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.velocity.VelocityManager;
import edu.wustl.dao.HibernateDAO;

public class CreateAliquotsAction extends BaseAction
{

    public static final String CREATION_DATE_MESSAGE = "Aliquot creation date is invalid.";
    public static final String PARSE_ERROR = "Error occured while parsing aliquot details.";
    public static final String AlIQUOT_QUANTITY = "Please enter valid aliquot quantity.";

    /**
     * Overrides the executeSecureAction method of SecureAction class.
     * @param mapping
     *            object of ActionMapping
     * @param form
     *            object of ActionForm
     * @param request
     *            object of HttpServletRequest
     * @param response
     *            object of HttpServletResponse
     * @throws Exception
     *             generic exception
     * @return ActionForward : ActionForward
     */
    @Override
    public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {

        HibernateDAO hibernateDao = null;
        JSONObject returnJsonObject = new JSONObject();
        try
        {
            String dataJSON = request.getParameter("dataJSON");
            Gson gson = CatissuePlusCommonUtil.getGson();
            JSONObject jsonObject = new JSONObject(dataJSON);

            SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
                    Constants.SESSION_DATA);
            hibernateDao = (HibernateDAO) AppUtility.openDAOSession((SessionDataBean) request.getSession()
                    .getAttribute(Constants.SESSION_DATA));

            AliquotDetailsDTO aliquotDetailsDTO = gson.fromJson(dataJSON, AliquotDetailsDTO.class);
            List<SingleAliquotDetailsDTO> singleAliquotDetailsDtoList = getSingleAliquotDetailList(jsonObject.get(
                    "aliquotsXml").toString());
            aliquotDetailsDTO.setPerAliquotDetailsCollection(singleAliquotDetailsDtoList);
            final AliquotBizLogic aliquotBizLogic = new AliquotBizLogic();
            aliquotBizLogic.createAliquotSpecimen(aliquotDetailsDTO, hibernateDao, sessionDataBean);
            hibernateDao.commit();
            if (aliquotDetailsDTO.isPrintLabel())
            {
                for (int i = 0; i < aliquotDetailsDTO.getPerAliquotDetailsCollection().size(); i++)
                {
                    PrintUtil.printSpecimenLabel(" ", " ", sessionDataBean, aliquotDetailsDTO
                            .getPerAliquotDetailsCollection().get(i).getAliquotId());

                }
            }
            String aliquotsInfoXmlString = VelocityManager.getInstance().evaluate(
                    aliquotDetailsDTO.getPerAliquotDetailsCollection(), "aliquotGridTemplate.vm");
            returnJsonObject.put("success", true);
            returnJsonObject.put("msg", Constants.ALIQUOTS_CREATION_SUCCESS_MSG);
            returnJsonObject.put("parentSpecimenId", aliquotDetailsDTO.getParentId());
            returnJsonObject.put("currentAvailableQuantity", aliquotDetailsDTO.getCurrentAvailableQuantity());
            returnJsonObject.put("unit",
                    AppUtility.getUnit(aliquotDetailsDTO.getSpecimenClass(), aliquotDetailsDTO.getType()));
            returnJsonObject.put(Constants.SUCCESS, "true");
            returnJsonObject.put("aliquotGridXml", aliquotsInfoXmlString.trim().replaceAll("\n", ""));

        }
        catch (NumberFormatException ex)
        {
            returnJsonObject.put("success", false);
            returnJsonObject.put("msg", AlIQUOT_QUANTITY);

        }
        catch (final ApplicationException exp)
        {
            String msgString;
            if (exp.getErrorKey() != null)
            {
                msgString = exp.getErrorKey().getMessageWithValues();
            }
            else if (exp.getWrapException() != null)
            {
                msgString = exp.getWrapException().getMessage();
            }
            else
            {
                msgString = exp.getMsgValues();
            }

            returnJsonObject.put("success", false);
            returnJsonObject.put("msg", msgString);

        }
        catch (JsonParseException ex)
        {
            returnJsonObject.put("success", false);
            if (ex.getMessage().equals("date"))
            {
                returnJsonObject.put("msg", CREATION_DATE_MESSAGE);
            }
            else
            {
                returnJsonObject.put("msg", PARSE_ERROR);
            }

        }

        finally
        {
            AppUtility.closeDAOSession(hibernateDao);
        }
        response.getWriter().write(returnJsonObject.toString());
        return null;

    }

    public static List<SingleAliquotDetailsDTO> getSingleAliquotDetailList(String aliquotItemXMLString)
            throws IOException, SAXException
    {
        final String rulesFileLocation = Constants.DIGESTER_RULES_XML;
        DigesterLoader digesterLoader = DigesterLoader.newLoader(new XmlRulesModule(rulesFileLocation));
        Digester digester = digesterLoader.newDigester();
        OrderGrid aliquotIGrid = digester.parse(new StringReader(aliquotItemXMLString));
        List<RowDTO> rowDTOs = new ArrayList<RowDTO>(aliquotIGrid.getRowDTOs());
        Collections.sort(rowDTOs);
        List<SingleAliquotDetailsDTO> singleAliquotDetailsDTOList = new ArrayList<SingleAliquotDetailsDTO>();
        for (int i = 0; i < rowDTOs.size(); i++)
        {
            RowDTO rowDTO = rowDTOs.get(i);
            List<String> cells = rowDTO.getCells();
            SingleAliquotDetailsDTO singleAliquotDetailsDTO = new SingleAliquotDetailsDTO();
            singleAliquotDetailsDTO.setAliqoutLabel(cells.get(0));
            singleAliquotDetailsDTO.setBarCode(cells.get(1));
            singleAliquotDetailsDTO.setQuantity(Double.parseDouble(cells.get(2)));
            singleAliquotDetailsDTO.setStoragecontainer(cells.get(3));
            singleAliquotDetailsDTO.setPos1(cells.get(4));
            singleAliquotDetailsDTO.setPos2(cells.get(5));
            singleAliquotDetailsDTOList.add(singleAliquotDetailsDTO);
        }

        return singleAliquotDetailsDTOList;
    }

}
