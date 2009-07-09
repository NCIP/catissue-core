/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author 
 *@version 1.0
 */

package edu.wustl.catissuecore.annotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;

public class ParticipantAnnotationCondition implements ICPCondition
{

	private transient final Logger logger = Logger
			.getCommonLogger( ParticipantAnnotationCondition.class );

	/**
	 * Returns the list of protocol with which the given participant is registered 
	 * 
	 */
	public List getCollectionProtocolList(Long entityInstanceId)
	{

		final List < Long > annotationsList = new ArrayList < Long >();
		final DefaultBizLogic bizLogic = new DefaultBizLogic();
		Collection objectList = new HashSet();
		try
		{
			if (entityInstanceId != null || !entityInstanceId.equals( "" ))
			{
				/*  objectList = bizLogic.retrieve(Participant.class.getName(),
				        "id", entityInstanceId);*/
				objectList = (Collection) bizLogic.retrieveAttribute( Participant.class.getName(),
						entityInstanceId, "elements(collectionProtocolRegistrationCollection)" );
			}
			if (objectList != null && !objectList.isEmpty())
			{

				// Participant participant = (Participant) objectList.get(0);
				/* Iterator it = participant
				         .getCollectionProtocolRegistrationCollection()
				         .iterator();*/
				final Iterator it = objectList.iterator();
				while (it.hasNext())
				{
					final CollectionProtocolRegistration cpReg = (CollectionProtocolRegistration) it
							.next();
					if (cpReg != null && cpReg.getCollectionProtocol() != null
							&& cpReg.getCollectionProtocol().getId() != null)
					{
						annotationsList.add( cpReg.getCollectionProtocol().getId() );
					}

				}

			}
		}
		catch (final BizLogicException e)
		{
			this.logger.debug( e.getMessage(), e );
			e.printStackTrace();
		}

		return annotationsList;
	}

}
