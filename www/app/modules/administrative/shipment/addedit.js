
angular.module('os.administrative.shipment.addedit', ['os.administrative.models', 'os.biospecimen.models'])
  .controller('ShipmentAddEditCtrl', function(
    $scope, $state, shipment, Shipment, 
    Institute, Site, Specimen, SpecimensHolder, Alerts) {

    function init() {
      $scope.shipment = shipment;
      $scope.shipment.shipmentItems = shipment.shipmentItems || [];
      $scope.input = {labelText: '', allItemStatus: false};

      if (!shipment.id && angular.isArray(SpecimensHolder.getSpecimens())) {
        shipment.shipmentItems = getShipmentItems(SpecimensHolder.getSpecimens());
        SpecimensHolder.setSpecimens(null);
      }
      
      if (!shipment.shippedDate) {
        shipment.shippedDate = new Date();
      }

      loadInstitutes();
      setUserAndSiteList(shipment);
    }
    
    function loadInstitutes () {
      Institute.query().then(
        function (institutes) {
          $scope.instituteNames = Institute.getNames(institutes);
        }
      );
    }

    function loadSites(instituteName) {
      Site.listForInstitute(instituteName, true).then(
        function(sites) {
          $scope.sites = sites;
        }
      );
    }

    function setUserFilterOpts(institute) {
      $scope.userFilterOpts = {institute: institute};
    }

    function setUserAndSiteList(shipment) {
      var instituteName = shipment.instituteName;
      if (instituteName) {
        setUserFilterOpts(instituteName);
        loadSites(instituteName);
      }
    }

    function getShipmentItems(specimens) {
      return specimens.filter(
        function(specimen) {
          return specimen.availableQty > 0 && specimen.activityStatus == 'Active';
        }).map(
        function(specimen) {
          return {
            specimen: specimen
          };
        });
    }

    function saveOrUpdate(status) {
      var shipment = angular.copy($scope.shipment);
      shipment.status = status;
      shipment.$saveOrUpdate().then(
        function(savedShipment) {
          $state.go('shipment-detail.overview', {shipmentId: savedShipment.id});
        }
      );
    };

    $scope.onInstituteSelect = function(instituteName) {
      $scope.shipment.siteName = undefined;
      $scope.shipment.notifyUsers = [];

      var instituteName = $scope.shipment.instituteName;
      loadSites(instituteName);
      setUserFilterOpts(instituteName);
    }

    $scope.onSiteSelect = function(siteName) {
      Site.getByName(siteName).then(
        function(site) {
          $scope.shipment.notifyUsers = site.coordinators;
        }
      );
    }

    $scope.addSpecimens = function() {
      var labels = 
        $scope.input.labelText.split(/,|\t|\n/)
          .map(function(label) { return label.trim(); })
          .filter(function(label) { return label.length != 0; });

      if (labels.length == 0) {
        return; 
      }

      angular.forEach($scope.shipment.shipmentItems, function(item) {
        var idx = labels.indexOf(item.specimen.label);
        if (idx != -1) {
          labels.splice(idx, 1);
        }
      });

      if (labels.length == 0) {
        return;
      }

      Specimen.listForShipment(labels, $scope.shipment.siteName).then(
        function (specimens) {
          angular.forEach(getShipmentItems(specimens), function(item) {
            $scope.shipment.shipmentItems.push(item);
          });
          $scope.input.labelText = '';
        }
      );
    }

    $scope.removeShipmentItem = function(shipmentItem) {
      var idx = shipment.shipmentItems.indexOf(shipmentItem);
      if (idx != -1) {
        shipment.shipmentItems.splice(idx, 1);
      }
    }

    $scope.ship = function() {
      saveOrUpdate('SHIPPED');
    }

    $scope.saveDraft = function() {
      saveOrUpdate('PENDING');
    }

    init();
  });
