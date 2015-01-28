angular.module('os.administrative.models.institute', ['os.common.models'])
  .factory('Institute', function(osModel, $http) {
    var Institute = osModel('institutes');

    Institute.list = function() {
      return Institute.query();
    }

    Institute.prototype.newDepartment = function() {
      return {id: '', name: ''}
    };

    Institute.prototype.addDepartment = function(department) {
      if(!this.departments) {
        this.departments = [];
      }

      this.departments.push(department);
    }

    Institute.prototype.removeDepartment = function(department) {
      var idx = this.departments ? this.departments.indexOf(department) : -1;
      if (idx != -1) {
        this.departments.splice(idx, 1);
      }

      return idx;
    };

    Institute.prototype.$saveProps = function() {
      var DepartmentNames = {};

      this.departments = this.departments.filter(
        function(department) {
          if(department.name in DepartmentNames) {
            return false;
          } else {
            DepartmentNames[department.name] = true;
            return true;
          }
        }
      );

      return this;
    }

    Institute.getDepartments = function (instituteName) {
      var d = $q.defer();
      var departments = [{"id" : 1, "name": "HBRC"}];
      d.resolve(departments);
      return d.promise;
    }

    return Institute;
  });

