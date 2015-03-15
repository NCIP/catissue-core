
angular.module('os.biospecimen.models.form', ['os.common.models'])
  .factory('Form', function(osModel, $http, $q, Util) {
    var Form = osModel('forms');

    Form.getDefinition = function(formId) {
      return $http.get(Form.url() + formId + '/definition').then(
        function(result) {
          return result.data;
        }
      );
    };

    Form.listFor = function(url, objectId, params) {
      var result = [];
    
      $http.get(url + objectId + '/forms', {params: params}).then(
        function(resp) {
          var opts = angular.extend({objectId: objectId}, params);
          angular.forEach(resp.data, function(form) {
            form.id = form.formId;
            result.push(new Form(angular.extend(form, opts)));
          });
        }
      );

      return result;
    };

    Form.listRecords = function(url) {
      var records = [];
      $http.get(url).then(
        function(resp) {
          Util.unshiftAll(records, createRecordsList(resp.data));
        }
      );

      return records;
    };

    Form.prototype.getRecords = function() {
      var result = [];
      var params = {objectId: this.objectId, entityType: this.entityType};
      $http.get(Form.url() + this.$id() + '/records', {params: params}).then(
        function(resp) {
          Util.unshiftAll(result, resp.data[0].records);
        }
      );

      return result;
    };

    function createRecordsList(formsRecords) {
      var recordsList = [];
      angular.forEach(formsRecords, function(formRecs) {
        angular.forEach(formRecs.records, function(record) {
          record.formCaption = formRecs.caption;
          record.formId = formRecs.id;
          recordsList.push(record);
        });
      });

      return recordsList.sort(function(rec1, rec2) {
        return +rec2.updateTime - +rec1.updateTime;
      });
    };

    return Form;
  });
