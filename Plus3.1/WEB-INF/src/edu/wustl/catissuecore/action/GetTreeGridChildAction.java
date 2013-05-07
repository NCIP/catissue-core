package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.action.BaseAction;
/*import edu.wustl.common.queryFolder.beans.AssignTag;
import edu.wustl.common.queryFolder.beans.Tag;
import edu.wustl.common.queryFolder.bizlogic.TagBizLogic;*/

public class GetTreeGridChildAction extends BaseAction
{

	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String tagIds = (String) request.getParameter("tagId");
/*
		CatissueDefaultBizLogic bizLogic = new CatissueDefaultBizLogic();
		long tagId = Long.parseLong(tagIds);
		TagBizLogic tagBizLogic = new TagBizLogic();
		Tag tagById = tagBizLogic.getTagById(tagId);
		JSONArray treeData = new JSONArray();
		Set<AssignTag> assignTagList = tagById.getAssignTag();
		int childCount=0;
		for (AssignTag assignTag : assignTagList) 
		{
			List objDetail = AppUtility.getObjDetails(assignTag.getObjId());
			JSONObject obj = new JSONObject();
			if(objDetail != null && objDetail.size() > 0)
			{
				List list = (List)objDetail.get(0);
				obj.put(AQConstants.IDENTIFIER , list.get(1));
				obj.put(AQConstants.NAME, list.get(0));
				childCount++;
				treeData.put(obj);
			}
 	 }
		JSONObject arrayObj = new JSONObject();
		arrayObj.put(AQConstants.TREE_DATA, treeData);
		arrayObj.put("childCount", childCount);
		response.flushBuffer();
		PrintWriter out = response.getWriter();
		out.write(arrayObj.toString());*/
		return null;
	}

}
