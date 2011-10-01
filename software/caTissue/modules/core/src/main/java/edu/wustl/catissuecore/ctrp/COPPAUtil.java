package edu.wustl.catissuecore.ctrp;

import java.lang.annotation.Annotation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.iso._21090.AD;
import org.iso._21090.ADXP;
import org.iso._21090.DSETTEL;
import org.iso._21090.ENXP;
import org.iso._21090.II;
import org.iso._21090.TEL;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.coppa.po.Entity;
import gov.nih.nci.coppa.po.Person;
import gov.nih.nci.coppa.services.pa.StudyProtocol;

/**
 * @author Ravi.Batchu
 * 
 */
public class COPPAUtil {
	public static final String ISO_TEL_EMAIL_TYPE = "TEL.Email";
	public static final String ISO_LAST_NAME_TYPE = "FAM";
	public static final String ISO_FIRST_NAME_TYPE = "GIV";
	public static final String ISO_STREET_ADDRESS_TYPE = "AL";
	public static final String ISO_CITY_TYPE = "CTY";
	public static final String ISO_STATE_TYPE = "STA";
	public static final String ISO_ZIP_TYPE = "ZIP";
	public static final String ISO_COUNTRY_TYPE = "CNT";
	public static final String ISO_TEL_PHONE_TYPE = "TEL.Phone";
	public static final String ISO_EMAIL_PREFIX = "mailto:";
	public static final String ISO_TEL_PHONE_PREFIX = "tel:";
	public static final String ISO_TEL_FAX_PREFIX = "x-text-fax:";
	public static final String DATE_FORMAT = "yyyyMMddHHmmss.SSSSZ";

	private static final Logger logger = Logger
			.getCommonLogger(COPPAUtil.class);

	public static String getRemoteIdentifier(Entity entity) {
		String identifier = "";
		if (entity != null) {
			identifier = entity.getIdentifier().getExtension();
		}
		return identifier;
	}

	public static String getLastName(Person person) {
		String lastname = "";
		if (person != null) {
			List<ENXP> partList = person.getName().getPart();
			for (ENXP part : partList) {
				if (ISO_LAST_NAME_TYPE.equalsIgnoreCase(part.getType().value())) {
					lastname = part.getValue();
				}
			}
		}
		return lastname;
	}

	public static String getFirstName(Person person) {
		String firstname = "";
		if (person != null) {
			List<ENXP> partList = person.getName().getPart();
			for (ENXP part : partList) {
				if (ISO_FIRST_NAME_TYPE
						.equalsIgnoreCase(part.getType().value())) {
					if (AppUtility.isEmpty(firstname)) {
						firstname = part.getValue();
					} else {
						// Concatenate Middle Name
						firstname += Constants.SPACE_STR + part.getValue();
					}
				}// if GIV name
			}// for - part list
		}
		return firstname;
	}

	public static String getStreetAddress(Person person) {
		return getStreetAddress(person.getPostalAddress());
	}

	public static String getCity(Person person) {
		return getCity(person.getPostalAddress());
	}

	public static String getState(Person person) {
		return getState(person.getPostalAddress());
	}

	public static String getCountry(Person person) {
		return getCountry(person.getPostalAddress());
	}

	public static String getZipCode(Person person) {
		return getZipCode(person.getPostalAddress());
	}

	public static String getEmailAddress(Person person) {
		return getEmail(person.getTelecomAddress());
	}

	public static String getTelephoneNumber(Person person) {
		return getTelephoneNumber(person.getTelecomAddress());
	}

	public static String getFaxNumber(Person person) {
		return getFaxNumber(person.getTelecomAddress());
	}

	public static String getTelephoneNumber(DSETTEL telecomAddress) {
		return getPhoneNumber(telecomAddress, ISO_TEL_PHONE_PREFIX);
	}

	public static String getFaxNumber(DSETTEL telecomAddress) {
		return getPhoneNumber(telecomAddress, ISO_TEL_FAX_PREFIX);
	}

