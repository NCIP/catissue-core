
package edu.wustl.catissuecore.cdms.integrator;

public interface ICatissueCdmsIntegrator
{

	public String getVisitInformationURL(CatissueCdmsURLInformationObject urlInformationObject,
			String loginName, String password) throws Exception;
}
