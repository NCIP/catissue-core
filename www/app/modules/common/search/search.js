
angular.module('openspecimen')
  .directive('osQuickSearch', function($rootScope, $state, $document, CollectionProtocolRegistration, Visit, Specimen, Container, Alerts) {

  function participantSearch() {
    CollectionProtocolRegistration.getParticipants({ppid: $rootScope.quickSearch.participant.ppid, uid: $rootScope.quickSearch.participant.uid}).then(
      function(participant) {
        if (participant == undefined || participant.length == 0) {
          Alerts.error('search.error', {component: 'Participant', id: $rootScope.quickSearch.participant.ppid || $rootScope.quickSearch.participant.uid});
          return;
        } else if (participant.length > 1) {
          $rootScope.quickSearch.cprs = participant;
          $state.go('participant-search');
        } else {
          $state.go('participant-detail.overview', {cprId: participant[0].cprId});
        }

        $rootScope.quickSearch.show = false;
      }
    );
  }

  function specimenSearch() {
    Specimen.listByLabels($rootScope.quickSearch.specimen).then(
      function(specimen) {
        if (specimen == undefined || specimen.length == 0) {
          Alerts.error('search.error', {component: 'Specimen', id: $rootScope.quickSearch.specimen});
          return;
        }

        $state.go('specimen-detail.overview', {eventId: specimen[0].eventId, specimenId: specimen[0].id, srId: specimen[0].reqId});
        $rootScope.quickSearch.show = false;
      }
    );
  }

  function visitSearch() {
    Visit.getByName($rootScope.quickSearch.visit).then(
      function(visit) {
        if (visit == undefined) {
          Alerts.error('search.error', {component: 'Visit', id: $rootScope.quickSearch.visit});
          return;
        }

        $state.go('visit-detail.overview', {visitId: visit.id, eventId: visit.eventId});
        $rootScope.quickSearch.show = false;
      }
    );
  }

  function containerSearch() {
    Container.getByName($rootScope.quickSearch.container).then(
      function(container) {
        if (container == undefined) {
          Alerts.error('search.error', {component: 'Container', id: $rootScope.quickSearch.container});
          return;
        }

        $state.go('container-detail.overview', {containerId: container.id});
        $rootScope.quickSearch.show = false;
      }
    );
  }

  return {
    restrict: 'A',
    templateUrl: 'modules/common/search/search.html',

    link: function(scope, element, attrs) {
      $rootScope.quickSearch = {};
      $rootScope.quickSearch.show = false;
      $rootScope.quickSearch.searchComponents = ["Participant", "Specimen", "Visit", "Container"];
      $rootScope.quickSearch.component = "Participant";

      element.bind('click', function(e) {
        e.stopPropagation();
      });

      $document.bind('click', function(e) {
        $rootScope.quickSearch.show = false;
        scope.$apply(attrs.osQuickSearch);
      });

      $rootScope.onComponentSelect = function(component) {
        $rootScope.quickSearch.component = component;
      }

      $rootScope.searchSubmit = function() {
        if ($rootScope.quickSearch.component == "Participant") {
          participantSearch();
        } else if ($rootScope.quickSearch.component == "Specimen") {
          specimenSearch();
        } else if ($rootScope.quickSearch.component == "Visit") {
          visitSearch();
        } else if ($rootScope.quickSearch.component == "Container") {
          containerSearch();
        }
      }
    }
  }
});
