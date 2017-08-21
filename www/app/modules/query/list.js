
angular.module('os.query.list', ['os.query.models'])
  .controller('QueryListCtrl', function(
    $scope, $state, $modal, currentUser, queryGlobal,
    Util, SavedQuery, QueryFolder, Alerts) {

    function init() {
      $scope.filterOpts = {searchString: ''};
      $scope.queryList = [];
      $scope.selectedQueries = [];
      $scope.folders = {
        selectedFolder: undefined,
        myFolders: [],
        sharedFolders: []
      };

      queryGlobal.clearQueryCtx();
      loadQueries($scope.filterOpts);
      loadAllFolders();
      Util.filter($scope, 'filterOpts', loadQueries);
    };

    function loadQueries(filterOpts) {
      $scope.selectedQueries = [];
      if (!$scope.folders.selectedFolder) {
        $scope.queryList = SavedQuery.list(filterOpts);
      } else {
        $scope.queryList = $scope.folders.selectedFolder.getQueries(filterOpts);
      }
    };
 
    function loadAllFolders() {
      QueryFolder.query().then(
        function(folders) {
          $scope.folders.myFolders = [];
          $scope.folders.sharedFolders = [];

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

    function loadFolderQueries(folder) {
      $scope.folders.selectedFolder = folder;
      $scope.filterOpts = {searchString: ''};
      loadQueries($scope.filterOpts);
    };

    $scope.viewResults = function(query) {
      $state.go('query-results', {queryId: query.id});
    }

    $scope.importQuery = function() {
      var mi = $modal.open({
        templateUrl: 'modules/query/import_query.html',
        controller: 'ImportQueryCtrl'
      });

      mi.result.then(
        function(result) {
          $scope.selectFolder(undefined, true);
          Alerts.success('queries.query_imported', {queryTitle: result.title});
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

    $scope.deleteQuery = function(query) {
      var mi = $modal.open({
        templateUrl: 'modules/query/confirm-delete.html',
        controller: 'DeleteQueryConfirmCtrl',
        resolve: {
          query: function() {
            return query;
          }
        }
      });

      mi.result.then(
        function(result) {
          loadFolderQueries($scope.folders.selectedFolder);     
          Alerts.success('queries.query_deleted', {queryTitle: query.title});
        }
      );
    };

    /**
     * Folder related actions
     */
    $scope.selectFolder = function(folder, force) {
      if (folder == $scope.folders.selectedFolder && !force) {
        return;
      }

      loadFolderQueries(folder);
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

    $scope.createNewFolder = function() {
      var modalInstance = $modal.open({
        templateUrl: 'modules/query/addedit-folder.html',
        controller: 'AddEditQueryFolderCtrl',
        resolve: {
          folder: function() {
            return {queries: $scope.selectedQueries};
          }
        }
      });

      modalInstance.result.then(
        function(folder) {
          $scope.folders.myFolders.push(folder);
          Alerts.success("queries.folder_created", {folderName: folder.name});
        }
      );
    };

    $scope.editFolder = function(folder) {
      var modalInstance = $modal.open({
        templateUrl: 'modules/query/addedit-folder.html',
        controller: 'AddEditQueryFolderCtrl',
        resolve: {
          folder: function() {
            return QueryFolder.getById(folder.id);
          }
        }
      });

      modalInstance.result.then(
        function(result) {
          if (result) {
            $scope.folders.selectedFolder = folder;
            $scope.queryList = {count: result.queries.length, queries: result.queries};
            Alerts.success("queries.folder_updated", {folderName: result.name});
          } else {
            var idx = $scope.folders.myFolders.indexOf(folder);
            $scope.folders.myFolders.splice(idx, 1);
            if ($scope.folders.selectedFolder == folder) {
              $scope.selectFolder(undefined);
            } 
            Alerts.success("queries.folder_deleted", {folderName: folder.name});
          }
        }
      );
    };

    init();
  });
