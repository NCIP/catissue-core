var openspecimen = openspecimen || {}
openspecimen.ui = openspecimen.ui || {};
openspecimen.ui.container = openspecimen.ui.container || {};

openspecimen.ui.container.ContainerPositionSelector = function(opts) {
  // opts: {parentEl: , onSelect:, inputPos:, container:, containerUtil:}

  var Utility = opts.containerUtil;
  var container = opts.container;
  var selectedPos = $.extend({}, opts.inputPos);

  function getGridHead() {
    var thead = $("<thead/>");
    var tr = $("<tr/>");
    tr.append($("<th/>").addClass("os-container-pos").append("&nbsp;"));
    for (var i = 0; i < +container.noOfColumns; ++i) {
      tr.append(
        $("<th/>")
          .addClass("os-container-pos")
          .append(Utility.fromOrdinal(container.columnLabelingScheme, i + 1))
      );
    }

    thead.append(tr);
    return thead;
  }

  function getOccupiedPositionsMap(occupied) {
    var occupiedPosMap = {};
    for (var i = 0; i < occupied.length; ++i) {
      occupiedPosMap[occupied[i]] = true;
    }

    return occupiedPosMap;
  }

  function getAssignedPositionsMap(container, assigned) {
    var assignedMap = {};
    var positions = assigned[container.name];
    if (!positions) {
      return assignedMap;
    }

    for (var i = 0; i < positions.length; ++i) {
      if (!positions[i].positionX || !positions[i].positionY) {
        continue;
      }

      var row = Utility.toNumber(container.rowLabelingScheme, positions[i].positionY);
      var column = Utility.toNumber(container.columnLabelingScheme, positions[i].positionX);
      assignedMap[(row - 1) * container.noOfColumns + column] = true;
    }

    return assignedMap;
  }

  function getGridBody() {
    var occupiedPosMap = getOccupiedPositionsMap(container.occupiedPositions);
    var assignedPosMap = getAssignedPositionsMap(container, opts.assignedPos || {});

    var tbody = $("<tbody/>");
    for (var i = 0; i < +container.noOfRows; ++i) {
      var posY = Utility.fromOrdinal(container.rowLabelingScheme, i + 1);
      var tr = $("<tr/>")
        .append($("<th/>").addClass("os-container-pos").append(posY));

      for (var j = 0; j < +container.noOfColumns; ++j) {
        var position = i * +container.noOfColumns + (j + 1);
        var posX     = Utility.fromOrdinal(container.columnLabelingScheme, j + 1);

        var tooltip = position;
        if (container.positionLabelingMode == 'TWO_D') {
          tooltip = '(' + posY + ', ' + posX + ')';
        }

        var td = $("<td/>")
          .addClass("os-container-pos")
          .attr({'data-pos-x': posX, 'data-pos-y': posY, 'data-pos': position, 'title': tooltip});

        var pos = i * container.noOfColumns + j + 1;
        if (occupiedPosMap[pos]) {
          td.addClass("occupied no-click");
        } else if (assignedPosMap[pos]) {
          td.addClass("assigned no-click");
        }

        td.append($("<span/>").addClass("os-circle"));

        if (selectedPos && selectedPos.posX == posX && selectedPos.posY == posY) {
          td.addClass("selected");
        }

        tr.append(td);
      }

      tbody.append(tr);
    }

    return tbody;
  }

  function listenForSelections(table, selectedPos) {
    table.find("td.os-container-pos:not(.no-click)").on("click", function(event) {
      var posEl = $(event.currentTarget);
      
      if (posEl.hasClass("selected")) {
        posEl.removeClass("selected");
        selectedPos = {};    
      } else {
        table.find("td.os-container-pos.selected").removeClass("selected");
        posEl.addClass("selected");
        selectedPos.posX = posEl.attr('data-pos-x');
        selectedPos.posY = posEl.attr('data-pos-y');
        selectedPos.pos  = posEl.attr('data-pos');
      }

      if (typeof(opts.onSelect) == 'function') {
        opts.onSelect(selectedPos);
      }
    });
  }

  this.render = function() {
    var table = $("<table/>").addClass("table table-bordered")
      .append(getGridHead())
      .append(getGridBody()); 

    opts.parentEl.append(table);
    listenForSelections(table, selectedPos); 
    return this;
  };

  this.destroy = function() {
    opts.parentEl.children().remove();
    return this;
  };

  this.getSelectedPos = function() {
    return selectedPos;
  };
};
