
angular.module('os.administrative.container.map', [])
  .directive('osContainerMap', function() {

    function getLocationsMatrix(container, occupancyMap) {
      var matrix = new Array(container.dimensionTwoCapacity);

      for (var i = 0; i < container.dimensionTwoCapacity; ++i) {
        matrix[i] = new Array(container.dimensionOneCapacity);

        for (var j = 0; j < container.dimensionOneCapacity; ++j) {
          matrix[i][j] = {posX: j + 1, posY: i + 1};
        }
      }

      angular.forEach(occupancyMap, function(position) {
        matrix[position.posTwoOrdinal - 1][position.posOneOrdinal - 1].occupied = position;
      });

      return matrix;
    };

    function formatPos(slot) {
      return "(" + slot.posX + ", " + slot.posY + ")";
    };

    function getContainerSlotEl(slot) {
      var el = $("<div/>").addClass('slot-element');

      var slotPos = $("<div/>").addClass('slot-pos').append(formatPos(slot));
      el.append(slotPos);

      if (slot.occupied) {
        var slotDesc = $("<a/>").addClass("slot-desc").append(slot.occupied.occupyingEntityName);
        el.append(slotDesc);
      }

      return el;
    }

    function getContainerSlot(slot) {
      return $("<div/>")
        .addClass("slot")
        .append("<div class='slot-dummy'></div>")
        .append(getContainerSlotEl(slot));
    };

    return {
      restrict: 'EA',

      replace: true,

      scope: {
        container: '=',
        occupancyMap: '='
      },

      link: function(scope, element, attrs) {
        var locationMatrix = getLocationsMatrix(scope.container, scope.occupancyMap);

        var width = element.width();
        var noOfColumns = scope.container.dimensionOneCapacity;
        var slotWidth = width / 10;
        if (slotWidth * noOfColumns > width) {
          slotWidth = width / noOfColumns;
          if (slotWidth < 85) {
            slotWidth = 85;
          }
        }

        var table = $("<table/>").addClass('os-container-map');
        for (var i = 0; i < locationMatrix.length; ++i) {
          var tr = $("<tr/>");

          for (var j = 0; j < locationMatrix[i].length; ++j) {
            var td = $("<td/>")
              .css("width", slotWidth)
              .append(getContainerSlot(locationMatrix[i][j]));

            if (locationMatrix[i][j].occupied) {
              td.addClass("slot-occupied");
            }

            tr.append(td);
          }

          table.append(tr);
        }

        element.append(table);

        if (slotWidth * noOfColumns < element.width()) {
          element.width(slotWidth * noOfColumns);
        } else {
          table.css('width', '100%');
        }

        if (table.height() + 20 < element.height()) {
          element.height(table.height() + 20);
        }
      },

      template: '<div class="os-container-map-wrapper"></div>'
    };
  });
