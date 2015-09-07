
angular.module('os.administrative.models.dp', ['os.common.models'])
  .factory('DistributionProtocol', function(osModel, $q, $http, $document) {
    var DistributionProtocol = osModel('distribution-protocols');

    DistributionProtocol.prototype.getType = function() {
      return 'distribution_protocol';
    }

    DistributionProtocol.prototype.getDisplayName = function() {
      return this.title;
    }
    
    DistributionProtocol.prototype.close = function () {
      return updateActivityStatus(this, 'Closed');
    }
    
    DistributionProtocol.prototype.reopen = function () {
      return updateActivityStatus(this, 'Active');
    }
    
    function updateActivityStatus (dp, status) {
      return $http.put(DistributionProtocol.url() + '/' + dp.$id() + '/activity-status', {activityStatus: status}).then(
        function (result) {
          return new DistributionProtocol(result.data);
        }
      );
    }
    
    function getHistory() {
      var deferred = $q.defer();
      
      deferred.resolve({
        "data": [{
          "name": "Distributed to Prof Tin",
          "executionDate": "1441174401000",
          "specimens": {
                         "type": "DNA",
                         "anatomicSite": "Lung",
                         "pathology": "Malignant"
                       },
          "specimenCnt": 20
        },
        {
          "name": "Distributed to Prof Tin",
          "executionDate": "1362002400000",
          "specimens": {
                         "type": "RNA",
                         "anatomicSite": "Lung",
                         "pathology": "Malignant"
                       },
          "specimenCnt": 508
        }]
      })
      
      return deferred.promise;
    }
    
    DistributionProtocol.prototype.getOrderHistory = function() {
      return getHistory().then(
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
