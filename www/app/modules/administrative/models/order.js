
angular.module('os.administrative.models.order', ['os.common.models'])
  .factory('DistributionOrder', function(osModel) {
    var DistributionOrder = osModel('distribution-orders');

    DistributionOrder.prototype.getType = function() {
      return 'distribution_order';
    }

    DistributionOrder.prototype.getDisplayName = function() {
      return this.name;
    }

    DistributionOrder.getItemStatusPvs = function() {
      return [
        'DISTRIBUTED',
        'DISTRIBUTED_AND_CLOSED'
      ];
    }

    DistributionOrder.prototype.$saveProps = function() {
      this.requester = {id: this.requester.id};
      this.distributionProtocol = {id: this.distributionProtocol.id};
      angular.forEach(this.orderItems, function(orderItem) {
        orderItem.specimen = {id: orderItem.specimen.id};
      });

      return this;
    }

    return DistributionOrder;
  });
