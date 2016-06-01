angular.module('os.biospecimen.specimenlist')
  .controller('SpecimenListsCtrl', function($scope, $state, lists, SpecimenList, Util) {
    
    function init() {
      $scope.ctx = {
        lists: lists,
        filterOpts: {}
      };

      Util.filter($scope, 'ctx.filterOpts', loadLists);
    }

    function loadLists(filterOpts) {
      var params = angular.extend({includeStats: true}, filterOpts);
      SpecimenList.query(params).then(
        function(lists) {
          $scope.ctx.lists = lists;
        }
      );
    }

    $scope.viewList = function(list) {
      $state.go('specimen-list', {listId: list.id});
    }

    init();
  });
