
angular.module('os.biospecimen.specimen.close', ['os.biospecimen.models'])
  .controller('SpecimenCloseCtrl', function($scope, $modalInstance, Specimen, specimens, Alerts) {
    function init() {
      $scope.specimen = specimens[0];
      $scope.closeSpec = {
        reason: ''
      };
    }

    function bulkClose() {
      var statusSpecs = [];
      angular.forEach(specimens, function(specimen) {
        var statusSpec = {status: 'Closed', reason: $scope.closeSpec.reason, id: specimen.id};
        statusSpecs.push(statusSpec);
      });

      Specimen.bulkStatusUpdate(statusSpecs).then(
        function(result) {
          angular.forEach(specimens, function(specimen) {
            specimen.activityStatus = 'Closed';
            specimen.selected = false;
          });
          Alerts.success('specimens.specimens_closed');
          $modalInstance.close(result);
        }
      );
    }

    $scope.close = function() {
      if (specimens.length > 1) {
        bulkClose();
      } else {
        $scope.specimen.close($scope.closeSpec.reason).then(
          function(result) {
            Alerts.success('specimens.specimen_closed');
            $modalInstance.close(result);
          }
        );
      }
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }

    init();
  });
