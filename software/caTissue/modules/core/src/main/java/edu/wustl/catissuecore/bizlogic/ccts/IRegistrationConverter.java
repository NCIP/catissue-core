/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.bizlogic.ccts;

import javax.xml.bind.JAXBElement;

import edu.duke.cabig.c3pr.webservice.common.DocumentIdentifier;
import edu.duke.cabig.c3pr.webservice.common.OrganizationIdentifier;
import edu.duke.cabig.c3pr.webservice.common.SubjectIdentifier;
import edu.duke.cabig.c3pr.webservice.subjectregistration.StudySubject;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.ccts.DataQueue;

/**
 * Defines an interface to perform conversion of registration data received from
 * the Subject Management service into local
 * {@link CollectionProtocolRegistration}.
 * 
 * @author Denis G. Krylov
 * 
 */
public interface IRegistrationConverter {

	public abstract CollectionProtocolRegistration convert(DataQueue item,
			IErrorsReporter errorsReporter);

	public abstract SubjectIdentifier convertToSubjectIdentifier(String pmi, CollectionProtocol cp);

	public abstract DocumentIdentifier convertToDocumentIdentifier(CollectionProtocol cp);

	public abstract OrganizationIdentifier convertToOrganizationIdentifier(Site site);

	public abstract JAXBElement<StudySubject> convert(CollectionProtocolRegistration cpr);

}