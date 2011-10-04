package edu.wustl.catissuecore.bizlogic.magetab;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.logging.api.util.StringUtils;

/**
 * 
 * @author Alexander Zgursky
 *
 */
public class MagetabExportWizardBean {    

	//TODO: move it to constants?
	public static final String MAGETAB_EXPORT_WIZARD_BEAN = "magetabExportWizardBean";
	
	private static transient final Logger logger = Logger.getCommonLogger(MagetabExportWizardBean.class);
	

	private State state = State.NEW;

	private int currentPage = 1;
	private List<Long> selectedSpecimensIds;
	private SseTable sseTable;
	
	private String defaultExtractType = "DNA";
	private String sourceColumnSelected;
	private String sampleColumnSelected;
	private String extractColumnSelected;

	private Map<String, TransformerSelections> transformersSelections;
    private String[] caArrayEnabled;
    private String displayColumns;
	
	public void init(List<Long> selectedSpecimensIds) {
		this.selectedSpecimensIds = selectedSpecimensIds;
		this.sseTable = null;
		this.currentPage = 1;
		this.state = State.READY;
	}
	
	public void deactivate() {
		this.state = State.DEACTIVATED;
		this.selectedSpecimensIds = null;
		this.sseTable = null;
	}

	public List<Long> getSelectedSpecimensIds() {
		return selectedSpecimensIds;
	}
	
	public String getDefaultExtractType() {
		return defaultExtractType;
	}
	
	public void setDefaultExtractType(String defaultExtractType) {
		this.defaultExtractType = defaultExtractType;
	}
	
