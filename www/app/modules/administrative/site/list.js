angular.module('os.administrative.site.list', ['os.administrative.models'])
  .controller('SiteListCtrl', function($scope, $state, Site, Util) {

    function init() {
      $scope.siteFilterOpts = {};
      loadSites();
      Util.filter($scope, 'siteFilterOpts', filter);
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

    function filter(filterOpts) {
      loadSites(filterOpts);
    }

    init();
  });
