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

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;



public class SpecimenAnnotationCondition implements ICPCondition
{
    
    /**
     *Returns the list Of collection protocol with which the given specimen is registerd 
     */
    public  List  getCollectionProtocolList(Long entityInstanceId) 
    {
        List<Long> annotationsList = new ArrayList<Long>();
        DefaultBizLogic bizLogic = new DefaultBizLogic();
        List objectList = new ArrayList();
        try
        {
            if(entityInstanceId!=null || !entityInstanceId.equals(""))
            {
                CollectionProtocol  collectionProtocol=(CollectionProtocol) bizLogic.retrieveAttribute(Specimen.class.getName(),entityInstanceId,"specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol");
                if(collectionProtocol != null && collectionProtocol.getId() != null)
                annotationsList.add(collectionProtocol.getId());
            }
        /*    if(objectList!=null && !objectList.isEmpty())
            {
                Specimen specimen = (Specimen) objectList.get(0);
                if(specimen.getSpecimenCollectionGroup()!=null)
                {
                    SpecimenCollectionGroup spg =(SpecimenCollectionGroup)   specimen.getSpecimenCollectionGroup();
                    if(spg.getCollectionProtocolRegistration()!=null)
                    {                           
                        CollectionProtocolRegistration cpReg = (CollectionProtocolRegistration)spg.getCollectionProtocolRegistration();
                        if(cpReg!=null && cpReg.getCollectionProtocol()!=null && cpReg.getCollectionProtocol().getId()!=null )
                            annotationsList.add(cpReg.getCollectionProtocol().getId());
                    }
                }
            }*/
        }
        catch(DAOException e){}
        
        return annotationsList;
    }

}

