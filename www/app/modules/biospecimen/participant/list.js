
angular.module('os.biospecimen.participant.list', ['os.biospecimen.models'])
  .controller('ParticipantListCtrl', function(
    $scope, $state, $stateParams, $modal, 
    CollectionProtocol, CollectionProtocolRegistration, Util) {

    function init() {
      $scope.cpId = $stateParams.cpId;
      $scope.filterOpts = {};
      loadCp();
      loadParticipants();
      Util.filter($scope, 'filterOpts', loadParticipants);
    }

    function loadCp() {
      CollectionProtocol.getById($scope.cpId).then(
        function(cp) {
          $scope.cp = cp;
        }
      );
    }

    function loadParticipants() {
      CollectionProtocolRegistration.listForCp($scope.cpId, true, $scope.filterOpts).then(
        function(cprList) {
          $scope.cprList = cprList;
        }
      )
    };

    $scope.clearFilters = function() {
      $scope.filterOpts = {};
    };

    $scope.showParticipantOverview = function(cpr) {
      $state.go('participant-detail.overview', {cprId: cpr.cprId});
    };

    init();
  });
