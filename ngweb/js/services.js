
angular.module('plus.services', [])
  .factory('QueryService', function($http, $document) {
    var baseUrl         = '/openspecimen/rest/ng/query/';
    var savedQueriesUrl = '/openspecimen/rest/ng/saved-queries/';
    var foldersUrl      = '/openspecimen/rest/ng/query-folders/';
    var auditLogsUrl    = '/openspecimen/rest/ng/query-audit-logs/';

    var successfn = function(result) { return result.data; };

    return {
      executeQuery: function(id, cpId, drivingForm, aql, runType, wideRows) {
        var req = {indexOf: 'Specimen.label', savedQueryId: id, cpId: cpId, drivingForm: drivingForm, aql: aql, runType: runType, wideRows: wideRows};
        return $http.post(baseUrl, req).then(successfn);
      },

      exportQueryData: function(id, cpId, drivingForm, aql, runType, wideRows) {
        var req = {savedQueryId: id, cpId: cpId, drivingForm: drivingForm, aql: aql, runType: runType, wideRows: wideRows};
        return $http.post(baseUrl + 'export', req).then(successfn);
      },

      downloadQueryData: function(fileId) {
        var link = angular.element('<a/>')
          .attr({
            href: '/openspecimen/rest/ng/query/export?fileId=' + fileId, 
            target: '_blank'});

        angular.element($document[0].body).append(link);

        if (typeof link[0].click == "function") {
          link[0].click();
        } else { // Safari fix
          var dispatch = document.createEvent("HTMLEvents");
          dispatch.initEvent("click", true, true);
          link[0].dispatchEvent(dispatch);
        }
        link.remove();
      },

      saveOrUpdateQuery: function(queryDef) {
        if (queryDef.id) {
          return $http.put(savedQueriesUrl + queryDef.id, queryDef).then(successfn);
        } else {
          return $http.post(savedQueriesUrl, queryDef).then(successfn);
        }
      },

      deleteQuery: function(query) {
        return $http.delete(savedQueriesUrl + query.id).then(successfn);
      },

      getQueries: function(countReq, startAt, maxRecs, searchString) {
        if (!countReq) { countReq = false; }
          var params = {
            countReq : countReq, 
            start : startAt,
            max : maxRecs,
            searchString: searchString,
      	  '_reqTime' : new Date().getTime()
        }
        return Utility.get($http, savedQueriesUrl, successfn, params);
      },

      getQuery: function(queryId) {
        return Utility.get($http, savedQueriesUrl + queryId, successfn);
      },

      getFolder: function(folderId) {
        return Utility.get($http, foldersUrl + folderId, successfn);
      },

      getFolders: function() {
        return Utility.get($http, foldersUrl, successfn);
      },

      getFolderQueries: function(folderId, countReq, startAt, maxRecs, searchString) {
        if (!countReq) { countReq = false; }
        var params = {
          countReq : countReq, 
          start : startAt,
          max : maxRecs,
          searchString: searchString,
          '_reqTime' : new Date().getTime()
        }
        return Utility.get($http, foldersUrl + folderId + "/saved-queries", successfn, params);
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
        return Utility.get($http, savedQueriesUrl + queryId + "/audit-logs", successfn);
      },

      getAllAuditLogs: function(type, countReq, startAt, maxRecs) {
        if (!countReq) { countReq = false; }
        
        var params = {
          countReq : countReq, 
          start : startAt,
          max : maxRecs,
          type: type,
          '_reqTime' : new Date().getTime()
       	}
        return Utility.get($http, auditLogsUrl, successfn, params);
      },

      getAuditLog: function(auditLogId) {
        return Utility.get($http, auditLogsUrl + auditLogId, successfn);
      }
    };
  })

  .factory('UsersService', function($http) {
    var baseUrl         = '/openspecimen/rest/ng/users/';
    var successfn = function(result) { return result.data; };

    return {
      getAllUsers: function(params) {
        return Utility.get($http, baseUrl, function(result) {
          if (result.data) {
            return result.data.users;
          }

          return [];
        }, params);
      }
    };
  })

  .factory('SpecimenListsService', function($http) {
    var baseUrl = '/openspecimen/rest/ng/specimen-lists/';
    var successfn = function(result) { return result.data; };

    return {
      getSpecimenLists: function() {
        return Utility.get($http, baseUrl, successfn);
      },

      addSpecimensToList: function(listId, specimens) {
        return $http.put(baseUrl + listId + '/specimens?operation=ADD', specimens).then(successfn);
      },

      saveOrUpdate: function(list) {
        if (list.id) {
          return $http.put(baseUrl + list.id, list).then(successfn);
        } else {
          return $http.post(baseUrl, list).then(successfn);
        }
      }
    };
  });
