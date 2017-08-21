angular.module('openspecimen')
  .directive('osUniqueItems', function() {
    return {
      controller: function() {
        this.ctrls = {};

        this.addCtrl = function(ctrl) {
          var fieldCtrls = this.ctrls[ctrl.$name];
          if (!fieldCtrls) {
            fieldCtrls = [];
            this.ctrls[ctrl.$name] = fieldCtrls;
          }


          if (fieldCtrls.indexOf(ctrl) == -1) {
            fieldCtrls.push(ctrl);
          }
        }

        this.removeCtrl = function(formCtrl, modelCtrl) {
          var fieldCtrls = this.ctrls[modelCtrl.$name];
          if (!fieldCtrls) {
            return;
          }

          var idx = fieldCtrls.indexOf(modelCtrl);
          if (idx == -1) {
            return;
          }

          fieldCtrls.splice(idx, 1);

          for (var k in this.ctrls) {
            fieldCtrls = this.ctrls[k];
            angular.forEach(this.ctrls[k], function(ctrl) {
              ctrl.$validate();
            });
          }
        }

        this.isUnique = function(ctrl) {
          var fieldCtrls = this.ctrls[ctrl.$name];
     
          for (var i = 0; i < fieldCtrls.length; ++i) {
            if (ctrl == fieldCtrls[i]) {
              continue;
            }

            if (ctrl.$viewValue == fieldCtrls[i].$viewValue) {
              return false;
            }
          }

          return true;
        }

        this.checkUniqueness = function(ctrl) {
          var fieldCtrls = this.ctrls[ctrl.$name];
 
          var values = [];
          for (var i = 0; i < fieldCtrls.length; ++i) {
            var value = fieldCtrls[i].$viewValue;
            if (!value) {
              continue;
            }

            if (values.indexOf(value) != -1) {
              fieldCtrls[i].$setValidity("dupItem", false);
            } else {
              values.push(value);
              fieldCtrls[i].$setValidity("dupItem", true);
            }
          }
        }
      }
    }
  })
  .directive('osUniqueItem', function() {
    return {
      require: ['^form', 'ngModel', '^osUniqueItems'],
  
      link: function(scope, elm, attrs, ctrls) {
        //
        // Workaround to ignore select container/div that have ng-model
        // associated with them...
        //
        if (elm[0].tagName == 'DIV' && !elm[0].firstElementChild.classList.contains('ui-select-container')) {
          return;
        }

        var formCtrl    = ctrls[0];
        var modelCtrl   = ctrls[1];
        var uqItemsCtrl = ctrls[2];

        uqItemsCtrl.addCtrl(modelCtrl);
        modelCtrl.$validators.dupItem = function(modelValue, viewValue) {
          if (modelCtrl.$isEmpty(modelValue)) {
            return true;
          }

          uqItemsCtrl.checkUniqueness(modelCtrl);
          return !modelCtrl.$error['dupItem'];
        }

        elm.on('$destroy', function() {
          uqItemsCtrl.removeCtrl(formCtrl, modelCtrl);
        });
      }
    };
  });
