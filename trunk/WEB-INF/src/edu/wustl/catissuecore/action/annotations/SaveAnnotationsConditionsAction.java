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

import edu.wustl.catissuecore.actionForm.AnnotationForm;
import edu.wustl.catissuecore.annotations.AnnotationUtil;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.domain.EntityMap;
import edu.wustl.catissuecore.domain.EntityMapCondition;
import edu.wustl.common.action.BaseAction;
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

    private void saveConditions(AnnotationForm annotationForm,
            HttpServletRequest request)
    {
        String containerId = request.getParameter("containerId");
        String selectedStaticEntityId = request
                .getParameter("selectedStaticEntityId");

        AnnotationBizLogic bizLogic = new AnnotationBizLogic();
        List dynamicList = new ArrayList();
        dynamicList = bizLogic.getListOfStaticEntities(new Long(containerId));

        if (dynamicList != null && !dynamicList.isEmpty())
        {

            EntityMap entityMap = (EntityMap) dynamicList.get(0);
            Collection collPrev = entityMap.getEntityMapConditionCollection();
            Collection entityMapConditionCollectionPrev = new HashSet();
            Iterator it = collPrev.iterator();

            AnnotationUtil util = new AnnotationUtil();
            Collection entityMapConditionCollectionCurr = new HashSet();
            
            if(annotationForm
                    .getConditionVal()!=null) 
            entityMapConditionCollectionCurr = util
                    .getEntityMapConditionsCollection(annotationForm
                            .getConditionVal(), entityMap);

            while (it.hasNext())
            {
                EntityMapCondition entityMapConditionPrev = (EntityMapCondition) it
                        .next();
                //if(entityMapConditionCollectionCurr.contains(entityMapConditionPrev))
                if (checkForDuplication(entityMapConditionCollectionCurr,
                        entityMapConditionPrev))
                {       
                }
                else
                {
                    entityMapConditionPrev.setEntityMap(null);
                    entityMapConditionCollectionPrev
                            .add(entityMapConditionPrev);
                }
            }
            entityMap
                    .setEntityMapConditionCollection(entityMapConditionCollectionPrev);
            bizLogic.updateEntityMap(entityMap);

            entityMap
                    .setEntityMapConditionCollection(entityMapConditionCollectionCurr);
            bizLogic.updateEntityMap(entityMap);
        }

    }

    private boolean checkForDuplication(
            Collection entityMapConditionCollectionCurr,
            EntityMapCondition entityMapConditionPrev)
    {
        Iterator it = entityMapConditionCollectionCurr.iterator();

        while (it.hasNext())
        {
            EntityMapCondition entityMapCondnObj = (EntityMapCondition) it
                    .next();
            if (entityMapCondnObj.getStaticRecordId().equals(
                    entityMapConditionPrev.getStaticRecordId())
                    && entityMapCondnObj.getTypeId().equals(
                            entityMapConditionPrev.getTypeId()))
            {
                entityMapConditionCollectionCurr.remove(entityMapCondnObj);
                return true;
            }
        }

        return false;
    }

}
