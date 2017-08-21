
angular.module('os.administrative.shipment.addedit', ['os.administrative.models', 'os.biospecimen.models'])
  .controller('ShipmentAddEditCtrl', function(
    $scope, $state, shipment, spmnRequest, cp, Shipment,
    Institute, Site, Specimen, SpecimensHolder, Alerts, Util, SpecimenUtil) {

    function init() {
      $scope.shipment = shipment;
      $scope.spmnOpts = {filters: {}, error: {}};

      shipment.request = spmnRequest;
      if (!!spmnRequest) {
        shipment.receivingInstitute = spmnRequest.requestorInstitute;
      }

      $scope.shipment.shipmentItems = shipment.shipmentItems || [];

      if (!!spmnRequest) {
        shipment.shipmentItems = getShipmentItemsFromReq(spmnRequest.items, shipment.shipmentItems);
      } else if (!shipment.id && angular.isArray(SpecimensHolder.getSpecimens())) {
        shipment.shipmentItems = getShipmentItems(SpecimensHolder.getSpecimens());
        SpecimensHolder.setSpecimens(null);
      }

      if (!shipment.id && !areSpmnsOfSameSite(shipment.shipmentItems)) {
        Alerts.error('shipments.multi_site_specimens');
        $scope.back();
        return;
      }

      if (!shipment.shippedDate) {
        shipment.shippedDate = new Date();
      }

      loadInstitutes();
      setUserAndSiteList(shipment);
    }

    function areSpmnsOfSameSite(shipmentItems) {
      if (!shipmentItems) {
        return true;
      }

      var site, sameSite = true;
      for (var i = 0; i < shipmentItems.length; ++i) {
        var spmn = shipmentItems[i].specimen;

        if (!spmn.storageSite || (!!spmnRequest && !spmn.selected)) {
          //
          // either the specimen doesn't have storage site or
          // it is coming from request but not selected for shipping
          //
          continue;
        }

        if (!!site && site != spmn.storageSite) {
          sameSite = false;
          break;
        }

        site = spmn.storageSite;
      }

      if (sameSite) {
        $scope.shipment.sendingSite = site;
      }

      return sameSite;
    }
    
    function loadInstitutes () {
      if (!!spmnRequest) {
        return;
      }

      Institute.query().then(
        function (institutes) {
          $scope.instituteNames = Institute.getNames(institutes);
        }
      );
    }

    function loadRecvSites(instituteName, searchTerm) {
      return Site.listForInstitute(instituteName, true, searchTerm).then(
        function(sites) {
          if (!spmnRequest) {
            return sites;
          }

          return cp.cpSites.map(
            function(cpSite) {
              return cpSite.siteName;
            }
          ).filter(
            function(site) {
              return sites.indexOf(site) != -1;
            }
          );
        }
      );
    }

    function loadSendingSites(searchTerm) {
      if (!spmnRequest) {
        return Site.list({name: searchTerm});
      } else {
        return cp.cpSites.map(
          function(cpSite) {
            return cpSite.siteName;
          }
        );
      }
    }

    function setUserFilterOpts(institute) {
      $scope.userFilterOpts = {institute: institute};
    }

    function setUserAndSiteList(shipment) {
      var instituteName = shipment.receivingInstitute;
      if (instituteName) {
        setUserFilterOpts(instituteName);
      }
    }

    function getShipmentItems(specimens) {
      return specimens.filter(
        function(specimen) {
          return (specimen.availableQty == undefined || specimen.availableQty > 0)
                 && specimen.activityStatus == 'Active';
        }
      ).map(
        function(specimen) {
          return {
            specimen: specimen
          };
        }
      );
    }

    function getShipmentItemsFromReq(reqItems, shipmentItems) {
      var shipmentItemsMap = {};
      angular.forEach(shipmentItems,
        function(shipmentItem) {
          shipmentItemsMap[shipmentItem.specimen.id] = shipmentItem;
        }
      );

      var items = [];
      angular.forEach(reqItems,
        function(reqItem) {
          var shipmentItem = shipmentItemsMap[reqItem.specimen.id];
          if (shipmentItem) {
            shipmentItem.specimen.selected = true;
          } else if (reqItem.status == 'PENDING') {
            shipmentItem = {
              specimen: reqItem.specimen,
            }

            reqItem.specimen.selected = reqItem.selected;
          }

          if (shipmentItem) {
            items.push(shipmentItem);
          }
        }
      );

      return items;
    }

    function saveOrUpdate(status) {
      var shipmentClone = angular.copy($scope.shipment);
      shipmentClone.status = status;

      if (!!spmnRequest) {
        var items = [];
        angular.forEach(shipmentClone.shipmentItems,
          function(item) {
            if (!item.specimen.selected) {
              return;
            }

            delete item.specimen.selected;
            items.push(item);
          }
        );

        shipmentClone.shipmentItems = items;
      }

      shipmentClone.$saveOrUpdate().then(
        function(savedShipment) {
          $state.go('shipment-detail.overview', {shipmentId: savedShipment.id});
        }
      );
    };

    $scope.loadSendingSites = loadSendingSites;

    $scope.loadRecvSites = loadRecvSites;

    $scope.onInstituteSelect = function(instituteName) {
      $scope.shipment.receivingSite = undefined;
      $scope.shipment.notifyUsers = [];

      setUserFilterOpts(instituteName);
    }

    $scope.onSiteSelect = function(siteName) {
      Site.getByName(siteName).then(
        function(site) {
          $scope.shipment.notifyUsers = site.coordinators;
        }
      );
    }

    $scope.initSpmnOpts = function(forward) {
      if (forward) {
        $scope.spmnOpts = {
          filters: {
            storageLocationSite: $scope.shipment.sendingSite
          },
          error: {
            code: 'specimens.specimen_not_found_at_send_site',
            params: {sendingSite: $scope.shipment.sendingSite}
          }
        }
      }

      return true;
    }

    $scope.addSpecimens = function(specimens) {
      if (!specimens) {
        return false;
      }

      Util.addIfAbsent($scope.shipment.shipmentItems, getShipmentItems(specimens), 'specimen.id');
      return true;
    }

    $scope.removeShipmentItem = function(shipmentItem) {
      var idx = shipment.shipmentItems.indexOf(shipmentItem);
      if (idx != -1) {
        shipment.shipmentItems.splice(idx, 1);
      }
    }

    $scope.ship = function() {
      saveOrUpdate('Shipped');
    }

    $scope.saveDraft = function() {
      saveOrUpdate('Pending');
    }

    $scope.passThrough = function() {
      return true;
    }

    $scope.toggleAllSpecimensSelect = function() {
      angular.forEach($scope.shipment.shipmentItems,
        function(item) {
          item.specimen.selected = $scope.input.allSelected;
        }
      );
    }

    $scope.toggleSpecimenSelect = function() {
      var allNotSelected = $scope.shipment.shipmentItems.some(
        function(item) {
          return !item.specimen.selected;
        }
      );

      $scope.input.allSelected = !allNotSelected;
    }

    init();
  });
