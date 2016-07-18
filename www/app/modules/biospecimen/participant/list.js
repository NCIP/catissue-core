
angular.module('os.biospecimen.participant.list', ['os.biospecimen.models'])
  .controller('ParticipantListCtrl', function(
    $scope, $state, $stateParams, $modal, $q, osRightDrawerSvc,
    cp, CollectionProtocolRegistration, Util, ListPagerOpts) {

    var pagerOpts, filterOpts;

    var ctrl = this;

    function init() {
      $scope.cpId = $stateParams.cpId;
      $scope.cp = cp;
      pagerOpts  = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getCprCount});
      filterOpts = $scope.filterOpts = {maxResults: pagerOpts.recordsPerPage + 1};

      $scope.participantResource = {
        registerOpts: {resource: 'ParticipantPhi', operations: ['Create'], cp: $scope.cp.shortTitle},
      }

      angular.extend($scope.listViewCtx, {
        listName: 'participant.list',
        ctrl: ctrl,
        headerButtonsTmpl: 'modules/biospecimen/participant/register-button.html',
        showSearch: true
      });

      loadParticipants();
      Util.filter($scope, 'filterOpts', loadParticipants);
    }

    function loadParticipants() {
      CollectionProtocolRegistration.listForCp($scope.cpId, true, $scope.filterOpts).then(
        function(cprList) {
          if (!$scope.cprList && cprList.length > 12) {
            //
            // Show search options when number of participants are more than 12
            //
            osRightDrawerSvc.open();
          }

          $scope.cprList = cprList;
          pagerOpts.refreshOpts(cprList);
        }
      )
    }

    function getCprCount() {
      return CollectionProtocolRegistration.getCprCount($scope.cpId, true, filterOpts);
    }

    $scope.clearFilters = function() {
      $scope.filterOpts = {maxResults: pagerOpts.recordsPerPage + 1};
    };

    $scope.showParticipantOverview = function(cpr) {
      $state.go('participant-detail.overview', {cprId: cpr.cprId});
    };

    init();
  });
