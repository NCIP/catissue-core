
angular.module('os.biospecimen.cp.consents', ['os.biospecimen.models'])
  .controller('CpConsentsCtrl', function($scope, $state, cp, consentTiers) {
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
        return stmt.$remove();
      }

      return undefined;
    };
  });
