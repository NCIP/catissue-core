package edu.wustl.catissuecore.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForward;

import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.ClinPortalIntegrationConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exception.PasswordEncryptionException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.wustlkey.util.global.WUSTLKeyUtility;


public class ClinPortalCaTissueIntegrationUtil
{
    private static final  Logger LOGGER = Logger.getCommonLogger(ClinPortalCaTissueIntegrationUtil.class);

    /**
     *
     * @param scgId
     * @return
     * @throws BizLogicException
     */
    public static List getCPBasedViewInfo(final String scgId)
            throws BizLogicException
    {
        final StringBuilder hql = new StringBuilder();
        hql.append("Select scg.collectionProtocolEvent.collectionProtocol.id,scg.collectionProtocolEvent.id,");
        hql.append(" scg.collectionProtocolRegistration.participant.id from ");
        hql.append(SpecimenCollectionGroup.class.getName()).append(" as scg");
        hql.append(" where scg.id=").append(scgId);
        final SpecimenCollectionGroupBizLogic scgBizLogic = new SpecimenCollectionGroupBizLogic();
        return scgBizLogic.executeQuery(hql.toString());
    }

    /**
     *
     * @return
     */
    public static ActionForward cpBasedViewActionFwd()
    {
        final ActionForward newActionForward = new ActionForward();
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
    public static String formReqParameter(final String parameterName, final String value)
    {
        final StringBuffer str = new StringBuffer();
        str.append(ClinPortalIntegrationConstants.DELIMITER).append(parameterName).append(ClinPortalIntegrationConstants.EQUALS)
                .append(value);
        return str.toString();
    }

    /**
     *
     * @param loginName
     * @return
     * @throws BizLogicException
     */
    public static String getPassword(final String loginName) throws BizLogicException
    {
        String password = "";
        try
        {
            final String queryStr = "SELECT PASSWORD FROM CSM_USER WHERE login_name='"
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

    /**
    *
    * @param collectionEventId
    * @param cprId
    * @param completedSCG
    * @param anticipatedSCG
    * @throws BizLogicException
    */
   public void getScg(final Long collectionEventId, final Long cprId,final Map<String, Date> completedSCG,final Map<String, Date> anticipatedSCG) throws BizLogicException
   {
       final StringBuilder scgHql = new StringBuilder();
       final DefaultBizLogic bizLogic = new DefaultBizLogic();
       scgHql.append("select scg.id,scg.collectionStatus  from ").append(SpecimenCollectionGroup.class.getName())
               .append(" as scg");
       scgHql.append(" where scg.collectionProtocolEvent.id= ").append(collectionEventId);
       scgHql.append(" and scg.collectionProtocolRegistration.id=").append(cprId);
       scgHql.append(" and scg.activityStatus='").append(Constants.ACTIVITY_STATUS_VALUES[1]).append("'");
       scgHql.append(" order by scg.id");
       final List scgColl = bizLogic.executeQuery(scgHql.toString());
       StringBuffer eventQuery = new StringBuffer();
       final Iterator iteratorSCG = scgColl.iterator();
        while (iteratorSCG.hasNext())
        {
            final Object[] scgDetails = (Object[]) iteratorSCG.next();
            eventQuery = new StringBuffer();
            eventQuery.append("select timestamp from ").append(CollectionEventParameters.class.getName());
            eventQuery.append(" where specimenCollectionGroup.id=").append(scgDetails[0].toString());
            List dateList = bizLogic.executeQuery(eventQuery.toString());
            Date date = null;
            if (!dateList.isEmpty())
            {
                final String receivedDate = edu.wustl.common.util.Utility.parseDateToString((Date) dateList.get(0),
                                CommonServiceLocator.getInstance().getDatePattern());
                date = getDateFromString(receivedDate);
            }
            if (!scgDetails[1].toString().contains(Constants.COLLECTION_STATUS_PENDING))
            {
                //complete scg
                completedSCG.put(scgDetails[0].toString(), date);
            }
            else
            {
                anticipatedSCG.put(scgDetails[0].toString(), date);
            }
        }
   }

   /***
    *
    * @param scgMap
    * @param visitCurrDate
    * @param visitPrevDate
    * @return
    */
    public String getClosestDate(final Map<String, Date> scgMap,final Date visitCurrDate,final Date visitPrevDate)
    {
        boolean flag = true;
        final Object[] keys = scgMap.keySet().toArray();
        final int size = keys.length;
        final ArrayList<String> keyList = new ArrayList<String>();
        for (int i = 0; i < size; i++)
        {
            keyList.add((String) keys[i]);
        }
        int i = 0;
        final Iterator<Date> dateIterator = scgMap.values().iterator();
        long diff1 = 0;
        long diff2 = 0;
        String scgId = keyList.get(0);
        Date receivedSCGDate = null;
        while (dateIterator.hasNext())
        {
            receivedSCGDate = (Date) dateIterator.next();
            if (receivedSCGDate != null)
            {
                diff2 = visitCurrDate.getTime() - receivedSCGDate.getTime();
                diff2=getPosititveValue(diff2);
                if (flag)
                {
                    flag = false;
                    diff1 = diff2;
                }
                if (diff1 >= diff2)
                {
                    diff1 = diff2;
                    scgId = keyList.get(i);
                }
            }
            i++;
        }
        if (visitPrevDate != null && receivedSCGDate != null)
        {
            diff2 = visitPrevDate.getTime() - receivedSCGDate.getTime();
            diff2=getPosititveValue(diff2);
            if (diff1 > diff2)
            { //that means recived SCgDate i smore close to prev Visit so to create new SCG
                scgId = null;
            }
        }
        return scgId;
    }

    /**
     *
     * @param num
     * @return
     */
    private long getPosititveValue(final long num)
    {
        long positiveNum=num;
        if (num < 0)
        {
            positiveNum = 0 - num;
        }
        return positiveNum;
    }

   /**
    *
    * @param date
    * @return
    */
   private Date getDateFromString(final String date)
   {
       final String pattern=CommonServiceLocator.getInstance().getDatePattern();
       final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern,CommonServiceLocator.getInstance().getDefaultLocale());
       Date toCheck=null;
       try
       {
           toCheck = dateFormat.parse(date);
       }
       catch (ParseException e)
       {
           LOGGER.error(e.getMessage());
       }
       return toCheck;
   }

   /**
    *
    * @param map
    * @return
    */
   public Object getSCGRelatedEncounteredDate(final Long scgId)
   {
       StringBuffer hql= new StringBuffer();
       hql.append("select ");

       return null;
   }


}
