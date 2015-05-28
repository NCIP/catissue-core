package com.krishagni.catissueplus.core.common;

import java.io.File;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFTextStripper;

import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class PdfDocument {
	
	public static String getText(InputStream spr) {
		String report = null;
		try {
			PDDocument pd = PDDocument.load(spr);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			pdfStripper.setStartPage(1);
			pdfStripper.setEndPage(pd.getNumberOfPages());
			report = pdfStripper.getText(pd);
			pd.close();
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		}
		return report;
	}
	
	public static void create(String text, String path, String fileName) {
		try {
			PDDocument document = new PDDocument();
			PDPage page = new PDPage();
			document.addPage( page );

			PDFont font = PDType1Font.HELVETICA_BOLD;

			PDPageContentStream contentStream = new PDPageContentStream(document, page);

			contentStream.beginText();
			contentStream.setFont(font, 10);
			contentStream.drawString(text);
			contentStream.endText();
			contentStream.close();
			
			File dir = new File(path);
			dir.mkdirs();
			String file = path + File.separator + fileName + ".pdf";
			document.save(file);
			document.close();
		} catch (Exception e) {
			OpenSpecimenException.serverError(e);
		}
	}
}
