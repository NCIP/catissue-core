
angular.module('os.administrative.models.dp', ['os.common.models'])
  .factory('DistributionProtocol', function(osModel) {
    var DistributionProtocol = osModel('distribution-protocols');

    DistributionProtocol.list = function(dpFilterOpts) {
      return DistributionProtocol.query(dpFilterOpts);
    }
    
    return DistributionProtocol;
  });
