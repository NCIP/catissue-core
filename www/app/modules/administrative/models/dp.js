
angular.module('os.administrative.models.dp', ['os.common.models'])
  .factory('DistributionProtocol', function(osModel, $http) {
    var DistributionProtocol = osModel('distribution-protocols');

    DistributionProtocol.prototype.getType = function() {
      return 'distribution_protocol';
    }

    DistributionProtocol.prototype.getDisplayName = function() {
      return this.title;
    }
    
    DistributionProtocol.prototype.close = function () {
      this.activityStatus = 'Closed';
      return updateActivityStatus(this);
    }
    
    DistributionProtocol.prototype.reopen = function () {
      this.activityStatus = 'Active';
      return updateActivityStatus(this);
    }
    
    function updateActivityStatus (dp) {
      return $http.put(DistributionProtocol.url() + '/' + dp.$id() + '/activity-status', dp).then(
        function (result) {
          angular.extend(dp, result.data);
          return new DistributionProtocol(result.data);
        }
      );
    }

    return DistributionProtocol;
  });
