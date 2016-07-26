angular.module('os.biospecimen.specimenlist')
  .controller('SpecimenListSpecimensCtrl', function(
    $scope, $state, $stateParams, $timeout, $filter, currentUser, reqBasedDistOrShip, list,
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
        url: SpecimenList.url(),
        breadcrumbs: $stateParams.breadcrumbs
      }

      $scope.$on('osRightDrawerOpen', initFilterPvs);
      resetSelection();
      Util.filter($scope, 'ctx.filterOpts', filterSpecimens);
    }

    function initFilterPvs() {
      if ($scope.ctx.filterPvs.init) {
        return;
      }

      var types = [], sites = [], cps = [], lineages = [], containers = [];
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

          var location = specimen.storageLocation;
          if (location && location.name && containers.indexOf(location.name) == -1) {
            containers.push(location.name);
          }
        }
      );

      var filterPvs = {
        init: true,
        types: types.sort(),
        sites: sites.sort(),
        cps: cps.sort(),
        lineages: lineages.sort(),
        containers: containers.sort()
      }

      $timeout(function() {$scope.ctx.filterPvs = filterPvs});
    }

    function reinitFilterPvs() {
      $scope.ctx.filterPvs.init = false;
      initFilterPvs();
    }

    function resetSelection() {
      return $scope.ctx.selection = {all: false, any: false, specimens: []};
    }

    function isSpecimenAvailable(spmn) {
      return (spmn.availableQty == undefined || spmn.availableQty > 0);
    }

    function filterSpecimens(filterOpts) {
      var showAvailable = filterOpts.showAvailable;
      filterOpts.showAvailable = undefined;

      var spmnsInView = $filter('filter')(list.specimens, filterOpts);
      if (showAvailable) {
        spmnsInView = spmnsInView.filter(isSpecimenAvailable);
      }

      filterOpts.showAvailable = showAvailable;

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
          reinitFilterPvs();
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

    $scope.addChildSpecimens = function() {
      var list = $scope.ctx.list;
      var oldLen = list.specimens.length;

      list.addChildSpecimens().then(
        function(listSpecimens) {
          list.specimens = listSpecimens.specimens;
          filterSpecimens($scope.ctx.filterOpts);

          var newLen = list.specimens.length;
          if (newLen > oldLen) {
            Alerts.success('specimen_list.child_specimens_added', {count: (newLen - oldLen)});
            reinitFilterPvs();
          }
        }
      );
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
