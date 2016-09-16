
angular.module('os.biospecimen.cp.list', ['os.biospecimen.models'])
  .controller('CpListCtrl', function(
    $scope, $state, cpList, CollectionProtocol, Util, PvManager, ListPagerOpts, AuthorizationService) {

    var pagerOpts, filterOpts;

    function init() {
      pagerOpts  = $scope.pagerOpts    = new ListPagerOpts({listSizeGetter: getCpCount});
      filterOpts = $scope.cpFilterOpts = {maxResults: pagerOpts.recordsPerPage + 1};

      $scope.allowReadJobs = AuthorizationService.isAllowed($scope.participantResource.createOpts) ||
        AuthorizationService.isAllowed($scope.participantResource.updateOpts) ||
        AuthorizationService.isAllowed($scope.specimenResource.updateOpts);

      setList(cpList);
      Util.filter($scope, 'cpFilterOpts', loadCollectionProtocols);
    }

    function setList(list) {
      $scope.cpList = list;
      pagerOpts.refreshOpts(list);
    }

    function getCpCount() {
      return CollectionProtocol.getCount(filterOpts);
    }

    function loadCollectionProtocols() {
      CollectionProtocol.list(filterOpts).then(
        function(cpList) {
          setList(cpList);
        }
      );
    };

    $scope.showCpSummary = function(cp) {
      $state.go('cp-summary-view', {cpId: cp.id});
    };

    $scope.viewCatalog = function(cp) {
      cp.getCatalogQuery().then(
        function(query) {
          $state.go('query-results', {queryId: query.id, cpId: cp.id});
        }
      );
    }

    init();
  });
