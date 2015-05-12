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
        id: $scope.lists.selectedList.id,
        specimens: $scope.selectedSpecimens,
        operation: 'REMOVE'
      }

      var specimenList = new SpecimenList(data);
      specimenList.updateSpecimens().then(function(specimens) {
        $scope.specimens = specimens;
        $scope.selectedSpecimens = [];
        Alerts.success('specimen_list.specimens_removed', {name: $scope.lists.selectedList.name});
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
      DeleteUtil.confirmDelete({
        entity: $scope.lists.selectedList,
        templateUrl: 'modules/biospecimen/specimen-list/confirm-remove-specimens.html',
        delete: removeSpecimensFromList
      });
    }

    init();
  });
