
angular.module('os.administrative.form', 
  [
    'os.administrative.form.list',
    'os.administrative.form.addedit',
    'os.administrative.form.formctxts'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('form-list', {
        url: '/forms',
        templateUrl: 'modules/administrative/form/list.html',
        controller: 'FormListCtrl',
        parent: 'signed-in'
      })
      .state('form-addedit', {
        url: '/form-addedit/:formId',
        templateUrl: 'modules/administrative/form/addedit.html',
        controller: 'FormAddEditCtrl',
        parent: 'signed-in'
      })
  });

