package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.context.MessageSource;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.services.SprPdfGenerator;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Utility;

public class SprText2PdfGenerator implements SprPdfGenerator {
	private MessageSource messageSource;
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public File generate(File file, Map<String, Object> contextMap) {
		Visit visit = (Visit)contextMap.get("visit"); 
		FileOutputStream out = null;
		Document document = new Document();
		try {
			File sprReport = File.createTempFile("spr-report", ".pdf");
			out= new FileOutputStream(sprReport);
			
			PdfWriter.getInstance(document, out);
			document.open();
			document.addTitle(getMessage("spr_title"));
			
			Paragraph header = new Paragraph();
			header.setAlignment(Element.ALIGN_CENTER);
			Chunk headerText = getChunk(getMessage("spr_header"), 10, true, true);
			header.add(headerText);
			document.add(header);
			document.add(Chunk.NEWLINE);
			
			Paragraph patientInfo = new Paragraph();
			Chunk patientInfoText = getChunk(getMessage("spr_patient_info"), 10, true, false);
			patientInfo.add(patientInfoText);
			document.add(patientInfo);
			PdfPTable patientInfoTbl = getHeader(visit); 
			document.add(patientInfoTbl);
		
			String fileText = Utility.getFileText(file);
			document.add(new Paragraph(fileText, getFont(8,false)));
			
			
			Chunk endReport = getChunk(getMessage("spr_end_of_report"), 6, false, true);
			Paragraph footer = new Paragraph();
			footer.setAlignment(Element.ALIGN_CENTER);
			footer.add(endReport);
			document.add(footer);
			
			return sprReport;
		} catch(Exception e) {
			throw OpenSpecimenException.serverError(e);
		} finally {
			document.close();
			IOUtils.closeQuietly(out);
		}
	}
	
	private PdfPTable getHeader(Visit visit) {
		PdfPTable header = new PdfPTable(3);
		header.setSpacingBefore(4f);
		header.setHorizontalAlignment(Element.ALIGN_LEFT);
		header.setWidthPercentage(100);
		header.setSpacingAfter(4f);

		Map<String, String> headerInfo = new LinkedHashMap<String, String>();
		headerInfo.put(getMessage("spr_protocol_name"), visit.getCollectionProtocol().getShortTitle());
		headerInfo.put(getMessage("spr_gender"), visit.getRegistration().getParticipant().getGender());
		headerInfo.put(getMessage("spr_printed_by"),
				AuthUtil.getCurrentUser().getLastName() + ", " + AuthUtil.getCurrentUser().getFirstName());
		headerInfo.put(getMessage("spr_visit_name"), visit.getName());
		headerInfo.put(getMessage("spr_ppid"), visit.getRegistration().getPpid());
		Integer age = Utility.getAge(visit.getRegistration().getParticipant().getBirthDate());
		headerInfo.put(getMessage("spr_age"), (age != null) ? age.toString() : "-");
		headerInfo.put(getMessage("spr_printed_on_date"), Utility.getDateString(new Date()));
		headerInfo.put(getMessage("spr_participant_race"), 
				Utility.stringListToCsv(visit.getRegistration().getParticipant().getRaces(), false));
		headerInfo.put(getMessage("spr_visit_date"), Utility.getDateString(visit.getVisitDate()));
		
		for (Map.Entry<String, String> entry : headerInfo.entrySet()) {
			Paragraph paragraph = new Paragraph();
			paragraph.add(getChunk(entry.getKey(), 8, true, false));
			
			String val = (entry.getValue() != null) ? entry.getValue() : "-";  
			paragraph.add(getChunk(val, 8, false, false));
			
			PdfPCell cell = new PdfPCell(paragraph);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			header.addCell(cell);
		}
		
		return header;
	}
	
	private Font getFont(int size, boolean bold) {
		int font = bold ? Font.BOLD: Font.NORMAL;
		return new Font(FontFamily.HELVETICA, size, font);
	}
	
	private Chunk getChunk(String message, int fontSize, boolean bold, boolean underline) {
		Chunk chunk = new Chunk(message);
		chunk.setFont(getFont(fontSize, bold));
		if (underline) {
			chunk.setUnderline(0.1f, -2f);
		}
		return chunk;
	}
	
	private String getMessage(String code) {
		return messageSource.getMessage(code, null, Locale.getDefault());
	}
}
