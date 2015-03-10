angular.module('os.administrative.models.site', ['os.common.models'])
  .factory('Site', function(osModel) {
    var Site = new osModel('sites');

    Site.list = function(siteFilterOpts) {
      return Site.query(siteFilterOpts);
    }

    return Site;
  });
