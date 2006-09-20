package edu.wustl.catissuecore.action;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.model.AppletModelInterface;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.util.SpecimenArrayAppletUtil;



/**
 * <p>This class initializes the fields of SpecimenArrayAppletAction.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayAppletAction extends Action {
	
	/**
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ObjectInputStream inputStream = new ObjectInputStream(request.getInputStream());
		Object object = inputStream.readObject();
		System.out.println(object);
		ObjectOutputStream outputStream = new ObjectOutputStream(response.getOutputStream());
		int rowCount = 3;
		int columnCount = 3;
		String value = "";
		Map tableModelMap = new HashMap();
		for (int i=0; i < rowCount ; i++) {
		  for (int j=0;j < columnCount; j++) {
			for(int k=0; k < AppletConstants.ARRAY_CONTENT_ATTRIBUTE_NAMES.length; k++) {
				if ((k == 2) || (k == 3)) {
					value = "20";
				} else {
					value = "";
				}
				tableModelMap.put(SpecimenArrayAppletUtil.getArrayMapKey(i,j,columnCount,k),value);
			}
		  }
		}
		AppletModelInterface model = new BaseAppletModel();
		model.setData(tableModelMap);
		outputStream.writeObject(model);
		return null;
	}
}