	public static String getStreetAddress(AD address) {
		String streetAddress = "";
		List<ADXP> partList = address.getPart();
		for (ADXP part : partList) {
			if (ISO_STREET_ADDRESS_TYPE
					.equalsIgnoreCase(part.getType().value())) {
				streetAddress = part.getValue();
			}
		}
		return streetAddress;
	}

	public static String getCity(AD address) {
		String city = "";
		List<ADXP> partList = address.getPart();
		for (ADXP part : partList) {
			if (ISO_CITY_TYPE.equalsIgnoreCase(part.getType().value())) {
				city = part.getValue();
			}
		}
		return city;
	}

	public static String getState(AD address) {
		String state = "";
		List<ADXP> partList = address.getPart();
		for (ADXP part : partList) {
			if (ISO_STATE_TYPE.equalsIgnoreCase(part.getType().value())) {
				state = part.getValue();
			}
		}
		return state;
	}

	public static String getCountry(AD address) {
		String country = "";
		List<ADXP> partList = address.getPart();
		for (ADXP part : partList) {
			if (ISO_COUNTRY_TYPE.equalsIgnoreCase(part.getType().value())) {
				country = part.getValue();
			}
		}
		return country;
	}

	public static String getZipCode(AD address) {
		String zipCode = "";
		List<ADXP> partList = address.getPart();
		for (ADXP part : partList) {
			if (ISO_ZIP_TYPE.equalsIgnoreCase(part.getType().value())) {
				zipCode = part.getValue();
			}
		}
		return zipCode;
	}

	public static String getEmail(DSETTEL telecomAddress) {
		String email = "";
		boolean emailTagFound = false;
		if (telecomAddress != null) {
			List<TEL> itemList = telecomAddress.getItem();
			for (TEL item : itemList) {
				Annotation[] tcomAnnotations = item.getClass().getAnnotations();
				for (Annotation a : tcomAnnotations) {
					if (a instanceof XmlType) {
						XmlType xmlType = (XmlType) a;
						if (ISO_TEL_EMAIL_TYPE.equalsIgnoreCase(xmlType.name())) {
							emailTagFound = true;
						}// if Tel.Email
					}// if xmlType
				}// for annotation
				if (emailTagFound) {
					email = item.getValue();
					if (email.indexOf(ISO_EMAIL_PREFIX) > -1) {
						email = email.substring(email.indexOf(ISO_EMAIL_PREFIX)
								+ ISO_EMAIL_PREFIX.length());
					}
					break;
				}// if email found
			}// for telecom item list
		}// if telecom address not null
		return email;
	}

	public static String getPhoneNumber(DSETTEL telecomAddress,
			String phonePrefixType) {
		String phoneNumber = "";
		boolean phoneNumberFound = false;
		if (telecomAddress != null) {
			List<TEL> itemList = telecomAddress.getItem();
			for (TEL item : itemList) {
				Annotation[] tcomAnnotations = item.getClass().getAnnotations();
				for (Annotation a : tcomAnnotations) {
					if (a instanceof XmlType) {
						XmlType xmlType = (XmlType) a;
						if (ISO_TEL_PHONE_TYPE.equalsIgnoreCase(xmlType.name())) {
							if (AppUtility.isNotEmpty(item.getValue())
									&& (item.getValue()
											.indexOf(phonePrefixType) > -1)) {
								phoneNumberFound = true;
								phoneNumber = item.getValue();
								phoneNumber = phoneNumber.substring(phoneNumber
										.indexOf(phonePrefixType)
										+ phonePrefixType.length());
								break;
							}// ISO_TEL_PHONE_PREFIX found
						}// if Tel.Phone
					}// if xmlType
				}// for annotation
				if (phoneNumberFound) {
					break;
				}// if email found
			}// for telecom item list
		}// if telecom address not null
		return phoneNumber;
	}

	public static String getRemoteIdentifier(StudyProtocol studyProtocol) {
		String identifier = "";
		if (studyProtocol != null) {
			identifier = studyProtocol.getIdentifier().getExtension();
		}
		return identifier;
	}

