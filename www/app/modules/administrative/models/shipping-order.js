
angular.module('os.administrative.models.shippingorder', ['os.common.models'])
  .factory('ShippingOrder', function(osModel, $http) {
    var ShippingOrder = osModel('shipping-orders');

    ShippingOrder.prototype.getType = function() {
      return 'shipping_order';
    }

    ShippingOrder.prototype.getDisplayName = function() {
      return this.name;
    }

    return ShippingOrder;
  }
);
