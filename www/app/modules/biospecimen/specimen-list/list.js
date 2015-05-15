angular.module('os.biospecimen.specimenlist.list', ['os.biospecimen.models'])
  .controller('SpecimenListsCtrl', function(
     $scope, $modal, $stateParams, currentUser, SpecimenList, DeleteUtil, Alerts) {

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
      list.removeSpecimens($scope.selectedSpecimens).then(
        function(specimens) {
          $scope.specimens = specimens;
          $scope.selectedSpecimens = [];
          Alerts.success('specimen_list.specimens_removed', {name: list.name});
        }
      );
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
      DeleteUtil.confirmDelete({
        entity: $scope.lists.selectedList,
        templateUrl: 'modules/biospecimen/specimen-list/confirm-remove-specimens.html',
        delete: removeSpecimensFromList
      });
    }

    init();
  });
