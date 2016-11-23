
angular.module('openspecimen')
  .controller('StoragePositionSelectorCtrl',
    function($scope, $modalInstance, $timeout, $q, entity, cpId, assignedPositions, Container) {
      function init() {
        $scope.listOpts = { 
          type: entity.getType(),
          name: !!entity.storageLocation ? entity.storageLocation.name : ''
        };

        if (entity.getType() == 'specimen') {
          $scope.listOpts.criteria = {
            storeSpecimensEnabled: true,
            specimenClass: entity.specimenClass,
            specimenType: entity.type,
            cpId: cpId
          }
        } else {
          $scope.listOpts.criteria = { site: entity.siteName };
        }

        $scope.selectedContainer = {}; // step 1
        $scope.selectedPos = {};       // step 2
        $scope.showGrid = false;       // when to draw and show occupancy grid
        $scope.entity = entity;        // occupying entity for which slot is being selected
        $scope.cpId = cpId;
        $scope.assignedPositions = assignedPositions; // positions are selected in current form/session
      };

      $scope.toggleContainerSelection = function(wizard, container) {
        $scope.showGrid = false;
        if (container.selected) {
          $scope.selectedPos = {id: container.id, name: container.name, mode: container.positionLabelingMode};
          $scope.selectedContainer = container;

          if (container.positionLabelingMode == 'NONE') {
            $modalInstance.close($scope.selectedPos);
          } else {
            wizard.next(false);
          }
        } else {
          $scope.selectedPos = {};
          $scope.selectedContainer = {};
        }
      }

      $scope.getOccupancyMap = function() {
        if ($scope.selectedContainer.occupiedPositions) {
          // occupied positions map already loaded
          $scope.showGrid = true;
          return true;
        }

        return Container.getById($scope.selectedContainer.id).then(
          function(container) {
            angular.extend($scope.selectedContainer, container);
            $scope.showGrid = true;
            return true;
          }
        );
      }

      $scope.passThrough = function() {
        return true;
      }

      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      };

      $scope.ok = function() {
        if (!$scope.selectedContainer || !$scope.selectedContainer.id) {
          $scope.cancel();
        } else {
          $modalInstance.close($scope.selectedPos);
        }
      };
             
      init();
    }
  );
