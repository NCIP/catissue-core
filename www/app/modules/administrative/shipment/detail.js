angular.module('os.administrative.shipment.detail', ['os.administrative.models'])
  .controller('ShipmentDetailCtrl', function($scope, shipment) {

    function init() {
      $scope.shipment = shipment;
    }

    init();
  });
