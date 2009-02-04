/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author 
 *@version 1.0
 */ 
package edu.wustl.catissuecore.annotations;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;





public class SCGAnnotationCondition implements ICPCondition
{
    
    public  List  getCollectionProtocolList(Long entityInstanceId) 
    {
        List<Long> annotationsList = new ArrayList<Long>();
        DefaultBizLogic bizLogic = new DefaultBizLogic();
        List objectList = new ArrayList();
        try
        {
            if(entityInstanceId!=null || !entityInstanceId.equals(""))
                objectList = bizLogic.retrieve(SpecimenCollectionGroup.class.getName(),"id",entityInstanceId);
            if(objectList!=null && !objectList.isEmpty())
            {
                SpecimenCollectionGroup scg = (SpecimenCollectionGroup) objectList.get(0);
                CollectionProtocolRegistration cpReg = (CollectionProtocolRegistration)scg.getCollectionProtocolRegistration();
                if(cpReg!=null && cpReg.getCollectionProtocol()!=null && cpReg.getCollectionProtocol().getId()!=null )
                    annotationsList.add(cpReg.getCollectionProtocol().getId());
                
            }
        }
        catch(DAOException e){}
        
        return annotationsList;
    }

}

