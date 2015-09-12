
angular.module('os.administrative.models.orderrequirement', ['os.common.models'])
  .factory('OrderRequirement', function(osModel, $q){
    var OrderRequirement = osModel('distribution-order-requirements');
    
    OrderRequirement.prototype.getType = function() {
      return 'distribution_order_requirements';
    }
    
    return OrderRequirement;
  });
