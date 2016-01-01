
angular.module('os.administrative.container.map', ['os.common.box', 'os.administrative.container.util'])
  .directive('osContainerMap', function($compile, BoxLayoutUtil) {
    return {
      restrict: 'EA',

      replace: true,

      scope: {
        container:    '=',
        occupancyMap: '=',
        onAddEvent:   '&'
      },

      link: function(scope, element, attrs) {
        var container = scope.container;
        var opts = {
          box: {
            instance             : container,
            row                  : function(occupant) { return occupant.posTwoOrdinal; },
            column               : function(occupant) { return occupant.posOneOrdinal; },
            numberOfRows         : function() { return container.noOfRows; },
            numberOfColumns      : function() { return container.noOfColumns; },
            rowLabelingScheme    : function() { return container.rowLabelingScheme; },
            columnLabelingScheme : function() { return container.columnLabelingScheme; }
          },

          occupants: [],
          occupantName: function(occupant) {
            return occupant.occupyingEntityName
          },
          occupantSref: function(occupant) {
            if (occupant.occuypingEntity == 'specimen') {
              return 'specimen({specimenId: ' + occupant.occupyingEntityId + '})';
            } else {
              return 'container-detail.locations({containerId: ' + occupant.occupyingEntityId + '})';
            }
          },
          allowClicks: angular.isDefined(attrs.onAddEvent)
        };

        scope.onClick = function($event) {
          var target = angular.element($event.originalEvent.target);
          while (!target.hasClass('slot-element') && !target.is("table")) {
            target = target.parent();
          }

          if (target.attr('data-pos-x') && target.attr('data-pos-y')) {
            scope.onAddEvent()(target.attr('data-pos-x'), target.attr('data-pos-y'));
          }
        };

        scope.$watch('occupancyMap', function() {
          element.children().remove();

          opts.occupants = scope.occupancyMap;
          BoxLayoutUtil.drawLayout(element, opts);

          $compile(element)(scope);
        });
      },

      template: '<div class="os-container-map-wrapper"></div>'
    };
  })

  .directive('osContainerPositionSelector', function($timeout, ContainerUtil) {
    function renderGrid(element, container, selectedPos) {
      return new openspecimen.ui.container.ContainerPositionSelector({
        parentEl: element,
        container: container,
        inputPos: selectedPos,
        containerUtil: ContainerUtil,
        onSelect: function(position) {
          $timeout(function() {
            selectedPos.positionX = position.posX;
            selectedPos.positionY = position.posY;
          });
        }
      }).render();
    };

    return {
      restrict: 'E',

      scope: {
        container: '=',
        selectedPos: '='
      },

      link: function(scope, element, attrs) {
        var grid = undefined;
        scope.$watch('container', function(newVal, oldVal) {
          if (grid && newVal == oldVal) {
            return;
          }

          if (grid) {
            grid.destroy();
            grid = undefined;
          }

          if (!scope.container) {
            return;
          }

          grid = renderGrid(element, scope.container, scope.selectedPos);
        });
      }
    };
  });
