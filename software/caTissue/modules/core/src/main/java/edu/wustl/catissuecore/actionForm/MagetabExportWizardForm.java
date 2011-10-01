package edu.wustl.catissuecore.actionForm;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

/**
 * 
 * @author Alexander Zgursky
 *
 */
public class MagetabExportWizardForm extends ActionForm implements Serializable {

	private static final long serialVersionUID = -8474586245309260780L;	
	private String pageNo;
	private String defaultExtractType;
	private String[] transformers;
	private String[] chains;
        private String[] caArrayEnabled;   
        private String displayColumns;
        
    	private String sourceColumnSelected;
    	private String sampleColumnSelected;
    	private String extractColumnSelected;

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

	public String getDisplayColumns() {
			return displayColumns;
		}

		public void setDisplayColumns(String displayColumns) {
			this.displayColumns = displayColumns;
		}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getDefaultExtractType() {
		return defaultExtractType;
	}

	public void setDefaultExtractType(String defaultExtractType) {
		this.defaultExtractType = defaultExtractType;
	}

	public String[] getChains() {
		return chains;
	}

	public void setChains(String[] chains) {
		this.chains = chains;
	}

	public String[] getTransformers() {            
		return transformers;
	}

	public void setTransformers(String[] transformers) {                           
		this.transformers = transformers;
	}

        public String[] getCaArrayEnabled() {           
            return caArrayEnabled;
        }

        public void setCaArrayEnabled(String[] caArrayEnabled) {          
            this.caArrayEnabled = caArrayEnabled;
        }     
        
}
