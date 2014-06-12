
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

      getQueries: function(countReq, startAt, maxRecs) {
        if (!countReq) { countReq = false; }
          var params = {
            countReq : countReq, 
            start : startAt,
            max : maxRecs,
      	  '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: savedQueriesUrl, params: params}).then(successfn);
      },

      getQuery: function(queryId) {
        var params = {
  	      '_reqTime' : new Date().getTime()
    	}
        return $http({method: 'GET', url: savedQueriesUrl + queryId, params: params}).then(successfn);
      },

      getFolder: function(folderId) {
        var params = {
    	  '_reqTime' : new Date().getTime()
        }
    	return $http({method: 'GET', url: foldersUrl + folderId, params: params}).then(successfn);
      },

      getFolders: function() {
	    var params = {
	      '_reqTime' : new Date().getTime()
	    }
	    return $http({method: 'GET', url: foldersUrl, params: params}).then(successfn);
      },

      getFolderQueries: function(folderId, countReq, startAt, maxRecs) {
        if (!countReq) { countReq = false; }
        var params = {
          countReq : countReq, 
          start : startAt,
          max : maxRecs,
          '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: foldersUrl + folderId + "/saved-queries", params: params}).then(successfn);
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
        var params = {
    	  start : startAt,
    	  max : maxRecs,
    	  '_reqTime' : new Date().getTime()
    	}
    	return $http({method: 'GET', url: savedQueriesUrl + queryId + "/audit-logs", params: params}).then(successfn);
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
        return $http({method: 'GET', url: auditLogsUrl, params: params}).then(successfn);
      },

      getAuditLog: function(auditLogId) {
    	var params = {
    	  '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: auditLogsUrl + auditLogId, params: params}).then(successfn);
      }
    };
  })
  .factory('UsersService', function($http) {
    var baseUrl         = '/catissuecore/rest/ng/users/';
    var successfn = function(result) { return result.data; };

    return {
      getAllUsers: function() {
        var params = {
    	  '_reqTime' : new Date().getTime()
    	}
    	return $http({method: 'GET', url: baseUrl, params: params}).then(successfn);
      }
    };
  });
