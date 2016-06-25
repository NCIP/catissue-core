
angular.module('os.common.import.importjob', ['os.common.models'])
  .factory('ImportJob', function(osModel, $http) {
    var ImportJob = osModel('import-jobs');

    ImportJob.processFileRecs = function(fileId, fields) {
      var req = {fileId: fileId, fields: fields};
      return $http.post(ImportJob.url() + 'process-file-records', req).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    ImportJob.getRecFieldsCsv = function(fields) {
      return $http.post(ImportJob.url() + 'record-fields-csv', fields).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    return ImportJob;
  });

