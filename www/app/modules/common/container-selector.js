angular.module('openspecimen')
  .directive('osContainerSelector', function(Container) {
    var extend = angular.extend;

    function linker(scope, element, attrs) {
      scope.entityType = scope.type;
      scope.toggleContainerSelection = toggleContainerSelection(scope);
      scope.loadChildren = loadChildren(scope);

      scope.$watch('criteria', function(newVal, oldVal) {
        if (!newVal) {
          return;
        }

        initContainersList(scope);
      });
    };

    function initContainersList(scope) {
      scope.containers = [];

      var criteria = getContainerListCriteria(scope);
      Container.query(extend({topLevelContainers: true}, criteria)).then(
        function(containers) {
          addChildPlaceholders(containers);
          scope.containers = Container.flatten(containers);
        }
      );
    }

    function getContainerListCriteria(scope) {
      var criteria = {
        onlyFreeContainers: true,
        hierarchical: true
      };

      return extend(criteria, scope.criteria);
    }

    function addChildPlaceholders(containers) {
      angular.forEach(containers, function(container) {
        container.childContainers = [{id: -1, name: 'Loading ...'}];
        container.childContainersLoaded = false;
      });
    }

    function toggleContainerSelection(scope) {
      return function(container) {
        if (scope.selectedContainer && scope.selectedContainer != container) {
          scope.selectedContainer.selected = false;
        }

        container.selected = !container.selected;
        scope.selectedContainer = container.selected ? container : {};
        scope.onToggleSelection({container: container});
      }
    }

    function loadChildren(scope) {
      return function(container) {
        if (container.childContainersLoaded) {
          return;
        }

        var idx = scope.containers.indexOf(container);
        var criteria = getContainerListCriteria(scope);
        Container.query(extend({parentContainerId: container.id}, criteria)).then(
          function(containers) {
            addChildPlaceholders(containers);
            container.childContainersLoaded = true;

            var childContainers = Container._flatten(containers, 'childContainers', container, container.depth + 1);
            var args = [idx + 1, 1].concat(childContainers)
            Array.prototype.splice.apply(scope.containers, args);
            if (containers.length == 0) {
              container.hasChildren = false;
            }
          }
        );
      };
    }

    return {
      restrict: 'E',
 
      replace: true,
 
      templateUrl: 'modules/common/container-selector.html',

      scope: {
        type: '=',
        criteria: '=',
        onToggleSelection: '&'
      },

      link: linker
    };
  });