	public State getState() {
		return state;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

        public String[] getCaArrayEnabled() {
            return caArrayEnabled;
        }

        public void setCaArrayEnabled(String[] caArrayEnabled) {
            this.caArrayEnabled = caArrayEnabled;
        }
	
	public Map<String, TransformerSelections> getTransformersSelections() {		
		if (transformersSelections == null) {
			transformersSelections = new LinkedHashMap<String, TransformerSelections>();
			populateTransformersSelections();
		} 
		return transformersSelections;
	}
	
	public SseTable getSseTable() {
		if (sseTable == null) {
			sseTable = new SseTable();
		}
		return sseTable;
	}
	
	private int getMaxSublistLength(List<? extends List<?>> lists) {
		int maxLength = 0;
		for (List<?> sublist : lists) {
			if (sublist.size() > maxLength) {
				maxLength = sublist.size();
			}
		}
		
		return maxLength;
	}
	
	public String getDisplayColumns() {
		return displayColumns;
	}

	public void setDisplayColumns(String displayColumns) {
		this.displayColumns = displayColumns;
	}
	
	public String getSourceColumnSelected() {
		return sourceColumnSelected;
	}

	public void setSourceColumnSelected(String sourceColumnSelected) {
		this.sourceColumnSelected = sourceColumnSelected;
	}

	public String getSampleColumnSelected() {
		return sampleColumnSelected;
	}

	public void setSampleColumnSelected(String sampleColumnSelected) {
		this.sampleColumnSelected = sampleColumnSelected;
	}

	public String getExtractColumnSelected() {
		return extractColumnSelected;
	}

	public void setExtractColumnSelected(String extractColumnSelected) {
		this.extractColumnSelected = extractColumnSelected;
	}

	private Set<Integer> getFullSelection(List<?> list) {
		Set<Integer> chainSelections = new HashSet<Integer>();
		for (int i = 0; i < list.size(); i++) {
			chainSelections.add(i);
		}
		
		return chainSelections;
	}
	
	private void populateTransformersSelections() {
		MagetabExportBizLogic bizLogic = new MagetabExportBizLogic();
		MagetabExportConfig config = bizLogic.getDefaultConfig();
		Map<String, Transformer> availableTransformers = config.getTransformers();
		List<String> sourceTransformers = config.getSourceTransformers();
		List<String> sampleTransformers = config.getSampleTransformers();
		List<String> extractTransformers = config.getExtractTransformers();
		
		for (Transformer transformer : availableTransformers.values()) {
			TransformerSelections transformerSelections = new TransformerSelections(
					transformer.getName(),
					transformer.getUserFriendlyName(),
					transformer.getLocalName(),
					sourceTransformers.contains(transformer.getName()),
					sampleTransformers.contains(transformer.getName()),
					extractTransformers.contains(transformer.getName()),
					transformer.isMageTabSpec());			
			transformersSelections.put(transformer.getName(), transformerSelections);
		}
	}
	
	private void populateSampleTransformersSelectionsIfNull() {
		MagetabExportBizLogic bizLogic = new MagetabExportBizLogic();
		MagetabExportConfig config = bizLogic.getDefaultConfig();
		Map<String, Transformer> availableTransformers = config.getTransformers();
		//List<String> sourceTransformers = config.getSourceTransformers();
		List<String> sampleTransformers = config.getSampleTransformers();
		//List<String> extractTransformers = config.getExtractTransformers();
		
		for (Transformer transformer : availableTransformers.values()) {
			TransformerSelections transformerSelections = new TransformerSelections(
					transformer.getName(),
					transformer.getUserFriendlyName(),
					transformer.getLocalName(),
					false,
					sampleTransformers.contains(transformer.getName()),
					false,
					transformer.isMageTabSpec());			
			transformersSelections.put(transformer.getName(), transformerSelections);
		}
	}
	
	
	public enum State {
		NEW,
		READY,
		WORKING,
		DEACTIVATED
	}
	
	public class SseTable {
		private String forDefaultExtractType;
		private List<List<Specimen>> specimenChains;
		private Set<Integer> chainSelections;
		private int maxChainLength;
		
		public SseTable() {
			MagetabExportBizLogic bizLogic = new MagetabExportBizLogic();
			specimenChains = bizLogic.querySpecimenChains(selectedSpecimensIds);
			maxChainLength = getMaxSublistLength(specimenChains);			
		}
		
		public List<List<Specimen>> getSpecimenChains() {
			return specimenChains;
		}

//		public void setSpecimenChains(List<List<Specimen>> specimenChains) {
//			this.specimenChains = specimenChains;
//		}

		public Set<Integer> getChainSelections() {
			if (	chainSelections == null ||
					(forDefaultExtractType == null && defaultExtractType != null) ||
					(forDefaultExtractType != null && !forDefaultExtractType.equals(defaultExtractType))) {
				initSelections();
				forDefaultExtractType = defaultExtractType;
			}
			return chainSelections;
		}

		public void setChainSelections(Set<Integer> chainSelections) {
			this.chainSelections = chainSelections;
		}

		public int getMaxChainLength() {
			return maxChainLength;
		}

//		public void setMaxChainLength(int maxChainLength) {
//			this.maxChainLength = maxChainLength;
//		}

		private void initSelections() {
			logger.debug("The extaction type is "+defaultExtractType +" and the chain selection size is "+specimenChains.size());
			if (defaultExtractType == null) {
				chainSelections = getFullSelection(specimenChains);
			} else {
				chainSelections = new HashSet<Integer>();
				for (int i = 0; i < specimenChains.size(); i++) {
					List<Specimen> chain = specimenChains.get(i);
					Specimen extract = chain.get(chain.size()-1);
					String extractType = extract.getSpecimenType();
					logger.debug(i+" "+(StringUtils.isBlank(extract.getLabel())?extract.getBarcode():extract.getLabel())+" is of type "+extract.getSpecimenType());
					if (extractType != null && extractType.toLowerCase().contains(defaultExtractType.toLowerCase())) {
						chainSelections.add(i);
					}
				}
			}
		}
	}
	
//	public class SseTableRow {
//		private Specimen selectedSpecimen;
//		private List<Specimen> possibleSources;
//		private int sourceSelection;
//		private int sampleSelection;
//		private Specimen extract;
//		
//		public Specimen getSelectedSpecimen() {
//			return selectedSpecimen;
//		}
//		
//		public void setSelectedSpecimen(Specimen selectedSpecimen) {
//			this.selectedSpecimen = selectedSpecimen;
//		}
//		
//		public List<Specimen> getPossibleSources() {
//			return possibleSources;
//		}
//		
//		public void setPossibleSources(List<Specimen> possibleSources) {
//			this.possibleSources = possibleSources;
//		}
//		
//		public int getSourceSelection() {
//			return sourceSelection;
//		}
//		
//		public void setSourceSelection(int sourceSelection) {
//			this.sourceSelection = sourceSelection;
//		}
//		
//		public int getSampleSelection() {
//			return sampleSelection;
//		}
//		
//		public void setSampleSelection(int sampleSelection) {
//			this.sampleSelection = sampleSelection;
//		}
//		
//		public Specimen getExtract() {
//			return extract;
//		}
//		
//		public void setExtract(Specimen extract) {
//			this.extract = extract;
//		}
//	}

}
