package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.services.ContainerMapExporter;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.util.CsvFileWriter;
import com.krishagni.catissueplus.core.common.util.CsvWriter;

public class ContainerMapExporterImpl implements ContainerMapExporter {
	private MessageSource messageSource;
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	@PlusTransactional
	public File exportToFile(StorageContainer container) 
	throws IOException {
		File file = null;
		CsvWriter writer = null;
		
		try {
			file = File.createTempFile("container-", ".csv");
			file.deleteOnExit();
			
			writer = CsvFileWriter.createCsvFileWriter(file);
			
			exportContainerDetails(writer, container);
			exportOccupiedPositions(writer, container);
			writer.flush();
			
			return file;
		} finally {			
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {					
				}
			}
		}		
	}
		
	private void exportContainerDetails(CsvWriter writer, StorageContainer container) 
	throws IOException {
		writer.writeNext(new String[] { getMessage(CONTAINER_DETAILS) });
		writer.writeNext(new String[] { getMessage(CONTAINER_NAME), container.getName() });
		writer.writeNext(new String[] { getMessage(CONTAINER_HIERARCHY), container.getStringifiedAncestors() });
		
		writer.writeNext(new String[] { getMessage(CONTAINER_RESTRICTIONS) });
		
		List<String> cps = new ArrayList<String>();
		cps.add(getMessage(CONTAINER_PROTOCOL));
		if (container.getCompAllowedCps().isEmpty()) {
			cps.add(getMessage(ALL));
		} else {
			for (CollectionProtocol cp : container.getCompAllowedCps()) {
				cps.add(cp.getTitle());
			}
		}
		writer.writeNext(cps.toArray(new String[0]));
		
		List<String> types = new ArrayList<String>();
		types.add(getMessage(CONTAINER_SPECIMEN_TYPES));
		if (container.getCompAllowedSpecimenClasses().isEmpty() && 
			container.getCompAllowedSpecimenTypes().isEmpty()) {
			types.add(getMessage(ALL));
		} else {
			for (String specimenClass : container.getCompAllowedSpecimenClasses()) {
				types.add(specimenClass);
			}
			
			for (String type : container.getCompAllowedSpecimenTypes()) {
				types.add(type);
			}
		}
		writer.writeNext(types.toArray(new String[0]));
	}
	
	private void exportOccupiedPositions(CsvWriter writer, StorageContainer container) {
		List<String> cells = new ArrayList<String>();
		cells.add("");
		for (int i = 1; i <= container.getNoOfColumns(); ++i) {
			cells.add(container.toColumnLabelingScheme(i));
		}		
		writer.writeNext(cells.toArray(new String[0]));
				
		int posIdx = 0;
		List<StorageContainerPosition> positions = 
				new ArrayList<StorageContainerPosition>(container.getOccupiedPositions());
		Collections.sort(positions);
		
		for (int j = 1; j <= container.getNoOfRows(); ++j) {
			cells.clear();
			cells.add(container.toRowLabelingScheme(j));
			
			for (int i = 1; i <= container.getNoOfColumns(); ++i) {				
				if (posIdx >= positions.size()) {
					cells.add("");
					continue;
				}
				
				StorageContainerPosition pos = positions.get(posIdx);
				if (pos.getPosOneOrdinal() != i || pos.getPosTwoOrdinal() != j) {
					cells.add("");
				} else {
					posIdx++;
					if (pos.getOccupyingContainer() != null) {
						cells.add(pos.getOccupyingContainer().getName());
					} else if (pos.getOccupyingSpecimen() != null) {
						cells.add(pos.getOccupyingSpecimen().getLabel());
					} else {
						cells.add("");
					}						
				}
			}
			
			writer.writeNext(cells.toArray(new String[0]));
		}
	}
		
	private String getMessage(String key) {
		return messageSource.getMessage(key, null, Locale.getDefault());
	}

	private static final String CONTAINER_DETAILS        = "storage_container_details";
	
	private static final String CONTAINER_NAME           = "storage_container_name";
	
	private static final String CONTAINER_HIERARCHY      = "storage_container_hierarchy";
	
	private static final String CONTAINER_RESTRICTIONS   = "storage_container_restrictions";
	
	private static final String CONTAINER_PROTOCOL       = "storage_container_restricted_protocols";
	
	private static final String CONTAINER_SPECIMEN_TYPES = "storage_container_specimen_types";
	
	private static final String ALL                      = "common_all";
}
