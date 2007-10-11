package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimenVO;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;

/**
 * @author abhijit_naik
 *
 */
public class MultipleSpecimenViewAction extends AnticipatorySpecimenViewAction 
{

	private SessionDataBean bean;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SpecimenCollectionGroupForm specimenCollectionGroupForm=
			(SpecimenCollectionGroupForm)form;

		HttpSession session = request.getSession();
		bean = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
		try{
			LinkedHashMap<String, CollectionProtocolEventBean> cpEventMap = new LinkedHashMap<String, CollectionProtocolEventBean> ();
			CollectionProtocolEventBean eventBean = new CollectionProtocolEventBean();
			
			eventBean.setUniqueIdentifier("EventID-1");
			Collection specimenCollection = (Collection) session
			.getAttribute(Constants.SPECIMEN_LIST_SESSION_MAP);
			
			eventBean.setSpecimenRequirementbeanMap(getSpecimensMap(
					specimenCollection));

			String globalSpecimenId = "E"+eventBean.getUniqueIdentifier() + "_";
			cpEventMap.put(globalSpecimenId, eventBean);			
			session.removeAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
			session
			.setAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP, cpEventMap);			
		
			Set<String> keySet = autoStorageSpecimenMap.keySet();
			if (!keySet.isEmpty())
			{
				Iterator<String> keySetIterator = keySet.iterator();
				storageContainerIds.clear();
				while(keySetIterator.hasNext())
				{
					String key = keySetIterator.next();
					ArrayList<GenericSpecimenVO> specimenList =
						autoStorageSpecimenMap.get(key);
					setSpecimenStorageDetails(specimenList,key);
				}
			}
			
			return mapping.findForward(Constants.SUCCESS);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
		
	}

}
