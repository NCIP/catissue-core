package edu.wustl.catissuecore.bizlogic.ccts;

import javax.xml.bind.JAXBElement;

import edu.duke.cabig.c3pr.webservice.common.BiologicEntity;
import edu.duke.cabig.c3pr.webservice.common.BiologicEntityIdentifier;
import edu.duke.cabig.c3pr.webservice.common.Subject;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.ccts.DataQueue;
import edu.wustl.common.exception.BizLogicException;

/**
 * Defines an interface to perform conversion of participant data received from
 * the Subject Management service into local {@link Participant}.
 * 
 * @author Denis G. Krylov
 * 
 */
public interface IParticipantConverter {

	public abstract void convert(Participant participant,
			final BiologicEntity bioEntity);

	public abstract void convert(Participant participant,
			final BiologicEntity bioEntity, IErrorsReporter errorsReporter);

	public abstract void convert(Participant p, DataQueue item,
			IErrorsReporter errorsReporter);

	public abstract Participant convert(DataQueue item,
			IErrorsReporter errorsReporter);

	public abstract JAXBElement<Subject> convert(Participant p);

	public abstract BiologicEntityIdentifier convertPMI(ParticipantMedicalIdentifier id) ;

}