angular.module('os.administrative.site.list', ['os.administrative.models'])
  .controller('SiteListCtrl', function($scope, $state, Site) {

    function init() {
      $scope.siteFilterOpts = {};
      loadSites();
    }

    var loadSites = function() {
      Site.list().then(
        function(siteList) {
          $scope.siteList = siteList;
        }
      );
    };

    $scope.showSiteOverview = function(site) {
      $state.go('site-detail.overview', {siteId: site.id});
    };

    $scope.filter = function(siteFilterOpts) {
      Site.list(siteFilterOpts).then(
        function(siteList) {
          $scope.siteList = siteList;
        }
      );
    }

    init();
  });
