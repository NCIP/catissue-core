package edu.wustl.catissuecore.bizlogic.uidomain;

import java.util.HashSet;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.DistributionProtocolForm;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.util.MapDataParser;

@InputUIRepOfDomain(DistributionProtocolForm.class)
public class DistributionProtocolTransformer
        extends
            AbstractSpecimenProtocolTransformer<DistributionProtocolForm, DistributionProtocol> {

    public DistributionProtocol createDomainObject(DistributionProtocolForm uiRepOfDomain) {
    	InstanceFactory<DistributionProtocol> instFact = DomainInstanceFactory.getInstanceFactory(DistributionProtocol.class);
        DistributionProtocol dp = instFact.createObject();//new DistributionProtocol();
        overwriteDomainObject(dp, uiRepOfDomain);
        return dp;
    }

    @Override
    public void overwriteDomainObject(DistributionProtocol domainObject, DistributionProtocolForm uiRepOfDomain) {
        super.overwriteDomainObject(domainObject, uiRepOfDomain);

        try {
            final Map map = uiRepOfDomain.getValues();
            // map = fixMap(map);
            // logger.debug("MAP " + map);
            final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
            domainObject.setDistributionSpecimenRequirementCollection(new HashSet(parser.generateData(map)));
            // logger.debug("specimenRequirementCollection " +
            // domainObject.getDistributionSpecimenRequirementCollection());
        } catch (final Exception excp) {
            // DistributionProtocol.logger.error(excp.getMessage(), excp);
            excp.printStackTrace();
            // final ErrorKey errorKey =
            // ErrorKey.getErrorKey("assign.data.error");
            // throw new AssignDataException(errorKey, null,
            // "DisposalEventParameters.java :");
        }
    }
}
