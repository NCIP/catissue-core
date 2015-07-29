
angular.module('openspecimen')
  .config(function($stateProvider) {
    $stateProvider
      .state('unplanned-bulk-pre-printing', {
        url: '/bulk-participant-regs',
        templateUrl: 'custom-modules/sgh/biospecimen/bulk-printing.html',
        controller: 'sgh.CpBulkPrintingCtrl',
        resolve: {
          cp: function() {
            return {};
          }
        },
        parent: 'signed-in'
      })
      .state('cp-bulk-part-regs', {
        url: '/bulk-participant-regs',
        templateUrl: 'custom-modules/sgh/biospecimen/bulk-registrations.html',
        controller: 'sgh.CpBulkRegistrationsCtrl',
        parent: 'cp-view'
      })
  })
  .run(function($templateCache, PluginReg) {
    $templateCache.put(
      'custom-modules/sgh/biospecimen/unplanned-bulk-pre-print-btn.html',
      '<button class="default" ui-sref="unplanned-bulk-pre-printing">' +
      '  <span translate="custom_sgh.bulk_pre_printing">' +
      '    Bulk Pre Print TRIDs' +
      '  </span>' +
      '</button>'
    );

    $templateCache.put(
      'custom-modules/sgh/biospecimen/cp-bulk-reg-btn.html',
      '<button class="default" ui-sref="cp-bulk-part-regs">' +
      '  <span class="fa fa-plus"></span>' +
      '  <span translate="custom_sgh.bulk_reg">' +
      '    Bulk Register' +
      '  </span>' +
      '</button>'
    );
      
    PluginReg.registerViews(
      'sgh',
      {
        'cp-list': {
          'page-header': {
            template: 'custom-modules/sgh/biospecimen/unplanned-bulk-pre-print-btn.html'
          }
        },

        'participant-list': {
          'page-header': {
            template: 'custom-modules/sgh/biospecimen/cp-bulk-reg-btn.html'
          }
        }
      }
    );
  });
