package com.krishagni.openspecimen.custom.sgh.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.services.impl.DefaultSpecimenLabelPrinter;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.domain.LabelPrintJob;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.openspecimen.custom.sgh.SghErrorCode;
import com.krishagni.openspecimen.custom.sgh.events.BulkTridPrintOpDetail;
import com.krishagni.openspecimen.custom.sgh.services.TridGenerator;
import com.krishagni.openspecimen.custom.sgh.services.TridPrintSvc;

public class TridPrintSvcImpl implements TridPrintSvc {

	private static final String SGH_MODULE = "plugin_sgh";
	
	private ConfigurationService cfgSvc;
	
	private TridGenerator tridGenerator;
	
	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}
	
	public void setTridGenerator(TridGenerator tridGenerator) {
		this.tridGenerator = tridGenerator;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> generateAndPrintTrids(RequestEvent<BulkTridPrintOpDetail> req) {
		BulkTridPrintOpDetail printReq = req.getPayload();
		int tridsCount = printReq.getTridCount();
		if (tridsCount < 1) {
			return ResponseEvent.userError(SghErrorCode.INVALID_TRID_COUNT);
		}
		
		DefaultSpecimenLabelPrinter printer = getLabelPrinter();
		if (printer == null) {
			throw OpenSpecimenException.serverError(SpecimenErrorCode.NO_PRINTER_CONFIGURED);
		}
		
		List<Specimen> specimens = new ArrayList<Specimen>();
		for(int i = 0; i < printReq.getTridCount(); i++){
			String trid = tridGenerator.getNextTrid();
			specimens.addAll(getSpecimensToPrint(trid));
		}
		
		int copiesToPrint = cfgSvc.getIntSetting(SGH_MODULE, "copies_to_print", 1);
		
		LabelPrintJob job = printer.print(specimens, copiesToPrint);
		if (job == null) {
			throw OpenSpecimenException.userError(SpecimenErrorCode.PRINT_ERROR);
		}
		return ResponseEvent.response(true);
	}
	
	private Collection<Specimen> getSpecimensToPrint(String visitName) {
		
		List<Specimen> specimens = new ArrayList<Specimen>();
		Visit visit = new Visit();
		visit.setName(visitName);
		
		int tridCopies = cfgSvc.getIntSetting(SGH_MODULE, "trid_copies", 2);
		
		for(int i = 0; i < tridCopies; ++i){
			Specimen specimen = new Specimen();
			specimen.setVisit(visit);
			specimen.setLabel(visitName);
			specimens.add(specimen);
		}
		
		String malignantAliqPrefix = visitName + "_" + getMalignantAliqSuffix() + "_"; 
		for (int i = 1; i <= getMalignantAliqCnt(); ++i) {
			Specimen aliquot = new Specimen();
			aliquot.setVisit(visit);
			aliquot.setLabel(malignantAliqPrefix + i);
			specimens.add(aliquot);
		}
		
		String nonMalignantAliqPrefix = visitName + "_" + getNonMalignantAliqSuffix() + "_";
		for (int i = 1; i <= getNonMalignantAliqCnt(); ++i) {
			Specimen aliquot = new Specimen();
			aliquot.setVisit(visit);
			aliquot.setLabel(nonMalignantAliqPrefix + i);
			specimens.add(aliquot);
		}
		return specimens;
	}
	
	private DefaultSpecimenLabelPrinter getLabelPrinter() {
		String labelPrinterBean = cfgSvc.getStrSetting(
				ConfigParams.MODULE, 
				ConfigParams.SPECIMEN_LABEL_PRINTER, 
				"defaultTridPrinter");
		return (DefaultSpecimenLabelPrinter)OpenSpecimenAppCtxProvider
				.getAppCtx()
				.getBean(labelPrinterBean);
	}

	private int getMalignantAliqCnt() {
		return cfgSvc.getIntSetting(SGH_MODULE, "malignant_aliq_cnt", 3);
	}
	
	private int getNonMalignantAliqCnt() {
		return cfgSvc.getIntSetting(SGH_MODULE, "non_malignant_aliq_cnt", 2);
	}

	private String getMalignantAliqSuffix() {
		return cfgSvc.getStrSetting(SGH_MODULE, "malignant_aliq_suffix", "FZ-T");
	}
	
	private String getNonMalignantAliqSuffix() {
		return cfgSvc.getStrSetting(SGH_MODULE, "non_malignant_aliq_suffix", "FZ-N");
	}
	
}
