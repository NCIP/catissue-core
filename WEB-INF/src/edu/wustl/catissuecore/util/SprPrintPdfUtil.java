package edu.wustl.catissuecore.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;


import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.dto.SprReportDTO;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;

public class SprPrintPdfUtil {

	public Map<String,Object>  generateIdentifiedPdf(Long reportId, Long deReportId,SessionDataBean sessionDataBean)
			throws DocumentException, FileNotFoundException, ApplicationException {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		IdentifiedSurgicalPathologyReportBizLogic bizLogic = new IdentifiedSurgicalPathologyReportBizLogic();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SprReportDTO reportData = null;
		reportData = bizLogic.getIdentifiedReportData(reportId,deReportId, sessionDataBean);

		
		Document document = new Document();
		PdfWriter pdfWriter = PdfWriter.getInstance(document, out);
		pdfWriter.setPageEvent(new PageWithRectangle());
		document.open();
		document.addTitle("IDENTIFIED SURGICAL PATHOLOGY REPORT");
		Paragraph paragraph2 = new Paragraph();
		paragraph2.setAlignment(Element.ALIGN_CENTER);
		Chunk chunk = new Chunk("IDENTIFIED SURGICAL PATHOLOGY REPORT");
		chunk.setFont(new Font(FontFamily.HELVETICA, 10, Font.BOLD));
		chunk.setUnderline(0.1f, -2f);
		paragraph2.add(chunk);

		document.add(paragraph2);
		document.add(Chunk.NEWLINE);
		Paragraph paragraph = new Paragraph();
		chunk = new Chunk("PATIENT INFORMATION");
		chunk.setFont(new Font(FontFamily.HELVETICA, 10, Font.BOLD));

		paragraph.add(chunk);
		document.add(paragraph);

		PdfPTable table = new PdfPTable(3);
		table.setSpacingBefore(4f);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setWidthPercentage(100);
		table.setSpacingAfter(4f);

		paragraph = new Paragraph();
		chunk = new Chunk("Protocol Name: ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 8, Font.BOLD));
		paragraph.add(chunk);
		// Add Value from DB
		chunk = new Chunk(reportData.getCpTitle());
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		PdfPCell cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		paragraph = new Paragraph();
		chunk = new Chunk("Birth Date: ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 8, Font.BOLD));
		paragraph.add(chunk);
		// Add Value from DB
		String datePattern = CommonServiceLocator.getInstance()
				.getDatePattern();
		String dateStr = "";
		if (reportData.getBirthDate() != null) {
			dateStr = CommonUtilities.parseDateToString(
					(Date) reportData.getBirthDate(), datePattern);
		}

		chunk = new Chunk(dateStr);
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		paragraph = new Paragraph();
		chunk = new Chunk("Printed by: ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 8, Font.BOLD));
		paragraph.add(chunk);
		// Add Value from DB
		String firstName = sessionDataBean.getFirstName()==null? "" : sessionDataBean.getFirstName();
		String lastName = sessionDataBean.getLastName() == null ? "" : sessionDataBean.getLastName();
		chunk = new Chunk((lastName+" "+firstName).trim());
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		paragraph = new Paragraph();
		chunk = new Chunk("Participant Name (Protocol ID): ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 8, Font.BOLD));
		paragraph.add(chunk);
		// Add Value from DB
		chunk = new Chunk(reportData.getParticipantName());
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		paragraph = new Paragraph();
		chunk = new Chunk("Gender: ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 8, Font.BOLD));
		paragraph.add(chunk);
		// Add Value from DB
		chunk = new Chunk(reportData.getGender());
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		paragraph = new Paragraph();
		chunk = new Chunk("Printed on date: ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 8, Font.BOLD));
		paragraph.add(chunk);
		// Add Value from DB
		chunk = new Chunk(CommonUtilities.parseDateToString(new Date(),
				datePattern));
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		paragraph = new Paragraph();
		chunk = new Chunk("Medicare Number (MRN): ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 8, Font.BOLD));
		paragraph.add(chunk);
		// Add Value from DB
		chunk = new Chunk(reportData.getMrnString());
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		table.addCell(new PdfPCell());
		table.addCell(new PdfPCell());
		document.add(table);

		document.add(chunk.NEWLINE);

		paragraph = new Paragraph();
		chunk = new Chunk(reportData.getData());
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		document.add(paragraph);
		document.add(chunk.NEWLINE);

		if(reportData.getConceptReferentMap()!=null){
			createConceptCodeTable(reportData.getConceptReferentMap(),document);
		}
		
		
		 paragraph2 = new Paragraph();
		paragraph2.setAlignment(Element.ALIGN_CENTER);
		chunk = new Chunk("End of Report ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 6));
		chunk.setUnderline(0.1f, -2f);
		paragraph2.add(chunk);

		document.add(paragraph2);
		
		document.close();
		
		String fileName = reportData.getPpid()+"_"+Constants.IDENTIFIED;
		returnMap.put("fileName", fileName);
		returnMap.put("fileData",  out.toByteArray());
		return returnMap;
	}
	
	public void createConceptCodeTable(Map<String,String> conceptReferentMap,Document document) throws DocumentException{
		Set<String>keySet = conceptReferentMap.keySet();
		if(!keySet.isEmpty()){
			Paragraph paragraph = new Paragraph();
			Chunk chunk = new Chunk("CONCEPT CODES: ");
			chunk.setFont(new Font(FontFamily.HELVETICA, 10, Font.BOLD));

			paragraph.add(chunk);
			document.add(paragraph);
			

			PdfPTable table = new PdfPTable(2);
			table.setSpacingBefore(4f);
			table.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.setWidthPercentage(100);
			table.setSpacingAfter(4f);
			
			Iterator<String> ite = keySet.iterator();
			
			while(ite.hasNext()){
				String conceptStr = ite.next();
				paragraph = new Paragraph();
				chunk = new Chunk(conceptStr);
				chunk.setFont(new Font(FontFamily.HELVETICA, 8, Font.BOLD));
				paragraph.add(chunk);
				PdfPCell cell = new PdfPCell(paragraph);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);
				
				
				String conceptReferent  = conceptReferentMap.get(conceptStr).trim();
				if(!"".equals(conceptReferent)){
					conceptReferent = conceptReferent.substring(0,conceptReferent.length()-1);
				}
				paragraph = new Paragraph();
				chunk = new Chunk(conceptReferent);
				chunk.setFont(new Font(FontFamily.HELVETICA,8));
				paragraph.add(chunk);
				cell = new PdfPCell(paragraph);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);
			}
			document.add(table);

			document.add(chunk.NEWLINE);

			
		}
	}

	
	
	public Map<String,Object> generateDIdentifiedPdf(Long reportId, SessionDataBean sessionDataBean)
			throws DocumentException, FileNotFoundException, ApplicationException {
		IdentifiedSurgicalPathologyReportBizLogic bizLogic = new IdentifiedSurgicalPathologyReportBizLogic();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SprReportDTO reportData = null;
		reportData = bizLogic.getDidentifiedReportData(reportId, sessionDataBean);

		
		Document document = new Document();
		PdfWriter pdfWriter = PdfWriter.getInstance(document, out);
		pdfWriter.setPageEvent(new PageWithRectangle());
		document.open();
		document.addTitle("DE-IDENTIFIED SURGICAL PATHOLOGY REPORT");
		Paragraph paragraph2 = new Paragraph();
		paragraph2.setAlignment(Element.ALIGN_CENTER);
		Chunk chunk = new Chunk("DE-IDENTIFIED SURGICAL PATHOLOGY REPORT");
		chunk.setFont(new Font(FontFamily.HELVETICA, 10, Font.BOLD));
		chunk.setUnderline(0.1f, -2f);
		paragraph2.add(chunk);

		document.add(paragraph2);
		document.add(Chunk.NEWLINE);
		Paragraph paragraph = new Paragraph();
		chunk = new Chunk("PATIENT INFORMATION");
		chunk.setFont(new Font(FontFamily.HELVETICA, 10, Font.BOLD));

		paragraph.add(chunk);
		document.add(paragraph);

		PdfPTable table = new PdfPTable(3);
		table.setSpacingBefore(4f);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setWidthPercentage(100);
		table.setSpacingAfter(4f);

		paragraph = new Paragraph();
		chunk = new Chunk("Protocol Name: ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 8, Font.BOLD));
		paragraph.add(chunk);
		// Add Value from DB
		chunk = new Chunk(reportData.getCpTitle());
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		PdfPCell cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		
		paragraph = new Paragraph();
		chunk = new Chunk("Gender: ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 8, Font.BOLD));
		paragraph.add(chunk);
		// Add Value from DB
		chunk = new Chunk(reportData.getGender());
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		paragraph = new Paragraph();
		chunk = new Chunk("Printed by: ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 8, Font.BOLD));
		paragraph.add(chunk);
		// Add Value from DB
		String firstName = sessionDataBean.getFirstName()==null? "" : sessionDataBean.getFirstName();
		String lastName = sessionDataBean.getLastName() == null ? "" : sessionDataBean.getLastName();
		chunk = new Chunk((lastName+" "+firstName).trim());
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		paragraph = new Paragraph();
		chunk = new Chunk("Participant Name (Protocol ID): ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 8, Font.BOLD));
		paragraph.add(chunk);
		// Add Value from DB
		chunk = new Chunk(reportData.getParticipantName());
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		paragraph = new Paragraph();
		chunk = new Chunk("Age: ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 8, Font.BOLD));
		paragraph.add(chunk);
		// Add Value from DB
		if(reportData.getAge()!=0){
			chunk = new Chunk(reportData.getAge()+"");
		}else{
			chunk = new Chunk("");
		}
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);


		paragraph = new Paragraph();
		chunk = new Chunk("Printed on date: ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 8, Font.BOLD));
		paragraph.add(chunk);
		// Add Value from DB
		String datePattern = CommonServiceLocator.getInstance()
				.getDatePattern();
	
		chunk = new Chunk(CommonUtilities.parseDateToString(new Date(),
				datePattern));
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		document.add(table);

		document.add(chunk.NEWLINE);

		paragraph = new Paragraph();
		chunk = new Chunk(reportData.getData());
		chunk.setFont(new Font(FontFamily.HELVETICA, 8));
		paragraph.add(chunk);
		document.add(paragraph);
		
		document.add(chunk.NEWLINE);
		if(reportData.getConceptReferentMap()!=null){
			createConceptCodeTable(reportData.getConceptReferentMap(),document);
		}
		
		

		 paragraph2 = new Paragraph();
		paragraph2.setAlignment(Element.ALIGN_CENTER);
		chunk = new Chunk("End of Report ");
		chunk.setFont(new Font(FontFamily.HELVETICA, 6));
		chunk.setUnderline(0.1f, -2f);
		paragraph2.add(chunk);

		document.add(paragraph2);
		
		document.close();
		
		String fileName = reportData.getPpid()+"_"+Constants.DEIDENTIFIED;
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		returnMap.put("fileName", fileName);
		returnMap.put("fileData",  out.toByteArray());
	

		return returnMap;
	}

	
	
	
}

class PageWithRectangle extends PdfPageEventHelper {
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		Rectangle pageSize = writer.getPageSize();
		cb.rectangle(pageSize.getLeft() + 9, pageSize.getBottom() + 9,
				pageSize.getWidth() - 18, pageSize.getHeight() - 18);
		cb.stroke();
	}
}