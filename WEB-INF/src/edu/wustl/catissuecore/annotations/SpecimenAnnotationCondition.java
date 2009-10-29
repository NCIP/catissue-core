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
import edu.wustl.common.util.logger.Logger;

public class SpecimenAnnotationCondition implements ICPCondition
{

	private transient final Logger logger = Logger
			.getCommonLogger(SpecimenAnnotationCondition.class);

	/**
	 *Returns the list Of collection protocol with which the given specimen is registerd 
	 */
	public List getCollectionProtocolList(Long entityInstanceId)
	{
		final List<Long> annotationsList = new ArrayList<Long>();
		final DefaultBizLogic bizLogic = new DefaultBizLogic();
		try
		{
			if (entityInstanceId != null || !entityInstanceId.equals(""))
			{
				final SpecimenCollectionGroup specimenCollecetionGrp = (SpecimenCollectionGroup) bizLogic
						.retrieveAttribute(Specimen.class.getName(), entityInstanceId,
								"specimenCollectionGroup");

				if (specimenCollecetionGrp != null)
				{
					final CollectionProtocol collectionProtocol = (CollectionProtocol) bizLogic
							.retrieveAttribute(SpecimenCollectionGroup.class.getName(),
									specimenCollecetionGrp.getId(),
									"collectionProtocolRegistration.collectionProtocol");

					if (collectionProtocol != null && collectionProtocol.getId() != null)
					{
						annotationsList.add(collectionProtocol.getId());
					}
				}
			}
		}
		catch (final BizLogicException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return annotationsList;
	}

}
