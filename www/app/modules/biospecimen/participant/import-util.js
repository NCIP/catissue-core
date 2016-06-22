angular.module('os.biospecimen.participant')
  .factory('ImportUtil', function($translate) {
    function addForms(importTypes, group, entityType, forms) {
      angular.forEach(forms, function(form) {
        if (form.sysForm) {
          return;
        }

        importTypes.push({
          group: group,
          type: 'extensions',
          title: form.caption,
          params: {
            entityType: entityType,
            formName: form.name
          }
        });
      });

      return importTypes;
    }

    function getParticipantTypes(entityForms) {
      var group = $translate.instant('participant.title');

      var importTypes = [
        {
          group: group, type: 'cpr', title: 'participant.list'
        },
        {
          group: group, type: 'consent', title: 'participant.consents',
          showImportType: false, csvType: 'MULTIPLE_ROWS_PER_OBJ', importType: 'UPDATE'
        }
      ];

      return addForms(importTypes, group, 'Participant', entityForms['Participant']);
    } 

    function getVisitTypes(entityForms) {
      var group = $translate.instant('visits.title');
      var importTypes = [{ group: group, type: 'visit', title: 'visits.list' }];
      return addForms(importTypes, group, 'SpecimenCollectionGroup', entityForms['SpecimenCollectionGroup']);
    }

    function getSpecimenTypes(entityForms) {
      var group = $translate.instant('specimens.title');

      var importTypes = [];

      importTypes.push({ group: group, type: 'specimen', title: 'specimens.list' });
      importTypes.push({
        group: group, type: 'specimenAliquot', title: 'specimens.spmn_aliquots',
        showImportType: false, importType    : 'CREATE'
      });
      importTypes.push({
        group: group, type: 'specimenDerivative', title: 'specimens.spmn_derivatives',
        showImportType: false, importType    : 'CREATE'
      });
      importTypes.push({
        group: group, type: 'masterSpecimen', title: 'participant.master_specimens',
        showImportType: false, importType    : 'CREATE'
      });

      addForms(importTypes, group, 'Specimen', entityForms['Specimen']);
      return addForms(importTypes, group, 'SpecimenEvent', entityForms['SpecimenEvent']);
    }

    function getImportDetail(cp, allowedEntityTypes, forms) {
      var entityForms = {};
      angular.forEach(forms, function(form) {
        if (!entityForms[form.entityType]) {
          entityForms[form.entityType] = [];
        }

        entityForms[form.entityType].push(form);
      });

      var importTypes = [];
      if (allowedEntityTypes.indexOf('Participant') >= 0) {
        importTypes = importTypes.concat(getParticipantTypes(entityForms));
      }

      if (allowedEntityTypes.indexOf('SpecimenCollectionGroup') >= 0) {
        importTypes = importTypes.concat(getVisitTypes(entityForms));
      }

      if (allowedEntityTypes.indexOf('Specimen') >= 0) {
        importTypes = importTypes.concat(getSpecimenTypes(entityForms));
      }

      angular.forEach(importTypes,
        function(importType) {
          if (!importType.params) {
            importType.params = {};
          }

          importType.params.cpId = cp.id;
        }
      );

      return {
        breadcrumbs: [
          {state: 'cp-detail.overview', title:  cp.shortTitle,     params: '{cpId:' + cp.id + '}'},
          {state: 'participant-list',   title: 'participant.list', params: '{cpId:' + cp.id + '}'}
        ],
        title: 'Bulk Import Records',
        objectType: undefined,
        onSuccess: {state: 'participant-list', params: {cpId: cp.id}},
        types: importTypes,
        objectParams: {
          cpId: cp.id
        }
      };
    }

    return {
      getImportDetail: function(cp, allowedEntityTypes, forms) {
        return $translate('common.none').then(
          function() {
            return getImportDetail(cp, allowedEntityTypes, forms);
          }
        );
      }
    }
  });
