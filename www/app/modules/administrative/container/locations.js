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
      var newMap = angular.copy($scope.pristineMap);
      var mapIdx = 0, labelIdx = 0;
      var labels = Util.splitStr($scope.input.labels, /,|\t|\n/);

      var done = false;
      $scope.input.noFreeLocs = false;
      for (var y = 1; y <= container.noOfRows; ++y) {
        for (var x = 1; x <= container.noOfColumns; ++x) {
          if (labelIdx >= labels.length) {
            done = true;
            break;
          }

          if (mapIdx < newMap.length && newMap[mapIdx].posOneOrdinal == x && newMap[mapIdx].posTwoOrdinal == y) {
            mapIdx++;
            continue;
          }
          
          var label = labels[labelIdx++];
          if (!label || label.trim().length == 0) {
            continue;
          }

          var newPos = {
            occuypingEntity: 'specimen', 
            occupyingEntityName: label,
            posOne: ContainerUtil.fromOrdinal(container.columnLabelingScheme, x),
            posTwo: ContainerUtil.fromOrdinal(container.rowLabelingScheme, y),
            posOneOrdinal: x,
            posTwoOrdinal: y
          };

          newMap.splice(mapIdx, 0, newPos);
          mapIdx++;
        }
        
        if (done) {
          break;
        }
      }
 
      while (labelIdx < labels.length) {
        if (!!labels[labelIdx] && labels[labelIdx].trim().length > 0) {
          $scope.input.noFreeLocs = true;
          Alerts.error("container.no_free_locs");
          break;
        }

        labelIdx++;
      }

      $scope.occupancyMap = newMap;
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
