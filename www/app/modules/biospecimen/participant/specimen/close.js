
angular.module('os.biospecimen.specimen.close', ['os.biospecimen.models'])
  .controller('SpecimenCloseCtrl', function($scope, $modalInstance, specimen) {
    function init() {
      $scope.specimen = specimen;
      $scope.closeSpec = {
        reason: ''
      };
    }

    $scope.close = function() {
      specimen.close($scope.closeSpec.reason).then(
        function(result) {
          $modalInstance.close(result);
        }
      );
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }

    init();
  });
