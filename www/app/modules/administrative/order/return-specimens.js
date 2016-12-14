angular.module('os.administrative.order.returnspecimens', [])
  .controller('OrderReturnSpecimensCtrl', function(
    $rootScope, $scope, $state, barcodingEnabled, DistributionOrder, Util, Alerts, SpecimenUtil) {

    function init() {
      $scope.containerListCache = {};

      $scope.input = {
        labels: '',
        returnItems: {},
        orderedLabels: [],
        barcodingEnabled: barcodingEnabled,
        useBarcode: false
      }
    }

    function uiReturnItem(distItem) {
      distItem.specimen.storageLocation = distItem.specimen.storageLocation || {};

      return {
        specimen: distItem.specimen,
        orders: [{name: distItem.orderName, quantity: distItem.quantity}],
        orderName: distItem.orderName,
        quantity: distItem.quantity,
        distQty: distItem.quantity,
        user: $rootScope.currentUser,
        time: new Date(),
        incrFreezeThaw: 1,
        comments: ''
      }
    }

    function resolveDistItems(labels, distItems) {
      return SpecimenUtil.resolveSpecimens(labels, getUqSpecimens(distItems)).then(
        function(specimens) {
          if (!specimens) {
            return;
          }

          var spmnsMap = specimens.reduce(function(map, spmn) {
            map[spmn.id] = spmn;
            return map;
          }, {});

          return distItems.filter(function(item) {
            return !!spmnsMap[item.specimen.id];
          });
        }
      );
    }

    function getUqSpecimens(distItems) {
      var spmnsMap = {}, specimens = [];
      angular.forEach(distItems,
        function(item) {
          if (!spmnsMap[item.specimen.id]) {
            specimens.push(item.specimen);
            spmnsMap[item.specimen.id] = item.specimen;
          }
        }
      );

      return specimens;
    }

    function addReturnItems(distItems) {
      var returnItems = $scope.input.returnItems;
      angular.forEach(distItems,
        function(distItem) {
          var key = distItem.specimen.label.toLowerCase();
          var returnItem = returnItems[key];
          if (!returnItem) {
            returnItems[key] = uiReturnItem(distItem);
            $scope.input.orderedLabels.push(key);
          } else {
            returnItem.orders.push({name: distItem.orderName, quantity: distItem.quantity});
            returnItem.distQty = returnItem.orderName = returnItem.quantity = undefined;
          }
        }
      );
    }

    $scope.getSpecimenDetails = function() {
      var labels = Util.splitStr($scope.input.labels, /,|\t|\n/);
      var filterOpts = {};
      if (!!$scope.input.useBarcode) {
        filterOpts.barcode = labels;
        labels = [];
      }

      labels = labels.filter(
        function(label) {
          return !$scope.input.returnItems[label.toLowerCase()];
        }
      );

      if (labels.length == 0 && (!filterOpts.barcode || filterOpts.barcode.length == 0)) {
        return;
      }

      DistributionOrder.getDistributionDetails(labels, filterOpts).then(
        function(distItems) {
          resolveDistItems(labels, distItems).then(addReturnItems);
        }
      );
    }

    $scope.orderSelected = function(retItem) {
      var selectedOrder = undefined;
      for (var i = 0; i < retItem.orders.length; ++i) {
        if (retItem.orders[i].name == retItem.orderName) {
          retItem.distQty = retItem.quantity = retItem.orders[i].quantity;
          break;
        }
      }
    }

    $scope.removeSpecimen = function(label) {
      var idx = $scope.input.orderedLabels.indexOf(label);
      if (idx == -1) {
        return;
      }

      delete $scope.input.returnItems[label];
      $scope.input.orderedLabels.splice(idx, 1);
    }

    $scope.copyFirstToAll = function() {
      if ($scope.input.orderedLabels.length < 1) {
        return;
      }

      var key = $scope.input.orderedLabels[0].toLowerCase();
      var srcItem = $scope.input.returnItems[key];
      angular.forEach($scope.input.returnItems,
        function(tgtItem, idx) {
          if (idx == 0) {
            return;
          }

          var location = srcItem.specimen.storageLocation;
          tgtItem.specimen.storageLocation = {name: location.name, mode: location.mode};
          tgtItem.user = srcItem.user;
          tgtItem.time = srcItem.time;
          tgtItem.comments = srcItem.comments;
        }
      );
    }

    $scope.returnSpecimens = function() {
      var returnItems = [];
      angular.forEach($scope.input.orderedLabels,
        function(label) {
          var retItem = $scope.input.returnItems[label];
          returnItems.push({
            orderName: retItem.orderName,
            specimenLabel: retItem.specimen.label,
            quantity: retItem.quantity,
            location: retItem.specimen.storageLocation,
            user: retItem.user,
            time: retItem.time,
            incrFreezeThaw: retItem.incrFreezeThaw,
            comments: retItem.comments
          });
        }
      );

      DistributionOrder.returnSpecimens(returnItems).then(
        function(spmns) {
          Alerts.success("orders.specimens_returned", {count: spmns.length});
          $state.go('order-list');
        }
      );
    }

    init();
  });
