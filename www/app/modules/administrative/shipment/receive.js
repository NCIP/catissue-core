angular.module('os.administrative.shipment.receive', ['os.administrative.models'])
  .controller('ShipmentReceiveCtrl', function($scope, $state, shipment, Specimen, PvManager) {

    function loadPvs() {
      $scope.qualityStatuses = PvManager.getPvs('quality-status');
    }

    function init() {
      $scope.shipment = shipment;
      angular.forEach(shipment.shipmentItems, function(item) {
        item.specimen = new Specimen(item.specimen);
      });

      if (!$scope.shipment.receivedDate) {
        $scope.shipment.receivedDate = new Date();
      }
      
      loadPvs();
    }

    $scope.passThrough = function() {
      return true;
    }

    $scope.receive = function() {
      var shipment = angular.copy($scope.shipment);
      shipment.status = "Received";
      shipment.$saveOrUpdate().then(
        function(resp) {
          $state.go('shipment-detail.overview', {shipmentId: resp.id});
        }
      );
    }
    
    $scope.applyFirstLocationToAll = function() {
      var location = $scope.shipment.shipmentItems[0].specimen.storageLocation;
      if (!location.name) {
        return;
      }

      angular.forEach($scope.shipment.shipmentItems, function(item, idx) {
        if (idx == 0) {
          return;
        }

        item.specimen.storageLocation = {name: location.name, mode: location.mode};
      });
    }

    init();
  });
