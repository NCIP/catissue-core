package edu.wustl.catissuecore.bizlogic.magetab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MagetabExportConfig {
	public static final String MGED = "MO";

    public static final String ICD_O_3_TOPOGRAPHY = "ICD-O-3 Topography";

    public static final String SNOMED_CT = "SNOMED-CT";

    public static final String NCI_THESAURUS = "NCI Thesaurus";

    private static Map<String, String> materialTypesMap = new HashMap<String, String>() {
	    /**
         * 
         */
        private static final long serialVersionUID = 1L;

    {	
		//any fluid is organism part.
		put("fluid","organism_part");
		//any tissue is organism part.
		put("tissue", "organism_part");
		//any cell is cell.
		put("cell", "cell");		
		// specific molecular types
		put("dna/molecular", "DNA");
		put("rna, cytoplasmic/molecular", "cytoplasmic_RNA");
		put("whole genome amplified dna/molecular", "genomic_DNA");
		put("rna, nuclear/molecular", "nuclear_RNA");
		put("rna, poly-a enriched/molecular", "polyA_RNA");
		put("protein/molecular", "protein");
		// non specific molecular type
		put("molecular","molecular_mixture");
		
		
	}};
	
	private static List<String> knownMolecularTypes=Arrays.asList(
			"dna",
			"rna, cytoplasmic",
			"whole genome amplified dna",
			"rna, nuclear",
			"rna, poly-a enriched",
			"protein"
			);

	private Map<String, Transformer> transformers;
	private Map<String, TermSource> termSources;
	private List<String> sourceTransformers;
	private List<String> sampleTransformers;
	private List<String> extractTransformers;
	private String sourceColumnSelected;
	private String sampleColumnSelected;
	private String extractColumnSelected;
	


	public MagetabExportConfig() {
		initTermSources();
		initTransformers();
		initSourceTransformers();
		initSampleTransformers();
		initExtractTransformers();
	}
        
        public MagetabExportConfig(MagetabExportWizardBean wizardBean)
        {
            initTermSources();
            initTransformers();
            sourceColumnSelected=wizardBean.getSourceColumnSelected();
            sampleColumnSelected=wizardBean.getSampleColumnSelected();
        	extractColumnSelected=wizardBean.getExtractColumnSelected();
            sourceTransformers=new ArrayList<String>();
            sampleTransformers=new ArrayList<String>();
            extractTransformers=new ArrayList<String>();            
            Map<String, TransformerSelections> ts = wizardBean.getTransformersSelections();
            for(TransformerSelections temp:ts.values())
            {
                if(temp.isSelectedForSource())
                    sourceTransformers.add(temp.getName());                
                if(temp.isSelectedForSample())
                    sampleTransformers.add(temp.getName());
                if(temp.isSelectedForExtract())
                    extractTransformers.add(temp.getName());                
            }
        }
	
	public Map<String, Transformer> getTransformers() {
		return transformers;
	}
	
	public Map<String, TermSource> getTermSources() {
		return termSources;
	}
	
	public List<String> getSourceTransformers() {
		return sourceTransformers;
	}

	public List<String> getSampleTransformers() {              
		return sampleTransformers;
	}

	public List<String> getExtractTransformers() {
		return extractTransformers;
	}
	
	public String getSourceColumnSelected() {
		return sourceColumnSelected;
	}

	public String getSampleColumnSelected() {
		return sampleColumnSelected;
	}

	public String getExtractColumnSelected() {
		return extractColumnSelected;
	}

	private void initTransformers() {
		transformers = new LinkedHashMap<String, Transformer>();		

		addTransformer(new ProviderTransformer());
		addTransformer(new RaceTransformer(new CharacteristicTransformerConfig(getTermSources().get(NCI_THESAURUS))));
		addTransformer(new EthnicityTransformer(new CharacteristicTransformerConfig(getTermSources().get(NCI_THESAURUS))));
		addTransformer(new SexTransformer(
				new CharacteristicTransformerConfig(getTermSources().get(NCI_THESAURUS))));
		addTransformer(new AgeTransformer(
				new CharacteristicTransformerConfig(getTermSources().get(NCI_THESAURUS))));
		
		addTransformer(new DiseaseStateTransformer(
				new CharacteristicTransformerConfig(getTermSources().get(SNOMED_CT))));
		addTransformer(new PathologicalStatusTransformer(
				new CharacteristicTransformerConfig(getTermSources().get(NCI_THESAURUS))));
		
		addTransformer(new ExternalIdTransformer());
		addTransformer(new GsIdTransformer());
		addTransformer(new LabelTransformer());
		addTransformer(new OrganismPartTransformer(
				new CharacteristicTransformerConfig(getTermSources().get(ICD_O_3_TOPOGRAPHY))));
	
		addTransformer(new MaterialTypeTransformer(
				new MaterialTypeTransformerConfig(
						getTermSources().get(MGED), materialTypesMap,knownMolecularTypes)));
		
		//addTransformer(new MaterialTypeTransformer());
		addTransformer(new SpecimenTypeTransformer(
				new CharacteristicTransformerConfig(null)));	
		addTransformer(new DescriptionTransformer());
		addTransformer(new CollectionProtocolTransformer());
		addTransformer(new PatientProtocolIdTransformer());
        addTransformer(new ClinicalStatusTransformer(
                new CharacteristicTransformerConfig(getTermSources().get(
                        NCI_THESAURUS))));
		addTransformer(new CalendarEventPointLabelTransformer());

	}
	
	private void addTransformer(Transformer transformer) {
		transformers.put(transformer.getName(), transformer);
	}
	
	private void initTermSources() {
		termSources = new LinkedHashMap<String, TermSource>();
		termSources.put(MGED, new TermSource(MGED, "http://mged.sourceforge.net/ontologies/MGEDOntology1.1.8.daml", "1.1.8"));
		termSources.put(ICD_O_3_TOPOGRAPHY, new TermSource(ICD_O_3_TOPOGRAPHY, "http://www.who.int/classifications/icd/adaptations/oncology/en/", "3"));
		termSources.put(SNOMED_CT, new TermSource(SNOMED_CT, "http://www.nlm.nih.gov/research/umls/Snomed/snomed_main.html", "07-2009"));
		termSources.put(NCI_THESAURUS, new TermSource(NCI_THESAURUS, "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "11.09d"));
	}
	
	private void initSourceTransformers() {
		sourceTransformers = Arrays.asList(
//				"Name",
				"Provider",
				"Race",
				"Ethnicity",
				"Sex",
				"Age",
				"DiseaseState",
				"PathologicalStatus",
				"ExternalId",
				"Global Specimen Identifier",
				"Label",
				"OrganismPart",
				"MaterialType",
				"SpecimenType",
				"Description",
				"Collection Protocol Name",
				"Protocol Participant ID",
				"Clinical Status",
				"Calendar Event Point Label");
		
	}

	private void initSampleTransformers() {
		sampleTransformers = Arrays.asList(
		//		"Name",
		//		"Provider",
		//		"Race",
		//		"Ethnicity",
		//		"Sex",
		//		"Age",
		//		"DiseaseState",
		//		"PathologicalStatus",
				"ExternalId",
				"Global Specimen Identifier",
				"Label",
		//		"OrganismPart",
				"MaterialType",
				"SpecimenType",
				"Description",
				"Clinical Status",
				"Calendar Event Point Label");
	}
	
	private void initExtractTransformers() {
		extractTransformers = Arrays.asList(
		//		"Name",
		//		"Provider",
		//		"Race",
		//		"Ethnicity",
		//		"Sex",
		//		"Age",
		//		"DiseaseState",
		//		"PathologicalStatus",
				"ExternalId",
				"Global Specimen Identifier",
				"Label",
		//		"OrganismPart",
				"MaterialType",
				"SpecimenType",
				"Description",
				"Clinical Status",
				"Calendar Event Point Label");
			}
}
