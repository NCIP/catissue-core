angular.module('os.administrative.shipment.detail', ['os.administrative.models'])
  .controller('ShipmentDetailCtrl', function($scope, shipment, Util) {

    function init() {
      $scope.shipment = shipment;
    }
    
    $scope.downloadReport = function() {
      Util.downloadReport(shipment, 'shipments');
    }
    
    init();
  });
