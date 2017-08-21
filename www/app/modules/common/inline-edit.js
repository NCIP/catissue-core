
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
  .directive('osInlineEdit', function($timeout, $document) {
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
              onClickListener();
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
              $document.off('click', onClick);
            };

            var clickedOnEdit;
            function onClickListener() {
              clickedOnEdit = true;
              $document.on('click', onClick);
            }

            function onClick(event) {
              var clickedEle = angular.element(event.target);
              if (!clickedOnEdit && !clickedEle.parents('div.os-inline-editable').length > 0) {
                $document.off('click', onClick);

                // If clicked on action buttons no need to cancel. Action buttons itself will do that.
                if (!(clickedEle.parents('div.action-btns').length > 0)) {
                  $timeout(function() {
                    scope.cancel();
                  }, 0);
                }
              }
              clickedOnEdit = false;
            }
          }
        }
      },

      templateUrl: 'modules/common/inline-edit-template.html'
    };
  });
