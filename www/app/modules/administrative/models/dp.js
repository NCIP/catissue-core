
angular.module('os.administrative.models.dp', ['os.common.models'])
  .factory('DistributionProtocol', function(osModel, $http, $document) {
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
    
    DistributionProtocol.prototype.getOrderHistory = function() {
      return $http.get(DistributionProtocol.url() + this.$id() + '/history').then(
        function(resp) {
          return resp.data;
        }
      );
    }
    
    DistributionProtocol.prototype.exportHistory = function() {
      filename = 'dp-history.csv';
      var link = angular.element('<a/>').attr(
        {
          href: DistributionProtocol.url() + this.$id() + '/export',
          target: '_blank'
        }
      );

      angular.element($document[0].body).append(link);

      if (typeof link[0].click == "function") {
        link[0].click();
      } else { // Safari fix
        var dispatch = document.createEvent("HTMLEvents");
        dispatch.initEvent("click", true, true);
        link[0].dispatchEvent(dispatch);
      }

      link.remove();
    }
    
    return DistributionProtocol;
  });
