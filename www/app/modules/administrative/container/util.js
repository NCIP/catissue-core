
angular.module('os.administrative.container.util', [])
  .factory('ContainerUtil', function(NumberConverterUtil, Util) {

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

    return {
      fromOrdinal: NumberConverterUtil.fromNumber,

      assignPositions: function(container, occupancyMap, inputLabels, vacateOccupants) {
        var newMap = angular.copy(occupancyMap);
        var mapIdx = 0, labelIdx = 0;
        var labels = Util.splitStr(inputLabels, /,|\t|\n/, true);

        var done = false;
        var noFreeLocs = false;
        for (var y = 1; y <= container.noOfRows; ++y) {
          for (var x = 1; x <= container.noOfColumns; ++x) {
            if (labelIdx >= labels.length) {
              //
              // we are done with iterating through all input labels
              //
              done = true;
              break;
            }

            var existing = undefined;
            if (mapIdx < newMap.length && newMap[mapIdx].posOneOrdinal == x && newMap[mapIdx].posTwoOrdinal == y) {
              //
              // current map location is occupied
              //
              if (!vacateOccupants || newMap[mapIdx].occuypingEntity != 'specimen') {
                //
                // When asked not to vacate existing occupants or present occupant
                // is not specimen, then examine next container slot
                //
                mapIdx++;
                continue;
              }

              existing = newMap[mapIdx];
              newMap.splice(mapIdx, 1);
            }
 
            var label = labels[labelIdx++];
            if ((!label || label.trim().length == 0) && (!vacateOccupants || !existing)) {
              //
              // Label is empty. Either asked not to vacate existing occupants or 
              // present slot is empty
              //
              continue;
            }

            var newPos = undefined;
            if (!!existing && existing.occupyingEntityName.toLowerCase() == label.toLowerCase()) {
              newPos = existing;
            } else {
              newPos = createSpmnPos(container, label, x, y, existing);
            }

            newMap.splice(mapIdx, 0, newPos);
            mapIdx++;
          }

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

        return {map: newMap, noFreeLocs: noFreeLocs};
      }
    };
  });
