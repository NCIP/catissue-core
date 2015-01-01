angular.module('os.administrative.site.detail', ['os.administrative.models'])
  .controller('SiteDetailCtrl', function($scope, $q, site, PvManager) {

    function init() {
      $scope.site = site;
      $scope.siteTypes = PvManager.getPvs('site-type');
    }

    $scope.editSite = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    init();
  });