
angular.module('os.biospecimen.specimen.close', ['os.biospecimen.models'])
  .controller('SpecimenCloseCtrl', function($scope, $modalInstance, Specimen, props, Alerts) {
    function init() {
      $scope.specimen = props.specimen;
      $scope.closeSpec = {
        reason: ''
      };
    }

    $scope.close = function() {
      if (props.statusSpecs) {
        $scope.bulkClose();
      } else {
        $scope.specimen.close($scope.closeSpec.reason).then(
          function(result) {
            Alerts.success('specimens.specimen_closed');
            $modalInstance.close(result);
          }
        );
      }
    }

    $scope.bulkClose = function() {
      var statusSpecs = props.statusSpecs;
      var specimensToClose = props.specimensToClose;

      angular.forEach(statusSpecs, function(statusSpec) {
        statusSpec.reason =  $scope.closeSpec.reason;
      });

      Specimen.bulkStatusUpdate(statusSpecs).then(
        function(result) {
          angular.forEach(specimensToClose, function(specimen) {
            specimen.activityStatus = 'Closed';
            specimen.selected = false;
          });
          Alerts.success('specimens.specimens_closed');
          $modalInstance.close(result);
        }
      );
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }

    init();
  });
