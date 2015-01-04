
angular.module('os.biospecimen.cp.events', ['os.biospecimen.models'])
  .controller('CpEventsCtrl', function(
     $scope, $state, $stateParams, 
     cp, events, 
     CollectionProtocolEvent, PvManager) {

    var pvsLoaded = false;

    function loadPvs() {
      if (pvsLoaded) {
        return;
      }

      $scope.clinicalStatuses = PvManager.getPvs('clinical-status');

      PvManager.loadSites().then(
        function(sites) {
          $scope.sites = sites.map(
            function(site) { 
              return site.name; 
            }
          );
        }
      );

      $scope.searchClinicalDiagnoses = function(searchTerm) {
        var params = {cpId: cp.id, searchTerm: searchTerm};
        $scope.clinicalDiagnoses = PvManager.getClinicalDiagnoses(params);
      };

      pvsLoaded = true;
    };

    function init() {
      $scope.cp = cp;
      $scope.events = events;
      $scope.addMode = false;
         
      $scope.clinicalStatuses = [];
      $scope.clinicalDiagnoses = [];
      $scope.sites = [];
      $scope.event = {};
      $scope.selected = {};
    };

    function loadSpecimenRequirements(event) {
      if ($scope.selected.id == event.id) {
        return;
      }

      $scope.selected = event;
      $state.go('cp-detail.specimen-requirements', {eventId: event.id});
    };

    $scope.selectEvent = function(event) { 
      $scope.selected = event;
    };

    $scope.showAddEvent = function() {
      $scope.event = new CollectionProtocolEvent({collectionProtocol: cp.title});
      $scope.addMode = true;
      loadPvs();
    };

    $scope.showEditEvent = function($event, evt) {
      $scope.event = angular.copy(evt);
      $scope.addMode = false;
      loadSpecimenRequirements(evt);
      loadPvs();
    };

    $scope.revertEdit = function() {
      $scope.addMode = false;
      $scope.event = {};
    };

    $scope.addEvent = function() {
      $scope.event.$saveOrUpdate().then(
        function(result) {
          $scope.events.push(result);
          $scope.addMode = false;
          loadSpecimenRequirements(result);
        }
      );
    };

    $scope.editEvent = function() {
      $scope.event.$saveOrUpdate().then(
        function(result) {
          for (var i = 0; i < $scope.events.length; ++i) {
            if ($scope.events[i].id == result.id) {
              $scope.events[i] = result;
              break;
            }
          }
          $scope.event = {};
          loadSpecimenRequirements(result);
        }
      );
    };

    init();
  });
