
angular.module('os.administrative.models.dpr', ['os.common.models'])
  .factory('DistributionProtocolRequirement', function(osModel, $q){
    var OrderRequirement = osModel('distribution-protocol-requirement');
    
    OrderRequirement.prototype.getType = function() {
      return 'distribution_protocol_requirement';
    }
    
    return OrderRequirement;
  });
