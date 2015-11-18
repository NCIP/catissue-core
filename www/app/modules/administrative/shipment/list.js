
angular.module('os.administrative.shipment.list', ['os.administrative.models'])
  .controller('ShipmentListCtrl', function(
    $scope, Shipment, Institute, Util) {
  
    function init() {
      $scope.filterOpts = {};

      loadInstitutes();
      loadShipments($scope.filterOpts);
      Util.filter($scope, 'filterOpts', loadShipments);
    }

    function loadShipments(filterOpts) {
      Shipment.query(filterOpts).then(
        function(result) {
          $scope.shipments = result; 
        }
      );
    }
 
    function loadInstitutes() {
      Institute.query().then(
        function(institutes) {
          $scope.instituteNames = Institute.getNames(institutes);
        }
      );
    }

    $scope.clearFilters = function() {
      $scope.filterOpts = {};
    }

    init();
  });
