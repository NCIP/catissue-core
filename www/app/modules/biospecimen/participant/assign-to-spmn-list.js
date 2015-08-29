
angular.module('os.biospecimen.participant.assignto',[])
  .directive('assignToSpmnList', function(SpecimenList) {

    function loadAllSpecimenList(scope) {
      SpecimenList.query().then(
        function(lists) {
          if (scope.currentUser.admin) {
            scope.specimenLists = lists;
          } else {
            scope.specimenLists = [];
            angular.forEach(lists, function(list) {
              if (list.owner.id == scope.currentUser.id) {
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
        onAddToList: '&',
        currentUser: '='
      },

      templateUrl: 'modules/biospecimen/participant/assign-to-spmn-list.html',

      link: function(scope, element, attrs) {
        loadAllSpecimenList(scope);

        scope.addToList= function(list){
          scope.onAddToList({list: list});
        }
      }
    }
  });
