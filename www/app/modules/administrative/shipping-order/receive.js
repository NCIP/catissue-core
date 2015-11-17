angular.module('os.administrative.shippingorder.receive', ['os.administrative.models'])
  .controller('ShippingOrderReceiveCtrl', function($scope, $state, order, Specimen, PvManager) {

    function loadPvs() {
      $scope.qualityStatuses = PvManager.getPvs('quality-status');
    }

    function init() {
      $scope.order = order;
      angular.forEach(order.orderItems, function(item) {
        item.specimen = new Specimen(item.specimen);
      });

      loadPvs();
    }

    $scope.receiveShipment = function() {
      var order = angular.copy($scope.order);
      order.$receiveShipment().then(
        function(resp) {
          $state.go('shipping-order-detail.overview', {orderId: resp.id});
        }
      );
    }
    
    $scope.applyFirstLocationToAll = function() {
      var containerName = undefined;
      
      if ($scope.order.orderItems[0].specimen.storageLocation.name != null) {
        containerName = $scope.order.orderItems[0].specimen.storageLocation.name;
      }
      
      angular.forEach($scope.order.orderItems, function(item) {
        item.specimen.storageLocation.name = containerName;
      });
    }

    init();
  });
  
