
angular.module('openspecimen')
  .directive('osInlineEditors', function() {
    return {
      restrict: 'A',
      scope: {
        onSave: '&'
      },
      controller: function($scope) {
        this.editors = {};

        this.addEditor = function(editor) {
          this.editors[editor.name] = editor;
          editor.setMgr(this);
        }

        this.save = function(editor) {
          return $scope.onSave()(editor.name, editor.editValue);
        }

        this.open = function(editor) {
          if (this.openedEditor) {
            if (!this.openedEditor.isSaving()) {
              this.openedEditor.close();
            } else {
              return;
            }
          }

          editor.open();
          this.openedEditor = editor;
        };

        this.close = function(editor) {
          if (this.openedEditor === editor) {
            this.openedEditor.close();
            this.openedEditor = undefined;
          } else {
            alert("Something wrong");
          }
        };
      }
    };
  })
  .directive('osInlineEdit', function($timeout) {
    return {
      restrict: 'E',
      require: ['^osInlineEditors', 'osInlineEdit'],
      transclude: true,
      replace: true,
      scope: {
        editor: '=',
        name: '@',
        viewValue: '=value'
      },
      controller: function($scope) {
        $scope.enabled = false;

        this.name = $scope.name;

        this.isEditMode = function() {
          return $scope.enabled;
        };

        this.open = function() {
          this.editValue = angular.copy($scope.viewValue);
          $scope.enabled = true;
        };

        this.isDisplayMode = function() {
          return !$scope.enabled;
        };

        this.close = function() {
          $scope.enabled = false;
        };

        this.setMgr = function(mgr) {
          this.mgr = mgr;
        };

        this.startSaving = function() {
          $scope.saving = true;
        };

        this.stopSaving = function() {
          $scope.saving = false;
        };

        this.isSaving = function() {
          return $scope.saving;
        }
      },
      compile: function(element, atts, transclude) {
        return {
          pre: function(scope, element, attrs, ctrls) {
            var thisEditor = ctrls[1];
            scope.editor = thisEditor;
            thisEditor.editValue = thisEditor.viewValue = scope.viewValue;
          },

          post: function(scope, element, attrs, ctrls) {
            var editorMgr = ctrls[0];
            var thisEditor = ctrls[1];

            editorMgr.addEditor(thisEditor);

            scope.edit = function() {
              editorMgr.open(thisEditor);
            };

            scope.ok = function() {
              thisEditor.startSaving();
              editorMgr.save(thisEditor).then(
                function() {
                  $timeout(function() { 
                    editorMgr.close(thisEditor);
                    thisEditor.stopSaving();
                    scope.viewValue = thisEditor.viewValue = thisEditor.editValue;
                  }, 1000);
                }
              );
            };

            scope.cancel = function() {
              editorMgr.close(thisEditor);
            };
          }
        }
      },
      template:
        '<div class="os-inline-editable" ng-class="{\'inactive\': !enabled, \'active\': enabled}">' +
          '<ng-transclude ng-if="!enabled"></ng-transclude>' + 
          '<button ng-if="!enabled" class="btn" ng-click="edit()"><span class="glyphicon glyphicon-pencil"></span></button>' +
          '<form ng-if="enabled" name="inlineForm">' +
            '<fieldset ng-disabled="saving">' +
              '<ng-transclude></ng-transclude>' +
            '</fieldset>' +
            '<div ng-if="saving" class="saving">' +
              '<i ng-if="saving" class="fa fa-spin fa-cog"></i>' +
            '</div>' +
            '<div ng-if="enabled && !saving" class="action-btns">'  +
              '<button ng-click="ok()" ng-disabled="inlineForm.$invalid"><span class="glyphicon glyphicon-ok"></span></button>' +
              '<button ng-click="cancel()"><span class="glyphicon glyphicon-remove"></span></button>' +
            '</div>' +
          '</form>' + 
        '</div>'
    };
  });