	public static String getTitle(StudyProtocol studyProtocol) {
		String title = "";
		if ((studyProtocol != null)
				&& (studyProtocol.getOfficialTitle() != null)) {
			title = studyProtocol.getOfficialTitle().getValue();
		}
		return title;
	}

	public static String getShortTitle(StudyProtocol studyProtocol) {
		String shortTitle = "";
		if ((studyProtocol != null) && (studyProtocol.getAcronym() != null)
				&& (studyProtocol.getAcronym().getValue() != null)) {
			shortTitle = studyProtocol.getAcronym().getValue();
		}
		return shortTitle;
	}

	public static String getNCIIdentitifer(StudyProtocol studyProtocol) {
		String identifier = "";
		if (studyProtocol != null) {
			List<II> secondaryIdentifierList = studyProtocol
					.getSecondaryIdentifiers().getItem();
			if (AppUtility.isNotEmpty(secondaryIdentifierList)) {
				identifier = secondaryIdentifierList.get(0).getExtension();
			}
		}
		return identifier;
	}

	/**
	 * Returns start date in mm-dd-yyyy format
	 * 
	 * @param studyProtocol
	 * @return
	 */
	public static String getStartDateStr(StudyProtocol studyProtocol) {
		String startDateStr = "";
		if ((studyProtocol != null) && (studyProtocol.getStartDate() != null)) {
			String coppaDateStr = "";
			try {
				coppaDateStr = studyProtocol.getStartDate().getValue();
				startDateStr = convertDateFormatCOPPAToCaTissue(coppaDateStr);
			} catch (ParseException e) {
				logger.error("Unparseable date:" + coppaDateStr + ":"
						+ getTitle(studyProtocol));
				return coppaDateStr;
			}
		}
		return startDateStr;
	}

	/**
	 * Returns start date in mm-dd-yyyy format
	 * 
	 * @param studyProtocol
	 * @return
	 */
	public static Date getStartDate(StudyProtocol studyProtocol) {
		Date startDate = null;
		if ((studyProtocol != null) && (studyProtocol.getStartDate() != null)) {
			String coppaDateStr = "";
			try {
				coppaDateStr = studyProtocol.getStartDate().getValue();
				startDate = parseCOPPADateFormat(coppaDateStr);
			} catch (ParseException e) {
				logger.error("Unparseable date:" + coppaDateStr + ":"
						+ getTitle(studyProtocol));
				return null;
			}
		}
		return startDate;
	}

	public static String convertDateFormatCOPPAToCaTissue(String coppaDateStr)
			throws ParseException {
		String caTissueDateStr = "";
		if (AppUtility.isEmpty(coppaDateStr)) {
			return caTissueDateStr;
		}
		SimpleDateFormat coppaDateFormat = new SimpleDateFormat(DATE_FORMAT);
		Date startDate = coppaDateFormat.parse(coppaDateStr);
		SimpleDateFormat caTissueDateFormat = new SimpleDateFormat(
				Constants.DATE_FORMAT);
		caTissueDateStr = caTissueDateFormat.format(startDate);
		return caTissueDateStr;
	}

	public static Date parseCOPPADateFormat(String coppaDateStr)
			throws ParseException {
		String caTissueDateStr = "";
		if (AppUtility.isEmpty(coppaDateStr)) {
			return null;
		}
		SimpleDateFormat coppaDateFormat = new SimpleDateFormat(DATE_FORMAT);
		Date startDate = coppaDateFormat.parse(coppaDateStr);
		return startDate;
	}

