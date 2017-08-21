
angular.module('os.administrative.models.shipment', ['os.common.models'])
  .factory('Shipment', function(osModel, $http) {
    var Shipment = osModel('shipments');

    Shipment.prototype.getType = function() {
      return 'shipment';
    }

    Shipment.prototype.getDisplayName = function() {
      return this.name;
    }

    Shipment.prototype.$saveProps = function() {
      this.request = !!this.request ? {id: this.request.id} : undefined;
      angular.forEach(this.shipmentItems, function(shipmentItem) {
        shipmentItem.specimen = {
          id: shipmentItem.specimen.id,
          storageLocation: shipmentItem.specimen.storageLocation
        };
      });

      return this;
    }
    
    Shipment.prototype.generateReport = function() {
      return $http.get(Shipment.url() + this.$id() + "/report").then(
        function(resp) {
          return resp.data;
        }
      );
    }

    return Shipment;
  }
);
