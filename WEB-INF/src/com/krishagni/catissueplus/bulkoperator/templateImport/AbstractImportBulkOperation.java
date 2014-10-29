/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

/**
 *
 */

package com.krishagni.catissueplus.bulkoperator.templateImport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.xml.sax.SAXException;
import au.com.bytecode.opencsv.CSVReader;
import com.krishagni.catissueplus.bulkoperator.csv.CsvReader;
import com.krishagni.catissueplus.bulkoperator.csv.impl.CsvFileReader;
import com.krishagni.catissueplus.bulkoperator.metadata.BulkOperationClass;
import com.krishagni.catissueplus.bulkoperator.metadata.BulkOperationMetaData;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationConstants;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import com.krishagni.catissueplus.bulkoperator.validator.TemplateValidator;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.exception.DAOException;

/**
 * @author shrishail_kalshetty
 *
 */
public abstract class AbstractImportBulkOperation
{

	/**
	 * logger Logger - Generic logger.
	 */
	protected static final Logger logger = Logger
			.getCommonLogger(ImportBulkOperationTemplate.class);

	/**
	 * logger Logger - Generic logger.
	 */

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir") + "/BulkOperations/conf");
	}

	/**
	 *
	 * @param operationName
	 * @param dropdownName
	 * @param csvFile
	 * @param xmlFile
	 * @param mappingXml
	 * @return
	 * @throws BulkOperationException
	 * @throws SQLException
	 * @throws IOException
	 * @throws DAOException
	 */
	protected Set<String> validate(String operationName, String dropdownName, String csvFile,
			String xmlFile, String mappingXml,String xsdLocation) throws BulkOperationException, SQLException,
			IOException, DAOException
	{
		Set<String> errorList = null;
		CsvReader csvReader=null;
		try
		{
			csvReader=CsvFileReader.createCsvFileReader(csvFile, true);
			
		}
		catch (Exception exp)
		{
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.error.incorrect.csv.file");
			throw new BulkOperationException(errorkey, exp, "");
		}
		BulkOperationMetaData bulkOperationMetaData  = null;
		try
		{
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			File schemaLocation = new File(xsdLocation);
			Schema schema = factory.newSchema(schemaLocation);
			DigesterLoader digesterLoader = DigesterLoader.newLoader(new XmlRulesModule(mappingXml));
			Digester digester = digesterLoader.newDigester();
			digester.setValidating(true);
			digester.setXMLSchema(schema);
			Validator validator = schema.newValidator();
			Source xmlFileForValidation = new StreamSource(new File(xmlFile));
			validator.validate(xmlFileForValidation);
            InputStream inputStream = new FileInputStream(xmlFile);
            bulkOperationMetaData = digester.parse(inputStream);
		}
		 catch (SAXException e) {
			 logger.debug(e.getMessage());
			 ErrorKey errorkey = ErrorKey.getErrorKey("bulk.error.xml.template");
			 throw new BulkOperationException(errorkey, e, e.getMessage());
		}
		Collection<BulkOperationClass> classList = bulkOperationMetaData.getBulkOperationClass();
		if (classList == null)
		{
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.no.templates.loaded.message");
			throw new BulkOperationException(errorkey, null, "");
		}
		else
		{
			Iterator<BulkOperationClass> iterator = classList.iterator();
			if (iterator.hasNext())
			{
				BulkOperationClass bulkOperationClass=iterator.next();
				TemplateValidator templateValidator = new TemplateValidator();
				errorList = templateValidator.validateXmlAndCsv(bulkOperationClass, operationName,
						csvReader);
			}
		}
		return errorList;
	}

	/**
	 *
	 * @param operationName
	 * @param operationNameFromDB
	 * @param dropdownName
	 * @param dropdownNameFromDB
	 * @return
	 * @throws BulkOperationException
	 */
	protected boolean isTemplateExist(String operationName, String operationNameFromDB,
			String dropdownName, String dropdownNameFromDB) throws BulkOperationException
	{
		boolean flag = false;
		if (operationNameFromDB.equals(operationName) & dropdownNameFromDB.equals(dropdownName))
		{
			flag = true;
		}
		else if (operationNameFromDB.equals(operationName)
				& !dropdownNameFromDB.equals(dropdownName))
		{
			logger.debug("Cannot insert the template as "
					+ "same Operation Name already exists in the database.");
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.matching.operation.name");

			throw new BulkOperationException(errorkey, null, "");
		}
		else if (!operationNameFromDB.equals(operationName)
				& dropdownNameFromDB.equals(dropdownName))
		{
			logger.debug("Cannot insert template as "
					+ "same DropDown Name already exists in the database.");
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.matching.dropdown.name");
			throw new BulkOperationException(errorkey, null, "");
		}
		return flag;

	}

	/**
	 * Get XML Template File Data.
	 * @param xmlFile String.
	 * @return String.
	 * @throws Exception Exception.
	 */
	protected String getXMLTemplateFileData(String xmlFile) throws BulkOperationException
	{
		StringWriter xmlFormatData = new StringWriter();
		try
		{
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			InputStream inputStream = new FileInputStream(new File(xmlFile));
			org.w3c.dom.Document doc = documentBuilderFactory.newDocumentBuilder().parse(
					inputStream);
			Transformer serializer = TransformerFactory.newInstance().newTransformer();
			serializer.transform(new DOMSource(doc), new StreamResult(xmlFormatData));
		}
		catch (FileNotFoundException fnfExp)
		{
			logger.debug("XML File Not Found at the specified path.", fnfExp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.error.xml.file.not.found");
			throw new BulkOperationException(errorkey, fnfExp, "");
		}
		catch (IOException ioExp)
		{
			logger.debug("Error in reading the XML File.", ioExp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.error.xml.file.reading");
			throw new BulkOperationException(errorkey, ioExp, "");
		}
		catch (Exception exp)
		{
			logger.debug("Error in encoding XML file to data stream.", exp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.issues");
			throw new BulkOperationException(errorkey, exp, exp.getMessage());
		}
		return xmlFormatData.toString();
	}

	/**
	 * Get CSV Template File Data.
	 * @param csvFile String.
	 * @return String.
	 * @throws Exception Exception.
	 */
	protected String getCSVTemplateFileData(String csvFile) throws BulkOperationException
	{
		CSVReader reader = null;
		List<String[]> list = null;
		StringBuffer commaSeparatedString = new StringBuffer();
		try
		{
			reader = new CSVReader(new FileReader(csvFile));
			list = reader.readAll();
			reader.close();
			Iterator<String[]> iterator = list.iterator();
			if (iterator.hasNext())
			{
				String string[] = iterator.next();
				int rowDataLength = string.length;
				for (int i = 0; i < rowDataLength; i++)
				{
					commaSeparatedString.append(string[i]);
					if (i < rowDataLength - 1)
					{
						commaSeparatedString.append(BulkOperationConstants.SINGLE_COMMA);
					}
				}
			}
		}
		catch (FileNotFoundException fnfExpp)
		{
			logger.debug("CSV File Not Found at the specified path.", fnfExpp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.error.csv.file.not.found");
			throw new BulkOperationException(errorkey, fnfExpp, "");
		}
		catch (IOException ioExpp)
		{
			logger.debug("Error in reading the CSV File.", ioExpp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.error.csv.file.reading");
			throw new BulkOperationException(errorkey, ioExpp, "");
		}
		finally
		{
			try
			{
				if (reader != null)
				{
					reader.close();
				}
			}
			catch (IOException ioExpp)
			{
				logger.debug("Error while closing the reader.", ioExpp);
			}
		}
		return commaSeparatedString.toString();
	}

	/**
	 *
	 * @param operationName
	 * @param dropdownName
	 * @param csvFile
	 * @param xmlFile
	 * @param mappingXml
	 * @throws BulkOperationException
	 * @throws SQLException
	 * @throws IOException
	 * @throws DAOException
	 */
	protected void importTemplates(String operationName, String dropdownName, String csvFile,
			String xmlFile, String mappingXml,String xsdLocation)
	{
		try
		{
			Set<String> errorList = validate(operationName, dropdownName, csvFile, xmlFile,
					mappingXml,xsdLocation);
			if (errorList == null || errorList.isEmpty())
			{
				saveTemplateInDatabase(operationName, dropdownName, csvFile, xmlFile);
			}
			else
			{
				logger.info("----------------------ERROR:errorList-------------------------");
				for (String error : errorList)
				{
					logger.info(error);
				}
				logger.info("----------------------ERROR-------------------------");
			}
		}
		catch (BulkOperationException exp)
		{
			logger
					.info("------------------------ERROR:BulkOperationException--------------------------------\n");
			logger.debug(exp.getMessage(), exp);
			logger.info(exp.getCause().getMessage() + "\n");
			logger.info("------------------------ERROR:--------------------------------");
		}
		catch (SQLException exp)
		{
			logger
					.info("------------------------ERROR:SQLException--------------------------------\n");
			logger.debug(exp.getMessage(), exp);
			logger.info(exp.getMessage() + "\n");
			logger.info("------------------------ERROR:--------------------------------");
		}
		catch (DAOException exp)
		{
			logger
					.info("------------------------ERROR:DAOException--------------------------------\n");
			logger.debug(exp.getMessage(), exp);
			logger.info(exp.getMessage() + "\n");
			logger.info("------------------------ERROR:--------------------------------");
		}
		catch (Exception exp)
		{
			logger
					.info("------------------------ERROR:Exception--------------------------------\n");
			logger.debug(exp.getMessage(), exp);
			logger.info(exp.getMessage() + "\n");
			logger.info("------------------------ERROR:--------------------------------");
		}
	}

	/**
	 *
	 * @param operationName
	 * @param dropdownName
	 * @param csvFile
	 * @param xmlFile
	 * @throws DAOException
	 * @throws BulkOperationException
	 * @throws SQLException
	 */
	protected void saveTemplateInDatabase(String operationName, String dropdownName,
			String csvFile, String xmlFile) throws DAOException, BulkOperationException,
			SQLException, IOException
	{
		boolean flag = checkAddOrEditTemplateCase(operationName, dropdownName);
		if (flag)
		{
			editTemplate(operationName, dropdownName, csvFile, xmlFile);
		}
		else
		{
			addTemplate(operationName, dropdownName, csvFile, xmlFile);
		}
	}

	/**
	 *
	 * @param operationName
	 * @param dropdownName
	 * @return
	 * @throws BulkOperationException
	 */
	protected abstract boolean checkAddOrEditTemplateCase(String operationName, String dropdownName)
			throws DAOException, BulkOperationException;

	/**
	 *
	 * @param operationName
	 * @param dropdownName
	 * @param csvFile
	 * @param xmlFile
	 * @throws DAOException
	 * @throws BulkOperationException
	 * @throws IOException
	 */
	protected abstract void editTemplate(String operationName, String dropdownName, String csvFile,
			String xmlFile) throws DAOException, BulkOperationException, IOException, SQLException;

	/**
	 *
	 * @param operationName
	 * @param dropdownName
	 * @param csvFile
	 * @param xmlFile
	 * @throws DAOException
	 * @throws BulkOperationException
	 * @throws IOException
	 */
	protected abstract void addTemplate(String operationName, String dropdownName, String csvFile,
			String xmlFile) throws DAOException, BulkOperationException, IOException, SQLException;

}
