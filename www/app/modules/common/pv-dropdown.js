
angular.module('openspecimen')
  .directive('osPvs', function(PvManager) {
    function linker(scope, element, attrs) {
      scope.pvs = [];
      scope.reload = true;
      scope.searchPvs = function(searchTerm) {
        if (scope.reload) {
          loadPvs(scope, searchTerm, attrs);
        }
      };
    }

    function loadPvs (scope, searchTerm, attrs) {
      var q = undefined;

      if (attrs.parentVal) {
        scope.$watch(attrs.parentVal, function(newVal) {
          if (!newVal) {
            return;
          }

          PvManager.loadPvsByParent(attrs.attribute, newVal).then(
            function(pvs) {
              scope.pvs = pvs;
              scope.reload = false;
            }
          );
        });
      } else {
        PvManager.loadPvs(attrs.attribute, searchTerm, undefined, attrs.showOnlyLeafValues).then(
          function(pvs) {
            scope.pvs = pvs;
            if (searchTerm == '' && pvs.length < 100) {
              scope.reload = false;
            }
          }
        );
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
