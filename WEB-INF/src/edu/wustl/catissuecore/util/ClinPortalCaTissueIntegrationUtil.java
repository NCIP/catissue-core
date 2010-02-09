package edu.wustl.catissuecore.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForward;

import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
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
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;
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
       final StringBuffer eventQuery = new StringBuffer();
       final Iterator iteratorSCG = scgColl.iterator();
        while (iteratorSCG.hasNext())
        {
            final Object[] scgDetails = (Object[]) iteratorSCG.next();
            Date date = null;
            date=getCEPDate(scgDetails[0].toString());
            if (date!=null)
            {
                final String receivedDate = edu.wustl.common.util.Utility.parseDateToString(date,
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
        String scgId = null;
        if(size>0)
        {
            scgId=keyList.get(0);
        }
        Date receivedSCGDate = null;
        while (dateIterator.hasNext())
        {
            receivedSCGDate = (Date) dateIterator.next();
            if (receivedSCGDate != null && visitCurrDate!=null)
            {
                receivedSCGDate.setHours(0);
                receivedSCGDate.setMinutes(0);
                receivedSCGDate.setSeconds(0);
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
* @throws BizLogicException
   */
  public Map getSCGRelatedEncounteredDate(final Long cpId,final Long pId,final Long cpeId,final Long scgId,Date currDate,Date prevDate) throws BizLogicException
  {
      final StringBuilder scgHql = new StringBuilder();
      Map map = new HashMap();
      final DefaultBizLogic bizLogic = new DefaultBizLogic();
      Long cprId=getCPRId(pId, cpId);
      scgHql.append("select scg.id from ").append(SpecimenCollectionGroup.class.getName()).append(" as scg");
      scgHql.append(" where scg.collectionProtocolEvent.id= ").append(cpeId);
      scgHql.append(" and scg.collectionProtocolRegistration.id=").append(cprId);
      scgHql.append(" and scg.activityStatus='").append(Constants.ACTIVITY_STATUS_VALUES[1]).append("'");
      scgHql.append(" and scg.collectionStatus='").append(Constants.COMPLETE).append("'");
      scgHql.append(" order by scg.id");
      final List scgColl = bizLogic.executeQuery(scgHql.toString());
      StringBuffer eventQuery = new StringBuffer();
      for(int i=0;i<scgColl.size();i++)
      {
           Object scgDetails = (Object) scgColl.get(i);
           eventQuery = new StringBuffer();
           if (scgDetails.toString().equals(String.valueOf(scgId)))
           {
              currDate= getCEPDate(scgDetails.toString());
              if(i>0)
              {
                  scgDetails = (Object) scgColl.get(i-1);
                  prevDate=getCEPDate(scgDetails.toString());
              }
              map.put(ClinPortalIntegrationConstants.RECENT_SCG_DATE,currDate);
              map.put(ClinPortalIntegrationConstants.PREV_SCG_DATE,prevDate);
              break;
           }
      }
      return map;
  }

  /**
   *
   * @param scgId
   * @return
   * @throws BizLogicException
   */
  private Date getCEPDate(final String scgId) throws BizLogicException
  {
      final StringBuffer eventQuery = new StringBuffer();
      final DefaultBizLogic bizLogic = new DefaultBizLogic();
      eventQuery.append("select timestamp from ").append(CollectionEventParameters.class.getName());
      eventQuery.append(" where specimenCollectionGroup.id=").append(scgId);
      final List dateList = bizLogic.executeQuery(eventQuery.toString());
      Date currDate=null;
      if (!dateList.isEmpty())
      {
          currDate = (Date) dateList.get(0);
      }
      return currDate;
  }

  /**
   *
   * @param participantId
   * @param cpId
   * @return
   * @throws BizLogicException
   */
  public Long getCPRId(final Long participantId,final Long cpId) throws BizLogicException
  {
       final QueryWhereClause queryClause = new QueryWhereClause(CollectionProtocolRegistration.class.getName());
       Long cprId = null;
       try
       {
           queryClause.addCondition(new EqualClause("participant.id", participantId)).andOpr();
           queryClause.addCondition(new EqualClause("collectionProtocol.id",cpId));
           final String[] colName = {"id"};
           List cpr = new DefaultBizLogic().retrieve(CollectionProtocolRegistration.class.getName(), colName,
                   queryClause);
           if (cpr != null && !cpr.isEmpty())
           {
               cprId = Long.valueOf(cpr.get(0).toString());
           }
       }
       catch (DAOException e)
       {
           throw new BizLogicException(e);
       }
       return cprId;
  }


  /**
  *
  * @param loginName
  * @param collectionProtocolId
  * @param participantId
  * @param collectionEventId
  * @param visitNum
  * @param visitId
  * @return
  * @throws BizLogicException
  */
 public String getRecentSCG(String loginName, Long collectionProtocolId,
         Long participantId, Long collectionEventId, String visitNum,
         Long visitId) throws BizLogicException
 {
     ClinPortalAPIService apiService = new ClinPortalAPIService();
     ClinPortalCaTissueIntegrationUtil util = new ClinPortalCaTissueIntegrationUtil();
     String scgId = null;
     try
     {
         Map<String, Date> map = apiService.getVisitRelatedEncounteredDate(
                 loginName, visitId, collectionEventId,collectionProtocolId,participantId);
         Date visitPrevDate = map.get(ClinPortalIntegrationConstants.PREV_VISIT_DATE);
         Date visitCurrDate = map.get(ClinPortalIntegrationConstants.RECENT_VISIT_DATE);
         Long cprId= util.getCPRId(participantId, collectionProtocolId);
         if (cprId != null )
         {
             Map<String, Date> completedSCG = new HashMap<String, Date>();
             Map<String, Date> anticipatedSCG = new HashMap<String, Date>();
             util.getScg(collectionEventId, cprId, completedSCG, anticipatedSCG);
             if (anticipatedSCG.size() > 0)
             {
                 scgId = util.getClosestDate(anticipatedSCG, visitCurrDate,visitPrevDate);
             }
             else
             {
                 scgId = util.getClosestDate(completedSCG, visitCurrDate,visitPrevDate);
             }
         }
     }
     catch (DAOException e)
     {
         throw new BizLogicException(e);
     }
     catch (Exception e1)
     {
         throw new BizLogicException(null,e1,e1.getMessage());
     }
     return scgId;
 }

 /**
  *
  * @param map
  * @return
  */
 public static boolean validateClinPortalMap(Map<String, Long> map)
 {
     boolean result= false;
     final Long csId = map.get(ClinPortalIntegrationConstants.CLINICAL_STUDY_ID);
     final Long cseId = map.get(ClinPortalIntegrationConstants.EVENT_ID);
     final Long pId = map.get(ClinPortalIntegrationConstants.CP_PARTICIPANT_ID);
     if(csId!=null && cseId!=null && pId!=null)
         result=true;

     return result;
 }
}
