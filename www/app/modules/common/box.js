
angular.module('os.common.box', [])
  .factory('BoxLayoutUtil', function(NumberConverterUtil) {
    //
    // opts: {
    //   box: {
    //     instance: box or array or any other object
    //     row: function to convert row number to corresponding labeling scheme number
    //     column: function to convert column number to corresponding labeling scheme number
    //     numberOfRows: function returning number of box rows
    //     numberOfColumns: function returning number of box columns
    //     rowLabelingScheme: function returning scheme to use for labeling box rows
    //     columnLabelingScheme: function returning scheme to use for labeling box columns
    //   },
    //   occupants: box occupants array
    //   occupantName: function returning name or caption to use for displaying cell occupant 
    //   occupantSref: function returning state to use for displaying cell occupant details
    //   allowClicks: boolean specifying whether to allow transition on clicking box occupants
    //   onAddEvent: function specifying action to perform when an empty cell is clicked
    // }
    //
    function drawLayout(element, opts) {
      var matrix = getMatrix(opts);

      var width = element.width();
      var noOfColumns = opts.box.numberOfColumns();
      var cellWidth = Math.floor(width / 10);
      if (cellWidth * noOfColumns > width) {
        cellWidth = width / noOfColumns;
        if (cellWidth < 85) {
          cellWidth = 85;
        }
      }

      var table = $("<table class='os-container-map' ng-click='onClick($event)'/>");
      for (var i = 0; i < matrix.length; ++i) {
        var tr = $("<tr/>");

        for (var j = 0; j < matrix[i].length; ++j) {
          var td = $("<td/>")
            .css("min-width", cellWidth)
            .append(getCell(matrix[i][j], opts));

          if (matrix[i][j].occupied) {
            td.addClass(!!matrix[i][j].occupied.id ? 'slot-occupied' : 'slot-assigned');

            if (!!matrix[i][j].occupied.oldOccupant) {
              td.addClass('slot-vacated');
            }
          }

          tr.append(td);
        }

        table.append(tr);
      }

      element.append(table);
    }

    function getMatrix(opts) {
      var matrix = new Array(opts.box.numberOfRows());
      for (var i = 0; i < opts.box.numberOfRows(); ++i) {
        matrix[i] = new Array(opts.box.numberOfColumns());

        for (var j = 0; j < opts.box.numberOfColumns(); ++j) {
          matrix[i][j] = {row: i + 1, column: j + 1};
        }
      }

      angular.forEach(opts.occupants, function(occupant) {
        matrix[opts.box.row(occupant) - 1][opts.box.column(occupant) - 1].occupied = occupant;
      });

      return matrix;
    };


    function getCell(cell, opts) {
      return $("<div class='slot'/>")
        .append("<div class='slot-dummy'></div>")
        .append(getCellEl(cell, opts));
    };

    function getCellEl(cell, opts) {
      var row = NumberConverterUtil.fromNumber(opts.box.rowLabelingScheme(), cell.row);
      var column = NumberConverterUtil.fromNumber(opts.box.columnLabelingScheme(), cell.column);

      var el = $("<div class='slot-element'/>")
        .append($("<div class='slot-pos'/>").append(formatPos(row, column)));

      if (cell.occupied) {
        var cellDesc = $("<a class='slot-desc'/>")
          .attr('title', opts.occupantName(cell.occupied))
          .append(opts.occupantName(cell.occupied));

        if (opts.allowClicks) {
          cellDesc.attr('ui-sref', opts.occupantSref(cell.occupied));
        }

        el.append(cellDesc);
      } else if (opts.allowClicks) {
        el.append(getAddMarker())
          .addClass("os-pointer-cursor")
          .attr({'data-pos-x': column, 'data-pos-y': row})
          .attr('title', 'Click to add container');
      }

      return el;
    }

    function formatPos(row, column) {
      return "(" + row + ", " + column + ")";
    };

    function getAddMarker() {
      return $("<div class='slot_add'/>")
        .append("<span class='fa fa-plus'></span>");
    };

    return {
      drawLayout: drawLayout
    }
  })
  .directive('osBoxLayout', function(BoxLayoutUtil, $compile) {
    return {
      restrict: 'EA',

      replace: true,

      scope: {
        opts: '='
      },

      link: function(scope, element, attrs) {
        scope.onClick = function($event) {
          var target = angular.element($event.originalEvent.target);
          while (!target.hasClass('slot-element') && !target.is("table")) {
            target = target.parent();
          }

          if (target.attr('data-pos-x') && target.attr('data-pos-y')) {
            opts.onAddEvent(target.attr('data-pos-y'), target.attr('data-pos-x'));
          }
        };

        scope.$watch('opts.occupants', function() {
          element.children().remove();
          BoxLayoutUtil.drawLayout(element, scope.opts);
          $compile(element)(scope);
        });
      },

      template: '<div class="os-container-map-wrapper"></div>'
    };
  });
