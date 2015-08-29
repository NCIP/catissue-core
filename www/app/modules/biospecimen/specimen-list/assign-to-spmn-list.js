
angular.module('os.biospecimen.specimenlist.assignto',[])
  .directive('osAssignToSpmnList', function(SpecimenList, $rootScope) {

    function loadAllSpecimenList(scope) {
      SpecimenList.query().then(
        function(lists) {
          if ($rootScope.currentUser.admin) {
            scope.specimenLists = lists;
          } else {
            scope.specimenLists = [];
            angular.forEach(lists, function(list) {
              if (list.owner.id == $rootScope.currentUser.id) {
                scope.specimenLists.push(list);
              }
            })
          }
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

        scope.addToList= function(list){
          scope.onAddToList({list: list});
        }
      }
    }
  });
