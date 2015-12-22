
angular.module('os.biospecimen.specimenlist.assignto',[])
  .directive('osAssignToSpmnList', function(SpecimenList, $rootScope) {

    function loadAllSpecimenList(scope) {
      SpecimenList.query().then(
        function(lists) {
          scope.specimenLists = lists;
        }
      );
    }

    return {
      restrict: 'E',
      scope: {
        onAddToList: '&'
      },

      templateUrl: 'modules/biospecimen/specimen-list/assign-to-spmn-list.html',

      link: function(scope, element, attrs) {
        loadAllSpecimenList(scope);

        scope.addToList = function(list) {
          scope.onAddToList({list: list});
        }

        scope.addToDefaultList = function() {
          scope.onAddToList({list: new SpecimenList({id: 0})});
        }
      }
    }
  });
