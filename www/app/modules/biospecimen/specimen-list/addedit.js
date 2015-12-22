angular.module('os.biospecimen.specimenlist.addedit', ['os.biospecimen.models'])
  .controller('AddEditSpecimenListCtrl', function(
    $scope, $state, list, SpecimenList, SpecimensHolder, DeleteUtil) {
 
    function init() { 
      $scope.list = list;
      $scope.list.specimens = SpecimensHolder.getSpecimens() || list.specimens;
      $scope.list.isAllowedToDeleteList = isAllowedToDeleteList(); 
      $scope.isQueryOrSpecimenPage =  SpecimensHolder.getSpecimens() != undefined;
      SpecimensHolder.setSpecimens(undefined);
    }

    function isAllowedToDeleteList() {
       return !!$scope.list.id &&
              !$scope.list.defaultList &&
              ($scope.list.owner.id == $scope.currentUser.id || $scope.currentUser.admin)
    }

    $scope.saveOrUpdateList = function() {
      var sharedWith = $scope.list.sharedWith.map(
        function(user) { 
          return {id: user.id} 
        }
      );

      var specimenList =  new SpecimenList({
        id: $scope.list.id,
        name: $scope.list.name,
        description: $scope.list.description,
        sharedWith: sharedWith
      }); 

      var $q = undefined;
      if (!specimenList.id) {
        specimenList.specimens =  $scope.list.specimens;
        $q = specimenList.$saveOrUpdate();
      } else {
        $q = specimenList.$patch(specimenList);
      }
 
      $q.then(
        function(savedList) {
          if ($scope.isQueryOrSpecimenPage) {
            $scope.back();
          } else {
            $state.go('specimen-list', {listId: savedList.id});
          }
        }
      );
    }

    $scope.deleteList = function() {
      DeleteUtil.delete($scope.list, {
        onDeleteState: 'specimen-list',
        deleteWithoutCheck: true
      });
    }

    init();
  }
);
