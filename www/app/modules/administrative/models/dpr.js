
angular.module('os.administrative.models.dpr', ['os.common.models'])
  .factory('DistributionProtocolRequirement', function(osModel) {
    var DPRequirement = osModel('distribution-protocol-requirements');
    
    DPRequirement.prototype.getType = function() {
      return 'distribution_protocol_requirement';
    }
    
    return DPRequirement;
  });

