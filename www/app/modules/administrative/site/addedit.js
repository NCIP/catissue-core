angular.module('os.administrative.site.addedit', ['os.administrative.models'])
  .controller('SiteAddEditCtrl', function($scope, $state, Site, PvManager) {

  var init = function() {
    $scope.site = new Site();
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
