
package com.krishagni.catissueplus.core.printer.printService.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.printer.printService.events.CreateLabelPrintEvent;
import com.krishagni.catissueplus.core.printer.printService.events.LabelPrintCreatedEvent;
import com.krishagni.catissueplus.core.printer.printService.factory.SpecimenLabelPrinterFactory;
import com.krishagni.catissueplus.core.printer.printService.services.PrintService;

public class SpecimenLabelPrinterService implements PrintService {

	@Autowired
	private DaoFactory daoFactory;

	@Autowired
	private SpecimenLabelPrinterFactory specLabelPrinterFact;

	public void setSpecLabelPrinterFact(SpecimenLabelPrinterFactory specLabelPrinterFact) {
		this.specLabelPrinterFact = specLabelPrinterFact;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public LabelPrintCreatedEvent print(CreateLabelPrintEvent event) {
		try {
			Specimen specimen = null;
			if (event.getName() != null) {
				specimen = daoFactory.getSpecimenDao().getSpecimenByLabel(event.getName());
			}
			else {
				specimen = daoFactory.getSpecimenDao().getSpecimen(event.getId());
			}

			if (specimen == null) {
				LabelPrintCreatedEvent.notFound();
			}
			specLabelPrinterFact.printLabel(specimen, event.getSessionDataBean().getIpAddress(), event.getSessionDataBean()
					.getUserName(), specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
					.getCollectionProtocol().getShortTitle());
			return LabelPrintCreatedEvent.ok();
		}
		catch (Exception e) {
			return LabelPrintCreatedEvent.serverError(e);
		}
	}
}
