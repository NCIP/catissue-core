angular.module('os.biospecimen.participant.newreg', ['os.biospecimen.models'])
  .controller('RegisterNewCtrl', function(
    $scope, $state, cpr, 
    Participant, CollectionProtocol, CollectionProtocolRegistration) {

    var registeredCps = [];

    function init() {
      $scope.cpList = [];
      $scope.cpr = cpr;
      $scope.newCpr = new CollectionProtocolRegistration({registrationDate: new Date()});

      angular.forEach(cpr.participant.registeredCps, function(cp) {
        registeredCps.push(cp.cpShortTitle);
      });
    }

    $scope.loadCps = function(searchTitle) {
      $scope.cpList = [];
      CollectionProtocol.listForRegistrations($scope.cpr.getMrnSites(), searchTitle).then(
        function(cps) {
          $scope.cpList = cps.filter(function(cp) { return registeredCps.indexOf(cp.shortTitle) == -1; });
        }
      );
    }

    $scope.register = function() {
      var cprToSave = new CollectionProtocolRegistration({
        participant: new Participant({id: cpr.participant.id, pmis: cpr.participant.getPmis()}),
        registrationDate: $scope.newCpr.registrationDate,
        cpId: $scope.newCpr.cp.id,
        ppid: $scope.newCpr.ppid
      });

      cprToSave.$saveOrUpdate().then(
        function(savedCpr) {
          $state.go('participant-detail.overview', {cprId: savedCpr.id});
        }
      );
    }

    init(); 
  });
