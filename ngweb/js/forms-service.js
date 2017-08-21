
angular.module('plus.formsServices', [])
  .factory('FormsService', function($http) {
    var apiUrl = '/openspecimen/rest/ng/';

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
        return Utility.get($http, baseUrl, successfn);
      },
      
      getAllSpecimenEventForms: function() {
    	var params = {
          formType : 'specimenEvent',
    	  '_reqTime' : new Date().getTime()
    	}
        return Utility.get($http, baseUrl, successfn, params);
      },

      getQueryForms: function() {
    	var params = {
          formType : 'query',
    	  '_reqTime' : new Date().getTime()
    	}
        return Utility.get($http, baseUrl, successfn, params);
      },

      getFormDef: function(formId) {
    	var url = baseUrl + "/" + formId + "/definition";
        return Utility.get($http, url, successfn);
      },

      deleteForm: function(formId) {
        var url = baseUrl + "/" + formId;
        return $http.delete(url).then(successfn);
      },

      getFormFields: function(formId) {
        var url = baseUrl + "/" + formId + "/fields";
        return Utility.get($http, url, successfn);
      },

      getQueryFormFields: function(cpId, formId) {
        var url = baseUrl + "/" + formId + "/fields";
        var params = {
          formType : 'query',
          extendedFields : true,
          cpId : cpId,
          '_reqTime' : new Date().getTime()
        }
        return Utility.get($http, url, successfn, params);
      },

      getFormData: function(formId, recordId) {
        var url = baseUrl + "/" + formId + "/data/" + recordId;
        return Utility.get($http, url, successfn);
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
        var url = baseUrl + "/" + formId + "/contexts";
        return Utility.get($http, url, successfn);
      },

      addFormContexts: function(formId, cpIds, entity, isMultiRecord) {
        var formCtxts = [];
        for (var i = 0; i < cpIds.length; ++i) {
          formCtxts.push({formId: formId, collectionProtocol: {id: cpIds[i]}, level: entity, multiRecord: isMultiRecord});
        }

        return $http.put(baseUrl + "/" + formId + "/contexts", formCtxts).then(successfn);
      },

      getCprForms: function(cprId) {
        return Utility.get($http, getFormsUrl('collection-protocol-registrations', cprId), successfn);
      },

      getSpecimenForms: function(specimenId) {
        return Utility.get($http, getFormsUrl('specimens', specimenId), successfn);
      },
      
      getSpecimenEventForms: function(specimenId){
          return Utility.get($http, getFormsUrl('specimen-events', specimenId), successfn);
      },

      getScgForms: function(scgId) {
        return Utility.get($http, getFormsUrl('specimen-collection-groups', scgId), successfn);
      },

      getCprFormRecords: function(cprId, formCtxtId) {
        return Utility.get($http, getRecordsUrl('collection-protocol-registrations', cprId, formCtxtId), successfn);
      },

      getSpecimenFormRecords: function(specimenId, formCtxtId) {
        return Utility.get($http, getRecordsUrl('specimens', specimenId, formCtxtId), successfn);
      },

      getScgFormRecords: function(scgId, formCtxtId) {
        var params = {
          '_reqTime' : new Date().getTime()
        }
        return Utility.get($http, getRecordsUrl('specimen-collection-groups', scgId, formCtxtId), successfn);
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
    var baseUrl = '/openspecimen/rest/ng/collection-protocols';
    var successfn = function(result) { return result.data; };

    return {
      getCpList: function() {
        return Utility.get($http, baseUrl+"?chkPrivilege=false", successfn);
      }
    }
  });
