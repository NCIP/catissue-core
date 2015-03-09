
angular.module('os.biospecimen.cp', 
  [
    'ui.router',
    'os.biospecimen.cp.list',
    'os.biospecimen.cp.addedit',
    'os.biospecimen.cp.import',
    'os.biospecimen.cp.detail',
    'os.biospecimen.cp.consents',
    'os.biospecimen.cp.events',
    'os.biospecimen.cp.specimens'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('cps', {
        url: '/cps',
        abstract: true,
        template: '<div ui-view></div>',
        parent: 'signed-in'
      })
      .state('cp-list', {
        url: '', 
        templateUrl: 'modules/biospecimen/cp/list.html',
        controller: 'CpListCtrl',
        parent: 'cps'
      })
      .state('cp-addedit', {
        url: '/addedit?cpId',
        templateUrl: 'modules/biospecimen/cp/addedit.html',
        controller: 'CpAddEditCtrl',
        parent: 'cps'
      })
      .state('cp-import', {
        url: '/import',
        templateUrl: 'modules/biospecimen/cp/import.html',
        controller: 'CpImportCtrl',
        parent: 'cps'
      })
      .state('cp-detail', {
        url: '/:cpId',
        templateUrl: 'modules/biospecimen/cp/detail.html',
        parent: 'cps',
        resolve: {
          cp: function($stateParams, CollectionProtocol) {
            return CollectionProtocol.getById($stateParams.cpId);
          }
        },
        controller: 'CpDetailCtrl',
        breadcrumb: {
          title: '{{cp.title}}',
          state: 'cp-detail.overview'
        }
      })
      .state('cp-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/biospecimen/cp/overview.html',
        parent: 'cp-detail'
      })
      .state('cp-detail.consents', {
        url: '/consents',
        templateUrl: 'modules/biospecimen/cp/consents.html',
        parent: 'cp-detail',
        resolve: {
          consentTiers: function(cp) {
            return cp.getConsentTiers();
          }
        },
        controller: 'CpConsentsCtrl'
      })
      .state('cp-detail.events', {
        templateUrl: 'modules/biospecimen/cp/events.html',
        parent: 'cp-detail',
        resolve: {
          events: function($stateParams, cp, CollectionProtocolEvent) {
            return CollectionProtocolEvent.listFor(cp.id);
          }
        },
        controller: 'CpEventsCtrl'
      })
      .state('cp-detail.specimen-requirements', {
        url: '/specimen-requirements?eventId',
        templateUrl: 'modules/biospecimen/cp/specimens.html',
        parent: 'cp-detail.events',
        resolve: {
          specimenRequirements: function($stateParams, SpecimenRequirement) {
            var eventId = $stateParams.eventId;
            if (!eventId) {
              return [];
            }

            return SpecimenRequirement.listFor(eventId);
          }
        },
        controller: 'CpSpecimensCtrl'
      });
  });
  