	public static boolean isCOPPAEnabled() {
		boolean isEnabledFlag = false;
		try {
			if (("Yes".equalsIgnoreCase(CTRPPropertyHandler
					.getProperty(CTRPConstants.CTRP_IS_ENABLED_KEY)))
					|| ("true".equalsIgnoreCase(CTRPPropertyHandler
							.getProperty(CTRPConstants.CTRP_IS_ENABLED_KEY)))) {
				isEnabledFlag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isEnabledFlag = false;
		}
		return isEnabledFlag;
	}

	public static String toCOPPAEmailFormat(String emailAddress) {
		if (AppUtility.isNotEmpty(emailAddress)) {
			return ISO_EMAIL_PREFIX + emailAddress;
		} else {
			return emailAddress;
		}
	}

	public static boolean compareAndSyncData(Institution localInstitution,
			Institution remoteInstitution) {
		boolean compareResult = true;
		try {
			if (!StringUtils.equals(localInstitution.getName(),
					remoteInstitution.getName())) {
				localInstitution.setName(remoteInstitution.getName());
				compareResult = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			compareResult = false;
		}
		return compareResult;
	}

	public static boolean compareAndSyncData(User localUser, User remoteUser) {
		boolean compareResult = true;
		try {
			if (!StringUtils.equals(localUser.getEmailAddress(),
					remoteUser.getEmailAddress())) {
				localUser.setEmailAddress(remoteUser.getEmailAddress());
				compareResult = false;
			}
			if (!StringUtils.equals(localUser.getFirstName(),
					remoteUser.getFirstName())) {
				localUser.setFirstName(remoteUser.getFirstName());
				compareResult = false;
			}
			if (!StringUtils.equals(localUser.getLastName(),
					remoteUser.getLastName())) {
				localUser.setLastName(remoteUser.getLastName());
				compareResult = false;
			}
			if (!compareAndSyncData(localUser.getAddress(),
					remoteUser.getAddress())) {
				compareResult = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			compareResult = false;
		}
		return compareResult;
	}

	public static boolean compareAndSyncData(Address localUserAddress,
			Address remoteUserAddress) {
		boolean compareResult = true;
		try {
			if (!StringUtils.equals(localUserAddress.getStreet(),
					remoteUserAddress.getStreet())) {
				localUserAddress.setStreet(remoteUserAddress.getStreet());
				compareResult = false;
			}
			if (!StringUtils.equals(localUserAddress.getCity(),
					remoteUserAddress.getCity())) {
				localUserAddress.setCity(remoteUserAddress.getCity());
				compareResult = false;
			}
			if (!StringUtils.equals(localUserAddress.getState(),
					remoteUserAddress.getState())) {
				localUserAddress.setState(remoteUserAddress.getState());
				compareResult = false;
			}
			if (!StringUtils.equals(localUserAddress.getCountry(),
					remoteUserAddress.getCountry())) {
				localUserAddress.setCountry(remoteUserAddress.getCountry());
				compareResult = false;
			}
			if (!StringUtils.equals(localUserAddress.getZipCode(),
					remoteUserAddress.getZipCode())) {
				localUserAddress.setZipCode(remoteUserAddress.getZipCode());
				compareResult = false;
			}
			if (!StringUtils.equals(localUserAddress.getPhoneNumber(),
					remoteUserAddress.getPhoneNumber())) {
				localUserAddress.setPhoneNumber(remoteUserAddress
						.getPhoneNumber());
				compareResult = false;
			}
			if (!StringUtils.equals(localUserAddress.getFaxNumber(),
					remoteUserAddress.getFaxNumber())) {
				localUserAddress.setFaxNumber(remoteUserAddress.getFaxNumber());
				compareResult = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			compareResult = false;
		}
		return compareResult;
	}

	public static boolean compareAndSyncData(
			CollectionProtocol localCollectionProtocol,
			CollectionProtocol remoteCollectionProtocol) {
		boolean compareResult = true;
		try {
			if (!StringUtils.equals(localCollectionProtocol.getTitle(),
					remoteCollectionProtocol.getTitle())) {
				localCollectionProtocol.setTitle(remoteCollectionProtocol
						.getTitle());
				compareResult = false;
			}
			if (!StringUtils.equals(localCollectionProtocol.getShortTitle(),
					remoteCollectionProtocol.getShortTitle())) {
				localCollectionProtocol.setShortTitle(remoteCollectionProtocol
						.getShortTitle());
				compareResult = false;
			}
			if (!DateUtils.isSameDay(localCollectionProtocol.getStartDate(),
					remoteCollectionProtocol.getStartDate())) {
				localCollectionProtocol.setStartDate(remoteCollectionProtocol
						.getStartDate());
				compareResult = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			compareResult = false;
		}
		return compareResult;
	}
}
