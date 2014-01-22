
angular.module('plus.controllers', [])
  .controller('QueryController', ['$scope', '$sce', 'CollectionProtocolService', 'FormService', function($scope, $sce, CollectionProtocolService, FormService) {
    var opsMap = {
      eq: {desc: "Equals", code: "&#61;"}, 
      ne: {desc: "Not Equals", code: "&#8800;"}, 
      lt: {desc: "Less than", code: "&#60;"}, 
      le: {desc: "Less than or Equals", code: "&#8804;"}, 
      gt: {desc: "Greater than", code: "&#62;"}, 
      ge: {desc: "Greater than or Equals", code:"&#8805;"},
      exists: {desc: "Exists", code: "&#8707;"}, 
      not_exists: {desc: "Not Exists", code: "&#8708;"}, 
      qin: {desc: "In", code:"&#8712;"}, 
      not_in: {desc: "Not In", code:"&#8713;"},
      starts_with: {desc: "Starts With", code: "&#8963;&#61;"}, 
      ends_with: {desc: "Ends With", code: "&#36;&#61;"}, 
      contains: {desc: "Contains", code: "&#126;"},
      and: {desc: 'and', code: 'and'},
      or: {desc: 'or', code: 'or'},
      intersect: {desc: 'intersect', code: '&#8745;'},
      not: {desc: 'not', code: 'not'}
    };

    var getField = function(form, fieldName) {
      var result = null;
      for (var i = 0; i < form.fields.length; ++i) {
        if (form.fields[i].name == fieldName) {
          result = form.fields[i];
          break;
        }
      }
      return result;
    };


    $scope.opDrag = function(e) {
      var op = angular.element(e.currentTarget).attr('data-arg');
      if (!op) {
        return;
      }

      var code = opsMap[op] ? opsMap[op].code : op;
      var cls = (op == '(' || op == ')') ? 'paren-node' : 'op-node';
      return angular.element("<div/>").addClass("pull-left")
               .append(angular.element("<div/>").addClass("filter-item-valign").addClass(cls).html(code));
    };

    $scope.exprSortOpts = {
      placeholder: 'sortablePlaceholder',
      stop: function(event, ui) {
        if (ui.item.attr('class').indexOf('btn') < 0) {
          return;
        }
      
        var nodeVal = ui.item.attr('data-arg');
        var nodeType = nodeVal == '(' || nodeVal == ')' ? 'paren' : 'op';
        var node = {type: nodeType, value: nodeVal};

        $scope.queryData.exprNodes[ui.item.index()] = node;
        $scope.$apply($scope.queryData);
        ui.item.remove();
        console.log($scope.queryData);
      }
    };

    $scope.removeNode = function(idx) {
      $scope.queryData.exprNodes.splice(idx, 1);
      console.log($scope.queryData);
    };

    $scope.queryData = {
      cpList: [],
      selectedCp: null,
      cpForms: [],
      openForm: null,
      filters: [],
      joinType: 'all',
      exprNodes: [],              //{type: filter/op/paran, val: filter id/and-or-intersect-not}
      filterId: 0,
      currFilter: {
        id:null,
        form: null,
        field: null,
        op: null,
        value: "TODO"
      }
    };

    $scope.onCpSelect = function(selectedCp) {
      if (selectedCp.id == -1) {
        $scope.queryData.cpForms = [];
        return;
      }

      CollectionProtocolService.getCpForms(selectedCp.id).then(function(forms) {
        var uniqIds = {};
        $scope.queryData.cpForms = forms.filter(function(form) {
          return !uniqIds[form.containerId] && (uniqIds[form.containerId] = 1);
        });
      });
    };

    CollectionProtocolService.getCpList().then(function(cpList) {
      $scope.queryData.cpList = cpList;
    });

    $scope.onFormClick = function(form) {
      $scope.openForm = form;
      if (!form.fields) {
        FormService.getFormFields(form.id).then(function(fields) {
          form.fields = fields;
        });
      }
    };

    $scope.onFieldSelect = function(fieldEl, droppableEl) {
      var fieldId   = fieldEl.attr('data-arg');
      var idx       = fieldId.indexOf(".");
      var formName  = fieldId.substring(0, idx);
      var fieldName = fieldId.substring(idx + 1);

      if ($scope.openForm.name != formName) {
        return;
      }

      var field = getField($scope.openForm, fieldName);
      if (field) {
        droppableEl.text(field.caption).removeClass("droppable").addClass("dropped");
        $scope.queryData.currFilter.form = $scope.openForm;
        $scope.queryData.currFilter.field = field;
        $scope.$apply($scope.queryData);
      }
    };

    $scope.onOperatorSelect = function(opEl, droppableEl) {
      var op = $scope.queryData.currFilter.op = opEl.attr('data-arg');
      $scope.$apply($scope.queryData);
      droppableEl.html(opsMap[op].code).removeClass("droppable").addClass("dropped");
    };

    $scope.onAdvOpDrop = function(opEl, droppableEl) {
      var op = opEl.attr('data-arg');
      var idx = droppableEl.attr('data-node-pos');
      $scope.queryData.exprNodes[idx].value = op;
      $scope.$apply($scope.queryData);
    }

    $scope.getOpDesc = function(op) {
      var opObj = opsMap[op];
      return opObj ? opObj.desc : "Unknown operator";
    };

    $scope.getOpCode = function(op) {
      var opObj = opsMap[op];
      return opObj ? $sce.trustAsHtml(opObj.code) : "Unknown operator";
    };

    $scope.$watch('queryData.currFilter', function(newVal, oldVal) {
      console.log("New: " + newVal + ", Old: " + oldVal);
    });

    $scope.getValueType = function() {
      var field = $scope.queryData.currFilter.field;
      var op = $scope.queryData.currFilter.op;

      if (!field) {
        return "text";
      } else if (field.pvs && (op == "qin" || op == "not_in")) {
        return "multiSelect";
      } else if (field.pvs) {
        return "select";
      } else if (field.dataType == "DATE") {
        return "datePicker";
      } else {
        return "text";
      }
    };

    $scope.addFilter = function() {
      $scope.queryData.filterId++;
      var filter = {
        id: $scope.queryData.filterId,
        form: $scope.queryData.currFilter.form.name,
        field: $scope.queryData.currFilter.field,
        op: $scope.queryData.currFilter.op,
        value: $scope.queryData.currFilter.value,
        formCaption: $scope.queryData.currFilter.form.caption
      };

      if ($scope.queryData.filters.length > 0) {
        var op = $scope.queryData.joinType == 'any' ? 'or' : 'and';
        $scope.queryData.exprNodes.push({type: 'op', value: op});
      }
      $scope.queryData.filters.push(filter);
      $scope.queryData.exprNodes.push({type: 'filter', value: filter.id});
      $scope.queryData.currFilter = {};
    };

    $scope.editFilter = function() {
      var filter = {
        id: $scope.queryData.currFilter.id,
        form: $scope.queryData.currFilter.form.name,
        field: $scope.queryData.currFilter.field,
        op: $scope.queryData.currFilter.op,
        value: $scope.queryData.currFilter.value,
        formCaption: $scope.queryData.currFilter.form.caption
      };

      for (var i = 0; i < $scope.queryData.filters.length; ++i) {
        if (filter.id == $scope.queryData.filters[i].id) {
          $scope.queryData.filters[i] = filter;
          break;
        }
      }
      $scope.queryData.currFilter = {};
    };

    $scope.displayFilter = function(filter) {
      $scope.queryData.currFilter = {
        form:  {name: filter.form, caption: filter.formCaption},
        id:  filter.id,
        field: filter.field,
        op: filter.op,
        value: filter.value
      };
    };

    $scope.deleteFilter = function(filter) {
      for (var i = 0; i < $scope.queryData.filters.length; ++i) {
        if (filter.id == $scope.queryData.filters[i].id) {
          $scope.queryData.filters.splice(i, 1);
          break;
        }
      }

      for (var i = 0; i < $scope.queryData.exprNodes.length; ++i) {
        var exprNode = $scope.queryData.exprNodes[i];
        var joinType = $scope.queryData.joinType;
        if (exprNode.type == 'filter' && filter.id == exprNode.value) {
          if (i == 0 && joinType != 'adv') {
            $scope.queryData.exprNodes.splice(0, 2);
          } else if (joinType != 'adv') {
            $scope.queryData.exprNodes.splice(i - 1, 2);
          } else {
            $scope.queryData.exprNodes.splice(i, 1);
          }
          break;
        }
      }
    };

    $scope.clearFilter = function() {
      $scope.queryData.currFilter = {};
    };

    $scope.setJoinType = function(joinType) {
      $scope.queryData.joinType = joinType;
      if (joinType == 'adv') {
        return;
      }

      var op = joinType == 'all' ? 'and' : 'or';
      var newExprNodes = [];
      for (var i = 0; i < $scope.queryData.filters.length; ++i) {
        if (i != 0) {
          newExprNodes.push({type: 'op', value: op});
        }
        newExprNodes.push({type: 'filter', value: $scope.queryData.filters[i].id});
      }
      $scope.queryData.exprNodes = newExprNodes;
    };
  }]);
