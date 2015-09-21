angular.module('os.administrative.container.locations', ['os.administrative.models'])
  .controller('ContainerLocationsCtrl', function(
    $scope, $state, container, occupancyMap,
    Util, ContainerUtil, Alerts) {

    function init() {
      $scope.container = container;
      $scope.pristineMap = $scope.occupancyMap = occupancyMap;
      $scope.input = {labels: '', noFreeLocs: false, vacateOccupants: false};
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
        $scope.input.labels,
        $scope.input.vacateOccupants);

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

      var posMap = {};
      var vacatedEntities = [];
      for (var i = 0; i < $scope.occupancyMap.length; ++i) {
        var pos = $scope.occupancyMap[i];
        if (!!pos.id) {
          continue;
        }

        if (!pos.occupyingEntityName || pos.occupyingEntityName.trim().length == 0) {
          vacatedEntities.push(pos.oldOccupant);
        } else {
          posMap[pos.occupyingEntityName.toLowerCase()] = pos;
          delete pos.oldOccupant;
        }
      }

      for (var i = vacatedEntities.length - 1; i >= 0; --i) {
        var label = vacatedEntities[i].occupyingEntityName.toLowerCase();
        if (!!posMap[label]) {
          vacatedEntities.splice(i, 1);
        } else {
          posMap[label] = {occuypingEntity: 'specimen', occupyingEntityName: label};
        }
      }

      var positions = [];
      angular.forEach(posMap, function(pos) {
        positions.push(pos);
      });

      if (positions.length == 0) {
        return;
      }

      var assignOp = {'vacateOccupant': $scope.input.vacateOccupants, 'positions': positions};
      container.assignPositions(assignOp).then(
        function(latestOccupancyMap) {
          $scope.pristineMap = $scope.occupancyMap = latestOccupancyMap;
          $scope.input.labels = undefined;
        }
      );
    }

    init();
  });
