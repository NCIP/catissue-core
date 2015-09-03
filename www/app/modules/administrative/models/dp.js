
angular.module('os.administrative.models.dp', ['os.common.models'])
  .factory('DistributionProtocol', function(osModel, $q) {
    var DistributionProtocol = osModel('distribution-protocols');

    DistributionProtocol.prototype.getType = function() {
      return 'distribution_protocol';
    }

    DistributionProtocol.prototype.getDisplayName = function() {
      return this.title;
    }
    
    function getHistory() {
      var deferred = $q.defer();
      
      deferred.resolve({
        "data": [{
          "name": "Distributed to Prof Tin",
          "executionDate": "1441174401000",
          "specimens": {
                         "specimenType": "DNA",
                         "tissueSite": "Lung",
                         "pathologyStatus": "Malignant"
                       },
          "specimenCnt": 20
        },
        {
          "name": "Distributed to Prof Tin",
          "executionDate": "1362002400000",
          "specimens": {
                         "specimenType": "RNA",
                         "tissueSite": "Lung",
                         "pathologyStatus": "Malignant"
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

    return DistributionProtocol;
  });
