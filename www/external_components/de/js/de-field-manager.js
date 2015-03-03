var edu = edu || {};
edu.common = edu.common || {};
edu.common.de = edu.common.de || {};

edu.common.de.FieldManager =
  (function() {
    var instance;

    function init() {
      var fieldMap = {};

      var register = function(args) {
        if (!args.name) {
          throw "Field name is either empty or undefined";
        }

        if (!args.displayName) {
          throw "Field display name is either empty or undefined";
        }

        if (!args.fieldCtor) {
          throw "Field initialization function undefined";
        }

        fieldMap[args.name] = args;
      };

      var getField = function(name, params) {
        var fieldDetail = fieldMap[name];
        if (!fieldDetail) {
          return null;
        }

        return new fieldDetail.fieldCtor(params);
      };

      var getFieldList = function() {
        var result = [];
        for (var k in fieldMap) {
          var fieldDetail = fieldMap[k];
          result.push({name: fieldDetail.name, displayName: fieldDetail.displayName});
        }

        return result;
      };

      return {
        register: register,
        getField: getField,
        getFieldList: getFieldList
      };      
    };

    return {
      getInstance: function() {
        if (!instance) {
          instance = init();
        }

        return instance;
      }
    };
  })();
