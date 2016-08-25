
angular.module('openspecimen')
  .directive('osStoragePositions', function(Container) {
    return {
      restrict: 'A',

      controllerAs: '$storagePositions',

      controller: function($scope) {
        this.entities = [];

        this.cachedContainers = {};

        this.addEntity = function(entity) {
          this.entities.push(entity);
        }

        this.clearEntities = function() {
          this.entities.length = 0;
        }

        this.assignedPositions = function() {
          var assignedPositions = {};
          angular.forEach(this.entities,
            function(entity) {
              if (!entity.storageLocation || !entity.storageLocation.name) {
                return;
              }

              var positions = assignedPositions[entity.storageLocation.name];
              if (!positions) {
                assignedPositions[entity.storageLocation.name] = (positions = []);
              }

              positions.push(entity.storageLocation);
            }
          );
          return assignedPositions;
        }

        this.getContainers = function(params) {
          var key = JSON.stringify(params);
          var q = this.cachedContainers[key];
          if (!q) {
            q = this.cachedContainers[key] = Container.query(params);
          }

          return q;
        }
      }
    }
  })
  .directive('osStoragePosition', function($modal, $timeout, Container) {
    function loadContainers(name, scope, ctrl) {
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
          maxResults: 10
        });
      } else {
        if (!scope.entity.siteName) {
          return;
        }

        angular.extend(params, {
          site: scope.entity.siteName,
          canHold: scope.entity.typeName
        });
      }

      var q;
      if (!!ctrl) {
        q = ctrl.getContainers(params);
      } else {
        var key = JSON.stringify(params);
        q = scope.containerListCache[key];
        if (!q) {
          q = scope.containerListCache[key] = Container.query(params);
        }
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

    function watchOccupyingEntityChanges(scope, ctrl) {
      var objType = scope.entity.getType() == 'specimen' ? ['entity.type'] : ['entity.siteName', 'entity.typeName'];
      scope.$watchGroup(objType, function(newVal, oldVal) {
        if (!newVal || newVal == oldVal) {
          return;
        }

        loadContainers('', scope, ctrl);
      });
    }

    function linker(scope, element, attrs, ctrl) {
      var entity = scope.entity;
      scope.containerListCache = scope.containerListCache || {};

      if (!!ctrl) {
        ctrl.addEntity(entity);
      }

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
            },

            assignedPositions: function() {
              return !!ctrl ?  ctrl.assignedPositions() : {};
            }
          }
        });

        modalInstance.result.then(
          function(position) {
            var location = angular.extend({}, scope.entity.storageLocation);
            scope.entity.storageLocation = angular.extend(location, position);
          }
        );
      };

      scope.searchContainer = function(name) {
        loadContainers(name, scope, ctrl);
      };

      scope.showStorageContainers = function () {
        scope.virtual = false;
      }

      watchOccupyingEntityChanges(scope, ctrl);
    };

    return {
      require: '?^osStoragePositions',

      restrict: 'E',
  
      replace: true,

      priority: 100,

      terminal: true,

      scope: {
        entity: '=',
        cpId: '=',
        virtual: "=",
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

          //
          // make button smaller in size
          // ensure button is aligned with other text fields;
          // 15 px is used to display labels for os-md-input fields
          //
          button.addClass('btn-xs');
          if (!tAttrs.hasOwnProperty('hidePlaceholder')) {
            button.css('margin-top', '15px');
          }
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
