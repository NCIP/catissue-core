/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import edu.duke.cabig.c3pr.webservice.common.BiologicEntity;
import edu.duke.cabig.c3pr.webservice.common.BiologicEntityIdentifier;
import edu.duke.cabig.c3pr.webservice.common.Organization;
import edu.duke.cabig.c3pr.webservice.common.OrganizationIdentifier;
import edu.duke.cabig.c3pr.webservice.common.Person;
import edu.duke.cabig.c3pr.webservice.common.Subject;
import edu.duke.cabig.c3pr.webservice.iso21090.CD;
import edu.duke.cabig.c3pr.webservice.iso21090.DSETCD;
import edu.duke.cabig.c3pr.webservice.iso21090.DSETENPN;
import edu.duke.cabig.c3pr.webservice.iso21090.ENPN;
import edu.duke.cabig.c3pr.webservice.iso21090.ENXP;
import edu.duke.cabig.c3pr.webservice.iso21090.EntityNamePartType;
import edu.duke.cabig.c3pr.webservice.iso21090.II;
import edu.duke.cabig.c3pr.webservice.iso21090.NullFlavor;
import edu.wustl.catissuecore.bizlogic.ISiteBizLogic;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.ccts.DataQueue;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;

/**
 * Converts {@link Subject} instances into {@link Participant} instances and
 * back.
 * 
 * @author Denis G. Krylov
 * 
 */
