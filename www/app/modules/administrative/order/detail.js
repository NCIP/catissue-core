angular.module('os.administrative.order.detail', ['os.administrative.models'])
  .controller('OrderDetailCtrl', function($scope, $state, order, Util, SpecimensHolder, Alerts) {
  
    function init() {
      $scope.order = order;
      $scope.rptTmplConfigured = false;
      if (!!order.distributionProtocol.report && !!order.distributionProtocol.report.id) {
        $scope.rptTmplConfigured = true;
      }

      $scope.selection = {all: false, items: []};
    }

    $scope.downloadReport = function() {
      Util.downloadReport(order, 'orders');
    };

    $scope.toggleAllItemSelect = function() {
      if ($scope.selection.all) {
        $scope.selection.items = [].concat($scope.order.orderItems);
      } else {
        $scope.selection.items = [];
      }

      angular.forEach($scope.order.orderItems, function(item) {
        item.selected = $scope.selection.all;
      });
    }

    $scope.toggleItemSelect = function(item) {
      if (item.selected) {
        $scope.selection.items.push(item);
      } else {
        var idx = $scope.selection.items.indexOf(item);
        if (idx != -1) {
          $scope.selection.items.splice(idx, 1);
        }
      }

      $scope.selection.all = ($scope.selection.items.length == $scope.order.orderItems.length);
    }

    $scope.returnSpecimens = function() {
      if ($scope.selection.items.length == 0) {
        Alerts.error('orders.no_specimen_to_return');
        return;
      }

      var error = false;
      angular.forEach($scope.selection.items, function(item) {
        if (item.status == "RETURNED") {
          Alerts.error('orders.specimen_already_returned', {spec_label: item.specimen.label});
          error = true;
        }
      });

      if (error) {
        return;
      }

      SpecimensHolder.setSpecimens($scope.selection.items);
      $state.go('return-specimens', {orderId: order.id});
    };

    init();
  });
