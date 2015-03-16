
angular.module('os.biospecimen.cp.list', ['os.biospecimen.models'])
  .controller('CpListCtrl', function($scope, $state, CollectionProtocol) {
    function init() {
      $scope.cpFilterOpts = {};
      loadCollectionProtocols();
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

    $scope.filter = function() {
      loadCollectionProtocols($scope.cpFilterOpts);
    }

    $scope.onPiSelect = function(pi) {
      $scope.cpFilterOpts.piId = pi.id;
      $scope.filter();
    }

    init();
  });
