angular.module('openspecimen')
  .directive('osPivotTable', function() {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        scope.$watch(attrs.osPivotTable, function(newOpts) {
          element.children().remove();

          if (newOpts) {
            element.append(new PivotTable(newOpts).render());
          }
        }, true);
      }
    };
  });

var PivotTable = (function($) {
  return function(opts) {
    this.opts = opts;

    this.render = function() {
      var el = $("<div/>").prop("class", "os-pivot-table");

      if (opts.title) {
        el.append(getTitleEl(opts.title));
      }

      el.append(getTableEl());
      return el;
    };

    var getTitleEl = function(title) {
      return $("<div/>").prop("class", "title")
        .css("width", opts.width || "100%")
        .append(title);
    };

    var getTableEl = function() {
      var containerEl = $("<div/>");
      if (opts.height) {
        containerEl.css("height", opts.height);
      }

      containerEl.css("width", opts.width || "100%")
        .css("overflow-x", "auto");

      var tableEl = $("<table/>")
        .append(getTableColGrp())
        .append(getTableHead())
        .append(getTableBody());

      makeTableResizable(tableEl);
      return containerEl.append(tableEl);
    };

    var getTableColGrp = function() {
      var colGrpEl = $("<colgroup/>");
      for (var i = 0; i < opts.colHeaders.length; ++i) {
        colGrpEl.append($("<col/>").css("width", "100px"));
      }

      return colGrpEl;
    };

    var getTableHead = function() {
      var tr = $("<tr/>");
      for (var i = 0; i < opts.colHeaders.length; ++i) {
        var colDiv = $("<div/>").prop("class", "ellipsis")
          .append(opts.colHeaders[i]);
        tr.append($("<th/>").append(colDiv)); 
      }

      return $("<thead/>").append(tr);
    };

    var getTableBody = function() {
      var tbody = $("<tbody/>");

      var numGrpCols = opts.numGrpCols;
      if (opts.numValueCols > 1) {
        numGrpCols++;
      }
          
      var result = preProcessRecords(numGrpCols, opts.data);

      if (result.grandTotal) {
        var trs = getGrandTotalTrs(numGrpCols, result.grandTotal);
        appendEls(tbody, trs);
      }

      if (result.subTotals && result.subTotals.length > 0) {
        var trs = getSubTotalTrs(opts.colHeaders, numGrpCols, result.subTotals);
        appendEls(tbody, trs);
      }

      var dataTrs = getDataTrs(numGrpCols, result.data);
      appendEls(tbody, dataTrs);
      return tbody;
    };

    var getGrandTotalTrs = function(numGrpCols, grandTotal) {
      var th = $("<th/>")
        .prop("class", "col-key")
        .prop("colspan", opts.numValueCols > 1 ? numGrpCols - 1 : numGrpCols)
        .prop("rowspan", opts.numRollupCols)
        .append("Grand Total");

      var startIdx = opts.numValueCols > 1 ? numGrpCols - 1 : numGrpCols;
      var addColKey = opts.numValueCols > 1;
      var trs = [];
      for (var i = 0; i < grandTotal.length; ++i) {
        var tr = $("<tr/>");
        if (i == 0) {
          tr.append(th);
        }

        for (var j = startIdx; j < grandTotal[i].length; ++j) {
          if (addColKey && j == startIdx) {
            tr.append($("<th/>").addClass("col-key").append(grandTotal[i][j]));
          } else {
            tr.append($("<td/>").append(grandTotal[i][j]));
          }
        }

        trs.push(tr);
      }

      return trs;
    };

    var getSubTotalTrs = function(colHeaders, numGrpCols, subTotals) {
      var trs = [];
      var addColKey = opts.numValueCols > 1;
      numGrpCols = opts.numValueCols > 1 ? numGrpCols - 1 : numGrpCols;

      var lastColIdx = undefined;
      var lastColVal = undefined;
      for (var i = 0; i < subTotals.length; ++i) {
        var type = subTotals[i].type;
        var subTotal = subTotals[i].record;

        var columnIdx = type.colIdx;
        var tr = $("<tr/>");
        if (columnIdx != lastColIdx && numGrpCols != 1) {
          tr.prop("class", "row-separator");
        }

        for (var j = 0; j < numGrpCols; ++j) {
          var th = $("<th/>").prop("class", "col-key");
          if ((columnIdx != lastColIdx || lastColVal != type.value) && j == columnIdx) { 
            th.prop("rowspan", opts.numRollupCols).append(type.value);
            tr.append(th);
          } else if (i % opts.numRollupCols == 0) {
            th.prop("rowspan", opts.numRollupCols).append("All");
            tr.append(th);
          }
        }

        for (var j = numGrpCols; j < subTotal.length; ++j) {
          if (addColKey && j == numGrpCols) {
            tr.append($("<th/>").addClass("col-key").append(subTotal[j]));
          } else {
            tr.append($("<td/>").append(subTotal[j]));
          }
        }

        lastColIdx = columnIdx;
        lastColVal = type.value;
        trs.push(tr);
      }

      return trs;
    };

    var getDataTrs = function(numGrpCols, data) {
      var trs = [];
      getDataTrs0(numGrpCols, data, trs, []);
      return trs;
    };

    var getDataTrs0 = function(numGrpCols, records, trs, tds) {
      for (var key in records) {
        if (key == "rowspan" || key == "data") {
          continue;
        }

        var th = $("<th/>").prop("class", "col-key").append(key);
        if (records[key].rowspan) {
          th.prop("rowspan", records[key].rowspan);
        }
        tds.push(th);

        if (!records[key].data) {
          getDataTrs0(numGrpCols, records[key], trs, tds);
        } else {
          var numCols = records[key].data.length;
          for (var i = numGrpCols; i < numCols; ++i) {
            tds.push($("<td/>").append(records[key].data[i]));
          }

          var tr = $("<tr/>");
          if (tds.length == numCols && numGrpCols != 1) {
            tr.prop("class", "row-separator");
          }

          trs.push(appendEls(tr, tds));
          tds.length = 0;
        }     
      }
    };

    var makeTableResizable = function(table) {
      table.find("thead th").resizable({
        handles: "e", 
 
        start: function(event, ui) {
          var colIndex = ui.helper.index() + 1;
          colElement = table.find("colgroup > col:nth-child(" + colIndex + ")");
 
          colWidth = parseInt(colElement.get(0).style.width, 10);
          originalSize = ui.size.width;
        },
 
        resize: function(event, ui) {
          var resizeDelta = ui.size.width - originalSize;
          var newColWidth = colWidth + resizeDelta;
          colElement.width(newColWidth);
 
          $(this).css("height", "auto");
        }
      });
    };

    var appendEls = function(parentEl, els) {
      for (var i = 0; i < els.length; ++i) {
        parentEl.append(els[i]);
      }

      return parentEl;
    };

    var getRecordType = function(numGrpCols, record) {
      numGrpCols = opts.numValueCols > 1 ? numGrpCols - 1 : numGrpCols;

      var count = 0, idx = -1, val = undefined;
      for (var i = 0; i < numGrpCols; ++i) {
        if (record[i] == '\u0000\u0000\u0000\u0000\u0000') {
          count++;
        } else {
          idx = i;
          val = record[i];
        }
      }

      if (count == 0) {
        return {type: 'data'};
      } else if (count == numGrpCols - 1 && numGrpCols != 1) {
        return {type: 'subTotal', colIdx: idx, value: val};
      } else if (count == numGrpCols) {
        return {type: 'grandTotal'};
      }
    };

    var preProcessRecords = function(numGrpCols, records) {
      var grandTotal = [], subTotals = [], data = {};

      for (var i = 0; i < records.length; ++i) {
        var ret = getRecordType(numGrpCols, records[i]);
        if (ret.type == 'grandTotal') {
          grandTotal.push(records[i]);
        } else if (ret.type == 'subTotal') {
          subTotals.push({type: ret, record: records[i]});
        } else {
          var obj = undefined;
          for (var k = 0; k < numGrpCols; ++k) {
            if (k == 0) {
              obj = data[records[i][k]];
              if (!obj) {
                obj = data[records[i][k]] = {rowspan: 0};
              }
            } else {
              if (!obj[records[i][k]]) {
                obj[records[i][k]] = {rowspan: 0};
              }

              obj = obj[records[i][k]];
            }

            obj.rowspan++;
          }

          obj.data = records[i];
        }
      }

      return {grandTotal: grandTotal, subTotals: subTotals, data: data}; 
    }; 
  };
})(jQuery);


