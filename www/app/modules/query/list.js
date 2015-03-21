
angular.module('os.query.list', ['os.query.models'])
  .controller('QueryListCtrl', function($scope, currentUser, SavedQuery, QueryFolder) {
    function init() {
      $scope.queryList = [];
      $scope.folders = {
        selectedFolder: undefined,
        myFolders: [],
        sharedFolders: []
      };

      loadAllQueries();
      loadAllFolders();
    };

    function loadAllQueries() {
      $scope.queryList = SavedQuery.list();
    };
 
    function loadAllFolders() {
      QueryFolder.query().then(
        function(folders) {
          angular.forEach(folders, function(folder) {
            if (folder.owner.id == currentUser.id) {
              $scope.folders.myFolders.push(folder);
            } else {
              $scope.folders.sharedFolders.push(folder);
            }
          });
        }
      );
    };

    $scope.selectFolder = function(folder) {
      if (folder == $scope.folders.selectedFolder) {
        return;
      }

      $scope.folders.selectedFolder = folder;
      if (!folder) {
        loadAllQueries();
      } else {
        $scope.queryList = folder.getQueries(true);
      }
    };

    init();
  });
