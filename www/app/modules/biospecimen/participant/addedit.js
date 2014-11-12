
angular.module('openspecimen')
  .controller('ParticipantAddEditCtrl', 
    function($scope, $modalInstance, $stateParams, AlertService, CollectionProtocolService, SiteService, PvManager) {

    $scope.cpId = $stateParams.cpId;

    $scope.cpr = {
      participant: {
        pmis: []
      },
      registrationDate: new Date()
    };

    $scope.addPmi = function() {
      $scope.cpr.participant.pmis.push({mrn: '', site: ''});
    };

    $scope.removePmi = function(index) {
      $scope.cpr.participant.pmis.splice(index, 1);
    };

    SiteService.getSites().then(
      function(result) {
        if (result.status != "ok") {
          alert("Failed to load sites information");
        }
        $scope.sites = result.data;
      }
    );

    PvManager.loadPvs($scope, 'gender');
    PvManager.loadPvs($scope, 'ethnicity');
    PvManager.loadPvs($scope, 'vitalStatus');
    PvManager.loadPvs($scope, 'race');


    var formatSsn = function(ssn) {
      if (ssn && ssn.length > 0) {
        ssn = [ssn.slice(0, 3), '-', ssn.slice(3, 5), '-', ssn.slice(5)].join('');
      } 

      return ssn;
    };

    var formatPmis = function(inputPmis) {
      var pmis = [];
      angular.forEach(inputPmis, function(pmi) {
        pmis.push({siteName: pmi.site.name, mrn: pmi.mrn});
      });

      return pmis;
    };


    var handleRegResult = function(result) {
      if (result.status == 'ok') {
        $modalInstance.close('ok');
      } else if (result.status == 'user_error') {
        var errMsgs = result.data.errorMessages;
        if (errMsgs.length > 0) {
          var errMsg = errMsgs[0].attributeName + ": " + errMsgs[0].message;
          AlertService.display($scope, errMsg, 'danger');
        }
      }
    };

    $scope.register = function() {
      var req = angular.copy($scope.cpr);
      req.participant.ssn = formatSsn(req.participant.ssn);
      req.participant.pmis = formatPmis(req.participant.pmis);
      CollectionProtocolService.registerParticipant($scope.cpId, req).then(handleRegResult);
    };
  });
