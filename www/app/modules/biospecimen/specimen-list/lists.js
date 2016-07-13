angular.module('os.biospecimen.specimenlist')
  .controller('SpecimenListsCtrl', function($scope, $state, lists, SpecimenList, Util, ListPagerOpts) {

    var pagerOpts;

    function init() {
      $scope.ctx = {filterOpts: {}};
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getSpecimenListsCount});

      setList(lists);
      Util.filter($scope, 'ctx.filterOpts', loadLists);
    }

    function setList(list) {
      $scope.ctx.lists = list;
      pagerOpts.refreshOpts(list);
    }

    function loadLists(filterOpts) {
      var params = angular.extend({includeStats: true}, filterOpts);
      SpecimenList.query(params).then(
        function(lists) {
          setList(lists);
        }
      );
    }

    function getSpecimenListsCount() {
      return SpecimenList.getCount($scope.ctx.filterOpts);
    }

    $scope.viewList = function(list) {
      $state.go('specimen-list', {listId: list.id});
    }

    init();
  });
