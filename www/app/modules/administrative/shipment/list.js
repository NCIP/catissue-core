
angular.module('os.administrative.shipment.list', ['os.administrative.models'])
  .controller('ShipmentListCtrl', function(
    $scope, $state, Shipment, Institute, Site, Util) {
  
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
    
    function loadSites(institute) {
      Site.listForInstitute(institute, true).then(
        function(sites) {
          $scope.siteNames = sites;
        }
      );
    }

    $scope.showShipmentOverview = function(shipment) {
      $state.go('shipment-detail.overview', {shipmentId: shipment.id});
    };
    
    $scope.onInstituteSelect = function(institute) {
      $scope.filterOpts.site = undefined;
      loadSites(institute);
    }

    $scope.clearFilters = function() {
      $scope.filterOpts = {};
    }

    init();
  });
