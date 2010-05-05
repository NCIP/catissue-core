
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
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
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
	public List<NameValueBean> getCollectionProtocolCollection(SessionDataBean sessionDataBean)
			throws ApplicationException
	{
		List<NameValueBean> cpDetailsList = new ArrayList<NameValueBean>();
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final UserBizLogic userBizLogic = (UserBizLogic) factory
				.getBizLogic(Constants.USER_FORM_ID);
		final Set<Long> cpIds = userBizLogic.getRelatedCPIds(sessionDataBean.getUserId(), true);
		final List<NameValueBean> collectionProtocolBeanList = this
				.getAndFilterCollectionProtocolBeanList(cpIds);
		final Set<Long> siteIds = userBizLogic.getRelatedSiteIds(sessionDataBean.getUserId());
		final Set<Long> cp_Ids = userBizLogic.getRelatedCPIds(sessionDataBean.getUserId(), false);
		cpDetailsList = this.getCollectionPorotocolForSiteIds(sessionDataBean, factory, siteIds,
				cp_Ids);
		this
				.removeDuplicateCollectionProtoclBeanFromList(cpDetailsList,
						collectionProtocolBeanList);
		return cpDetailsList;
	}

	/**
	 * @param sessionDataBean : SessionDataBean
	 * @param factory : IFactory object
	 * @param siteIds : list of site ids.
	 * @param cp_Ids list of collection protocol ids
	 * @return list of collection protocol
	 */
	private List<NameValueBean> getCollectionPorotocolForSiteIds(SessionDataBean sessionDataBean,
			IFactory factory, Set<Long> siteIds, Set<Long> cp_Ids)
	{

		final List<NameValueBean> cpDetailsList = new ArrayList<NameValueBean>();
		try
		{
			if (siteIds != null && !siteIds.isEmpty())
			{
				final List<NameValueBean> list = new ArrayList<NameValueBean>();
				final PrivilegeCache privilegeCache = PrivilegeManager.getInstance()
						.getPrivilegeCache(sessionDataBean.getUserName());
				final SiteBizLogic siteBizLogic = (SiteBizLogic) factory
						.getBizLogic(Constants.SITE_FORM_ID);
				for (final Long siteId : siteIds)
				{
					final String peName = Constants.getCurrentAndFuturePGAndPEName(siteId);
					if (privilegeCache.hasPrivilege(peName,
							edu.wustl.common.util.global.Variables.privilegeDetailsMap
									.get(Constants.EDIT_PROFILE_PRIVILEGE)))
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
									for (final NameValueBean nameValueBean : list)
									{
										if (nameValueBean.getValue().equalsIgnoreCase(
												cp.getId().toString()))
										{
											isPresent = true;
											break;
										}
									}
									if (!isPresent)
									{
										list.add(new NameValueBean(cp.getShortTitle(), cp.getId()));
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
	 * This method will retrieve all the CPs from the database and keep the CPs which are in
	 * cpIds list.
	 * @param cpIds
	 * @return list of CPs
	 */
	private List<NameValueBean> getAndFilterCollectionProtocolBeanList(Set<Long> cpIds)
	{
		final List<NameValueBean> collectionProtocolBeanList = new Vector<NameValueBean>();
		List<NameValueBean> participantRegistrationBeanList = null;
		try
		{
			IFactory factory = null;
			factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final CollectionProtocolRegistrationBizLogic cBizLogic = (CollectionProtocolRegistrationBizLogic) factory
					.getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
			participantRegistrationBeanList = cBizLogic.getCollectionProtocolBeanList();
			if (cpIds == null)
			{
				collectionProtocolBeanList.addAll(participantRegistrationBeanList);
			}
			else
			{
				for (int counter = 0; counter < participantRegistrationBeanList.size(); counter++)
				{
					final NameValueBean cpDetails = participantRegistrationBeanList.get(counter);
					final Long cpId = Long.parseLong(cpDetails.getValue());

					if (cpIds.contains(cpId))
					{
						collectionProtocolBeanList.add(cpDetails);
					}
				}
			}
		}
		catch (final BizLogicException bizEx)
		{
			CpBasedViewBizLogic.LOGGER.error(bizEx.getMessage(),bizEx);
		}
		return collectionProtocolBeanList;
	}

	/**
	 * method remove the duplicate CollectionCollectionProtoclBean from the cpDetailsList.
	 * @param cpDetailsList : list of collection protocol
	 * @param collectionProtocolBeanList
	 */
	private void removeDuplicateCollectionProtoclBeanFromList(List<NameValueBean> cpDetailsList,
			List<NameValueBean> collectionProtocolBeanList)
	{
		final Iterator<NameValueBean> iter = collectionProtocolBeanList.iterator();
		while (iter.hasNext())
		{
			final NameValueBean cpDetails = iter.next();
			boolean isPresent = false;
			for (final NameValueBean nameValueBean : cpDetailsList)
			{
				if (nameValueBean.getValue().equalsIgnoreCase(cpDetails.getValue()))
				{
					isPresent = true;
					break;
				}
			}
			if (!isPresent)
			{
				cpDetailsList.add(cpDetails);
			}

		}
	}

	/**
	 * This method will retrieve all the participant for the selected CP.
	 * @param cpId
	 * @return list of CpAndParticipentsBeans
	 * @throws BizLogicException : BizLogicException
	 */

	public List<CpAndParticipentsBean> getRegisteredParticipantInfoCollection(Long cpId)
			throws BizLogicException, ApplicationException
	{
		final List<CpAndParticipentsBean> participantInfoList = new Vector<CpAndParticipentsBean>();
		final StringBuffer hql = new StringBuffer();
		String participantDisplayInfo = null;
		try
		{
			hql
					.append("select cpr.participant.id,cpr.participant.lastName,cpr.participant.firstName,cpr.protocolParticipantIdentifier from ");
			hql.append(CollectionProtocolRegistration.class.getName());
			hql.append(" as cpr  where  cpr.collectionProtocol.id = ");
			hql.append(cpId);
			hql.append(" and cpr.activityStatus != '");
			hql.append(Status.ACTIVITY_STATUS_DISABLED.toString());
			hql.append("' and ");
			hql.append(" cpr.participant.activityStatus != '");
			hql
					.append(Status.ACTIVITY_STATUS_DISABLED.toString()
							+ "' order by cpr.participant.id");

			final List<Object[]> participantList = AppUtility.executeQuery(hql.toString());

			for (int j = 0; j < participantList.size(); j++)
			{
				final Object[] participantObj = participantList.get(j);
				participantDisplayInfo = this.getFormattedParticpantInfo(participantObj);
				final int index = participantDisplayInfo.indexOf(":");
				Long identifier = null;
				String name = "";
				//Id = new Long(participantDisplayInfo.substring(0, index));
				identifier = Long.valueOf(participantDisplayInfo.substring(0, index));
				name = participantDisplayInfo.substring(index + 1);
				participantInfoList.add(new CpAndParticipentsBean(name, identifier.toString()));

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
