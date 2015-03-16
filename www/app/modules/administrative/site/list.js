angular.module('os.administrative.site.list', ['os.administrative.models'])
  .controller('SiteListCtrl', function($scope, $state, Site) {

    function init() {
      $scope.siteFilterOpts = {};
      loadSites();
    }

    var loadSites = function(filterOpts) {
      Site.query(filterOpts).then(
        function(siteList) {
          $scope.siteList = siteList;
        }
      );
    };

    $scope.showSiteOverview = function(site) {
      $state.go('site-detail.overview', {siteId: site.id});
    };

    $scope.filter = function() {
      loadSites($scope.siteFilterOpts);
    }

    init();
  });
