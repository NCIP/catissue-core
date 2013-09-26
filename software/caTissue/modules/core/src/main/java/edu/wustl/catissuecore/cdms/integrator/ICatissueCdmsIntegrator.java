/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


package edu.wustl.catissuecore.cdms.integrator;

public interface ICatissueCdmsIntegrator
{

	public String getVisitInformationURL(CatissueCdmsURLInformationObject urlInformationObject,
			String loginName, String password) throws Exception;
}
