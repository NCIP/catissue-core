
angular.module('os.biospecimen.specimensrequest.specimens', ['os.biospecimen.models'])
  .controller('SpmnReqSpecimensCtrl', function($scope, $state, spmnRequest, SpecimensHolder, Alerts) {
    /**
     * TODO: Merge specimen list functionality and below into one widget 
     */
    function init() {
      $scope.orderCreateOpts = {resource: 'Order', operations: ['Create']};
      $scope.shipmentCreateOpts = {resource: 'ShippingAndTracking', operations: ['Create']};
      resetSelection();
    }

    function resetSelection() {
      $scope.selection = {all: false, any: false, specimens: []};
      angular.forEach(spmnRequest.specimens, function(specimen) {
        specimen.selected = false;
      });
    }

    $scope.toggleAllSpecimenSelect = function() {
      $scope.selection.any = $scope.selection.all;
      if (!$scope.selection.all) {
        $scope.selection.specimens = [];
      } else {
        $scope.selection.specimens = [].concat(spmnRequest.specimens);
      }

      angular.forEach(spmnRequest.specimens, function(specimen) {
        specimen.selected = $scope.selection.all;
      });
    }

    $scope.toggleSpecimenSelect = function (specimen) {
      if (specimen.selected) {
        $scope.selection.specimens.push(specimen);
      } else {
        var idx = $scope.selection.specimens.indexOf(specimen);
        if (idx != -1) {
          $scope.selection.specimens.splice(idx, 1);
        }
      }

      $scope.selection.all = ($scope.selection.specimens.length == spmnRequest.specimens.length);
      $scope.selection.any = ($scope.selection.specimens.length > 0);
    };

    $scope.distributeSpecimens = function() {
      if (!$scope.selection.any) {
        Alerts.error("specimen_list.no_specimens_for_distribution");
        return;
      }

      SpecimensHolder.setSpecimens($scope.selection.specimens);
      $state.go('order-addedit', {orderId: ''});
    }

    $scope.shipSpecimens = function() {
      if (!$scope.selection.any) {
        Alerts.error("specimen_list.no_specimens_for_shipment");
        return;
      }

      SpecimensHolder.setSpecimens($scope.selection.specimens);
      $state.go('shipment-addedit', {orderId: ''});
    }

    init();
  });
