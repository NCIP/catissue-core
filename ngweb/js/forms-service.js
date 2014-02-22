
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
        return $http.get(baseUrl).then(successfn);
      },

      getQueryForms: function() {
        return $http.get(baseUrl + '?formType=query').then(successfn);
      },

      getFormDef: function(formId) {
        return $http.get(baseUrl + "/" + formId + "/definition").then(successfn);
      },

      getFormFields: function(formId) {
        return $http.get(baseUrl + "/" + formId + "/fields").then(successfn);
      },

      getFormData: function(formId, recordId) {
        return $http.get(baseUrl + "/" + formId + "/data/" + recordId).then(successfn); 
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
        return $http.get(baseUrl + "/" + formId + "/contexts").then(successfn);
      },

      addFormContexts: function(formId, cpIds, entity) {
        var formCtxts = [];
        for (var i = 0; i < cpIds.length; ++i) {
          formCtxts.push({formId: formId, collectionProtocol: {id: cpIds[i]}, level: entity});
        }

        return $http.put(baseUrl + "/" + formId + "/contexts", formCtxts).then(successfn);
      },

      getCprForms: function(cprId) {
        return $http.get(getFormsUrl('collection-protocol-registrations', cprId)).then(successfn);
      },

      getSpecimenForms: function(specimenId) {
        return $http.get(getFormsUrl('specimens', specimenId)).then(successfn);
      },

      getScgForms: function(scgId) {
        return $http.get(getFormsUrl('specimen-collection-groups', scgId)).then(successfn);
      },

      getCprFormRecords: function(cprId, formCtxtId) {
        return $http.get(getRecordsUrl('collection-protocol-registrations', cprId, formCtxtId)).then(successfn);
      },

      getSpecimenFormRecords: function(specimenId, formCtxtId) {
        return $http.get(getRecordsUrl('specimens', specimenId, formCtxtId)).then(successfn);
      },

      getScgFormRecords: function(scgId, formCtxtId) {
        return $http.get(getRecordsUrl('specimen-collection-groups', scgId, formCtxtId)).then(successfn);
      }
    };
  })
  
  /** Need to merge this with rest of services **/
  .factory('CollectionProtocolService', function($http) {
    var baseUrl = '/catissuecore/rest/ng/collection-protocols/';

    return {
      getCpList: function() {
        return $http.get(baseUrl).then(function(result) { return result.data; });
      }
    }
  });
