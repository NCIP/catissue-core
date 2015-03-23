
angular.module('os.query.util', [])
  .factory('QueryUtil',function() {
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

    return {
      flattenStaticFields: flattenStaticFields, 

      flattenExtnFields: flattenExtnFields, 

      getExtnForms: getExtnForms 
    }
  });
