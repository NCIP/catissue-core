package edu.wustl.catissuecore.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import au.com.bytecode.opencsv.CSVReader;
import edu.wustl.bulkoperator.BulkOperator;
import edu.wustl.bulkoperator.metadata.BulkOperationMetaData;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.actionForm.BulkOperationForm;
import edu.wustl.catissuecore.bizlogic.BulkOperationBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;

/**
 * This class uploads the CSV file of bulk operations and returns a new CSv file
 * with the results in it. 
 * @author sagar_baldwa
 *
 */
public class FileUploadAction extends SecureAction
{
	@Override
	/**
	 * Execute Secure Action.
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		BulkOperationForm bulkOperationForm= (BulkOperationForm)form;
		String dropdownName = bulkOperationForm.getOperationName();
		FormFile file = bulkOperationForm.getFile();
		List<String[]> csvDataList = null;
		try
		{
			csvDataList = getFileData(file);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new BulkOperationException("bulk.error.reading.csv.file");
		}
		BulkOperationBizLogic bulkOperationBizLogic = new BulkOperationBizLogic();
		List<String> list = null;
		try
		{
			list = bulkOperationBizLogic.getOperationNameAndXml(dropdownName);
		}
		catch(Exception exp)
		{
			throw new BulkOperationException("bulk.error.database.operation." +
					"reading.operation.name.xml.template");
		}
		try
		{
			if(!list.isEmpty())
			{
				if(csvDataList != null && !csvDataList.isEmpty())
				{
					BulkOperationMetaData bulkOperationMetaData =
						bulkOperationBizLogic.convertStringToXml(list.get(1));

					SessionDataBean sessionDataBean = this.getSessionData(request);
					String loginName = sessionDataBean.getUserName();
					
					//DAO dao = AppUtility.openDAOSession(sessionDataBean);
					File resultFile = BulkOperator.initiateBulkOperationFromUI(list.get(0), csvDataList,
							bulkOperationMetaData, loginName);
					//AppUtility.closeDAOSession(dao);
					request.setAttribute("resultFile", resultFile);
				}
				else
				{
					throw new BulkOperationException("bulk.error.reading.csv.file");
				}
			}
			else
			{
				throw new BulkOperationException("bulk.error.incorrect.operation.name");
			}
		}
		catch(Exception e)
		{
			ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
			if (errors == null)
			{
				errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					e.getMessage()));
			}
			this.saveErrors(request, errors);
			e.printStackTrace();
		}
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * Splits the input CSV file.  
	 * @param file FormFile.
	 * @return List of Strings.
	 * @throws FileNotFoundException FileNotFoundException.
	 * @throws IOException IOException.
	 */
	private List<String[]> getFileData(FormFile file)
			throws FileNotFoundException, IOException
	{
		List<String[]> csvDataList = null;
		if(file != null)
		{
			InputStream inputStream = file.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			csvDataList = new ArrayList<String[]>();
			String line = null;
			String line1 = "";
			File newCsvFile = new File(file.getFileName());
			while ((line = reader.readLine()) != null)
			{
				line1 = line1 + line + "\n";
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(newCsvFile));
			out.write(line1);
			out.close();
			inputStream.close();
			reader.close();
			CSVReader csvReader = new CSVReader(new FileReader(newCsvFile));
			csvDataList = csvReader.readAll();
			csvReader.close();
		}
		return csvDataList;
	}
}