
angular.module('os.biospecimen.cp.events', ['os.biospecimen.models'])
  .controller('CpEventsCtrl', function(
     $scope, $state, $stateParams, $modal,
     cp, events, 
     CollectionProtocolEvent, PvManager) {

    var pvsLoaded = false;

    var copyFrom = undefined;

    function init() {
      $scope.cp = cp;
      $scope.events = events;
      $scope.mode = undefined;
         
      $scope.event = {};
      $scope.selected = {};
    };

    function loadPvs() {
      if (pvsLoaded) {
        return;
      }

      $scope.visitNamePrintModes = PvManager.getPvs('visit-name-print-modes');
      pvsLoaded = true;
    };

    function loadSpecimenRequirements(event) {
      if ($scope.selected.id == event.id) {
        return;
      }

      $scope.selected = event;
      $state.go('cp-detail.specimen-requirements', {eventId: event.id});
    };

    $scope.selectEvent = function(event) { 
      loadSpecimenRequirements(event);
    };

    $scope.showAddEvent = function() {
      $scope.event = new CollectionProtocolEvent({collectionProtocol: cp.title});
      $scope.mode = 'add';
      loadPvs();
    };

    $scope.showEditEvent = function(evt) {
      $scope.event = angular.copy(evt);
      $scope.mode = undefined;
      loadSpecimenRequirements(evt);
      loadPvs();
    };

    $scope.showCopyEvent = function(evt) {
      $scope.event = angular.copy(evt);

      copyFrom = $scope.event.id;
      delete $scope.event.code;
      delete $scope.event.eventLabel;
      delete $scope.event.id;

      $scope.mode = 'copy';
      loadPvs();
    };

    $scope.revertEdit = function() {
      $scope.mode = undefined;
      $scope.event = {};
    };

    $scope.addEvent = function() {
      var ret = undefined;
      if ($scope.mode == 'add') {
        ret = $scope.event.$saveOrUpdate();
      } else {
        ret = $scope.event.copy(copyFrom);
      }

      ret.then(
        function(result) {
          $scope.events.push(result);
          $scope.event = {};
          $scope.mode = undefined;
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

    $scope.deleteEvent = function(evt) {
      var modalInstance = $modal.open({
        templateUrl: 'cp_event_delete.html',
        controller: function($scope, $modalInstance) {
          $scope.yes = function() {
            $modalInstance.close(true);
          }

          $scope.no = function() {
            $modalInstance.dismiss('cancel');
          }
        }
      });

      modalInstance.result.then(
        function() {
          evt.delete().then(
            function() {
              var idx = $scope.events.indexOf(evt);
              $scope.events.splice(idx, 1);
              if ($scope.events.length > 0) {
                $scope.selectEvent($scope.events[0]);           
              }
            }
          );
        }
      );
    }; 

    init();
  });
