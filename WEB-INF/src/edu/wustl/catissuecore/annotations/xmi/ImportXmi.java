
package edu.wustl.catissuecore.annotations.xmi;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.xmi.XMIConfiguration;
import edu.common.dynamicextensions.xmi.importer.AbstractXMIImporter;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.domain.StudyFormContext;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

/**
 * @author ashish_gupta
 *
 */
public class ImportXmi extends AbstractXMIImporter
{

	static
	{
		LoggerConfig.configureLogger( System.getProperty( "user.dir" ) );
	}
	private static Logger logger = Logger.getCommonLogger( ImportXmi.class );

	/**
	 * main method
	 * @param args command line arguments
	 */
	public static void main(String[] args)
	{
		ImportXmi xmiImporter = new ImportXmi();

		if (args.length >= 4)
		{
			if (AnnotationConstants.ENTITY_NAME_PARTICIPANT.equals(args[3]))
			{
				args[3] = AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY;
			}
			else if (AnnotationConstants.ENTITY_NAME_SPECIMEN.equals(args[3]))
			{
				args[3] = AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY;
			}
			else if (AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP.equals(args[3]))
			{
				args[3] = AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY;
			}
		}

		xmiImporter.importXMI(args);
	}

	/**
	 * @param isEntityGroupSystemGenerated true if entity group is system generated
	 * @return XMIConfiguration object.
	 */
	protected XMIConfiguration getXMIConfigurationObject()
	{
		final XMIConfiguration xmiConfiguration = XMIConfiguration.getInstance();
		xmiConfiguration.setCreateTable( true );
		xmiConfiguration.setAddIdAttr( true );
		xmiConfiguration.setAddColumnForInherianceInChild( false );
		xmiConfiguration.setAddInheritedAttribute( false );
		xmiConfiguration.setEntityGroupSystemGenerated(false);
		return xmiConfiguration;
	}


	protected List<AssociationInterface> getAssociationListForCurratedPath(HibernateDAO hibernatedao)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		// TODO Auto-generated method stub
		return new ArrayList<AssociationInterface>();
	}



	protected void postProcess(boolean isEditedXmi, String coRecObjCsvFName,
			List<ContainerInterface> mainContainerList, String domainModelName)
			throws BizLogicException, DAOException, DynamicExtensionsApplicationException
	{
		// TODO Auto-generated method stub
		for(ContainerInterface container : mainContainerList)
		{
			StudyFormContext sfc = new StudyFormContext();
			sfc.setActivityStatus("Active");
			sfc.setHideForm(false);
			sfc.setContainerId(container.getId());
			final AnnotationBizLogic annotation = new AnnotationBizLogic();
			annotation.insert(sfc);
		}
	}

}
