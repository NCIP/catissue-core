angular.module('os.administrative.container.locations', ['os.administrative.models'])
  .controller('ContainerLocationsCtrl', function(
    $scope, $state, container, occupancyMap, 
    Util, ContainerUtil, Alerts) {

    function init() {
      $scope.container = container;
      $scope.pristineMap = $scope.occupancyMap = occupancyMap;
      $scope.input = {labels: '', noFreeLocs: false};
    }

    $scope.addContainer = function(posOne, posTwo) {
      var params = {
        containerId: '',
        posOne: posOne, posTwo: posTwo,
        parentContainerName: container.name,
        parentContainerId: container.id
      };
      $state.go('container-addedit', params);
    }

    $scope.showUpdatedMap = function() {
      var result = ContainerUtil.assignPositions(
        container, 
        $scope.pristineMap, 
        $scope.input.labels);

      $scope.occupancyMap = result.map;
      $scope.input.noFreeLocs = result.noFreeLocs;
      if (result.noFreeLocs) {
        Alerts.error("container.no_free_locs");
      }
    }

    $scope.assignPositions = function() {
      if ($scope.input.noFreeLocs) {
        Alerts.error("container.no_free_locs");
        return;
      }

      var positions = [];
      for (var i = 0; i < $scope.occupancyMap.length; ++i) {
        var pos = $scope.occupancyMap[i];
        if (!!pos.id) {
          continue;
        }

        positions.push(pos);
      }

      if (positions.length == 0) {
        return;
      }

      container.assignPositions(positions).then(
        function(latestOccupancyMap) {
          $scope.pristineMap = $scope.occupancyMap = latestOccupancyMap;
          $scope.input.labels = undefined;
        }
      );
    }

    init();
  });
