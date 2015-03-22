
angular.module('os.query.list', ['os.query.models'])
  .controller('QueryListCtrl', function($scope, currentUser, SavedQuery, QueryFolder, Alerts) {
    function init() {
      $scope.queryList = [];
      $scope.selectedQueries = [];
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

    $scope.toggleQuerySelect = function(query) {
      if (query.selected) {
        $scope.selectedQueries.push(query);
      } else {
        var idx = $scope.selectedQueries.indexOf(query);
        if (idx != -1) {
          $scope.selectedQueries.splice(idx, 1);
        }
      }
    };

    $scope.selectFolder = function(folder) {
      if (folder == $scope.folders.selectedFolder) {
        return;
      }

      $scope.selectedQueries = [];
      $scope.folders.selectedFolder = folder;
      if (!folder) {
        loadAllQueries();
      } else {
        $scope.queryList = folder.getQueries(true);
      }
    };

    $scope.addSelectedQueriesToFolder = function(folder) {
      folder.addQueries($scope.selectedQueries).then(
        function(assignedQueries) {
          var params = {
            count: $scope.selectedQueries.length,
            folderName: folder.name
          };
          Alerts.success("queries.queries_assigned_to_folder", params);
        }
      );
    };

    init();
  });
