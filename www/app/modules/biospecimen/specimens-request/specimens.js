
angular.module('os.biospecimen.specimensrequest.specimens', ['os.biospecimen.models'])
  .controller('SpmnReqSpecimensCtrl', function($scope, $state, spmnRequest, SpecimensHolder, SpecimenRequestHolder, Alerts) {
    /**
     * TODO: Merge specimen list functionality and below into one widget 
     */
    function init() {
      $scope.orderCreateOpts = {resource: 'Order', operations: ['Create']};
      $scope.shipmentCreateOpts = {resource: 'ShippingAndTracking', operations: ['Create']};
      resetSelection();
    }

    function resetSelection() {
      $scope.selection = {
        all: false,
        any: false,
        items: [],
        closed: spmnRequest.activityStatus == 'Closed'
      };

      angular.forEach(spmnRequest.items,
        function(item) {
          item.selected = false;
        }
      );
    }

    $scope.toggleAllSpecimenSelect = function() {
      $scope.selection.any = $scope.selection.all;
      if (!$scope.selection.all) {
        $scope.selection.items = [];
      } else {
        $scope.selection.items = [].concat(spmnRequest.items);
      }

      angular.forEach(spmnRequest.items,
        function(item) {
          item.selected = $scope.selection.all;
        }
      );
    }

    $scope.toggleSpecimenSelect = function(item) {
      if (item.selected) {
        $scope.selection.items.push(item);
      } else {
        var idx = $scope.selection.items.indexOf(item);
        if (idx != -1) {
          $scope.selection.items.splice(idx, 1);
        }
      }

      $scope.selection.all = ($scope.selection.items.length == spmnRequest.items.length);
      $scope.selection.any = ($scope.selection.items.length > 0);
    };

    $scope.distributeSpecimens = function() {
      SpecimenRequestHolder.set(spmnRequest);
      $state.go('order-addedit', {orderId: '', requestId: spmnRequest.id});
    }

    $scope.shipSpecimens = function() {
      SpecimenRequestHolder.set(spmnRequest);
      $state.go('shipment-addedit', {shipmentId: '', requestId: spmnRequest.id});
    }

    init();
  });
