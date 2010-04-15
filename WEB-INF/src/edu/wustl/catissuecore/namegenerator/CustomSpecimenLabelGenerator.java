package edu.wustl.catissuecore.namegenerator;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.SpecimenUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;


/**
 * The Class CustomSpecimenLabelGenerator.
 */
/**
 * @author nitesh_marwaha
 *
 */
public class CustomSpecimenLabelGenerator extends DefaultSpecimenLabelGenerator
{

	/** The logger. */
	private static Logger logger = Logger.getCommonLogger(CustomSpecimenLabelGenerator.class);

	private Long currentValue;

	public Long getCurrentValue()
	{
		return currentValue;
	}


	public void setCurrentValue(Long currentValue)
	{
		this.currentValue = currentValue;
	}

	/**
	 * Instantiates a new custom specimen label generator.
	 *
	 * @throws ApplicationException the application exception
	 */
	public CustomSpecimenLabelGenerator() throws ApplicationException
	{
		super();

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.DefaultSpecimenLabelGenerator#setLabel(java.lang.Object)
	 */
	@Override
	/**
	 * Setting label.
	 * @param obj Specimen object
	 */
	public synchronized void setLabel(Object obj) throws LabelGenException
	{

		final Specimen objSpecimen = (Specimen) obj;
		if(objSpecimen.getCollectionStatus() == null || !Constants.COLLECTION_STATUS_COLLECTED.equals(objSpecimen.getCollectionStatus()))
		{
			return ;//throw new LabelGenException("Specimen status is not "+Constants.COLLECTION_STATUS_COLLECTED);
		}
		if (objSpecimen.getLabel() != null)
		{
			return;//throw new LabelGenException("Label already assigned");
		}
			final Specimen parentSpecimen = (Specimen) objSpecimen.getParentSpecimen();
			if (isAliquot(objSpecimen))
			{

				StringBuffer buffer = new StringBuffer();
				String labelFormat=objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat();
				if(labelFormat == null)
					labelFormat = "";
				StringTokenizer st = new StringTokenizer(labelFormat.toString(), "%");
				while (st.hasMoreTokens())
				{
					String token=st.nextToken();
//					if(token.equals("ID"))
//					{
//						currentLabel= currentLabel+1;
//						buffer.append(currentLabel);
//						//objSpecimen.setLabel(buffer.toString()+"_"+currentLabel);
//					}
//					else
//					{
				         try
						{

				        	 String mylable = TokenFactory.getInstance(token).getTokenValue(objSpecimen, token, currentValue);
				        	 if(mylable == null)
				        	 {
				        		 mylable = "";
				        	 }
							buffer.append(mylable);

						}
				         catch (final Exception ex)
				 		{
				        	logger.error(ex.getMessage());
				 		}
					}
//			    }
//				if(!labelFormat.contains("ID"))
//				{
//					currentLabel= currentLabel+1;
//					objSpecimen.setLabel(buffer.toString()+"_"+currentLabel);
//				}
				objSpecimen.setLabel(buffer.toString());
				//currentLabel= currentLabel+1;
					//objSpecimen.setLabel(buffer.toString()+"_"+currentLabel);
			}
			else
			{
				this.setNextAvailableAliquotSpecimenlabel(parentSpecimen, objSpecimen);
			}

			if (objSpecimen.getChildSpecimenCollection().size() > 0)
			{
				final Collection<AbstractSpecimen> specimenCollection = objSpecimen
						.getChildSpecimenCollection();
				final Iterator<AbstractSpecimen> specCollItr = specimenCollection.iterator();
				while (specCollItr.hasNext())
				{
					final Specimen objChildSpecimen = (Specimen) specCollItr.next();
					this.setLabel(objChildSpecimen);
				}
			}
	}

	/**
	 * Checks if is aliquot.
	 *
	 * @param objSpecimen the obj specimen
	 *
	 * @return true, if checks if is aliquot
	 */
	private boolean isAliquot(final Specimen objSpecimen)
	{
		if(objSpecimen.getLineage() == null)
		{
			objSpecimen.setLineage(Constants.NEW_SPECIMEN);
		}
		return objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN) ||objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN);
	}


	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.DefaultSpecimenLabelGenerator#setNextAvailableDeriveSpecimenlabel(edu.wustl.catissuecore.domain.Specimen, edu.wustl.catissuecore.domain.Specimen)
	 */
	@Override
	/**
	 * @param parentObject parent obj
	 * @param specimenObject specimen obj
	 */
	synchronized void setNextAvailableDeriveSpecimenlabel(Specimen parentObject,
			Specimen specimenObject)
	{
		this.currentLabel = this.currentLabel + 1;
		specimenObject.setLabel(this.currentLabel.toString());
	}

	public synchronized void setLabel(Collection<AbstractDomainObject> object) throws LabelGenException
	{
		Iterator<AbstractDomainObject> iterator = object.iterator();

			SpecimenCollectionGroup scg = null;
			while (iterator.hasNext())
			{
				final Specimen newSpecimen = (Specimen) iterator.next();
					if(currentValue == null || currentValue == 0)
					{
						currentValue = getSpecimenCountforPPI(newSpecimen);
//						if(newSpecimen.getSpecimenCollectionGroup() == null || newSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration() == null)
//						{
//
//						}
					}
					else
					{
						currentValue = currentValue+1;
					}
					this.setLabel(newSpecimen);
			}
	}

	private Long getSpecimenCountforPPI(Specimen  specimen)
	{
		Long count=0l;
//		String ppi=specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getProtocolParticipantIdentifier();
		Long cprId = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getId();
		String yearOfColl = SpecimenUtil.getCollectionYear(specimen);
			String hql= "select count(specimen) from edu.wustl.catissuecore.domain.Specimen as specimen"
				+" where specimen.specimenCollectionGroup.collectionProtocolRegistration.id ="+ cprId
				+" and (specimen.lineage='New' or specimen.lineage='Derived') and specimen.collectionStatus = 'Collected'";
		List<Object[]> list=null;
		try
		{
			list=AppUtility.executeQuery(hql);
			if(list!=null)
			{
				Object object = list.get(0);
				count=Long.valueOf(object.toString());
			}
		}
		catch(ApplicationException exp)
		{
			logger.error(exp.getMessage());
		}
		return count;
	}

	private List getSCGData(Specimen spec)
	{
		String hql = "select scg.collectionProtocolRegistration.collectionProtocol.generateLabel," +
		" scg.collectionProtocolRegistration.protocolParticipantIdentifier, " +
		"scg.id "  +
		"from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg where scg.id ="+ spec.getSpecimenCollectionGroup().getId();

		List list = null;
		try
		{
			list = AppUtility.executeQuery(hql);

			Object[] obje = (Object[])list.get(0);
			boolean generateLabel = (Boolean)obje[0];
			String PPI= obje[1].toString();
			Long scgId = Long.valueOf(obje[2].toString());
		}
		catch (ApplicationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
