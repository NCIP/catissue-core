angular.module('os.administrative.institute.delete', ['os.administrative.models'])
  .controller('InstituteDeleteCtrl', function($scope, $modalInstance, $translate, institute, instituteDependencies, Alerts) {

    var init = function() {
      $scope.institute = institute;
      $scope.instituteDependencies = $.isEmptyObject(instituteDependencies) ? undefined : instituteDependencies;
    }

    var onDeleted = function(institute) {
      if (!!institute) {
        $translate('institute.institute_deleted', {name: institute.name}).then(function(msg) {
          Alerts.success(msg);
        })
        $modalInstance.close(institute);
      }
    }

    $scope.delete = function () {
      $scope.institute.$remove().then(onDeleted)
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    init();
  })

