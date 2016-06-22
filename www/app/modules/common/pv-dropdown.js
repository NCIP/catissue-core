
angular.module('openspecimen')
  .directive('osPvs', function(PvManager) {
    function linker(scope, element, attrs) {
      scope.pvs = [];
      scope.reload = true;

      if (attrs.parentVal) {
        scope.$watch(attrs.parentVal,
          function(newVal) {
            if (newVal == undefined) {
              return;
            }

            scope.reload = true;
            loadPvs(scope, null, attrs, newVal);
          }
        );
      }

      scope.searchPvs = function(searchTerm) {
        if (scope.reload) {
          loadPvs(scope, searchTerm, attrs);
        }
      };
    }

    function loadPvs (scope, searchTerm, attrs, parentVal) {
      var q = undefined;

      if (attrs.parentVal) {
        PvManager.loadPvsByParent(attrs.attribute, parentVal).then(
          function(pvs) {
            setPvs(scope, searchTerm, attrs, pvs);
          }
        );
      } else {
        PvManager.loadPvs(attrs.attribute, searchTerm, undefined, attrs.showOnlyLeafValues).then(
          function(pvs) {
            setPvs(scope, searchTerm, attrs, pvs);
          }
        );
      }
    }

    function setPvs(scope, searchTerm, attrs, pvs) {
      scope.pvs = pvs;
      if (attrs.unique == 'true') {
        scope.pvs = pvs.filter(function(el, pos) { return pvs.indexOf(el) == pos; });
      }

      if (!searchTerm && pvs.length < 100) {
        scope.reload = false;
      }
    }
    
    return {
      restrict: 'E',
      scope: true,
      replace: true,
      link : linker,
      template: '<os-select refresh="searchPvs($select.search)" list="pvs"></os-select>'
    };
  });
