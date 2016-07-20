
angular.module('os.biospecimen.participant.list', ['os.biospecimen.models'])
  .controller('ParticipantListCtrl', function($scope, $state, osRightDrawerSvc, cp, participantListCfg, Util) {

    var ctrl = this;

    function init() {
      $scope.cpId = cp.id;

      $scope.ctx = {
        filtersCfg: angular.copy(participantListCfg.filters),
        filters: {},
        participants: {}
      };

      angular.extend($scope.listViewCtx, {
        listName: 'participant.list',
        ctrl: ctrl,
        headerButtonsTmpl: 'modules/biospecimen/participant/register-button.html',
        showSearch: (participantListCfg.filters && participantListCfg.filters.length > 0)
      });

      loadParticipants();
      Util.filter($scope, 'ctx.filters', loadParticipants);
    }

    function loadParticipants() {
      var filters = [];
      if ($scope.ctx.$listFilters) {
        filters = $scope.ctx.$listFilters.getFilters();
      }

      cp.getParticipants(filters).then(
        function(participants) {
          $scope.ctx.participants = participants;
          if (participants.rows.length > 12 && $scope.listViewCtx.showSearch) {
            osRightDrawerSvc.open();
          }
        }
      );
    }

    $scope.showParticipant = function(row) {
      $state.go('participant-detail.overview', {cprId: row.hidden.cprId});
    };

    $scope.loadFilterValues = function(expr) {
      return cp.getExpressionValues(expr);
    }

    $scope.setListCtrl = function($list) {
      $scope.ctx.$list = $list;
    }

    $scope.setFiltersCtrl = function($listFilters) {
      $scope.ctx.$listFilters = $listFilters;
    }

    init();
  });
