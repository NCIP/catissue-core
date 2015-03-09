angular.module('os.administrative.site.list', ['os.administrative.models'])
  .controller('SiteListCtrl', function($scope, $state, Site) {

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

    $scope.filter = function(site) {
      Site.list(site).then(
        function(siteList) {
          $scope.siteList = siteList;
        }
      );
    }

    loadSites();
  });
