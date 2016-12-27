
angular.module('os.administrative.container.util', ['os.common.box'])
  .factory('ContainerUtil', function(BoxLayoutUtil, NumberConverterUtil) {

    function createSpmnPos(container, label, x, y, oldOccupant) {
      return {
        occuypingEntity: 'specimen', 
        occupyingEntityName: label,
        posOne: NumberConverterUtil.fromNumber(container.columnLabelingScheme, x),
        posTwo: NumberConverterUtil.fromNumber(container.rowLabelingScheme, y),
        posOneOrdinal: x,
        posTwoOrdinal: y,
        oldOccupant: oldOccupant
      };
    }

    function getOccupantDisplayName(container, occupant) {
      if (occupant.occuypingEntity == 'specimen') {
        if (container.cellDisplayProp == 'SPECIMEN_PPID') {
          return occupant.occupantProps.ppid;
        } else if (container.cellDisplayProp == 'SPECIMEN_BARCODE' && !!occupant.occupantProps.barcode) {
          return occupant.occupantProps.barcode;
        }
      }

      return occupant.occupyingEntityName;
    }

    function getOpts(container, allowClicks, showAddMarker, useBarcode) {
      return {
        box: {
          instance             : container,
          row                  : function(occupant) { return occupant.posTwoOrdinal; },
          column               : function(occupant) { return occupant.posOneOrdinal; },
          numberOfRows         : function() { return container.noOfRows; },
          numberOfColumns      : function() { return container.noOfColumns; },
          positionLabelingMode : function() { return container.positionLabelingMode; },
          rowLabelingScheme    : function() { return container.rowLabelingScheme; },
          columnLabelingScheme : function() { return container.columnLabelingScheme; },
          occupantClick        : function() { /* dummy method to make box allow cell clicks */ }
        },

        occupants: [],
        occupantName: function(occupant) {
          if (!!useBarcode && occupant.occuypingEntity == 'specimen') {
            return occupant.occupantProps.barcode || '';
          }

          return occupant.occupyingEntityName;
        },
        occupantDisplayHtml: function(occupant) {
          var displayName = undefined;
          if (occupant.occuypingEntity == 'specimen' && !!occupant.occupantProps) {
            displayName = getOccupantDisplayName(container, occupant);
           } else {
            displayName = occupant.occupyingEntityName;
          }

          return angular.element('<span class="slot-desc"/>')
            .attr('title', displayName)
            .append(displayName);
        },
        allowClicks: allowClicks,
        isVacatable: function(occupant) {
          return occupant.occuypingEntity == 'specimen';
        },
        createCell: function(label, x, y, existing) {
          return createSpmnPos(container, label, x, y, existing);
        },
        onAddEvent: showAddMarker ? function() {} : undefined
      };
    }

    return {
      fromOrdinal: NumberConverterUtil.fromNumber,

      toNumber: NumberConverterUtil.toNumber,

      getOpts: getOpts,

      assignPositions: function(container, occupancyMap, inputLabels, userOpts) {
        userOpts = userOpts || {};

        var opts = getOpts(container, false, false, userOpts.useBarcode);
        opts.occupants = occupancyMap;

        var result = BoxLayoutUtil.assignCells(opts, inputLabels, userOpts.vacateOccupants);
        return {map: result.occupants, noFreeLocs: result.noFreeLocs};
      }
    };
  });
