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

import net.sf.ehcache.CacheException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.domain.integration.EntityMapCondition;
import edu.common.dynamicextensions.domain.integration.FormContext;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.actionForm.AnnotationForm;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Constants;

public class SaveAnnotationsConditionsAction extends BaseAction
{

    @Override
    protected ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        ActionForward actionfwd = null;

        AnnotationForm annotationForm = (AnnotationForm) form;

        saveConditions(annotationForm, request);
        actionfwd = mapping.findForward(Constants.SUCCESS);

        return actionfwd;
    }
/**
 * 
 * @param annotationForm
 * @param request
 * @throws CacheException
 * @throws BizLogicException
 * @throws DynamicExtensionsApplicationException
 */
    private void saveConditions(AnnotationForm annotationForm,
            HttpServletRequest request) throws CacheException, BizLogicException, DynamicExtensionsSystemException
    {
        
        String containerId = request.getParameter("containerId"); 
        AnnotationBizLogic bizLogic = new AnnotationBizLogic();
        List dynamicList = new ArrayList();
        dynamicList = bizLogic.getListOfStaticEntities(Long.valueOf(containerId));
        CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager
                .getInstance();

        if (dynamicList != null && !dynamicList.isEmpty())
        {
            EntityMap entityMap = (EntityMap) dynamicList.get(0);
            
            Collection<FormContext> formCollPrev = AppUtility.getFormContexts(entityMap.getId());

			Collection<FormContext> currFormColl= new HashSet<FormContext>();
            int conditionValindex = 0;            

            if (formCollPrev != null && !formCollPrev.isEmpty())
            {
                Iterator<FormContext> formCollIt = formCollPrev.iterator();
                
                if (annotationForm.getConditionVal() != null)
                {
	                while (formCollIt.hasNext())
	                {
	                    FormContext formContext = (FormContext) formCollIt.next();
	                    
	        			Collection<EntityMapCondition> entityMapConditions = AppUtility.getEntityMapConditions(formContext.getId());
	        			
	                    if((formContext.getNoOfEntries() == null || formContext.getNoOfEntries().equals(""))&&(formContext.getStudyFormLabel() == null || formContext.getStudyFormLabel().equals("")))
	                    {
	                    if (entityMapConditions != null
	                            && !entityMapConditions.isEmpty())
	                    {
	                        Iterator<EntityMapCondition> entityMapCondIter = entityMapConditions.iterator();
	                        while (entityMapCondIter.hasNext())
	                        {
	                            if (conditionValindex < annotationForm.getConditionVal().length)
	                            {
	                                //Use existing condition objects in edit operation
	                                if (conditionValindex <  entityMapConditions.size() )
	                                {
	                                    EntityMapCondition condn = entityMapCondIter.next();
	                                    condn.setStaticRecordId(Long.valueOf(annotationForm.getConditionVal()[conditionValindex]));
	                                }
	                                else 
	                                {//if current conditions are more than previously added then make new condn obj
	                                    EntityMapCondition condn = new EntityMapCondition();
	                                    condn.setStaticRecordId(Long.valueOf(annotationForm.getConditionVal()[conditionValindex]));
	                                    condn.setFormContext(formContext);
	                                    condn.setTypeId(Long.valueOf(catissueCoreCacheManager.getObjectFromCache(
	                                            AnnotationConstants.COLLECTION_PROTOCOL_ENTITY_ID).toString()));
	                                    entityMapConditions.add(condn);
	                                }
	                                conditionValindex++;
	                            }
	                            else if(annotationForm.getConditionVal().length <= entityMapConditions.size())
	                            {//if previously added conditions were more than current one then deassociate previous 
	                                EntityMapCondition condn = entityMapCondIter.next();
	                                condn.setFormContext(null);
	                            }
	                        }
	                    }
	                    //previously no condition exists but now conditions are added
	                    if(annotationForm.getConditionVal()!= null && annotationForm.getConditionVal().length > conditionValindex)
	                    {
	                        while (annotationForm.getConditionVal().length > conditionValindex)
	                        {
	                            EntityMapCondition condn = new EntityMapCondition();
	                            condn.setStaticRecordId(Long.valueOf(annotationForm.getConditionVal()[conditionValindex]));
	                            condn.setFormContext(formContext);
	                            condn.setTypeId(Long.valueOf(catissueCoreCacheManager.getObjectFromCache(
	                                    AnnotationConstants.COLLECTION_PROTOCOL_ENTITY_ID).toString()));
	                            entityMapConditions.add(condn);
	                            conditionValindex++;
	                        }
	                    }
	                    formContext.setEntityMapConditionCollection(entityMapConditions);
	                    currFormColl.add(formContext);
	                    }
	                }
                }
                entityMap.setFormContextCollection(currFormColl);
                bizLogic.updateEntityMap(entityMap);
            }               
        }
    }
    
}