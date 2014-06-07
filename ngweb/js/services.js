
angular.module('plus.services', [])
  .factory('QueryService', function($http, $document) {
    var baseUrl         = '/catissuecore/rest/ng/query/';
    var savedQueriesUrl = '/catissuecore/rest/ng/saved-queries/';
    var foldersUrl      = '/catissuecore/rest/ng/query-folders/';
    var auditLogsUrl    = '/catissuecore/rest/ng/query-audit-logs/';

    var successfn = function(result) { return result.data; };

    return {
      executeQuery: function(id, cpId, drivingForm, aql, runType, wideRows) {
        var req = {savedQueryId: id, cpId: cpId, drivingForm: drivingForm, aql: aql, runType: runType, wideRows: wideRows};
        return $http.post(baseUrl, req).then(successfn);
      },

      exportQueryData: function(id, cpId, drivingForm, aql, runType, wideRows) {
        var req = {savedQueryId: id, cpId: cpId, drivingForm: drivingForm, aql: aql, runType: runType, wideRows: wideRows};
        return $http.post(baseUrl + 'export', req).then(successfn);
      },

      downloadQueryData: function(fileId) {
        var link = angular.element('<a/>')
          .attr({
            href: '/catissuecore/rest/ng/query/export?fileId=' + fileId, 
            target: '_blank'});

        angular.element($document[0].body).append(link);
        link[0].click();
        link.remove();
      },

      saveOrUpdateQuery: function(queryDef) {
        if (queryDef.id) {
          return $http.put(savedQueriesUrl + queryDef.id, queryDef).then(successfn);
        } else {
          return $http.post(savedQueriesUrl, queryDef).then(successfn);
        }
      },

      getQueries: function(countReq, startAt, maxRecs) {
        if (!countReq) { countReq = false; }
        return $http.get(savedQueriesUrl + '?countReq=' + countReq + '&start=' + startAt + '&max=' + maxRecs).then(successfn);
      },

      getQuery: function(queryId) {
        return $http.get(savedQueriesUrl + queryId).then(successfn);
      },

      getFolder: function(folderId) {
        return $http.get(foldersUrl + folderId).then(successfn);
      },

      getFolders: function() {
        return $http.get(foldersUrl).then(successfn);
      },

      getFolderQueries: function(folderId, countReq, startAt, maxRecs) {
        if (!countReq) { countReq = false; }
        return $http.get(foldersUrl + folderId + "/saved-queries" + '?countReq=' + countReq + '&start=' + startAt + '&max=' + maxRecs).then(successfn);
      },

      addQueriesToFolder: function(folderId, queries) {
        return $http.put(foldersUrl + folderId + "/saved-queries?operation=ADD", queries).then(successfn); 
      },

      removeQueriesFromFolder: function(folderId, queries) {
        if (folderId != -1) {
          return $http.put(foldersUrl + folderId + "/saved-queries?operation=REMOVE", queries).then(successfn);
        } else {
        }
      },

      saveOrUpdateQueryFolder: function(folderDetail) {
        if (folderDetail.id) {
          return $http.put(foldersUrl + folderDetail.id, folderDetail).then(successfn);
        } else {
          return $http.post(foldersUrl, folderDetail).then(successfn);
        }
      },

      deleteFolder: function(folderId) {
        return $http.delete(foldersUrl + folderId).then(successfn);
      },

      getAuditLogs: function(queryId, startAt, maxRecs) {
        return $http.get(savedQueriesUrl + queryId + "/audit-logs" + '?start=' + startAt + '&max=' + maxRecs).then(successfn);
      },

      getAllAuditLogs: function(type, countReq, startAt, maxRecs) {
        if (!countReq) { countReq = false; }
        return $http.get(auditLogsUrl + '?type=' + type + '&countReq=' + countReq + '&start=' + startAt + '&max=' + maxRecs).then(successfn);
      },

      getAuditLog: function(auditLogId) {
        return $http.get(auditLogsUrl + auditLogId).then(successfn);
      }
    };
  })
  .factory('UsersService', function($http) {
    var baseUrl         = '/catissuecore/rest/ng/users/';
    var successfn = function(result) { return result.data; };

    return {
      getAllUsers: function() {
        return $http.get(baseUrl).then(successfn); 
      }
    };
  });
