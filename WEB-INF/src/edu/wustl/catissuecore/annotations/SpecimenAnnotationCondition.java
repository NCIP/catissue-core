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
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.exception.DAOException;

public class SpecimenAnnotationCondition implements ICPCondition
{

	/**
	 *Returns the list Of collection protocol with which the given specimen is registerd 
	 */
	public List getCollectionProtocolList(Long entityInstanceId)
	{
		List<Long> annotationsList = new ArrayList<Long>();
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		try
		{
			if (entityInstanceId != null || !entityInstanceId.equals(""))
			{
				SpecimenCollectionGroup specimenCollecetionGrp = (SpecimenCollectionGroup) bizLogic
						.retrieveAttribute(Specimen.class.getName(), entityInstanceId,
								"specimenCollectionGroup");

				if (specimenCollecetionGrp != null)
				{
					CollectionProtocol collectionProtocol = (CollectionProtocol) bizLogic
							.retrieveAttribute(SpecimenCollectionGroup.class.getName(),
									specimenCollecetionGrp.getId(),
									"collectionProtocolRegistration.collectionProtocol");

					if (collectionProtocol != null && collectionProtocol.getId() != null)
						annotationsList.add(collectionProtocol.getId());
				}
			}
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
		}

		return annotationsList;
	}

}
