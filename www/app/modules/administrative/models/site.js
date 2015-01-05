
angular.module('os.administrative.models.site', ['os.common.models'])
  .factory('Site', function(osModel) {
    var Site = osModel('sites');
    
    return Site;
  });

