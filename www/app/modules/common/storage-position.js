
angular.module('openspecimen')
  .directive('osStoragePositions', function(Container) {
    return {
      restrict: 'A',

      controllerAs: '$storagePositions',

      controller: function($scope) {
        var ctrl = this;

        this.entities = [];

        this.cachedContainers = {};

        this.addEntity = function(entity) {
          ctrl.entities.push(entity);
        }

        this.clearEntities = function() {
          ctrl.entities.length = 0;
        }

        this.assignedPositions = function() {
          var assignedPositions = {};
          angular.forEach(ctrl.entities,
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
          var q = ctrl.cachedContainers[key];
          if (!q) {
            q = ctrl.cachedContainers[key] = Container.query(params);
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
          site: scope.site,
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
          scope.containers     = containers;
          scope.containerNames = containers.map(function(c) { return c.name; });
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
        if (!entity.storageLocation || !entity.storageLocation.name) {
          // case of unselect
          entity.storageLocation = {};
          return;
        }

        var containers = scope.containers;
        for (var i = 0; i < containers.length; ++i) {
          if (containers[i].name == entity.storageLocation.name) {
            entity.storageLocation = {name: containers[i].name, mode: containers[i].positionLabelingMode};
            break;
          }
        }
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
        containerListCache: '=?',
        site: '=?'
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
  })

  .directive('osDispStoragePosition', function() {
    return {
      restrict: 'E',

      replace: true,

      scope: {
        position: '='
      },

      template:
        '<span ng-switch on="!!position.name"> ' +
          '<span ng-switch-when="true"> ' +
            '<span>{{position.name}}</span> ' +
            '<span ng-switch on="position.mode == \'LINEAR\'"> ' +
              '<span ng-switch-when="true">({{position.position}})</span> ' +
              '<span ng-switch-when="false">({{position.positionY}} x  {{position.positionX}})</span> ' +
            '</span>' +
          '</span>' +
          '<span ng-switch-default> ' +
            '<span translate="specimens.virtually_located">Virtual</span>' +
          '</span>' +
        '</span>',

      link: function() { }
    };
  });
