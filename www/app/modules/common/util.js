
angular.module('openspecimen')
  .factory('Util', function($rootScope, $timeout, $document, $q, QueryExecutor, Alerts) {
    var isoDateRe = /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2}(?:\.\d*))(?:Z|(\+|-)([\d|:]*))?$/;
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

    function getEscapeMap(str) {
      var map = {}, insideSgl = false, insideDbl = false;
      var lastIdx = -1;

      for (var i = 0; i < str.length; ++i) {
        if (str[i] == "'" && !insideDbl) {
          if (insideSgl) {
            map[lastIdx] = i;
          } else {
            lastIdx = i;
          }

          insideSgl = !insideSgl;
        } else if (str[i] == '"' && !insideSgl) {
          if (insideDbl) {
            map[lastIdx] = i;
          } else {
            lastIdx = i;
          }

          insideDbl = !insideDbl;
        }
      }

      return map;
    }

    function getToken(token) {
      token = token.trim();
      if (token.length != 0) {
        if ((token[0] == "'" && token[token.length - 1] == "'") ||
            (token[0] == '"' && token[token.length - 1] == '"')) {
          token = token.substring(1, token.length - 1);
        }
      }

      return token;
    }

    function splitStr(str, re, returnEmpty) {
      var result = [], token = '', escUntil = undefined;
      var map = getEscapeMap(str);

      for (var i = 0; i < str.length; ++i) {
        if (escUntil == undefined) {
          escUntil = map[i];
        }

        if (i <= escUntil) {
          token += str[i];
          if (i == escUntil) {
            escUntil = undefined;
          }
        } else {
          if (re.exec(str[i]) == null) {
            token += str[i];
          } else {
            token = getToken(token);
            if (token.length > 0 || !!returnEmpty) {
              result.push(token);
            }
            token = '';
          }
        }
      }

      token = getToken(token);
      if (token.length > 0) {
        result.push(token);
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

    function hidePopovers() {
      var popovers = $document.find('div.popover');
      angular.forEach(popovers, function(popover) {
        angular.element(popover).scope().$hide();
      });
    }

    function getNumberInScientificNotation(input, minRange, fractionDigits) {
      minRange = minRange || 1000000;
      fractionDigits = fractionDigits || 6;
      
      if (angular.isNumber(input) && input > minRange) {
        input = input.toExponential(fractionDigits);
      }

      return input;
    }

    function parseDate(value) {
      if (typeof value === 'string') {
        var matches = isoDateRe.exec(value);
        if (matches) {
          return new Date(value);
        }
      }

      return value;
    }

    function downloadReport(entity, msgClass) {
      var alert = Alerts.info(msgClass + '.report_gen_initiated', {}, false);
      entity.generateReport().then(
        function(result) {
          Alerts.remove(alert);
          if (result.completed) {
            Alerts.info(msgClass + '.downloading_report');
            QueryExecutor.downloadDataFile(result.dataFile, entity.name + '.csv');
          } else if (result.dataFile) {
            Alerts.info(msgClass + '.report_will_be_emailed');
          }
        },

        function() {
          Alerts.remove(alert);
        }
      );
    }

    function booleanPromise(condition) {
      var deferred = $q.defer();
      if (condition) {
        deferred.resolve(true);
      } else {
        deferred.reject(false);
      }
      
      return deferred.promise;
    }

    function appendAll(array, elements) {
      Array.prototype.push.apply(array, elements);
    }

    function merge(src, dst, deep) {
      var h = dst.$$hashKey;

      if (!angular.isObject(src) && !angular.isFunction(src)) {
        return dst;
      }

      var keys = Object.keys(src);
      for (var i = 0; i < keys.length; i++) {
        var key = keys[i];
        var value = src[key];

        if (deep && angular.isObject(value)) {
          if (angular.isDate(value)) {
            dst[key] = new Date(value.valueOf());
          } else {
            if (!angular.isObject(dst[key])) {
              dst[key] = angular.isArray(value) ? [] : {};
            }

            merge(value, dst[key], true);
          }
        } else {
          dst[key] = value;
        }
      }

      if (h) {
        dst.$$hashKey = h;
      } else {
        delete dst.$$hashKey;
      }

      return dst;
    }

    return {
      clear: clear,

      unshiftAll: unshiftAll,

      assign: assign,

      filter: filter,

      splitStr: splitStr,

      getDupObjects: getDupObjects,

      hidePopovers: hidePopovers,

      getNumberInScientificNotation: getNumberInScientificNotation,

      parseDate: parseDate,

      downloadReport : downloadReport,

      booleanPromise: booleanPromise,

      appendAll: appendAll,

      merge: merge
    };
  });
