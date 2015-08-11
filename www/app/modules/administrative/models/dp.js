
angular.module('os.administrative.models.dp', ['os.common.models'])
  .factory('DistributionProtocol', function(osModel) {
    var DistributionProtocol = osModel('distribution-protocols');

    DistributionProtocol.prototype.getType = function() {
      return 'distribution_protocol';
    }

    DistributionProtocol.prototype.getDisplayName = function() {
      return this.title;
    }
    
    DistributionProtocol.prototype.newDistSite = function () {
      return {instituteName: '', sites:[]};
    }
    
    DistributionProtocol.prototype.addDistSite = function (distSite) {
      if (!this.distributingSites) {
        this.distributingSites = [];
      }
      
      this.distributingSites.push(distSite);
    }
    
    DistributionProtocol.prototype.removeDistSite = function (distSite) {
      var index = this.distributingSites ? this.distributingSites.indexOf(distSite) : -1;
      if (index != -1) {
        this.distributingSites.splice(index, 1);
      }
      
      return index;
    }
    
    DistributionProtocol.prototype.getDistSite = function () {
      var distSites = [];
      angular.forEach(this.distributingSites, function (site) {
        if (site.instituteName && site.sites) {
          distSites.push(site);
        }
      });
      
      return distSites;
    }
    
    DistributionProtocol.prototype.getDistInstNames = function () {
      var distInstNames = [];
      angular.forEach(this.distributingSites, function (site) {
        if (site.instituteName) {
          distInstNames.push(site.instituteName);
        }
      });
      
      return distInstNames;
    }
    
    DistributionProtocol.prototype.$saveProps = function () {
      var distSites = this.getDistSite();
      this.distributingSites = distSites.length == 0 ? [] : distSites;
      return this;
    }

    return DistributionProtocol;
  });
