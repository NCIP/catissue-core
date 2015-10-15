
angular.module('openspecimen')
  .directive('osTabs', function() {
    return {
      restrict: 'E',

      transclude: true,

      controller: function($scope) {
        this.selected = 0;

        $scope.tabs = [];

        this.addTab = function(tab) {
          angular.extend(tab, {selected: false});
          $scope.tabs.push(tab);
        }

        this.selectTab = function($index) {
          if (this.selected == $index) {
            return;
          }

          angular.extend($scope.tabs[this.selected], {selected: false});

          this.selected = $index;
          angular.extend($scope.tabs[this.selected], {selected: true});
        }
      },

      link: function(scope, element, attrs, ctrl) {
        scope.tabs[ctrl.selected].selected = true;

        scope.selectTab = function(tab) {
          ctrl.selectTab(tab);
        }
      },

      templateUrl: 'modules/common/tabs.html'
    };
  })

  .directive('osTab', function() {
    return {
      restrict: 'E',
      require : '^osTabs',
      template: '<div ng-transclude ng-show="selected"></div>',
      transclude: true,
      replace : true,
      scope : {
        title : '@'
      },

      link: function(scope, element, attrs, tabsCtrl) {
        tabsCtrl.addTab(scope);
      }
    };
  });
