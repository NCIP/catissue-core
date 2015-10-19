
angular.module('os.administrative.models.dpr', ['os.common.models'])
  .factory('DistributionProtocolRequirement', function(osModel, $q) {
    var DPRequirement = osModel('distribution-protocol-requirement');
    
    DPRequirement.prototype.getType = function() {
      return 'distribution_protocol_requirement';
    }
    
    return DPRequirement;
  });

