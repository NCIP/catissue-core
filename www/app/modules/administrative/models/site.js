angular.module('os.administrative.models.site', ['os.common.models'])
  .factory('Site', function(osModel) {
    var Site = new osModel('sites');

    Site.list = function(site) {
      return Site.query(site);
    }

    return Site;
  });
