package com.krishagni.catissueplus.core.common;

import java.io.InputStream;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class PdfUtil {
	
	public static String getText(InputStream in) {
		PdfReader reader = null;
		try {
			StringBuffer pdfText = new StringBuffer();
			reader = new PdfReader(in);
			int noOfPages = reader.getNumberOfPages();
			for (int i = 1; i <= noOfPages; i++) {
				pdfText.append(PdfTextExtractor.getTextFromPage(reader, i));
			}
			return pdfText.toString();
			
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		} finally {
			if (reader != null) {
				reader.close();
			}	
		}
	}
}