angular.module('os.biospecimen.common.uniquespecimenlabel', [])
  .directive('osUniqueSpecimenLabel', function($q, Specimen) {
    //
    // Assumes CP is set in the scope
    //
    return {
      require: 'ngModel',

      link: function(scope, elm, attrs, ctrl) {
        var lastChecked = undefined;
        ctrl.$asyncValidators.uniqueSpecimenLabel = function(modelValue, viewValue) {
          if (ctrl.$pristine || ctrl.$isEmpty(modelValue) || lastChecked == modelValue) {
            return $q.when();
          }

          var def = $q.defer();
          lastChecked = modelValue;
          Specimen.isUniqueLabel(scope.cp.shortTitle, modelValue).then(
            function(result) {
              if (result) {
                def.resolve();
              } else {
                def.reject();
              }
            },

            function(result) {
              def.resolve();
            }
          );

          return def.promise;
        }
      }
    };
  });
