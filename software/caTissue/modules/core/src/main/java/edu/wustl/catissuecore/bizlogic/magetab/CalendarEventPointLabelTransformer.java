package edu.wustl.catissuecore.bizlogic.magetab;

import edu.wustl.catissuecore.domain.Specimen;

public class CalendarEventPointLabelTransformer extends AbstractCharacteristicTransformer {

	public CalendarEventPointLabelTransformer() {
		super("Calendar Event Point Label", "Calendar Event Point Label", "Calendar Event Point Label");
	}
	
	@Override
	public String getCharacteristicValue(Specimen specimen) {
		// TODO Auto-generated method stub
		return specimen.getSpecimenCollectionGroup().getCollectionProtocolEvent().getStudyCalendarEventPoint().toString()  + " " +
		specimen.getSpecimenCollectionGroup().getCollectionProtocolEvent().getCollectionPointLabel();
	}

}
