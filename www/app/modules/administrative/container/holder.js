
angular.module('os.administrative.container')
  .factory('ContainerViewState', function($translate, $document, $location, $anchorScroll, $timeout, Container) {
    var placeholder = {name: 'Loading ...'};
    $translate('common.loading').then(
      function(val) {
        placeholder.name = val;
      }
    );

    function setChildrenPlaceholders(containers) {
      angular.forEach(containers,
        function(container, idx) {
          if (container.childContainers && container.childContainers.length > 0) {
            container.childContainersLoaded = true;
            setChildrenPlaceholders(container.childContainers);
          } else {
            container.childContainersLoaded = false;
            container.hasChildren = true;
            container.childContainers = [angular.extend({id: -1 * container.id + '.' + idx}, placeholder)];
          }
        }
      );

      return containers;
    }

    function ViewStateFactory() {
      var state = { hierarchy: [] }
    
      state.setHierarchy = function(hierarchy) {
        state.hierarchy = Container.flatten(setChildrenPlaceholders([hierarchy]));
        return state.hierarchy.length > 1 ? state.hierarchy[0].id : -1;
      }

      state.getHierarchy = function() {
        return state.hierarchy;
      }

      state.getRootContainerId = function() {
        return state.hierarchy.length > 0 ? state.hierarchy[0].id : -1;
      }

      state.getContainer = function(containerId) {
        var container = undefined;
        for (var i = 0; i < state.hierarchy.length; ++i) {
          if (state.hierarchy[i].id == containerId) {
            container = state.hierarchy[i];
            break;
          }
        }

        if (!container) {
          //
          // Details of requested container should be within hierarchy
          //
          return null;
        }

        if (!container.$$detailLoaded) {
          return Container.getById(containerId).then(
            function(result) {
              angular.extend(container, result);
              container.$$detailLoaded = true;
              return container;
            }
          );
        } else {
          return container;
        }
      }

      state.recordScrollPosition = function() {
        state.scrollTop = $document.find('.os-container-tree').scrollTop();
      }

      state.scrollTo = function(containerId) {
        $timeout(function() {
          if (state.scrollTop == undefined) {
            $location.hash('container-' + containerId);
            $anchorScroll();
          } else {
            $document.find('.os-container-tree').scrollTop(state.scrollTop);
          }
        }, 0, false);
      }

      return state;
    }

    return ViewStateFactory;
  });
