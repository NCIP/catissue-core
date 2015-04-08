
angular.module('openspecimen')
  .factory('Util', function($rootScope, $timeout) {
    function clear(input) {
      input.splice(0, input.length);
    };

    function unshiftAll(arr, elements) {
      Array.prototype.splice.apply(arr, [0, 0].concat(elements));
    };

    function assign(arr, elements) {
      clear(arr);
      unshiftAll(arr, elements);
    };

    function filter($scope, varName, callback) {
      $scope.$watch(varName, function(newVal, oldVal) {
        if (newVal == oldVal) {
          return;
        }

        if ($scope._filterQ) {
          $timeout.cancel($scope._filterQ);
        }

        $scope._filterQ = $timeout(
          function() {
            callback(newVal);
          },
          $rootScope.global.filterWaitInterval
        );
      }, true);
    }

    function csvToArray(input, delimiter) {    
      if (!input || input.trim().length == 0) {
        return [];
      }

      input = input.trim();
      delimiter = (delimiter || ",").replace(/([[^$.|?*+(){}])/g, '\\$1');

      var quotes = "\"'";

      // Create a regular expression to parse the CSV values.
      // match[1] = Contains the delimiter if the RegExp is not at the begin
      // match[2] = quote, if any
      // match[3] = string inside quotes, if match[2] exists
      // match[4] = non-quoted strings
      var objPattern = new RegExp(
        // Delimiter or marker of new row
        "(?:(" + delimiter + ")|[\\n\\r]|^)" +
        // Quoted fields
        "(?:([" + quotes + "])((?:[^" + quotes + "]+|(?!\\2).|\\2\\2)*)\\2" + 
        // Standard fields
        "|([^" + quotes + delimiter + "\\n\\r]*))",
        "gi");

      var result = [];

      var arrMatches;
      while (arrMatches = objPattern.exec(input)) {
        var quote = arrMatches[2]
        if (quote) {
          // We found a quoted value. When we capture
          // this value, unescape any double quotes.
          var strMatchedValue = arrMatches[3].replace(new RegExp( quote + quote, "g" ), quote);
        } else {
          // We found a non-quoted value.
          var strMatchedValue = arrMatches[4];
        }

        result.push(strMatchedValue);
      }

      return result;
    }

    function getDupObjects(objs, props) {
      var dupObjs = {};
      var scannedObjs = {};
      angular.forEach(props, function(prop) {
        dupObjs[prop] = [];
        scannedObjs[prop] = [];
      });

      angular.forEach(objs, function(obj) {
        angular.forEach(props, function(prop) {
          if (!obj[prop]) {
            return;
          }

          if (scannedObjs[prop].indexOf(obj[prop]) >= 0) {
            if (dupObjs[prop].indexOf(obj[prop]) == -1) {
              dupObjs[prop].push(obj[prop]);
            }
          }

          scannedObjs[prop].push(obj[prop]);
        })
      });
 
      return dupObjs;
    }

    return {
      clear: clear,

      unshiftAll: unshiftAll,

      assign: assign,

      filter: filter,

      csvToArray: csvToArray,

      getDupObjects: getDupObjects
    };
  });
