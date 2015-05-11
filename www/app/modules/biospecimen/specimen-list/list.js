angular.module('os.biospecimen.specimenlist.list', ['os.biospecimen.models'])
  .controller('SpecimenListsCtrl', function(
     $scope, $modal, currentUser, SpecimenList, DeleteUtil, Alerts) {

    function init() { 
      $scope.specimens = [];
      $scope.selectedSpecimens = [];
      $scope.lists = {
        selectedList: undefined,
        myLists: [],
        sharedLists: []
      }

      loadAllSpecimenList();
    }

    function loadAllSpecimenList() {
      SpecimenList.query().then(
        function(lists) {
          angular.forEach(lists, function(list) {
            if (list.owner.id == currentUser.id) {
              $scope.lists.myLists.push(list);
            } else {
              $scope.lists.sharedLists.push(list);
            }
          });

          if (lists.length > 0) {
            $scope.selectList(lists[0]);
          }
        }
      );
    }

    function removeSpecimensFromList() {
      var data = {
        list: $scope.lists.selectedList,
        specimens: $scope.selectedSpecimens,
        operation: 'REMOVE'
      }

      var specimen = $scope.lists.selectedList.newSpecimen(data);
      specimen.$saveOrUpdate().then(function(specimens) {
        $scope.specimens = specimens;
        $scope.selectedSpecimens = [];
      })
    }

    $scope.selectList = function (specimenList) {
      $scope.selectedSpecimens = [];
      $scope.lists.selectedList = specimenList;
      specimenList.getSpecimens().then(function(specimens) {
        $scope.specimens = specimens;
      })
    }

    $scope.toggleSpecimenSelect = function (specimen) {
      if (specimen.selected) {
        $scope.selectedSpecimens.push(specimen);
      } else {
        var idx = $scope.selectedSpecimens.indexOf(specimen);
        if (idx != -1) {
          $scope.selectedSpecimens.splice(idx, 1);
        }
      }
    };

    $scope.confirmRemoveSpecimens = function () {
      var props = {
        templateUrl: 'modules/biospecimen/specimen-list/confirm-remove-specimens.html',
        delete: removeSpecimensFromList
      }
      DeleteUtil.confirmDelete($scope.lists.selectedList, props);
    }

    $scope.editList = function(list) {
      var modalInstance = $modal.open({
        templateUrl: 'modules/biospecimen/specimen-list/addedit-list.html',
        controller: 'AddEditSpecimenListCtrl',
        resolve: {
          list: function() {
            return SpecimenList.getById(list.id);
          }
        }
      });

      modalInstance.result.then(
        function(result) {
          $scope.selectedSpecimens = [];
          if (result) {
            $scope.lists.selectedList = list;
            $scope.specimens = result.specimens;
            Alerts.success("specimen_list.list_updated", {name: list.name});
          } else {
            var idx = $scope.lists.myLists.indexOf(list);
            $scope.lists.myLists.splice(idx, 1);
            if ($scope.lists.selectedList == list) {
              $scope.lists.selectedList = undefined;
              $scope.specimens = [];
            }
            Alerts.success("specimen_list.list_deleted", {name: list.name});
          }
        }
      );
    };

    init();
  });

