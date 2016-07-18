
angular.module('os.administrative.shipment.list', ['os.administrative.models'])
  .controller('ShipmentListCtrl', function(
    $scope, $state, Shipment, Institute, Site, Util, ListPagerOpts) {

    var pagerOpts;

    function init() {
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getShipmentsCount, recordsPerPage: 50});
      $scope.filterOpts = {maxResults: pagerOpts.recordsPerPage + 1};

      loadInstitutes();
      loadShipments($scope.filterOpts);
      Util.filter($scope, 'filterOpts', loadShipments);
    }

    function loadShipments(filterOpts) {
      Shipment.query(filterOpts).then(
        function(result) {
          $scope.shipments = result;
          pagerOpts.refreshOpts(result);
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

    function getShipmentsCount() {
      return Shipment.getCount($scope.filterOpts);
    }

    $scope.showShipmentOverview = function(shipment) {
      $state.go('shipment-detail.overview', {shipmentId: shipment.id});
    };
    
    $scope.onInstituteSelect = function(institute) {
      $scope.filterOpts.recvSite = undefined;
      loadSites(institute);
    }

    $scope.clearFilters = function() {
      $scope.filterOpts = {maxResults: pagerOpts.recordsPerPage + 1};
    }

    init();
  });
