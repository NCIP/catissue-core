
angular.module('os.administrative.shipment.list', ['os.administrative.models'])
  .controller('ShipmentListCtrl', function(
    $scope, $state, Shipment, Institute, Util, Site) {
  
    function init() {
      $scope.filterOpts = {};

      loadInstitutes();
      loadShipments($scope.filterOpts);
      loadSites();
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
    
    function loadSites() {
      Site.list().then(
        function(sites) {
          $scope.siteNames = sites;
        }
      );
    }

    $scope.showShipmentOverview = function(shipment) {
      $state.go('shipment-detail.overview', {shipmentId: shipment.id});
    };
    
    $scope.clearFilters = function() {
      $scope.filterOpts = {};
    }

    init();
  });
