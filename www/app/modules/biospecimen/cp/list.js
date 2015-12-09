
angular.module('os.biospecimen.cp.list', ['os.biospecimen.models'])
  .controller('CpListCtrl', function($scope, $state, CollectionProtocol, Util, PvManager, SpecimenRequest) {
    function init() {
      $scope.cpFilterOpts = {};
      loadCollectionProtocols();
      $scope.sites = PvManager.getSites();
      Util.filter($scope, 'cpFilterOpts', filter);

      $scope.haveRequests = false;
      SpecimenRequest.haveRequests().then(
        function(haveRequests) {
          $scope.haveRequests = haveRequests;
        }
      );
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

    $scope.showParticipants = function(cp) {
      $state.go('participant-list', {cpId: cp.id});
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
