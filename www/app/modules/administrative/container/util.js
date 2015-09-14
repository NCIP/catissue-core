
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

    function createSpmnPos(container, label, x, y) {
      return {
        occuypingEntity: 'specimen', 
        occupyingEntityName: label,
        posOne: fromOrdinal(container.columnLabelingScheme, x),
        posTwo: fromOrdinal(container.rowLabelingScheme, y),
        posOneOrdinal: x,
        posTwoOrdinal: y
      };
    }

    function removeSpecimenIfExist(map, newPos) {
       for (var i = 0; i < map.length; ++i) {
         var pos = map[i];
         if(pos.posOneOrdinal == newPos.posOneOrdinal &&
           pos.posTwoOrdinal == newPos.posTwoOrdinal) {
           map.splice(i, 1);
         }
       }
    }

    return {
      fromOrdinal: fromOrdinal, 

      assignPositions: function(container, occupancyMap, inputLabels, overwrite) {
        var newMap = angular.copy(occupancyMap);
        var mapIdx = 0, labelIdx = 0;
        var labels = Util.splitStr(inputLabels, /,|\t|\n/, true);

        var done = false;
        var noFreeLocs = false;
        for (var y = 1; y <= container.noOfRows; ++y) {
          for (var x = 1; x <= container.noOfColumns; ++x) {
            if (labelIdx >= labels.length) {
              done = true;
              break;
            }

            if (mapIdx < newMap.length &&
              newMap[mapIdx].posOneOrdinal == x &&
              newMap[mapIdx].posTwoOrdinal == y &&
              !overwrite) {
              mapIdx++;
              continue;
            }
 
            var label = labels[labelIdx++];
            if (!label || label.trim().length == 0) {
              continue;
            }

            var newPos = createSpmnPos(container, label, x, y);
            if (overwrite) {
              removeSpecimenIfExist(newMap, newPos);
              newMap.splice(0, 0, newPos);
            } else {
              newMap.splice(mapIdx, 0, newPos);
            }

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
