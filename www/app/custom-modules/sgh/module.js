
angular.module('openspecimen')
  .config(function($translatePartialLoaderProvider) {
    $translatePartialLoaderProvider.addPart('custom-modules/sgh');
  });
