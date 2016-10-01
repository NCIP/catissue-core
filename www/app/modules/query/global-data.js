
angular.module('os.query.globaldata', ['os.query.models', 'os.biospecimen.models'])
  .factory('QueryGlobalData', function($translate, $q, CollectionProtocol, Form, SavedQuery, QueryUtil, Util) {
    var QueryGlobalData = function() {
      this.cpsQ = undefined;
      this.cpList = undefined;
      this.defSelectList = [];
    };

    QueryGlobalData.prototype.getCps = function() {
      var d = $q.defer();
      
      if (!this.cpsQ) {
        var that = this;
        this.cpsQ = CollectionProtocol.list({detailedList: false, maxResults: 1000}).then(
          function(result) {
            return $translate('common.none').then(
              function(none) {
                result.unshift({id: -1, shortTitle: none, title: none});
                that.cpList = result;
                return that.cpList;
              }
            );
          }
        );
      }

      this.cpsQ.then(function(result) { d.resolve(result); });
      return d.promise;
    };

    QueryGlobalData.prototype.loadCpForms = function(cp) {
      var d = $q.defer();
      if (!this.formsQ) {
        this.formsQ = Form.listQueryForms();
      }

      if (!cp.forms) {
        cp.forms = [];
        this.formsQ.then(
          function(forms) { 
            Util.unshiftAll(cp.forms, angular.copy(forms));

            var refCp = new CollectionProtocol(cp);
            delete refCp.forms;

            angular.forEach(cp.forms, function(form) {
              form.cp = refCp;
              form.fields = undefined;
            });
            d.resolve(cp.forms);
          }
        );
      } else {
        d.resolve(cp.forms);
      }  
    
      return d.promise;
    }

    QueryGlobalData.prototype.newQueryCtx = function(savedQuery) {
      savedQuery = savedQuery || {};

      var selectList = savedQuery.selectList;
      if (!selectList) {
        selectList = [];
        initDefSelectList(this, selectList);
      }

      this.queryCtx = {
        currentFilter: {},
        disableCpSelection: false,
        id: savedQuery.id,
        title: savedQuery.title,
        filters: [],
        filtersMap: {},
        exprNodes: [],
        filterId: 0,
        selectedFields: selectList,
        reporting: savedQuery.reporting || {type: 'none', params: {}},
        selectedCp: {id: savedQuery.cpId},
        isValid: true,
        drivingForm: 'Participant',
        wideRowMode: savedQuery.wideRowMode || 'DEEP'
      };

      var that = this;
      if (!savedQuery.cpId || savedQuery.cpId == -1) {
        this.getCps().then(
          function(cps) {
            that.queryCtx.selectedCp = cps[0];
          }
        );
      }

      return this.queryCtx;
    }

    QueryGlobalData.prototype.clearQueryCtx = function() {
      this.queryCtx = undefined;
    }

    QueryGlobalData.prototype.setQueryCtx = function(queryCtx) {
      this.queryCtx = queryCtx;
    }

    QueryGlobalData.prototype.getQueryCtx = function(queryId, cpId) {
      if (this.queryCtx && !cpId) {
        return this.queryCtx;
      }

      if (!queryId || queryId <= 0) {
        return this.newQueryCtx();
      }

      var that = this;
      return SavedQuery.getById(queryId).then(
        function(savedQuery) {
          return createQueryCtx(that, savedQuery, cpId);
        }
      );
    };

    function createQueryCtx(queryGlobal, savedQuery, cpId) {
      var queryCtx = queryGlobal.newQueryCtx(savedQuery);
      return queryGlobal.getCps().then(
        function(cps) {
          if (!!cpId) {
            savedQuery.cpId = cpId;
          }

          var selectedCp = queryCtx.selectedCp = getCp(cps, savedQuery.cpId || -1);
          if (!selectedCp) {
            return undefined;
          }
         
          var promise = queryGlobal.loadCpForms(selectedCp).then(
            function(forms) {
              recreateUiFilters(queryCtx, savedQuery.filters);
              recreateUiExprNodes(queryCtx, savedQuery.queryExpression);
              return $q.all(loadFormFieldsNeededForFilters(queryCtx.filters));
            }
          );

          return promise.then(
            function() {
              fleshOutFilterFields(queryCtx);
              return queryCtx;
            }
          )
        }
      );
    }
        
    function getCp(cps, cpId) {
      for (var i = 0; i < cps.length; ++i) {
        if (cps[i].id == cpId) {
          return cps[i];
        }
      }

      return null;
    }

    function recreateUiFilters(queryCtx, filters) {
      var uiFilters = queryCtx.filters = []; 
      var filtersMap = queryCtx.filtersMap = {};
      var filterId = 0;

      angular.forEach(filters, function(filter) {
        var uiFilter = QueryUtil.getUiFilter(queryCtx.selectedCp, filter);
        uiFilters.push(uiFilter);
        filtersMap[uiFilter.id] = uiFilter;

        if (filterId < uiFilter.id) {
          filterId = uiFilter.id;
        }
      });

      queryCtx.filterId = filterId;
    }

    function recreateUiExprNodes(queryCtx, queryExpressions) {
      var exprNodes = queryCtx.exprNodes = [];
      angular.forEach(queryExpressions, function(expr) {
        if (expr.nodeType == 'FILTER') {
          exprNodes.push({type: 'filter', value: expr.value});
        } else if (expr.nodeType == 'OPERATOR') {
          exprNodes.push({type: 'op', value: QueryUtil.getOpByModel(expr.value).name});
        } else if (expr.nodeType == 'PARENTHESIS') {
          exprNodes.push({type: 'paren', value: expr.value == 'LEFT' ? '(' : ')'});
        }
      });

      return exprNodes;
    }

    function loadFormFieldsNeededForFilters(filters) {
      var promises = [];
      var loadedForms = {};
      angular.forEach(filters, function(filter) {
        if (filter.expr) {
          return;
        }

        var form = filter.form;
        if (!loadedForms[form.name]) {
          promises.push(form.getFields());
          loadedForms[form.name] = true;
        }
      });

      return promises;
    }

    function fleshOutFilterFields(queryCtx) {
      for (var i = 0; i < queryCtx.filters.length; ++i) {
        var filter = queryCtx.filters[i];
        if (filter.expr) {
          continue;
        }
                  
        filter.field = filter.form.getField(filter.fieldName);
        if (!filter.field) {
          return undefined;
        }
      }

      QueryUtil.disableCpSelection(queryCtx);
    }

    function initDefSelectList(queryGlobal, selectList) {
      if (queryGlobal.defSelectList.length > 0) {
        Util.unshiftAll(selectList, queryGlobal.defSelectList);
        return;
      }

      getDefParticipantForm(queryGlobal).then(
        function(form) {
          form.getFields().then(
            function(fields) {
              angular.forEach(fields, function(field) {
                if (field.type == 'SUBFORM') {
                  return;
                }

                queryGlobal.defSelectList.push('Participant.' + field.name);
              });

              Util.unshiftAll(selectList, queryGlobal.defSelectList);
            }
          );
        }
      );
    }

    function getDefParticipantForm(queryGlobal) {
      return queryGlobal.getCps().then(
        function(cps) {
          return getParticipantForm(queryGlobal, cps[0]);
        }
      );
    }

    function getParticipantForm(queryGlobal, cp) {
      return queryGlobal.loadCpForms(cp).then(
        function(forms) {
          for (var i = 0; i < forms.length; ++i) {
            if (forms[i].name == 'Participant') {
              return forms[i];
            }
          }

          return null;
        }
      );
    }

    return QueryGlobalData;
  })

  .factory('QueryCtxHolder', function() {
    var queryCtx;
 
    return {
      getCtx: function() {
        return queryCtx;
      },

      setCtx: function(ctx) {
        queryCtx = ctx;
      },

      clearCtx: function() {
        queryCtx = undefined;
      }
    };
  });
