/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.krishagni.catissueplus.bulkoperator.metadata.BulkOperationMetadata;
import com.krishagni.catissueplus.bulkoperator.metadata.BulkOperationMetadataUtil;
import com.krishagni.catissueplus.bulkoperator.templateImport.XmlRulesModule;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

public class BulkOperator
{

	/**
	 * logger Logger - Generic logger.
	 */
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir") + "/conf");
	}
	/**
	 * logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(BulkOperator.class);
	/**
	 * metadata.
	 */
	protected transient BulkOperationMetadata metadata;

	/**
	 * @return the metadata
	 */
	public BulkOperationMetadata getMetadata()
	{
		return metadata;
	}

	public BulkOperator(String xmlTemplateFilePath, String mappingFilePath)
			throws BulkOperationException
	{
		try
		{
			BulkOperationMetadataUtil bulkOperationMetadataUtil = new BulkOperationMetadataUtil();
			this.metadata = bulkOperationMetadataUtil.unmarshall(xmlTemplateFilePath,
					mappingFilePath);
		}
		catch (BulkOperationException exp)
		{
			logger.debug(exp.getMessage(), exp);
			throw new BulkOperationException(exp.getErrorKey(), exp, exp.getMsgValues());
		}
	}

	public BulkOperator(InputSource xmlTemplate, String mappingFile)
			throws BulkOperationException
	{
		try
		{
			DigesterLoader digesterLoader = DigesterLoader.newLoader(new XmlRulesModule(mappingFile));
			Digester digester = digesterLoader.newDigester();
            this.metadata = digester.parse(xmlTemplate);
		}
		catch (IOException e) {
			logger.debug(e.getMessage(), e);
			throw new BulkOperationException(e.getMessage());
		} catch (SAXException e) {
			logger.debug(e.getMessage(), e);
			throw new BulkOperationException(e.getMessage());
		}
	}
}