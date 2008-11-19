package edu.wustl.catissuecore.namegenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;

/**
 * This class which contains the default implementation for Protocol 
 * Participant label generation.
 * @author sagar_baldwa
 */
 
public class ProtocolParticipantIdentifierLabelGenerator implements LabelGenerator
{
	/**
	 * 
	 */
	public String getLabel(Object object)
	{
		return null;
	}

	/**
	 * This method generates a label and sets it to CollectionProtocolRegistration
	 * domain object. Label is in the format "COLLECTIONPROTOCOLID_PARTICIPANTID"
	 */
	public void setLabel(Object object)
	{
		CollectionProtocolRegistration collectionProtocolRegistration = 
			(CollectionProtocolRegistration) object;
		if(collectionProtocolRegistration.getProtocolParticipantIdentifier() == null
				|| Constants.DOUBLE_QUOTES.equals(collectionProtocolRegistration.getProtocolParticipantIdentifier()))
		{
			collectionProtocolRegistration.setProtocolParticipantIdentifier(
					collectionProtocolRegistration.getCollectionProtocol().getId() + "_" + 
					collectionProtocolRegistration.getParticipant().getId());
		}		
	}

	/**
	 * 
	 */
	public void setLabel(List object)
	{
		// TODO Auto-generated method stub		
	}
}