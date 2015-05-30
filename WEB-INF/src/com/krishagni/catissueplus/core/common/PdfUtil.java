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

public class PdfUtil {
	
	public static String getText(InputStream in) {
		PDDocument pd = null;
		try {
			pd = PDDocument.load(in);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			pdfStripper.setStartPage(1);
			pdfStripper.setEndPage(pd.getNumberOfPages());
			return pdfStripper.getText(pd);
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		} finally {
			if (pd != null) {
				try {
					pd.close();
				} catch (Exception e) {
					throw OpenSpecimenException.serverError(e);
				}
			}
		}
	}
	
	public static void create(String filePath, String fileText) {
		PDDocument document = null;
		try {
			document = new PDDocument();
			PDPage page = new PDPage();
			document.addPage(page);

			PDFont font = PDType1Font.HELVETICA_BOLD;

			PDPageContentStream contentStream = new PDPageContentStream(document, page);

			contentStream.beginText();
			contentStream.setFont(font, 10);
			contentStream.drawString(fileText);
			contentStream.endText();
			contentStream.close();
			
			File file = new File(filePath);
			file.getParentFile().mkdirs();
			document.save(file);
			
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		} finally {
			if (document != null) {
				try {
					document.close();
				}
				catch (Exception e) {
					throw OpenSpecimenException.serverError(e);
				}
			}	
		}
	}

}