package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.query.generator.ColumnValueBean;

public class ExportSpecimenListAction extends BaseAction{

	@Override
	protected ActionForward executeAction(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String specIds = request.getParameter("specId");
		String sql = "Select scg.NAME, Specimen1.LABEL, Specimen1.BARCODE, " +
				" (select label from catissue_Specimen where identifier=Specimen1.parent_specimen_id),Specimen1.SPECIMEN_CLASS, " +
				" Specimen1.SPECIMEN_TYPE, Specimen1.AVAILABLE_QUANTITY, Specimen1.LINEAGE, Specimen1.IDENTIFIER " +
				" FROM  " +
				" CATISSUE_EXTERNAL_IDENTIFIER ExternalIdentifier1 , CATISSUE_SPECIMEN Specimen1 , catissue_specimen_coll_group scg " +
				" WHERE scg.identifier = Specimen1.SPECIMEN_COLLECTION_GROUP_ID AND " +
				" Specimen1.IDENTIFIER   =  ExternalIdentifier1.SPECIMEN_ID  AND Specimen1.IDENTIFIER   =  ExternalIdentifier1.SPECIMEN_ID   AND " +
				" ( ( Specimen1.IDENTIFIER  in ("+specIds+")  )   AND UPPER(Specimen1.ACTIVITY_STATUS ) != UPPER('Disabled')  ) " +
				" ORDER BY Specimen1.IDENTIFIER ,ExternalIdentifier1.IDENTIFIER";
		ColumnValueBean bean = new ColumnValueBean(specIds);
		List<ColumnValueBean> list = new ArrayList<ColumnValueBean>();
		list.add(bean);
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("Specimen Collection Group Name");
		columnNames.add("Label");
		columnNames.add("Barcode");
		columnNames.add("ParentLabel");
		columnNames.add("Class");
		columnNames.add("Type");
		columnNames.add("Available Quantity");
		columnNames.add("Lineage");
		columnNames.add("Identifier");
		final List<List<String>> exportList = new ArrayList<List<String>>();
		exportList.add(columnNames);
final HttpSession session = request.getSession();
		
		try
		{
			
		List result = AppUtility.executeSQLQuery(sql);
		for (Object object : result) 
		{
			List resultRow = (List)object;
			exportList.add(resultRow);
		}
		
		
			final ExportReport report = new ExportReport(this.getFileName(session));
			report.writeData(exportList, Constants.DELIMETER);
			report.closeFile();
		}
		catch (final IOException e)
		{
			final ActionErrors errors = new ActionErrors();
			final ActionError error = new ActionError("shoppingcart.exportfilexception");
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			this.saveErrors(request, errors);
		}

		SendFile.sendFileToClient(response, this.getFileName(session),
				Constants.SHOPPING_CART_FILE_NAME, Constants.APPLICATION_DOWNLOAD);
		return null;
	}
	
	private String getFileName(HttpSession session)
	{
		final String fileName = CommonServiceLocator.getInstance().getAppHome()
				+ System.getProperty("file.separator") + session.getId() + Constants.DOT_CSV;
		return fileName;
	}

}
