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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.bizlogic.test.BaseTestCaseUtility;
import edu.wustl.catissuecore.bizlogic.test.TestCaseUtility;
import edu.wustl.catissuecore.domain.Participant;
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
		set.add(BaseTestCaseUtility.initBioHazard());
		set.add(BaseTestCaseUtility.initCancerResearchGrp());
		set.add(BaseTestCaseUtility.initCellSpecimen());
		set.add(BaseTestCaseUtility.initCollectionProtocol());
		set.add(BaseTestCaseUtility.initCollectionProtocolEvent());
		Participant participant = BaseTestCaseUtility.initParticipant();
		set.add(participant);
		set.add(BaseTestCaseUtility.initCollectionProtocolRegistration(participant));
		set.add(BaseTestCaseUtility.initDeIdentifiedSurgicalPathologyReport());
		set.add(BaseTestCaseUtility.initDepartment());
		set.add(BaseTestCaseUtility.initDistribution());
		set.add(BaseTestCaseUtility.initDistributionProtocol());
		set.add(BaseTestCaseUtility.initDistributionSpecimenRequirement());
		set.add(BaseTestCaseUtility.initFluidSpecimen());
		set.add(BaseTestCaseUtility.initIdentifiedSurgicalPathologyReport());
		set.add(BaseTestCaseUtility.initInstitution());
		set.add(BaseTestCaseUtility.initMolecularSpecimen());
		set.add(BaseTestCaseUtility.initOrder());
		set.add(BaseTestCaseUtility.initParticipantWithCPR());
		set.add(BaseTestCaseUtility.initSCG());
		set.add(BaseTestCaseUtility.initSite());

		set.add(BaseTestCaseUtility.initSpecimenArray());
		set.add(BaseTestCaseUtility.initSpecimenCollectionGroup());
		set.add(BaseTestCaseUtility.initSpecimenSpecimenArrayType());
		set.add(BaseTestCaseUtility.initStorageContainer());

		set.add(BaseTestCaseUtility.initStorageContainerHoldsTissueSpec());
		set.add(BaseTestCaseUtility.initStorageType());
		set.add(BaseTestCaseUtility.initTissueSpecimen());
		set.add(BaseTestCaseUtility.initTissueStorageType());
		set.add(BaseTestCaseUtility.initUser());

		String s = encodeCollection(set);
		System.out.println(s);
		Collection objectSet = getObjectList(s);
		
		
	}
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
		FileInputStream fis= new FileInputStream(xmlFile);
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
