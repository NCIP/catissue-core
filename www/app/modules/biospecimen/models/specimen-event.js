angular.module('os.biospecimen.models.specimenevent', ['os.common.models'])
  .factory('SpecimenEvent', function(osModel, $http, Form) {
    var SpecimenEvent = osModel('specimen-events');
    
    SpecimenEvent.getEvents = function() {
      return Form.listForms('SpecimenEvent');
    }

    SpecimenEvent.save = function(formId, data) {
      return $http.post(SpecimenEvent.url() + '/' + formId, data);
    }
    
    return SpecimenEvent;
  });
