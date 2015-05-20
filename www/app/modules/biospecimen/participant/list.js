
angular.module('os.biospecimen.participant.list', ['os.biospecimen.models'])
  .controller('ParticipantListCtrl', function(
    $scope, $state, $stateParams, $modal, $q, 
    cp, CollectionProtocolRegistration, Util, CpConfigSvc) {

    function init() {
      $scope.cpId = $stateParams.cpId;
      $scope.cp = cp;
      $scope.filterOpts = {};
      $scope.participantResource = {
        registerOpts: {resource: 'ParticipantPhi', operations: ['Create'], cp: $scope.cp.shortTitle},
      }
      loadParticipants();
      Util.filter($scope, 'filterOpts', loadParticipants);
      showOrHideBulkRegBtn();
    }

    function loadParticipants() {
      CollectionProtocolRegistration.listForCp($scope.cpId, true, $scope.filterOpts).then(
        function(cprList) {
          $scope.cprList = cprList;
        }
      )
    }

    function showOrHideBulkRegBtn() {
      $q.when(CpConfigSvc.getBulkRegParticipantTmpl($scope.cpId, -1)).then(
        function(tmpl) {
          $scope.showBulkRegBtn = tmpl != undefined;
        }
      );
    };

    $scope.clearFilters = function() {
      $scope.filterOpts = {};
    };

    $scope.showParticipantOverview = function(cpr) {
      $state.go('participant-detail.overview', {cprId: cpr.cprId});
    };

    init();
  });
