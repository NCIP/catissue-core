angular.module('os.biospecimen.specimenlist.list', ['os.biospecimen.models'])
  .controller('SpecimenListsCtrl', function(
    $scope, $modal, $state, $stateParams, currentUser, 
    SpecimensHolder, SpecimenList, DeleteUtil, Alerts) {

    function init() { 
      $scope.specimens = [];
      $scope.selection = resetSelection();
      $scope.lists = {
        selectedList: undefined,
        myLists: [],
        sharedLists: []
      }

      loadAllSpecimenLists();
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

          if (!!selectedList) {
            $scope.selectList(selectedList);
          }
        }
      );
    }

    function removeSpecimensFromList() {
      var list = $scope.lists.selectedList;
      list.removeSpecimens($scope.selection.specimens).then(
        function(specimens) {
          $scope.specimens = specimens;
          $scope.selection = resetSelection();
          Alerts.success('specimen_list.specimens_removed', {name: list.name});
        }
      );
    }

    $scope.selectList = function (specimenList) {
      $scope.selection = resetSelection();
      $scope.lists.selectedList = specimenList;
      specimenList.getSpecimens().then(
        function(specimens) {
          $scope.specimens = specimens;
        }
      );
    }

    $scope.toggleAllSpecimenSelect = function() {
      $scope.selection.any = $scope.selection.all;
      if (!$scope.selection.all) {
        $scope.selection.specimens = [];
      } else {
        $scope.selection.specimens = [].concat($scope.specimens);
      }

      angular.forEach($scope.specimens, function(specimen) {
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

      $scope.selection.all = ($scope.selection.specimens.length == $scope.specimens.length);
      $scope.selection.any = ($scope.selection.specimens.length > 0);
    };

    $scope.confirmRemoveSpecimens = function () {
      DeleteUtil.confirmDelete({
        entity: $scope.lists.selectedList,
        templateUrl: 'modules/biospecimen/specimen-list/confirm-remove-specimens.html',
        delete: removeSpecimensFromList
      });
    }

    $scope.distributeSpecimens = function() {
      SpecimensHolder.setSpecimens($scope.selection.specimens);
      $state.go('order-addedit', {orderId: ''});
    }

    init();
  });
