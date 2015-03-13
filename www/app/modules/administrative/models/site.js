angular.module('os.administrative.models.site', ['os.common.models'])
  .factory('Site', function(osModel) {
    var Site = new osModel('sites');

    return Site;
  });
