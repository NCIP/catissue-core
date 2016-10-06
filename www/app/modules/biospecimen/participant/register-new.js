
angular.module('os.biospecimen.participant.newreg', ['os.biospecimen.models'])
  .factory('RegisterToNewCpsHolder', function() {
    var cpList = [];

    return {
      getCpList: function() { return cpList; },
      setCpList: function(input) { cpList = input; }
    };
  })
  .controller('RegisterNewCtrl', function(
    $scope, $state, cpr, 
    Participant, CollectionProtocol, CollectionProtocolRegistration, RegisterToNewCpsHolder) {

    function init() {
      $scope.cpr = cpr;
      $scope.newCpr = new CollectionProtocolRegistration({registrationDate: new Date()});
      $scope.cpList = RegisterToNewCpsHolder.getCpList();
      if ($scope.cpList.length == 1) {
        $scope.newCpr.cp = $scope.cpList[0];
      }
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
          $state.go('participant-detail.overview', {cpId: savedCpr.cpId, cprId: savedCpr.id});
        }
      );
    }

    init(); 
  });
