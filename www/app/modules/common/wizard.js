
angular.module('openspecimen')
  .directive('osWizard', function($q) {
    return {
      restrict: 'A',

      transclude: true,

      controller: function($scope) {
        $scope.ctrl = this;
        $scope.steps = [];

        this.addStep = function(step) {
          angular.extend(step, {selected: $scope.steps.length == 0, finished: false});
          $scope.steps.push(step);
        };

        this.previous = function() {
          this.forward = false;
          if ($scope.selectedStep == 0) {
            return false;
          }

          var onFinish = $scope.steps[$scope.selectedStep].onFinish();
          if (!onFinish) {
            onFinish = function() { return true; };
          }

          return $q.when(onFinish(this.forward)).then(
            function(result) {
              if (!result) {
                return false;
              }

              $scope.selectedStep--;
              $scope.steps[$scope.selectedStep + 1].selected = false;
              $scope.steps[$scope.selectedStep].selected = true;
              $scope.steps[$scope.selectedStep].finished = false;
              return true;
            }
          );
        };

        this.next = function() {
          this.forward = true;

          var onFinish = $scope.steps[$scope.selectedStep].onFinish();
          if (!onFinish) {
            onFinish = function() { return true; };
          }
       
          return $q.when(onFinish(this.forward)).then(
            function(result) {
              if (!result) {
                return false;
              }

              if ($scope.selectedStep == $scope.steps.length - 1) {
                return true; // should it return true or false; // earlier it was false
              }

              $scope.selectedStep++;
              $scope.steps[$scope.selectedStep - 1].finished = true;
              $scope.steps[$scope.selectedStep - 1].selected = false;
              $scope.steps[$scope.selectedStep].selected= true;
              return true;
            }
          );
        };

        this.getCurrentStep = function() {
          return $scope.selectedStep ;
        };


        var stepThrough = function(current, numSteps, fn) {
          if (current == numSteps) {
            return;
          }

          $q.when(fn()).then(
            function(result) {
              if (result) {
                stepThrough(current + 1, numSteps, fn);
              }
            }
          );
        };

        $scope.gotoStep = function(step) {
          if ($scope.selectedStep == step) {
            return;
          }

          var fn = undefined;
          var numSteps = undefined;
          if (step > $scope.selectedStep) {
            numSteps = step - $scope.selectedStep;
            fn = $scope.ctrl.next;
          } else {
            numSteps = $scope.selectedStep - step;
            fn = $scope.ctrl.previous;
          }

          stepThrough(0, numSteps, fn);
        };

        this.isFirstStep = function() {
          return $scope.selectedStep == 0;
        };

        this.isLastStep = function() {
          return $scope.selectedStep == $scope.steps.length - 1;
        };
      },

      compile: function(element, attributes, transclude) {
        return {
          pre: function (scope, element, attrs, controller) {
            scope[attrs.osWizard] = controller;
          },

          post: function (scope, element, attributes, controller) {
            scope.selectedStep = 0;
            if (scope.steps.length > 0) {
              scope.steps[0].selected = true;
            }
          }
        }
      },

      templateUrl: function(tElem, tAttrs) {
        if (tAttrs.type == 'classic') {
          return 'modules/common/classic-wizard-template.html';
        } else if (tAttrs.type == 'vertical') {
          return 'modules/common/vertical-wizard-template.html';
        }

        return 'modules/common/tube-wizard-template.html';
      }
    };
  })

  .directive('osWizardStep', function() {
    return {
      restrict: 'E',
      require : '^osWizard',
      template: '<div ng-transclude class="step-content" ng-show="selected"></div>',
      transclude: true,
      replace : true,
      scope : {
        title : '@',
        desc  : '@',
        onFinish : '&'
      },

      link: function(scope, element, attrs, wizardCtrl) {
        wizardCtrl.addStep(scope);
      }
    };
  });
