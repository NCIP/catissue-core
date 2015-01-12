
angular.module('os.administrative.models.site', ['os.common.models'])
  .factory('Site', function(osModel, $q) {
    var Site = osModel('sites');
    //TODO: will fix this with back-end change
    var cps = {
      "In Transit": [{id: -1, shortTitle: 'All'}, {"id" : 1, "shortTitle": "Cp1"}, {"id" : 1, "shortTitle": "Cp2"}],
      "Site container": [{id: -1, shortTitle: 'All'}, {"id" : 1, "shortTitle": "Cp3"}, {"id" : 1, "shortTitle": "Cp4"}],
      "Univ of Leicester": [{id: -1, shortTitle: 'All'}, {"id" : 1, "shortTitle": "Cp1"}, {"id" : 1, "shortTitle": "Cp3"}]
    };
    
    Site.list = function() {
      return Site.query();
    }
    
    Site.getCps = function (siteName) {
      var d = $q.defer();
      d.resolve(cps[siteName]);
      return d.promise;
    }
        
    return Site;
  });

