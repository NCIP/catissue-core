
angular.module('os.administrative.container.map', ['os.administrative.container.util'])
  .directive('osContainerMap', function($compile, ContainerUtil) {
    function drawMap(scope, element, allowClicks) {
      var locationMatrix = getLocationsMatrix(scope.container, scope.occupancyMap);

      var width = element.width();
      var noOfColumns = scope.container.noOfColumns;
      var slotWidth = Math.floor(width / 10);
      if (slotWidth * noOfColumns > width) {
        slotWidth = width / noOfColumns;
        if (slotWidth < 85) {
          slotWidth = 85;
        }
      }

      var table = $("<table class='os-container-map' ng-click='addContainer($event)'/>");
      for (var i = 0; i < locationMatrix.length; ++i) {
        var tr = $("<tr/>");

        for (var j = 0; j < locationMatrix[i].length; ++j) {
          var td = $("<td/>")
            .css("min-width", slotWidth)
            .append(getContainerSlot(scope.container, locationMatrix[i][j], allowClicks));

          if (locationMatrix[i][j].occupied) {
            td.addClass(!!locationMatrix[i][j].occupied.id ? 'slot-occupied' : 'slot-assigned');

            if (!!locationMatrix[i][j].occupied.oldOccupant) {
              td.addClass('slot-vacated');
            }
          }

          tr.append(td);
        }

        table.append(tr);
      }

      element.append(table);
    }

    function getLocationsMatrix(container, occupancyMap) {
      var matrix = new Array(container.noOfRows);
      for (var i = 0; i < container.noOfRows; ++i) {
        matrix[i] = new Array(container.noOfColumns);

        for (var j = 0; j < container.noOfColumns; ++j) {
          matrix[i][j] = {posX: j + 1, posY: i + 1};
        }
      }

      angular.forEach(occupancyMap, function(position) {
        matrix[position.posTwoOrdinal - 1][position.posOneOrdinal - 1].occupied = position;
      });

      return matrix;
    };

    function formatPos(posX, posY) {
      return "(" + posY + ", " + posX + ")";
    };

    function getContainerSlotEl(container, slot, allowClicks) {
      var el = $("<div class='slot-element'/>");

      var posOne = ContainerUtil.fromOrdinal(container.columnLabelingScheme, slot.posX);
      var posTwo = ContainerUtil.fromOrdinal(container.rowLabelingScheme, slot.posY);
      el.append($("<div class='slot-pos'/>").append(formatPos(posOne, posTwo)));

      if (slot.occupied) {
        var slotDesc = $("<a class='slot-desc'/>")
          .attr('title', slot.occupied.occupyingEntityName)
          .append(slot.occupied.occupyingEntityName);

        if (allowClicks) {
          var entityId = slot.occupied.occupyingEntityId;
          if (slot.occupied.occuypingEntity == 'specimen') {
            slotDesc.attr('ui-sref', 'specimen({specimenId: ' + entityId + '})');
          } else {
            slotDesc.attr('ui-sref', 'container-detail.locations({containerId: ' + entityId + '})');
          }
        }

        el.append(slotDesc);
      } else if (allowClicks) {
        el.append(getAddContainerMarker())
          .addClass("os-pointer-cursor")
          .attr({'data-pos-x': posOne, 'data-pos-y': posTwo})
          .attr('title', 'Click to add container');
      }

      return el;
    }

    function getContainerSlot(container, slot, allowClicks) {
      return $("<div class='slot'/>")
        .append("<div class='slot-dummy'></div>")
        .append(getContainerSlotEl(container, slot, allowClicks));
    };

    function getAddContainerMarker() {
      return $("<div class='slot_add'/>")
        .append("<span class='fa fa-plus'></span>");
    };

    return {
      restrict: 'EA',

      replace: true,

      scope: {
        container: '=',
        occupancyMap: '=',
        onAddEvent: '&'
      },

      link: function(scope, element, attrs) {
        scope.addContainer = function($event) {
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
          drawMap(scope, element, angular.isDefined(attrs.onAddEvent));
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
