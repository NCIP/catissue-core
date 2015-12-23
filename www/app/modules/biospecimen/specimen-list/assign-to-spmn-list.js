
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
          var defList = new SpecimenList({
            id: 0, 
            name: '$$$$user_' + $rootScope.currentUser.id, 
            owner: $rootScope.currentUser
          });

          scope.onAddToList({list: defList});
        }
      }
    }
  });
