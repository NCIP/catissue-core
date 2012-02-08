package edu.wustl.catissuecore.bizlogic.magetab;

import edu.wustl.catissuecore.domain.Specimen;

public class CalendarEventPointLabelTransformer extends AbstractCharacteristicTransformer {

	public CalendarEventPointLabelTransformer() {
		super("Calendar Event Point Label", "Calendar Event Point Label", "Calendar Event Point Label");
	}
	
	@Override
	public String getCharacteristicValue(Specimen specimen) {
        Double scep = specimen.getSpecimenCollectionGroup().getCollectionProtocolEvent().getStudyCalendarEventPoint();
        String cplabel = specimen.getSpecimenCollectionGroup().getCollectionProtocolEvent().getCollectionPointLabel();
        return (scep != null ? (scep.toString() + " " + cplabel) : cplabel);
	}
	
	@Override
	public boolean isMageTabSpec() {
		// TODO Auto-generated method stub
		return false;
	}

}
