
angular.module('plus.formDataServices', [])
  .factory('FormDataService', function($http) {
    var baseUrl = '/catissuecore/rest/';
      return {
	    getForms: function(appData) {
          var queryParams = "";
          for (var key in appData) {
            queryParams += key + "=" + appData[key] + "&";
          }
	      return $http.get(baseUrl + "forms?"+queryParams).then(function(result) { return result.data; });
	    },
	    getFormDef: function(formId) {
		   return $http.get(baseUrl + "forms/"+formId).then(function(result) { return result.data; });
		},
	    
	    getRecords: function(appData, formId) {
	      var queryParams = "";
	      for (var key in appData) {
	        queryParams += key + "=" + appData[key] + "&";
	      }
	      return $http.get(baseUrl + "forms/"+formId+"/records?"+queryParams).then(function(result) { return result.data; });
	    }
      }
  });
