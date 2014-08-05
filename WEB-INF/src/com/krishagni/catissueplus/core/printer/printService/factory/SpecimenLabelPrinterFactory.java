
package com.krishagni.catissueplus.core.printer.printService.factory;

import java.io.IOException;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public interface SpecimenLabelPrinterFactory {

	public void printLabel(Specimen specimen, String ipAddress, String loginName, String cpShortTitle) throws IOException;
}
