angular.module('os.biospecimen.participant')
  .controller('SpecimensListViewCtrl', function(
    $scope, $state, cp, spmnListCfg, reqBasedDistOrShip,
    Util, Specimen, SpecimensHolder, DeleteUtil, Alerts, ListPagerOpts) {

    var ctrl = this;

    var pagerOpts, listParams;

    function init() {
      pagerOpts  = new ListPagerOpts({listSizeGetter: getSpecimensCount});
      listParams = {listName: 'specimen-list-view', maxResults: pagerOpts.recordsPerPage + 1};

      $scope.ctx = {
        reqBasedDistOrShip: reqBasedDistOrShip,
        filtersCfg: angular.copy(spmnListCfg.filters),
        filters: {},
        specimens: {},
        listSize: -1
      };

      angular.extend($scope.listViewCtx, {
        listName: 'specimens.list',
        ctrl: ctrl,
        headerActionsTmpl: 'modules/biospecimen/participant/specimens-list-pager.html',
        headerButtonsTmpl: 'modules/biospecimen/participant/specimens-list-ops.html',
        showSearch: (spmnListCfg.filters && spmnListCfg.filters.length > 0)
      });

      loadSpecimens();
      Util.filter($scope, 'ctx.filters', loadSpecimens);
    }

    function loadSpecimens() {
      cp.getListDetail(listParams, getFilters()).then(
        function(specimens) {
          $scope.ctx.specimens = specimens;
          if (listParams.includeCount) {
            $scope.ctx.listSize = specimens.size;
          }

          pagerOpts.refreshOpts(specimens.rows);
        }
      );
    }

    function getSpecimensCount() {
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

    function gotoView(state, params, msgCode) {
      var selectedSpmns = $scope.ctx.$list.getSelectedItems();
      if (!selectedSpmns || selectedSpmns.length == 0) {
        Alerts.error('specimen_list.' + msgCode);
        return;
      }

      var ids = selectedSpmns.map(function(spmn) { return spmn.hidden.specimenId; });
      Specimen.getByIds(ids).then(
        function(spmns) {
          SpecimensHolder.setSpecimens(spmns);
          $state.go(state, params);
        }
      );
    }

    $scope.showSpecimen = function(row) {
      $state.go('specimen', {specimenId: row.hidden.specimenId});
    }

    $scope.loadFilterValues = function(expr) {
      return cp.getExpressionValues(expr);
    }

    $scope.setListCtrl = function($list) {
      $scope.ctx.$list = $list;
    }

    $scope.setFiltersCtrl = function($listFilters) {
      $scope.ctx.$listFilters = $listFilters;
    }

    this.deleteSpecimens = function() {
      var spmns = $scope.ctx.$list.getSelectedItems();
      if (!spmns || spmns.length == 0) {
        Alerts.error('specimens.no_specimens_for_delete');
        return;
      }

      var specimenIds = spmns.map(function(spmn) { return spmn.hidden.specimenId; });
      var opts = {
        confirmDelete: 'specimens.delete_specimens_heirarchy',
        successMessage: 'specimens.specimens_hierarchy_deleted',
        onBulkDeletion: loadSpecimens
      }
      DeleteUtil.bulkDelete({bulkDelete: Specimen.bulkDelete}, specimenIds, opts);
    }

    this.distributeSpecimens = function() {
      gotoView('order-addedit', {orderId: ''}, 'no_specimens_for_distribution');
    }

    this.shipSpecimens = function() {
      gotoView('shipment-addedit', {shipmentId: ''}, 'no_specimens_for_shipment');
    }

    this.createAliquots = function() {
      gotoView('specimen-bulk-create-aliquots', {}, 'no_specimens_to_create_aliquots');
    }

    this.createDerivatives = function() {
      gotoView('specimen-bulk-create-derivatives', {}, 'no_specimens_to_create_derivatives');
    }

    this.addEvent = function() {
      gotoView('bulk-add-event', {}, 'no_specimens_to_add_event');
    }

    this.pagerOpts = function() {
      return pagerOpts;
    }

    init();
  });
