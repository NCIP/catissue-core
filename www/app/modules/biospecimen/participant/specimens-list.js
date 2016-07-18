angular.module('os.biospecimen.participant')
  .controller('SpecimensListViewCtrl', function(
    $scope, $state, cp, spmnListCfg, reqBasedDistOrShip,
    CheckList, Util, Specimen, SpecimensHolder, DeleteUtil, Alerts) {

    var ctrl = this;

    function init() {
      $scope.ctx = {
        reqBasedDistOrShip: reqBasedDistOrShip,
        checkList: new CheckList([]),
        filtersCfg: angular.copy(spmnListCfg.filters),
        filters: {},
        specimens: {}
      };

      angular.extend($scope.listViewCtx, {
        listName: 'specimens.list',
        ctrl: ctrl,
        headerButtonsTmpl: 'modules/biospecimen/participant/specimens-list-ops.html',
        showSearch: (spmnListCfg.filters && spmnListCfg.filters.length > 0)
      });

      loadSpecimens();
      Util.filter($scope, 'ctx.filters', loadSpecimens);
    }

    function loadSpecimens() {
      var filtersCfg = $scope.ctx.filtersCfg;

      var filters = [];
      angular.forEach($scope.ctx.filters,
        function(filter, index) {
          filter = getFilter(filter, filtersCfg[index]);
          if (!!filter) {
            filters.push(filter);
          }
        }
      );

      cp.getSpecimens(filters).then(
        function(specimens) {
          $scope.ctx.checkList = new CheckList(specimens.rows);
          $scope.ctx.specimens = specimens;
        }
      );
    }

    function getFilter(filter, filterCfg) {
      var values;
      if (filterCfg.dataType == 'INTEGER' ||
          filterCfg.dataType == 'FLOAT' ||
          filterCfg.dataType == 'DATE') {

        var min = filter.min, max = filter.max;

        if (!min && min != 0 && !max && max != 0) {
          return null;
        }

        if (filterCfg.dataType == 'DATE') {
          if (!!min) {
            min = "\"" + min + "\"";
          }

          if (!!max) {
            max = "\"" + max + "\"";
          }
        }

        values = [min, max];
      } else {

        if (!filter) {
          return null;
        }

        values = [filter];
      }

      return {expr: filterCfg.expr, values: values};
    }

    function gotoView(state, params, msgCode) {
      var selectedSpmns = $scope.ctx.checkList.getSelectedItems();

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

    $scope.showSpecimen = function(specimenId) {
      $state.go('specimen', {specimenId: specimenId});
    }

    $scope.loadFilterValues = function(filterCfg) {
      if (filterCfg.values && filterCfg.values.length > 0) {
        return;
      }

      if (!filterCfg.valuesQ) {
        filterCfg.valuesQ = cp.getExpressionValues(filterCfg.expr);
      }

      filterCfg.valuesQ.then(
        function(values) {
          filterCfg.values = values;
        }
      );
    }

    this.deleteSpecimens = function() {
      var spmns = $scope.ctx.checkList.getSelectedItems();
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

    init();
  });
