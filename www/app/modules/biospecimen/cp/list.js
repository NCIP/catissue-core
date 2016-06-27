
angular.module('os.biospecimen.cp.list', ['os.biospecimen.models'])
  .controller('CpListCtrl', 
    function($scope, $state, cpList, CollectionProtocol, Util, PvManager, AuthorizationService) {

    function init() {
      $scope.cpFilterOpts = {};
      $scope.cpList = cpList;
      $scope.sites = PvManager.getSites();
      Util.filter($scope, 'cpFilterOpts', filter);

      $scope.allowReadJobs = AuthorizationService.isAllowed($scope.participantResource.createOpts) ||
        AuthorizationService.isAllowed($scope.participantResource.updateOpts) ||
        AuthorizationService.isAllowed($scope.specimenResource.updateOpts);
    }

    function loadCollectionProtocols(filterOpts) {
      CollectionProtocol.list(filterOpts).then(
        function(cpList) {
          $scope.cpList = cpList;
        }
      );
    };

    function filter(filterOpts) {
      var cpFilterOpts = angular.copy(filterOpts);
      if (cpFilterOpts.pi) {
        cpFilterOpts.piId = cpFilterOpts.pi.id;
        delete cpFilterOpts.pi;
      }

      loadCollectionProtocols(cpFilterOpts);
    }

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
