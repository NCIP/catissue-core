
angular.module('os.query.models.savedquery', ['os.common.models'])
  .factory('SavedQuery', function($http, osModel, QueryUtil) {
    var SavedQuery = osModel('saved-queries');

    SavedQuery.list = function(filterOpts) {
      var result = {count: 0, queries: []};
      var params = angular.extend({countReq: false}, filterOpts);
      
      $http.get(SavedQuery.url(), {params: params}).then(
        function(resp) {
          result.count = resp.data.count;
          result.queries = resp.data.queries.map(
            function(query) {
              return new SavedQuery(query);
            }
          );
        }
      );

      return result;
    }

    SavedQuery.getImportQueryDefUrl = function() {
      return SavedQuery.url() + 'definition-file';
    }

    SavedQuery.fromQueryCtx = function(qc) {
      return new SavedQuery({
        id: qc.id,
        title: qc.title,
        selectList: qc.selectedFields,
        filters: getCuratedFilters(qc.filters),
        queryExpression: getCuratedExprNodes(qc.exprNodes),
        cpId: qc.selectedCp.id,
        drivingForm: qc.drivingForm,
        reporting: qc.reporting,
        wideRowMode: qc.wideRowMode
      })
    }

    SavedQuery.prototype.getQueryDefUrl = function() {
      return SavedQuery.url() + this.$id() + '/definition-file';
    }

    function getCuratedFilters(filters) {
      return filters.map(
        function(filter) {
          if (filter.expr) {
            return {
              id: filter.id, expr: filter.expr, 
              desc: filter.desc, parameterized: filter.parameterized
            };
          } else {
            var values = filter.value instanceof Array ? filter.value : [filter.value];
            return {
              id: filter.id, field: filter.form.name + "." + filter.field.name,
              op: filter.op.model, values: values,
              parameterized: filter.parameterized
            };
          }
        }
      );
    };

    function getCuratedExprNodes(exprNodes) {
      return exprNodes.map(
        function(node) {
          if (node.type == 'paren') {
            return {nodeType: 'PARENTHESIS', value: node.value == '(' ? 'LEFT' : 'RIGHT'};
          } else if (node.type == 'op') {
            return {nodeType: 'OPERATOR', value: QueryUtil.getOp(node.value).model};
          } else if (node.type == 'filter') {
            return {nodeType: 'FILTER', value: node.value};
          }
        }
      );
    }

    return SavedQuery;
  });
