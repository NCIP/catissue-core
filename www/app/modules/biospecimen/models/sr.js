angular.module('os.biospecimen.models.sr', ['os.common.models'])
  .factory('SpecimenRequirement', function(osModel) {
    var Sr = osModel('specimen-requirements');
 
    Sr.listFor = function(cpeId) {
      return Sr.query({eventId: cpeId});
    };

    return Sr;
  });
