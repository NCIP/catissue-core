angular.module('os.biospecimen.specimenlist.addedit', ['os.biospecimen.models'])
  .controller('AddEditSpecimenListCtrl', function($scope, $modalInstance, list, SpecimenList) {
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
          $modalInstance.close(savedList);
        }
      );
    
    }
    
    $scope.deleteList = function() {
      $scope.list.$remove().then(
        function() {
          $modalInstance.close(null);
        }
      );
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.removeSpecimen = function(specimen, idx) {
      $scope.list.specimens.splice(idx, 1);
    };

  }
);
