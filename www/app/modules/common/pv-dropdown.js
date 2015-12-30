
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
      PvManager.loadPvs(attrs.attribute, searchTerm, undefined, attrs.showOnlyLeafValues).then(
        function(pvs) {
          scope.pvs = pvs;
          if (searchTerm == '' && pvs.length < 100) {
            scope.reload = false;
          }
        }
      );
    }
    
    return {
      restrict: 'E',
      scope: true,
      replace: true,
      link : linker,
      template: '<os-select refresh="searchPvs($select.search)" list="pvs"></os-select>'      
    };
  });
