
angular.module('os.administrative.models.site', ['os.common.models'])
  .factory('Site', function(osModel, $q) {
    var Site = osModel('sites');
    
    Site.list = function() {
      return Site.query();
    }
    
    Site.getCps = function (siteName) {
      var d = $q.defer();
      var cps = [{"id" : 1, "shortTitle": "AAAA"},{"id" : 2, "shortTitle": "AAA CP"}];
      d.resolve(cps);
      return d.promise;
    }
        
    return Site;
  });

