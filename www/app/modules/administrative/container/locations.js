angular.module('os.administrative.container.locations', ['os.administrative.models'])
  .controller('ContainerLocationsCtrl', function(
    $scope, $state, container, Container, ContainerUtil, Alerts, Specimen, SpecimenUtil, Util) {

    function init() {
      $scope.ctx.showTree  = true;
      $scope.ctx.viewState = 'container-detail.locations';
      $scope.lctx = {
        mapState: 'loading',
        input: {labels: '', noFreeLocs: false, vacateOccupants: false},
        entityInfo: {}
      };

      if (container.noOfRows > 0 && container.noOfColumns > 0) {
        loadMap(container);
      }
    }

    function loadMap(container) {
      $scope.lctx.mapState = 'loading';
      container.getOccupiedPositions().then(
        function(occupancyMap) {
          $scope.lctx.mapState = 'loaded';
          $scope.lctx.pristineMap = $scope.lctx.occupancyMap = occupancyMap;
        },

        function() {
          $scope.lctx.mapState = 'error';
        }
      );
    }

    function addSpecimens() {
      var labels = Util.splitStr($scope.lctx.input.labels,/,|\t|\n/, false)
      SpecimenUtil.getSpecimens(labels).then(
        function(specimens) {
          if (!specimens) {
            return;
          }

          var positions = specimens.map(
            function(specimen) {
              return {occuypingEntity: 'specimen', occupyingEntityId: specimen.id};
            }
          );

          var assignOp = {positions: positions};
          container.assignPositions(assignOp).then(
            function() {
              Alerts.success('container.added_specimens', {spmnsCount: positions.length});
              $scope.lctx.input.labels = undefined;
            }
          );
        }
      );
    }

    $scope.showInfo = function(entityType, entityId) {
      var promise;
      if (entityType == 'specimen') {
        promise = Specimen.getById(entityId);
      } else if (entityType == 'container') {
        promise = Container.getById(entityId);
      } else {
        return;
      }

      promise.then(
        function(entity) {
          $scope.ctx.showTree    = false;
          $scope.lctx.entityInfo = {type: entityType, id: entityId};
          $scope.lctx.entityInfo[entityType] = entity;
        }
      );
    }

    $scope.hideInfo = function() {
      $scope.lctx.entityInfo    = {};
      $scope.ctx.showTree       = true;
    }

    $scope.addContainer = function(posOne, posTwo, pos) {
      var params = {
        containerId: '',
        posOne: posOne, posTwo: posTwo, pos: pos,
        siteName: container.siteName,
        parentContainerName: container.name,
        parentContainerId: container.id
      };
      $state.go('container-addedit', params);
    }

    $scope.showUpdatedMap = function() {
      if ($scope.ctx.dimless) {
        return;
      }

      var result = ContainerUtil.assignPositions(
        container,
        $scope.lctx.pristineMap,
        $scope.lctx.input.labels,
        $scope.lctx.input.vacateOccupants);

      $scope.lctx.occupancyMap = result.map;

      $scope.lctx.input.noFreeLocs = result.noFreeLocs;
      if (result.noFreeLocs) {
        Alerts.error("container.no_free_locs");
      }
    }

    $scope.assignPositions = function() {
      if ($scope.ctx.dimless) {
        addSpecimens();
        return;
      }

      if ($scope.lctx.input.noFreeLocs) {
        Alerts.error("container.no_free_locs");
        return;
      }

      var addedEntities = [], vacatedEntities = [];
      for (var i = 0; i < $scope.lctx.occupancyMap.length; ++i) {
        var pos = $scope.lctx.occupancyMap[i];
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

          var assignOp = {vacateOccupant: $scope.lctx.input.vacateOccupants, positions: positions};
          container.assignPositions(assignOp).then(
            function(latestOccupancyMap) {
              $scope.lctx.pristineMap = $scope.lctx.occupancyMap = latestOccupancyMap;
              $scope.lctx.input.labels = undefined;
            }
          );
        }
      );
    }

    init();
  });
