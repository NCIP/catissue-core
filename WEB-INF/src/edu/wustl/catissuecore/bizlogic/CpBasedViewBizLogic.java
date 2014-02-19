
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.bean.CpAndParticipentsBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * This class will fetch all the collection protocols from the DB and
 * also the participants for each collection protocol.
 * @author geeta_jaggal
 *
 */
public class CpBasedViewBizLogic extends CatissueDefaultBizLogic
{

	/**
	 *  Logger object.
	 */
	private static final transient Logger LOGGER = Logger
			.getCommonLogger(CpBasedViewBizLogic.class);

	/**
	 * This method returns list of CP ids and CP short titles.
	 * @param sessionDataBean : SessionDataBean
	 * @return list of collection protocol
	 * @throws ApplicationException : ApplicationException
	 */
	public List<CpAndParticipentsBean> getCollectionProtocolCollection(SessionDataBean sessionDataBean)
			throws ApplicationException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final UserBizLogic userBizLogic = (UserBizLogic) factory
				.getBizLogic(Constants.USER_FORM_ID);
		
		//get List of cp's associated with user.
		final List<CpAndParticipentsBean> collectionProtocolBeanList = userBizLogic.getRelatedCPAndParticipantBean(sessionDataBean.getUserId());
				
		//Add List of Cp's associated with user associated sites.
		final Set<Long> siteIds = userBizLogic.getRelatedSiteIds(sessionDataBean.getUserId());
		final Set<Long> cp_Ids = userBizLogic.getRelatedCPIds(sessionDataBean.getUserId(), false);
		this.addCollectionPorotocolForSiteIds(sessionDataBean, factory, siteIds, 
				cp_Ids,collectionProtocolBeanList);

