
angular.module('os.common.box', [])
  .factory('BoxLayoutUtil', function(NumberConverterUtil, Util) {
    //
    // opts: {
    //   box: {
    //     instance: box or array or any other object
    //     row: function to convert row number to corresponding labeling scheme number
    //     column: function to convert column number to corresponding labeling scheme number
    //     numberOfRows: function returning number of box rows
    //     numberOfColumns: function returning number of box columns
    //     positionLabelingMode: function returning how slots should be labeled. LINEAR or TWO_D
    //     rowLabelingScheme: function returning scheme to use for labeling box rows
    //     columnLabelingScheme: function returning scheme to use for labeling box columns
    //   },
    //   occupants: box occupants array
    //   occupantName: function returning name or caption to use for displaying cell occupant 
    //   occupantSref: function returning state to use for displaying cell occupant details
    //   allowClicks: boolean specifying whether to allow transition on clicking box occupants
    //   onAddEvent: function specifying action to perform when an empty cell is clicked
    //   isVacatable: function specifying whether existing cell occupant is vacatable or not
    //   createCell: function returning object to represent cell to newly assigned entity
    // }
    //
    function drawLayout(element, opts) {
      var matrix = getMatrix(opts);

      var width = element.width();
      var noOfColumns = opts.box.numberOfColumns();
      var cellWidth = Math.floor(width / 10);
      if (width > cellWidth * noOfColumns) {
        cellWidth = width / noOfColumns;
      }

      if (cellWidth < 85) {
        cellWidth = 85;
      } else if (cellWidth > 125) {
        cellWidth = 125;
      }

      var table = $("<table class='os-container-map' ng-click='onClick($event)'/>");
      var disableSelects = false;
      for (var i = 0; i < matrix.length; ++i) {
        var tr = $("<tr/>");

        for (var j = 0; j < matrix[i].length; ++j) {
          var td = $("<td/>")
            .css("min-width", cellWidth)
            .append(getCell(matrix[i][j], opts));

          if (matrix[i][j].occupied) {
            if (!!matrix[i][j].occupied.id) {
              td.addClass('slot-occupied');
            } else {
              disableSelects = true;
              td.addClass('slot-assigned');
            }

            if (!!matrix[i][j].occupied.oldOccupant) {
              disableSelects = true;
              td.addClass('slot-vacated');
            }
          }

          tr.append(td);
        }

        table.append(tr);
      }

      if (disableSelects) {
        table.addClass('disable-slot-selectors');
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
      var pos = (cell.row - 1) * opts.box.numberOfColumns() + cell.column;
      var row = NumberConverterUtil.fromNumber(opts.box.rowLabelingScheme(), cell.row);
      var column = NumberConverterUtil.fromNumber(opts.box.columnLabelingScheme(), cell.column);

      var el = $("<div class='slot-element'/>");

      var occupant = cell.occupied;
      if (occupant && opts.toggleCellSelect) {
        var attrs = {'data-id': occupant.id, 'data-pos-y': row, 'data-pos-x': column, 'data-pos': pos};
        var checkbox = $("<label class='os-checkbox'/>")
          .append($("<input type='checkbox'>").attr(attrs))
          .append($("<span class='box'/>"))
          .append($("<span class='tick'/>"));
        el.append($("<div class='slot-selector'/>").append(checkbox));
      }

      el.append($("<div class='slot-pos'/>").append(formatPos(opts, row, column, pos)));

      if (occupant) {
        var cellDesc = $("<a/>").attr('title', opts.occupantName(occupant));
        if (typeof opts.occupantDisplayHtml == 'function') {
          // let the display html decide styling of cell...
          cellDesc.append(opts.occupantDisplayHtml(occupant));
        } else {
          cellDesc.addClass('slot-desc').append(opts.occupantName(occupant));
        }

        if (opts.allowClicks) {
          if (opts.box.occupantClick) {
            var params = '"' + occupant.occuypingEntity + '", ' + occupant.occupyingEntityId;
            cellDesc.attr('ng-click', 'occupantClick(' + params + ')');
          } else if (opts.occupantSref) {
            cellDesc.attr('ui-sref', opts.occupantSref(occupant));
          }
        }

        el.append(cellDesc);
      } else if (typeof opts.onAddEvent == 'function') {
        el.append(getAddMarker())
          .addClass("os-pointer-cursor")
          .attr({'data-pos-x': column, 'data-pos-y': row, 'data-pos': pos})
          .attr('title', 'Click to add container');
      }

      return el;
    }

    function formatPos(opts, row, column, pos) {
      if (opts.box.positionLabelingMode() == 'LINEAR') {
        return pos;
      } else {
        return "(" + row + ", " + column + ")";
      }
    };

    function getAddMarker() {
      return $("<div class='slot-add'/>")
        .append("<span class='fa fa-plus'></span>");
    };

    function assignCells(opts, inputLabels, vacateOccupants) {
      var occupants = angular.copy(opts.occupants);

      //
      // Below regular expression will break (A, B) x, y, z into following parts
      // match[0] = '(A, B) x, y, z' ; entire matched string
      // match[1] = '(A, B)'; starting cell in parenthesis
      // match[2] = 'A, B'; starting cell without parenthesis
      // match[3] = 'x, y, z'; labels to scan or parse
      //
      var re = /(\(([^)]+)\))?\s*([^(]+)/g;
      inputLabels = inputLabels.trim();

      var input = [], match;
      while ((match = re.exec(inputLabels)) != null) {
        input.push({startCell: match[2], labels: Util.splitStr(match[3], /,|\t|\n/, true)});
      }

      var noFreeLocs = false
      for (var i = 0; i < input.length; ++i) {
        var startRow = 1, startColumn = 1;
        var mapIdx = 0, labelIdx = 0;

        var labels = input[i].labels;
        if (!!input[i].startCell) {
          //
          // Explicit starting cell specified
          //
          var startCell = input[i].startCell.trim().split(',');
          if (startCell.length != 2) {
            alert("Invalid start position: " + input[1].startCell);
            return;
          }

          startRow    = NumberConverterUtil.toNumber(opts.box.rowLabelingScheme(),    startCell[0].trim());
          startColumn = NumberConverterUtil.toNumber(opts.box.columnLabelingScheme(), startCell[1].trim());

          //
          // fast forward map index to point to first occupant in cell (row, column)
          // such that row > startRow or row == startRow and column > startColumn
          //
          while (mapIdx < occupants.length) {
            if (!isOccupiedBefore(opts, occupants[mapIdx], startRow, startColumn)) {
              break;
            }

            ++mapIdx;
          }
        }

        var done = false;
        for (var y = startRow; y <= opts.box.numberOfRows(); ++y) {
          for (var x = startColumn; x <= opts.box.numberOfColumns(); ++x) {
            if (labelIdx >= labels.length) {
              //
              // we are done with probing/iterating through all input labels
              //
              done = true;
              break;
            }

            var existing = undefined;
            if (mapIdx < occupants.length && isOccupied(opts, occupants[mapIdx], x, y)) {
              //
              // current cell is occupied
              //
              if (!vacateOccupants || !opts.isVacatable(occupants[mapIdx])) {
                //
                // When asked not to vacate existing occupants or present occupant
                // is not vacatable, then examine next cell
                //
                mapIdx++;
                continue;
              }

              existing = occupants[mapIdx];
              occupants.splice(mapIdx, 1);
            }
 
            var label = labels[labelIdx++];
            if ((!label || label.trim().length == 0) && (!vacateOccupants || !existing)) {
              //
              // Label is empty. Either asked not to vacate existing occupants or 
              // present cell is empty
              //
              continue;
            }

            var cell = undefined;
            if (!!existing && opts.occupantName(existing).toLowerCase() == label.toLowerCase()) {
              cell = existing;
            } else {
              cell = opts.createCell(label, x, y, existing);
            }

            occupants.splice(mapIdx, 0, cell);
            mapIdx++;
          }

          //
          // start of next row
          //
          startColumn = 1;

          if (done) {
            break;
          }
        }

        while (labelIdx < labels.length) {
          if (!!labels[labelIdx] && labels[labelIdx].trim().length > 0) {
            noFreeLocs = true;
            break;
          }

          labelIdx++;
        }
      }

      return {occupants: occupants, noFreeLocs: noFreeLocs};
    }

    function isOccupied(opts, occupant, x, y) {
      return opts.box.row(occupant) == y && opts.box.column(occupant) == x;
    }

    function isOccupiedBefore(opts, occupant, y, x) {
      var row = opts.box.row(occupant);
      var col = opts.box.column(occupant);
      return (row < y) || (row == y && col < x);
    }

    return {
      drawLayout: drawLayout,

      assignCells: assignCells
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
        scope.occupantClick = function(entityType, entityId) {
          scope.opts.box.occupantClick(entityType, entityId);
        }

        scope.onClick = function($event) {
          var target = angular.element($event.originalEvent.target);

          if (target.is("input[type='checkbox']")) {
            var slotEl = target.closest('.slot-element');
            if (target[0].checked) {
              slotEl.addClass('slot-assigned');
            } else {
              slotEl.removeClass('slot-assigned');
            }

            scope.opts.toggleCellSelect(
              target.attr('data-id'),
              target.attr('data-pos-y'),
              target.attr('data-pos-x'),
              target.attr('data-pos'),
              target[0].checked);
            return;
          }

          while (!target.hasClass('slot-element') && !target.is("table")) {
            target = target.parent();
          }

          if (target.attr('data-pos-y') && target.attr('data-pos-x')) {
            scope.opts.onAddEvent(target.attr('data-pos-y'), target.attr('data-pos-x'), target.attr('data-pos'));
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
