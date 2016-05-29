angular.module('os.biospecimen.specimenlist.list', ['os.biospecimen.models'])
  .controller('SpecimenListsCtrl', function(
    $scope, $modal, $state, $stateParams, $timeout, currentUser, reqBasedDistOrShip,
    SpecimensHolder, SpecimenList, DeleteUtil, Alerts) {

    function init() { 
      $scope.orderCreateOpts =    {resource: 'Order', operations: ['Create']};
      $scope.shipmentCreateOpts = {resource: 'ShippingAndTracking', operations: ['Create']};
      $scope.specimenUpdateOpts = {resource: 'VisitAndSpecimen', operations: ['Update']};

      $scope.listSpecimens = {
        specimens: [],
        actualCount: 0
      }

      $scope.selection = resetSelection();
      $scope.filterOpts = {};
      $scope.lists = {
        selectedList: undefined,
        myLists: [],
        sharedLists: [],
        reqBasedDistOrShip: (reqBasedDistOrShip.value == 'true'),
        url: SpecimenList.url(),
        filtersOpen: false
      }

      $scope.$on('osRightDrawerOpen', toggleFiltersOpen);
      $scope.$on('osRightDrawerClose', toggleFiltersOpen);
      loadAllSpecimenLists();
    }

    function toggleFiltersOpen() {
      $timeout(function() {
        $scope.lists.filtersOpen = !$scope.lists.filtersOpen;
      });
    }

    function resetSelection() {
      return $scope.selection = {all: false, any: false, specimens: []};
    }

    function loadAllSpecimenLists() {
      SpecimenList.query().then(
        function(lists) {
          var selectedList = undefined;
          angular.forEach(lists, function(list) {
            if (!selectedList && (!$stateParams.listId || $stateParams.listId == list.id)) {
              selectedList = list;
            }

            if (list.owner.id == currentUser.id) {
              $scope.lists.myLists.push(list);
            } else {
              $scope.lists.sharedLists.push(list);
            }
          });

          //set default selected specimen list from mylists if not empty
          if (!$stateParams.listId && $scope.lists.myLists.length > 0) {
            selectedList = $scope.lists.myLists[0];
          }

          if (!!selectedList) {
            $scope.selectList(selectedList);
          }
        }
      );
    }

    function removeSpecimensFromList() {
      var list = $scope.lists.selectedList;
      list.removeSpecimens($scope.selection.specimens).then(
        function(listSpecimens) {
          $scope.listSpecimens = listSpecimens;
          $scope.selection = resetSelection();

          var type = list.getListType(currentUser);
          Alerts.success('specimen_list.specimens_removed_from_' + type, list);
        }
      );
    }

    function showSelectSpecimensErrMsg(msgCode) {
      Alerts.error(msgCode);
    };

    $scope.selectList = function (specimenList) {
      $scope.filterOpts = {};
      $scope.selection = resetSelection();
      $scope.lists.selectedList = specimenList;
      specimenList.getSpecimens().then(
        function(listSpecimens) {
          $scope.listSpecimens = listSpecimens
        }
      );
    }

    $scope.viewSpecimen = function(specimen) {
      $state.go('specimen', {specimenId: specimen.id});
    }

    $scope.toggleAllSpecimenSelect = function() {
      $scope.selection.any = $scope.selection.all;
      if (!$scope.selection.all) {
        $scope.selection.specimens = [];
      } else {
        $scope.selection.specimens = [].concat($scope.listSpecimens.specimens);
      }

      angular.forEach($scope.listSpecimens.specimens, function(specimen) {
        specimen.selected = $scope.selection.all;
      });
    }

    $scope.toggleSpecimenSelect = function (specimen) {
      if (specimen.selected) {
        $scope.selection.specimens.push(specimen);
      } else {
        var idx = $scope.selection.specimens.indexOf(specimen);
        if (idx != -1) {
          $scope.selection.specimens.splice(idx, 1);
        }
      }

      $scope.selection.all = ($scope.selection.specimens.length == $scope.listSpecimens.specimens.length);
      $scope.selection.any = ($scope.selection.specimens.length > 0);
    };

    $scope.confirmRemoveSpecimens = function () {
      if (!$scope.selection.any) {
        showSelectSpecimensErrMsg("specimen_list.no_specimens_for_deletion");
        return;
      }

      var selectedList = $scope.lists.selectedList;
      var listType = selectedList.getListType(currentUser);
      DeleteUtil.confirmDelete({
        entity: selectedList,
        props: {messageKey: 'specimen_list.confirm_remove_specimens_from_' + listType},
        templateUrl: 'modules/biospecimen/specimen-list/confirm-remove-specimens.html',
        delete: removeSpecimensFromList
      });
    }

    $scope.distributeSpecimens = function() {
      if (!$scope.selection.any) {
        showSelectSpecimensErrMsg("specimen_list.no_specimens_for_distribution");
        return;
      }

      SpecimensHolder.setSpecimens($scope.selection.specimens);
      $state.go('order-addedit', {orderId: ''});
    }

    $scope.shipSpecimens = function() {
      if (!$scope.selection.any) {
        showSelectSpecimensErrMsg("specimen_list.no_specimens_for_shipment");
        return;
      }

      SpecimensHolder.setSpecimens($scope.selection.specimens);
      $state.go('shipment-addedit', {orderId: ''});
    }
    
    $scope.addEvent = function() {
      if (!$scope.selection.any) {
        showSelectSpecimensErrMsg('specimen_list.no_specimens_to_add_event');
        return;
      }
      
      SpecimensHolder.setSpecimens($scope.selection.specimens);
      $state.go('bulk-add-event');
    }

    init();
  });
