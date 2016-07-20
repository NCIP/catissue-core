
angular.module('os.biospecimen.participant.list', ['os.biospecimen.models'])
  .controller('ParticipantListCtrl', function(
    $scope, $state, osRightDrawerSvc, cp, participantListCfg,
    Util, ListPagerOpts) {

    var ctrl = this;

    var pagerOpts, listParams;

    function init() {
      pagerOpts  = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getParticipantsCount});
      listParams = {listName: 'participant-list-view', maxResults: pagerOpts.recordsPerPage + 1};

      $scope.cpId = cp.id;

      $scope.ctx = {
        filtersCfg: angular.copy(participantListCfg.filters),
        filters: {},
        participants: {},
        listSize: -1
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
      cp.getListDetail(listParams, getFilters()).then(
        function(participants) {
          $scope.ctx.participants = participants;
          if (listParams.includeCount) {
            $scope.ctx.listSize = participants.size;
          }

          pagerOpts.refreshOpts(participants.rows);
          if (participants.rows.length > 12 && $scope.listViewCtx.showSearch) {
            osRightDrawerSvc.open();
          }
        }
      );
    }

    function getParticipantsCount() {
      if (!listParams.includeCount) {
        listParams.includeCount = true;
        return cp.getListSize(listParams, getFilters()).then(
          function(size) {
            $scope.ctx.listSize = size;
            return {count: size};
          }
        );
      } else {
        return {count: $scope.ctx.listSize};
      }
    }

    function getFilters() {
      var filters = [];
      if ($scope.ctx.$listFilters) {
        filters = $scope.ctx.$listFilters.getFilters();
      }

      return filters;
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
