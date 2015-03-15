angular.module('os.administrative.site.delete', ['os.administrative.models'])
  .controller('SiteDeleteCtrl', function($scope, $modalInstance, $translate, site, siteDependencies, Alerts) {

    var init = function() {
      $scope.site = site;
      $scope.siteDependencies = $.isEmptyObject(siteDependencies) ? undefined : siteDependencies;
    }

    var onDeleted = function(site) {
      if (!!site) {
        $translate('site.site_deleted', {name: site.name}).then(function(msg) {
          Alerts.success(msg);
        })
        $modalInstance.close(site);
      }
    }

    $scope.delete = function () {
      $scope.site.$remove().then(onDeleted);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    init();
  })

