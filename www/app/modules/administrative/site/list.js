angular.module('os.administrative.site.list', ['os.administrative.models'])
  .controller('SiteListCtrl', function($scope, $state, Site, Util, ListPagerOpts) {

    var pagerOpts;

    function init() {
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getSitesCount});
      $scope.siteFilterOpts = {includeStats: true, maxResults: pagerOpts.recordsPerPage + 1};
      loadSites($scope.siteFilterOpts);
      Util.filter($scope, 'siteFilterOpts', loadSites);
    }

    function loadSites(filterOpts) {
      Site.query(filterOpts).then(
        function(siteList) {
          $scope.siteList = siteList;
          pagerOpts.refreshOpts(siteList);
        }
      );
    };

    function getSitesCount() {
      return Site.getCount($scope.siteFilterOpts);
    }

    $scope.showSiteOverview = function(site) {
      $state.go('site-detail.overview', {siteId: site.id});
    };

    init();
  });
