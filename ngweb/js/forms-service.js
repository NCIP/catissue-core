
angular.module('plus.formsServices', [])
  .factory('FormsService', function($http) {
    var apiUrl = '/catissuecore/rest/ng/';

    var baseUrl = apiUrl + 'forms/';

    var successfn = function(result) { return result.data; };

    var getFormsUrl = function(resource, resourceId) {
      return apiUrl + '/' + resource + '/' + resourceId + '/forms';
    };

    var getRecordsUrl = function(resource, resourceId, formCtxtId) {
      return getFormsUrl(resource, resourceId) + '/' + formCtxtId + '/records';
    };

    return {
      getAllForms: function() {
        var params = {
          '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: baseUrl, params: params}).then(successfn);
      },

      getQueryForms: function() {
    	var params = {
          formType : 'query',
    	  '_reqTime' : new Date().getTime()
    	}
    	return $http({method: 'GET', url: baseUrl , params: params}).then(successfn);

      },

      getFormDef: function(formId) {
    	var url = baseUrl + "/" + formId + "/definition";
    	var params = {
    	  '_reqTime' : new Date().getTime()
    	}
        return $http({method: 'GET', url: url, params: params}).then(successfn);

      },

      getFormFields: function(formId) {
        var url = baseUrl + "/" + formId + "/fields";
        var params = {
    	  '_reqTime' : new Date().getTime()
    	}
        return $http({method: 'GET', url: url, params: params}).then(successfn);
      },

      getQueryFormFields: function(cpId, formId) {
        var url = baseUrl + "/" + formId + "/fields";
        var params = {
          formType : 'query',
          extendedFields : true,
          cpId : cpId,
          '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: url, params: params}).then(successfn);
      },

      getFormData: function(formId, recordId) {
        var params = {
          '_reqTime' : new Date().getTime()
        }    	  
        var url = baseUrl + "/" + formId + "/data/" + recordId;
        return $http({method: 'GET', url: url, params: params}).then(successfn);
      },

      saveFormData: function(formId, recordId, formDataJson) {
        var url = baseUrl + "/" + formId + "/data/";
        var ret = undefined;
        if (recordId) {
          ret = $http.put(url + recordId, formDataJson).then(successfn);
        } else {
          ret = $http.post(url, formDataJson).then(successfn);
        }

        return ret;
      },

      getFormContexts: function(formId) {
        var params = {
          '_reqTime' : new Date().getTime()
        }
        var url = baseUrl + "/" + formId + "/contexts";
        return $http({method: 'GET', url: url, params: params}).then(successfn);
      },

      addFormContexts: function(formId, cpIds, entity, isMultiRecord) {
        var formCtxts = [];
        for (var i = 0; i < cpIds.length; ++i) {
          formCtxts.push({formId: formId, collectionProtocol: {id: cpIds[i]}, level: entity, multiRecord: isMultiRecord});
        }

        return $http.put(baseUrl + "/" + formId + "/contexts", formCtxts).then(successfn);
      },

      getCprForms: function(cprId) {
        var params = {
          '_reqTime' : new Date().getTime()
        }
        
        return $http({method: 'GET', url: getFormsUrl('collection-protocol-registrations', cprId), params: params}).then(successfn);
      },

      getSpecimenForms: function(specimenId) {
        var params = {
          '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: getFormsUrl('specimens', specimenId), params: params}).then(successfn);

      },

      getScgForms: function(scgId) {
        var params = {
           '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: getFormsUrl('specimen-collection-groups', scgId), params: params}).then(successfn);

      },

      getCprFormRecords: function(cprId, formCtxtId) {
        var params = {
          '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: getRecordsUrl('collection-protocol-registrations', cprId, formCtxtId), params: params}).then(successfn);
      },

      getSpecimenFormRecords: function(specimenId, formCtxtId) {
        var params = {
          '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: getRecordsUrl('specimens', specimenId, formCtxtId), params: params}).then(successfn);

      },

      getScgFormRecords: function(scgId, formCtxtId) {
        var params = {
          '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: getRecordsUrl('specimen-collection-groups', scgId, formCtxtId), params: params}).then(successfn);
      },
      
      deleteRecords: function(formId, recIds) {
        var delUrl = baseUrl + "/" + formId + "/data";
        return  $http({method: 'DELETE', url: delUrl, data: recIds, 
        		  headers: {'Content-Type': 'application/json;charset=utf-8'}}).then(successfn);
      }
    };
  })
  
  /** Need to merge this with rest of services **/
  .factory('CollectionProtocolService', function($http) {
    var baseUrl = '/catissuecore/rest/ng/collection-protocols/';

    return {
      getCpList: function() {
        var params = {
          '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: baseUrl, params: params}).then(function(result) { return result.data; });

      }
    }
  });
