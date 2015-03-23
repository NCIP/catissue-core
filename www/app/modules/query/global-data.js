
angular.module('os.query.globaldata', ['os.query.models', 'os.biospecimen.models'])
  .factory('QueryGlobalData', function($translate, $q, CollectionProtocol, Form) {
    var QueryGlobalData = function() {
      this.cpsQ = undefined;
      this.cpList = undefined;
    };

    QueryGlobalData.prototype.getCps = function() {
      var d = $q.defer();
      
      if (!this.cpsQ) {
        var that = this;
        this.cpsQ = CollectionProtocol.query().then(
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
        this.formsQ.then(
          function(forms) { 
            cp.forms = angular.copy(forms); 
            angular.forEach(cp.forms, function(form) {
              form.cp = cp;
            });
            d.resolve(cp.forms);
          }
        );
      } else {
        d.resolve(cp.forms);
      }  
    
      return d.promise;
    }

    return QueryGlobalData;
  });
