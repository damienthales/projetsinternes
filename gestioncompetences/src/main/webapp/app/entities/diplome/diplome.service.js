(function() {
    'use strict';
    angular
        .module('gestioncompetencesApp')
        .factory('Diplome', Diplome);

    Diplome.$inject = ['$resource', 'DateUtils'];

    function Diplome ($resource, DateUtils) {
        var resourceUrl =  'api/diplomes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.diplomeDate = DateUtils.convertDateTimeFromServer(data.diplomeDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
