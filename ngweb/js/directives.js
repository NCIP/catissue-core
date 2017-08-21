
angular.module("plus.directives", [])
  .directive("kaSelect", function() { // TODO: Deprecated
    return {
      restrict: "E",
      replace: "true",
      template: "<select></select>",
      scope: {
        options: "=options",
        selected: "=selected",
        onSelect: "&onSelect",
        disabled: "=disabled"
      },

      link: function(scope, element, attrs) {
        var config = {
          options: [],
          id: attrs.optionId,
          value: attrs.optionValue,
          onSelect: function(selected) {
            scope.selected = selected;
            var fn = scope.onSelect();
            if (fn) {
              fn(selected);
            }

            if (!scope.$$phase) {
              scope.$apply();
            }
          }
        };

        scope.select = new Select2(element, config).render();
        scope.$watch('selected', function(selected) {
          scope.select.selectedOpts(selected);
        });

        scope.$watch('options', function(options) {
          scope.select.options(options).selectedOpts(scope.selected).render();
        });

        scope.$watch('disabled', function(disabled) {
          scope.select.enable(!disabled);
        });
      }
    };
  })

  .directive('kaLookup', function($timeout) { 
    var formatNames = function(format, objs) {
      var result = [];
      for (var i = 0; i < objs.length; ++i) {
        result.push(formatName(format, objs[i]));
      }

      return result;
    };

    var formatName = function(format, obj) {
      if (!obj) {
        return "";
      }

      if (obj.$kaLookupVal) {
        return obj.$kaLookupVal;
      }

      return format.replace(
        /{{(\w+)}}/g, 
        function(all, s) {
          if (obj) {
            return obj[s];
          } 

          return "undefined";
        }
      );
    };

    var render = function(scope, element, multiple) {
      var opts = scope.opts;

      element.select2('destroy');

      element.select2({
        placeholder: scope.placeholder,

        minimumInputLength: 3,

        multiple: multiple ? true : false,

        ajax: {
          url: '/openspecimen/' + opts.apiUrl,

          dataType: 'json',

          data: function(term, page) {
            var obj = {};
            obj[opts.searchTermName] = term;
            return obj;                
          },

          results: function(data, page) {
            var results = data;
            if (opts.respField) {
              results = data[opts.respField];
            }

            return {results: results};
          }
        },

        initSelection: function(el, callback) {
        },

        formatResult: function(obj) {
          return formatName(opts.resultFormat, obj);
        },

        formatSelection: function(obj) {
          return formatName(opts.resultFormat, obj);
        }                         
      }).change(function(evt) {
        $timeout(function() { 
          var val = element.select2('data');
          if (val instanceof Array) {
            scope.selectedOpt = formatNames(opts.resultFormat, val);
          } else {
            scope.selectedOpt = formatName(opts.resultFormat, val); 
          }
        });
      });

      if (scope.selectedOpt instanceof Array) {
        var items = [];
        for (var i = 0; i < scope.selectedOpt.length; ++i) {
          items.push({id: i, $kaLookupVal: scope.selectedOpt[i]});
        }

        element.select2('data', items);
      } else if (scope.selectedOpt) {
        element.select2('data', {$kaLookupVal: scope.selectedOpt});
      }
    };

    return {
      restrict: "A",

      scope: {
        opts : "=",
        selectedOpt: "=",
        placeholder: '@'
      },

      link: function(scope, element, attrs) {
        scope.$watch('opts', function(opts) {
          render(scope, element, attrs.multiple);
        });
      }
    };
  })

  .directive('kaSearch', function($timeout) { // TODO: Deprecated
    return {
      restrict: "E",
      replace : "true",
      template: "<input id='remote' type='hidden' value='val'>",
      scope   : {
        query         : "&onQuery",
        onSelect      : "&onSelect",
        initSelection : "&onInitselectionfn",
        elem          : "=ngModel"
      },

      link: function(scope, element, attrs) {
        var timeout = undefined;
        var opts = {multiple: attrs.multiple != undefined};
        control = new Select2Search(element, opts);

        scope.select = control;//new Select2Search(element);
        control
          .onQuery(function(qTerm, qCallback) {
            var timeInterval = 500;
            if (qTerm.length == 0) {
              timeInterval = 0;
            }
            if (timeout != undefined) {
              $timeout.cancel(timeout);
            }

            timeout = $timeout(function() {
              return scope.query()(undefined, qTerm, qCallback);
            }, timeInterval);
          })
          .onChange(function(option) {
            scope.elem  = option;
            scope.onSelect({selected: option});

            if (!scope.$$phase) {
              scope.$apply();
            }
          })
	  .onInitSelection(function(elem, callback) {
            scope.initSelection()(elem, callback);
          })
          .render();

        scope.$watch('elem', function(selected) {
          if (selected instanceof Array) {
            control.setValue(selected);
          } else if (!selected || !selected.id) {
            control.setValue('');
          } else {
            control.setValue(selected.id);
          }
        });
      }
    }
  })

  .directive("kaDraggable", function() {
    return {
      restrict: "A",
      link: function(scope, element, attrs) {
        var options = scope.$eval(attrs.kaDraggable);
        element.draggable(options);
      }
    };
  })

  .directive("kaDroppable", function() {
    return {
      restrict: "A",
      scope: {
        onDrop: "&"
      },
      link: function(scope, element, attrs) {
        var options = scope.$eval(attrs.kaDroppable);
        options.drop = function(event, ui) {
          var onDrop = scope.onDrop();
          if (onDrop) {
            onDrop(ui.draggable, angular.element(this));
          } else {
            alert(ui.draggable.text());
          }
        };
        element.droppable(options);
      }
    };
  })

  .directive("kaSortable", function() {
    return {
      restrict: "A",
      link: function(scope, element, attrs) {
        var opts = scope.$eval(attrs.sortOpts);
        element.sortable(opts);
      }
    };
  })

  .directive('kaDatePicker', function() {
    return {
      restrict: "A",
      link: function(scope, element, attrs) {
        var options = {format: 'mm-dd-yyyy'};
        if (attrs.kaDatePicker) {
          options = JSON.parse(attrs.kaDatePicker);
        }
        element.datepicker({format: options.format, autoclose: true});
      }
    };
  })

  .directive('kaJqGrid', function() {
    return {
      restrict: "E",
      scope: {
        config: "=",
        data: "="
      },

      link: function(scope, element, attrs) {
        var table, nameValue;

        scope.$watch('config', function (newValue) {
          element.children().empty();
          table = angular.element('<table></table>');
          element.append(table);

          var colNames = [];
          var colModel = [];

          var colDefs = newValue.colDefs;
          for (var i = 0; i < colDefs.length; ++i) {
            colNames.push(colDefs[i].caption);
            colModel.push({
              name: colDefs[i].name,
              index: colDefs[i].name,
              sorttype: colDefs[i].sorttype ? colDefs[i].sorttype : "string"
            });
          }

          nameValue = newValue.nameValue ? true : false;
          $(table).jqGrid({
            datatype: "local",
            height: newValue.height,
            colNames: colNames,
            colModel: colModel,
            caption: newValue.caption,
            autowidth: true
          });
        });

        scope.$watch('data', function (newValue, oldValue) {
          var i;
          for (i = oldValue.length - 1; i >= 0; i--) {
            $(table).jqGrid('delRowData', i);
          }
          for (i = 0; i < newValue.length; i++) {
            $(table).jqGrid('addRowData', i, newValue[i]);
          }
        });
      }
    };
  })

  .directive('kaShowOnParentFocus', function() {
    return {
      restrict: "A",
      link: function(scope, element, attrs) {
        element.parent().bind('mouseenter', function() { element.show(); });
        element.parent().bind('mouseleave', function() { element.hide(); });
      }
    };
  })

  .directive('kaCallout', function() {
    return {
      restrict: "E",
      link: function(scope, element, attrs) {
        attrs.$observe('showWhen', function(show) {
          if (show == "true") {
            element.popover().popover('show');
            if (attrs.fadeOut) {
              setTimeout(function() { console.log("timeout destroy"); element.popover('destroy'); }, parseInt(attrs.fadeOut));
            }
          } else {
            element.popover('destroy');
          }
        });
      }
    };
  })

  .directive("kaFixHeight", function() {
    return {
      restrict: "A",
      priority: 9933,
      link: function(scope, element, attrs) {
        var height = attrs.kaFixHeight;
        element.children().first().css("height", height);
      }
    }
  })

  .directive('kaPopoverTemplatePopup', ['$templateCache', '$compile', function ( $templateCache, $compile ) {
    return {
      restrict: 'EA',
      replace: true,
      scope: { title: '@', content: '@', placement: '@', animation: '&', isOpen: '&' },
      templateUrl: 'templates/popover-template.html',
      link: function(scope, iElement) {
        var content = angular.fromJson(scope.content);
        template = $templateCache.get(content.templateUrl);
        templateScope = scope;
        scopeElements = document.getElementsByClassName('ng-scope');

        angular.forEach(scopeElements, function(element) {
          var aScope = angular.element(element).scope();
          if (aScope.$id == content.scopeId) {
            templateScope = aScope;
          }
        });

        scope.infoLink = content.infoLink;
        iElement.find('div.popover-content').html($compile(template)(templateScope));
      }
    };
  }])

  .directive('kaPopoverTemplate', ['$tooltip', function($tooltip) {
    var tooltip = $tooltip('kaPopoverTemplate', 'popover', 'click');
    var linker = tooltip.compile;

    tooltip.compile = function(tElem, tAttrs) {
      return {
        'pre': function(scope, iElement, iAttrs) {
          iAttrs.$set('kaPopoverTemplate', {templateUrl: iAttrs.kaPopoverTemplate, scopeId: scope.$id, infoLink: iAttrs.infoLink});
        },
        'post': linker(tElem, tAttrs)
      };
    };

    return tooltip;
  }])

  .directive('treeView', function() {
    return {
      restrict: "E",
      replace : true,
      templateUrl: "ngweb/pages/templates/tree.html"
    }
  })

  .directive('kaTree', function() {
    return {
      restrict: 'E',
      replace: true,
      template:
        '<div class="ka-tree">' +
        '  <ul ui-sortable ng-model="opts.treeData"> ' +
        '    <ka-tree-node ng-repeat="node in opts.treeData" node="node" node-tmpl="opts.nodeTmpl"></ka-tree-node> ' + 
        '  </ul> ' +
        '</div>',

      link: function(scope, element, attrs) {
        var opts = attrs.opts;
        scope.$watch(opts, function(newOpts) {
          scope.opts = newOpts;
        }, true);
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
      }
    };
  })

  .directive('kaTreeNode', function($compile) {
    return {
      restrict: 'E',
      require: '^kaTree',
      replace: 'true',
      scope: {
        node: '=',
        nodeTmpl: '='
      },
      link: function(scope, element, attrs, ctrl) {
        element.append($compile(
          '<ul ui-sortable ng-model="node.children" ng-if="node.expanded"> ' +
          '  <ka-tree-node node-tmpl="nodeTmpl" ng-repeat="child in node.children" node="child"></ka-tree-node> ' +
          '</ul>')(scope));

        scope.nodeChecked = ctrl.nodeChecked;
        scope.onNodeToggle = ctrl.onNodeToggle;
        scope.isNodeChecked = ctrl.isNodeChecked;
      },

      template:
        '<li>' +
        '  <div class="clearfix ka-tree-node" ng-class="{\'ka-tree-node-offset\': node.children && node.children.length <= 0}" ' +
        '    ng-mouseenter="hover=true" ng-mouseleave="hover=false"> ' +
        '    <div ng-if="!node.children || node.children.length > 0" ' +
        '         class="ka-tree-node-toggle-marker fa" ' +
        '         ng-class="{true: \'fa-plus-square-o\', false: \'fa-minus-square-o\'}[!node.expanded]" ' +
        '         ng-click="onNodeToggle(node)"> ' +
        '    </div> ' +
        '    <div class="ka-tree-node-checkbox"> ' +
        '      <input type="checkbox" ng-model="node.checked" ng-checked="isNodeChecked(node)" ng-change="nodeChecked(node)"> ' +
        '    </div> ' +
        '    <div class="ka-tree-node-label"> ' +
        '      <div ng-if="nodeTmpl" ng-include src="nodeTmpl"></div> ' +
        '      <div ng-if="!nodeTmpl">{{node.val}}</div> ' +
        '    </div> ' +
        '  </div> ' +
        '</li>'
    };
  })

  .directive('uiEvent', ['$parse',
    function ($parse) {
      return function ($scope, elm, attrs) {
        var events = $scope.$eval(attrs.uiEvent);
        angular.forEach(events, function (uiEvent, eventName) {
          var fn = $parse(uiEvent);
          elm.bind(eventName, function (evt) {
            var params = Array.prototype.slice.call(arguments);
            //Take out first paramater (event object);
            params = params.splice(1);
            fn($scope, {$event: evt, $params: params});
            if (!$scope.$$phase) {
              $scope.$apply();
            }
          });
        });
      };
    }])

  .directive('kaTags', function() {
    return {
      restrict: 'E',
      scope: {
        tags: '=',
        placeholder: '@'
      },
      replace: true,
      templateUrl: 'templates/tags-template.html',

      link: function(scope, element, attrs) {
        scope.keyPress = function(event) {
          if (event.keyCode != 13 || !scope.newTag) {
            return;
          }

          if (!scope.tags) {
            scope.tags = [];
          }

          if (scope.tags.indexOf(scope.newTag) == -1) {
            scope.tags.push(scope.newTag);
          }

          scope.newTag = "";
        };

        scope.removeTag = function(index) {
          scope.tags.splice(index, 1);
        };
      }
    };
  })

  .directive('kaPivotTable', function() {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        scope.$watch(attrs.kaPivotTable, function(newOpts) {
          element.children().remove();
          
          if (newOpts) {
            element.append(new PivotTable(newOpts).render()); 
          }
        }, true);
      }
    };
  })

  .directive('kaWizard', function() {
    return {
      restrict: 'A',

      transclude: true,

      controller: function($scope) {
        $scope.ctrl = this;
        $scope.steps = [];

        this.addStep = function(step) {
          angular.extend(step, {selected: false, finished: false});
          $scope.steps.push(step);
        };

        this.previous = function() {
          this.forward = false;
          if ($scope.selectedStep == 0) {
            return false;
          }

          if ($scope.steps[$scope.selectedStep].onFinish() &&
              !$scope.steps[$scope.selectedStep].onFinish()(this.forward)) {
            return false;
          }

          $scope.selectedStep--;
          $scope.steps[$scope.selectedStep + 1].selected = false;
          $scope.steps[$scope.selectedStep].selected = true;
          $scope.steps[$scope.selectedStep].finished = false;
          return true;
        };

        this.next = function() {
          this.forward = true;
          if ($scope.steps[$scope.selectedStep].onFinish() &&
              !$scope.steps[$scope.selectedStep].onFinish()(this.forward)) {
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
        };

        this.getCurrentStep = function() {
          return $scope.selectedStep ;
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

          for (var i = 0; i < numSteps; ++i) {
            if(!fn()) {
              break;
            }
          }
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
            scope[attrs.kaWizard] = controller;
            // scope['dfTemplate'] = (attrs.dfTemplate == undefined ? 'df_std_wizard.html' : attrs.dfTemplate);
          },

          post: function (scope, element, attributes, controller) {
            scope.selectedStep = 0;
            scope.steps[0].selected = true;
          }
        }
      },

      templateUrl: 'templates/wizard-template.html'
    };
  })

  .directive('kaWizardStep', function() {
    return {
      restrict: 'E',
      require : '^kaWizard',
      template: '<div ng-transclude ng-show="selected"></div>',
      transclude: true,
      replace : true,
      scope : {
        title : '@',
        onFinish : '&'
      },

      link: function(scope, element, attrs, wizardCtrl) {
        wizardCtrl.addStep(scope);
      }
    };
  });
