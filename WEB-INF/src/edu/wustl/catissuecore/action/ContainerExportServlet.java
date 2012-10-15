package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.wustl.common.xml2excel.CSVWriter;
import edu.wustl.common.xml2excel.ExcelWriter;
import edu.wustl.common.xml2excel.HTMLWriter;
import edu.wustl.common.xml2pdf.PDFWriter;


public class ContainerExportServlet  extends HttpServlet{
	
	/**
	 *
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,
			IOException
	{
		doPost(req, res);
	}

	
	/**
	 * This method is used to download files saved in database.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException
	{
		String fileType = request.getParameter("filetype");
		request.setCharacterEncoding("UTF-8");
		String xml = request.getParameter("grid_xml");
		xml = URLDecoder.decode(xml, "UTF-8");
		String fileName =  request.getParameter("filename");
		if(fileType.equals("pdf")){
			xml = request.getParameter("grid_xml");
			xml = URLDecoder.decode(xml, "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" +fileName + ".pdf\";");
			(new PDFWriter()).generate(xml, response);
		}else if(fileType.equals("csv")){
			fileName =  "\""+fileName+".csv\"";
			CSVWriter writer = new CSVWriter();
			writer.generate(xml,fileName, response);
		} else if(fileType.equals("excel")){
			(new ExcelWriter()).generate(xml, response);
		}else{
			HTMLWriter writer = new HTMLWriter();
			writer.generate(xml, response);
		}

		
	}

}
