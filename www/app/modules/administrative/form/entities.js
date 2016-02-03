angular.module('os.administrative.form.entities', [])
  .factory('FormEntityReg', function($translate) {
    var entities = [];

    function getEntities() {
      return entities;
    }

    function addEntity(entity) {
      var existing = undefined;
      for (var i = 0; i < entities.length; ++i) {
        if (entities[i].name == entity.name) {
          existing = entities[i];
          break;
        }
      }

      if (!existing) {
        entities.push(entity);
      }
    }

    function init() {
      var participant = {name: 'Participant', caption: ''};
      addEntity(participant);

      var visit       = {name: 'SpecimenCollectionGroup', caption: ''};
      addEntity(visit);

      var specimen    = {name: 'Specimen', caption: ''};
      addEntity(specimen);

      var event       = {name: 'SpecimenEvent', caption: '', allCps: true};
      addEntity(event);

      $translate('entities.participant').then(
        function() { 
          participant.caption = $translate.instant('entities.participant');
          visit.caption       = $translate.instant('entities.visit');
          specimen.caption    = $translate.instant('entities.specimen');
          event.caption       = $translate.instant('entities.specimen_event');
        }
      );
    }

    init();

    return {
      getEntities: getEntities,

      addEntity: addEntity
    }
  });
