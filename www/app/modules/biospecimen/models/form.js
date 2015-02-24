
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

    return Form;
  });
