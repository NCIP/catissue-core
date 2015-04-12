angular.module('os.query.addeditfolder', ['os.query.models'])
  .controller('AddEditQueryFolderCtrl', function($scope, $modalInstance, folder, QueryFolder) {
    $scope.folder = folder;

    $scope.saveOrUpdateFolder = function () {
      var sharedWith = [];
      if (!$scope.folder.sharedWithAll) {
        sharedWith = $scope.folder.sharedWith.map(function(user) { return {id: user.id} });
      }

      var queries = $scope.folder.queries.map(function(query) { return {id: query.id} });
        
      var folderToSave = new QueryFolder({
        id: $scope.folder.id,
        name: $scope.folder.name,
        sharedWithAll: $scope.folder.sharedWithAll,
        sharedWith: sharedWith,
        queries: queries
      });
        
      folderToSave.$saveOrUpdate().then(
        function(savedFolder) {
          $modalInstance.close(savedFolder);
        }
      );
    };

    $scope.deleteFolder = function() {
      $scope.folder.$remove().then(
        function() {
          $modalInstance.close(null);
        }
      );      
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.removeQuery = function(query, idx) {
      $scope.folder.queries.splice(idx, 1);
    };
  }
);
