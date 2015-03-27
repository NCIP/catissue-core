
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

    Form.listQueryForms = function() {
      return $http.get(Form.url(), {params: {formType: 'query'}}).then(
        function(result) {
          return result.data.map(function(form) {
            form.id = form.formId;
            return new Form(form);
          });
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

    Form.prototype.getFields = function() {
      var cpId = -1;
      if (!!this.cp) {
        cpId = this.cp.id;
      }

      var params = {cpId: cpId, extendedFields: true};
      var d = $q.defer();
      var that = this;
      if (this.fields) {
        d.resolve(this.fields);
      } else {
        $http.get(Form.url() + this.$id() + '/fields', {params: params}).then(
          function(resp) {
            that.fields = resp.data;
            that.staticFields = flattenStaticFields("", that.fields);
            that.extnForms = getExtnForms("", that.fields);
            that.extnFields = flattenExtnFields(that.extnForms);

            d.resolve(that.fields);
            return resp.data;
          }
        );
      }

      return d.promise;
    }

    Form.prototype.getField = function(fieldName) {
      for (var i = 0; i < this.staticFields.length; ++i) {
        if (fieldName == this.staticFields[i].name) {
          return this.staticFields[i];
        }
      }

      for (var i = 0; i < this.extnFields.length; ++i) {
        if (fieldName == this.extnFields[i].name) {
          return this.extnFields[i];
        }
      }
 
      return undefined;
    }

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

    function flattenFields(fqn, fields) {
      var result = [];
      angular.forEach(fields, function(field) {
        if (field.type == 'SUBFORM') {
          result = result.concat(flattenFields(fqn + field.name + '.', field.subFields));
        } else {
          var f = angular.extend({}, field);
          f.name = fqn + field.name;
          result.push(f);
        }
      });

      return result;
    };

    function filterAndFlattenFields(fqn, fields, filterfn) {
      var result = [];
      angular.forEach(fields, function(field) {
        if (filterfn(field)) {
          result.push(field);
        }
      });

      return flattenFields(fqn, result);
    };
      
    function flattenStaticFields(fqn, fields) {
      return filterAndFlattenFields(
        fqn, 
        fields, 
        function(field) {
          return field.type != 'SUBFORM' || field.name != 'extensions';
        });
    };

    function flattenExtnFields(extnForms) {
      var fields = [];
      angular.forEach(extnForms, function(extnForm) {
        fields = fields.concat(extnForm.fields);
      });

      return fields;
    };

    function getExtnForms(fqn, fields) {
      var extnSubForm = undefined;
      for (var i = 0; i < fields.length; ++i) {
        if (fields[i].type != 'SUBFORM' || fields[i].name != 'extensions') {
          continue;
        }

        extnSubForm = fields[i];
        break;
      }

      if (!extnSubForm) {
        return [];
      }

      var extnForms = [];
      for (var i = 0; i < extnSubForm.subFields.length; ++i) {
        var subForm = extnSubForm.subFields[i];
        var extnFields = flattenFields(fqn + "extensions." + subForm.name + ".", subForm.subFields);
        for (var j = 0; j < extnFields.length; ++j) {
          extnFields[j].extensionForm = subForm.caption;
        }
        extnForms.push({name: subForm.name, caption: subForm.caption, fields: extnFields});
      }

      return extnForms;
    };

    return Form;
  });