public final class ParticipantConverter implements IDataConverter<Participant>,
		IParticipantConverter {

	private static final String MRN = "MRN";
	private static final String CTEP = "CTEP";
	public static final String ACTIVITY_STATUS_DISABLED = "Disabled";
	public static final String ACTIVITY_STATUS_ACTIVE = "Active";
	public static final String VITAL_STATUS_ALIVE = "Alive";
	public static final String VITAL_STATUS_DEAD = "Dead";
	public static final String VITAL_STATUS_UNSPECIFIED = "Unspecified";
	public static final String GENDER_UNSPECIFIED = VITAL_STATUS_UNSPECIFIED;
	public static final String NAME_SEP = " ";
	public static final String FAM = "FAM";
	public static final String GIV = "GIV";

	public static final Map<String, String> genderMap = new LinkedHashMap<String, String>();
	public static final Map<String, String> raceMap = new LinkedHashMap<String, String>();
	public static final Map<String, String> ethniticyMap = new LinkedHashMap<String, String>();
	private static final Logger logger = Logger
			.getCommonLogger(ParticipantConverter.class);
	private static final ISO21090Helper h = null;
	private static final ThreadLocal<IErrorsReporter> ierHolder = new ThreadLocal<IErrorsReporter>();
	static {
		// gender
		genderMap.put("Male", "Male Gender");
		genderMap.put("Female", "Female Gender");
		genderMap.put("Not Reported", GENDER_UNSPECIFIED);
		genderMap.put("Unknown", "Unknown");

		// race
		raceMap.put("Asian", "Asian");
		raceMap.put("White", "White");
		raceMap.put("Black or African American", "Black or African American");
		raceMap.put("American Indian or Alaska Native",
				"American Indian or Alaska Native");
		raceMap.put("Native Hawaiian or Pacific Islander",
				"Native Hawaiian or Other Pacific Islander");
		raceMap.put("Not Reported", "Not Reported");
		raceMap.put("Unknown", "Unknown");

		// ethnicity
		ethniticyMap.put("Hispanic or Latino", "Hispanic or Latino");
		ethniticyMap.put("Not Reported", "Not Reported");
		ethniticyMap.put("Not Hispanic or Latino", "Not Hispanic or Latino");
		ethniticyMap.put("Non Hispanic or Latino", "Not Hispanic or Latino");
		ethniticyMap.put("Unknown", "Unknown");
	}

	private ISiteBizLogic siteBizLogic;

	@Override
	public Class<Participant> supports(DataQueue item) {
		if (item.isParticipantRelated())
			return Participant.class;
		return null;
	}

	@Override
	public Participant convert(DataQueue item, IErrorsReporter errorsReporter) {
		Participant participant = new Participant();
		convert(participant, item, errorsReporter);
		return participant;
	}

	@Override
	public void convert(Participant p, DataQueue item,
			IErrorsReporter errorsReporter) {
		ierHolder.set(errorsReporter);
		try {
			Subject subject = convertToSubject(item);
			if (subject != null) {
				convert(p, subject);
			}
			p.setGridId(item.getNotification().getObjectIdValue());
		} catch (Exception e) {
			throw new ProcessingException(e);
		} finally {
			ierHolder.remove();
		}
	}

	private void convert(Participant participant, Subject subject) {
		final BiologicEntity bioEntity = subject.getEntity();
		convert(participant, bioEntity);
		participant.setActivityStatus(getActivityStatus(subject));
	}

	/**
	 * @param participant
	 * @param bioEntity
	 */
	@Override
	public void convert(Participant participant, final BiologicEntity bioEntity) {
		// ACTIVE by default.
		participant.setActivityStatus(ACTIVITY_STATUS_ACTIVE);
		if (bioEntity != null) {
			Person person = (Person) bioEntity;
			// gender
			CD gender = person.getAdministrativeGenderCode();
			participant.setGender(!h.isNull(gender) ? resolveGenderCode(gender)
					: GENDER_UNSPECIFIED);
			// birth date
			participant.setBirthDate(h.toDate(person.getBirthDate()));
			participant.setDeathDate(h.toDate(person.getDeathDate()));
			participant
					.setVitalStatus(!h.isNull(person.getDeathIndicator()) ? resolveVitalStatus(person
							.getDeathIndicator().isValue())
							: VITAL_STATUS_UNSPECIFIED);
			participant
					.setEthnicity(resolveEthnicity(getEthnicGroupCode(person)));
			participant
					.setFirstName(StringUtils.isEmpty(getFirstName(person)) ? ""
							: getFirstName(person));
			participant
					.setLastName(StringUtils.isEmpty(getLastName(person)) ? ""
							: getLastName(person));
			participant.setMiddleName(StringUtils
					.isEmpty(getMiddleName(person)) ? ""
					: getMiddleName(person));

			// handle race collection
			if (participant.getRaceCollection() == null) {
				participant.setRaceCollection(new LinkedHashSet());
			} else {
				participant.getRaceCollection().clear();
			}
			participant.getRaceCollection().addAll(
					getRaceCodes(person, participant));
			// participant.setRaceCollection(getRaceCodes(person, participant));

			final Metaphone metaPhoneObj = new Metaphone();
			final String lNameMetaPhone = metaPhoneObj.metaphone(participant
					.getLastName());
			participant.setMetaPhoneCode(lNameMetaPhone);

			// Let's try to process MRNs from C3PR as well. Currently, there is
			// no way to establish a mapping
			// between C3PR's assigning organization and caTissue's Site, so we
			// will have to simply report MRNs as warning messages asking the
			// user
			// to enter them manually.
			for (ParticipantIdentifier pid : getParticipantIdentifiers(person)) {
				addParticipantIdentifier(participant, pid);
			}

		}
	}

	private String resolveEthnicity(String ethnicGroupCode) {
		if (StringUtils.isNotBlank(ethnicGroupCode)) {
			String localCode = lowerCaseKeys(ethniticyMap).get(
					ethnicGroupCode.toLowerCase());
			if (localCode == null) {
				error("Encountered unsupported ethnicity code: "
						+ ethnicGroupCode);
			} else {
				return localCode;
			}
		}
		return "";
	}
	
	private String reverseResolveEthnicity(String ethnicGroupCode) {
		return reverseResolve(ethniticyMap, ethnicGroupCode);
	}
	

	private Map<String, String> lowerCaseKeys(Map<String, String> map) {
		Map<String, String> newMap = new HashMap<String, String>();
		for (String key : map.keySet()) {
			newMap.put(key.toLowerCase(), map.get(key));
		}
		return newMap;
	}

	private void addParticipantIdentifier(Participant participant,
			final ParticipantIdentifier pid) {
		if (participant.getParticipantMedicalIdentifierCollection() == null) {
			participant
					.setParticipantMedicalIdentifierCollection(new LinkedHashSet<ParticipantMedicalIdentifier>());
		}
		if (!MRN.equalsIgnoreCase(pid.type)) {
			error("Participant identifiers of type "
					+ pid.type
					+ " are not supported in caTissue. Identifier "
					+ pid.value
					+ " cannot be assigned. Please resolve this problem manually.");
			return;
		}
		Site site = null;
		try {
			site = siteBizLogic.getSiteByCtepId(pid.orgCtepId);
		} catch (BizLogicException e) {
			logger.error(e.toString(), e);
		}
		if (site == null) {
			error("MRN of "
					+ pid.value
					+ " cannot be assigned to the participant, because a site with CTEP ID of "
					+ pid.orgCtepId
					+ " cannot be found. Please resolve this problem manually by creating a site first.");
			return;
		}
		// go through existing identifiers and trash those that have the same
		// site. Be careful and not trash those
		// that we might have just added!
		final Site finalSite = site;
		CollectionUtils.filter(
				participant.getParticipantMedicalIdentifierCollection(),
				new Predicate() {
					@Override
					public boolean evaluate(Object arg0) {
						ParticipantMedicalIdentifier pmi = (ParticipantMedicalIdentifier) arg0;
						return !(finalSite.equals(pmi.getSite()) && pmi.getId() != null);
					}
				});

		ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
		pmi.setMedicalRecordNumber(pid.value);
		pmi.setSite(site);
		pmi.setParticipant(participant);
		participant.getParticipantMedicalIdentifierCollection().add(pmi);

	}

	private List<ParticipantIdentifier> getParticipantIdentifiers(Person person) {
		List<ParticipantIdentifier> list = new ArrayList<ParticipantIdentifier>();
		List<BiologicEntityIdentifier> identifiers = person
				.getBiologicEntityIdentifier();
		if (CollectionUtils.isNotEmpty(identifiers)) {
			for (BiologicEntityIdentifier bioId : identifiers) {
				final II ii = bioId.getIdentifier();
				final CD typeCode = bioId.getTypeCode();
				String value = h.str(ii);
				String typeCodeStr = h.str(typeCode);
				String orgCtepId = getOrganizationCtepId(bioId
						.getAssigningOrganization());
				if (StringUtils.isNotBlank(value)
						&& StringUtils.isNotBlank(typeCodeStr)
						&& StringUtils.isNotBlank(orgCtepId)) {
					list.add(new ParticipantIdentifier(value, typeCodeStr,
							orgCtepId));
				} else {
					// error("Incomplete participant identifier information has been received. All of site's CTEP ID, identifier value, and identifier type have to be specified.");
				}
			}
		}
		return list;
	}

	private String getOrganizationCtepId(Organization org) {
		String ctepId = "";
		if (org != null) {
			for (OrganizationIdentifier oid : org.getOrganizationIdentifier()) {
				String value = h.str(oid.getIdentifier());
				String typeCodeStr = h.str(oid.getTypeCode());
				if (CTEP.equalsIgnoreCase(typeCodeStr)) {
					ctepId = value;
				}
			}
		}
		return ctepId;
	}

	private String getActivityStatus(Subject subject) {
		if (ACTIVITY_STATUS_ACTIVE.equalsIgnoreCase(h.str(subject
				.getStateCode()))) {
			return ACTIVITY_STATUS_ACTIVE;
		}
		return ACTIVITY_STATUS_DISABLED;
	}

	private String resolveVitalStatus(Boolean deathIndicator) {
		return Boolean.TRUE.equals(deathIndicator) ? VITAL_STATUS_DEAD
				: VITAL_STATUS_ALIVE;
	}

	/**
	 * @param gender
	 * @return
	 */
	private String resolveGenderCode(CD gender) {
		final String cd = (String) ObjectUtils.defaultIfNull(gender.getCode(),
				GENDER_UNSPECIFIED);
		final String localGender = lowerCaseKeys(genderMap).get(
				cd.toLowerCase());
		if (localGender == null) {
			error("Encountered unsupported gender code: " + cd);
		}
		return localGender != null ? localGender : GENDER_UNSPECIFIED;

	}

	private String resolveGenderCode(String gender) {
		return reverseResolve(genderMap, gender);
	}
	
	private String resolveRaceCode(String race) {
		return reverseResolve(raceMap, race);
	}
	

	private String reverseResolve(Map<String, String> map, String value) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue().equalsIgnoreCase(value)) {
				return entry.getKey();
			}
		}
		return value;
	}

	private Subject convertToSubject(DataQueue item) throws JAXBException {
		String xml = item.getPayload();
		Subject subject = null;
		if (StringUtils.isNotBlank(xml)) {
			JAXBContext jc = JAXBContext.newInstance(Subject.class, CD.class);
			Unmarshaller m = jc.createUnmarshaller();
			JAXBElement<Subject> element = m.unmarshal(new StreamSource(
					new StringReader(xml)), Subject.class);
			subject = element.getValue();
		} else {
			error("The data queue entry has an empty XML payload. Conversion cannot be performed.");
		}
		return subject;
	}

	protected String getEthnicGroupCode(Person person) {
		DSETCD codes = person.getEthnicGroupCode();
		return getFirstCode(codes);
	}

	/**
	 * @param codes
	 * @return
	 */
	protected String getFirstCode(DSETCD codes) {
		String code = null;
		if (codes != null && CollectionUtils.isNotEmpty(codes.getItem())) {
			code = codes.getItem().get(0).getCode();
		}
		return code;
	}

	protected String getFirstName(Person person) {
		String name = "";
		List<String> list = extractNameParts(person, EntityNamePartType.GIV);
		if (CollectionUtils.isNotEmpty(list)) {
			name = list.get(0);
		}
		return name;
	}

	protected List<String> extractNameParts(Person person,
			EntityNamePartType type) {
		List<String> list = new ArrayList<String>();
		DSETENPN parts = person.getName();
		if (parts != null && CollectionUtils.isNotEmpty(parts.getItem())) {
			ENPN nameEntry = parts.getItem().get(0);
			if (nameEntry.getPart() != null) {
				for (ENXP nameEntryPart : nameEntry.getPart()) {
					if (type.equals(nameEntryPart.getType())) {
						list.add(nameEntryPart.getValue());
					}
				}
			}
		}
		return list;
	}

	protected String getLastName(Person person) {
		String name = "";
		List<String> list = extractNameParts(person, EntityNamePartType.FAM);
		if (CollectionUtils.isNotEmpty(list)) {
			name = StringUtils.join(list, NAME_SEP);
		}
		return name;
	}

	protected String getMiddleName(Person person) {
		String name = "";
		List<String> list = extractNameParts(person, EntityNamePartType.GIV);
		if (CollectionUtils.isNotEmpty(list) && list.size() > 1) {
			name = list.get(1);
		}
		return name;
	}

	protected Collection<Race> getRaceCodes(Person person,
			Participant participant) {
		Collection<Race> list = new LinkedHashSet<Race>();
		DSETCD dsetcd = person.getRaceCode();
		if (!h.isNull(dsetcd) && dsetcd.getItem() != null) {
			for (CD cd : dsetcd.getItem()) {
				String raceCodeStr = cd.getCode();
				String localRaceCode = lowerCaseKeys(raceMap).get(
						(raceCodeStr + "").toLowerCase());
				if (localRaceCode != null) {
					Race race = new Race();
					race.setRaceName(localRaceCode);
					race.setParticipant(participant);
					list.add(race);
				} else {
					error("Encountered unsupported race code: " + raceCodeStr);
					logger.error("Encountered unsupported race code: "
							+ raceCodeStr);
				}
			}
		}
		return list;
	}

	private void error(String msg) {
		if (ierHolder.get() != null) {
			ierHolder.get().error(msg);
		}
	}

	private static final class ParticipantIdentifier implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String value;
		private String type;
		private String orgCtepId;

		private ParticipantIdentifier(String value, String type,
				String orgCtepId) {
			super();
			this.value = StringUtils.trim(value);
			this.type = StringUtils.trim(type);
			this.orgCtepId = StringUtils.trim(orgCtepId);
		}

	}

	/**
	 * @return the siteBizLogic
	 */
	public final ISiteBizLogic getSiteBizLogic() {
		return siteBizLogic;
	}

	/**
	 * @param siteBizLogic
	 *            the siteBizLogic to set
	 */
	public final void setSiteBizLogic(ISiteBizLogic siteBizLogic) {
		this.siteBizLogic = siteBizLogic;
	}

	@Override
	public void convert(Participant participant, BiologicEntity bioEntity,
			IErrorsReporter errorsReporter) {
		ierHolder.set(errorsReporter);
		try {
			convert(participant, bioEntity);
		} finally {
			ierHolder.remove();
		}

	}

	@Override
	public JAXBElement<Subject> convert(Participant p) {
		Subject subject = new Subject();
		if (p != null) {
			Person person = new Person();
			subject.setEntity(person);
			for (ParticipantMedicalIdentifier id : new LinkedHashSet<ParticipantMedicalIdentifier>(
					p.getParticipantMedicalIdentifierCollection())) {

				BiologicEntityIdentifier bioId = convertPMI(id);
				person.getBiologicEntityIdentifier().add(bioId);
			}
			person.setAdministrativeGenderCode(p.getGender() != null ? h
					.CD(resolveGenderCode(p.getGender())) : h.CD(NullFlavor.NI));
			person.setBirthDate(h.TSDateTime(p.getBirthDate()));
			person.setDeathDate(h.TSDateTime(p.getDeathDate()));
			person.setDeathIndicator(p.getVitalStatus() != null ? h.BL(p
					.getVitalStatus().equalsIgnoreCase(VITAL_STATUS_DEAD)) : h
					.BL(NullFlavor.NI));
			person.setEthnicGroupCode(getEthnicGroupCode(p));
			person.setName(getName(p));
			person.setRaceCode(getRaceCodes(p));
			if (p.getActivityStatus() != null) {
				subject.setStateCode(h.ST(p.getActivityStatus()));
			}
		}
		return new JAXBElement<Subject>(new QName(Constants.NES_COMMON_NS,
				"subject"), Subject.class, subject);
	}

	/**
	 * @param pmi
	 * @return
	 */
	@Override
	public BiologicEntityIdentifier convertPMI(ParticipantMedicalIdentifier pmi) {
		BiologicEntityIdentifier bioId = new BiologicEntityIdentifier();
		if (pmi != null) {
			bioId.setTypeCode(h.CD(MRN));
			bioId.setIdentifier(h.II(pmi.getMedicalRecordNumber()));
			bioId.setPrimaryIndicator(h.BL(true));
			Site site = pmi.getSite();
			try {
				if (site != null
						&& StringUtils.isNotBlank((site = siteBizLogic
								.getSiteById(site.getId())).getCtepId())) {
					Organization org = new Organization();
					OrganizationIdentifier orgId = new OrganizationIdentifier();
					orgId.setTypeCode(h.CD(CTEP));
					orgId.setIdentifier(h.II(site.getCtepId()));
					orgId.setPrimaryIndicator(h.BL(true));
					org.getOrganizationIdentifier().add(orgId);
					bioId.setAssigningOrganization(org);
				}
			} catch (BizLogicException e) {
				logger.error(e, e);
			}
		}
		return bioId;
	}

	protected DSETCD getEthnicGroupCode(Participant p) {
		DSETCD dsetcd = new DSETCD();
		if (StringUtils.isNotBlank(p.getEthnicity())) {
			dsetcd.getItem().add(h.CD(reverseResolveEthnicity(p.getEthnicity())));
		} else {
			dsetcd.setNullFlavor(NullFlavor.NI);
		}
		return dsetcd;
	}

	protected DSETENPN getName(Participant p) {
		DSETENPN dsetenpn = new DSETENPN();
		ENPN enpn = new ENPN();
		if (StringUtils.isNotBlank(p.getFirstName()))
			enpn.getPart().add(
					h.ENXP(p.getFirstName(), EntityNamePartType.valueOf(GIV)));
		if (StringUtils.isNotBlank(p.getMiddleName()))
			enpn.getPart().add(
					h.ENXP(p.getMiddleName(), EntityNamePartType.valueOf(GIV)));
		if (StringUtils.isNotBlank(p.getLastName()))
			enpn.getPart().add(
					h.ENXP(p.getLastName(), EntityNamePartType.valueOf(FAM)));
		dsetenpn.getItem().add(enpn);
		return dsetenpn;
	}

	protected DSETCD getRaceCodes(Participant p) {
		DSETCD dsetcd = new DSETCD();
		for (Race raceCode : p.getRaceCollection()) {
			dsetcd.getItem().add(h.CD(resolveRaceCode(raceCode.getRaceName())));
		}
		return dsetcd;
	}

}