		return collectionProtocolBeanList;
	}

	/**
	 * @param sessionDataBean : SessionDataBean
	 * @param factory : IFactory object
	 * @param siteIds : list of site ids.
	 * @param cp_Ids list of collection protocol ids
	 * @return list of collection protocol
	 */
	private List<CpAndParticipentsBean> addCollectionPorotocolForSiteIds(SessionDataBean sessionDataBean,
			IFactory factory, Set<Long> siteIds, Set<Long> cp_Ids,List<CpAndParticipentsBean> cpDetailsList)
	{

		try
		{
			if (siteIds != null && !siteIds.isEmpty())
			{
				final List<CpAndParticipentsBean> list = new ArrayList<CpAndParticipentsBean>();
				final PrivilegeCache privilegeCache = PrivilegeManager.getInstance()
						.getPrivilegeCache(sessionDataBean.getUserName());
				final SiteBizLogic siteBizLogic = (SiteBizLogic) factory
						.getBizLogic(Constants.SITE_FORM_ID);
				boolean hasViewPrivilege = false; // This checks if user has Registration and/or Specimen_Processing privilege
				boolean isPhiView = false;
				
				for (final Long siteId : siteIds)
				{
					final String peName = Constants.getCurrentAndFuturePGAndPEName(siteId);
					if (privilegeCache.hasPrivilege(peName,
							edu.wustl.common.util.global.Variables.privilegeDetailsMap
									.get(Constants.EDIT_PROFILE_PRIVILEGE)))
					{
						isPhiView = true;
						hasViewPrivilege = true;
					}
					else if(privilegeCache.hasPrivilege(peName,Permissions.SPECIMEN_PROCESSING))
					{
						isPhiView = false;
						hasViewPrivilege = true;
					}
					if(hasViewPrivilege)
					{
						Collection<CollectionProtocol> cpCollection;
						DAO dao = null;
						try
						{
							dao = this.openDAOSession(sessionDataBean);
							cpCollection = siteBizLogic.getRelatedCPs(siteId, dao);
							if (cpCollection != null && !cpCollection.isEmpty())
							{
								for (final CollectionProtocol cp : cpCollection)
								{
									if (cp_Ids.contains(cp.getId()))
									{
										continue;
									}
									boolean isPresent = false;
									for (final CpAndParticipentsBean cpBean : list)
									{
										if (cpBean.getValue().equalsIgnoreCase(
												cp.getId().toString()))
										{
											isPresent = true;
											break;
										}
									}
									if (!isPresent)
									{
										list.add(new CpAndParticipentsBean(cp.getShortTitle(), String.valueOf(cp.getId()),isPhiView));
									}
								}
							}
						}
						finally
						{
							this.closeDAOSession(dao);
						}
					}
				}
				cpDetailsList.addAll(list);
			}
		}
		catch (final BizLogicException bizEx)
		{
			CpBasedViewBizLogic.LOGGER.error(bizEx.getMessage(),bizEx);
		}
		catch (final SMException smEx)
		{
			CpBasedViewBizLogic.LOGGER.error(smEx.getMessage(),smEx);
		}
		return cpDetailsList;
	}


	/**
	 * This method will retrieve all the participant for the selected CP.
	 * @param cpId
	 * @return list of CpAndParticipentsBeans
	 * @throws BizLogicException : BizLogicException
	 */

	public List<CpAndParticipentsBean> getRegisteredParticipantInfoCollection(Long cpId,boolean isPHIView)
			throws BizLogicException, ApplicationException
	{
		final List<CpAndParticipentsBean> participantInfoList = new Vector<CpAndParticipentsBean>();
		final StringBuffer hql = new StringBuffer();
		try
		{
			hql.append("select cpr.participant.id");
			if(isPHIView)
			{	
				/*Added case  statements because when we add participants from BO or SPR reports,
				 * participant's first name, last name can be null. If the last name,
				 * first name or ppid is null then it will be replaced by blank string.
				 */
				String columnName=", case"+ 
									" when cpr.participant.lastName is null"+
									" then '' "+
									"else"+
									" cpr.participant.lastName"+ 
									" end"+
									"||', '||"+
									"case "+
									" when cpr.participant.firstName is null"+ 
									" then '' "+ 
									"else "+
									"cpr.participant.firstName"+ 
									" end"+
									"||'( '||"+
									"case"+
									" when cpr.protocolParticipantIdentifier is null"+ 
									" then '' "+
									"else"+
									" cpr.protocolParticipantIdentifier "+ 
									"end"+
									"||' )'";
				hql.append(columnName);
			}		
			else
			{
				hql.append(",cpr.protocolParticipantIdentifier");
			}
			
			hql.append(" from ");
			hql.append(CollectionProtocolRegistration.class.getName());
			hql.append(" as cpr  where  cpr.collectionProtocol.id = ");
			hql.append("?");
			hql.append(" and cpr.activityStatus != '");
			hql.append(Status.ACTIVITY_STATUS_DISABLED.toString());
			hql.append("' and ");
			hql.append(" cpr.participant.activityStatus != '");
			hql.append(Status.ACTIVITY_STATUS_DISABLED.toString()
							+ "' order by cpr.participant.id");
			ColumnValueBean colValueBean = new ColumnValueBean(cpId);
			List<ColumnValueBean> colvaluebeanlist = new ArrayList<ColumnValueBean>();
			colvaluebeanlist.add(colValueBean);
			
			final List<Object[]> participantList = AppUtility.executeHqlQuery(hql.toString(), colvaluebeanlist);
            	for (int j = 0; j < participantList.size(); j++)
    			{
    				final Object[] participantObj = participantList.get(j);
    				String display_name = (String) participantObj[1] ;
    				
    				Long identifier = (Long) participantObj[0];
    				participantInfoList.add(new CpAndParticipentsBean(display_name, identifier.toString(),isPHIView));
    			}
    	}
		catch (final DAOException daoExp)
		{
			CpBasedViewBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final ApplicationException appEx)
		{
			CpBasedViewBizLogic.LOGGER.error(appEx.getMessage(),appEx);
			throw new ApplicationException(appEx.getErrorKey(), appEx, appEx.getMsgValues());
		}
		catch (final Exception exp)
		{
			CpBasedViewBizLogic.LOGGER.error(exp.getMessage(),exp);
		}
		return participantInfoList;
	}

	   /**
     * This method will retrieve all the participant for the selected CP.
     * @param cpId
     * @return list of CpAndParticipentsBeans
     * @throws BizLogicException : BizLogicException
     */

    public List<CpAndParticipentsBean> getRegisteredParticipantInfoCollection(Long cpId,String searchString,boolean isPHIView)
            throws BizLogicException, ApplicationException
    {
        final List<CpAndParticipentsBean> participantInfoList = new Vector<CpAndParticipentsBean>();
        final StringBuffer hql = new StringBuffer();
        HibernateDAO hibernateDAO = null;
        try
        {
            hql.append("select cpr.participant.id");
            if(isPHIView)
            {   
                /*Added case  statements because when we add participants from BO or SPR reports,
                 * participant's first name, last name can be null. If the last name,
                 * first name or ppid is null then it will be replaced by blank string.
                 */
                String columnName=", case"+ 
                                    " when cpr.participant.lastName is null"+
                                    " then '' "+
                                    "else"+
                                    " cpr.participant.lastName"+ 
                                    " end"+
                                    "||', '||"+
                                    "case "+
                                    " when cpr.participant.firstName is null"+ 
                                    " then '' "+ 
                                    "else "+
                                    "cpr.participant.firstName"+ 
                                    " end"+
                                    "||'( '||"+
                                    "case"+
                                    " when cpr.protocolParticipantIdentifier is null"+ 
                                    " then '' "+
                                    "else"+
                                    " cpr.protocolParticipantIdentifier "+ 
                                    "end"+
                                    "||' )'";
                hql.append(columnName);
            }       
            else
            {
                hql.append(",cpr.protocolParticipantIdentifier");
            }
            
            hql.append(" from ");
            hql.append(CollectionProtocolRegistration.class.getName());
            hql.append(" as cpr  where  cpr.collectionProtocol.id = ");
            hql.append("?");
            hql.append(" and cpr.activityStatus != '");
            hql.append(Status.ACTIVITY_STATUS_DISABLED.toString());
            hql.append("' and ");
            hql.append(" cpr.participant.activityStatus != '");
            hql.append(Status.ACTIVITY_STATUS_DISABLED.toString()+"'");
            hibernateDAO = (HibernateDAO) AppUtility.openDAOSession(null);
            List paramList = new ArrayList();
            paramList.add(cpId);
            String hsqlString = hql.toString();
            List<Object[]> participantList = null;
            int upperLimit =  Integer.parseInt(XMLPropertyHandler.getValue("participant.list.count"));
            if(Validator.isEmpty(searchString)){
                participantList  = hibernateDAO.executeQuery(hsqlString+" order by cpr.registrationDate desc", 0,upperLimit,paramList );
            }else{
                String appendHql = "and ( lower(cpr.participant.lastName) like ? or   lower(cpr.participant.firstName) like ? or  lower(cpr.protocolParticipantIdentifier) like ?)";
                paramList.add("%"+searchString.toLowerCase()+"%");
                paramList.add("%"+searchString.toLowerCase()+"%");
                paramList.add("%"+searchString.toLowerCase()+"%");
                participantList  = hibernateDAO.executeQuery(hsqlString+appendHql, 0, upperLimit,paramList );
                
            }
                    
                    
                    
                    
                for (int j = 0; j < participantList.size(); j++)
                {
                    final Object[] participantObj = participantList.get(j);
                    String display_name = (String) participantObj[1] ;
                    
                    Long identifier = (Long) participantObj[0];
                    participantInfoList.add(new CpAndParticipentsBean(display_name, identifier.toString(),isPHIView));
                }
        }
        catch (final DAOException daoExp)
        {
            CpBasedViewBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
            throw this
                    .getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
        }
        catch (final ApplicationException appEx)
        {
            CpBasedViewBizLogic.LOGGER.error(appEx.getMessage(),appEx);
            throw new ApplicationException(appEx.getErrorKey(), appEx, appEx.getMsgValues());
        }
        catch (final Exception exp)
        {
            CpBasedViewBizLogic.LOGGER.error(exp.getMessage(),exp);
        }
        finally{
            AppUtility.closeDAOSession(hibernateDAO);
        }
        return participantInfoList;
    }
    
	/**
	 * This method will format the participant name.
	 * @param participantObj : participant object.
	 * @return formated participant name.
	 */
	public String getFormattedParticpantInfo(Object[] participantObj)
	{
		String participantDisplayInfo = null;
		String protocolParticipantId = null;
		String lastName = null;
		String firstName = null;
		String info = null;
		if (participantObj[0] != null)
		{
			info = participantObj[0].toString();
		}
		if (participantObj[1] != null)
		{
			lastName = participantObj[1].toString();
		}
		if (participantObj[2] != null)
		{
			firstName = participantObj[2].toString();
		}

		if (participantObj[3] != null)
		{
			protocolParticipantId = participantObj[3].toString();
		}
		participantDisplayInfo = "";

		if (lastName != null && !lastName.equals(""))
		{
			participantDisplayInfo = lastName;
		}
		if (firstName != null && !firstName.equals(""))
		{
			if (firstName == null || firstName.equals(""))
			{
				participantDisplayInfo = firstName;
			}
			else
			{
				participantDisplayInfo = participantDisplayInfo + " , " + firstName;
			}
		}
		if ("".equals(participantDisplayInfo))
		{
			participantDisplayInfo = "N/A";
		}
		if (protocolParticipantId != null && !protocolParticipantId.equals(""))
		{
			participantDisplayInfo = participantDisplayInfo + " (" + protocolParticipantId + ")";
		}
		else
		{
			participantDisplayInfo = participantDisplayInfo + " (N/A)";
		}

		if ("".equals(participantDisplayInfo))
		{
			participantDisplayInfo = "N/A";
		}
		info = info + ":" + participantDisplayInfo;

		return info;
	}
	
}
