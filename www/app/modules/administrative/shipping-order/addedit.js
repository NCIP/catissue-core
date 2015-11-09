
angular.module('os.administrative.shippingorder.addedit', ['os.administrative.models', 'os.biospecimen.models'])
  .controller('ShippingOrderAddEditCtrl', function(
    $scope, $state, order, ShippingOrder, 
    Institute, Site, Specimen, SpecimensHolder, Alerts) {

    function init() {
      $scope.order = order;
      $scope.order.orderItems = order.orderItems || [];
      $scope.input = {labelText: '', allItemStatus: false};

      if (!order.id && angular.isArray(SpecimensHolder.getSpecimens())) {
        order.orderItems = getOrderItems(SpecimensHolder.getSpecimens());
        SpecimensHolder.setSpecimens(null);
      }
      
      if (!order.shippingDate) {
        order.shippingDate = new Date();
      }

      if ($scope.order.instituteName) {
        $scope.loadSites($scope.order.instituteName);
      }

      loadInstitutes();
    }
    
    function loadInstitutes () {
      Institute.query().then(
        function (institutes) {
          $scope.instituteNames = Institute.getNames(institutes);
        }
      );
    }

    function getOrderItems(specimens) {
      
      return specimens.filter(
        function(specimen) {
          return specimen.availableQty > 0;
        }).map(
        function(specimen) {
          return {
            specimen: specimen
          };
        });
    }

    function saveOrUpdate() {
      var order = angular.copy($scope.order);
      order.$saveOrUpdate().then(
        function(savedOrder) {
          $state.go('shipping-order-detail.overview', {orderId: savedOrder.id});
        }
      );
    };

    $scope.loadSites = function (instituteName) {
      Site.listForInstitute(instituteName, true).then(
        function(sites) {
          $scope.sites = sites;
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

      angular.forEach($scope.order.orderItems, function(item) {
        var idx = labels.indexOf(item.specimen.label);
        if (idx != -1) {
          labels.splice(idx, 1);
        }
      });

      if (labels.length == 0) {
        return;
      }

      Specimen.listByLabels(labels).then(
        function (specimens) {
          Array.prototype.push.apply($scope.order.orderItems, getOrderItems(specimens));
          $scope.input.labelText = '';
        }
      );
    }

    $scope.removeOrderItem = function(orderItem) {
      var idx = order.orderItems.indexOf(orderItem);
      if (idx != -1) {
        order.orderItems.splice(idx, 1);
      }
    }

    $scope.ship = function() {
      $scope.order.status = 'SHIPPED';
      saveOrUpdate();
    }

    $scope.saveDraft = function() {
      $scope.order.status = 'PENDING';
      saveOrUpdate();
    }

    init();
  });
