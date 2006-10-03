import gov.nih.nci.system.applicationservice.*;
import java.util.*;

//import gov.nih.nci.cabio.domain.*;
//import gov.nih.nci.cabio.domain.impl.*;
import gov.nih.nci.common.util.*;

import edu.wustl.catissuecore.domain.*;
import org.hibernate.criterion.*;
import gov.nih.nci.system.comm.client.ClientSession;

import edu.wustl.common.domain.AbstractDomainObject;
/*
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Variables;
import org.apache.log4j.PropertyConfigurator;
*/
/**
 * <!-- LICENSE_TEXT_START -->
* Copyright 2001-2004 SAIC. Copyright 2001-2003 SAIC. This software was developed in conjunction with the National Cancer Institute,
* and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
* in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
* "This product includes software developed by the SAIC and the National Cancer Institute."
* If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
* wherever such third-party acknowledgments normally appear.
* 3. The names "The National Cancer Institute", "NCI" and "SAIC" must not be used to endorse or promote products derived from this software.
* 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
* the recipient to use any trademarks owned by either NCI or SAIC-Frederick.
* 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
* SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author caBIO Team
 * @version 1.0
 */



/**
	* TestClient.java demonstartes various ways to execute searches with and without
      * using Application Service Layer (convenience layer that abstracts building criteria
      * Uncomment different scenarios below to demonstrate the various types of searches
*/

public class TestClient 
{
static ApplicationService appService = null;

    public static void main(String[] args) 
	{
	
		System.out.println("*** TestClient...");
		try
		{
			//ApplicationService appService = ApplicationService.getRemoteInstance("http://localhost:8080/catissuecoresdk/server/HTTPServer");
			//ApplicationService appService = ApplicationServiceProvider.getApplicationService();
			ApplicationServiceProvider applicationServiceProvider = new ApplicationServiceProvider(); 
			appService = applicationServiceProvider.getApplicationService();
			ClientSession cs = ClientSession.getInstance();
			try
			{ 
				cs.startSession("admin@admin.com", "login123"); 
			} 
			catch (Exception ex) 
			{ 
				System.out.println(ex.getMessage()); 
				ex.printStackTrace();
				return;
			}

			try 
			{
			TestClient testClient = new TestClient();
				testClient.createObjects();
				//System.out.println("Retrieving all genes based on symbol=IL*");
				//APIDemo api = new APIDemo();

				/*Department department = getDepartmentForInsertion(2);
				appService.createObject(department);
				*/
				//Object obj = api.initCancerResearchGroup();
				//appService.createObject(obj);

				/*	
				Department department = getDepartmentForUpdation(1);
				appService.updateObject(department);
				*/
				/*try 
				{
					List resultList = appService.search(Department.class, department);
					for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
					{
						Department returneddepartment = (Department) resultsIterator.next();
						System.out.println(returneddepartment.getName());
					}
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}*/
			} 
			catch (RuntimeException e2) 
			{
				e2.printStackTrace();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Test client throws Exception = "+ ex);
		}
	}
	
	/*private static Department getDepartmentForInsertion(int uniqueId)
	{
				Department department = new Department();
				department.setName("test_department");
				return department;
	}
	
	private static Department getDepartmentForUpdation(int id)
	{
				Department department = new Department();
				department.setId(new Long(id));
				department.setName("Up_Object_from_Client_" + id);
				return department;
	}*/

	private void createObjects()
	{
	try
	{
				APIDemo api = new APIDemo();
				Object obj = api.initCancerResearchGroup();
				appService.createObject(obj);

				obj = api.initDepartment();
				appService.createObject(obj);

				/*obj = api.initBioHazard();
				appService.createObject(obj);

				obj = api.initInstitution();
				appService.createObject(obj);

				obj = api.initSite();
				appService.createObject(obj);

				obj = api.initStorageType();
				appService.createObject(obj);

				obj = api.initSpecimenArrayType();
				appService.createObject(obj);

				obj = api.initStorageContainer();
				appService.createObject(obj);

				obj = api.initUser();
				appService.createObject(obj);

				obj = api.initParticipant();
				appService.createObject(obj);

				obj = api.initCollectionProtocol();
				appService.createObject(obj);

				obj = api.initSpecimen();
				appService.createObject(obj);

				obj = api.initSpecimenCollectionGroup();
				appService.createObject(obj);

				obj = api.initDistribution();
				appService.createObject(obj);

				obj = api.initDistributionProtocol();
				appService.createObject(obj);

				obj = api.initCollectionProtocolRegistration();
				appService.createObject(obj);
				
				obj = api.initSpecimenArray();
				appService.createObject(obj);*/
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Test client throws Exception = "+ ex);
		}


	}

/*	private void updateObjects(int id)
	{
				APIDemo api = new APIDemo();
				Object obj = api.initCancerResearchGroup();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initDepartment();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initBioHazard();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initInstitution();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initSite();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initStorageType();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initSpecimenArrayType();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initStorageContainer();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initUser();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initParticipant();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initCollectionProtocol();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initSpecimen();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initSpecimenCollectionGroup();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initDistribution();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initDistributionProtocol();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initCollectionProtocolRegistration();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

				obj = api.initSpecimenArray();
				AbstractDomainObject domainObject = setId(obj,new Long(1)) ;
				appService.updateObject(domainObject);

	}

	private AbstractDomainObject setId(Object obj,Long id)
	{
		AbstractDomainObject domainObject = (AbstractDomainObject) obj;
		domainObject.setId(id);
		return domainObject;
	}*/

}


