package edu.wustl.catissuecore.action.bulkOperations;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.nutility.BOTemplateGenerator;
import edu.wustl.bulkoperator.dao.BulkOperationDao;

public class BOTemplateUpdater implements BOTemplateGenerator {

    private String templateFormat = "%s_%d_%s";
    @Override
    public void update(Long containerId) {
        BulkOperationDao boDao = new BulkOperationDao(JdbcDaoFactory.getJdbcDao());
        Container c = Container.getContainer(containerId);
        String.format(templateFormat, c.getCaption(), c.getId(), "Participant");

        String participantTemplate =  String.format(templateFormat, c.getCaption(), c.getId(), "Participant");
        String scgTemplate =  String.format(templateFormat, c.getCaption(), c.getId(), "SpecimenCollectionGroup");
        String specimenTemplate = String.format(templateFormat, c.getCaption(), c.getId(), "Specimen");

        try {
            BOTemplateGeneratorUtil generator = new BOTemplateGeneratorUtil();
            if (boDao.doesTemplateExists(participantTemplate)) {
                generator.generateAndUploadTemplate(c.getId(), "Participant");
            }
            if (boDao.doesTemplateExists(scgTemplate)) {
                generator.generateAndUploadTemplate(c.getId(), "SpecimenCollectionGroup");
            }
            if (boDao.doesTemplateExists(specimenTemplate)) {
                generator.generateAndUploadTemplate(c.getId(), "Specimen");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error in updating BO Template", e);
        }

    }
}
