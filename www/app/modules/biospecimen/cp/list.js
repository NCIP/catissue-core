
angular.module('os.biospecimen.cp.list', ['os.biospecimen.models'])
  .controller('CpListCtrl', function($scope, $state, CollectionProtocol, Util) {
    function init() {
      $scope.cpFilterOpts = {};
      loadCollectionProtocols();
      Util.filter($scope, 'cpFilterOpts', filter);
    }

    function loadCollectionProtocols(filterOpts) {
      CollectionProtocol.list(filterOpts).then(
        function(cpList) {
          $scope.cpList = cpList;
        }
      );
    };

    $scope.showParticipants = function(cp) {
      $state.go('participant-list', {cpId: cp.id});
    };

    function filter(filterOpts) {
      var cpFilterOpts = angular.copy(filterOpts);
      if (cpFilterOpts.pi) {
        cpFilterOpts.piId = cpFilterOpts.pi.id;
        delete cpFilterOpts.pi;
      }

      loadCollectionProtocols(cpFilterOpts);
    }

    init();
  });
