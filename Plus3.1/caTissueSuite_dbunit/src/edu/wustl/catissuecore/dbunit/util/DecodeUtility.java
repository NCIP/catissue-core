/**
 * @Class DecodeUtility.java
 * @Author abhijit_naik
 * @Created on Aug 27, 2008
 */
package edu.wustl.catissuecore.dbunit.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


/**
 * @author abhijit_naik
 *
 */
public class DecodeUtility
{
	public static void main(String [] args) throws Exception
	{
		Logger.configure();
		Collection set = new HashSet();
		Specimen sp1 = new Specimen();
		sp1.setId(Long.valueOf(1));
		sp1.setLabel("label11");
		sp1.setBarcode("1");
		sp1.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		set.add(sp1);
		sp1 = new Specimen();
		sp1.setId(Long.valueOf(12));
		sp1.setLabel("label2333");
		sp1.setBarcode("12");
		sp1.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		set.add(sp1);
//		addObjectToSet(set,BaseTestCaseUtility.initUser());
//
//		addObjectToSet(set,BaseTestCaseUtility.initBioHazard());
//		addObjectToSet(set,BaseTestCaseUtility.initCancerResearchGrp());
//		addObjectToSet(set,BaseTestCaseUtility.initCellSpecimen());
//		addObjectToSet(set,BaseTestCaseUtility.initCollectionProtocol());
//		addObjectToSet(set,BaseTestCaseUtility.initCollectionProtocolEvent());
//		Participant participant = BaseTestCaseUtility.initParticipant();
//		set.add(participant);
//		addObjectToSet(set,BaseTestCaseUtility.initCollectionProtocolRegistration(participant));
//		addObjectToSet(set,BaseTestCaseUtility.initDepartment());
//		addObjectToSet(set,BaseTestCaseUtility.initDistribution());
//		addObjectToSet(set,BaseTestCaseUtility.initDistributionProtocol());
//		addObjectToSet(set,BaseTestCaseUtility.initDistributionSpecimenRequirement());
//		addObjectToSet(set,BaseTestCaseUtility.initFluidSpecimen());
//
//		addObjectToSet(set,BaseTestCaseUtility.initInstitution());
//		addObjectToSet(set,BaseTestCaseUtility.initMolecularSpecimen());
//		addObjectToSet(set,BaseTestCaseUtility.initOrder());
//		addObjectToSet(set,BaseTestCaseUtility.initParticipantWithCPR());
//		addObjectToSet(set,BaseTestCaseUtility.initSCG());
//		addObjectToSet(set,BaseTestCaseUtility.initSite());
//
//		addObjectToSet(set,BaseTestCaseUtility.initSpecimenArray());
//		addObjectToSet(set,BaseTestCaseUtility.initSpecimenCollectionGroup());
//		addObjectToSet(set,BaseTestCaseUtility.initSpecimenSpecimenArrayType());
//		addObjectToSet(set,BaseTestCaseUtility.initStorageContainer());
//
//		addObjectToSet(set,BaseTestCaseUtility.initStorageContainerHoldsTissueSpec());
//		addObjectToSet(set,BaseTestCaseUtility.initStorageType());
//		addObjectToSet(set,BaseTestCaseUtility.initTissueSpecimen());
//		addObjectToSet(set,BaseTestCaseUtility.initTissueStorageType());
//		addObjectToSet(set,BaseTestCaseUtility.initIdentifiedSurgicalPathologyReport());
//		addObjectToSet(set,BaseTestCaseUtility.initDeIdentifiedSurgicalPathologyReport());

		String s = encodeCollection(set);
		System.out.println(s);
		Collection objectSet = getObjectList(s);
		
		
	}
	
//	private static void addObjectToSet(Collection cl,AbstractDomainObject domainObject)
//	{
//		cl.add(domainObject);
//		TestCaseUtility.setObjectMap(domainObject, domainObject.getClass());
//	}
	/**
	 * @param set
	 * @return
	 * @throws IOException
	 */
	private static String encodeCollection(Collection set) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(baos);
		encoder.writeObject(set);
		baos.close();
		encoder.close();
		String s = baos.toString();
		return s;
	}
	public static Collection getObjectList(String xmlText) throws Exception
	{
		ByteArrayInputStream bais= new ByteArrayInputStream(xmlText.getBytes());
		return decodeObject(bais);
		
	}
	public static Collection getObjectListFromFile(String xmlFile) throws Exception
	{
		InputStream fis=  DecodeUtility.class.getClassLoader().getResourceAsStream(xmlFile);
		return decodeObject(fis);
		
	}

	/**
	 * @param bais
	 * @return
	 * @throws IOException
	 */
	private static Collection decodeObject(InputStream bais) throws IOException
	{
		XMLDecoder decoder = new XMLDecoder(bais);
		Collection col = (Collection)decoder.readObject();
		bais.close();
		return col;
	}
}
