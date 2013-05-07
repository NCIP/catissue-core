
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.SpecimenEventParametersBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.Position;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;

public class TransferEventAjaxAction extends SecureAction
{

	/**
	 * This method take the attributes from request then populate transfer event object 
	 * then insert the object into database.
	 * @param mapping object of ActionMapping class.
	 * @param form object of ActionForm class.
	 * @param request object of HttpServletRequest class.
	 * @param response object of HttpServletResponse class.
	 * @return forward mapping.
	 * @throws Exception if some problem occurs.
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		DAO dao = null;
		try
		{
			String fromContainerName = request.getParameter(Constants.FROM_CONTAINER_NAME);
			String fromPos1 = request.getParameter(Constants.FROM_POSITION_DIMENSION_ONE);
			String fromPos2 = request.getParameter(Constants.FROM_POSITION_DIMENSION_TWO);

			String toContainerName = request.getParameter(Constants.TO_CONTAINER_NAME);
			String toPos1 = request.getParameter(Constants.TO_POSITION_DIMENSION_ONE);
			String toPos2 = request.getParameter(Constants.TO_POSITION_DIMENSION_TWO);

			String specimenId = request.getParameter(Constants.SPECIMEN_ID);
			final SessionDataBean sessionDataBean = this.getSessionData(request);
			SpecimenPosition specPos = null;
			if (sessionDataBean != null && specimenId != null)
			{
				dao = AppUtility.openDAOSession(sessionDataBean);
				TransferEventParameters trfrEvent = new TransferEventParameters();
				StorageContainer fromStorageContainer;
				if(Boolean.valueOf(request.getParameter("isVirtual")))
				{
					fromStorageContainer = null;
					trfrEvent.setFromPositionDimensionOne(null);
					trfrEvent.setFromPositionDimensionTwo(null);
					specPos = new SpecimenPosition();
				}
				else
				{
					fromStorageContainer = new StorageContainer();
					fromStorageContainer.setName(fromContainerName);
					
					trfrEvent.setFromPositionDimensionOne(StorageContainerUtil
							.convertSpecimenPositionsToInteger(fromContainerName, 1, fromPos1));
					trfrEvent.setFromPositionDimensionTwo(StorageContainerUtil
							.convertSpecimenPositionsToInteger(fromContainerName, 2, fromPos2));
				}

				StorageContainer toStorageContainer = new StorageContainer();
				toStorageContainer.setName(toContainerName);
				trfrEvent.setToStorageContainer(toStorageContainer);
				if(Validator.isEmpty(toPos1) && Validator.isEmpty(toPos2) && Validator.isEmpty(toContainerName))
				{
					trfrEvent.setToPositionDimensionOne(null);
					trfrEvent.setToPositionDimensionTwo(null);
					trfrEvent.setToStorageContainer(null);
				}
				else
				{
					Integer toPos1Int = null, toPos2Int = null;
					if (toPos1 != null && !"".equals(toPos1.trim()) && toPos2 != null
							&& !"".equals(toPos2.trim()))
					{
						toPos1Int = StorageContainerUtil.convertSpecimenPositionsToInteger(
								toContainerName, 1, toPos1);
						toPos2Int = StorageContainerUtil.convertSpecimenPositionsToInteger(
								toContainerName, 2, toPos2);
						trfrEvent.setToPositionDimensionOne(toPos1Int);
						trfrEvent.setToPositionDimensionTwo(toPos2Int);
					}
					else
					{
						List idList = AppUtility
								.executeSQLQuery("select identifier from catissue_container cont where cont.name like '"
										+ toContainerName + "'");
						if(idList!=null && !idList.isEmpty())
						{
							ArrayList idArr = (ArrayList) idList.get(0);
							toStorageContainer.setId(Long.valueOf((String) idArr.get(0)));
						}
						else
						{
							throw new BizLogicException( ErrorKey.getErrorKey( "invalid.container.name" ) ,null , "" );
						}
						Position position = StorageContainerUtil.getFirstAvailablePositionsInContainer(
								toStorageContainer, new HashSet(), dao, null, null);
						trfrEvent.setToPositionDimensionOne(position.getXPos());
						trfrEvent.setToPositionDimensionTwo(position.getYPos());
					}
	
					final String sourceObjectName1 = StorageContainer.class.getName();
					final String[] selectColumnName1 = {"id"};
					final QueryWhereClause queryWhereClause1 = new QueryWhereClause(sourceObjectName1);
					queryWhereClause1.addCondition(new EqualClause("name", toContainerName));
					final List list1 = dao.retrieve(sourceObjectName1, selectColumnName1,
							queryWhereClause1);
					if (list1.size() != 0)
					{
						toStorageContainer.setId((Long) list1.get(0));
					}
					
					//dao=AppUtility.openDAOSession(sessionDataBean);
					final String sourceObjectName3 = Specimen.class.getName();
					final String[] selectColumnName3 = {"specimenPosition"};
					final QueryWhereClause queryWhereClause3 = new QueryWhereClause(sourceObjectName3);
					queryWhereClause3.addCondition(new EqualClause("id", Long.valueOf(specimenId)));
					final List list3 = dao.retrieve(sourceObjectName3, selectColumnName3,
							queryWhereClause3);
					if (list3.size() != 0)
					{
						specPos = (SpecimenPosition) list3.get(0);
					}
					
					specPos.setPositionDimensionOneString(toPos1);
					specPos.setPositionDimensionTwoString(toPos2);
					specPos.setPositionDimensionOne(toPos1Int);
					specPos.setPositionDimensionTwo(toPos2Int);

				}
				trfrEvent.setFromStorageContainer(fromStorageContainer);

//				dao = AppUtility.openDAOSession(sessionDataBean);
				final String sourceObjectName2 = StorageContainer.class.getName();
				final String[] selectColumnName2 = {"id"};
				final QueryWhereClause queryWhereClause2 = new QueryWhereClause(sourceObjectName2);
				queryWhereClause2.addCondition(new EqualClause("name", fromContainerName));
				final List list2 = dao.retrieve(sourceObjectName2, selectColumnName2,
						queryWhereClause2);
				if (list2.size() != 0)
				{
					fromStorageContainer.setId((Long) list2.get(0));
				}

				Specimen specimen = new Specimen();
				specimen.setId(Long.valueOf(specimenId));

				
				trfrEvent.setSpecimen(specimen);

				trfrEvent.setTimestamp(new Date());

				User user = new User();
				user.setId(sessionDataBean.getUserId());
				trfrEvent.setUser(user);

				SpecimenEventParametersBizLogic eventBizLogic = new SpecimenEventParametersBizLogic();
				eventBizLogic.insert(trfrEvent, sessionDataBean);

				SpecimenPosition pos = ((Specimen) trfrEvent.getSpecimen()).getSpecimenPosition();;

				StringBuffer sb = new StringBuffer();
				if(pos == null)
				{
					sb.append("virtual");
				}
				else
				{
					
					sb.append(toContainerName);
					sb.append("#");
					sb.append(pos.getPositionDimensionOneString());
					sb.append("#");
					sb.append(pos.getPositionDimensionTwoString());
				}
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				final String msg = sb.toString();
				response.getWriter().write(msg);
			}
		}
		catch (Exception exp)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(exp.getMessage());
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			final String msg = sb.toString();
			response.getWriter().write(msg);
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}
		return null;
	}
}
