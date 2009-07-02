
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
	private static final transient Logger logger = Logger
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
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		UserBizLogic userBizLogic = (UserBizLogic) factory.getBizLogic(Constants.USER_FORM_ID);
		Set<Long> cpIds = userBizLogic.getRelatedCPIds(sessionDataBean.getUserId(), true);
		List<NameValueBean> collectionProtocolBeanList = getAndFilterCollectionProtocolBeanList(cpIds);
		Set<Long> siteIds = userBizLogic.getRelatedSiteIds(sessionDataBean.getUserId());
		Set<Long> cp_Ids = userBizLogic.getRelatedCPIds(sessionDataBean.getUserId(), false);
		cpDetailsList = getCollectionPorotocolForSiteIds(sessionDataBean, factory, siteIds, cp_Ids);
		removeDuplicateCollectionProtoclBeanFromList(cpDetailsList, collectionProtocolBeanList);
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

		List<NameValueBean> cpDetailsList = new ArrayList<NameValueBean>();
		try
		{
			if (siteIds != null && !siteIds.isEmpty())
			{
				List<NameValueBean> list = new ArrayList<NameValueBean>();
				PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
						sessionDataBean.getUserName());
				SiteBizLogic siteBizLogic = (SiteBizLogic) factory
						.getBizLogic(Constants.SITE_FORM_ID);
				for (Long siteId : siteIds)
				{
					String peName = Constants.getCurrentAndFuturePGAndPEName(siteId);
					if (privilegeCache.hasPrivilege(peName,
							edu.wustl.common.util.global.Variables.privilegeDetailsMap
									.get(Constants.EDIT_PROFILE_PRIVILEGE)))
					{
						Collection<CollectionProtocol> cpCollection;
						cpCollection = siteBizLogic.getRelatedCPs(siteId);
						if (cpCollection != null && !cpCollection.isEmpty())
						{
							for (CollectionProtocol cp : cpCollection)
							{
								if (cp_Ids.contains(cp.getId()))
								{
									continue;
								}
								boolean isPresent = false;
								for (NameValueBean nameValueBean : list)
								{
									if (nameValueBean.getValue().equalsIgnoreCase(
											cp.getId().toString()))
									{
										isPresent = true;
										break;
									}
								}
								if (!isPresent)
									list.add(new NameValueBean(cp.getShortTitle(), cp.getId()));
							}
						}
					}
				}
				cpDetailsList.addAll(list);
			}
		}
		catch (BizLogicException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SMException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		List<NameValueBean> collectionProtocolBeanList = new Vector<NameValueBean>();
		List<NameValueBean> participantRegistrationBeanList = null;
		try
		{
			IFactory factory = null;
			factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			CollectionProtocolRegistrationBizLogic cBizLogic = (CollectionProtocolRegistrationBizLogic) factory
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
					NameValueBean cpDetails = (NameValueBean) participantRegistrationBeanList
							.get(counter);
					Long cpId = Long.parseLong(cpDetails.getValue());

					if (cpIds.contains(cpId))
					{
						collectionProtocolBeanList.add(cpDetails);
					}
				}
			}
		}
		catch (BizLogicException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		Iterator<NameValueBean> iter = collectionProtocolBeanList.iterator();
		while (iter.hasNext())
		{
			NameValueBean cpDetails = (NameValueBean) iter.next();
			boolean isPresent = false;
			for (NameValueBean nameValueBean : cpDetailsList)
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
			throws BizLogicException
	{
		List<CpAndParticipentsBean> participantInfoList = new Vector<CpAndParticipentsBean>();
		StringBuffer hql = new StringBuffer();
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
			hql.append(" ' and ");
			hql.append(" cpr.participant.activityStatus != '");
			hql
					.append(Status.ACTIVITY_STATUS_DISABLED.toString()
							+ "' order by cpr.participant.id");

			List<Object[]> participantList = AppUtility.executeQuery(hql.toString());

			for (int j = 0; j < participantList.size(); j++)
			{
				Object[] participantObj = (Object[]) participantList.get(j);
				participantDisplayInfo = getFormattedParticpantInfo(participantObj);
				int index = participantDisplayInfo.indexOf(":");
				Long Id = null;
				String name = "";
				//Id = new Long(participantDisplayInfo.substring(0, index));
				Id = Long.valueOf((participantDisplayInfo.substring(0, index)));
				name = participantDisplayInfo.substring(index + 1);
				participantInfoList.add(new CpAndParticipentsBean(name, Id.toString()));

			}
		}
		catch (DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (ApplicationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		String info = (String) participantObj[0].toString();
		lastName = (String) participantObj[1].toString();
		firstName = (String) participantObj[2].toString();
		protocolParticipantId = (String) participantObj[3].toString();
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
		if (participantDisplayInfo.equals(""))
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

		if (participantDisplayInfo.equals(""))
		{
			participantDisplayInfo = "N/A";
		}
		info = info + ":" + participantDisplayInfo;

		return info;
	}

}
