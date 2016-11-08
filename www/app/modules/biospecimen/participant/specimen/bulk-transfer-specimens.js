angular.module('os.biospecimen.specimen')
  .controller('BulkTransferSpecimensCtrl', function($scope, SpecimensHolder, Specimen, Alerts) {
    function init() {
      var specimens = SpecimensHolder.getSpecimens() || [];
      SpecimensHolder.setSpecimens(null);

      angular.forEach(specimens, function(spmn) {
        spmn.oldLocation = spmn.storageLocation;
        spmn.storageLocation = {};
      });

      $scope.specimens = specimens;
    }

    $scope.removeSpecimen = function(index) {
      $scope.specimens.splice(index, 1);
    }

    $scope.copyFirstLocationToAll = function() {
      var location = $scope.specimens[0].storageLocation;
      angular.forEach($scope.specimens, function(spmn) {
        angular.extend(spmn.storageLocation, location);
      });
    }

    $scope.transferSpecimens = function() {
      var specimens = $scope.specimens.map(
        function(spmn) {
          return {id: spmn.id, storageLocation: spmn.storageLocation};
        }
      );

      Specimen.bulkUpdate(specimens).then(
        function() {
          Alerts.success('specimens.bulk_transfer.success', {spmnsCount: specimens.length});
          $scope.back();
        }
      );
    }

    init();
  });
