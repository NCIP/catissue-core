
angular.module('os.biospecimen.participant.specimen-position')
  .controller('SpecimenPositionSelectorCtrl', 
    function($scope, $modalInstance, $timeout, $q, specimen, cpId, Container) {
      function showErrorMsg(code) {
        $scope.errorCode = code;
        $timeout(function() {$scope.errorCode = undefined;}, 5000);
      }

      function addChildPlaceholders(containers) {
        angular.forEach(containers, function(container) {
          container.childContainers = [{id: -1, name: 'Loading ...'}];
          container.childContainersLoaded = false;
        });
      }

      function isAllowed(container, cpId, specimen) {
        if (typeof container.allowed == 'undefined') {
          return container.isSpecimenAllowed(cpId, specimen.specimenClass, specimen.type).then(
            function(allowed) {
              container.allowed = allowed;
              return allowed;
            }
          );
        } else {
          var def = $q.defer();
          def.resolve(container.allowed);
          return def.promise;
        }
      };

      function init() {
        $scope.selectedContainer = undefined;
        $scope.selectedPos = {};
        $scope.showGrid = false;
        $scope.containers = [];

        Container.query({topLevelContainers: true}).then(
          function(containers) {
            addChildPlaceholders(containers);
            $scope.containers = Container.flatten(containers);
          }
        );
      };

      $scope.toggleSelectedContainer = function(container) {
        $scope.showGrid = false;

        if ($scope.selectedContainer) {
          $scope.selectedContainer.selected = false;
        }

        if (container.selected) {
          $scope.selectedPos = {id: container.id, name: container.name};
          $scope.selectedContainer = container;
        } else {
          $scope.selectedPos = {};
          $scope.selectedContainer = undefined;
        }
      };

      $scope.getOccupancyMap = function() {
        if (!$scope.selectedContainer) {
          showErrorMsg('container.no_container_selected');
          return false;
        }

        return isAllowed($scope.selectedContainer, cpId, specimen).then(
          function(allowed) {
            if (!allowed) {
              showErrorMsg('container.cannot_hold_specimen');
              return false;
            }
         
            if ($scope.selectedContainer.occupiedPositions) {
              $scope.showGrid = true;
              return $scope.selectedContainer;
            }

            return Container.getById($scope.selectedContainer.id).then(
              function(container) {
                angular.extend($scope.selectedContainer, container);
                $scope.showGrid = true;
                return true;
              }
            );
          }
        );
      };

      $scope.loadChildren = function(container) {
        if (container.childContainersLoaded) {
          return;
        }

        var idx = $scope.containers.indexOf(container);
        Container.query({parentContainerId: container.id}).then(
          function(containers) {
            addChildPlaceholders(containers);
            container.childContainersLoaded = true;

            var childContainers = Container._flatten(containers, 'childContainers', container, container.depth + 1);
            var args = [idx + 1, 1].concat(childContainers)
            Array.prototype.splice.apply($scope.containers, args);
            if (containers.length == 0) {
              container.hasChildren = false;
            }
          }
        );
      };

      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      };

      $scope.ok = function() {
        if (!$scope.selectedContainer) {
          $scope.cancel();
        } else {
          isAllowed($scope.selectedContainer, cpId, specimen).then(
            function(allowed) {
              if (allowed) {
                $modalInstance.close($scope.selectedPos);
              } else {
                showErrorMsg('container.cannot_hold_specimen');
              }
            }
          );
        }
      };
             
      init();
    }
  );
