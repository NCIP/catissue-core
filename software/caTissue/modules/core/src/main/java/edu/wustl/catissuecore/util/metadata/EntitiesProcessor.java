/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.util.metadata;

import javax.xml.bind.JAXBElement;

import org.xml.sax.SAXException;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.metadata.StaticMetaDataType;

public class EntitiesProcessor {


/*	public static void main(String[] args) throws DynamicExtensionsSystemException, SAXException
	{
		EntitiesProcessor entityProcessor = new EntitiesProcessor(
				);


		//entityProcessor.importPvVersionValues(args[0], args[1]);
		entityProcessor.importMetaDataValues("StaticModelMetaData.xml", "E:/workspace/caTISSUE_SUITE24may/software/caTissue/src/resources/xml");
	}
*/
	public StaticMetaDataType importMetaDataValues(final String filePath, final String baseDir) throws DynamicExtensionsSystemException, SAXException
	{
		//
		// This packageName is used for parsing XML.
		final String packageName = StaticMetaDataType.class.getPackage().getName();

	/*	final StaticMetaDataType staticMetadataType = (StaticMetaDataType) XMLUtility.getJavaObjectForXML(filePath,
				baseDir, packageName, "StaticModelMetaData.xsd");
			*/




		final JAXBElement jAXBElement = (JAXBElement) XMLUtility.getJavaObjectForXML(filePath,
				baseDir, packageName, "StaticModelMetaData.xsd");

		StaticMetaDataType staticMetadataType = (StaticMetaDataType)jAXBElement.getValue();

		return staticMetadataType;

	}



}
