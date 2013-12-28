package krishagni.catissueplus.csd;

import javax.servlet.http.HttpServletRequest;

import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dynamicextensions.formdesigner.usercontext.AppUserContextProvider;

public class CatissueUserContextProviderImpl implements AppUserContextProvider {

	@Override
	public UserContext getUserContext(HttpServletRequest request) {
		final SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute("sessionData");
		
		return new UserContext() {	
			@Override
			public String getUserName() {
				return sessionDataBean.getUserName();
			}

			@Override
			public Long getUserId() {
				return sessionDataBean.getUserId();
			}

			@Override
			public String getIpAddress() {
				return sessionDataBean.getIpAddress();
			}
		};
	}

	@Override
	public String getUserNameById(Long arg0) {
		return "";
	}

}
