angular.module('os.biospecimen.specimenlist')
  .controller('SpecimenListSpecimensCtrl', function(
    $scope, $state, $timeout, $filter, currentUser, reqBasedDistOrShip, list,
    SpecimensHolder, SpecimenList, DeleteUtil, Alerts, Util) {

    function init() { 
      $scope.orderCreateOpts =    {resource: 'Order', operations: ['Create']};
      $scope.shipmentCreateOpts = {resource: 'ShippingAndTracking', operations: ['Create']};
      $scope.specimenUpdateOpts = {resource: 'VisitAndSpecimen', operations: ['Update']};

      $scope.ctx = {
        list: list,
        spmnsInView: list.specimens,
        filterOpts: {},
        filterPvs: {init: false},
        selection: {},
        reqBasedDistOrShip: (reqBasedDistOrShip.value == 'true'),
        url: SpecimenList.url()
      }

      $scope.$on('osRightDrawerOpen', initFilterPvs);
      resetSelection();
      Util.filter($scope, 'ctx.filterOpts', filterSpecimens);
    }

    function initFilterPvs() {
      if ($scope.ctx.filterPvs.init) {
        return;
      }

      var types = [], sites = [], cps = [], lineages = [];
      angular.forEach(list.specimens,
        function(specimen) {
          if (types.indexOf(specimen.type) == -1) {
            types.push(specimen.type);
          }

          if (sites.indexOf(specimen.anatomicSite) == -1) {
            sites.push(specimen.anatomicSite);
          }

          if (cps.indexOf(specimen.cpShortTitle) == -1) {
            cps.push(specimen.cpShortTitle);
          }

          if (lineages.indexOf(specimen.lineage) == -1) {
            lineages.push(specimen.lineage);
          }
        }
      );

      var filterPvs = {
        init: true,
        types: types.sort(),
        sites: sites.sort(),
        cps: cps.sort(),
        lineages: lineages.sort()
      }

      $timeout(function() {$scope.ctx.filterPvs = filterPvs});
    }

    function resetSelection() {
      return $scope.ctx.selection = {all: false, any: false, specimens: []};
    }

    function filterSpecimens(filterOpts) {
      var spmnsInView = $filter('filter')(list.specimens, filterOpts);
      var selectedSpmns = spmnsInView.filter(function(spmn) { return !!spmn.selected });

      $scope.ctx.spmnsInView = spmnsInView;
      $scope.ctx.selection.specimens = selectedSpmns;
      $scope.ctx.selection.all = (selectedSpmns.length > 0 && selectedSpmns.length == spmnsInView.length);
      $scope.ctx.selection.any = (selectedSpmns.length > 0);
    }

    function removeSpecimensFromList() {
      var list = $scope.ctx.list;
      list.removeSpecimens($scope.ctx.selection.specimens).then(
        function(listSpecimens) {
          var spmnsInView = $scope.ctx.spmnsInView;
          for (var i = spmnsInView.length - 1; i >= 0; --i) {
            if (spmnsInView[i].selected) {
              spmnsInView.splice(i, 1);
            }
          }

          list.specimens = listSpecimens.specimens;
          $scope.ctx.selection = resetSelection();

          var type = list.getListType(currentUser);
          Alerts.success('specimen_list.specimens_removed_from_' + type, list);
        }
      );
    }

    function gotoView(state, params, msgCode) {
      if (!$scope.ctx.selection.any) {
        Alerts.error('specimen_list.' + msgCode);
        return;
      }

      SpecimensHolder.setSpecimens($scope.ctx.selection.specimens);
      $state.go(state, params);
    }

    $scope.viewSpecimen = function(specimen) {
      $state.go('specimen', {specimenId: specimen.id});
    }

    $scope.toggleAllSpecimenSelect = function() {
      $scope.ctx.selection.any = $scope.ctx.selection.all;
      if (!$scope.ctx.selection.all) {
        $scope.ctx.selection.specimens = [];
      } else {
        $scope.ctx.selection.specimens = [].concat($scope.ctx.spmnsInView);
      }

      angular.forEach($scope.ctx.spmnsInView,
        function(specimen) {
          specimen.selected = $scope.ctx.selection.all;
        }
      );
    }

    $scope.toggleSpecimenSelect = function (specimen) {
      var specimens = $scope.ctx.selection.specimens;

      if (specimen.selected) {
        specimens.push(specimen);
      } else {
        var idx = specimens.indexOf(specimen);
        if (idx != -1) {
          specimens.splice(idx, 1);
        }
      }

      $scope.ctx.selection.all = (specimens.length == $scope.ctx.spmnsInView.length);
      $scope.ctx.selection.any = (specimens.length > 0);
    };

    $scope.confirmRemoveSpecimens = function () {
      if (!$scope.ctx.selection.any) {
        Alerts.error("specimen_list.no_specimens_for_deletion");
        return;
      }

      var listType = list.getListType(currentUser);
      DeleteUtil.confirmDelete({
        entity: list,
        props: {messageKey: 'specimen_list.confirm_remove_specimens_from_' + listType},
        templateUrl: 'modules/biospecimen/specimen-list/confirm-remove-specimens.html',
        delete: removeSpecimensFromList
      });
    }

    $scope.distributeSpecimens = function() {
      gotoView('order-addedit', {orderId: ''}, 'no_specimens_for_distribution');
    }

    $scope.shipSpecimens = function() {
      gotoView('shipment-addedit', {shipmentId: ''}, 'no_specimens_for_shipment');
    }
    
    $scope.createAliquots = function() {
      gotoView('specimen-bulk-create-aliquots', {}, 'no_specimens_to_create_aliquots');
    }

    $scope.createDerivatives = function() {
      gotoView('specimen-bulk-create-derivatives', {}, 'no_specimens_to_create_derivatives');
    }

    $scope.addEvent = function() {
      gotoView('bulk-add-event', {}, 'no_specimens_to_add_event');
    }

    $scope.clearFilters = function() {
      $scope.ctx.filterOpts = {};
    }

    init();
  });
