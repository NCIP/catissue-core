
angular.module('os.query.addedit', ['os.query.models', 'os.query.util'])
  .controller('QueryAddEditCtrl', function($scope, cps, query, queryGlobal, QueryUtil) {
    function init() {
      $scope.cps = cps;
      $scope.query = query;

      $scope.queryLocal = {};

      var cpId = query.cpId || -1;
      $scope.queryLocal.selectedCp = getCp(cpId);
      loadCpForms($scope.queryLocal.selectedCp);
    }

    function loadCpForms(cp) {
      queryGlobal.loadCpForms(cp);
    }

    function getCp(cpId) {
      var cp = undefined;
      for (var i = 0; i < cps.length; ++i) {
        if (cps[i].id == cpId) {
          cp = cps[i];
          break;
        }
      }

      return cp;
    }

    $scope.loadCpForms = function() {
      loadCpForms($scope.queryLocal.selectedCp);
    }

    $scope.onFormSelect = function(form) {
      var ql = $scope.queryLocal;

      ql.searchField = '';
      //hidePopovers();
      if (ql.openForm) {
        ql.openForm.showExtnFields = false; // previously selected form
      }

      ql.openForm = form;
      ql.currFilter = {form: form};
      form.getFields().then(
        function(fields) {
          form.staticFields = QueryUtil.flattenStaticFields("", fields);
          form.extnForms = QueryUtil.getExtnForms("", fields);
          form.extnFields = QueryUtil.flattenExtnFields(form.extnForms);
        }
      );
    }

    init();
  });
