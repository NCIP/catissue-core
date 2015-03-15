angular.module('os.administrative.dp.delete', ['os.administrative.models'])
  .controller('DpDeleteCtrl', function($scope, $modalInstance, $translate, distributionProtocol, dpDependencies, Alerts) {

    var init = function() {
      $scope.distributionProtocol = distributionProtocol;
      $scope.dpDependencies = $.isEmptyObject(dpDependencies) ? undefined : dpDependencies; 
    }

    var onDeleted = function(distributionProtocol) {
      if (!!distributionProtocol) {
        var title = distributionProtocol.title;
        $translate('dp.dp_deleted', {title: title}).then(function(msg) {
          Alerts.success(msg);
        })

        $modalInstance.close(distributionProtocol);
      }
    }

    $scope.delete = function () {
      $scope.distributionProtocol.$remove().then(onDeleted);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    init();
  })
