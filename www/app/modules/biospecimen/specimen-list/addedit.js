angular.module('os.biospecimen.specimenlist.addedit', ['os.biospecimen.models'])
  .controller('AddEditSpecimenListCtrl', function($scope, $state, list, SpecimenList) {
    $scope.list = list;

    $scope.saveOrUpdateList = function() {
      var sharedWith = $scope.list.sharedWith.map(function(user) { return {id: user.id} });
      var specimenList =  new SpecimenList({
        id: $scope.list.id,
        name: $scope.list.name,
        sharedWith: sharedWith,
        specimens: $scope.list.specimens
      }); 
 
      specimenList.$saveOrUpdate().then(
        function(savedList) {
          $state.go('specimen-list');
        }
      );
    }
    
    $scope.deleteList = function() {
      $scope.list.$remove().then(
        function() {
          $state.go('specimen-list');
        }
      );
    };
  }
);
