
angular.module('os.administrative.form', 
  [
    'os.administrative.form.list',
    'os.administrative.form.addedit',
    'os.administrative.form.formctxts',
    'os.administrative.form.entities'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('form-list', {
        url: '/forms',
        templateUrl: 'modules/administrative/form/list.html',
        controller: 'FormListCtrl',
        parent: 'admin-view'
      })
      .state('form-addedit', {
        url: '/form-addedit/:formId',
        templateUrl: 'modules/administrative/form/addedit.html',
        controller: 'FormAddEditCtrl',
        resolve: {
          form: function($stateParams, Form) {
            if ($stateParams.formId) {
              return new Form({id: $stateParams.formId});
            }

            return new Form();
          }
        },
        parent: 'admin-view'
      })
  });

