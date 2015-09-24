
angular.module('os.administrative.container.util', [])
  .factory('ContainerUtil', function(Util) {

    function toAlphabet(num, aCharCode) {
      var result = "";
      while (num > 0) {
        result = String.fromCharCode((num - 1) % 26 + aCharCode) + result;
        num = Math.floor((num - 1) / 26);
      }

      return result;
    };

    var romanLookupTab = {
      M: 1000, CM: 900,
      D:  500, CD: 400,
      C:  100, XC:  90,
      L:   50, XL:  40,
      X:   10, IX:   9,
      V:    5, IV:   4,
      I:    1
    };

    function toRoman(num, upper) {
      var result = '';
      for (var i in romanLookupTab) {
        while (num >= romanLookupTab[i]) {
          result += i;
          num -= romanLookupTab[i];
        }
      }
 
      return upper ? result : result.toLowerCase();
    };

    function toNum(num) {
      return num;
    };

    function toUpperAlphabet(num) {
      return toAlphabet(num, 'A'.charCodeAt(0));
    };

    function toLowerAlphabet(num) {
      return toAlphabet(num, 'a'.charCodeAt(0));
    };

    function toUpperRoman(num) {
      return toRoman(num, true);
    };

    function toLowerRoman(num) {
      return toRoman(num, false);
    }

    var convertersMap = {
      'Numbers'             : toNum,
      'Alphabets Upper Case': toUpperAlphabet,
      'Alphabets Lower Case': toLowerAlphabet,
      'Roman Upper Case'    : toUpperRoman,
      'Roman Lower Case'    : toLowerRoman
    };

    function fromOrdinal(scheme, ordinal) {
      return convertersMap[scheme](ordinal);
    }

    function createSpmnPos(container, label, x, y, oldOccupant) {
      return {
        occuypingEntity: 'specimen', 
        occupyingEntityName: label,
        posOne: fromOrdinal(container.columnLabelingScheme, x),
        posTwo: fromOrdinal(container.rowLabelingScheme, y),
        posOneOrdinal: x,
        posTwoOrdinal: y,
        oldOccupant: oldOccupant
      };
    }

    return {
      fromOrdinal: fromOrdinal, 

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
