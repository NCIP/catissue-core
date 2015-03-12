
angular.module('os.biospecimen.cp.list', ['os.biospecimen.models'])
  .controller('CpListCtrl', function($scope, $state, CollectionProtocol) {
    function init() {
      $scope.cpFilterOpts = {};
      loadCollectionProtocols();
    }

    function loadCollectionProtocols() {
      CollectionProtocol.query({detailedList: true}).then(
        function(cpList) {
          $scope.cpList = cpList;
        }
      );
    };

    $scope.showParticipants = function(cp) {
      $state.go('participant-list', {cpId: cp.id});
    };

    $scope.filter = function(cpFilterOpts) {
      CollectionProtocol.query(cpFilterOpts).then(
        function(cpList) {
          $scope.cpList = cpList;
        }
      )
    }

    init();
  });
