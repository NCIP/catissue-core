angular.module('os.administrative.site.addedit', ['os.administrative.models'])
  .controller('SiteAddEditCtrl', function($scope, $state, site, Site, PvManager) {

  var init = function() {
    $scope.site = site;
    $scope.siteTypes = PvManager.getPvs('site-type');
  }

  $scope.save = function() {
    var site = angular.copy($scope.site);
    site.$saveOrUpdate().then(
      function(savedSite) {
        $state.go('site-detail.overview', {siteId: savedSite.id});
      }
    );
  }

  init();
 });
