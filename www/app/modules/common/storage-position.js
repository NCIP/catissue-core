
angular.module('openspecimen')
  .directive('osStoragePosition', function($modal, $timeout, Container) {
    function loadContainers(name, scope) {
      scope.entityType = scope.entity.getType();
      var params = {
        name: name, 
        onlyFreeContainers: true
      }

      if (scope.entityType == 'specimen') {
        if (!scope.entity.type) {
          return;
        }

        angular.extend(params, {
          cpId: scope.cpId,
          specimenClass: scope.entity.specimenClass,
          specimenType: scope.entity.type,
          storeSpecimensEnabled: true,
          maxRecords: 10
        });
      } else {
        if (!scope.entity.siteName) {
          return;
        }

        angular.extend(params, {site: scope.entity.siteName});
      }

      var q = scope.containerListCache[JSON.stringify(params)];
      if (!q) {
        q = Container.query(params);
        scope.containerListCache[JSON.stringify(params)] = q;
      }

      return q.then(
        function(containers) {
          scope.containers = containers.map(
            function(container) { 
              return container.name; 
            }
          );
        }
      );
    };

    function watchOccupyingEntityChanges(scope) {
      var objType = scope.entity.getType() == 'specimen' ? 'entity.type' : 'entity.siteName';
      scope.$watch(objType, function(newVal, oldVal) {
        if (!newVal || newVal == oldVal) {
          return;
        }

        loadContainers('', scope);
      });
    }

    function linker(scope, element, attrs) {
      var entity = scope.entity;
      scope.containerListCache = scope.containerListCache || {};

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
            scope.entity.storageLocation = scope.entity.storageLocation || {};
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

      watchOccupyingEntityChanges(scope);
    };

    return {
      restrict: 'E',
  
      replace: true,

      priority: 100,

      terminal: true,

      scope: {
        entity: '=',
        cpId: '=',
        containerListCache: '=?'
      },

      compile: function(tElem, tAttrs) {
        var select = tElem.find('os-select');
        var input = tElem.find('input');

        if (tAttrs.hasOwnProperty('osMdInput')) {
          var button = tElem.find('button');

          select.attr('os-md-input', '');
          input.attr('os-md-input', '');

          if (tAttrs.hasOwnProperty('editWhen')) {
            select.attr('edit-when', tAttrs.editWhen);
            input.attr('edit-when', tAttrs.editWhen);
            button.attr('ng-if', tAttrs.editWhen);
          }

          tElem.find('button')
            .addClass('btn-xs')         // make button smaller in size
            .css('margin-top', '15px'); // ensure button is aligned with other text fields; 
                                        // 15 px is used to display labels for os-md-input fields
        }

        if (tAttrs.hasOwnProperty('hidePlaceholder')) {
          select.removeAttr('placeholder');
          input.removeAttr('placeholder');
        }

        return linker;
      },


      templateUrl: 'modules/common/storage-position.html'
    }
  });
    
