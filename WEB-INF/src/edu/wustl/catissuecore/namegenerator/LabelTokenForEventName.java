package edu.wustl.catissuecore.namegenerator;
import edu.wustl.catissuecore.domain.Specimen;

/*Class LabelTokenForEventName*/
/*@author gupta/dimaggio 11/17/2011*/

public class LabelTokenForEventName implements LabelTokens
{
                /**
                * This will return the value of the token provided.
                * @see edu.wustl.catissuecore.namegenerator.LabelTokens#getTokenValue(java.lang.Object,
                * java.lang.String, java.lang.Long)
                */

                public String getTokenValue(Object object)
                {
                                Specimen objSpecimen = (Specimen) object;
                                return objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolEvent().getCollectionPointLabel();
                }
}
