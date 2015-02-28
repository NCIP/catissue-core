
angular.module('os.biospecimen.models.form', ['os.common.models'])
  .factory('Form', function(osModel, $http, $q) {
    var Form = osModel('forms');

    Form.getDefinition = function(formId) {
      return $http.get(Form.url() + formId + '/definition').then(
        function(result) {
          return result.data;
        }
      );
    };

    Form.listFor = function(url, objectId) {
      var result = [];
      $http.get(url + objectId + '/forms').then(
        function(resp) {
          Form._lazyCollectionInit(resp.data, result);
        }
      );

      return result;
    };

    Form.listRecords = function(url, objectId, formCtxtId) {
      var result = {records: []};
      $http.get(url + objectId + '/forms/' + formCtxtId + '/records').then(
        function(resp) {
          angular.extend(result, resp.data);
        }
      );

      return result;
    };

    return Form;
  });
