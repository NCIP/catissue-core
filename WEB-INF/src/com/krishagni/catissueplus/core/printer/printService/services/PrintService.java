package com.krishagni.catissueplus.core.printer.printService.services;

import com.krishagni.catissueplus.core.printer.printService.events.CreateLabelPrintEvent;
import com.krishagni.catissueplus.core.printer.printService.events.LabelPrintCreatedEvent;


public interface PrintService{

	public LabelPrintCreatedEvent print(CreateLabelPrintEvent event);
}
