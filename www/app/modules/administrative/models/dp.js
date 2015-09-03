
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
    
    DistributionProtocol.prototype.getInstitutes = function() {
      if (!this.distributingSites) {
        this.distributingSites = [];
      }
      
      return this.distributingSites.map(function(instSite) { return instSite.instituteName});
    }
     
    DistributionProtocol.prototype.newDistSite = function () {
      if (!this.distributingSites) {
        this.distributingSites = [];
      }
      
      this.distributingSites.push({instituteName: '', sites: []});
    }
    
    DistributionProtocol.prototype.removeDistSite = function (index) {
      this.distributingSites.splice(index, 1);
      if (this.distributingSites.length == 0) {
        this.newDistSite();
      }
    }
    
    DistributionProtocol.prototype.$saveProps = function () {
      var sites = [];
      angular.forEach(this.distributingSites, function (distSite) {
        angular.forEach(distSite.sites, function (siteName) {
          sites.push({name: siteName});
        });
      });
      
      this.distributingSites = sites;
      return this;
    };

    return DistributionProtocol;
  });
