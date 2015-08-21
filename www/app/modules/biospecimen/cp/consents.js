
angular.module('os.biospecimen.cp.consents', ['os.biospecimen.models'])
  .controller('CpConsentsCtrl', function($scope, $state, $q, cp, consentTiers, DeleteUtil, Alerts) {
    $scope.cp = cp;

    var consents = {
      tiers: consentTiers,
      stmtAttr: 'statement'
    };

    $scope.consents = consents;

    $scope.listChanged = function(action, stmt) {
      if (action == 'add') {
        stmt = cp.newConsentTier(stmt);
      }

      if (action == 'add' || action == 'update') {
        return stmt.$saveOrUpdate();
      } else if (action == 'remove') {
        var deferred = $q.defer();
        DeleteUtil.delete(
          stmt,
          {
            onDeletion: onConsentDeletion(deferred),
            onDeleteFail: onConsentDeleteFail(deferred)
          }
        );
        return deferred.promise;
      }

      return undefined;
    };

    $scope.updateConsentEnabled = function() {
      $scope.cp.updateConsentEnabled().then(
        function(result) {
          Alerts.success("cp.consent_enabled_updated", {enabled: result.consentEnabled});
        }
      );
    }

    function onConsentDeletion(deferred) {
      return function() {
        deferred.resolve(true);
      }
    }

    function onConsentDeleteFail(deferred) {
      return function() {
        deferred.reject();
      }
    }

  });
