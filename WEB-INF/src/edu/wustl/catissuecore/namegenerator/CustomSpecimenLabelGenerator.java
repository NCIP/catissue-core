package edu.wustl.catissuecore.namegenerator;

import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
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
			throw new LabelGenException("Specimen status is not "+Constants.COLLECTION_STATUS_COLLECTED);
		}
		if (objSpecimen.getLabel() != null)
		{
			throw new LabelGenException("Label already assigned");
		}
			final Specimen parentSpecimen = (Specimen) objSpecimen.getParentSpecimen();
			if (isAliquot(objSpecimen))
			{

				StringBuffer buffer = new StringBuffer();
				String labelFormat=objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat();
				StringTokenizer st = new StringTokenizer(labelFormat.toString(), "%");
				while (st.hasMoreTokens())
				{
					String token=st.nextToken();
			         try
					{

			        	 String mylable = TokenFactory.getInstance(token).getTokenValue(objSpecimen, token);
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

				currentLabel= currentLabel+1;
					objSpecimen.setLabel(buffer.toString()+"_"+currentLabel);
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
}
