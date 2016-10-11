
angular.module('openspecimen')
  .directive('osPvs', function($q, PvManager) {
    function linker(scope, element, attrs, formCtrl) {
      scope.pvs = [];
      scope.reload = true;
      scope.isSelectedValFetched = false;

      if (attrs.parentVal) {
        scope.$watch(attrs.parentVal,
          function(newVal) {
            if (newVal == undefined) {
              return;
            }

            scope.reload = true;
            loadPvs(formCtrl, scope, null, attrs, newVal);
          }
        );
      }

      scope.searchPvs = function(searchTerm) {
        if (scope.reload) {
          loadPvs(formCtrl, scope, searchTerm, attrs);
        }
      };
    }

    function loadPvs(formCtrl, scope, searchTerm, attrs, parentVal) {
      var q = undefined;
      if (attrs.options) {
        q = $q.defer();
        q.resolve(getLocalPvs(scope, attrs));
        q = q.promise;
      } else if (attrs.parentVal) {
        var prop = 'P:' + attrs.attribute + ':' + parentVal;
        q = getCachedValues(formCtrl, 'pvs', prop, function() { return _loadPvsByParent(attrs, parentVal); });
      } else {
        if (!searchTerm) {
          var prop = attrs.attribute + ":" + attrs.showOnlyLeafValues;
          q = getCachedValues(formCtrl, 'pvs', prop, function() { return _loadPvs(scope, attrs, searchTerm); });
        } else {
          q = _loadPvs(scope, attrs, searchTerm);
        }
      }

      q.then(
        function(pvs) {
          setPvs(scope, searchTerm, attrs, pvs);
        }
      );
    }

    function getLocalPvs(scope, attrs) {
      var list = scope.$eval(attrs.options);
      if (!list) {
        list = [];
      }

      return list.map(
        function(v) {
          return addDisplayValue(typeof v == 'object' ? v : {value: v});
        }
      );
    }

    function getCachedValues(formCtrl, group, prop, getter) {
      if (!formCtrl) {
        return getter();
      }

      return formCtrl.getCachedValues(group, prop, getter);
    }

    function _loadPvs(scope, attrs, searchTerm) {
      return PvManager.loadPvs(attrs.attribute, searchTerm, addDisplayValue, attrs.showOnlyLeafValues);
    }

    function _loadPvsByParent(attrs, parentVal) {
      return PvManager.loadPvsByParent(attrs.attribute, parentVal, false, addDisplayValue);
    }

    function setPvs(scope, searchTerm, attrs, pvs) {
      var selectedVal = scope.$eval(attrs.ngModel);
      if (!scope.isSelectedValFetched && !!selectedVal && selectedVal == searchTerm) {
        //
        // case when search was initiated to obtain selected value
        //
        scope.pvs = scope.pvs.concat(pvs);
        scope.isSelectedValFetched = true;
        return;
      }

      scope.pvs = pvs;
      if (attrs.unique == 'true') {
        scope.pvs = pvs.filter(function(el, pos) { return pvs.indexOf(el) == pos; });
      }

      if (!searchTerm && pvs.length < 100) {
        scope.reload = false;
      } 

      if (!searchTerm && !scope.isSelectedValFetched) {
        // init case
        checkAndFetchSelectedVal(scope, selectedVal, pvs);
      }
    }

    //
    // PV dropdown shows only 100 PVs to begin with.
    // The selected PV is not displayed if it is not present in initial list of 100 PVs.
    // This function decides whether to initiate a search to obtain selected PV from backend
    //
    function checkAndFetchSelectedVal(scope, selectedVal, pvs) {
      scope.isSelectedValFetched = !selectedVal || pvs.length < 100;
      if (!scope.isSelectedValFetched) {
        for (var i = 0; i < pvs.length; i++) {
          if (pvs[i].value == selectedVal) {
            scope.isSelectedValFetched = true;
            return;
          }
        }

        //
        // selected value is not in list;
        // initiate search to retrieve it from backend
        //
        scope.searchPvs(selectedVal);
      }
    }

    function addDisplayValue(input) {
      var displayValue = input.value + ( input.conceptCode ? ' (' + input.conceptCode + ') ' : '' );
      return angular.extend(input, {displayValue: displayValue});
    }
    
    return {
      restrict: 'E',
      require: '?^osFormValidator',
      scope: true,
      replace: true,
      link : linker,
      template: '<os-select refresh="searchPvs($select.search)" list="pvs" ' +
                '  select-prop="value" display-prop="displayValue"> ' +
                '</os-select>'
    };
  });
