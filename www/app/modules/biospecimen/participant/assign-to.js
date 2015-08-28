
angular.module('os.biospecimen.participant.assignto',[])
  .directive('osAssignToList', function($translate) {

    function assignTextToLabels(scope, searchListText, createListText) {
      searchListText = searchListText || 'common.search_list';
      createListText = createListText || 'common.create_list';
      scope.searchListText = $translate.instant(searchListText);
      scope.createListText = $translate.instant(createListText);
    }

    return {
      restrict: 'E',
      scope: {
        lists: '=',
        onAddToList: '&'
      },

      templateUrl: 'modules/biospecimen/participant/assign-to-list.html',

      link: function(scope, element, attrs) {
        assignTextToLabels(scope, attrs.searchListText, attrs.createListText);

        scope.addToList= function(list){
          scope.onAddToList({list: list});
        }
      }
    }
  });
