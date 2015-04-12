
angular.module('openspecimen')
  .directive('osTree', function() {
    return {
      restrict: 'E',

      replace: true,

      link: function(scope, element, attrs) {
        scope.$watch(
          attrs.opts, 
          function(newOpts) {
            scope.opts = newOpts;
          }
        );
      },

      controller: function($scope) {
        this.nodeChecked = function(node) {
          if (typeof $scope.opts.nodeChecked == "function") {
            $scope.opts.nodeChecked(node);
          }
        };

        this.onNodeToggle = function(node) {
          node.expanded = !node.expanded;
          if (typeof $scope.opts.toggleNode == "function") {
            $scope.opts.toggleNode(node);
          }
        };

        this.isNodeChecked = function(node) {
          if (node.checked) {
            return true;
          }

          if (!node.children) {
            return false;
          }

          for (var i = 0; i < node.children.length; ++i) {
            if (this.isNodeChecked(node.children[i])) {
              return true;
            }
          }

          return false;
        }
      },

      template:
        '<div class="os-tree">' +
        '  <ul ui-sortable ng-model="opts.treeData"> ' +
        '    <os-tree-node ng-repeat="node in opts.treeData" node="node" node-tmpl="opts.nodeTmpl"> ' +
        '    </os-tree-node> ' + 
        '  </ul> ' +
        '</div>'
    };
  })

  .directive('osTreeNode', function($compile) {
    return {
      restrict: 'E',
      require: '^osTree',
      replace: 'true',
      scope: {
        node: '=',
        nodeTmpl: '='
      },
      link: function(scope, element, attrs, ctrl) {
        element.append(
          $compile(
            '<ul ui-sortable ng-model="node.children" ng-if="node.expanded"> ' +
              '  <os-tree-node node-tmpl="nodeTmpl" ng-repeat="child in node.children" node="child"> ' +
              '  </os-tree-node> ' +
            '</ul>'
          )(scope)
        );

        scope.nodeChecked = ctrl.nodeChecked;
        scope.onNodeToggle = ctrl.onNodeToggle;
        scope.isNodeChecked = ctrl.isNodeChecked;
      },

      template:
        '<li>' +
        '  <div class="clearfix os-tree-node" ' +
        '    ng-class="{\'os-tree-node-offset\': node.children && node.children.length <= 0}" ' +
        '    ng-mouseenter="hover=true" ng-mouseleave="hover=false"> ' +
        '    <div ng-if="!node.children || node.children.length > 0" ' +
        '         class="os-tree-node-toggle-marker fa" ' +
        '         ng-class="{true: \'fa-plus-square-o\', false: \'fa-minus-square-o\'}[!node.expanded]" ' +
        '         ng-click="onNodeToggle(node)"> ' +
        '    </div> ' +
        '    <div class="os-tree-node-checkbox"> ' +
        '      <input type="checkbox" ng-model="node.checked" ng-checked="isNodeChecked(node)" ' +
        '        ng-change="nodeChecked(node)"> ' +
        '    </div> ' +
        '    <div class="os-tree-node-label"> ' +
        '      <div ng-if="nodeTmpl" ng-include src="nodeTmpl"></div> ' +
        '      <div ng-if="!nodeTmpl">{{node.val}}</div> ' +
        '    </div> ' +
        '  </div> ' +
        '</li>'
    };
  });
