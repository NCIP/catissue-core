angular.module('os.administrative.models.site', ['os.common.models'])
  .factory('Site', function(osModel) {
    var Site = new osModel('sites');

    Site.prototype.getType = function() {
      return 'site';
    }

    Site.prototype.getDisplayName = function() {
      return this.name;
    }

    return Site;
  });
