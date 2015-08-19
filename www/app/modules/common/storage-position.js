
angular.module('openspecimen')
  .directive('osStoragePosition', function($modal, $timeout, Container) {
    function loadContainers(name, scope) {
      var params = {
        name: name, 
        onlyFreeContainers: true
      }

      scope.entityType = scope.entity.getType();

      var entityParams = getEntityParams(scope);
      if (!entityParams) {
        return;
      }

      angular.extend(params, entityParams);

      return Container.query(params).then(
        function(containers) {
          scope.containers = containers.map(
            function(container) { 
              return container.name; 
            }
          );
        }
      );
    };

    function getEntityParams(scope) {
      var params = {};
      if (scope.entity.getType() == 'specimen') {
        angular.extend(params, {
          cpId: scope.cpId,
          specimenClass: scope.entity.specimenClass,
          specimenType: scope.entity.type,
          storeSpecimensEnabled: true
        })
      } else if (scope.entity.getType() == 'storage_container') {
        if (!scope.entity.siteName) {
          return;
        }

        angular.extend(params, {site: scope.entity.siteName});
      }
      return params;
    }

    function addWatch(scope) {
      var obj = undefined;
      if (scope.entity.getType() == 'specimen') {
        obj = 'entity.type';
      } else if (scope.entity.getType() == 'storage_container') {
        obj = 'entity.siteName';
      }

      scope.$watch(obj, function(newVal, oldVal) {
        if (!newVal || newVal == oldVal) {
          return;
        }

        loadContainers('', scope);
      });
    }

    function linker(scope, element, attrs) {
      var entity = scope.entity;

      scope.onContainerChange = function() {
        entity.storageLocation = {name: entity.storageLocation.name};
      };

      scope.openPositionSelector = function() {
        var modalInstance = $modal.open({
          templateUrl: 'modules/common/storage-position-selector.html',
          controller: 'StoragePositionSelectorCtrl',
          size: 'lg',
          resolve: {
            entity: function() {
              return scope.entity
            },

            cpId: function() {
              return scope.cpId;
            }
          }
        });

        modalInstance.result.then(
          function(position) {
            if (!scope.entity.storageLocation) {
              scope.entity.storageLocation = {};
            }
            angular.extend(scope.entity.storageLocation, position);
            $timeout(function() {
              angular.extend(scope.entity.storageLocation, position);
            });
          }
        );
      };

      scope.searchContainer = function(name) {
        loadContainers(name, scope);
      };

      addWatch(scope);
    };

    return {
      restrict: 'E',
  
      replace: true,

      priority: 100,

      terminal: true,

      scope: {
        entity: '=',
        cpId: '='
      },

      compile: function(tElem, tAttrs) {
        if (tAttrs.hasOwnProperty('osMdInput')) {
          var select = tElem.find('os-select');
          var input = tElem.find('input');
          var button = tElem.find('button');

          select.attr('os-md-input', '');
          input.attr('os-md-input', '');

          if (tAttrs.hasOwnProperty('editWhen')) {
            select.attr('edit-when', tAttrs.editWhen);
            input.attr('edit-when', tAttrs.editWhen);
            button.attr('ng-if', tAttrs.editWhen);
          }

          tElem.find('button').addClass('btn-xs');
        }
 
        return linker;
      },


      templateUrl: 'modules/common/storage-position.html'
    }
  });
    
