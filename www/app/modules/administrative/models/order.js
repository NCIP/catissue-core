
angular.module('os.administrative.models.order', ['os.common.models'])
  .factory('DistributionOrder', function(osModel, $http, Specimen) {
    var DistributionOrder = osModel('distribution-orders');

    DistributionOrder.prototype.getType = function() {
      return 'distribution_order';
    }

    DistributionOrder.prototype.getDisplayName = function() {
      return this.name;
    }

    DistributionOrder.prototype.$saveProps = function() {
      this.requester = {id: this.requester.id};
      this.distributionProtocol = {id: this.distributionProtocol.id};
      this.request = !!this.request ? {id: this.request.id} : undefined;

      angular.forEach(this.orderItems,
        function(orderItem) {
          orderItem.specimen = {id: orderItem.specimen.id};
        }
      );

      return this;
    }

    DistributionOrder.prototype.generateReport = function() {
      return $http.get(DistributionOrder.url() + this.$id() + '/report').then(
        function(resp) {
          return resp.data;
        }
      );
    }

    DistributionOrder.getItemStatusPvs = function() {
      return [
        'DISTRIBUTED',
        'DISTRIBUTED_AND_CLOSED'
      ];
    }

    DistributionOrder.getDistributionDetails = function(labels) {
      return $http.get(DistributionOrder.url() + 'specimens', {params: {label: labels}}).then(
        function(resp) {
          return resp.data.map(
            function(item) {
              item.specimen = new Specimen(item.specimen);
              return item;
            }
          );
        }
      );
    }

    DistributionOrder.returnSpecimens = function(returnedSpmns) {
      return $http.post(DistributionOrder.url() + 'return-specimens', returnedSpmns).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    return DistributionOrder;
  });
