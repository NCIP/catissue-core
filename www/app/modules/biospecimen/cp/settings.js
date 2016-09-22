
angular.module('os.biospecimen.cp')
  .factory('CpSettingsReg', function(osDisplayList) {
    var settings = osDisplayList();

    settings.addItem({
      name: 'LabelSettings',
      state: 'cp-detail.settings.labels',
      key: 'cp.label_format.title',
      caption: ''
    });

    settings.addItem({
      name: 'CatalogSettings',
      state: 'cp-detail.settings.catalog',
      key: 'cp.catalog.title',
      caption: ''
    });

    settings.addItem({
      name: 'ContainerSettings',
      state: 'cp-detail.settings.container',
      key: 'cp.container.title',
      caption: ''
    });

    settings.addItem({
      name: 'ReportSettings',
      state: 'cp-detail.settings.reporting',
      key: 'cp.reporting.title',
      caption: ''
    });
    
    return {
      getSettings: settings.getItems,

      addSetting: settings.addItem
    }
  });
