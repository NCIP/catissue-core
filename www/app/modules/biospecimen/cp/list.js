
angular.module('os.biospecimen.cp.list', ['os.biospecimen.models'])
  .controller('CpListCtrl', function($scope, $state, cpList, CollectionProtocol, Util, PvManager) {
    function init() {
      $scope.cpFilterOpts = {};
      $scope.cpList = cpList;
      $scope.sites = PvManager.getSites();
      Util.filter($scope, 'cpFilterOpts', filter);
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

    init();
  });
