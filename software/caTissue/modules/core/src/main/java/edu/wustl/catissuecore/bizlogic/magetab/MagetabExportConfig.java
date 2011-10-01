package edu.wustl.catissuecore.bizlogic.magetab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MagetabExportConfig {
	private static Map<String, String> materialTypesMap = new HashMap<String, String>() {{	
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
		
//		addTransformer(new NameTransformer());
		addTransformer(new ProviderTransformer());
		addTransformer(new RaceTransformer());
		addTransformer(new EthnicityTransformer());
		addTransformer(new SexTransformer(
				new CharacteristicTransformerConfig(getTermSources().get("MO"))));
		addTransformer(new AgeTransformer(
				new CharacteristicTransformerConfig(null)));
		
		addTransformer(new DiseaseStateTransformer(
				new CharacteristicTransformerConfig(getTermSources().get("SNOMED-CT"))));
		addTransformer(new PathologicalStatusTransformer(
				new CharacteristicTransformerConfig(null)));
		
		addTransformer(new ExternalIdTransformer());
		addTransformer(new OrganismPartTransformer(
				new CharacteristicTransformerConfig(getTermSources().get("ICD-O-3 Topography"))));
	
		addTransformer(new MaterialTypeTransformer(
				new MaterialTypeTransformerConfig(
						getTermSources().get("MO"), materialTypesMap,knownMolecularTypes)));
		
		addTransformer(new SpecimenTypeTransformer(
				new CharacteristicTransformerConfig(null)));	
		addTransformer(new DescriptionTransformer());


	}
	
	private void addTransformer(Transformer transformer) {
		transformers.put(transformer.getName(), transformer);
	}
	
	private void initTermSources() {
		termSources = new LinkedHashMap<String, TermSource>();
		termSources.put("MO", new TermSource("MO", "http://mged.sourceforge.net/ontologies/MGEDOntology1.1.8.daml", "1.1.8"));
		termSources.put("ICD-O-3 Topography", new TermSource("ICD-O-3 Topography", "", ""));
		termSources.put("SNOMED-CT", new TermSource("SNOMED-CT", "", ""));
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
				"OrganismPart",
				"MaterialType",
				"SpecimenType",
				"Description");
		
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
		//		"OrganismPart",
				"MaterialType",
				"SpecimenType",
				"Description");
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
		//		"OrganismPart",
				"MaterialType",
				"SpecimenType",
				"Description");
			}
}
