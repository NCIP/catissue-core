/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author
 *@version 1.0
 */

package edu.wustl.catissuecore.action.annotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.domain.integration.EntityMapCondition;
import edu.common.dynamicextensions.domain.integration.FormContext;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.actionForm.AnnotationForm;
import edu.wustl.catissuecore.annotations.AnnotationUtil;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Constants;

/**
 * @author renuka_bajpai
 *
 */
public class SaveAnnotationsConditionsAction extends BaseAction
{

	/**
	 * @param mapping - mapping.
	 * @param form - ActionForm
	 * @param request - HttpServletRequest object
	 * @param response - HttpServletResponse
	 * @return ActionForward
	 * @throws Exception - Exception
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward actionfwd = null;

		final AnnotationForm annotationForm = (AnnotationForm) form;

		this.saveConditions( annotationForm, request );
		actionfwd = mapping.findForward( Constants.SUCCESS );

		return actionfwd;
	}

	/**
	 * @param annotationForm : annotationForm
	 * @param request : request
	 * @throws BizLogicException : BizLogicException
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 */
	private void saveConditions(AnnotationForm annotationForm, HttpServletRequest request)
			throws BizLogicException, DynamicExtensionsSystemException
	{

		final String containerId = request.getParameter( "containerId" );
		final AnnotationBizLogic bizLogic = new AnnotationBizLogic();
		List dynamicList = new ArrayList();
		dynamicList = bizLogic.getListOfStaticEntities( Long.valueOf( containerId ) );
		final CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager
				.getInstance();

		if (dynamicList != null && !dynamicList.isEmpty())
		{
			final EntityMap entityMap = (EntityMap) dynamicList.get( 0 );

			final Collection < FormContext > formCollPrev = AppUtility.getFormContexts( entityMap
					.getId() );

			final Collection < FormContext > currFormColl = new HashSet < FormContext >();
			int conditionValindex = 0;
			final AnnotationUtil util = new AnnotationUtil();
			final Boolean check = util.checkForAll( annotationForm.getConditionVal() );
			if (formCollPrev != null && !formCollPrev.isEmpty())
			{
				final Iterator < FormContext > formCollIt = formCollPrev.iterator();

				if (annotationForm.getConditionVal() != null)
				{
					while (formCollIt.hasNext())
					{
						final FormContext formContext = formCollIt.next();

						final Collection < EntityMapCondition > entityMapConditions = AppUtility
								.getEntityMapConditions( formContext.getId() );

						if (( formContext.getNoOfEntries() == null || formContext.getNoOfEntries()
								.equals( "" ) )
								&& ( formContext.getStudyFormLabel() == null || formContext
										.getStudyFormLabel().equals( "" ) ))
						{
							if (entityMapConditions != null && !entityMapConditions.isEmpty())
							{
								final Iterator < EntityMapCondition > entityMapCondIter = entityMapConditions
										.iterator();
								while (entityMapCondIter.hasNext())
								{
									if (conditionValindex < annotationForm.getConditionVal().length
											&& !check)
									{
										//Use existing condition objects in edit operation
										if (conditionValindex < entityMapConditions.size())
										{
											final EntityMapCondition condn = entityMapCondIter
													.next();
											condn.setStaticRecordId( Long.valueOf( annotationForm
													.getConditionVal()[conditionValindex] ) );
										}
										else
										{//if current conditions are more than previously added then make new condn obj
											final EntityMapCondition condn = new EntityMapCondition();
											condn.setStaticRecordId( Long.valueOf( annotationForm
													.getConditionVal()[conditionValindex] ) );
											condn.setFormContext( formContext );
											condn
													.setTypeId( Long
															.valueOf( catissueCoreCacheManager
																	.getObjectFromCache(
																			AnnotationConstants.COLLECTION_PROTOCOL_ENTITY_ID )
																	.toString() ) );
											entityMapConditions.add( condn );
										}
										conditionValindex++;
									}
									else if (annotationForm.getConditionVal().length <= entityMapConditions
											.size())
									{//if previously added conditions were more than current one then deassociate previous
										final EntityMapCondition condn = entityMapCondIter.next();
										condn.setFormContext( null );
									}
								}
							}
							//previously no condition exists but now conditions are added
							if (annotationForm.getConditionVal() != null
									&& annotationForm.getConditionVal().length > conditionValindex
									&& !check)
							{
								while (annotationForm.getConditionVal().length > conditionValindex)
								{
									final EntityMapCondition condn = new EntityMapCondition();
									condn.setStaticRecordId( Long.valueOf( annotationForm
											.getConditionVal()[conditionValindex] ) );
									condn.setFormContext( formContext );
									condn
											.setTypeId( Long
													.valueOf( catissueCoreCacheManager
															.getObjectFromCache(
																	AnnotationConstants.COLLECTION_PROTOCOL_ENTITY_ID )
															.toString() ) );
									entityMapConditions.add( condn );
									conditionValindex++;
								}
							}
							formContext.setEntityMapConditionCollection( entityMapConditions );
							currFormColl.add( formContext );
						}
					}
				}
				entityMap.setFormContextCollection( currFormColl );
				bizLogic.updateEntityMap( entityMap );
			}
		}
	}
}
