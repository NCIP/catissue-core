package edu.wustl.catissuecore.GSID;

import java.util.List;

import org.apache.log4j.Logger;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import gov.nih.nci.logging.api.util.StringUtils;

/****
 * @author srikalyan
 */
public class GSIDUtil {
	//private static final Log LOG = LogFactory.getLog(GSIDUtil.class);
	private static Logger LOG = Logger.getLogger(GSIDClient.class);
	private GSIDClient gsidClient;

	public GSIDUtil(GSIDClient gsidClient) {
		this.gsidClient = gsidClient;
	}
	
	public GSIDUtil()
	{
		this.gsidClient=new GSIDClient();
	}

	/****
	 * Registers a GSID for the Specimen.
	 * @param specimen
	 * @return
	 * @throws GSIDException 
	 */
	public Specimen registerSpecimen(Specimen specimen, String suggestedIdentifier) throws GSIDException  {

		if ( (specimen != null && StringUtils.isBlank(specimen.getGlobalSpecimenIdentifier())) || suggestedIdentifier != null  ) {
			String[] parentIdentifiers = null;
			if (specimen.getParentSpecimen() != null
					&& specimen.getParentSpecimen() instanceof Specimen) {
				Specimen parentSpecimen = (Specimen) specimen
						.getParentSpecimen();
				if (parentSpecimen != null
						&& !StringUtils.isBlank(parentSpecimen
								.getGlobalSpecimenIdentifier())) {
					parentIdentifiers = new String[1];
					parentIdentifiers[0] = parentSpecimen
							.getGlobalSpecimenIdentifier();
				}
				else
				{
					// parent has no specimen so return unchanged specimen
					return specimen;
				}
			}
			String identifier = gsidClient.getGSID(suggestedIdentifier, parentIdentifiers,true);
			LOG.error(GSIDConstant.GSID_IDENTIFIER_PRINT_MSG+ identifier);
			if (!StringUtils.isBlank(identifier)) {
				specimen.setGlobalSpecimenIdentifier(identifier);
			}
		} else {
			if (specimen != null)
				LOG.debug(GSIDConstant.GSID_IDENTIFIER_PRINT_MSG
						+ specimen.getGlobalSpecimenIdentifier());
		}
		return specimen;
	}
	
	/************
	 * This method is used to register GSID for a specimen with a given id.
	 * @param id identifier of the specimen.
	 * @param sessionBean session data bean instance.
	 * @return
	 * @throws GSIDException 
	 */
	public Specimen registerSpecimenById(long id, SessionDataBean sessionBean) throws GSIDException
	{
		final String applicationName = CommonServiceLocator.getInstance()
		.getAppName();
		DAO dao = null;
		Specimen specimen=null;
		try 
		{
			dao = DAOConfigFactory.getInstance().getDAOFactory(applicationName)
					.getDAO();			
			dao.openSession(sessionBean);
			final String sourceObjectName = Specimen.class.getName();
			List<Specimen> list = dao
					.executeQuery("select md from "
							+ sourceObjectName
							+ " md "
							+ "where md.id= "+id);
			if(list.size()>0)
			{
				specimen=list.get(0);				
				registerSpecimen(specimen,null);
				dao.update(specimen);
				dao.commit();			
				dao.closeSession();
			}		
		}
		catch(DAOException e)
		{
			try {
				dao.closeSession();
			} catch (DAOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return specimen;
	}
}