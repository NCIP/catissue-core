
angular.module('os.administrative.models.dp', ['os.common.models'])
  .factory('DistributionProtocol', function(osModel) {
    var DistributionProtocol = osModel('distribution-protocols');

    return DistributionProtocol;
  });
