
angular.module('os.administrative.models.dp', ['os.common.models'])
  .factory('DistributionProtocol', function(osModel, $http) {
    var DistributionProtocol = osModel('distribution-protocols');

    DistributionProtocol.prototype.getType = function() {
      return 'distribution_protocol';
    }

    DistributionProtocol.prototype.getDisplayName = function() {
      return this.title;
    }
    
    DistributionProtocol.prototype.close = function() {
      return updateActivityStatus(this, 'Closed');
    }
    
    DistributionProtocol.prototype.reopen = function() {
      return updateActivityStatus(this, 'Active');
    }
    
    function updateActivityStatus(dp, status) {
      return $http.put(DistributionProtocol.url() + '/' + dp.$id() + '/activity-status', {activityStatus: status}).then(
        function(result) {
          return new DistributionProtocol(result.data);
        }
      );
    }
    
    DistributionProtocol.getOrders = function(params) {
      return $http.get(DistributionProtocol.url() + 'orders', {params: params}).then(
        function(resp) {
          return resp.data;
        }
      );
    }
    
    DistributionProtocol.prototype.historyExportUrl = function() {
      return DistributionProtocol.url() + this.$id() + '/orders-report';
    }
    
    return DistributionProtocol;
  });

