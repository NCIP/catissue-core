angular.module('openspecimen')
  .directive('osContainerSelector', function(Container) {
    var extend = angular.extend;

    function linker(scope, element, attrs) {
      var criteria = getContainerListCriteria(scope);

      scope.entityType = scope.entity.getType();
      scope.toggleContainerSelection = toggleContainerSelection(scope);
      scope.loadChildren = loadChildren(scope, criteria);

      initContainersList(scope, criteria);
    };

    function initContainersList(scope, criteria) {
      scope.containers = [];

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

      if (scope.entity.getType() == 'specimen') {
        extend(criteria, {
          storeSpecimensEnabled: true,
          specimenClass: scope.entity.specimenClass,
          specimenType: scope.entity.type,
          cpId: scope.cpId
        });
      } else {
        extend(criteria, {site: scope.entity.siteName});
      }

      return criteria;
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

    function loadChildren(scope, criteria) {
      return function(container) {
        if (container.childContainersLoaded) {
          return;
        }

        var idx = scope.containers.indexOf(container);
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
        entity: '=',
        cpId: '=?',
        onToggleSelection: '&'
      },

      link: linker
    };
  });
