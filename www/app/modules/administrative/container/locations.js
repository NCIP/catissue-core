angular.module('os.administrative.container.locations', ['os.administrative.models'])
  .controller('ContainerLocationsCtrl', function(
    $scope, $state, container, occupancyMap,
    Util, ContainerUtil, Alerts, SpecimenUtil) {

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

      var addedEntities = [], vacatedEntities = [];
      for (var i = 0; i < $scope.occupancyMap.length; ++i) {
        var pos = $scope.occupancyMap[i];
        if (!!pos.id) {
          continue;
        }

        if (!pos.occupyingEntityName || pos.occupyingEntityName.trim().length == 0) {
          vacatedEntities.push(pos.oldOccupant);
        } else {
          addedEntities.push(pos);
          delete pos.oldOccupant;
        }
      }

      if (addedEntities.length == 0 && vacatedEntities.length == 0) {
        return;
      }

      var labels = addedEntities.map(
        function(pos) {
          return pos.occupyingEntityName;
        }
      );

      SpecimenUtil.getSpecimens(labels).then(
        function(specimens) {
          if (!specimens) {
            return;
          }

          var specimensMap = {};
          angular.forEach(specimens, function(spmn) {
            specimensMap[spmn.id] = spmn;
          });

          var positions = [];
          angular.forEach(vacatedEntities, function(entity) {
            if (!specimensMap[entity.occupyingEntityId]) {
              //
              // specimen is not reassigned a new position, vacate it from container
              //
              positions.push({occuypingEntity: 'specimen', occupyingEntityId: entity.occupyingEntityId});
            }
          });

          angular.forEach(addedEntities, function(pos, index) {
            pos.occupyingEntityId = specimens[index].id;
            positions.push(pos);
          });

          var assignOp = {vacateOccupant: $scope.input.vacateOccupants, positions: positions};
          container.assignPositions(assignOp).then(
            function(latestOccupancyMap) {
              $scope.pristineMap = $scope.occupancyMap = latestOccupancyMap;
              $scope.input.labels = undefined;
            }
          );
        }
      );
    }

    init();
  });
