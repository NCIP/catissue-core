angular.module('os.administrative.form.entities', ['os.common'])
  .factory('FormEntityReg', function(osDisplayList) {
    var list = osDisplayList();

    list.addItem({name: 'Participant', key: 'entities.participant', caption: ''});
    list.addItem({name: 'SpecimenCollectionGroup', key: 'entities.visit', caption: ''});
    list.addItem({name: 'Specimen', key: 'entities.specimen', caption: ''});
    list.addItem({name: 'SpecimenEvent', caption: '', key: 'entities.specimen_event', allCps: true});

    return {
      getEntities: list.getItems,

      addEntity: list.addItem
    }
  });
