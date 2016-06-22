angular.module('os.administrative.form.entities', ['os.common'])
  .factory('FormEntityReg', function(osDisplayList) {
    var list = osDisplayList();

    list.addItem({name: 'Participant', key: 'entities.participant', caption: ''});
    list.addItem({name: 'SpecimenCollectionGroup', key: 'entities.visit', caption: ''});
    list.addItem({name: 'Specimen', key: 'entities.specimen', caption: ''});
    list.addItem({name: 'SpecimenEvent', caption: '', key: 'entities.specimen_event', allCps: true});

    list.addItem({
      name: 'ParticipantExtension',
      caption: '', key: 'entities.participant_extension',
      multipleRecs: false
    });

    list.addItem({
      name: 'VisitExtension',
      caption: '', key: 'entities.visit_extension',
      multipleRecs: false
    });

    list.addItem({
      name: 'SpecimenExtension',
      caption: '', key: 'entities.specimen_extension',
      multipleRecs: false
    });

    list.addItem({
      name: 'SiteExtension',
      caption: '', key: 'entities.site_extension',
      allCps: true, multipleRecs: false
    });

    list.addItem({
      name: 'CollectionProtocolExtension',
      caption: '', key: 'entities.cp_extension',
      allCps: true, multipleRecs: false
    });

    list.addItem({
      name: 'DistributionProtocolExtension',
      caption: '', key: 'entities.dp_extension',
      allCps: true, multipleRecs: false
    });

    return {
      getEntities: list.getItems,

      addEntity: list.addItem
    }
  });
