package com.krishagni.catissueplus.core.biospecimen.domain;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol.SpecimenLabelAutoPrintMode;

@Audited
@AuditTable(value="OS_SPMN_LBL_PRINT_SETTINGS_AUD")
public class CpSpecimenLabelPrintSetting extends BaseEntity {
	private CollectionProtocol collectionProtocol;
	
	private String lineage;
	
	private SpecimenLabelAutoPrintMode printMode;
	
	private Integer copies;

	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
	}

	public SpecimenLabelAutoPrintMode getPrintMode() {
		return printMode;
	}

	public void setPrintMode(SpecimenLabelAutoPrintMode printMode) {
		this.printMode = printMode;
	}

	public Integer getCopies() {
		return copies;
	}

	public void setCopies(Integer copies) {
		this.copies = copies;
	}

	public void update(CpSpecimenLabelPrintSetting setting) {
		setCollectionProtocol(setting.getCollectionProtocol());
		setLineage(setting.getLineage());
		setPrintMode(setting.getPrintMode());
		setCopies(setting.getCopies());
	}
}
