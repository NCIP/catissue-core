package edu.wustl.catissuecore.util;

import java.util.List;

import org.apache.struts.action.ActionForward;

import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.ClinPortalIntegrationConstants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exception.PasswordEncryptionException;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.wustlkey.util.global.WUSTLKeyUtility;


public class ClinPortalCaTissueIntegrationUtil
{

    /**
     *
     * @param scgId
     * @return
     * @throws BizLogicException
     */
    public static List getCPBasedViewInfo(String scgId) throws BizLogicException
    {
        StringBuilder hql = new StringBuilder();
        hql.append("Select scg.collectionProtocolEvent.collectionProtocol.id,scg.collectionProtocolEvent.id,");
        hql.append(" scg.collectionProtocolEvent.collectionProtocolRegistration.participant.id from ");
        hql.append(SpecimenCollectionGroup.class.getName()).append(" as scg");
        hql.append(" where scg.id=").append(scgId);
        SpecimenCollectionGroupBizLogic scgBizLogic = new SpecimenCollectionGroupBizLogic();
       return scgBizLogic.executeQuery(hql.toString());
    }

    /**
     *
     * @return
     */
    public static ActionForward cpBasedViewActionFwd()
    {
        ActionForward newActionForward = new ActionForward();
        newActionForward.setName(ClinPortalIntegrationConstants.CP_BASED_VIEW);
        newActionForward.setRedirect(false);
        newActionForward.setContextRelative(false);
        return newActionForward;
    }

    /**
     *
     * @param parameterName
     * @param value
     * @return
     */
    public static String formReqParameter(String parameterName,String value)
    {
            StringBuffer str= new StringBuffer();
            str.append(ClinPortalIntegrationConstants.DELIMITER).append(parameterName).append(ClinPortalIntegrationConstants.EQUALS).append(value);
            return str.toString();
    }

    /**
     *
     * @param loginName
     * @return
     * @throws BizLogicException
     */
    public static String getPassword(String loginName) throws BizLogicException
    {
        String password = "";
        try
        {
            String queryStr = "SELECT PASSWORD FROM CSM_USER WHERE login_name='"
                    + loginName + "'";
            List pwdList = WUSTLKeyUtility.executeQueryUsingDataSource(queryStr, false,
                    edu.wustl.wustlkey.util.global.Constants.APPLICATION_NAME);
            if (pwdList != null && !pwdList.isEmpty())
            {
                pwdList=(List)pwdList.get(0);
                password = pwdList.get(0).toString();
                password = PasswordManager.decrypt(password);
            }
        }
        catch (ApplicationException e)
        {
            throw new BizLogicException(ErrorKey.getErrorKey("db.insert.data.error"), e,
                    "Error in database operation");
        }
        catch (PasswordEncryptionException e)
        {
            throw new BizLogicException(ErrorKey.getErrorKey("common.errors.item"), e,
                    "Failed while updating ");
        }

        return password;
    }
}
