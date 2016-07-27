(function() {
    'use strict';
    angular
        .module('gestioncompetencesApp')
        .factory('Cv', Cv);

    Cv.$inject = ['$resource', 'DateUtils'];

    function Cv ($resource, DateUtils) {
        var resourceUrl =  'api/cvs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateCv = DateUtils.convertDateTimeFromServer(data.dateCv);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
