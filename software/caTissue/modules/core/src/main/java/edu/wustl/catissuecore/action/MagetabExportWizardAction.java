package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.MagetabExportWizardForm;
import edu.wustl.catissuecore.bizlogic.magetab.MagetabExportBizLogic;
import edu.wustl.catissuecore.bizlogic.magetab.MagetabExportConfig;
import edu.wustl.catissuecore.bizlogic.magetab.MagetabExportWizardBean;
import edu.wustl.catissuecore.bizlogic.magetab.MagetabExportWizardBean.State;
import edu.wustl.catissuecore.bizlogic.magetab.TransformerSelections;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;

/**
 * 
 * @author Alexander Zgursky
 * @author Srikalyan Swayampakula.
 */
public class MagetabExportWizardAction extends BaseAction{

//	private static final Map<State, String> operationsToForwardNames =  
//		new HashMap<State, String>() {{
//			   put(State.NEW, "Potter");
//		}};
//	
	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(MagetabExportAction.class);
	
	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response : response obj
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		MagetabExportWizardBean wizardBean = (MagetabExportWizardBean)
				session.getAttribute(MagetabExportWizardBean.MAGETAB_EXPORT_WIZARD_BEAN);
		State state = wizardBean.getState();
		logger.debug("State is "+state);
		switch (wizardBean.getState()) {
		case NEW :
			throw new  RuntimeException("MagetabExportWizardBean is not initialized");
		case DEACTIVATED :
			return mapping.findForward("page1");
		case READY :
			updateWizardBean(wizardBean, (MagetabExportWizardForm) form);
			String operation = request.getParameter(Constants.OPERATION);
			if (operation == null || operation.isEmpty()) {
				return mapping.findForward("page1");
			} else if (operation.equalsIgnoreCase("back")) {
				wizardBean.setCurrentPage(Math.max(wizardBean.getCurrentPage() - 1, 1));
				return mapping.findForward("page" + wizardBean.getCurrentPage());
			} else if (operation.equalsIgnoreCase("forward")) {
				int nextPage = wizardBean.getCurrentPage() + 1;
				ActionForward result = mapping.findForward("page" + nextPage);
				if (result != null) {
					wizardBean.setCurrentPage(nextPage);
				} else {
					result = mapping.findForward("page" + wizardBean.getCurrentPage());
				}
				return result;
			} else if (operation.equalsIgnoreCase("export")) {
				
				Set<Integer> chainSelections = wizardBean.getSseTable().getChainSelections(); 
				List<List<Specimen>> chains = wizardBean.getSseTable().getSpecimenChains();
				List<List<Specimen>> filteredChains = new ArrayList<List<Specimen>>(chainSelections.size());
				for (int i : chainSelections) {
					filteredChains.add(chains.get(i));
				}
				
				MagetabExportBizLogic bizLogic = new MagetabExportBizLogic();				
                                MagetabExportConfig config=new MagetabExportConfig(wizardBean);                                
				String path = CommonServiceLocator.getInstance().getAppHome() + System.getProperty("file.separator");
				String zipFileName = path + session.getId() + Constants.ZIP_FILE_EXTENTION;
				bizLogic.export(filteredChains, config, zipFileName);
				SendFile.sendFileToClient(response, zipFileName, "magetab.zip", "application/download");
				return null;//mapping.findForward("page" + wizardBean.getCurrentPage());
			} else if (operation.equalsIgnoreCase("done")) {
				wizardBean.deactivate();
				return mapping.findForward("page1");
			} else {
				return mapping.findForward("page1");
			}
		case WORKING :
		default :
			return null;
		}
		
		
		
	}
	
	private void updateWizardBean(
			MagetabExportWizardBean wizardBean, MagetabExportWizardForm wizardForm)
	{
		int pageNo = 0;
		try {
			pageNo = Integer.parseInt(wizardForm.getPageNo());
		} catch (RuntimeException ex) {
			//It's ok
		}
		
		if (pageNo == 1) {
                        wizardBean.setCaArrayEnabled(wizardForm.getCaArrayEnabled());                        
			if (wizardForm.getDefaultExtractType() != null) {
				wizardBean.setDefaultExtractType(wizardForm.getDefaultExtractType());
			}
		} else if (pageNo == 2) {
			Set<Integer> chainSelections = new HashSet<Integer>();  
			wizardBean.setDisplayColumns(wizardForm.getDisplayColumns());
			if (wizardForm.getChains() != null) {
				for (String token : wizardForm.getChains()) {
					if (token.startsWith("chk_chain_")) {
						int selectedChain = Integer.parseInt(token.substring("chk_chain_".length()));
						chainSelections.add(selectedChain);
					}
				}
			}
                        wizardBean.setCaArrayEnabled(wizardForm.getCaArrayEnabled());
			wizardBean.getSseTable().setChainSelections(chainSelections);
			
		} else if (pageNo == 3) {
			wizardBean.setSampleColumnSelected(wizardForm.getSampleColumnSelected());
			wizardBean.setSourceColumnSelected(wizardForm.getSourceColumnSelected());
			wizardBean.setExtractColumnSelected(wizardForm.getExtractColumnSelected());
			Map<String, TransformerSelections> ts = wizardBean.getTransformersSelections();
			
			for (TransformerSelections sel : ts.values()) {
				sel.setSelectedForSource(false);
				sel.setSelectedForSample(false);
				sel.setSelectedForExtract(false);
			}
			
			if (wizardForm.getTransformers() != null) {
				for (String token : wizardForm.getTransformers()) {                                    
					if (token.startsWith("chk_source_")) {
						String name = token.substring("chk_source_".length());
						TransformerSelections sel = ts.get(name);
						sel.setSelectedForSource(true);
					} else if (token.startsWith("chk_sample_")) {
						String name = token.substring("chk_sample_".length());
						TransformerSelections sel = ts.get(name);
						sel.setSelectedForSample(true);
					} else if (token.startsWith("chk_extract_")) {
						String name = token.substring("chk_extract_".length());
						TransformerSelections sel = ts.get(name);
						sel.setSelectedForExtract(true);
					}
				}
			}
		}
	}
}
