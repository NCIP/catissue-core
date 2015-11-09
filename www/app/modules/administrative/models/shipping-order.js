
angular.module('os.administrative.models.shippingorder', ['os.common.models'])
  .factory('ShippingOrder', function(osModel, $http) {
    var ShippingOrder = osModel('shipping-orders');

    ShippingOrder.prototype.getType = function() {
      return 'shipping_order';
    }

    ShippingOrder.prototype.getDisplayName = function() {
      return this.name;
    }

    ShippingOrder.prototype.$saveProps = function() {
      angular.forEach(this.orderItems, function(orderItem) {
        orderItem.specimen = {id: orderItem.specimen.id};
      });

      return this;
    }

    ShippingOrder.prototype.$receiveShipment = function() {
      angular.forEach(this.orderItems, function(orderItem) {
        orderItem.specimen = {
          id: orderItem.specimen.id, 
          storageLocation: orderItem.specimen.storageLocation
        };
      });
      
      return $http.put(ShippingOrder.url() + this.id + "/receive-shipment", this).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    return ShippingOrder;
  }
);
